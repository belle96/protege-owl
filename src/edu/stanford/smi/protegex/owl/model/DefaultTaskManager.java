package edu.stanford.smi.protegex.owl.model;

/**
 * The default implementation of TaskManager.
 * <p/>
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 11, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class DefaultTaskManager implements TaskManager {

	private TaskProgressDisplay taskProgressDisplay;


	public DefaultTaskManager() {
	}


	public void setProgressDisplay(TaskProgressDisplay taskProgressDisplay) {
		this.taskProgressDisplay = taskProgressDisplay;
	}


	public TaskProgressDisplay getProgressDisplay() {
		return taskProgressDisplay;
	}


	public void run(Task task)
	        throws Exception {
		// Delegate to the task progress display
		taskProgressDisplay.run(task);
	}


	public void setProgress(final Task task,
	                        final int progress) {
		taskProgressDisplay.setProgress(task, progress);
	}


	public void setIndeterminate(final Task task,
	                             final boolean b) {
		taskProgressDisplay.setProgressIndeterminate(task, b);
	}


	public void setMessage(final Task task,
	                       final String message) {
		taskProgressDisplay.setMessage(task, message);
	}
}

