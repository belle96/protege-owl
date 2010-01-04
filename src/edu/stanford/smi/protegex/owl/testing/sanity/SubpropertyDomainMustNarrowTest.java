package edu.stanford.smi.protegex.owl.testing.sanity;

import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.testing.AbstractOWLTest;
import edu.stanford.smi.protegex.owl.testing.DefaultOWLTestResult;
import edu.stanford.smi.protegex.owl.testing.OWLTestResult;
import edu.stanford.smi.protegex.owl.testing.RDFPropertyTest;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author Holger Knublauch
 */
public class SubpropertyDomainMustNarrowTest extends AbstractOWLTest
        implements RDFPropertyTest {

    public SubpropertyDomainMustNarrowTest() {
        super(SANITY_GROUP, "Domain of a subproperty can only narrow superproperty");
    }


    public static OWLObjectProperty fails(OWLObjectProperty objectProperty) {
        Collection domain = objectProperty.getUnionDomain();
        for (Iterator it = objectProperty.getSuperproperties(true).iterator(); it.hasNext();) {
            RDFProperty superproperty = (RDFProperty) it.next();
            if (superproperty instanceof OWLObjectProperty) {
                Collection superDomain = superproperty.getUnionDomain();
                if (!superDomain.isEmpty() || superproperty.getSuperpropertyCount() == 0) {
                    if (!isSubClasses(superDomain, domain)) {
                        return (OWLObjectProperty) superproperty;
                    }
                }
            }
        }
        return null;
    }


    // All classes from the sub domain must have at least one superclass
    // in super domain
    private static boolean isSubClasses(Collection superDomain, Collection subDomain) {
        for (Iterator it = subDomain.iterator(); it.hasNext();) {
            RDFSClass subCls = (RDFSClass) it.next();
            if (subCls instanceof RDFSNamedClass) {
                boolean hasSuperCls = false;
                for (Iterator sit = superDomain.iterator(); sit.hasNext();) {
                    RDFSClass superCls = (RDFSClass) sit.next();
                    if (superCls instanceof RDFSNamedClass) {
                        if (subCls.equals(superCls) || subCls.getSuperclasses(true).contains(superCls)) {
                            hasSuperCls = true;
                            break;
                        }
                    }
                }
                if (!hasSuperCls) {
                    return false;
                }
            }
        }
        return true;
    }


    public List test(RDFProperty property) {
        if (property instanceof OWLObjectProperty) {
            OWLProperty failProperty = fails((OWLObjectProperty) property);
            if (failProperty != null) {
                return Collections.singletonList(new DefaultOWLTestResult("The domain of " +
                        property.getBrowserText() + " is not a subset of the domain of its superproperty " +
                        failProperty.getBrowserText() + ".",
                        property,
                        OWLTestResult.TYPE_WARNING,
                        this));
            }
        }
        return Collections.EMPTY_LIST;
    }
}
