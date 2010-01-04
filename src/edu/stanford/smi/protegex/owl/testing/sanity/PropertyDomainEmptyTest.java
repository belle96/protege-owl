package edu.stanford.smi.protegex.owl.testing.sanity;

import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.testing.AbstractOWLTest;
import edu.stanford.smi.protegex.owl.testing.DefaultOWLTestResult;
import edu.stanford.smi.protegex.owl.testing.OWLTestResult;
import edu.stanford.smi.protegex.owl.testing.RDFPropertyTest;

import java.util.Collections;
import java.util.List;

/**
 * @author Holger Knublauch
 */
public class PropertyDomainEmptyTest extends AbstractOWLTest
        implements RDFPropertyTest {

    public PropertyDomainEmptyTest() {
        super(SANITY_GROUP, "Domain of a property should not be empty");
    }


    public static boolean fails(RDFProperty property) {
        return property.getSuperpropertyCount() == 0 && property.getUnionDomain(true).isEmpty();
    }


    public List test(RDFProperty property) {
        if (fails(property)) {
            return Collections.singletonList(new DefaultOWLTestResult("The property " +
                    property.getBrowserText() + " has an empty domain and thus cannot be used anywhere.",
                    property,
                    OWLTestResult.TYPE_WARNING,
                    this));
        }
        return Collections.EMPTY_LIST;
    }
}
