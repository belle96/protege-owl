package edu.stanford.smi.protegex.owl.ui.cls;

import edu.stanford.smi.protege.resource.Icons;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.actions.ResourceAction;

import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateRDFListResourceAction extends ResourceAction {

    public CreateRDFListResourceAction() {
        super("Create restricted subclass of rdf:List...", Icons.getBlankIcon());
    }


    public void actionPerformed(ActionEvent e) {
        OWLModel owlModel = getOWLModel();
        RDFSClass typeClass = ProtegeUI.getSelectionDialogFactory().selectClass(getComponent(), owlModel,
                owlModel.getOWLThingClass(), "Select type of list elements");
        if (typeClass != null) {
            RDFSNamedClass listClass = owlModel.createOWLNamedClass(null);
            listClass.addSuperclass(owlModel.getRDFListClass());
            listClass.removeSuperclass(owlModel.getOWLThingClass());
            listClass.addSuperclass(owlModel.createOWLCardinality(owlModel.getRDFFirstProperty(), 1));
            listClass.addSuperclass(owlModel.createOWLAllValuesFrom(owlModel.getRDFFirstProperty(), typeClass));
            listClass.addSuperclass(owlModel.createOWLCardinality(owlModel.getRDFRestProperty(), 1));
            listClass.addSuperclass(owlModel.createOWLAllValuesFrom(owlModel.getRDFRestProperty(), listClass));
            owlModel.getRDFNil().addProtegeType(listClass);
            OWLClassesTab tab = OWLClassesTab.getOWLClassesTab(getComponent());
            if (tab != null) {
                tab.setSelectedCls(listClass);
            }
        }
    }


    public boolean isSuitable(Component component, RDFResource resource) {
        return resource.getName().equals(RDFNames.Cls.LIST);
    }
}
