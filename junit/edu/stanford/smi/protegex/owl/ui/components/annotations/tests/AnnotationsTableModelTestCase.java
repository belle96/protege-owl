package edu.stanford.smi.protegex.owl.ui.components.annotations.tests;

import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;
import edu.stanford.smi.protegex.owl.ui.components.annotations.AnnotationsTableModel;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class AnnotationsTableModelTestCase extends AbstractJenaTestCase {

    public void testSimpleTableModel() {
        OWLNamedClass c = owlModel.createOWLNamedClass("Class");
        c.addPropertyValue(owlModel.getRDFSLabelProperty(), "Value");
        AnnotationsTableModel tableModel = new AnnotationsTableModel(c);
	    int defPropSize = tableModel.getDefaultProperties().size();
        assertEquals(defPropSize + 1, tableModel.getRowCount());
        assertTrue(tableModel.isCellEditable(defPropSize, AnnotationsTableModel.COL_VALUE));
        assertTrue(tableModel.isCellEditable(defPropSize, AnnotationsTableModel.COL_VALUE + 1));
        assertFalse(tableModel.isCellEditable(defPropSize, AnnotationsTableModel.COL_PROPERTY));
    }


    public void testEditableSystemResources() {
        RDFProperty property = owlModel.getRDFSLabelProperty();
        RDFResource resource = owlModel.getOWLNamedClassClass(); // Any non-editable resource
        AnnotationsTableModel tableModel = new AnnotationsTableModel(resource);
        int defPropSize = tableModel.getDefaultProperties().size();
	    assertEquals(defPropSize, tableModel.getRowCount());
        String value = "Value";
        resource.addPropertyValue(property, value);
        assertEquals(defPropSize + 1, tableModel.getRowCount());
        assertEquals(value, tableModel.getValueAt(defPropSize, AnnotationsTableModel.COL_VALUE));
        assertTrue(tableModel.isCellEditable(defPropSize, AnnotationsTableModel.COL_VALUE));
        final String newValue = "NewValue";
        tableModel.setValueAt(newValue, defPropSize, AnnotationsTableModel.COL_VALUE);
        assertSize(defPropSize, resource.getPropertyValues(property));
        assertContains(newValue, resource.getPropertyValues(property));
    }


    public void testEditableImports() throws Exception {
        loadRemoteOntology("importTravel.owl");
        RDFResource resource = owlModel.getRDFResource("travel:Accommodation");
        assertNotNull(resource);
        AnnotationsTableModel tableModel = new AnnotationsTableModel(resource);
        Collection defProps = new ArrayList(tableModel.getDefaultProperties());
	    defProps.removeAll(resource.getRDFProperties());
	    int defPropSize = defProps.size();
	    assertEquals(defPropSize + 1, tableModel.getRowCount());
        assertFalse(tableModel.isCellEditable(0, AnnotationsTableModel.COL_VALUE));
        resource.addPropertyValue(owlModel.getOWLVersionInfoProperty(), "Value");
        assertEquals(defPropSize + 2, tableModel.getRowCount());
        assertEquals(owlModel.getOWLVersionInfoProperty(), tableModel.getPredicate(defPropSize));
        assertTrue(tableModel.isCellEditable(defPropSize, AnnotationsTableModel.COL_VALUE));
        assertTrue(tableModel.isDeleteEnabled(defPropSize));
    }


    public void testEditXMLLiteral() {
        RDFResource subject = owlThing;
        RDFProperty predicate = owlModel.createAnnotationOWLDatatypeProperty("property");
        RDFSDatatype type = owlModel.getRDFXMLLiteralType();
        predicate.setRange(type);
        String lexicalValue = "<foo>value</foo>";
        RDFSLiteral literal = owlModel.createRDFSLiteral(lexicalValue, type);
        subject.setPropertyValue(predicate, literal);
        AnnotationsTableModel tableModel = new AnnotationsTableModel(subject);
        int defPropSize = tableModel.getDefaultProperties().size();
	    assertEquals(defPropSize + 1, tableModel.getRowCount());
        assertEquals(lexicalValue, tableModel.getValueAt(1, 1));
        String newLexicalValue = "<bar>value</bar>";
        tableModel.setValueAt(newLexicalValue, defPropSize, 1);
        assertSize(1, subject.getPropertyValues(predicate));
        Object value = subject.getPropertyValue(predicate);
        assertTrue(value instanceof RDFSLiteral);
        RDFSLiteral newLiteral = (RDFSLiteral) value;
        assertEquals(newLexicalValue, newLiteral.getString());
        assertEquals(type, newLiteral.getDatatype());
    }
}
