package edu.stanford.smi.protegex.owl.ui.search.finder;

import javax.swing.*;
import javax.swing.text.JTextComponent;

/**
 * @author Nick Drummond, Medical Informatics Group, University of Manchester
 *         27-Jan-2006
 */
public interface FindAction extends Action {

    public void setTextBox(JTextComponent textBox);
}
