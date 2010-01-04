package edu.stanford.smi.protegex.owl.inference.protegeowl.task.digreasoner;

import java.util.Collection;

import edu.stanford.smi.protegex.owl.inference.protegeowl.task.ReasonerTask;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Aug 17, 2004<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 * <p/>
 * Implemented by tasks that return a <code>Collection</code>
 * as a result.
 */
public interface CollectionResultReasonerTask extends ReasonerTask {

    public Collection getResult();
}
