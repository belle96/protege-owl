package edu.stanford.smi.protegex.owl.ui.components.triples.tests;

import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;
import edu.stanford.smi.protegex.owl.ui.components.triples.TriplesTableModel;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class TriplesTableModelTestCase extends AbstractJenaTestCase {

    public void testCreateTableModelForOWLThing() {
        TriplesTableModel tableModel = new TriplesTableModel(owlThing);
        assertEquals(4, tableModel.getColumnCount());
        assertEquals(1, tableModel.getRowCount());
        assertEquals(owlModel.getRDFTypeProperty(), tableModel.getPredicate(0));
        assertEquals(owlModel.getOWLNamedClassClass(), tableModel.getValue(0));
        assertNull(tableModel.getValueAt(0, 3));
    }


    public void testEditDatatype() {
        RDFResource subject = owlThing.createInstance("Instance");
        TriplesTableModel tableModel = new TriplesTableModel(subject);
        assertEquals(4, tableModel.getColumnCount());
        assertEquals(1, tableModel.getRowCount());
        RDFProperty predicate = owlModel.getOWLVersionInfoProperty();
        subject.addPropertyValue(predicate, "42");
        assertEquals(2, tableModel.getRowCount());
        assertEquals(predicate, tableModel.getValueAt(0, TriplesTableModel.COL_PROPERTY));
        assertEquals(owlModel.getXSDstring(), tableModel.getValueAt(0, 2));
        assertTrue(tableModel.isCellEditable(0, 2));
        tableModel.setValueAt(owlModel.getXSDint(), 0, 2);
        assertEquals(owlModel.getXSDint(), tableModel.getValueAt(0, 2));
    }


    public void testEditLanguage() {
        RDFResource subject = owlThing.createInstance("Instance");
        TriplesTableModel tableModel = new TriplesTableModel(subject);
        assertEquals(4, tableModel.getColumnCount());
        String text = "Test";
        RDFProperty property = owlModel.getRDFSLabelProperty();
        subject.addPropertyValue(property, text);
        assertEquals(2, tableModel.getRowCount());
        assertEquals(property, tableModel.getPredicate(1));
        tableModel.setValueAt("de", 1, 3);
        assertEquals(2, tableModel.getRowCount());
        assertEquals("de", tableModel.getValueAt(1, 3));
        tableModel.setValueAt("es", 1, 3);
        assertEquals(2, tableModel.getRowCount());
    }
}
