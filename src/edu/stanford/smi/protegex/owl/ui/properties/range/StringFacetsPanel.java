package edu.stanford.smi.protegex.owl.ui.properties.range;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSDatatype;
import edu.stanford.smi.protegex.owl.model.RDFSDatatypeFactory;
import edu.stanford.smi.protegex.owl.ui.OWLLabeledComponent;
import edu.stanford.smi.protegex.owl.ui.widget.AssigningTextField;

import javax.swing.*;
import java.awt.*;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class StringFacetsPanel extends FacetsPanel {

    private JTextField patternField;

    private JTextField minLengthField;

    private JTextField maxLengthField;


    public StringFacetsPanel(OWLRangeWidget rangeWidget) {
        super(rangeWidget);

        minLengthField = new AssigningTextField(new AssigningTextField.Assign() {
            public void assign(String value) {
                assignNewType();
            }
        });
        minLengthField.setHorizontalAlignment(JTextField.RIGHT);

        maxLengthField = new AssigningTextField(new AssigningTextField.Assign() {
            public void assign(String value) {
                assignNewType();
            }
        });
        maxLengthField.setHorizontalAlignment(JTextField.RIGHT);
        Box lengthBox = Box.createHorizontalBox();
        lengthBox.add(new JLabel("  Min: "));
        lengthBox.add(minLengthField);
        lengthBox.add(new JLabel("   Max: "));
        lengthBox.add(maxLengthField);
        OWLLabeledComponent lengthComponent = new OWLLabeledComponent("String Length", lengthBox);
        add(BorderLayout.NORTH, lengthComponent);

        patternField = new AssigningTextField(new AssigningTextField.Assign() {
            public void assign(String value) {
                assignNewType();
            }
        });
        OWLLabeledComponent patternComponent = new OWLLabeledComponent("Regular Expression", patternField);
        add(BorderLayout.CENTER, patternComponent);
    }


    private void assignNewType() {
        int maxLength = getInt(maxLengthField);
        int minLength = getInt(minLengthField);
        String pattern = patternField.getText().trim();
        if (pattern.length() == 0) {
            pattern = null;
        }
        RDFResource range = rangeWidget.getEditedProperty().getRange();
        if (range instanceof RDFSDatatype) {
            RDFSDatatype datatype = (RDFSDatatype) range;
            RDFSDatatype baseDatatype = datatype.getBaseDatatype();
            RDFSDatatype target = datatype;
            if (baseDatatype != null) {
                target = baseDatatype;
            }
            if (pattern == null && minLength < 0 && maxLength < 0) {
                rangeWidget.setRange(target);
            }
            else {
                OWLModel owlModel = rangeWidget.getOWLModel();
                RDFSDatatypeFactory factory = owlModel.getRDFSDatatypeFactory();
                if (!target.isEditable()) {
                    target = factory.createAnonymousDatatype(target);
                }
                if (pattern != null) {
                    factory.setPattern(target, pattern);
                }
                if (maxLength >= 0) {
                    if (maxLength == minLength) {
                        factory.setLength(target, maxLength);
                    }
                    else {
                        factory.setMaxLength(target, maxLength);
                        if (minLength >= 0) {
                            factory.setMinLength(target, minLength);
                        }
                    }
                }
                else if (minLength >= 0) {
                    factory.setMinLength(target, minLength);
                }
                rangeWidget.setRange(target);
            }
        }
    }


    private int getInt(JTextField textField) {
        String text = textField.getText().trim();
        if (text.length() > 0) {
            try {
                return Integer.parseInt(text);
            }
            catch (Exception ex) {
            }
        }
        return -1;
    }


    public void setEditable(boolean value) {
        patternField.setEditable(value);
    }


    public void update(RDFSDatatype datatype) {
        String pattern = datatype.getPattern();
        patternField.setText(pattern == null ? "" : pattern);
        int length = datatype.getLength();
        if (length >= 0) {
            maxLengthField.setText("" + length);
            minLengthField.setText("" + length);
        }
        else {
            int minLength = datatype.getMinLength();
            if (minLength >= 0) {
                minLengthField.setText("" + minLength);
            }
            else {
                minLengthField.setText("");
            }
            int maxLength = datatype.getMaxLength();
            if (maxLength >= 0) {
                maxLengthField.setText("" + maxLength);
            }
            else {
                maxLengthField.setText("");
            }
        }
    }
}
