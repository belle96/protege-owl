package edu.stanford.smi.protegex.owl.ui.clsdesc.manchester;

import javax.swing.*;
import java.awt.*;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 7, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class SuggestionRenderer extends DefaultListCellRenderer {


    public Component getListCellRendererComponent(JList list,
                                                  Object value,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof Suggestion) {
            Suggestion suggestion = (Suggestion) value;
            label.setText(suggestion.getDescription());
            label.setIcon(suggestion.getIcon());
            label.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        }
        return label;
    }
}

