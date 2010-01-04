package edu.stanford.smi.protegex.owl.testing.owldl;

import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.testing.*;

import java.util.Collections;
import java.util.List;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class NoMetaclassOWLDLTest extends AbstractOWLTest implements OWLDLTest, RDFSClassTest {

    public NoMetaclassOWLDLTest() {
        super(GROUP, null);
    }


    public static boolean fails(RDFSClass aClass) {
        return aClass.isMetaclass();
    }


    public List test(RDFSClass aClass) {
        if (fails(aClass)) {
            return Collections.singletonList(new DefaultOWLTestResult("Metaclasses are not allowed in OWL DL.",
                    aClass,
                    OWLTestResult.TYPE_OWL_FULL,
                    this));
        }
        else {
            return Collections.EMPTY_LIST;
        }
    }
}
