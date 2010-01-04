package edu.stanford.smi.protegex.owl.inference.protegeowl.task;

import edu.stanford.smi.protegex.owl.inference.reasoner.exception.ProtegeReasonerException;


/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Jul 22, 2004<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 * <p/>
 * Reasoner tasks are encapsulated to allow them to
 * be started in worker threads and monitored.  The
 * <code>ReasonerTask</code> interface defines the various
 * 'monitoring' methods that are available.
 */
public interface ReasonerTask {

    /**
     * Gets the size of the task.  When the progress
     * reaches this size, the task should be complete.
     */
    public int getTaskSize();


    /**
     * Gets the current progress of a task.
     */
    public int getProgress();


    /**
     * Determines whether the task can determine
     * its overall progress.  If the task cannot
     * determine its progress then the progress may
     * not be tracked - the progress is indeterminate.
     *
     * @return <code>true</code> if the progress cannot
     *         be determined.  <code>false</code> if the progress
     *         can be determined.  If a task is indeterminate the
     *         results for <code>getTaskSize</code> and <code>getProgress</code>
     *         are undefined.
     */
    public boolean isProgressIndeterminate();


    /**
     * Gets a high level description of the task.
     * This description should typically describe
     * the overall status of the task e.g. "Classifying
     * taxonomy"
     */
    public String getDescription();


    /**
     * Gets the current task message.  This will typically
     * change throughout the course of the task procedure.
     */
    public String getMessage();


    /**
     * Executes the task.
     *
     * @throws ProtegeReasonerException
     */
    public void run() throws ProtegeReasonerException;


    /**
     * Adds a <code>ReasonerTaskListener</code>, which is
     * notified when the state of the task changes, for example
     * when progress changes, or the task message changes.
     */
    public void addTaskListener(ReasonerTaskListener lsnr);


    /**
     * Removes a previously added task listener.
     */
    public void removeTaskListener(ReasonerTaskListener lsnr);


    public void setRequestAbort();


    public boolean isRequestAbort();
}
