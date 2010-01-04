package edu.stanford.smi.protegex.owl.model.event;

import java.util.Collection;

import edu.stanford.smi.protege.event.FrameAdapter;
import edu.stanford.smi.protege.event.FrameEvent;
import edu.stanford.smi.protegex.owl.model.RDFNames;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ResourceAdapter extends FrameAdapter implements ResourceListener {

    public void typeAdded(RDFResource resource, RDFSClass type, FrameEvent event) {
    	typeAdded(resource, type);
    }
	
    public void typeAdded(RDFResource resource, RDFSClass type) {
        // Do nothing
    }


    public void typeRemoved(RDFResource resource, RDFSClass type, FrameEvent event) {
    	typeRemoved(resource, type);
    }

    
    public void typeRemoved(RDFResource resource, RDFSClass type) {
        // Do nothing
    }

    
    /************* Deprecated methods **************/
    
    /*
     * @deprecated Use frame listeners directly if you know what you are doing
     * 				and are going to look below the advertised protege owl api interface.
     */
    @Deprecated
	@SuppressWarnings("unchecked")
	public void ownSlotValueChanged(FrameEvent event) {
		if (event.getSlot().getName().equals(RDFNames.Slot.TYPE) && 
				event.getFrame() instanceof RDFResource) {
			RDFResource resource = (RDFResource) event.getFrame();
			Collection types = resource.getRDFTypes();
			Collection oldTypes = event.getOldValues();
			
			for (Object newType : types) {
				if (newType instanceof RDFSClass) {
					if (oldTypes == null || !oldTypes.contains(newType)) {
						typeAdded(resource, (RDFSClass) newType, event);
					}					
				}
			}
			
			if (oldTypes == null) {
				return;
			}
			
			for (Object oldType : oldTypes) {
				if (oldType instanceof RDFSClass && !types.contains(oldType)) {					
					typeRemoved(resource, (RDFSClass) oldType, event);					
				}
			}
		}
	}
}
