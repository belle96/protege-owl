package edu.stanford.smi.protegex.owl.testing.sanity;

import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.testing.*;

import java.util.*;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class InversePropertyMustHaveMatchingRangeAndDomainTest extends AbstractOWLTest
        implements RepairableOWLTest, RDFPropertyTest {

    public InversePropertyMustHaveMatchingRangeAndDomainTest() {
        super(SANITY_GROUP, null);
    }


    private static boolean equalCollections(Collection a, Collection b) {
        return a.containsAll(b) && b.containsAll(a);
    }


    public static boolean fails(RDFProperty property) {
        if (property instanceof OWLObjectProperty && ((OWLObjectProperty) property).getInverseProperty() instanceof OWLObjectProperty) {
            OWLObjectProperty inverseSlot = (OWLObjectProperty) property.getInverseProperty();
            if (!property.isDomainDefined() || !inverseSlot.isDomainDefined()) {
                return false;
            }
            else {
                Collection domain = property.getUnionDomain();
                Collection range = property.getUnionRangeClasses();
                Collection inverseDomain = inverseSlot.getUnionDomain();
                Collection inverseRange = inverseSlot.getUnionRangeClasses();
                return !equalCollections(domain, inverseRange) ||
                        !equalCollections(range, inverseDomain);
            }
        }
        else {
            return false;
        }
    }


    public boolean repair(OWLTestResult testResult) {
        OWLProperty property = (OWLProperty) testResult.getHost();
        if (fails(property) && property instanceof OWLObjectProperty &&
                ((OWLObjectProperty) property).getInverseProperty() instanceof OWLObjectProperty) {
            OWLObjectProperty inverseSlot = (OWLObjectProperty) property.getInverseProperty();
            if (property.isDomainDefined() && inverseSlot.isDomainDefined()) {
                Collection inverseDomain = inverseSlot.getUnionDomain();
                Collection inverseRange = inverseSlot.getUnionRangeClasses();
                property.setUnionRangeClasses(inverseDomain);
                Collection oldDomain = new ArrayList(property.getUnionDomain());
                for (Iterator it = oldDomain.iterator(); it.hasNext();) {
                    OWLNamedClass namedCls = (OWLNamedClass) it.next();
                    property.removeUnionDomainClass(namedCls);
                }
                for (Iterator it = inverseRange.iterator(); it.hasNext();) {
                    RDFSClass cls = (RDFSClass) it.next();
                    property.addUnionDomainClass(cls);
                }
            }
        }
        return !fails(property);
    }


    public List test(RDFProperty property) {
        if (fails(property)) {
            return Collections.singletonList(new DefaultOWLTestResult("Inverse properties must have inverse domains and ranges.",
                    property,
                    OWLTestResult.TYPE_ERROR,
                    this));
        }
        else {
            return Collections.EMPTY_LIST;
        }
    }
}
