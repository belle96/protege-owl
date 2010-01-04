package edu.stanford.smi.protegex.owl.ui.classform.form;

import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.model.util.ClosureAxiomFactory;
import edu.stanford.smi.protegex.owl.ui.cls.SwitchableType;
import edu.stanford.smi.protegex.owl.ui.widget.ClassFormWidget;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ClassFormSwitchableType implements SwitchableType {

    public String getButtonText() {
        return "Form View";
    }


    /**
     * Gets the list of all classes that need to checked for the sufficiently
     * expressive test.  This includes all superclasses and operands of
     * equivalent intersections.
     *
     * @param namedClass the named class
     * @return the "parent" classes
     */
    private List getParentClasses(RDFSNamedClass namedClass) {
        List classes = new ArrayList();
        Iterator it = namedClass.getSuperclasses(true).iterator();
        while (it.hasNext()) {
            RDFSClass rdfsClass = (RDFSClass) it.next();
            if (rdfsClass instanceof OWLIntersectionClass) {
                classes.addAll(((OWLIntersectionClass) rdfsClass).getOperands());
            }
            else {
                classes.add(rdfsClass);
            }
        }
        return classes;
    }


    public Class getWidgetClassType() {
        return ClassFormWidget.class;
    }


    /**
     * Checks if a given OWLAllValuesFrom restriction is a closure axiom for
     * any existential restriction from a given list.
     *
     * @param namedClass    the named class hosting the axiom/classes
     * @param classes       the parent classes of namedClass
     * @param allValuesFrom the potential closure axiom
     * @return true if allValuesFrom is a closure axiom
     */
    private boolean isClosureAxiom(OWLNamedClass namedClass, List classes, OWLAllValuesFrom allValuesFrom) {
        boolean existentialExists = false;
        boolean closed = false;
        Iterator es = classes.iterator();
        while (es.hasNext()) {
            RDFSClass e = (RDFSClass) es.next();
            if (e instanceof OWLExistentialRestriction) {
                existentialExists = true;
                OWLExistentialRestriction ex = (OWLExistentialRestriction) e;
                OWLAllValuesFrom closure = ClosureAxiomFactory.getClosureAxiom(namedClass, ex);
                if (allValuesFrom.equals(closure)) {
                    closed = true;
                    break;
                }
            }
        }
        return existentialExists && closed;
    }


    public boolean isSufficientlyExpressive(RDFSNamedClass namedClass) {
        if (namedClass instanceof OWLNamedClass) {
            OWLNamedClass oc = (OWLNamedClass) namedClass;
            if (oc.getEquivalentClasses().size() > 1) {
                return false;
            }
            List classes = getParentClasses(namedClass);
            Iterator cs = classes.iterator();
            while (cs.hasNext()) {
                RDFSClass cls = (RDFSClass) cs.next();
                if (cls instanceof OWLLogicalClass || cls instanceof OWLCardinalityBase) {
                    return false;
                }
                if (cls instanceof OWLAllValuesFrom) {
                    if (!isClosureAxiom(oc, classes, (OWLAllValuesFrom) cls)) {
                        return false;
                    }
                }
            }
            return true;
        }
        else {
            return false;
        }
    }


    public boolean isSuitable(OWLModel owlModel) {
        return true;
    }
}
