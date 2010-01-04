package edu.stanford.smi.protegex.owl.ui.clsproperties;

import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.resourceselection.ResourceSelectionAction;
import edu.stanford.smi.protegex.owl.ui.widget.OWLUI;
import edu.stanford.smi.protegex.owl.ui.widget.PropertyWidget;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * An Action to add a selected property to the domain of the currently
 * edited Cls.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class AddPropertyAction extends ResourceSelectionAction {

    private PropertyWidget propertyWidget;


    public AddPropertyAction(PropertyWidget propertyWidget) {
        super("Add this class to the domain of properties...",
                OWLIcons.getAddIcon(OWLIcons.RDF_PROPERTY), true);
        this.propertyWidget = propertyWidget;
    }


    public void resourceSelected(RDFResource resource) {
        RDFSClass cls = (RDFSClass) propertyWidget.getEditedResource();
        final RDFProperty property = (RDFProperty) resource;
        RDFSClass rootCls = cls.getOWLModel().getOWLThingClass();
        OWLModel owlModel = resource.getOWLModel();
        try {
            owlModel.beginTransaction("Add " + cls.getBrowserText() + " to the domain of " + property.getBrowserText(), property.getName());
            if (property.getUnionDomain(false).contains(rootCls)) {
                property.removeUnionDomainClass(rootCls);
            }
            property.addUnionDomainClass(cls);
            owlModel.commitTransaction();
        }
        catch (Exception ex) {
        	owlModel.rollbackTransaction();
            OWLUI.handleError(owlModel, ex);
        }
    }


    protected RDFSNamedClass getBasePropertyMetaclass() {
        OWLModel owlModel = (OWLModel) propertyWidget.getKnowledgeBase();
        return owlModel.getRDFPropertyClass();
    }


    public Collection getSelectableResources() {
        RDFSClass propertyMetaclass = getBasePropertyMetaclass();
        List properties = new ArrayList(propertyMetaclass.getInstances(true));
        RDFSClass cls = (RDFSClass) propertyWidget.getEditedResource();
        properties.removeAll(cls.getUnionDomainProperties());
        Collection choice = new ArrayList();
        for (Iterator it = properties.iterator(); it.hasNext();) {
            RDFProperty property = (RDFProperty) it.next();
            if (!(property instanceof OWLProperty) || !property.isAnnotationProperty()) {
                if (!property.isSystem()) {
                    choice.add(property);
                }
            }
        }
        return choice;
    }


    public Collection pickResources() {
        String label = "Select " + propertyWidget.getLabel();
        OWLModel owlModel = (OWLModel) propertyWidget.getKnowledgeBase();
        return ProtegeUI.getSelectionDialogFactory().selectResourcesFromCollection((Component) propertyWidget,
                owlModel, getSelectableResources(), label);
    }
}
