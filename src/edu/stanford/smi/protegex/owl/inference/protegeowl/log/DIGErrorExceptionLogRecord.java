package edu.stanford.smi.protegex.owl.inference.protegeowl.log;

import edu.stanford.smi.protegex.owl.inference.dig.exception.DIGReasonerException;
import edu.stanford.smi.protegex.owl.inference.ui.icons.InferenceIcons;

import javax.swing.*;
import java.awt.*;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Aug 26, 2004<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class DIGErrorExceptionLogRecord extends ReasonerLogRecord {

    public DIGErrorExceptionLogRecord(DIGReasonerException ex, ReasonerLogRecord parent) {
        super(parent);

        label = new JLabel();

        label.setIcon(InferenceIcons.getErrorMessageIcon());

        label.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        String htmlMessage;

        htmlMessage = "DIG Reasoner Error: " + ex.getMessage().replaceAll("[\n]", "<br>");

        label.setText("<html><body color=\"ff0000\">" +
                htmlMessage + "</body></html>");
    }


    private JLabel label;


    public Component getListCellRendererComponent(JList list,
                                                  Object value,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus) {
        return label;
    }


    public Component getTreeCellRendererComponent(JTree tree,
                                                  Object value,
                                                  boolean selected,
                                                  boolean expanded,
                                                  boolean leaf,
                                                  int row,
                                                  boolean hasFocus) {
        return label;
    }
}

