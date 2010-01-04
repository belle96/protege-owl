package edu.stanford.smi.protegex.owl.model.impl;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protegex.owl.model.OWLLogicalClass;
import edu.stanford.smi.protegex.owl.model.RDFSClass;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * A basic implementation of OWLLogicalClass.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public abstract class AbstractOWLLogicalClass extends AbstractOWLAnonymousClass
        implements OWLLogicalClass {


    public AbstractOWLLogicalClass(KnowledgeBase kb, FrameID id) {
        super(kb, id);
    }


    AbstractOWLLogicalClass() {
    }


    public Collection getDependingClasses() {
        Collection result = new ArrayList();
        addAnonymousClses(result, getOperands());
        return result;
    }


    public void getNestedNamedClasses(Set set) {
        for (Iterator it = getOperands().iterator(); it.hasNext();) {
            Cls operand = (Cls) it.next();
            if (operand instanceof RDFSClass) {
                ((RDFSClass) operand).getNestedNamedClasses(set);
            }
        }
    }


    public abstract Collection getOperands();
}
