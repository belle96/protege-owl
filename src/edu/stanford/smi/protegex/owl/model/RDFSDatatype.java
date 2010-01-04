package edu.stanford.smi.protegex.owl.model;

/**
 * An RDF resource representing an XML Schema datatype.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface RDFSDatatype extends RDFResource {

    /**
     * If this is a user-defined datatype, then this method gets the restricted base type.
     * <B>Note: This experimental method should not be used yet.</B>
     *
     * @return the base type or null
     */
    RDFSDatatype getBaseDatatype();


    /**
     * Creates a default value for this datatype (e.g. Integer(0) for xsd:int).
     *
     * @return a default value
     */
    Object getDefaultValue();


    /**
     * Gets the value of the xsd:length facet of this (user-defined) datatype.
     * <B>Note: This experimental method should not be used yet.</B>
     *
     * @return the length of this (string) datatype or -1 if none is defined
     */
    int getLength();


    /**
     * Gets the value of the xsd:maxExclusive facet of this (user-defined) datatype.
     * <B>Note: This experimental method should not be used yet.</B>
     *
     * @return the exclusive maximum value of this datatype or null
     */
    RDFSLiteral getMaxExclusive();


    /**
     * Gets the value of the xsd:maxInclusive facet of this (user-defined) datatype.
     * <B>Note: This experimental method should not be used yet.</B>
     *
     * @return the inclusive maximum value of this datatype or null
     */
    RDFSLiteral getMaxInclusive();


    /**
     * Gets the value of the xsd:maxLength facet of this (user-defined) datatype.
     * <B>Note: This experimental method should not be used yet.</B>
     *
     * @return the maximum length of this (string) datatype or -1 if none is defined
     */
    int getMaxLength();


    /**
     * Gets the value of the xsd:minExclusive facet of this (user-defined) datatype.
     * <B>Note: This experimental method should not be used yet.</B>
     *
     * @return the exclusive minimum value of this datatype or null
     */
    RDFSLiteral getMinExclusive();


    /**
     * Gets the value of the xsd:minInclusive facet of this (user-defined) datatype.
     * <B>Note: This experimental method should not be used yet.</B>
     *
     * @return the inclusive minimum value of this datatype or null
     */
    RDFSLiteral getMinInclusive();


    /**
     * Gets the value of the xsd:minLength facet of this (user-defined) datatype.
     * <B>Note: This experimental method should not be used yet.</B>
     *
     * @return the minimum length of this (string) datatype or -1 if none is defined
     */
    int getMinLength();


    /**
     * Gets the value of the xsd:pattern facet of this (user-defined) datatype.
     * Patterns must be regular expressions.
     * <B>Note: This experimental method should not be used yet.</B>
     *
     * @return the pattern or null if none is defined
     */
    String getPattern();


    /**
     * Checks if this is a numeric type.  This is for example needed to decide
     * whether values shall be edited with left or right alignment.
     *
     * @return true  if this is a numeric datatype (such as xsd:int)
     */
    boolean isNumericDatatype();


    /**
     * Checks if a given would be a valid value for this property.
     * This should check for XML Schema datatype facet violations.
     *
     * @param object the potential value
     * @return true if the object would be valid
     */
    boolean isValidValue(RDFSLiteral object);
}
