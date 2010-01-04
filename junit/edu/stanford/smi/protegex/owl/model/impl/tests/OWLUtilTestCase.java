package edu.stanford.smi.protegex.owl.model.impl.tests;

import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * These two tests should actually work, but have been delayed by Ray's departure.
 *
 * They are disabled for now.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class OWLUtilTestCase extends AbstractJenaTestCase {

    public void testGetPropertyValuesWithSubproperty() {
        /*
        RDFProperty hasMemberProperty = owlModel.createOWLObjectProperty("hasMember");
        RDFProperty hasLeaderProperty = owlModel.createOWLObjectProperty("hasLeader");
        hasLeaderProperty.addSuperproperty(hasMemberProperty);
        RDFResource person = owlThing.createInstance("Person");
        RDFResource team = owlThing.createInstance("Team");
        team.addPropertyValue(hasLeaderProperty, person);
        assertSize(1, team.getPropertyValues(hasMemberProperty, true));
        team.addPropertyValue(hasMemberProperty, person);
        assertSize(1, team.getPropertyValues(hasMemberProperty, true));
        */
    }


    public void testGetOwnSlotValuesWithSubslotInCoreProtege() {
        /*
        Project project = Project.createNewProject(new ClipsKnowledgeBaseFactory(), Collections.EMPTY_LIST);
        KnowledgeBase kb = project.getKnowledgeBase();
        Slot hasMemberSlot = kb.createSlot("hasMember");
        Slot hasLeaderSlot = kb.createSlot("hasLeader");
        hasLeaderSlot.addDirectSuperslot(hasMemberSlot);
        Instance person = kb.getRootCls().createDirectInstance("Person");
        Instance team = kb.getRootCls().createDirectInstance("Team");
        team.addOwnSlotValue(hasLeaderSlot, person);
        assertEquals(1, team.getOwnSlotValues(hasMemberSlot).size());
        team.addOwnSlotValue(hasMemberSlot, person);
        assertEquals(1, team.getOwnSlotValues(hasMemberSlot).size());
        */
    }
}
