package edu.stanford.smi.protegex.owl.ui.conditions;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * A DefaultTableCellRenderer (derived from JLabel) used to display separators
 * in the ConditionsTable.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class SeparatorCellRenderer extends DefaultTableCellRenderer {

    private boolean grayed;


    public SeparatorCellRenderer(boolean grayed) {
        this.grayed = grayed;
        setHorizontalAlignment(JLabel.RIGHT);
        setVerticalAlignment(JLabel.BOTTOM);
        setVerticalTextPosition(JLabel.BOTTOM);
    }


    public void paint(Graphics g) {
        setForeground(grayed ? Color.gray : Color.black);
        setFont(getFont().deriveFont(Font.PLAIN, 9.0f));
        super.paint(g);
        int y = getHeight() / 2;
        g.setColor(grayed ? Color.lightGray : Color.black);
        int strWidth = getFontMetrics(getFont()).stringWidth(getText());
        g.drawLine(4, y, getWidth() - 6 - strWidth, y);
    }
}
