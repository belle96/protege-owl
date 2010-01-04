package edu.stanford.smi.protegex.owl.ui.widget;

import com.hexidec.ekit.EkitCore;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFSLiteral;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.components.ComponentUtil;
import edu.stanford.smi.protegex.owl.ui.dialogs.ModalDialogFactory;

import javax.swing.*;
import java.awt.*;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class HTMLEditorPanel extends JPanel {

    private EkitCore ekitCore;

    private JComboBox languageComboBox;


    public HTMLEditorPanel(OWLModel owlModel, RDFSLiteral literal) {
        this(owlModel, literal.getString(), literal.getLanguage());
    }


    public HTMLEditorPanel(String text) {
        this(null, text, null);
    }


    public HTMLEditorPanel(OWLModel owlModel, String text, String language) {
        setLayout(new BorderLayout());
        ekitCore = new EkitCore(null, null, null, null, false, true, true, null, null, false, false);
        ekitCore.setDocumentText(text);
        JPanel ekitTopPanel = new JPanel();
        ekitTopPanel.setLayout(new GridLayout(2, 1));
        ekitTopPanel.add(ekitCore.getMenuBar());
        ekitTopPanel.add(ekitCore.getToolBar(true));
        if (owlModel != null) {
            languageComboBox = ComponentUtil.createLanguageComboBox(owlModel, language);
            Box languagePanel = Box.createHorizontalBox();
            languagePanel.add(new JLabel("Language: "));
            languagePanel.add(languageComboBox);
            add(BorderLayout.SOUTH, languagePanel);
        }
        add(BorderLayout.NORTH, ekitTopPanel);
        add(BorderLayout.CENTER, ekitCore);
    }


    public String getFullText() {
        return ekitCore.getDocumentText().trim();
    }


    public RDFSLiteral getRDFSLiteral(OWLModel owlModel) {
        String text = getText();
        if (text != null) {
            String lang = null;
            Object language = languageComboBox.getSelectedItem();
            if (language instanceof String && ((String) language).trim().length() > 0) {
                lang = ((String) language).trim();
            }
            return owlModel.createRDFSLiteral(text, lang);
        }
        else {
            return null;
        }
    }


    public String getText() {
        return ekitCore.getDocumentBody().trim();
    }


    public static String show(Component parent, String text, String title) {
        return show(parent, text, title, false);
    }


    public static String show(Component parent, String text, String title, boolean fullText) {
        HTMLEditorPanel panel = new HTMLEditorPanel(text);
        if (ProtegeUI.getModalDialogFactory().showDialog(parent, panel, title, ModalDialogFactory.MODE_OK_CANCEL) ==
                ModalDialogFactory.OPTION_OK) {
            return fullText ? panel.getFullText() : panel.getText();
        }
        else {
            return null;
        }
    }


    public static RDFSLiteral show(Component parent, RDFSLiteral literal, String title, OWLModel owlModel) {
        HTMLEditorPanel panel = new HTMLEditorPanel(owlModel, literal);
        if (ProtegeUI.getModalDialogFactory().showDialog(parent, panel, title, ModalDialogFactory.MODE_OK_CANCEL) ==
                ModalDialogFactory.OPTION_OK) {
            return panel.getRDFSLiteral(owlModel);
        }
        else {
            return null;
        }
    }
}
