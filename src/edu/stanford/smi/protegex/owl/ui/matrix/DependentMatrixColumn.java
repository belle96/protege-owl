package edu.stanford.smi.protegex.owl.ui.matrix;

import edu.stanford.smi.protegex.owl.model.RDFResource;

/**
 * A MatrixColumn that needs to be closed if a certain instance has been deleted.
 * This is typically implemented by columns that represent values of a certain property,
 * so that if the property is being deleted.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface DependentMatrixColumn extends MatrixColumn {

    /**
     * Called after any RDFResource has been deleted from the ontology.
     *
     * @param instance the instance that has been deleted
     * @return true  if this column must be removed from the table
     */
    boolean isDependentOn(RDFResource instance);
}
