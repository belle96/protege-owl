package edu.stanford.smi.protegex.owl.ui.existential;

import edu.stanford.smi.protege.ui.FrameComparator;
import edu.stanford.smi.protegex.owl.model.*;

import java.util.*;

/**
 * A utility class to compute transitive relationships between classes.
 *
 * @deprecated This class should not be used - it has been replaced
 * with ExistentialFillerProvider
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class Existential {

    /**
     * Computes the collection of all NamedClses which are related to cls
     * through a direct someValuesFrom restriction for a given property.
     *
     * @param cls      the named class to start at
     * @param property the (transitive) property to check
     * @return a List of OWLNamedClass instances - sorted alphabetically
     */
    public static List getExistentialDependents(OWLNamedClass cls, OWLObjectProperty property) {
        List result = new ArrayList();
        for (Iterator it = cls.getSuperclasses(true).iterator(); it.hasNext();) {
            RDFSClass superClass = (RDFSClass) it.next();
            if (superClass.getSuperclasses(false).contains(cls)) {
                if (superClass instanceof OWLIntersectionClass) {
                    getExistentialDependents(result, (OWLIntersectionClass) superClass, property);
                }
                else {
                    perhapsAdd(result, superClass, property);
                }
            }
            else {
                perhapsAdd(result, superClass, property);
            }
        }
        Collections.sort(result, new FrameComparator());
        return result;
    }


    private static void getExistentialDependents(List result,
                                                 OWLIntersectionClass superclass,
                                                 OWLObjectProperty property) {
        for (Iterator oit = superclass.getOperands().iterator(); oit.hasNext();) {
            RDFSClass rdfsClass = (RDFSClass) oit.next();
            perhapsAdd(result, rdfsClass, property);
        }
    }


    private static void perhapsAdd(List result, RDFSClass superclass, OWLObjectProperty property) {
        if (superclass instanceof OWLSomeValuesFrom) {
            OWLSomeValuesFrom someRestriction = (OWLSomeValuesFrom) superclass;
            final RDFProperty restrictedProperty = someRestriction.getOnProperty();
            if (property.equals(restrictedProperty) ||
                    restrictedProperty.getSuperproperties(true).contains(property)) {
                RDFSClass c = (RDFSClass) someRestriction.getFiller();
                if (c instanceof OWLNamedClass) {
                    if (!result.contains(c)) {
                        result.add(c);
                        // Remove any superclasses of c
                        Collection supers = c.getSuperclasses(true);
                        for (Iterator it = result.iterator(); it.hasNext();) {
                            if (supers.contains(it.next())) {
                                it.remove();
                            }
                        }
                    }
                }
            }
        }
    }


    public static OWLSomeValuesFrom getDirectExistentialRelation(OWLNamedClass parentClass,
                                                                 OWLObjectProperty property,
                                                                 OWLNamedClass childClass) {
        for (Iterator it = parentClass.getSuperclasses(false).iterator(); it.hasNext();) {
            RDFSClass superclass = (RDFSClass) it.next();
            if (superclass instanceof OWLSomeValuesFrom) {
                OWLSomeValuesFrom someRestriction = (OWLSomeValuesFrom) superclass;
                if (someRestriction.getOnProperty().equals(property) && someRestriction.getFiller().equals(childClass)) {
                    return someRestriction;
                }
            }
        }
        return null;
    }


    public static boolean isDirectExistentialDependent(OWLNamedClass parentClass,
                                                       OWLObjectProperty property,
                                                       OWLNamedClass childClass) {
        return getDirectExistentialRelation(parentClass, property, childClass) != null;
    }


    public static void removeExistentialDependent(OWLNamedClass parentClass,
                                                  OWLObjectProperty property,
                                                  OWLNamedClass childClass) {
        parentClass.removeSuperclass(getDirectExistentialRelation(parentClass, property, childClass));
    }
}
