package edu.stanford.smi.protegex.owl.ui.explorer;

import edu.stanford.smi.protege.util.LazyTreeNode;
import edu.stanford.smi.protegex.owl.model.OWLComplementClass;
import edu.stanford.smi.protegex.owl.model.OWLLogicalClass;
import edu.stanford.smi.protegex.owl.model.OWLNAryLogicalClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class OWLLogicalClassTreeNode extends ExplorerTreeNode {


    public OWLLogicalClassTreeNode(LazyTreeNode parent, OWLLogicalClass cls, ExplorerFilter filter) {
        super(parent, cls, filter);
    }


    protected List createChildObjects() {
        if (getRDFSClass() instanceof OWLComplementClass) {
            return Collections.singletonList(((OWLComplementClass) getRDFSClass()).getComplement());
        }
        else {
            return new ArrayList(((OWLNAryLogicalClass) getRDFSClass()).getOperands());
        }
    }


    public String toString(boolean expanded) {
        OWLLogicalClass logicalClass = (OWLLogicalClass) getRDFSClass();
        if (expanded) {
            return "" + logicalClass.getOperatorSymbol();
        }
        else {
            return logicalClass.getBrowserText();
        }
    }
}
