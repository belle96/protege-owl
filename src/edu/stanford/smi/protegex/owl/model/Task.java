package edu.stanford.smi.protegex.owl.model;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 11, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 * <p/>
 * Represents a task that could potentially take
 * a significant amount of time to complete and
 * should therefore have some kind of progress
 * display if this is the case.
 */
public interface Task {

    /**
     * Requests that the task be cancelled. This
     * method will only be called if the task
     * can be cancelled.
     */
    public void cancelTask();


    /**
     * Gets the title for this task.
     */
    public String getTitle();


    /**
     * Gets the minimum progress value for
     * this task.
     */
    public int getProgressMin();


    /**
     * Gets the maximum progress value for
     * this task.
     */
    public int getProgressMax();


    /**
     * Checks whether this Task has been cancelled.
     * Unless either method is overloaded, this will return true after cancelTask
     * has been called (e.g., via the cancel button).
     *
     * @return true  if this has been cancelled
     */
    public boolean isCancelled();


    /**
     * Determines if the task can be cancelled
     *
     * @return <code>true</code> if the task can
     *         be cancelled, or <code>false</code> if the
     *         task cannot be cancelled.
     */
    public boolean isPossibleToCancel();


    public void runTask() throws Exception;
}
