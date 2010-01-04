package edu.stanford.smi.protegex.owl.ui.resourcedisplay;

import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.widget.FormWidget;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.resourceselection.ResourceSelectionAction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * An Action to remove a property widget of a currently visible slot from the
 * FormWidget of the direct type of a given resource.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class RemovePropertyWidgetFromFormAction extends ResourceSelectionAction {

    private RDFResource resource;

    private ResourceDisplay parent;


    public RemovePropertyWidgetFromFormAction(RDFResource resource, ResourceDisplay parent) {
        super("Remove property widget from form...", OWLIcons.getImageIcon("RemovePropertyWidgetFromForm"));
        this.resource = resource;
        this.parent = parent;
    }


    public void resourceSelected(RDFResource resource) {
        RDFSClass directType = this.resource.getProtegeType();
        Project project = this.resource.getProject();
        FormWidget formWidget = (FormWidget) project.getDesignTimeClsWidget(directType);
        Slot slot = (Slot) resource;
        formWidget.replaceWidget(slot, null);
        parent.setInstance(null);
        parent.setInstance(this.resource);
    }


    public Collection getSelectableResources() {
        RDFSClass directType = resource.getProtegeType();
        FormWidget formWidget = (FormWidget) resource.getProject().getDesignTimeClsWidget(directType);
        Collection properties = new ArrayList(directType.getUnionDomainProperties(true));
        for (Iterator it = directType.getUnionDomainProperties(true).iterator(); it.hasNext();) {
            RDFProperty property = (RDFProperty) it.next();
            if (property.isSystem() ||
                    !(property instanceof OWLProperty) ||
                    ((OWLProperty) property).isAnnotationProperty() ||
                    formWidget.getSlotWidget(property) == null) {
                properties.remove(property);
            }
        }
        return properties;
    }


    public RDFResource pickResource() {
        String label = "Select a property to remove the widget for";
        OWLModel owlModel = resource.getOWLModel();
        return ProtegeUI.getSelectionDialogFactory().selectProperty(parent, owlModel,
                getSelectableResources(), label);
    }
}
