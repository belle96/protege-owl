package edu.stanford.smi.protegex.owl.ui.code;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface SymbolErrorDisplay {

    void displayError(Throwable throwable);


    void displayError(String message);


    void setErrorFlag(boolean error);

}
