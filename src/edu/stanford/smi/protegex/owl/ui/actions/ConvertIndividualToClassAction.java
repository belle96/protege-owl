package edu.stanford.smi.protegex.owl.ui.actions;

import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLIndividual;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.widget.OWLUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ConvertIndividualToClassAction extends ResourceAction {

    public ConvertIndividualToClassAction() {
        super("Convert individual to class",
              OWLIcons.getCreateIcon(OWLIcons.PRIMITIVE_OWL_CLASS), null, true);
    }


    public void actionPerformed(ActionEvent e) {
        RDFIndividual instance = (RDFIndividual) getResource();
        String clsName = getClsName(instance);
        if (ProtegeUI.getModalDialogFactory().showConfirmDialog(getComponent(),
                                                                "This will create a new class " + clsName +
                                                                " as a subclass of " + instance.getProtegeType().getName() + "\n" +
                                                                "with restrictions that represent the values of the individual\n" +
                                                                "and then make this individual an instance of the new class.",
                                                                "Confirm conversion")) {
            OWLModel owlModel = instance.getOWLModel();
            try {
                owlModel.beginTransaction("Convert individual " + instance.getBrowserText() + " to class", clsName);
                performAction(instance);
                owlModel.commitTransaction();
            }
            catch (Exception ex) {
            	owlModel.rollbackTransaction();
            	OWLUI.handleError(owlModel, ex);                
            }
        }
    }


    private static void createRestrictionsForValues(OWLNamedClass cls, RDFProperty property, Collection values) {
        OWLModel owlModel = cls.getOWLModel();
        for (Iterator it = values.iterator(); it.hasNext();) {
            Object value = (Object) it.next();
            cls.addSuperclass(owlModel.createOWLHasValue(property, value));
        }
    }


    public static String getClsName(RDFIndividual individual) {
        String baseName = individual.getName() + "Class";
        String name = baseName;
        for (int i = 1; individual.getOWLModel().getRDFResource(name) != null; i++) {
            name = baseName + i;
        }
        return name;
    }


    public boolean isSuitable(Component component, RDFResource resource) {
        if (resource instanceof RDFIndividual &&
            resource.isEditable() &&
            !(resource instanceof SWRLIndividual) &&
            !(resource instanceof OWLOntology)) {
            return !resource.isAnonymous();
        }
        else {
            return false;
        }
    }


    public static OWLNamedClass performAction(RDFIndividual individual) {
        OWLModel owlModel = individual.getOWLModel();
        String name = getClsName(individual);
        OWLNamedClass cls = (OWLNamedClass) owlModel.createOWLNamedSubclass(name, (OWLNamedClass) individual.getProtegeType());
        for (Iterator it = individual.getRDFProperties().iterator(); it.hasNext();) {
            RDFProperty rdfProperty = (RDFProperty) it.next();
            if (!rdfProperty.equals(owlModel.getRDFTypeProperty())) {
                Collection values = individual.getPropertyValues(rdfProperty);
                if (!values.isEmpty()) {
                    if (rdfProperty.isAnnotationProperty()) {
                        cls.setPropertyValues(rdfProperty, values);
                    }
                    else {
                        createRestrictionsForValues(cls, rdfProperty, values);
                    }
                }
            }
        }
        individual.setProtegeType(cls);
        return cls;
    }
}
