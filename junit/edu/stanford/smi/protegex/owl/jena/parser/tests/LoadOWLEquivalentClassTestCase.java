package edu.stanford.smi.protegex.owl.jena.parser.tests;

import edu.stanford.smi.protegex.owl.model.OWLIntersectionClass;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

import java.util.Collection;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class LoadOWLEquivalentClassTestCase extends AbstractJenaTestCase {

    /**
     * C = (A & B) declared without owl:equivalentClass statement
     */
    public void testLoadInlineEquivalentClass() throws Exception {
        loadRemoteOntology("inlineEquivalentClass.owl");
        OWLNamedClass a = owlModel.getOWLNamedClass("A");
        OWLNamedClass b = owlModel.getOWLNamedClass("B");
        OWLNamedClass c = owlModel.getOWLNamedClass("C");
        Collection superclasses = c.getSuperclasses(false);
        assertSize(4, superclasses);
        assertContains(a, superclasses);
        assertContains(b, superclasses);
        assertContains(owlThing, superclasses);
        //assertTrue(superclasses.iterator().next() instanceof RDFSNamedClass);
        OWLIntersectionClass intersectionClass = (OWLIntersectionClass) c.getDefinition();
        assertSize(2, intersectionClass.getOperands());
        assertContains(a, intersectionClass.getOperands());
        assertContains(b, intersectionClass.getOperands());
        RDFProperty owlEquivalentProperty = owlModel.getOWLEquivalentClassProperty();
        assertSize(0, a.getPropertyValues(owlEquivalentProperty));
        assertSize(0, b.getPropertyValues(owlEquivalentProperty));
        assertSize(1, c.getPropertyValues(owlEquivalentProperty));
        assertEquals(intersectionClass, c.getPropertyValue(owlEquivalentProperty));
    }
}
