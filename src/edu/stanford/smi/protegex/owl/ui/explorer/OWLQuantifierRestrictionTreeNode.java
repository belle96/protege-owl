package edu.stanford.smi.protegex.owl.ui.explorer;

import edu.stanford.smi.protege.util.LazyTreeNode;
import edu.stanford.smi.protegex.owl.model.OWLQuantifierRestriction;
import edu.stanford.smi.protegex.owl.model.OWLRestriction;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;

import java.util.Collections;
import java.util.List;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class OWLQuantifierRestrictionTreeNode extends ExplorerTreeNode {

    public OWLQuantifierRestrictionTreeNode(LazyTreeNode parent,
                                            OWLQuantifierRestriction restriction,
                                            ExplorerFilter filter) {
        super(parent, restriction, filter);
    }


    protected List createChildObjects() {
        RDFResource filler = ((OWLQuantifierRestriction) getRDFSClass()).getFiller();
        if (filler instanceof RDFSClass) {
            return Collections.singletonList(filler);
        }
        else {
            return Collections.EMPTY_LIST;
        }
    }


    public String toString(boolean expanded) {
        String str = super.toString(expanded);
        if (expanded) {
            OWLRestriction restriction = (OWLRestriction) getRDFSClass();
            return /*str.substring(0, 2) + */restriction.getOnProperty().getBrowserText();
        }
        else {
            return str;
        }
    }
}
