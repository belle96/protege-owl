package edu.stanford.smi.protegex.owl.model;


/**
 * An OWL property with objects as values.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface OWLObjectProperty extends OWLProperty {

    void addUnionRangeClass(RDFSClass rangeClass);


    boolean isSymmetric();


    boolean isTransitive();


    void removeUnionRangeClass(RDFSClass rangeClass);


    void setSymmetric(boolean value);


    void setTransitive(boolean value);
}
