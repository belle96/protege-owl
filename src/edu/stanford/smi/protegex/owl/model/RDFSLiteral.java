package edu.stanford.smi.protegex.owl.model;

import edu.stanford.smi.protegex.owl.model.visitor.Visitable;

/**
 * An interface to represent an RDF/XML Schema literal.
 * This encapsulates the value, the datatype and the language tag.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface RDFSLiteral extends Comparable<RDFSLiteral>, RDFObject, Visitable {


    /**
     * Gets the value as a boolean.
     *
     * @return the boolean value
     */
    boolean getBoolean();


    /**
     * Gets the appropriate byte array if the value has datatype xsd:base64Binary
     *
     * @return the byte array, I guess
     */
    byte[] getBytes();


    /**
     * Gets the RDFSDatatype of this value.
     *
     * @return the RDFSDatatype
     */
    RDFSDatatype getDatatype();

    double getDouble();

    float getFloat();

    short getShort();


    /**
     * Gets the value as an int.
     *
     * @return the int value
     */
    int getInt();


    /**
     * Gets the language if it has been defined for this.
     *
     * @return a language or null
     */
    String getLanguage();


    long getLong();


    /**
     * If the datatype of this is one of the default datatypes, which can be
     * optimized by Protege, then this returns an optimized value.
     *
     * @return null or a Boolean, Integer, Float, or String
     */
    Object getPlainValue();
    
    String getRawValue();


    String getString();

}
