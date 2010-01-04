package edu.stanford.smi.protegex.owl.model;

import edu.stanford.smi.protegex.owl.model.visitor.Visitable;

/**
 * The common type of RDFSLiteral and RDFResource.
 * This can be used to ensure type safety for variables and method parameters.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface RDFObject extends Visitable {

    String getBrowserText();


    /**
     * Determines whether or not the specified class is structurally
     * equal to this class.  Note that this does not test for
     * structural equivalence using structural subsumption tests.
     *
     * @param object The class to test against.
     * @return <code>true</code> if the class is structurally equal
     *         to this, <code>false</code> if the class is not structurally
     *         equal to this.
     */
    boolean equalsStructurally(RDFObject object);

}
