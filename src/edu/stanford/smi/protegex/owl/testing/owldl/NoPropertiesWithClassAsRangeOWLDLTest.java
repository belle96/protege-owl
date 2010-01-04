package edu.stanford.smi.protegex.owl.testing.owldl;

import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.testing.*;

import java.util.Collections;
import java.util.List;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class NoPropertiesWithClassAsRangeOWLDLTest extends AbstractOWLTest implements OWLDLTest, RDFPropertyTest {

    public NoPropertiesWithClassAsRangeOWLDLTest() {
        super(GROUP, null);
    }


    public static boolean fails(RDFProperty property) {
        RDFResource range = property.getRange();
        return range instanceof RDFSClass && ((RDFSClass) range).isMetaclass();
    }


    public List test(RDFProperty property) {
        if (fails(property)) {
            return Collections.singletonList(new DefaultOWLTestResult("Properties with owl:Class as range are not allowed in OWL DL.",
                    property,
                    OWLTestResult.TYPE_OWL_FULL,
                    this));
        }
        else {
            return Collections.EMPTY_LIST;
        }
    }
}
