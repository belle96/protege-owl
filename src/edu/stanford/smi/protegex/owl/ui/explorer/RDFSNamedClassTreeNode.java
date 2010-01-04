package edu.stanford.smi.protegex.owl.ui.explorer;

import edu.stanford.smi.protege.util.LazyTreeNode;
import edu.stanford.smi.protegex.owl.model.OWLIntersectionClass;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class RDFSNamedClassTreeNode extends ExplorerTreeNode {

    public RDFSNamedClassTreeNode(LazyTreeNode parent, RDFSNamedClass namedClass, ExplorerFilter filter) {
        super(parent, namedClass, filter);
    }


    protected List createChildObjects() {
        RDFSNamedClass namedClass = (RDFSNamedClass) getRDFSClass();
        List results = new ArrayList();
        Collection superclasses = namedClass.getSuperclasses(false);
        if (filter.getUseInferredSuperclasses() && namedClass instanceof OWLNamedClass) {
            superclasses = ((OWLNamedClass) namedClass).getInferredSuperclasses();
        }
        Iterator it = superclasses.iterator();
        while (it.hasNext()) {
            RDFSClass superclass = (RDFSClass) it.next();
            if (superclass instanceof OWLIntersectionClass && superclass.getSuperclasses(false).contains(namedClass)) {  // Equivalent
                OWLIntersectionClass intersectionClass = (OWLIntersectionClass) superclass;
                Iterator ot = intersectionClass.getOperands().iterator();
                while (ot.hasNext()) {
                    RDFSClass operand = (RDFSClass) ot.next();
                    if (!results.contains(operand)) {
                        results.add(operand);
                    }
                }
            }
            else {
                if (!results.contains(superclass)) {
                    results.add(superclass);
                }
            }
        }
        return results;
    }
}
