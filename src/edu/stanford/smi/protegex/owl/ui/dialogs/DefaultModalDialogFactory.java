package edu.stanford.smi.protegex.owl.ui.dialogs;

import edu.stanford.smi.protege.Application;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;

import javax.swing.*;
import java.awt.*;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultModalDialogFactory extends AbstractModalDialogFactory {

    public void attemptDialogClose(int result) {
        ModalDialog.attemptDialogClose(result);
    }


    private Component getParentComponent(OWLModel owlModel) {
        if (owlModel == null) {
            return Application.getMainWindow();
        }
        else {
            Component comp = ProtegeUI.getProjectView(owlModel.getProject());
            if (comp == null) {
                comp = Application.getMainWindow();
            }
            return comp;
        }
    }


    public int showConfirmCancelDialog(OWLModel owlModel, String message, String title) {
        Component parentComponent = getParentComponent(owlModel);
        return showConfirmCancelDialog(parentComponent, message, title);
    }


    public int showConfirmCancelDialog(Component parent, String message, String title) {
        int result = JOptionPane.showConfirmDialog(parent, message, title, JOptionPane.YES_NO_CANCEL_OPTION);
        if(result == JOptionPane.NO_OPTION) {
            return ModalDialogFactory.OPTION_NO;
        }
        else if(result == JOptionPane.YES_OPTION) {
            return ModalDialogFactory.OPTION_YES;
        }
        else {
            return ModalDialogFactory.OPTION_CANCEL;
        }
    }


    public boolean showConfirmDialog(OWLModel owlModel, String message, String title) {
        Component parentComponent = getParentComponent(owlModel);
        return showConfirmDialog(parentComponent, message, title);
    }


    public boolean showConfirmDialog(Component parent, String message, String title) {
        return JOptionPane.showConfirmDialog(parent, message, title, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }


    public int showDialog(Component parent, Component panel, String title, int mode, CloseCallback callback, 
    		boolean enableCloseButton, Component componentToFocus) {
        return ModalDialog.showDialog(parent, panel, title, mode, callback, enableCloseButton, componentToFocus);
    }


    public void showErrorMessageDialog(OWLModel owlModel, String message, String title) {
        Component parentComponent = getParentComponent(owlModel);
        showErrorMessageDialog(parentComponent, message, title);
    }


    public void showErrorMessageDialog(Component parent, String message, String title) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
    }


    public String showInputDialog(OWLModel owlModel, String message, String initialValue) {
        Component parentComponent = getParentComponent(owlModel);
        return showInputDialog(parentComponent, message, initialValue);
    }


    public String showInputDialog(Component parent, String message, String initialValue) {
        if(initialValue == null) {
            initialValue = "";
        }
        return JOptionPane.showInputDialog(parent, message, initialValue);
    }


    public void showMessageDialog(OWLModel owlModel, String message, String title) {
        Component parentComponent = getParentComponent(owlModel);
        showMessageDialog(parentComponent, message, title);
    }


    public void showMessageDialog(Component parent, String message, String title) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
