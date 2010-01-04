package edu.stanford.smi.protegex.owl.ui.search.finder;

import edu.stanford.smi.protege.model.Frame;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;

/**
 * @author Nick Drummond, Medical Informatics Group, University of Manchester
 *         20-Oct-2005
 */
public class DefaultClassFind extends ResultsViewModelFind {

    public DefaultClassFind(OWLModel owlModel, int type) {
        super(owlModel, type);
    }

    protected boolean isValidFrameToSearch(Frame f) {
        return f instanceof RDFSNamedClass && f.isVisible();
    }

    public String getDescription() {
        return "Find Named Class";
    }
}
