package edu.stanford.smi.protegex.owl.ui.components.rdflist.tests;

import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFList;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;
import edu.stanford.smi.protegex.owl.ui.components.rdflist.RDFListComponent;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class RDFListComponentTestCase extends AbstractJenaTestCase {

    public void testRDFListEmpty() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Class");
        RDFProperty property = owlModel.createOWLObjectProperty("property");
        property.setRange(owlModel.getRDFListClass());
        RDFResource individual = cls.createOWLIndividual("Individual");
        RDFListComponent comp = new RDFListComponent(property);
        comp.setSubject(individual);
        assertEquals(0, comp.getRowCount());
        assertSize(0, comp.getSelection());
        assertTrue(comp.isAddEnabled());
        assertTrue(comp.isCreateEnabled());
        assertFalse(comp.isDeleteEnabled());
        assertFalse(comp.isRemoveEnabled());
        assertFalse(comp.isMoveDownEnabled());
        assertFalse(comp.isMoveUpEnabled());
    }


    public void testRDFListSingleEntry() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Class");
        RDFProperty property = owlModel.createOWLObjectProperty("property");
        property.setRange(owlModel.getRDFListClass());
        RDFResource individual = cls.createOWLIndividual("Individual");
        RDFListComponent comp = new RDFListComponent(property);
        comp.setSubject(individual);
        assertEquals(0, comp.getRowCount());
        RDFList list = owlModel.createRDFList();
        individual.setPropertyValue(property, list);
        comp.valuesChanged();
        assertEquals(0, comp.getRowCount());
        list.append(individual);
        assertEquals(1, comp.getRowCount());
        assertTrue(comp.isAddEnabled());
        assertTrue(comp.isCreateEnabled());
        assertFalse(comp.isDeleteEnabled());
        assertFalse(comp.isRemoveEnabled());
        assertFalse(comp.isMoveDownEnabled());
        assertFalse(comp.isMoveUpEnabled());
        comp.setSelectedRow(0);
        assertTrue(comp.isAddEnabled());
        assertTrue(comp.isCreateEnabled());
        assertTrue(comp.isDeleteEnabled());
        assertTrue(comp.isRemoveEnabled());
        assertFalse(comp.isMoveDownEnabled());
        assertFalse(comp.isMoveUpEnabled());
    }


    public void testImportedRDFList() throws Exception {
        loadRemoteOntology("importListProperty.owl");
        RDFResource individual = owlModel.getOWLIndividual("imp:Individual");
        assertNotNull(individual);
        RDFProperty property = owlModel.getRDFProperty("imp:property");
        assertNotNull(property);
        RDFListComponent comp = new RDFListComponent(property);
        comp.setSubject(individual);
        assertEquals(0, comp.getRowCount());
        assertSize(0, comp.getSelection());
        assertFalse(comp.isAddEnabled());
        assertFalse(comp.isCreateEnabled());
        assertFalse(comp.isDeleteEnabled());
        assertFalse(comp.isRemoveEnabled());
        assertFalse(comp.isMoveDownEnabled());
        assertFalse(comp.isMoveUpEnabled());
    }
}
