package edu.stanford.smi.protegex.owl.inference.protegeowl.task.digreasoner;

import edu.stanford.smi.protegex.owl.inference.dig.exception.DIGReasonerException;
import edu.stanford.smi.protegex.owl.inference.protegeowl.ProtegeOWLReasoner;
import edu.stanford.smi.protegex.owl.model.OWLClass;
import org.w3c.dom.Document;


/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Aug 17, 2004<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class GetAncestorConceptsTask extends AbstractSingleConceptWithConceptCollectionResultTask {


    public GetAncestorConceptsTask(OWLClass aClass,
                                   ProtegeOWLReasoner protegeOWLReasoner) {
        super("Computing ancestor classes",
                aClass,
                protegeOWLReasoner);

    }


    public void createQuery(Document doc) throws DIGReasonerException {
        getTranslator().createAncestorConceptsQuery(doc,
                "q0", getCls());
    }


}

