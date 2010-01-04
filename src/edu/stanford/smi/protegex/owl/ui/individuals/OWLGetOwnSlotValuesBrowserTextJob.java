package edu.stanford.smi.protegex.owl.ui.individuals;

import java.util.Collection;

import edu.stanford.smi.protege.model.Frame;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.util.GetOwnSlotValuesBrowserTextJob;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;

public class OWLGetOwnSlotValuesBrowserTextJob extends
		GetOwnSlotValuesBrowserTextJob {

	private static final long serialVersionUID = 5135524417428952393L;

	public OWLGetOwnSlotValuesBrowserTextJob(KnowledgeBase kb, Frame frame,
			Slot slot, boolean directValues) {
		super(kb, frame, slot, directValues);
	}

	@Override
	protected Collection getValues() {
		if (frame instanceof RDFResource && slot instanceof RDFProperty) {
			Collection values = ((RDFResource)frame).getHasValuesOnTypes((RDFProperty)slot);
			values.addAll(super.getValues());
			return values;
		} else {
			return super.getValues();
		}
	}

}
