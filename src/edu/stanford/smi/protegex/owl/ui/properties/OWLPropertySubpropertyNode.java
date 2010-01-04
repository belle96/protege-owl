package edu.stanford.smi.protegex.owl.ui.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.ui.SlotSubslotNode;
import edu.stanford.smi.protege.util.LazyTreeNode;
import edu.stanford.smi.protegex.owl.model.RDFProperty;

public class OWLPropertySubpropertyNode extends SlotSubslotNode {

	public OWLPropertySubpropertyNode(LazyTreeNode parentNode, Slot parentSlot) {
		super(parentNode, parentSlot);
	}
	
	@Override
	protected Collection getChildObjects() {
		//TODO: make sorting configurable		
		ArrayList<RDFProperty> sortedPropeties = new ArrayList<RDFProperty>(getSlot().getDirectSubslots());
		Collections.sort(sortedPropeties, getComparator());
		
        return sortedPropeties;
    }
	

}
