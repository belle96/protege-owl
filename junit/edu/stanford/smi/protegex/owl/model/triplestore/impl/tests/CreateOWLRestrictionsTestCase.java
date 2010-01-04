package edu.stanford.smi.protegex.owl.model.triplestore.impl.tests;

import edu.stanford.smi.protege.model.Frame;
import edu.stanford.smi.protegex.owl.model.OWLCardinality;
import edu.stanford.smi.protegex.owl.model.OWLNames;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateOWLRestrictionsTestCase extends AbstractTripleStoreTestCase {

    public void testCreateCardinalityRestriction() {
        OWLObjectProperty property = owlModel.createOWLObjectProperty("property");
        RDFResource c = createAnonymousResource();
        ts.add(c, owlModel.getRDFTypeProperty(), owlModel.getRDFSNamedClass(OWLNames.Cls.RESTRICTION));
        ts.add(c, owlModel.getRDFProperty(OWLNames.Slot.CARDINALITY), new Integer(1));
        ts.add(c, owlModel.getRDFProperty(OWLNames.Slot.ON_PROPERTY), property);
        ts.add(owlThing, owlModel.getRDFSSubClassOfProperty(), c);
        Frame frame = owlModel.getFrame(c.getName());
        OWLCardinality restriction = (OWLCardinality) frame;
        assertEquals(1, restriction.getCardinality());
        assertEquals(property, restriction.getOnProperty());
    }
}
