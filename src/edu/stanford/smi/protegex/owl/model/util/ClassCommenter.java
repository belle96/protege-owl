package edu.stanford.smi.protegex.owl.model.util;

import edu.stanford.smi.protegex.owl.model.*;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Jan 16, 2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class ClassCommenter {

	private RDFProperty isCommentedOutProperty;

	public ClassCommenter(OWLModel owlModel) {		
		isCommentedOutProperty = owlModel.getRDFProperty(ProtegeNames.Slot.IS_COMMENTED_OUT);		
	}

	public boolean isCommentedOut(RDFSClass cls) {
		if(isCommentedOutProperty != null) {
			return cls.getPropertyValue(isCommentedOutProperty) != null;
		}
		return false;
	}

	public void setCommentedOut(RDFSClass cls, boolean b) {
		if(isCommentedOutProperty != null) {
			if(b) {
				cls.setPropertyValue(isCommentedOutProperty, Boolean.toString(b));
			}
			else {
				Object val = cls.getPropertyValue(isCommentedOutProperty);
				if(val != null) {
					cls.removePropertyValue(isCommentedOutProperty, val);
				}
			}
		}
	}
}

