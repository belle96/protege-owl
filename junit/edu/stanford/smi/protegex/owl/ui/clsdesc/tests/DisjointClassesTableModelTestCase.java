package edu.stanford.smi.protegex.owl.ui.clsdesc.tests;

import edu.stanford.smi.protegex.owl.model.OWLComplementClass;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;
import edu.stanford.smi.protegex.owl.ui.clsdesc.DisjointClassesTableModel;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DisjointClassesTableModelTestCase extends AbstractJenaTestCase {

    public void testSubclassesDisjoint() throws Exception {

        loadRemoteOntologyWithProtegeMetadataOntology();

        OWLNamedClass superCls = owlModel.createOWLNamedClass("Cls");
        superCls.setSubclassesDisjoint(true);
        OWLNamedClass a = owlModel.createOWLNamedSubclass("A", superCls);
        OWLNamedClass b = owlModel.createOWLNamedSubclass("B", superCls);
        OWLNamedClass c = owlModel.createOWLNamedSubclass("C", superCls);
        OWLNamedClass z = owlModel.createOWLNamedClass("Z");
        a.addDisjointClass(z);
        assertSize(3, a.getDisjointClasses());

        DisjointClassesTableModel tableModel = new DisjointClassesTableModel();
        tableModel.setCls(a);
        assertEquals(3, tableModel.getRowCount());
        assertFalse(tableModel.isRemoveEnabledFor(b));
        assertFalse(tableModel.isRemoveEnabledFor(c));
        assertTrue(tableModel.isRemoveEnabledFor(z));
    }


    public void testImportedClasses() throws Exception {
        loadRemoteOntology("importTravel.owl");
        OWLNamedClass adventureClass = owlModel.getOWLNamedClass("travel:Adventure");
        DisjointClassesTableModel tableModel = new DisjointClassesTableModel();
        tableModel.setCls(adventureClass);
        assertEquals(3, tableModel.getRowCount());
        RDFSClass disjointClass = tableModel.getClass(0);
        assertFalse(tableModel.isDeleteEnabledFor(disjointClass));
        assertFalse(tableModel.isRemoveEnabledFor(disjointClass));
        assertFalse(tableModel.isCellEditable(0, 0));
    }


    public void testEditImportedClasses() throws Exception {
        loadRemoteOntology("importTravel.owl");
        OWLNamedClass activityClass = owlModel.getOWLNamedClass("travel:Activity");
        OWLNamedClass contactClass = owlModel.getOWLNamedClass("travel:Contact");
        assertNotNull(activityClass);
        assertNotNull(contactClass);
        assertSize(0, contactClass.getDisjointClasses());

        DisjointClassesTableModel tableModel = new DisjointClassesTableModel();
        tableModel.setCls(contactClass);
        assertEquals(0, tableModel.getRowCount());

        contactClass.addDisjointClass(activityClass);
        assertEquals(1, tableModel.getRowCount());
        assertEquals(activityClass, tableModel.getClass(0));
        assertTrue(tableModel.isRemoveEnabledFor(activityClass));
    }


    public void testAddExpression() {
        OWLNamedClass c = owlModel.createOWLNamedClass("Class");
        assertSize(0, c.getDisjointClasses());
        DisjointClassesTableModel tableModel = new DisjointClassesTableModel();
        tableModel.setCls(c);
        tableModel.addEmptyRow(0);
        tableModel.setValueAt("not Class", 0, 0);
        assertSize(1, c.getDisjointClasses());
        RDFSClass disjoint = (RDFSClass) c.getDisjointClasses().iterator().next();
        assertTrue(disjoint instanceof OWLComplementClass);
        assertEquals(1, tableModel.getRowCount());
        assertEquals(disjoint, tableModel.getRDFResource(0));
    }
}
