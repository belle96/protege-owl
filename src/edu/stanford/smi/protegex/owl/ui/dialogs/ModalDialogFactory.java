package edu.stanford.smi.protegex.owl.ui.dialogs;

import edu.stanford.smi.protegex.owl.model.OWLModel;

import java.awt.*;

/**
 * The abstraction of objects that can create various dialogs
 * (comparable to JOptionPane etc).
 * <p/>
 * A static instance of this class can be acquired from
 * <CODE>ProtegeUI.getModalDialogFactory()</CODE>/
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface ModalDialogFactory {

    public static final int OPTION_OK = 1;

    public static final int OPTION_YES = 2;

    public static final int OPTION_NO = 3;

    public static final int OPTION_CANCEL = 4;

    public static final int OPTION_CLOSE = 5;

    public static final int RESULT_ERROR = 6;

    public static final int MODE_OK_CANCEL = 11;

    public static final int MODE_YES_NO_CANCEL = 12;

    public static final int MODE_YES_NO = 13;

    public static final int MODE_CLOSE = 14;


    public static interface CloseCallback {

        boolean canClose(int result);
    }


    void attemptDialogClose(int result);


    int showDialog(Component parent, Component panel, String title, int mode);


    int showDialog(Component parent, Component panel, String title, int mode, CloseCallback callback);


    int showDialog(Component parent, Component panel,
                          String title, int mode, CloseCallback callback,
                          boolean enableCloseButton);


    int showDialog(Component parent, Component panel,
            String title, int mode, CloseCallback callback,
            boolean enableCloseButton, Component componentToFocus);
    
    
    int showDialog(Component parent, Component panel, String title, int mode, Component componentToFocus);
    
    int showConfirmCancelDialog(OWLModel owlModel, String message, String title);


    /**
     * Shows a dialog with Yes, No, and Cancel options.
     * @param parent
     * @param message
     * @param title
     * @return OPTION_YES, OPTION_NO or OPTION_CANCEL
     */
    int showConfirmCancelDialog(Component parent, String message, String title);


    boolean showConfirmDialog(OWLModel owlModel, String message, String title);


    boolean showConfirmDialog(Component parent, String message, String title);


    void showErrorMessageDialog(OWLModel owlModel, String message);


    void showErrorMessageDialog(OWLModel owlModel, String message, String title);


    void showErrorMessageDialog(Component parent, String message);


    void showErrorMessageDialog(Component parent, String message, String title);


    String showInputDialog(OWLModel owlModel, String message, String initialValue);


    String showInputDialog(Component parent, String message, String initialValue);


    void showMessageDialog(OWLModel owlModel, String message);


    void showMessageDialog(OWLModel owlModel, String message, String title);


    void showMessageDialog(Component parent, String message);


    void showMessageDialog(Component parent, String message, String title);


    void showThrowable(OWLModel owlModel, Throwable t);
}
