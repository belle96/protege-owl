package edu.stanford.smi.protegex.owl.model;

/**
 * A common interface for resources that can be deprecated, i.e.
 * RDFSClasses and RDFProperties.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface Deprecatable {

    /**
     * Checks whether this has been declared deprecated (using owl:DeprecatedClass or
     * owl:DeprecatedProperty).
     *
     * @return true  if this was deprecated
     */
    boolean isDeprecated();


    void setDeprecated(boolean value);
}
