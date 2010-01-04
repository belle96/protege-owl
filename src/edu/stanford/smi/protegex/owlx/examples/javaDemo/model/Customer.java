package edu.stanford.smi.protegex.owlx.examples.javaDemo.model;


/**
 * An extension of Customer_ with extra methods.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface Customer extends Customer_ {

    /**
     * Computes the sum of all products that were purchased by this Customer.
     *
     * @return the sum of all purchases
     */
    public float getPurchasesSum();
}
