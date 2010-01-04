package edu.stanford.smi.protegex.owl.ui.search.tests;

import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLUnionClass;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;
import edu.stanford.smi.protegex.owl.ui.search.FindUsage;
import edu.stanford.smi.protegex.owl.ui.search.FindUsageTableItem;

import java.util.Collection;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class FindUsageAsSuperclassTestCase extends AbstractJenaTestCase {

    public void testFindSuperclassUsage() {
        OWLNamedClass hostCls = owlModel.createOWLNamedClass("Host");
        OWLNamedClass searchCls = owlModel.createOWLNamedClass("Search");
        OWLUnionClass unionCls = owlModel.createOWLUnionClass();
        unionCls.addOperand(owlModel.createOWLComplementClass(searchCls));
        unionCls.addOperand(owlModel.createOWLComplementClass(searchCls));
        hostCls.addSuperclass(unionCls);
        Collection items = FindUsage.getItems(searchCls);
        assertSize(1, items);
        FindUsageTableItem item = (FindUsageTableItem) items.iterator().next();
        assertEquals(hostCls, item.host);
        assertEquals(unionCls, item.usage);
        assertEquals(FindUsageTableItem.SUPERCLASS, item.type);
    }


    public void testFindEquivalentClassUsage() {
        OWLNamedClass hostCls = owlModel.createOWLNamedClass("Host");
        OWLNamedClass searchCls = owlModel.createOWLNamedClass("Search");
        OWLUnionClass unionCls = owlModel.createOWLUnionClass();
        unionCls.addOperand(owlModel.createOWLComplementClass(searchCls));
        unionCls.addOperand(owlModel.createOWLComplementClass(searchCls));
        hostCls.addEquivalentClass(unionCls);
        Collection items = FindUsage.getItems(searchCls);
        assertSize(1, items);
        FindUsageTableItem item = (FindUsageTableItem) items.iterator().next();
        assertEquals(hostCls, item.host);
        assertEquals(unionCls, item.usage);
        assertEquals(FindUsageTableItem.EQUIVALENT_CLASS, item.type);
    }
}
