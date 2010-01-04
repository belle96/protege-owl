package edu.stanford.smi.protegex.owl.inference.protegeowl.task.protegereasoner;

import java.util.Collection;

import edu.stanford.smi.protegex.owl.inference.reasoner.ProtegeReasoner;
import edu.stanford.smi.protegex.owl.inference.reasoner.exception.ProtegeReasonerException;
import edu.stanford.smi.protegex.owl.model.OWLClass;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Aug 17, 2004<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class GetSubConceptsTask extends AbstractSingleConceptWithConceptCollectionResultTask {

    public GetSubConceptsTask(OWLClass aClass,
                              ProtegeReasoner protegeReasoner) {
        super("Computing subclasses", aClass, protegeReasoner);
    }


    @Override
    public Collection getQueryResults() throws ProtegeReasonerException {
    	return getProtegeReasoner().getSubclasses(getCls());
    }
}

