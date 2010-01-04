package edu.stanford.smi.protegex.owl.model.impl;

import edu.stanford.smi.protegex.owl.model.Task;
import edu.stanford.smi.protegex.owl.model.TaskProgressDisplay;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 13, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class NoopProgressDisplay implements TaskProgressDisplay {

	public void run(Task task) throws Exception {
		// Just run the task in what ever thread it
		// was called from
		task.runTask();
	}


	public void setProgress(Task task,
	                        int progress) {
		// Do nothing
	}


	public void setProgressIndeterminate(Task task,
	                                     boolean b) {
		// Do nothing
	}


	public void setMessage(Task task,
	                       String message) {
		// Do nothing
	}


	public void end(Task task) {
		// Do nothing
	}
}

