package edu.stanford.smi.protegex.owl.ui.components.singleliteral;

import edu.stanford.smi.protegex.owl.model.RDFProperty;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class SingleLiteralComponent extends AbstractSingleLiteralComponent {


    public SingleLiteralComponent(RDFProperty predicate) {
        this(predicate, null);
    }

    public SingleLiteralComponent(RDFProperty predicate, String label) {
    	this(predicate, label, false);
	}
    
    public SingleLiteralComponent(RDFProperty predicate, String label, boolean isReadOnly) {
    	super(predicate, label, isReadOnly);
	}
    
    protected JTextComponent createTextComponent() {
        return new JTextField();
    }


    protected Component createTextComponentHolder(JTextComponent textComponent) {
        return textComponent;
    }


    protected void updateTextFieldAlignment(final boolean numericDatatype) {
        JTextField textField = (JTextField) getTextComponent();
        textField.setHorizontalAlignment(numericDatatype ?
                JTextField.RIGHT : JTextField.LEFT);
    }
}
