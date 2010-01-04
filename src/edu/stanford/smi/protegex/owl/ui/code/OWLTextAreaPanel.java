package edu.stanford.smi.protegex.owl.ui.code;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.dialogs.ModalDialogFactory;
import edu.stanford.smi.protegex.owl.ui.dialogs.ModalDialogFactory.CloseCallback;

import javax.swing.*;
import java.awt.*;

/**
 * A panel which can be used to edit an OWL expression in a multi-line dialog.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class OWLTextAreaPanel extends JPanel implements ModalDialogFactory.CloseCallback {

    private OWLModel owlModel;

    private OWLSymbolPanel symbolPanel;

    private OWLTextArea textArea;


    OWLTextAreaPanel(OWLModel anOWLModel, RDFSClass inputClass) {
        this.owlModel = anOWLModel;
        symbolPanel = new OWLSymbolPanel(anOWLModel, false);
        textArea = new OWLTextArea(anOWLModel, symbolPanel) {
            protected void checkExpression(String text) throws Throwable {
                owlModel.getOWLClassDisplay().getParser().checkClass(owlModel, text);
            }
        };
        textArea.setPreferredSize(new Dimension(600, 300));
        if (inputClass != null) {
            textArea.setText(inputClass);
        }
        symbolPanel.setSymbolEditor(textArea);
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, new JScrollPane(textArea));
        add(BorderLayout.SOUTH, symbolPanel);
    }


    public boolean canClose(int result) {
        if (result == ModalDialogFactory.OPTION_OK) {
            String text = textArea.getText();
            if (text.length() == 0) {
                return false;
            }
            else {
                try {
                    owlModel.getOWLClassDisplay().getParser().checkClass(owlModel, text);
                    return true;
                }
                catch (Throwable ex) {
                    symbolPanel.displayError(ex);
                    return false;
                }
            }
        }
        else {
            return true;
        }
    }

    /*RDFSClass getResultAsCls() {
       try {
           String uniCodeText = textArea.getText();
           String text = OWLTextFormatter.getParseableString(uniCodeText);
           OWLClassParser parser = owlModel.getOWLClassDisplay().getParser();
           return parser.parseClass(owlModel, text);
       }
       catch (Exception ex) {
           return null;
       }
   } */


    String getResultAsString() {
        return textArea.getText();
    }


    public static String showEditDialog(Component parent, OWLModel owlModel, RDFSClass input) {
        OWLTextAreaPanel panel = new OWLTextAreaPanel(owlModel, input);
        String title = "Edit OWL Expression";
        if (ProtegeUI.getModalDialogFactory().showDialog(parent, panel, title, ModalDialogFactory.MODE_OK_CANCEL, (CloseCallback)panel) == ModalDialogFactory.OPTION_OK) {
            return panel.getResultAsString();
        }
        else {
            return null;
        }
    }
}
