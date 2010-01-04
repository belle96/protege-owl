package edu.stanford.smi.protegex.owl.ui.widget;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * A JTextField that "assigns" its value when it loses its focus and when
 * the user hits enter.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class AssigningTextField extends JTextField {

    public AssigningTextField(final Assign assign) {
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    assign.assign(getText());
                }
            }
        });
        addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                assign.assign(getText());
            }
        });
    }


    public static interface Assign {

        void assign(String value);
    }
}
