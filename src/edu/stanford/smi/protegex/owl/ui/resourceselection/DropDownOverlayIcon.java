package edu.stanford.smi.protegex.owl.ui.resourceselection;

import javax.swing.*;
import java.awt.*;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DropDownOverlayIcon implements Icon {

    private Icon baseIcon;

    private JButton button;


    public DropDownOverlayIcon(Icon baseIcon, JButton button) {
        this.baseIcon = baseIcon;
        this.button = button;
    }


    public int getIconHeight() {
        return baseIcon.getIconHeight();
    }


    public int getIconWidth() {
        return baseIcon.getIconWidth();
    }


    public void paintIcon(Component c, Graphics g, int x, int y) {
        baseIcon.paintIcon(c, g, x, y);
        int bx = button.getWidth() - 19; //8;
        int by = button.getHeight() - 6;
        g.drawLine(bx, by, bx + 4, by);
        g.drawLine(bx + 1, by + 1, bx + 3, by + 1);
        g.drawLine(bx + 2, by + 2, bx + 2, by + 2);
    }
}
