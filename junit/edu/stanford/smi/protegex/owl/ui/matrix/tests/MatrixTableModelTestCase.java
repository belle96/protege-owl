package edu.stanford.smi.protegex.owl.ui.matrix.tests;

import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;
import edu.stanford.smi.protegex.owl.ui.matrix.MatrixFilter;
import edu.stanford.smi.protegex.owl.ui.matrix.MatrixTableModel;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class MatrixTableModelTestCase extends AbstractJenaTestCase {

    public void testAddSortedByName() {
        MatrixTableModel tableModel = new MatrixTableModel(owlModel, new DummyFilter());
        assertEquals(0, tableModel.getRowCount());
        OWLNamedClass b = owlModel.createOWLNamedClass("b");
        assertEquals(1, tableModel.getRowCount());
        OWLNamedClass a = owlModel.createOWLNamedClass("a");
        assertEquals(2, tableModel.getRowCount());
        assertEquals(a, tableModel.getInstance(0));
        assertEquals(b, tableModel.getInstance(1));
        OWLNamedClass c = owlModel.createOWLNamedClass("c");
        assertEquals(3, tableModel.getRowCount());
        assertEquals(a, tableModel.getInstance(0));
        assertEquals(b, tableModel.getInstance(1));
        assertEquals(c, tableModel.getInstance(2));
    }


    private class DummyFilter implements MatrixFilter {

        public Collection getInitialValues() {
            return Collections.EMPTY_LIST;
        }


        public String getName() {
            return "Test";
        }


        public boolean isSuitable(RDFResource instance) {
            return true;
        }
    }
}
