package edu.stanford.smi.protegex.owl.testing.style;

import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.impl.OWLUtil;
import edu.stanford.smi.protegex.owl.testing.*;

import java.util.*;

/**
 * Normalisation rule - all primitive siblings should be disjoint.
 *
 * This test reports at the superclass level (ie where prim  subs aren't disjoint)
 * to keep the number of results down.
 *
 * Fixing this uses <code>OWLUtil.ensureSubclassesDisjoint()</code> to add
 * required disjoints in.
 *
 * @author Nick Drummond, Medical Informatics Group, University of Manchester
 *         06-Feb-2006
 */
public class PrimitiveSubclassesDisjointTest extends AbstractOWLTest
        implements RDFSClassTest, AutoRepairableOWLTest {

    public String getDocumentation() {
        return "Checks that all primitive subclasses are disjoint";
    }


    public String getGroup() {
        return "Style";
    }


    public String getName() {
        return "Normalisation: Primitive Subclasses Disjoint";
    }


    public List test(RDFSClass aClass) {
        Collection primSubs = getPrimitiveSubs(aClass);

        if (primSubs.size() > 1){

            Collection idealDisjoints = new ArrayList(primSubs);
            Collection brokenDisjoints = new ArrayList();

            for (Iterator i = primSubs.iterator(); i.hasNext();){
                OWLNamedClass sub = (OWLNamedClass)i.next();
                idealDisjoints.remove(sub);
                if (!sub.getDisjointClasses().containsAll(idealDisjoints)){
                    brokenDisjoints.add(sub);
                }
                idealDisjoints.add(sub);
            }

            if (!brokenDisjoints.isEmpty()){

                String brokenDisjointsText = "Missing disjoints on primitive subclasses:";

                for (Iterator i=brokenDisjoints.iterator(); i.hasNext();){
                    brokenDisjointsText += " " + ((OWLNamedClass)i.next()).getBrowserText();
                }

                return Collections.singletonList(
                        new DefaultOWLTestResult (brokenDisjointsText,
                                                  aClass,
                                                  OWLTestResult.TYPE_WARNING,
                                                  this)
                );
            }
        }
        return Collections.EMPTY_LIST;
    }

    private Collection getPrimitiveSubs(RDFSClass aClass) {

        Collection primNamedSubs = aClass.getNamedSubclasses();

        for (Iterator j = primNamedSubs.iterator(); j.hasNext();){
            RDFSClass namedSub = (RDFSClass) j.next();
            if ((namedSub instanceof OWLNamedClass) &&
                (namedSub.isVisible())){
                if (((OWLNamedClass)namedSub).isDefinedClass()){
                    j.remove();
                }
            }
            else{
                j.remove();
            }
        }

        return primNamedSubs;
    }

    public boolean repair(OWLTestResult testResult) {
        OWLUtil.ensureSubclassesDisjoint((OWLNamedClass)testResult.getHost());
        return true;
    }
}
