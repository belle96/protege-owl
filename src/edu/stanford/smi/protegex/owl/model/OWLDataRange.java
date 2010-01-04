package edu.stanford.smi.protegex.owl.model;

import java.util.List;

/**
 * An RDFResource representing an owl:DataRange, i.e. an enumeration of
 * literal values.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface OWLDataRange extends RDFResource {

    /**
     * Gets a collection of the values in this owl:DataRange.
     * The values are either primitive objects or RDFLiterals.
     *
     * @return the values
     */
    RDFList getOneOf();


    /**
     * Gets the values of the owl:oneOf property as RDFSLiterals.
     *
     * @return the values as RDFSLiterals
     */
    List getOneOfValueLiterals();


    /**
     * Gets the values of the owl:oneOf property.
     *
     * @return the values
     */
    List getOneOfValues();


    /**
     * Gets the RDFSDatatype of the first element in this data range.
     *
     * @return the RDFSDatatype or null if this is empty
     */
    RDFSDatatype getRDFDatatype();
}
