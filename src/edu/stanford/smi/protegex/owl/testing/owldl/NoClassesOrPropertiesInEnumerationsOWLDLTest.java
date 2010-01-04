package edu.stanford.smi.protegex.owl.testing.owldl;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protegex.owl.model.OWLEnumeratedClass;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.testing.*;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class NoClassesOrPropertiesInEnumerationsOWLDLTest extends AbstractOWLTest implements OWLDLTest, RDFSClassTest {

    public NoClassesOrPropertiesInEnumerationsOWLDLTest() {
        super(GROUP, null);
    }


    public static boolean fails(RDFSClass aClass) {
        if (aClass instanceof OWLEnumeratedClass) {
            for (Iterator it = aClass.getInstances(false).iterator(); it.hasNext();) {
                Instance instance = (Instance) it.next();
                if (instance instanceof Cls || instance instanceof Slot) {
                    return true;
                }
            }
        }
        return false;
    }


    public List test(RDFSClass aClass) {
        if (fails(aClass)) {
            return Collections.singletonList(new DefaultOWLTestResult("Enumerations cannot contain classes or properties in OWL DL.",
                    aClass,
                    OWLTestResult.TYPE_OWL_FULL,
                    this));
        }
        else {
            return Collections.EMPTY_LIST;
        }
    }
}
