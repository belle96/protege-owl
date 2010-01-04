package edu.stanford.smi.protegex.owl.model;



/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 11, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface TaskManager {


    TaskProgressDisplay getProgressDisplay();


    void run(Task task) throws Exception;


    void setIndeterminate(Task task, final boolean b);


    void setMessage(Task task, final String message);


    void setProgress(Task task, final int progress);


    void setProgressDisplay(TaskProgressDisplay taskProgressDisplay);
}

