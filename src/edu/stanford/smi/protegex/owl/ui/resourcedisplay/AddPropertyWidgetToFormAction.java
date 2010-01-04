package edu.stanford.smi.protegex.owl.ui.resourcedisplay;

import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protege.widget.FormWidget;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.resourceselection.ResourceSelectionAction;
import edu.stanford.smi.protegex.owl.ui.widget.OWLWidgetMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * An Action to add a slot widget for a currently invisible slot to the
 * FormWidget of the direct type of a given Instance.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class AddPropertyWidgetToFormAction extends ResourceSelectionAction {

    private RDFResource resource;

    private ResourceDisplay parent;


    public AddPropertyWidgetToFormAction(RDFResource resource, ResourceDisplay parent) {
        super("Add property widget to form...", OWLIcons.getImageIcon("AddPropertyWidgetToForm"));
        this.resource = resource;
        this.parent = parent;
    }


    public void resourceSelected(RDFResource resource) {
        RDFSClass directType = this.resource.getProtegeType();
        Project project = this.resource.getProject();
        FormWidget formWidget = (FormWidget) project.getDesignTimeClsWidget(directType);
        RDFProperty slot = (RDFProperty) resource;
        OWLWidgetMapper owm = (OWLWidgetMapper) project.getWidgetMapper();
        String className = owm.getDefaultWidgetClassName(directType, slot, null, true);
        formWidget.replaceWidget(slot, className);
        formWidget.setModified(true);
        formWidget.reload();
        parent.setInstance(null);
        parent.setInstance(this.resource);
    }


    public Collection getSelectableResources() {
        RDFSClass type = resource.getProtegeType();
        FormWidget formWidget = (FormWidget) resource.getProject().getDesignTimeClsWidget(type);
        Collection properties = new ArrayList(type.getUnionDomainProperties(true));
        for (Iterator it = type.getUnionDomainProperties(true).iterator(); it.hasNext();) {
            RDFProperty property = (RDFProperty) it.next();
            if (property.isSystem() || !property.isVisible() ||
                    property.isAnnotationProperty() ||
                    formWidget.getSlotWidget(property) != null) {
                properties.remove(property);
            }
        }
        return properties;
    }


    public RDFResource pickResource() {
        String label = "Select a property to add a widget for";
        OWLModel owlModel = resource.getOWLModel();
        return ProtegeUI.getSelectionDialogFactory().selectResourceFromCollection(parent, owlModel,
                getSelectableResources(), label);
    }
}
