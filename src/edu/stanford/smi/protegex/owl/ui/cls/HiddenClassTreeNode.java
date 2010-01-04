package edu.stanford.smi.protegex.owl.ui.cls;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.util.LazyTreeNode;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFSClass;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Nick Drummond, Medical Informatics Group, University of Manchester
 *         23-Feb-2006
 */
public class HiddenClassTreeNode extends ClassTreeNode {

    public HiddenClassTreeNode(HiddenClassTreeRoot hiddenClassTreeRoot, Cls cls) {
        super(hiddenClassTreeRoot, cls);
    }


    protected LazyTreeNode createNode(Object o) {
        return new ClassTreeNode(this, (Cls) o);
    }

    protected int getChildObjectCount() {
        return getChildObjects().size();
    }

    protected Collection getChildObjects() {
        Cls cls = getCls();
        List result = new ArrayList(HiddenClassTreeRoot.filter(cls.getDirectSubclasses()));
        // Remove all equivalent classes that have other superclasses as well
        if (cls instanceof OWLNamedClass) {
            Iterator equis = ((OWLNamedClass) cls).getEquivalentClasses().iterator();
            while (equis.hasNext()) {
                RDFSClass equi = (RDFSClass) equis.next();
                if (equi instanceof OWLNamedClass && equi.getSuperclassCount() > 1) {
                    result.remove(equi);
                }
            }
        }
        return result;
    }
}
