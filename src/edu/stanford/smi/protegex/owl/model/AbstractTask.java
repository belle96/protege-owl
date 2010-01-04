package edu.stanford.smi.protegex.owl.model;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 12, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public abstract class AbstractTask implements Task {

    private boolean cancelled;

    private String title;

    private boolean canBeCancelled;

    private TaskManager tm;

	private int progMin;

	private int progMax;


    public AbstractTask(String title, boolean canBeCancelled, TaskManager tm) {
        this(title, canBeCancelled, tm, 0, 100);
    }

	public AbstractTask(String title, boolean canBeCancelled, TaskManager tm, int progMin, int progMax) {
		this.title = title;
        this.canBeCancelled = canBeCancelled;
        this.tm = tm;
		this.progMin = progMin;
		this.progMax = progMax;
	}


    public boolean isPossibleToCancel() {
        return canBeCancelled;
    }


    public void cancelTask() {
        cancelled = true;
    }


    public String getTitle() {
        return title;
    }


    public int getProgressMin() {
        return progMin;
    }


    public int getProgressMax() {
        return progMax;
    }


    public boolean isCancelled() {
        return cancelled;
    }


    public void setProgress(int value) {
        tm.setProgress(this, value);
    }


    public void setProgressIndeterminate(boolean b) {
        tm.setIndeterminate(this, true);
    }


    public void setMessage(String message) {
        tm.setMessage(this, message);
    }


}

