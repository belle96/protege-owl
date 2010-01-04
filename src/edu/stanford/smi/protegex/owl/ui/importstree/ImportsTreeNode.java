package edu.stanford.smi.protegex.owl.ui.importstree;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import edu.stanford.smi.protege.event.FrameAdapter;
import edu.stanford.smi.protege.event.FrameEvent;
import edu.stanford.smi.protege.event.FrameListener;
import edu.stanford.smi.protege.model.Frame;
import edu.stanford.smi.protege.util.LazyTreeNode;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLOntology;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.impl.OWLUtil;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ImportsTreeNode extends LazyTreeNode {

	private FrameListener _frameListener;

	@SuppressWarnings("deprecation")
	public ImportsTreeNode(LazyTreeNode parent, RDFResource resource) {
		super(parent, resource);	
		resource.addFrameListener(getFrameListener());
	}


	protected LazyTreeNode createNode(Object o) {
		return new ImportsTreeNode(this, (RDFResource) o);
	}


	protected int getChildObjectCount() {
		RDFResource resource = getResource();
		if (resource instanceof OWLOntology) {
			OWLOntology ontology = (OWLOntology) resource;
			return ontology.getImports().size();
		}
		else {
			return 0;
		}
	}


	protected Collection getChildObjects() {
		RDFResource resource = getResource();
		if (resource instanceof OWLOntology) {
			OWLOntology ontology = (OWLOntology) resource;
			return ontology.getImportResources();
		}
		else {
			return Collections.EMPTY_LIST;
		}
	}


	protected Comparator getComparator() {
		return null;
	}


	private RDFResource getResource() {
		return (RDFResource) getUserObject();
	}


	protected FrameListener getFrameListener() {
		if (_frameListener == null) {
			_frameListener = new FrameAdapter() {
			
			    // ToDo This code doesn't belong here.
				@Override
				public void frameReplaced(FrameEvent event) {
					Frame oldFrame = event.getFrame();
					Frame newFrame = event.getNewFrame();
					RDFResource resource = getResource();    	    		
					if (resource != null && resource.equals(oldFrame)) {	
					    
						OWLModel model = resource.getOWLModel();
                        OWLUtil.synchronizeTripleStoreAfterOntologyRename(model, oldFrame.getName(), (OWLOntology) newFrame);
						model.getTripleStoreModel().getTopTripleStore().setOriginalXMLBase(newFrame.getName());
						reload(newFrame);
					}
				}
			};
		}

		return _frameListener;
	}


	@SuppressWarnings("deprecation")
	protected void dispose() {
		super.dispose();
		if (getResource() != null) {
			getResource().removeFrameListener(_frameListener);
		}
	}

}
