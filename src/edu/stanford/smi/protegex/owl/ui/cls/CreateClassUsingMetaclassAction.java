package edu.stanford.smi.protegex.owl.ui.cls;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.resource.Icons;
import edu.stanford.smi.protege.util.CollectionUtilities;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.actions.ResourceAction;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateClassUsingMetaclassAction extends ResourceAction {


    public CreateClassUsingMetaclassAction() {
        super("Create subclass using metaclass...", Icons.getBlankIcon(), ChangeMetaclassAction.GROUP);
    }


    public void actionPerformed(ActionEvent e) {
        OWLSubclassPane pane = (OWLSubclassPane) getComponent();
        OWLModel owlModel = getOWLModel();
        RDFSClass parent = (RDFSClass) getResource();
        RDFSClass rootMetaCls = ChangeMetaclassAction.getRootMetaCls(parent);
        Collection roots = CollectionUtilities.createCollection(rootMetaCls);
        Cls metaCls = ProtegeUI.getSelectionDialogFactory().selectClass(pane, owlModel, roots, "Select Metaclass");
        if (metaCls != null) {
            Cls cls = ((KnowledgeBase) owlModel).createCls(null, Collections.singleton(parent), metaCls);
            pane.extendSelection(cls);
        }
    }


    public boolean isSuitable(Component component, RDFResource resource) {
        return resource.isEditable() &&
                component instanceof OWLSubclassPane &&
                ChangeMetaclassAction.getRootMetaCls(resource).getSubclassCount() > 0;
    }
}
