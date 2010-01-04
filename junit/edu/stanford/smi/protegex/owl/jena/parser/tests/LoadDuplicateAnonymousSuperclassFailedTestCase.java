package edu.stanford.smi.protegex.owl.jena.parser.tests;

import edu.stanford.smi.protegex.owl.model.OWLMinCardinality;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLProperty;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class LoadDuplicateAnonymousSuperclassFailedTestCase extends AbstractJenaTestCase {

    public void testLoadDuplicateRestriction() throws Exception {
        OWLNamedClass oldClass = owlModel.createOWLNamedClass("Class");
        OWLProperty oldProperty = owlModel.createOWLObjectProperty("oldProperty");
        oldClass.addSuperclass(owlModel.createOWLMinCardinality(oldProperty, 1));
        oldClass.addSuperclass(owlModel.createOWLMinCardinality(oldProperty, 1));
        assertSize(3, oldClass.getSuperclasses(false));
        OWLModel newModel = reload(owlModel);
        OWLNamedClass newClass = newModel.getOWLNamedClass(oldClass.getName());
        final Collection superclasses = new ArrayList(newClass.getSuperclasses(false));
        assertSize(2, superclasses);
        superclasses.remove(newModel.getOWLThingClass());
        assertSize(1, superclasses);
        OWLMinCardinality newRestriction = (OWLMinCardinality) superclasses.iterator().next();
        assertEquals(1, newRestriction.getCardinality());
    }
}
