package edu.stanford.smi.protegex.owl.ui.code;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * An object that can be used to drag and drop a Component in its parent.
 * It has to be attached to the component both as a MouseListener and a
 * MouseMotionListener.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ComponentDragger extends MouseAdapter implements MouseMotionListener {

    private int baseX;

    private int baseY;

    private Component component;


    public ComponentDragger(Component comp) {
        this.component = comp;
    }


    public void mouseDragged(MouseEvent e) {
        int dx = e.getX() - baseX;
        int dy = e.getY() - baseY;
        int newX = component.getX() + dx;
        int newY = component.getY() + dy;
        component.setLocation(newX, newY);
    }


    public void mouseMoved(MouseEvent e) {
        // Ignore
    }


    public void mousePressed(MouseEvent e) {
        baseX = e.getX();
        baseY = e.getY();
    }
}
