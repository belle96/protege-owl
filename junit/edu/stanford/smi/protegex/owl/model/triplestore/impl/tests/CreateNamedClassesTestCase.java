package edu.stanford.smi.protegex.owl.model.triplestore.impl.tests;

import edu.stanford.smi.protege.model.Frame;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFNames;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateNamedClassesTestCase extends AbstractTripleStoreTestCase {

    public void testCreateOWLNamedClass() {
        String name = "Class";
        RDFResource c = createRDFResource(name);
        ts.add(c, owlModel.getRDFProperty(RDFNames.Slot.TYPE), owlModel.getOWLNamedClassClass());
        Frame f = owlModel.getFrame(name);
        assertTrue(f instanceof OWLNamedClass);
    }


    public void testCreateRDFSNamedClass() {
        String name = "Class";
        RDFResource c = createRDFResource(name);
        ts.add(c, owlModel.getRDFProperty(RDFNames.Slot.TYPE), owlModel.getRDFSNamedClassClass());
        Frame f = owlModel.getFrame(name);
        assertTrue(f instanceof RDFSNamedClass);
        assertFalse(f instanceof OWLNamedClass);
    }
}
