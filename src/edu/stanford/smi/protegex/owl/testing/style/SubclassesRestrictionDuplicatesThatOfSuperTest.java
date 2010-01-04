package edu.stanford.smi.protegex.owl.testing.style;

import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLRestriction;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.testing.*;

import java.util.*;

/**
 * Tests whether a subclass has a redundant restriction on it.
 *
 * This is currently only implemented as the simple case where the restriction
 * structurally matches that on a superclass.
 *
 * This could be generalised to check if the filler os more general,
 * or if a cardinality is more general etc
 *
 * @author Nick Drummond, Medical Informatics Group, University of Manchester
 *         07-Feb-2006
 */
public class SubclassesRestrictionDuplicatesThatOfSuperTest extends AbstractOWLTest
        implements RDFSClassTest, AutoRepairableOWLTest {

    private static String failedRest;

    public String getDocumentation() {
        return "Find classes that reimplement the same restriction as on a subclass";
    }


    public String getGroup() {
        return "Style";
    }


    public String getName() {
        return "Duplicate Restriction on Superclass";
    }


    public List test(RDFSClass aClass) {
        if (fails(aClass)){
            return Collections.singletonList(
                    new DefaultOWLTestResult("This class duplicates restrictions from parents (" +
                                             failedRest + ")",
                                             aClass,
                                             OWLTestResult.TYPE_WARNING,
                                             this)
            );
        }
        return Collections.EMPTY_LIST;
    }


    public static boolean fails(RDFSClass aClass){
        boolean result = false;
        failedRest = null;
        if (aClass instanceof OWLNamedClass){
            for (Iterator i = getDuplicateRestrictions((OWLNamedClass)aClass); i.hasNext();){
                result = true;
                if (failedRest == null){
                    failedRest = ((OWLRestriction)i.next()).getBrowserText();
                }
                else{
                    failedRest +=  ", " + ((OWLRestriction)i.next()).getBrowserText();
                }
            }
        }
        return result;
    }


    public boolean repair(OWLTestResult testResult) {
        boolean result = false;
        if (testResult.getHost() instanceof OWLNamedClass){
            result = fix((OWLNamedClass)testResult.getHost());
        }
        return result;
    }


    public static boolean fix(OWLNamedClass namedClass){
        boolean result = false;

        for (Iterator i = getDuplicateRestrictions(namedClass); i.hasNext();){
            ((OWLRestriction)i.next()).delete();
            result = true;
        }

        return result;
    }

    /**
     * getRestrictions(true) does not return "overloaded" restrictions
     * @param aClass
     * @return a collection of restrictions (from all of the named superclasses of aClass) - empty if none
     */
    private static Collection getAllInheritedRestrictions(OWLNamedClass aClass){
        Iterator superclasses = aClass.getNamedSuperclasses(true).iterator();
        Collection restrs = new ArrayList();
        while (superclasses.hasNext()){
            RDFSClass superclass = (RDFSClass) superclasses.next();
            if (superclass instanceof OWLNamedClass &&
                !superclass.equals(aClass)){
                restrs.addAll(((OWLNamedClass)superclass).getRestrictions());
            }
        }
        return restrs;
    }

    private static Iterator getDuplicateRestrictions(OWLNamedClass namedClass){
        Collection directRestrictions = namedClass.getRestrictions();
        Collection inheritedRestrictions = getAllInheritedRestrictions(namedClass);
        Collection duplicateRestrictions = new ArrayList();

        for (Iterator i = directRestrictions.iterator(); i.hasNext();){
            OWLRestriction directRestr = (OWLRestriction)i.next();
            for (Iterator j = inheritedRestrictions.iterator(); j.hasNext();){
                OWLRestriction inheritedRestr = (OWLRestriction)j.next();
                if (directRestr.equalsStructurally(inheritedRestr)){
                    duplicateRestrictions.add(directRestr);
                }
            }
        }

        return duplicateRestrictions.iterator();
    }
}
