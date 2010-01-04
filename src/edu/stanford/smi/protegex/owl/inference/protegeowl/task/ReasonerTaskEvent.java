package edu.stanford.smi.protegex.owl.inference.protegeowl.task;


/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Aug 13, 2004<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class ReasonerTaskEvent {

    private ReasonerTask task;


    public ReasonerTaskEvent(ReasonerTask task) {
        this.task = task;
    }


    public ReasonerTask getSource() {
        return task;
    }
}

