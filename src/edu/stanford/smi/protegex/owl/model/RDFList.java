package edu.stanford.smi.protegex.owl.model;

import java.util.List;

/**
 * An RDFResource represents an rdf:List.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface RDFList extends RDFResource {

    /**
     * Appends a value to this list. This method will first find the last
     * RDFList following the rdf:rest links.
     * If this does not have a rdf:first yet, then this will be assigned to it.
     * Otherwise, it will create a new RDFList of the same type like this
     * as rdf:rest to the end of this list, and assigns the given instance as its
     * rdf:first value.
     *
     * @param value the value to append a list node for
     */
    void append(Object value);


    /**
     * Checks whether a given value is among the entries in this list.
     *
     * @param value the value to look for
     * @return true if the values contain value
     */
    boolean contains(Object value);


    Object getFirst();


    /**
     * Gets the rdf:first value of this as an RDFSLiteral.
     * The calling method must make sure that we have indeed a primitive value.
     *
     * @return an RDFSLiteral
     */
    RDFSLiteral getFirstLiteral();


    RDFList getRest();


    /**
     * Gets the start of the RDFList chain containing this.
     * This method basically follows the backward references where this is rdf:rest,
     * and does so recursively until it reaches a node which is never used as rdf:rest
     * anywhere in the OWLModel.
     *
     * @return the start RDFList node (may be this)
     */
    RDFList getStart();


    /**
     * Gets the values in this list as RDFSLiterals.
     * The caller must make sure that only primitive values are currently
     * in the list.
     *
     * @return a List of RDFSLiteral values.
     */
    List getValueLiterals();


    /**
     * Gets the values in this list.
     *
     * @return a List of Object instances
     */
    List getValues();


    /**
     * Checks whether this is eventually terminated with an rdf:rest rdf:nil triple.
     *
     * @return true  if the last entry in this list points to rdf:nil.
     */
    boolean isClosed();


    void setFirst(Object value);


    void setRest(RDFList rest);


    int size();
}
