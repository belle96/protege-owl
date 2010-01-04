package edu.stanford.smi.protegex.owl.ui.properties.range;

import edu.stanford.smi.protegex.owl.model.RDFSDatatype;

import javax.swing.*;
import java.awt.*;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public abstract class FacetsPanel extends JPanel {

    protected OWLRangeWidget rangeWidget;


    public FacetsPanel(OWLRangeWidget rangeWidget) {
        this.rangeWidget = rangeWidget;
        setLayout(new BorderLayout());
    }


    public abstract void setEditable(boolean value);


    public abstract void update(RDFSDatatype datatype);
}
