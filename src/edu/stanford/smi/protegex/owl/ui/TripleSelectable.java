package edu.stanford.smi.protegex.owl.ui;

import java.util.Collection;
import java.util.List;

/**
 * A common interface for user interface components that can select
 * triples from an ontology.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface TripleSelectable extends edu.stanford.smi.protege.util.Selectable {


    /**
     * Gets triples with an empty object or subject, indicating the typical values
     * of this.  For example, if this component displays the rdfs:comment of the
     * resource Person, then the result would be the Triple (Person, rdfs:comment, null).
     *
     * @return a List of Triples
     */
    List getPrototypeTriples();


    /**
     * Gets the selected triples in an order that is meaningful to the component.
     *
     * @return a List of Triples
     */
    List getSelectedTriples();


    /**
     * Attempts to select given Triples in this.  The method may just do nothing
     * if none of the triple is not shown.
     * @param triples  the Triples to show
     */
    void setSelectedTriples(Collection triples);
}
