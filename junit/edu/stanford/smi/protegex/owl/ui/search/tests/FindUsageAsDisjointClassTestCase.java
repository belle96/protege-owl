package edu.stanford.smi.protegex.owl.ui.search.tests;

import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.OWLUnionClass;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;
import edu.stanford.smi.protegex.owl.ui.search.FindUsage;
import edu.stanford.smi.protegex.owl.ui.search.FindUsageTableItem;

import java.util.Collection;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class FindUsageAsDisjointClassTestCase extends AbstractJenaTestCase {

    public void testFindSimpleDisjointClass() {
        OWLNamedClass hostCls = owlModel.createOWLNamedClass("Host");
        OWLNamedClass otherCls = owlModel.createOWLNamedClass("Other");
        hostCls.addDisjointClass(otherCls);

        Collection items = FindUsage.getItems(otherCls);
        assertSize(1, items);
        FindUsageTableItem item = (FindUsageTableItem) items.iterator().next();
        assertEquals(hostCls, item.host);
        assertEquals(otherCls, item.usage);
        assertEquals(FindUsageTableItem.DISJOINT_CLASS, item.type);
    }


    public void testFindComplexDisjointClass() {
        OWLObjectProperty property = owlModel.createOWLObjectProperty("property");
        OWLNamedClass hostCls = owlModel.createOWLNamedClass("Host");
        OWLNamedClass otherCls = owlModel.createOWLNamedClass("Other");
        OWLUnionClass unionCls = owlModel.createOWLUnionClass();
        unionCls.addOperand(owlModel.createOWLMinCardinality(property, 1));
        unionCls.addOperand(owlModel.createOWLComplementClass(otherCls));
        hostCls.addDisjointClass(unionCls);

        Collection items = FindUsage.getItems(otherCls);
        assertSize(1, items);
        FindUsageTableItem item = (FindUsageTableItem) items.iterator().next();
        assertEquals(hostCls, item.host);
        assertEquals(unionCls, item.usage);
        assertEquals(FindUsageTableItem.DISJOINT_CLASS, item.type);
    }
}
