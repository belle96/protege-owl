package edu.stanford.smi.protegex.owl.ui.cls;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Frame;
import edu.stanford.smi.protege.resource.Icons;
import edu.stanford.smi.protege.util.CollectionUtilities;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.model.impl.OWLUtil;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.actions.ResourceAction;
import edu.stanford.smi.protegex.owl.ui.profiles.OWLProfiles;
import edu.stanford.smi.protegex.owl.ui.profiles.ProfilesManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Collection;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ChangeMetaclassAction extends ResourceAction {

    public static String GROUP = "Metaclasses/";


    public ChangeMetaclassAction() {
        super("Change metaclass...", Icons.getBlankIcon(), GROUP);
    }


    public void actionPerformed(ActionEvent e) {
        final OWLModel owlModel = getOWLModel();
        OWLNamedClass owlClassMetaCls = owlModel.getOWLNamedClassClass();
        RDFSNamedClass rdfsClassMetaCls = owlModel.getRDFSNamedClassClass();
        boolean rdfsMetaClsWasVisible = rdfsClassMetaCls.isVisible();
        boolean owlMetaClsWasVisible = owlClassMetaCls.isVisible();
        if (OWLUtil.hasRDFProfile(owlModel)) {
            rdfsClassMetaCls.setVisible(true);
            owlClassMetaCls.setVisible(true);
        }
        Cls rootMetaClass = rdfsClassMetaCls.isVisible() ? rdfsClassMetaCls :
                owlModel.getOWLNamedClassClass();
        Collection roots = CollectionUtilities.createCollection(rootMetaClass);
        RDFSNamedClass metaClass = ProtegeUI.getSelectionDialogFactory().selectClass(getComponent(), owlModel, roots, "Select Metaclass");
        rdfsClassMetaCls.setVisible(rdfsMetaClsWasVisible);
        owlClassMetaCls.setVisible(owlMetaClsWasVisible);
        Cls cls = (Cls) getResource();
        if (metaClass != null && !metaClass.equals(cls.getDirectType())) {
            cls.setDirectType(metaClass);
        }
    }


    public static RDFSNamedClass getRootMetaCls(Frame frame) {
        final OWLModel owlModel = (OWLModel) frame.getKnowledgeBase();
        RDFSNamedClass rdfsClassMetaCls = owlModel.getRDFSNamedClassClass();
        return rdfsClassMetaCls.isVisible() ? rdfsClassMetaCls :
                (RDFSNamedClass) owlModel.getOWLNamedClassClass();
    }


    public boolean isSuitable(Component component, RDFResource resource) {
        OWLModel owlModel = resource.getOWLModel();
        return resource.isEditable() &&
                component instanceof OWLSubclassPane &&
                (getRootMetaCls(resource).getSubclassCount() > 0 ||
                        (ProfilesManager.isFeatureSupported(owlModel, OWLProfiles.Create_OWLClass) &&
                                ProfilesManager.isFeatureSupported(owlModel, OWLProfiles.CreateRDFSClass)));
    }
}
