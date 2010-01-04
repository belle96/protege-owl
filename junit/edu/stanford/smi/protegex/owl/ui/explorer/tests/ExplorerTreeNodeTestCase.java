package edu.stanford.smi.protegex.owl.ui.explorer.tests;

import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;
import edu.stanford.smi.protegex.owl.ui.explorer.ExplorerFilter;
import edu.stanford.smi.protegex.owl.ui.explorer.ExplorerTreeNode;
import edu.stanford.smi.protegex.owl.ui.explorer.RDFSNamedClassTreeNode;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ExplorerTreeNodeTestCase extends AbstractJenaTestCase {

    public void testNamedSuperclasses() {
        OWLNamedClass parentClass = owlModel.createOWLNamedClass("Parent");
        OWLNamedClass cls = owlModel.createOWLNamedSubclass("Subclass", parentClass);
        ExplorerTreeNode treeNode = new RDFSNamedClassTreeNode(null, cls, new ExplorerFilter() {
            public boolean getUseInferredSuperclasses() {
                return false;
            }


            public boolean isValidChild(RDFSClass parentClass, RDFSClass childClass) {
                return true;
            }
        });
        assertEquals(1, treeNode.getChildCount());
        assertEquals(parentClass, treeNode.getChildNode(0).getRDFSClass());
    }
}
