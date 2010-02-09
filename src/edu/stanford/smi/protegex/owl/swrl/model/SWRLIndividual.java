package edu.stanford.smi.protegex.owl.swrl.model;

import java.util.Set;

import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.RDFResource;

/**
 * The common base interface of all SWRL related classes.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface SWRLIndividual extends OWLIndividual {

    /**
     * Collects all instances (SWRLInstances, classes, etc)
     * that are somehow involved in the subexpression below this.
     *
     * @param set
     */
    void getReferencedInstances(Set<RDFResource> set);
}
