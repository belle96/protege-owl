package edu.stanford.smi.protegex.owl.model;


/**
 * The base class of all OWL restriction classes.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface OWLRestriction extends OWLAnonymousClass {


    /**
     * Checks the filler from a textual expression.  This should be called prior to
     * assigning a new filler value.
     *
     * @param value the potential filler value
     * @throws Exception to indicate a parse exception
     */
    void checkFillerText(String value) throws Exception;


    /**
     * Gets the Unicode operator character that is typically used to represent this
     * type of restriction.
     *
     * @return the operator char
     */
    char getOperator();


    /**
     * Gets the Slot that is used to store the filler at this kind of restriction
     * (e.g., owl:cardinality).
     *
     * @return the filler slot
     */
    RDFProperty getFillerProperty();


    /**
     * Gets the filler of this restriction for display purposes.
     *
     * @return the filler text (never null)
     */
    String getFillerText();


    /**
     * Gets the Slot that is restricted by this restriction.
     *
     * @return the value of the owl:onProperty slot at this restriction
     */
    RDFProperty getOnProperty();


    /**
     * Checks whether this restriction has been completely defined already.
     * If this is false, then the user still has to define the restriction values.
     *
     * @return true  if this is completely defined
     */
    boolean isDefined();


    /**
     * Sets the filler from a (valid) textual expression.
     *
     * @param value the new filler value
     * @throws Exception to indicate a parse exception
     */
    void setFillerText(String value) throws Exception;


    /**
     * Sets the restricted property at this restriction.
     *
     * @param property the RDFProperty to restrict
     */
    void setOnProperty(RDFProperty property);
}
