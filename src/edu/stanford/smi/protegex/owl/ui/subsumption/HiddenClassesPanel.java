package edu.stanford.smi.protegex.owl.ui.subsumption;

import edu.stanford.smi.protege.model.Model;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.ui.cls.HiddenClassTreeRoot;
import edu.stanford.smi.protegex.owl.ui.cls.Hierarchy;
import edu.stanford.smi.protegex.owl.ui.search.finder.Find;
import edu.stanford.smi.protegex.owl.ui.search.finder.HiddenClassFind;

/**
 * @author Nick Drummond, Medical Informatics Group, University of Manchester
 *         23-Feb-2006
 */
public class HiddenClassesPanel extends SubsumptionTreePanel {

    private OWLModel owlModel;

    public HiddenClassesPanel(OWLModel owlModel) {
        super(new HiddenClassTreeRoot(owlModel),
              owlModel.getSlot(Model.Slot.DIRECT_SUPERCLASSES),
              false,
              new HiddenClassFind(owlModel, Find.CONTAINS));
        this.owlModel = owlModel;
        getTree().setShowsRootHandles(true);
    }

    public Hierarchy createClone() {
        return new HiddenClassesPanel(owlModel);
    }

    public String getTitle() {
        return "Hidden Classes";
    }

    public void navigateToResource(RDFResource resource) {
        if (resource instanceof RDFSClass && !resource.isVisible()) {
            setSelectedClass((RDFSClass) resource);
        }
    }
}
