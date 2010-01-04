package edu.stanford.smi.protegex.owl.model;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 11, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public interface TaskProgressDisplay {

    /**
     * Runs the task and starts the display of progress to the user.
     * @param task The <code>Task</code> that will be run and
     * whose progress will be monitored and displayed.
     */
    public void run(Task task) throws Exception;


    /**
     * Updates the value of the progress that is
     * displayed to the user.
     *
     * @param task The task which the progress relates to
     * @param progress The progress that will
     *                 be between the min and max progress for
     *                 the <code>Task</code>
     */
    public void setProgress(Task task, int progress);


    /**
     * Sets the progress display to indicate that the
     * progress cannot be determined, but the task is
     * proceding as normal.
     *
     * @param b <code>true</code> if the progress is
     *          indeterminate, or <code>false</code> if the progress
     *          is not indeterminate.
     */
    public void setProgressIndeterminate(Task task, boolean b);


    /**
     * Sets the message that will be displayed to the user.
     */
    public void setMessage(Task task, String message);


    /**
     * Stops (hides) the progress display. This methods is
     * generally called when the task is complete.
     */
    public void end(Task task);
}
