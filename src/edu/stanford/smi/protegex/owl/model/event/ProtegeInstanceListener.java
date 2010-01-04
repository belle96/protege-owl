package edu.stanford.smi.protegex.owl.model.event;

import edu.stanford.smi.protege.event.InstanceEvent;
import edu.stanford.smi.protege.event.InstanceListener;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface ProtegeInstanceListener extends InstanceListener {

    /**
     * @see ResourceListener#typeAdded
     * @deprecated
     */
    void directTypeAdded(InstanceEvent event);


    /**
     * @see ResourceListener#typeRemoved
     * @deprecated
     */
    void directTypeRemoved(InstanceEvent event);
}
