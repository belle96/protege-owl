package edu.stanford.smi.protegex.owl.ui.classform.component.property;

import edu.stanford.smi.protege.util.ComponentUtilities;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.event.PropertyValueAdapter;
import edu.stanford.smi.protegex.owl.model.event.PropertyValueListener;
import edu.stanford.smi.protegex.owl.ui.OWLLabeledComponent;
import edu.stanford.smi.protegex.owl.ui.classform.component.FormComponent;

import javax.swing.*;
import java.awt.*;

/**
 * A FormComponent displaying the fillers of quantifier restrictions.
 *
 * @author Matthew Horridge  <matthew.horridge@cs.man.ac.uk>
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class PropertyFormComponent extends FormComponent {

    private AddIndividualAction addIndividualAction;

    private AddNamedClassAction addNamedClassAction;

    private ChangeClosureAxiomButton closureAxiomAction;

    private DeleteRowAction deleteRowAction;

    private OWLLabeledComponent lc;

    private RDFProperty property;

    private PropertyFormTable table;

    private PropertyValueListener valueListener = new PropertyValueAdapter() {
        public void browserTextChanged(RDFResource resource) {
            updatePropertyBrowserText();
        }
    };

    /**
     * The number of rows that shall be made visible by default without scrolling
     */
    private static final int MAX_ROWS = 8;


    public PropertyFormComponent(RDFProperty property) {
        this.property = property;
        property.addPropertyValueListener(valueListener);
    }


    public void dispose() {
        property.removePropertyValueListener(valueListener);
    }


    public Dimension getPreferredSize() {
        if (lc != null && table != null) {
            Dimension superSize = super.getPreferredSize();
            Dimension lcSize = lc.getCenterComponent().getPreferredSize();
            int width = superSize.width - lcSize.width;
            int rowCount = Math.min(Math.max(1, table.getTableModel().getRowCount()), MAX_ROWS);
            int rowHeight = table.getRowHeight();
            int height = superSize.height - lcSize.height + rowCount * rowHeight + 3;
            return new Dimension(width, height);
        }
        else {
            return new Dimension(0, 0);
        }
    }


    public void init(OWLModel owlModel) {
    }


    public void setNamedClass(OWLNamedClass cls) {
        if (table != null) {
            ComponentUtilities.dispose(table);
        }
        PropertyFormTableModel tableModel = new PropertyFormTableModel(cls, property);
        table = new PropertyFormTable(tableModel, cls, property);
        JScrollPane sp = new JScrollPane(table);
        sp.getViewport().setBackground(Color.white);
        String text = property.getBrowserText() + "  ";
        if (table.isClosed()) {
            text += "only some";
        }
        else {
            text += "some";
        }
        lc = new OWLLabeledComponent(text, sp);
        // lc.setHeaderIcon(OWLIcons.getImageIcon(OWLIcons.OWL_SOME_VALUES_FROM));
        addNamedClassAction = new AddNamedClassAction(table);
        addIndividualAction = new AddIndividualAction(table);
        deleteRowAction = new DeleteRowAction(table);
        closureAxiomAction = new ChangeClosureAxiomButton(table);
        //JButton closureButton = lc.addHeaderButton(closureAxiomAction);
        //closureAxiomAction.init(closureButton);
        lc.addHeaderButton(addNamedClassAction);
        lc.addHeaderButton(addIndividualAction);
        lc.addHeaderButton(deleteRowAction);
        setContent(lc);
    }


    private void updatePropertyBrowserText() {
        lc.setHeaderLabel(property.getBrowserText());
    }
}
