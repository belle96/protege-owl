package edu.stanford.smi.protegex.owl.ui.conditions.tests;

import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;
import edu.stanford.smi.protegex.owl.ui.conditions.AddClosureAxiomAction;
import edu.stanford.smi.protegex.owl.ui.conditions.ConditionsTable;
import edu.stanford.smi.protegex.owl.ui.conditions.ConditionsTableModel;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class AddClosureAxiomTestCase extends AbstractJenaTestCase {

    public void testAddClosureAxiomToPrimitiveClass() {
        OWLNamedClass personCls = owlModel.createOWLNamedClass("Person");
        final ConditionsTableModel tableModel = new ConditionsTableModel(owlModel);
        ConditionsTable table = new ConditionsTable(owlModel, tableModel);
        tableModel.setCls(personCls);

        OWLNamedClass fatherCls = owlModel.createOWLNamedClass("Father");
        OWLNamedClass motherCls = owlModel.createOWLNamedClass("Mother");
        OWLObjectProperty property = owlModel.createOWLObjectProperty("hasParent");
        OWLSomeValuesFrom fatherRestriction = owlModel.createOWLSomeValuesFrom(property, fatherCls);
        OWLSomeValuesFrom motherRestriction = owlModel.createOWLSomeValuesFrom(property, motherCls);
        personCls.addSuperclass(fatherRestriction);
        personCls.addSuperclass(motherRestriction);
        AddClosureAxiomAction action = new AddClosureAxiomAction();
        assertFalse(action.isSuitable(table, personCls));
        assertFalse(action.isSuitable(table, fatherCls));
        assertTrue(action.isSuitable(table, fatherRestriction));
        assertTrue(action.isSuitable(table, motherRestriction));
        action.initialize(table, fatherRestriction);
        action.actionPerformed(null);
        assertEquals(4, personCls.getSuperclassCount());
        OWLAllValuesFrom allRestriction = (OWLAllValuesFrom) owlModel.getCls(OWLNames.Cls.ALL_VALUES_FROM_RESTRICTION).getDirectInstances().iterator().next();
        assertContains(allRestriction, personCls.getPureSuperclasses());
        assertEquals(property, allRestriction.getOnProperty());
        OWLUnionClass filler = (OWLUnionClass) allRestriction.getFiller();
        assertContains(fatherCls, filler.getOperands());
        assertContains(motherCls, filler.getOperands());
        assertSize(2, filler.getOperands());
    }


    public void testAddClosureAxiomToSimpleDefinedClass() {
        OWLNamedClass personCls = owlModel.createOWLNamedClass("Person");
        final ConditionsTableModel tableModel = new ConditionsTableModel(owlModel);
        ConditionsTable table = new ConditionsTable(owlModel, tableModel);
        tableModel.setCls(personCls);

        OWLNamedClass fatherCls = owlModel.createOWLNamedClass("Father");
        OWLObjectProperty property = owlModel.createOWLObjectProperty("hasParent");
        OWLSomeValuesFrom fatherRestriction = owlModel.createOWLSomeValuesFrom(property, fatherCls);
        personCls.setDefinition(fatherRestriction);
        AddClosureAxiomAction action = new AddClosureAxiomAction();
        assertFalse(action.isSuitable(table, personCls));
        assertFalse(action.isSuitable(table, fatherCls));
        assertTrue(action.isSuitable(table, fatherRestriction));
        action.initialize(table, fatherRestriction);
        action.actionPerformed(null);
        assertEquals(2, personCls.getSuperclassCount());
        OWLIntersectionClass newIntersectionCls = (OWLIntersectionClass) personCls.getDefinition();
        assertEquals("(hasParent some Father) and (hasParent only Father)",
                newIntersectionCls.getBrowserText());
    }


    public void testAddClosureAxiomToComplexDefinedClass() {
        OWLNamedClass personCls = owlModel.createOWLNamedClass("Person");
        final ConditionsTableModel tableModel = new ConditionsTableModel(owlModel);
        ConditionsTable table = new ConditionsTable(owlModel, tableModel);
        tableModel.setCls(personCls);

        OWLNamedClass fatherCls = owlModel.createOWLNamedClass("Father");
        OWLNamedClass motherCls = owlModel.createOWLNamedClass("Mother");
        OWLObjectProperty property = owlModel.createOWLObjectProperty("hasParent");
        OWLSomeValuesFrom fatherRestriction = owlModel.createOWLSomeValuesFrom(property, fatherCls);
        OWLSomeValuesFrom motherRestriction = owlModel.createOWLSomeValuesFrom(property, motherCls);
        OWLIntersectionClass intersectionCls = owlModel.createOWLIntersectionClass();
        intersectionCls.addOperand(fatherRestriction);
        intersectionCls.addOperand(motherRestriction);
        personCls.setDefinition(intersectionCls);
        AddClosureAxiomAction action = new AddClosureAxiomAction();
        assertFalse(action.isSuitable(table, personCls));
        assertFalse(action.isSuitable(table, fatherCls));
        assertTrue(action.isSuitable(table, fatherRestriction));
        assertTrue(action.isSuitable(table, motherRestriction));
        action.initialize(table, fatherRestriction);
        action.actionPerformed(null);
        assertEquals(2, personCls.getSuperclassCount());
        OWLIntersectionClass newIntersectionCls = (OWLIntersectionClass) personCls.getDefinition();
        assertEquals("(hasParent some Father) and (hasParent some Mother) and (hasParent only (Father or Mother))",
                newIntersectionCls.getBrowserText());
    }
}
