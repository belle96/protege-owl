package edu.stanford.smi.protegex.owl.ui.dialogs;

import java.awt.Component;
import java.util.logging.Level;

import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.ui.dialogs.ModalDialogFactory.CloseCallback;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public abstract class AbstractModalDialogFactory implements ModalDialogFactory {

    private static final String ERROR = "Error";

    private static final String INFORMATION = "Information";


    public int showDialog(Component parent, Component panel, String title, int mode) {
        return showDialog(parent, panel, title, mode, (CloseCallback)null);
    }

    public int showDialog(Component parent, Component panel, String title, int mode, CloseCallback callback) {
        return showDialog(parent, panel, title, mode, callback, true);
    }

    public int showDialog(Component parent, Component panel,  String title, int mode, CloseCallback callback, boolean enableCloseButton) {
    	return showDialog(parent, panel, title, mode, callback, enableCloseButton, null);
    }
    
    public int showDialog(Component parent, Component panel, String title, int mode, Component componentToFocus) {
    	return showDialog(parent, panel, title, mode, null, true, componentToFocus);
    }
    
    public void showErrorMessageDialog(OWLModel owlModel, String message) {
        showErrorMessageDialog(owlModel, message, ERROR);
    }


    public void showErrorMessageDialog(Component parent, String message) {
        showErrorMessageDialog(parent, message, ERROR);
    }


    public void showMessageDialog(OWLModel owlModel, String message) {
        showMessageDialog(owlModel, message, INFORMATION);
    }


    public void showMessageDialog(Component parent, String message) {
        showMessageDialog(parent, message, INFORMATION);
    }


    public void showThrowable(OWLModel owlModel, Throwable t) {
        Log.getLogger().log(Level.SEVERE, "Exception caught", t);
        showErrorMessageDialog(owlModel, "Unexpected Error - please see console for stack trace.\n" + t.getMessage(), "Unexpected Error");
    }
}
