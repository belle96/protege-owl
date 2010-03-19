package edu.stanford.smi.protegex.owl.model.framestore;

import java.util.Collection;

import edu.stanford.smi.protege.exception.ModificationException;
import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Facet;
import edu.stanford.smi.protege.model.Frame;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.model.framestore.FrameStoreAdapter;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.ProtegeNames;

public class ProtegeReadOnlyFrameStore extends FrameStoreAdapter {
	private OWLModel owlModel;
	private Slot readOnlySlot;
	
	public ProtegeReadOnlyFrameStore(OWLModel owlModel) {
		this.owlModel = owlModel;
	}
	
	private Slot getReadOnlySlot() {
		if (readOnlySlot == null) {
			readOnlySlot = ((KnowledgeBase) owlModel).getSlot(ProtegeNames.getReadOnlySlotName());
		}
		return readOnlySlot;
	}
	
	private void  checkReadOnly(Frame frame) {
		if (getReadOnlySlot() == null) {
			return;
		}
		Collection values = getDelegate().getDirectOwnSlotValues(frame, getReadOnlySlot());
		if (values == null || values.isEmpty()) {
			return;
		}
		for (Object value : values) {
			if (value instanceof Boolean && ((Boolean) value).booleanValue()) {
				throw new ModificationException("Not allowed to modify readonly entities");
			}
		}
	}
	
	@Override
	public void moveDirectOwnSlotValue(Frame frame, Slot slot, int from, int to) {
		checkReadOnly(frame);
		super.moveDirectOwnSlotValue(frame, slot, from, to);
	}
	
	@Override
	public void moveDirectType(Instance instance, Cls type, int index) {
		checkReadOnly(instance);
		super.moveDirectType(instance, type, index);
	}
	
	@Override
	public void setDirectOwnSlotValues(Frame frame, Slot slot, Collection values) {
		checkReadOnly(frame);
		super.setDirectOwnSlotValues(frame, slot, values);
	}
	
	@Override
	public void setDirectTemplateFacetValues(Cls cls, Slot slot, Facet facet, Collection values) {
		checkReadOnly(cls);
		super.setDirectTemplateFacetValues(cls, slot, facet, values);
	}
	
	@Override
	public void setDirectTemplateSlotValues(Cls cls, Slot slot, Collection values) {
		checkReadOnly(cls);
		super.setDirectTemplateSlotValues(cls, slot, values);
	}
	


}
