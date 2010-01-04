package edu.stanford.smi.protegex.owl.testing.owldl;

import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.testing.*;

import java.util.Collections;
import java.util.List;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class NoSuperOrSubPropertiesOfAnnotationPropertiesTest extends AbstractOWLTest implements OWLDLTest, RDFPropertyTest {

    public NoSuperOrSubPropertiesOfAnnotationPropertiesTest() {
        super(GROUP, null);
    }


    public static boolean fails(RDFProperty property) {
        if (property.isAnnotationProperty()) {
            return property.getSubpropertyCount() > 0 || property.getSuperpropertyCount() > 0 ||
                    property.getEquivalentProperties().size() > 0;
        }
        else {
            return false;
        }
    }


    public List test(RDFProperty property) {
        if (fails(property)) {
            return Collections.singletonList(new DefaultOWLTestResult("Annotation properties cannot have super properties or sub properties in OWL DL.",
                    property,
                    OWLTestResult.TYPE_OWL_FULL,
                    this));
        }
        else {
            return Collections.EMPTY_LIST;
        }
    }
}
