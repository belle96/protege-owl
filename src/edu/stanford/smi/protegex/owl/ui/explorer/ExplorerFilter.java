package edu.stanford.smi.protegex.owl.ui.explorer;

import edu.stanford.smi.protegex.owl.model.RDFSClass;

/**
 * An interface for objects that define the policy by which child nodes
 * are added to an ExplorerNode.  Objects of this type essentially determine
 * whether a child node shall appear or not, and whether asserted or inferred
 * class relationships shall be taken into account.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface ExplorerFilter {

    /**
     * Checks if the tree should display inferred relationships instead
     * of asserted ones.
     *
     * @return true  if inferred superclasses shall be displayed
     */
    boolean getUseInferredSuperclasses();


    /**
     * Checks whether a given superclass (childClass) shall be be displayed
     * as a child of a given subclass node (parentClass).
     *
     * @param parentClass the class displayed by the parent node
     * @param childClass  the class displayed by the potential child node
     * @return true if childClass shall be used as child node for parentClass
     */
    boolean isValidChild(RDFSClass parentClass, RDFSClass childClass);
}
