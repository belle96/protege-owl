package edu.stanford.smi.protegex.owl.ui.widget;

/**
 * An interface for UI components that can be toggled between asserted and
 * inferred modes.  This flag can then be controlled by a toggle on form level.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface InferredModeWidget {


    /**
     * Sets the inferred mode.
     *
     * @param value true to show the inferred mode, false for asserted mode
     */
    void setInferredMode(boolean value);
}
