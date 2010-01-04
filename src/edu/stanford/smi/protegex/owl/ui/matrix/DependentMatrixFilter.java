package edu.stanford.smi.protegex.owl.ui.matrix;

import edu.stanford.smi.protegex.owl.model.RDFResource;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface DependentMatrixFilter extends MatrixFilter {

    /**
     * Called after any RDFResource has been deleted from the ontology.
     *
     * @param instance the instance that has been deleted
     * @return true  if the associated table must be removed
     */
    boolean isDependentOn(RDFResource instance);
}
