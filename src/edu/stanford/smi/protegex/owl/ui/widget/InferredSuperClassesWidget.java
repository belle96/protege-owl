package edu.stanford.smi.protegex.owl.ui.widget;

import java.util.Collection;

import javax.swing.Action;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Facet;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.util.LabeledComponent;
import edu.stanford.smi.protege.widget.ClsListWidget;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;

public class InferredSuperClassesWidget extends ClsListWidget {

	
	@Override
	public void initialize() {
		setLabel("Inferred superclasses");
		super.initialize();	
	}
	
	@Override
	public void setValues(Collection values) {
		Instance cls = getInstance();
		
		if (cls instanceof OWLNamedClass) {
			super.setValues(((OWLNamedClass)cls).getInferredSuperclasses());
		} else {
			super.setValues(values);
		}		
	}

	@Override
	protected void addButtons(LabeledComponent c, Action viewAction) {
		addButton(getViewInstanceAction());
	}	

	/**
	 * This is just tentative. More conditions can be added
	 */
	public static boolean isSuitable(Cls cls, Slot slot, Facet facet) {
		boolean isSuitable;
		if (cls == null || slot == null) {
			isSuitable = false;
		} else {
			if (!(cls.getKnowledgeBase() instanceof OWLModel)) {
				return false;
			}

			if (!cls.isMetaCls() || !(cls instanceof OWLNamedClass)) {
				return false;
			}	

			return ClsListWidget.isSuitable(cls, slot, facet);
		}
		return isSuitable;
	}

}
