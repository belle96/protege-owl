package edu.stanford.smi.protegex.owl.ui.existential;

import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Model;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.cls.HierarchyManager;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.resourceselection.ResourceSelectionAction;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ExistentialAction extends ResourceSelectionAction {

    private OWLNamedClass cls;

    private Component component;

    private HierarchyManager hierarchyManager;


    public ExistentialAction(Component component, HierarchyManager hierarchyManager, OWLNamedClass cls) {
        super("Explore existential relationships...", OWLIcons.getImageIcon("Transitivity"));
        this.cls = cls;
        this.component = component;
        this.hierarchyManager = hierarchyManager;
    }


    public void resourceSelected(RDFResource resource) {
        if (resource instanceof OWLObjectProperty) {
            OWLObjectProperty property = (OWLObjectProperty) resource;
            ExistentialTreePanel tp = new ExistentialTreePanel(cls,
                    ((KnowledgeBase) cls.getOWLModel()).getSlot(Model.Slot.DIRECT_SUPERCLASSES),
                    property);
            hierarchyManager.addHierarchy(tp);
        }
    }


    public Collection getSelectableResources() {
        OWLModel owlModel = cls.getOWLModel();
        Collection properties = new ArrayList(owlModel.getVisibleUserDefinedOWLProperties());
        List list = new ArrayList();
        for (Iterator it = properties.iterator(); it.hasNext();) {
            OWLProperty property = (OWLProperty) it.next();
            if (property instanceof OWLObjectProperty) {
                list.add(property);
            }
        }
        return list;
    }


    public RDFResource pickResource() {
        Collection list = getSelectableResources();
        return (OWLObjectProperty) ProtegeUI.getSelectionDialogFactory().selectResourceFromCollection(
                component, cls.getOWLModel(), list, "Select a property to explore");
    }


    public void setCls(OWLNamedClass cls) {
        this.cls = cls;
    }
}
