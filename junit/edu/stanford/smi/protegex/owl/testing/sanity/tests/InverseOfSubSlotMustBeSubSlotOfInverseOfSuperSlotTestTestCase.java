package edu.stanford.smi.protegex.owl.testing.sanity.tests;

import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.OWLProperty;
import edu.stanford.smi.protegex.owl.testing.sanity.InverseOfSubpropertyMustBeSubpropertyOfInverseOfSuperpropertyTest;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class InverseOfSubSlotMustBeSubSlotOfInverseOfSuperSlotTestTestCase extends AbstractJenaTestCase {

    public void testPippi() throws Exception {
        loadRemoteOntology("pipi.owl");
        OWLProperty q = owlModel.getOWLProperty("Q");
        OWLProperty q1 = owlModel.getOWLProperty("Q1");
        OWLProperty q2 = owlModel.getOWLProperty("Q2");
        OWLProperty p = owlModel.getOWLProperty("P");
        OWLProperty p1 = owlModel.getOWLProperty("P1");
        OWLProperty p2 = owlModel.getOWLProperty("P2");
        assertEquals(q, p.getInverseProperty());
        assertEquals(q1, p1.getInverseProperty());
        assertEquals(q2, p2.getInverseProperty());
        assertFalse(InverseOfSubpropertyMustBeSubpropertyOfInverseOfSuperpropertyTest.fails(q));
        assertFalse(InverseOfSubpropertyMustBeSubpropertyOfInverseOfSuperpropertyTest.fails(p));
        assertFalse(InverseOfSubpropertyMustBeSubpropertyOfInverseOfSuperpropertyTest.fails(q1));
        assertFalse(InverseOfSubpropertyMustBeSubpropertyOfInverseOfSuperpropertyTest.fails(q2));
        assertFalse(InverseOfSubpropertyMustBeSubpropertyOfInverseOfSuperpropertyTest.fails(p1));
        assertFalse(InverseOfSubpropertyMustBeSubpropertyOfInverseOfSuperpropertyTest.fails(p2));
    }


    /**
     * a    x
     * b    y
     * c    z
     */
    public void testMove() {
        OWLObjectProperty a = owlModel.createOWLObjectProperty("a");
        OWLObjectProperty b = owlModel.createOWLObjectProperty("b");
        OWLObjectProperty c = owlModel.createOWLObjectProperty("c");
        b.addSuperproperty(a);
        c.addSuperproperty(a);

        OWLObjectProperty x = owlModel.createOWLObjectProperty("x");
        OWLObjectProperty y = owlModel.createOWLObjectProperty("y");
        OWLObjectProperty z = owlModel.createOWLObjectProperty("z");
        y.addSuperproperty(x);
        z.addSuperproperty(x);

        a.setInverseProperty(x);
        b.setInverseProperty(y);
        c.setInverseProperty(z);

        assertFalse(InverseOfSubpropertyMustBeSubpropertyOfInverseOfSuperpropertyTest.fails(a));
        assertFalse(InverseOfSubpropertyMustBeSubpropertyOfInverseOfSuperpropertyTest.fails(b));
        assertFalse(InverseOfSubpropertyMustBeSubpropertyOfInverseOfSuperpropertyTest.fails(c));
        assertFalse(InverseOfSubpropertyMustBeSubpropertyOfInverseOfSuperpropertyTest.fails(x));
        assertFalse(InverseOfSubpropertyMustBeSubpropertyOfInverseOfSuperpropertyTest.fails(y));
        assertFalse(InverseOfSubpropertyMustBeSubpropertyOfInverseOfSuperpropertyTest.fails(z));

        c.removeSuperproperty(a);
        c.addSuperproperty(b);

        assertFalse(InverseOfSubpropertyMustBeSubpropertyOfInverseOfSuperpropertyTest.fails(a));
        assertFalse(InverseOfSubpropertyMustBeSubpropertyOfInverseOfSuperpropertyTest.fails(b));
        assertTrue(InverseOfSubpropertyMustBeSubpropertyOfInverseOfSuperpropertyTest.fails(c));
        assertFalse(InverseOfSubpropertyMustBeSubpropertyOfInverseOfSuperpropertyTest.fails(x));
        assertFalse(InverseOfSubpropertyMustBeSubpropertyOfInverseOfSuperpropertyTest.fails(y));
        assertFalse(InverseOfSubpropertyMustBeSubpropertyOfInverseOfSuperpropertyTest.fails(z));

        InverseOfSubpropertyMustBeSubpropertyOfInverseOfSuperpropertyTest.repair(c);

        assertFalse(InverseOfSubpropertyMustBeSubpropertyOfInverseOfSuperpropertyTest.fails(c));
    }
}
