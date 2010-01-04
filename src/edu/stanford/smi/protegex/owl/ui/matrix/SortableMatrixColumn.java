package edu.stanford.smi.protegex.owl.ui.matrix;

import java.util.Comparator;

/**
 * A MatrixColumn that can be defined as a sort criteria.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface SortableMatrixColumn extends MatrixColumn {

    Comparator getSortComparator();
}
