package edu.stanford.smi.protegex.owl.model.event;

import edu.stanford.smi.protege.event.ClsEvent;
import edu.stanford.smi.protege.event.ClsListener;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface ProtegeClsListener extends ClsListener {

    /**
     * @see ClassListener#instanceAdded
     * @deprecated
     */
    void directInstanceAdded(ClsEvent event);


    /**
     * @see ClassListener#instanceRemoved
     * @deprecated
     */
    void directInstanceRemoved(ClsEvent event);


    /**
     * @see ClassListener#subclassAdded
     * @deprecated
     */
    void directSubclassAdded(ClsEvent event);


    /**
     * @deprecated not supported by OWL
     */
    void directSubclassMoved(ClsEvent event);


    /**
     * @see ClassListener#subclassRemoved
     * @deprecated
     */
    void directSubclassRemoved(ClsEvent event);


    /**
     * @see ClassListener#superclassAdded
     * @deprecated
     */
    void directSuperclassAdded(ClsEvent event);


    /**
     * @see ClassListener#subclassRemoved
     * @deprecated
     */
    void directSuperclassRemoved(ClsEvent event);


    /**
     * @deprecated no OWL equivalent
     */
    void templateFacetAdded(ClsEvent event);


    /**
     * @deprecated no OWL equivalent
     */
    void templateFacetRemoved(ClsEvent event);


    /**
     * @deprecated no OWL equivalent
     */
    void templateFacetValueChanged(ClsEvent event);


    /**
     * @see ClassListener#addedToUnionDomainOf
     * @deprecated
     */
    void templateSlotAdded(ClsEvent event);


    /**
     * @see ClassListener#removedFromUnionDomainOf
     * @deprecated
     */
    void templateSlotRemoved(ClsEvent event);


    /**
     * @deprecated no OWL equivalent
     */
    void templateSlotValueChanged(ClsEvent event);
}
