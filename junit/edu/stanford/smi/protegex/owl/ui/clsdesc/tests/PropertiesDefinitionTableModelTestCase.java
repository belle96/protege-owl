package edu.stanford.smi.protegex.owl.ui.clsdesc.tests;

import edu.stanford.smi.protegex.owl.model.OWLComplementClass;
import edu.stanford.smi.protegex.owl.model.OWLIntersectionClass;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;
import edu.stanford.smi.protegex.owl.ui.clsdesc.PropertiesDefinitionTableModel;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class PropertiesDefinitionTableModelTestCase extends AbstractJenaTestCase {

    public void testDefinedClassFill() {

        OWLNamedClass superCls = owlModel.createOWLNamedClass("SuperCls");
        OWLNamedClass otherCls = owlModel.createOWLNamedClass("Other");
        OWLNamedClass cls = owlModel.createOWLNamedSubclass("Cls", superCls);
        OWLComplementClass complementCls = owlModel.createOWLComplementClass(otherCls);
        OWLIntersectionClass intersectionCls = owlModel.createOWLIntersectionClass();
        intersectionCls.addOperand(superCls);
        intersectionCls.addOperand(complementCls);
        cls.addEquivalentClass(intersectionCls);

        PropertiesDefinitionTableModel tableModel = new PropertiesDefinitionTableModel();
        tableModel.setCls(cls);
        assertEquals(2, tableModel.getRowCount());
        assertEquals(superCls, tableModel.getClass(0));
        assertEquals(complementCls, tableModel.getClass(1));
    }


    public void testAddRows() {

        OWLNamedClass superCls = owlModel.createOWLNamedClass("SuperCls");
        OWLNamedClass cls = owlModel.createOWLNamedSubclass("Cls", superCls);
        cls.addEquivalentClass(superCls);

        PropertiesDefinitionTableModel tableModel = new PropertiesDefinitionTableModel();
        tableModel.setCls(cls);
        assertEquals(1, tableModel.getRowCount());
        assertEquals(superCls, tableModel.getClass(0));

        OWLNamedClass otherCls = owlModel.createOWLNamedClass("Other");
        tableModel.addRow(otherCls, 0);

        OWLIntersectionClass oldIntersectionCls = (OWLIntersectionClass) cls.getDefinition();
        assertSize(2, oldIntersectionCls.getOperands());
        assertContains(superCls, oldIntersectionCls.getOperands());
        assertContains(otherCls, oldIntersectionCls.getOperands());

        tableModel.addRow(owlModel.createOWLComplementClass(otherCls), 1);
        OWLIntersectionClass newIntersectionCls = (OWLIntersectionClass) cls.getDefinition();
        Collection operands = new ArrayList(newIntersectionCls.getOperands());
        operands.remove(superCls);
        operands.remove(otherCls);
        assertSize(1, operands);
        OWLComplementClass complementCls = (OWLComplementClass) operands.iterator().next();
        assertEquals(otherCls, complementCls.getComplement());

        assertFalse(tableModel.addRow(otherCls, 1));
        assertFalse(tableModel.addRow(owlModel.createOWLComplementClass(otherCls), 1));
    }


    public void testSetValueAt() {

        OWLNamedClass superCls = owlModel.createOWLNamedClass("SuperClass");
        OWLNamedClass cls = owlModel.createOWLNamedSubclass("Class", superCls);
        cls.addEquivalentClass(superCls);

        PropertiesDefinitionTableModel tableModel = new PropertiesDefinitionTableModel();
        tableModel.setCls(cls);
        assertEquals(1, tableModel.getRowCount());
        assertEquals(superCls, tableModel.getClass(0));

        OWLNamedClass otherCls = owlModel.createOWLNamedClass("Other");
        tableModel.addEmptyRow(1);
        tableModel.setValueAt(otherCls.getLocalName(), 1, 0);
        tableModel.removeEmptyRow();

        OWLIntersectionClass oldIntersectionCls = (OWLIntersectionClass) cls.getDefinition();
        assertSize(2, oldIntersectionCls.getOperands());
        assertContains(superCls, oldIntersectionCls.getOperands());
        assertContains(otherCls, oldIntersectionCls.getOperands());

        assertEquals(2, tableModel.getRowCount());
        assertEquals(otherCls, tableModel.getClass(0));
        int superClsRow = tableModel.getClassRow(superCls);
        tableModel.setValueAt("not " + otherCls.getLocalName(), superClsRow, 0);
        OWLIntersectionClass newIntersectionCls = (OWLIntersectionClass) cls.getDefinition();
        final Collection operands = new ArrayList(newIntersectionCls.getOperands());
        operands.remove(otherCls);
        OWLComplementClass complementCls = (OWLComplementClass) operands.iterator().next();
        assertEquals(otherCls, complementCls.getComplement());
    }


    public void testSetValueAndReplaceOnlyNamedClass() {

        OWLNamedClass superCls = owlModel.createOWLNamedClass("SuperClass");
        OWLNamedClass cls = owlModel.createOWLNamedSubclass("Class", superCls);
        cls.addEquivalentClass(superCls);

        PropertiesDefinitionTableModel tableModel = new PropertiesDefinitionTableModel();
        tableModel.setCls(cls);
        assertEquals(1, tableModel.getRowCount());
        assertEquals(superCls, tableModel.getClass(0));

        assertEquals(1, cls.getSuperclassCount());
        tableModel.setValueAt("not " + superCls.getLocalName(), 0, 0);
        assertEquals(2, cls.getSuperclassCount());
        assertContains(owlThing, cls.getSuperclasses(false));
    }


    public void testDeleteRow() {

        OWLNamedClass superCls = owlModel.createOWLNamedClass("SuperClass");
        OWLNamedClass cls = owlModel.createOWLNamedSubclass("Class", superCls);
        OWLNamedClass otherCls = owlModel.createOWLNamedClass("Other");
        OWLComplementClass complementCls = owlModel.createOWLComplementClass(otherCls);
        OWLIntersectionClass intersectionCls = owlModel.createOWLIntersectionClass();
        intersectionCls.addOperand(superCls);
        intersectionCls.addOperand(complementCls);
        cls.addEquivalentClass(intersectionCls);

        PropertiesDefinitionTableModel tableModel = new PropertiesDefinitionTableModel();
        tableModel.setCls(cls);
        assertEquals(2, tableModel.getRowCount());
        assertEquals(superCls, tableModel.getClass(0));

        tableModel.deleteRow(1);

        assertEquals(1, tableModel.getRowCount());
        assertEquals(superCls, tableModel.getClass(0));
    }
}
