package edu.stanford.smi.protegex.owl.ui.conditions.tests;

import java.net.URI;

import edu.stanford.smi.protegex.owl.model.OWLAllValuesFrom;
import edu.stanford.smi.protegex.owl.model.OWLMaxCardinality;
import edu.stanford.smi.protegex.owl.model.OWLMinCardinality;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.OWLSomeValuesFrom;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.ui.conditions.ConditionsTableModel;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ImportTestCase extends AbstractConditionsTableTestCase {

    private OWLObjectProperty hasChildrenProperty;

    private OWLNamedClass parentCls;

    private OWLNamedClass personCls;

    private ConditionsTableModel tableModel;

    public static final String FILE = "http://protege.stanford.edu/plugins/owl/testdata/importer.owl";


    public void testAddingNamedClassToImportedClass() throws Exception {
        initTestData();
        assertFalse(tableModel.isAddEnabledAt(0));
        assertFalse(tableModel.isAddEnabledAt(1));
        assertTrue(tableModel.isAddEnabledAt(2));
        assertTrue(tableModel.isAddEnabledAt(3));
        assertTrue(tableModel.isAddEnabledAt(4));
        assertFalse(tableModel.isAddEnabledAt(5));
        assertFalse(tableModel.isAddEnabledAt(6));
    }


    public void testAddingAnonymousClassToImportedClass() throws Exception {
        initTestData();
        assertFalse(tableModel.isCreateEnabledAt(0));
        assertFalse(tableModel.isCreateEnabledAt(1));
        assertTrue(tableModel.isCreateEnabledAt(2));
        assertTrue(tableModel.isCreateEnabledAt(3));
        assertTrue(tableModel.isCreateEnabledAt(4));
        assertFalse(tableModel.isCreateEnabledAt(5));
        assertFalse(tableModel.isCreateEnabledAt(6));
    }


    public void testDeletingFromImportedClass() throws Exception {
        initTestData();
        assertFalse(tableModel.isDeleteEnabledFor(tableModel.getClass(1)));
        assertFalse(tableModel.isDeleteEnabledFor(tableModel.getClass(3)));
        assertFalse(tableModel.isDeleteEnabledFor(tableModel.getClass(4)));
        assertFalse(tableModel.isDeleteEnabledFor(tableModel.getClass(6)));
    }


    public void testAddSuperclasses() throws Exception {
        initTestData();
        RDFSClass testClass = owlModel.createOWLSomeValuesFrom(hasChildrenProperty, personCls);
        assertTrue(tableModel.isCreateEnabledAt(2));
        tableModel.addRow(testClass, 2);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                OWLMinCardinality.class,
                NECESSARY,
                personCls,
                OWLAllValuesFrom.class,
                testClass,
                INHERITED,
                OWLMaxCardinality.class
        });
        assertTrue(tableModel.isCreateEnabledAt(3));
        assertFalse(tableModel.isDeleteEnabledFor(tableModel.getClass(4)));
        assertTrue(tableModel.isDeleteEnabledFor(testClass));
        tableModel.addEmptyRow(3);
        assertTrue(tableModel.isCellEditable(6, ConditionsTableModel.COL_EXPRESSION));
    }


    public void testAddAndRemoveEquivalentClass() throws Exception {
        initTestData();
        RDFSClass testClass = owlModel.createOWLSomeValuesFrom(hasChildrenProperty, personCls);
        assertFalse(tableModel.isCreateEnabledAt(0));
        parentCls.addEquivalentClass(testClass);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                testClass,
                SUFFICIENT,
                OWLMinCardinality.class,
                NECESSARY,
                personCls,
                OWLAllValuesFrom.class,
                INHERITED,
                OWLMaxCardinality.class
        });
        assertTrue(tableModel.isCreateEnabledAt(1));
        assertTrue(tableModel.isDeleteEnabledFor(testClass));
        tableModel.addEmptyRow(1);
        assertTrue(tableModel.isCellEditable(2, ConditionsTableModel.COL_EXPRESSION));
    }


    private void initTestData() throws Exception {
        loadTestOntology(new URI(FILE));
        personCls = (OWLNamedClass) owlModel.getOWLNamedClass("imported:Person");
        parentCls = (OWLNamedClass) owlModel.getOWLNamedClass("imported:Parent");
        hasChildrenProperty = (OWLObjectProperty) owlModel.getOWLObjectProperty("imported:hasChildren");
        assertNotNull(personCls);
        assertNotNull(parentCls);
        assertNotNull(hasChildrenProperty);
        tableModel = getTableModel(parentCls);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                OWLMinCardinality.class,
                NECESSARY,
                personCls,
                OWLAllValuesFrom.class,
                INHERITED,
                OWLMaxCardinality.class
        });
    }


    public void testAddNamedClassToImportedDefinedClass() throws Exception {
        loadRemoteOntology("importTravel.owl");
        OWLNamedClass c = owlModel.getOWLNamedClass("travel:BackpackersDestination");
        assertNotNull(c);
        tableModel = getTableModel(c);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                owlModel.getOWLNamedClass("travel:Destination"),
                OWLSomeValuesFrom.class,
                OWLSomeValuesFrom.class,
                NECESSARY
        });
        assertFalse(tableModel.isAddEnabledAt(0));
        assertFalse(tableModel.isAddEnabledAt(1));
        assertTrue(tableModel.isAddEnabledAt(4));
        assertFalse(tableModel.isCreateEnabledAt(0));
        assertFalse(tableModel.isCreateEnabledAt(1));
        assertTrue(tableModel.isCreateEnabledAt(4));
    }


    public void testAddNamedClassToImportedPrimitiveClass() throws Exception {
        loadRemoteOntology("importTravel.owl");
        OWLNamedClass c = owlModel.getOWLNamedClass("travel:Activity");
        assertNotNull(c);
        tableModel = getTableModel(c);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                NECESSARY,
                owlThing
        });
        assertTrue(tableModel.isCreateEnabledAt(0));
        assertTrue(tableModel.isCreateEnabledAt(1));
        assertTrue(tableModel.isCreateEnabledAt(2));
        assertTrue(tableModel.isAddEnabledAt(0));
        assertTrue(tableModel.isAddEnabledAt(1));
        assertTrue(tableModel.isAddEnabledAt(2));

        OWLNamedClass a = owlModel.getOWLNamedClass("travel:Accommodation");
        assertNotNull(a);
        tableModel.addRow(a, 2);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                NECESSARY,
                owlThing,
                a
        });
        assertTrue(tableModel.isRemoveEnabledFor(3));
        assertTrue(tableModel.isDeleteEnabledFor(a));
    }
}
