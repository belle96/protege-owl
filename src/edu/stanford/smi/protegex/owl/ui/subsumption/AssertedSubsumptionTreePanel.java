package edu.stanford.smi.protegex.owl.ui.subsumption;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Model;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.ui.cls.Hierarchy;

/**
 * A SubsumptionTreePanel displaying the asserted superclass relationships.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class AssertedSubsumptionTreePanel extends SubsumptionTreePanel {

    private Cls root;

    public final static String TITLE = "Asserted Hierarchy";


    public AssertedSubsumptionTreePanel(OWLModel owlModel) {
        this(owlModel.getOWLThingClass());
    }


    public AssertedSubsumptionTreePanel(Cls root) {
        super(root,
                root.getKnowledgeBase().getSlot(Model.Slot.DIRECT_SUBCLASSES),
                root.getKnowledgeBase().getSlot(Model.Slot.DIRECT_SUPERCLASSES), false);
        this.root = root;
    }


    public Hierarchy createClone() {
        return new AssertedSubsumptionTreePanel(root);
    }


    public String getTitle() {
        return TITLE;
    }


    public void navigateToResource(RDFResource resource) {
        if (resource instanceof RDFSClass) {
            setSelectedClass((RDFSClass) resource);
        }
    }
}
