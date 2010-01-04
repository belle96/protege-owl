package edu.stanford.smi.protegex.owl.testing.sanity;

import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.testing.*;

import java.util.*;

/**
 * @author Holger Knublauch
 */
public class PropertyDomainRedundancyTest extends AbstractOWLTest
        implements RDFPropertyTest, RepairableOWLTest {

    public PropertyDomainRedundancyTest() {
        super(SANITY_GROUP, "Domain of a property should not contain redundant classes");
    }


    public static Collection fails(OWLObjectProperty slot) {
        Collection results = new HashSet();
        Collection domain = new HashSet(slot.getUnionDomain());
        if (domain.size() >= 2) {
            for (Iterator it = domain.iterator(); it.hasNext();) {
                RDFSClass subCls = (RDFSClass) it.next();
                for (Iterator oit = domain.iterator(); oit.hasNext();) {
                    RDFSClass superCls = (RDFSClass) oit.next();
                    if (!superCls.equals(subCls) && subCls.getSuperclasses(true).contains(superCls)) {
                        results.add(subCls);
                    }
                }
            }
        }
        return results;
    }


    public boolean repair(OWLTestResult testResult) {
        RDFResource host = testResult.getHost();
        if (host instanceof OWLObjectProperty) {
            OWLObjectProperty slot = (OWLObjectProperty) host;
            Collection clses = fails(slot);
            for (Iterator it = clses.iterator(); it.hasNext();) {
                RDFSClass cls = (RDFSClass) it.next();
                slot.removeUnionDomainClass(cls);
            }
            return fails(slot).isEmpty();
        }
        return false;
    }


    public List test(RDFProperty property) {
        if (property instanceof OWLObjectProperty) {
            Collection failClses = fails((OWLObjectProperty) property);
            if (!failClses.isEmpty()) {
                String str = failClses.size() > 1 ? "es " : " ";
                for (Iterator it = failClses.iterator(); it.hasNext();) {
                    RDFSClass cls = (RDFSClass) it.next();
                    str += cls.getBrowserText();
                    if (it.hasNext()) {
                        str += ", ";
                    }
                }
                return Collections.singletonList(new DefaultOWLTestResult("The domain of " +
                        property.getBrowserText() + " contains the redundant class" +
                        str + ".",
                        property,
                        OWLTestResult.TYPE_WARNING,
                        this));
            }
        }
        return Collections.EMPTY_LIST;
    }
}
