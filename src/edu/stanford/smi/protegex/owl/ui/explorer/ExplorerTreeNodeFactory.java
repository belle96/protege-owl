package edu.stanford.smi.protegex.owl.ui.explorer;

import edu.stanford.smi.protege.util.LazyTreeNode;
import edu.stanford.smi.protegex.owl.model.OWLLogicalClass;
import edu.stanford.smi.protegex.owl.model.OWLQuantifierRestriction;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ExplorerTreeNodeFactory {

    public static ExplorerTreeNode create(LazyTreeNode parent, RDFSClass cls, ExplorerFilter filter) {
        if (cls instanceof RDFSNamedClass) {
            return new RDFSNamedClassTreeNode(parent, (RDFSNamedClass) cls, filter);
        }
        else if (cls instanceof OWLQuantifierRestriction) {
            return new OWLQuantifierRestrictionTreeNode(parent, (OWLQuantifierRestriction) cls, filter);
        }
        else if (cls instanceof OWLLogicalClass) {
            return new OWLLogicalClassTreeNode(parent, (OWLLogicalClass) cls, filter);
        }
        else {
            return new LeafExplorerTreeNode(parent, cls, filter);
        }
    }
}
