package edu.stanford.smi.protegex.owl.ui.search.finder;

import edu.stanford.smi.protege.model.Frame;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFIndividual;
import edu.stanford.smi.protegex.owl.model.RDFResource;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author Nick Drummond, Medical Informatics Group, University of Manchester
 *         20-Oct-2005
 */
public class DefaultIndividualFind extends ResultsViewModelFind {

    public DefaultIndividualFind(OWLModel owlModel, int type) {
        super(owlModel, type);
    }

    protected boolean isValidFrameToSearch(Frame f) {
        if (f instanceof RDFIndividual) {
            Collection types = ((RDFIndividual) f).getRDFTypes();
            for (Iterator i = types.iterator(); i.hasNext();) {
                if (((RDFResource) i.next()).isVisible()) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getDescription() {
        return "Find Individual Of Any Class";
    }
}
