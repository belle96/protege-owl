package edu.stanford.smi.protegex.owl.ui.search.tests;

import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;
import edu.stanford.smi.protegex.owl.ui.search.FindUsage;
import edu.stanford.smi.protegex.owl.ui.search.FindUsageTableItem;

import java.util.Collection;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class FindUsageOfIndividualTestCase extends AbstractJenaTestCase {

    public void testFindUsageAsPropertyValue() {
        OWLNamedClass c = owlModel.createOWLNamedClass("Person");
        RDFProperty property = owlModel.createOWLObjectProperty("hasChild");
        property.setDomain(c);
        property.setRange(c);
        OWLIndividual holgi = c.createOWLIndividual("Holger");
        OWLIndividual darwin = c.createOWLIndividual("Darwin");
        holgi.setPropertyValue(property, darwin);
        Collection items = FindUsage.getItems(darwin);
        assertSize(1, items);
        FindUsageTableItem item = (FindUsageTableItem) items.iterator().next();
        assertEquals(FindUsageTableItem.VALUE, item.type);
        assertEquals(property, item.usage);
    }
}
