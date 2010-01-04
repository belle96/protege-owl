package edu.stanford.smi.protegex.owl.model.impl.tests;

import edu.stanford.smi.protegex.owl.model.OWLAllDifferent;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFList;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultOWLAllDifferentTestCase extends AbstractJenaTestCase {

    public void testDistinctMembers() {
        OWLNamedClass colorClass = owlModel.createOWLNamedClass("Color");
        OWLIndividual red = colorClass.createOWLIndividual("red");
        OWLIndividual blue = colorClass.createOWLIndividual("blue");
        OWLAllDifferent allDifferent = owlModel.createOWLAllDifferent();
        assertNull(allDifferent.getPropertyValue(owlModel.getOWLDistinctMembersProperty()));
        assertSize(0, allDifferent.getDistinctMembers());

        allDifferent.addDistinctMember(red);
        allDifferent.addDistinctMember(blue);

        Object value = allDifferent.getPropertyValue(owlModel.getOWLDistinctMembersProperty());
        assertTrue(value instanceof RDFList);
        assertSize(2, ((RDFList) value).getValues());
        assertSize(2, allDifferent.getDistinctMembers());
        assertContains(red, allDifferent.getDistinctMembers());
        assertContains(blue, allDifferent.getDistinctMembers());

        allDifferent.removeDistinctMember(blue);
        allDifferent.removeDistinctMember(red);
        assertNull(allDifferent.getPropertyValue(owlModel.getOWLDistinctMembersProperty()));
        assertSize(0, allDifferent.getDistinctMembers());
    }
}
