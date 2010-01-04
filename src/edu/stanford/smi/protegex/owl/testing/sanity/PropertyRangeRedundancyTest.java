package edu.stanford.smi.protegex.owl.testing.sanity;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.testing.*;

import java.util.*;

/**
 * @author Holger Knublauch
 */
public class PropertyRangeRedundancyTest extends AbstractOWLTest
        implements RDFPropertyTest, RepairableOWLTest {

    public PropertyRangeRedundancyTest() {
        super(SANITY_GROUP, "Range of a property should not contain redundant classes");
    }


    public static Collection fails(OWLObjectProperty slot) {
        Collection results = new HashSet();
        Collection range = slot.getUnionRangeClasses();
        if (range.size() >= 2) {
            for (Iterator it = range.iterator(); it.hasNext();) {
                Cls subCls = (Cls) it.next();
                for (Iterator oit = range.iterator(); oit.hasNext();) {
                    Cls superCls = (Cls) oit.next();
                    if (!superCls.equals(subCls) && subCls.hasSuperclass(superCls)) {
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
            Collection newRange = new ArrayList(slot.getUnionRangeClasses());
            newRange.removeAll(clses);
            slot.setUnionRangeClasses(newRange);
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
                    Cls cls = (Cls) it.next();
                    str += cls.getBrowserText();
                    if (it.hasNext()) {
                        str += ", ";
                    }
                }
                return Collections.singletonList(new DefaultOWLTestResult("The range of " +
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
