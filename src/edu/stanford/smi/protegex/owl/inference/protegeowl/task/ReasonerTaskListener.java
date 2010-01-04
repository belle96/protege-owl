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
public interface ReasonerTaskListener {

    public void addedToTask(ReasonerTaskEvent event);


    public void progressChanged(ReasonerTaskEvent event);


    public void progressIndeterminateChanged(ReasonerTaskEvent event);


    public void descriptionChanged(ReasonerTaskEvent event);


    public void messageChanged(ReasonerTaskEvent event);


    public void taskFailed(ReasonerTaskEvent event);


    public void taskCompleted(ReasonerTaskEvent event);
}
