package edu.stanford.smi.protegex.owl.ui.widget;

import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protegex.owl.model.OWLModel;

/**
 * A convenience wrapper of the standard AbstractTabWidget class to make it
 * easier accessible for OWL programmers.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public abstract class AbstractTabWidget extends edu.stanford.smi.protege.widget.AbstractTabWidget {

    /**
     * @see #getOWLModel
     * @deprecated use getOWLModel() instead
     */
    public KnowledgeBase getKnowledgeBase() {
        return super.getKnowledgeBase();
    }


    public OWLModel getOWLModel() {
        return (OWLModel) super.getKnowledgeBase();
    }
}
