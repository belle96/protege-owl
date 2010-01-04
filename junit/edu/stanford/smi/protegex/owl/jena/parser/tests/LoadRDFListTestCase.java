package edu.stanford.smi.protegex.owl.jena.parser.tests;

import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFList;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

import java.net.URI;
import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class LoadRDFListTestCase extends AbstractJenaTestCase {


    public void testLoadSWRLOld() throws Exception {
        loadTestOntology(new URI("http://www.daml.org/2003/11/swrl/swrl.owl"));
    }


    public void testLoadSubClassOfList() throws Exception {
        loadRemoteOntology("list-example.owl");
        OWLNamedClass personListCls = owlModel.getOWLNamedClass("PersonList");
        assertNotNull(personListCls);
        assertEquals(5, personListCls.getInstanceCount(false));
        for (Iterator it = personListCls.getInstances(false).iterator(); it.hasNext();) {
            Instance instance = (Instance) it.next();
            assertTrue(instance instanceof RDFList);
            //assertFalse(li.getName().startsWith("Anonymous"));
        }
        Slot personsSlot = owlModel.getRDFProperty("persons");
        assertNotNull(personsSlot);
        Instance myRanking = owlModel.getOWLIndividual("MyRanking");
        assertNotNull(myRanking);
        RDFList head = (RDFList) myRanking.getDirectOwnSlotValue(personsSlot);
        assertNotNull(head);
        Instance first = owlModel.getOWLIndividual("FirstPerson");
        assertNotNull(first);
        assertEquals(first, head.getFirst());
    }


    public void testLoadPetList() throws Exception {
        loadRemoteOntology("randy.owl");
        Instance randy = owlModel.getOWLIndividual("Randy");
        Slot petsSlot = owlModel.getRDFProperty("pets");
        RDFList li = (RDFList) randy.getDirectOwnSlotValue(petsSlot);
        assertSize(3, li.getValues());
    }
}
