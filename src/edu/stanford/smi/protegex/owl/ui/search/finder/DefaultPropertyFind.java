package edu.stanford.smi.protegex.owl.ui.search.finder;

import edu.stanford.smi.protege.model.Frame;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFProperty;

/**
 * @author Nick Drummond, Medical Informatics Group, University of Manchester
 *         20-Oct-2005
 */
public class DefaultPropertyFind extends ResultsViewModelFind {

    public DefaultPropertyFind(OWLModel owlModel, int type) {
        super(owlModel, type);
    }

    protected boolean isValidFrameToSearch(Frame f) {
        return f instanceof RDFProperty && f.isVisible();
    }

    public String getDescription() {
        return "Find Property";
    }
}
