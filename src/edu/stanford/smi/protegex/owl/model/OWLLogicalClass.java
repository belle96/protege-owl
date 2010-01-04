package edu.stanford.smi.protegex.owl.model;


/**
 * The base interface of intersection, union and complement metaclasses.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface OWLLogicalClass extends OWLAnonymousClass {

    /**
     * Gets the OWL system property that is used to store the operands of this
     * logical class type.  Depending on the subclass, these values are either
     * owl:complementOf, owl:intersectionOf or owl:unionOf
     *
     * @return the operands property
     */
    RDFProperty getOperandsProperty();


    /**
     * Gets the (unicode) character used for expressions of this type.
     *
     * @return the symbol
     */
    char getOperatorSymbol();
}
