package edu.stanford.smi.protegex.owl.model.classdisplay.tests;

import edu.stanford.smi.protegex.owl.model.classdisplay.OWLClassDisplayFactory;
import edu.stanford.smi.protegex.owl.model.classdisplay.compact.CompactOWLClassDisplay;
import edu.stanford.smi.protegex.owl.model.classdisplay.manchester.ManchesterOWLClassDisplay;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class OWLClassRendererFactoryFailedTestCase extends AbstractJenaTestCase {

    public void testDefault() {
        assertTrue(OWLClassDisplayFactory.getDefaultDisplay() instanceof ManchesterOWLClassDisplay);
    }


    public void testAvailables() {
        Collection classes = Arrays.asList(OWLClassDisplayFactory.getAvailableDisplayClasses());
        assertEquals(3, classes.size());
        assertContains(CompactOWLClassDisplay.class, classes);
        assertContains(ManchesterOWLClassDisplay.class, classes);
    }
}
