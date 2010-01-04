package edu.stanford.smi.protegex.owl.jena.creator;

/**
 * An interface for objects that can display the progress of the JenaCreator.
 * The usual contract is start()  (setProgressText() | setProgressValue())*  stop()
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface ProgressDisplay {

    void setProgressText(String str);


    /**
     * @param value 0..1
     */
    void setProgressValue(double value);


    void start();


    void stop();
}
