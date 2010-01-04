package edu.stanford.smi.protegex.owl.model.event;

import edu.stanford.smi.protege.event.SlotEvent;
import edu.stanford.smi.protege.event.SlotListener;

/**
 * An interface that wraps the core Protege SlotListener to declare
 * those methods deprecated that should not be used with OWL.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface ProtegeSlotListener extends SlotListener {


    /**
     * @see PropertyListener#unionDomainClassAdded
     * @deprecated
     */
    void templateSlotClsAdded(SlotEvent event);


    /**
     * @see PropertyListener#unionDomainClassRemoved
     * @deprecated
     */
    void templateSlotClsRemoved(SlotEvent event);


    /**
     * @see PropertyListener#subpropertyAdded
     * @deprecated
     */
    void directSubslotAdded(SlotEvent event);


    /**
     * @see PropertyListener#subpropertyRemoved
     * @deprecated
     */
    void directSubslotRemoved(SlotEvent event);


    /**
     * @deprecated not supported in OWL
     */
    void directSubslotMoved(SlotEvent event);


    /**
     * @see PropertyListener#superpropertyAdded
     * @deprecated
     */
    void directSuperslotAdded(SlotEvent event);


    /**
     * @see PropertyListener#superpropertyRemoved
     * @deprecated
     */
    void directSuperslotRemoved(SlotEvent event);
}
