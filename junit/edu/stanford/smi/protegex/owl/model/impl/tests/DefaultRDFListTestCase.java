package edu.stanford.smi.protegex.owl.model.impl.tests;

import edu.stanford.smi.protegex.owl.model.RDFList;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultRDFListTestCase extends AbstractJenaTestCase {

    public void testRDFListsAreAnonymous() {
        Collection values = Arrays.asList(new String[]{"A", "B"});
        RDFList list = owlModel.createRDFList(values.iterator());
        assertTrue(list.isAnonymous());
        assertTrue(list.getRest().isAnonymous());
    }


    public void testRDFNilIsNotAnonymous() {
        assertFalse(owlModel.getRDFNil().isAnonymous());
    }

    public void testRDFListAppendOrdering() {
        List values = Arrays.asList(new String[]{"A", "B", "C", "D"});
        RDFList list = owlModel.createRDFList();
        for (Iterator i = values.iterator(); i.hasNext();) {
            list.append(i.next());
        }

        RDFList sublist = list;
        for (int i = 0; i < values.size(); i++) {
            assertTrue(sublist.getFirst().equals(values.get(i)));
            sublist = sublist.getRest();
        }
    }
}
