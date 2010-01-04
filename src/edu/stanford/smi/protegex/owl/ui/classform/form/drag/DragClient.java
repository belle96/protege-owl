package edu.stanford.smi.protegex.owl.ui.classform.form.drag;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Oct 9, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public interface DragClient {

    public Collection getComponents();


    public JComponent getDraggableComponent(Point pt);


    public boolean isDroppable(JComponent component, Point pt);


    public Rectangle getDropRectangle(JComponent component, Point pt);
}
