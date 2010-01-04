package edu.stanford.smi.protegex.owl.ui.metadata;

import java.awt.Dimension;

import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.util.ComponentUtilities;
import edu.stanford.smi.protege.widget.InstanceNameWidget;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.ui.refactoring.RenameAcrossFilesAction;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 * @deprecated is no longer needed because the name is now shown in the header
 */
public class RDFResourceNameWidget extends InstanceNameWidget {

    private RenameAcrossFilesAction renameAcrossFilesAction = new RenameAcrossFilesAction();

    private JToolBar toolBar;


    public Dimension getPreferredSize() {
        return new Dimension(100, ComponentUtilities.getStandardRowHeight() / 2);
    }


    public JToolBar getToolBar() {
        return toolBar;
    }


    public void initialize() {
        super.initialize();

        getTextField().getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                if (getTextField().getText().indexOf(' ') > 0) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            replaceSpaces();
                        }
                    });
                }
            }


            public void changedUpdate(DocumentEvent e) {
            }


            public void removeUpdate(DocumentEvent e) {
            }
        });
    }


    private void replaceSpaces() {
        String str = getTextField().getText();
        str = str.replace(' ', '_');
        int pos = getTextField().getCaretPosition();
        getTextField().setText(str);
        getTextField().setCaretPosition(pos);
    }


    public void setEditable(boolean b) {
        super.setEditable(b);
        // renameAcrossFilesAction.setEnabled(b);
    }


    public void setInstance(Instance instance) {
        super.setInstance(instance);
        renameAcrossFilesAction.initialize(this, (RDFResource) instance);
        renameAcrossFilesAction.setEnabled(renameAcrossFilesAction.isSuitable(this, (RDFResource) instance));
    }
}
