package edu.stanford.smi.protegex.owl.model.event;

import edu.stanford.smi.protege.event.FrameEvent;
import edu.stanford.smi.protege.event.FrameListener;

/**
 * A wrapper to deprecate most methods of the Protege FrameListener interface.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 * @see PropertyValueListener
 */
public interface ProtegeFrameListener extends FrameListener {


    /**
     * @see PropertyValueListener#browserTextChanged
     * @deprecated
     */
    public void browserTextChanged(FrameEvent event);


    /**
     * @deprecated did not work in Protege anyway
     */
    public void deleted(FrameEvent event);


    /**
     * @see PropertyValueListener#nameChanged
     * @deprecated
     */
    public void nameChanged(FrameEvent event);


    /**
     * @deprecated not supported in OWL
     */
    public void ownFacetAdded(FrameEvent event);


    /**
     * @deprecated not supported in OWL
     */
    public void ownFacetRemoved(FrameEvent event);


    /**
     * @deprecated not supported in OWL
     */
    public void ownFacetValueChanged(FrameEvent event);


    /**
     * @deprecated not needed
     */
    public void ownSlotAdded(FrameEvent event);


    /**
     * @deprecated not needed
     */
    public void ownSlotRemoved(FrameEvent event);


    /**
     * @see PropertyValueListener#propertyValueChanged
     * @deprecated
     */
    public void ownSlotValueChanged(FrameEvent event);


    /**
     * @see PropertyValueListener#visibilityChanged
     * @deprecated
     */
    public void visibilityChanged(FrameEvent event);
}
