package edu.stanford.smi.protegex.owl.model;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface OWLNAryLogicalClass extends OWLLogicalClass {

    /**
     * Adds an operand to this.  Note that the API only supports to add operands
     * during initialization.  Once the logical class is set up, it should not be
     * changed.
     *
     * @param operand the Operand to add
     * @see #removeOperand
     */
    void addOperand(RDFSClass operand);


    /**
     * Gets all operands which are named classes.
     *
     * @return the named operands (without duplicates)
     */
    Collection<RDFSNamedClass> getNamedOperands();


    /**
     * Gets the classes that are combined in this logical statement.
     * In the intersection A & B, this returns the classes A and B.
     *
     * @return a Collection of RDFSClass instances
     */
    Collection<RDFSClass> getOperands();


    boolean hasOperandWithBrowserText(String browserText);


    boolean hasSameOperands(OWLNAryLogicalClass other);


    /**
     * Gets the operand classes as an ordered Iterator.
     *
     * @return an Iterator of RDFSClass objects
     */
    Iterator listOperands();


    /**
     * Removes an operand that was previously added to this logical class.
     *
     * @param operand the operand to remove
     * @see #addOperand
     */
    void removeOperand(RDFSClass operand);
}
