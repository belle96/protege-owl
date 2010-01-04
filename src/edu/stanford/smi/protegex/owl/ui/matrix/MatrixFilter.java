package edu.stanford.smi.protegex.owl.ui.matrix;

import edu.stanford.smi.protegex.owl.model.RDFResource;

import java.util.Collection;

/**
 * An interface for objects that can determine whether a certain
 * Frame shall appear in a MatrixTable.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface MatrixFilter {

    /**
     * Gets the values that shall be taken to build up the contents
     * of the matrix.  These values will not be filtered using the isSuitable
     * method.
     *
     * @return the initial values (Frames)
     */
    Collection getInitialValues();


    /**
     * Gets the name of the filter for display purposes (the tab name).
     *
     * @return the name
     */
    String getName();


    /**
     * Checks whether a given Frame meets the requirements of this filter.
     *
     * @param instance the Frame to test
     * @return true  if the Frame shall be included, false otherwise
     */
    boolean isSuitable(RDFResource instance);
}
