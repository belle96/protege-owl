package edu.stanford.smi.protegex.owl.ui.clsdesc.tests;

import edu.stanford.smi.protegex.owl.model.OWLComplementClass;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;
import edu.stanford.smi.protegex.owl.ui.clsdesc.PropertiesSuperclassesTableModel;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class PropertiesClassesTableModelTestCase extends AbstractJenaTestCase {

    public void testFill() {

        OWLNamedClass superCls = owlModel.createOWLNamedClass("SuperCls");
        OWLNamedClass otherCls = owlModel.createOWLNamedClass("Other");
        OWLNamedClass cls = owlModel.createOWLNamedSubclass("Cls", superCls);
        OWLComplementClass complementCls = owlModel.createOWLComplementClass(otherCls);
        cls.addSuperclass(complementCls);

        PropertiesSuperclassesTableModel tableModel = new PropertiesSuperclassesTableModel();
        tableModel.setCls(cls);
        assertEquals(2, tableModel.getRowCount());
        assertEquals(superCls, tableModel.getClass(0));
        assertEquals(complementCls, tableModel.getClass(1));
    }


    public void testAddsRemoveAndDelete() {

        OWLNamedClass superCls = owlModel.createOWLNamedClass("SuperCls");
        OWLNamedClass cls = owlModel.createOWLNamedSubclass("Cls", superCls);
        OWLNamedClass otherCls = owlModel.createOWLNamedClass("Other");

        PropertiesSuperclassesTableModel tableModel = new PropertiesSuperclassesTableModel();
        tableModel.setCls(cls);
        assertEquals(1, tableModel.getRowCount());
        assertEquals(superCls, tableModel.getClass(0));
        assertFalse(tableModel.isRemoveEnabledFor(superCls));
        assertFalse(tableModel.isDeleteEnabledFor(superCls));

        tableModel.addEmptyRow(1);
        tableModel.setValueAt("Other", 1, 0);
        tableModel.removeEmptyRow();
        assertTrue(cls.isSubclassOf(otherCls));
        assertEquals(2, tableModel.getRowCount());
        assertTrue(tableModel.isRemoveEnabledFor(otherCls));
        assertTrue(tableModel.isRemoveEnabledFor(superCls));

        tableModel.addEmptyRow(2);
        tableModel.setValueAt("not Other", 2, 0);
        tableModel.removeEmptyRow();
        assertEquals(3, tableModel.getRowCount());
        OWLComplementClass complementCls = (OWLComplementClass) tableModel.getClass(2);
        assertTrue(cls.isSubclassOf(complementCls));
        assertTrue(tableModel.isDeleteEnabledFor(complementCls));
        assertEquals(otherCls, complementCls.getComplement());

        assertEquals(otherCls, tableModel.getClass(0));
        tableModel.deleteRow(0);
        assertFalse(cls.isSubclassOf(otherCls));

        tableModel.deleteRow(1);
        assertFalse(cls.isSubclassOf(complementCls));
        assertEquals(1, tableModel.getRowCount());
    }


    public void testUpdateOnExternalChange() {

        OWLNamedClass superCls = owlModel.createOWLNamedClass("SuperCls");
        OWLNamedClass cls = owlModel.createOWLNamedSubclass("Cls", superCls);
        OWLNamedClass otherCls = owlModel.createOWLNamedClass("Other");

        PropertiesSuperclassesTableModel tableModel = new PropertiesSuperclassesTableModel();
        tableModel.setCls(cls);
        assertEquals(1, tableModel.getRowCount());
        assertEquals(superCls, tableModel.getClass(0));

        cls.addSuperclass(otherCls);
        assertEquals(2, tableModel.getRowCount());
    }
}
