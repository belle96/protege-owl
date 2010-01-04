package edu.stanford.smi.protegex.owl.inference.dig.reasoner;

import java.io.OutputStream;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Feb 15, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class DIGReasonerPreferences {
    private static DIGReasonerPreferences instance;

    private boolean treatErrorsAsWarnings;

    private OutputStream logOutputStream;


    private DIGReasonerPreferences() {
        treatErrorsAsWarnings = false;
    }


    public static DIGReasonerPreferences getInstance() {
        if (instance == null) {
            instance = new DIGReasonerPreferences();
        }
        return instance;
    }


    /**
     * Determines if DIG Errors should be regarded
     * as warnings.
     */
    public boolean isTreatErrorsAsWarnings() {
        return treatErrorsAsWarnings;
    }


    /**
     * Specifies whether DIG Errors should be regarded as
     * warnings
     *
     * @param b <code>true</code> if errors should be regarded
     *          as warnings, otherwise <code>false</code>.
     */
    public void setTreatErrorsAsWarnings(boolean b) {
        this.treatErrorsAsWarnings = b;
    }


    /**
     * Gets the output stream used for logging.
     */
    public OutputStream getLogOutputStream() {
        if (logOutputStream == null) {
            return System.out;
        }
        else {
            return logOutputStream;
        }
    }


    /**
     * Sets the output stream used for logging.
     */
    public void setLogOutputStream(OutputStream logOutputStream) {
        this.logOutputStream = logOutputStream;
    }
}

