package edu.stanford.smi.protegex.owl.ui.search.tests;

import edu.stanford.smi.protegex.owl.model.OWLComplementClass;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.OWLUnionClass;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;
import edu.stanford.smi.protegex.owl.ui.search.FindUsage;
import edu.stanford.smi.protegex.owl.ui.search.FindUsageTableItem;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class FindUsageAsRangeTestCase extends AbstractJenaTestCase {

    public void testFindAllValuesFrom() {

        OWLObjectProperty property = owlModel.createOWLObjectProperty("property");
        OWLNamedClass hostCls = owlModel.createOWLNamedClass("Host");
        OWLNamedClass otherCls = owlModel.createOWLNamedClass("Other");
        OWLUnionClass unionCls = owlModel.createOWLUnionClass();
        unionCls.addOperand(owlModel.createOWLAllValuesFrom(property, otherCls));
        unionCls.addOperand(owlModel.createOWLComplementClass(otherCls));
        hostCls.addSuperclass(unionCls);

        Collection items = FindUsage.getItems(otherCls);
        assertSize(1, items);
        FindUsageTableItem item = (FindUsageTableItem) items.iterator().next();
        assertEquals(hostCls, item.host);
        assertEquals(unionCls, item.usage);
        assertEquals(FindUsageTableItem.SUPERCLASS, item.type);
    }


    public void testFindDirectRange() {
        OWLNamedClass findCls = owlModel.createOWLNamedClass("Find");
        OWLObjectProperty property = owlModel.createOWLObjectProperty("property");
        property.setUnionRangeClasses(Collections.singleton(findCls));

        Collection items = FindUsage.getItems(findCls);
        assertSize(1, items);
        FindUsageTableItem item = (FindUsageTableItem) items.iterator().next();
        assertEquals(property, item.host);
        assertEquals(findCls, item.usage);
        assertEquals(FindUsageTableItem.RANGE, item.type);
    }


    public void testFindNestedRange() {
        OWLNamedClass findCls = owlModel.createOWLNamedClass("Find");
        OWLObjectProperty property = owlModel.createOWLObjectProperty("property");
        OWLComplementClass complementCls = owlModel.createOWLComplementClass(findCls);
        property.setUnionRangeClasses(Collections.singleton(complementCls));

        Collection items = FindUsage.getItems(findCls);
        assertSize(1, items);
        FindUsageTableItem item = (FindUsageTableItem) items.iterator().next();
        assertEquals(property, item.host);
        assertEquals(complementCls, item.usage);
        assertEquals(FindUsageTableItem.RANGE, item.type);
    }
}
