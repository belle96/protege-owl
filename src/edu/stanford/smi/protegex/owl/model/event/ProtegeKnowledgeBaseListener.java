package edu.stanford.smi.protegex.owl.model.event;

import edu.stanford.smi.protege.event.KnowledgeBaseEvent;
import edu.stanford.smi.protege.event.KnowledgeBaseListener;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface ProtegeKnowledgeBaseListener extends KnowledgeBaseListener {

    /**
     * @see ModelListener#classCreated
     * @deprecated
     */
    void clsCreated(KnowledgeBaseEvent event);


    /**
     * @see ModelListener#classDeleted
     * @deprecated
     */
    void clsDeleted(KnowledgeBaseEvent event);


    /**
     * @deprecated not needed
     */
    void defaultClsMetaClsChanged(KnowledgeBaseEvent event);


    /**
     * @deprecated not needed
     */
    void defaultFacetMetaClsChanged(KnowledgeBaseEvent event);


    /**
     * @deprecated not needed
     */
    void defaultSlotMetaClsChanged(KnowledgeBaseEvent event);


    /**
     * @deprecated not needed
     */
    void facetCreated(KnowledgeBaseEvent event);


    /**
     * @deprecated not needed
     */
    void facetDeleted(KnowledgeBaseEvent event);


    /**
     * @see ModelListener#resourceNameChanged
     * @deprecated
     */
    void frameNameChanged(KnowledgeBaseEvent event);


    /**
     * @see ModelListener#individualCreated
     * @deprecated
     */
    void instanceCreated(KnowledgeBaseEvent event);


    /**
     * @see ModelListener#individualDeleted
     * @deprecated
     */
    void instanceDeleted(KnowledgeBaseEvent event);


    /**
     * @see ModelListener#propertyCreated
     * @deprecated
     */
    void slotCreated(KnowledgeBaseEvent event);


    /**
     * @see ModelListener#propertyDeleted
     * @deprecated
     */
    void slotDeleted(KnowledgeBaseEvent event);
}
