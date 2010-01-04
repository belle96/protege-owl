package edu.stanford.smi.protegex.owl.testing.style;

import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.testing.AbstractOWLTest;
import edu.stanford.smi.protegex.owl.testing.DefaultOWLTestResult;
import edu.stanford.smi.protegex.owl.testing.OWLTestResult;
import edu.stanford.smi.protegex.owl.testing.RDFSClassTest;

import java.util.Collections;
import java.util.List;

/**
 * Normalisation rule - classes should only have a single asserted named superclass, i.e., be a pure tree
 *
 * @author Nick Drummond, Medical Informatics Group, University of Manchester, 06-Feb-2006
 * 
 */
public class SingleAssertedSuperclassTest extends AbstractOWLTest implements RDFSClassTest {

    public String getDocumentation() {
        return "Checks that named classes only have one asserted parent";
    }

    public String getGroup() {
        return "Style";
    }

    public String getName() {
        return "Normalisation: Single Asserted Superclass";
    }

	public List test(RDFSClass aClass) {
		if (aClass.getNamedSuperclasses().size() > 1) {
			return Collections.singletonList(new DefaultOWLTestResult(
					"This class has multiple asserted parents", aClass,
					OWLTestResult.TYPE_WARNING, this));
		}
		return Collections.EMPTY_LIST;
	}
}
