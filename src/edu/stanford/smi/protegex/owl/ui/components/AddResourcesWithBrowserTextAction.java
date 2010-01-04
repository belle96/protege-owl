package edu.stanford.smi.protegex.owl.ui.components;

import java.util.Collection;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.ui.dialogs.DefaultSelectionDialogFactory;

public class AddResourcesWithBrowserTextAction extends AddResourceAction {

	private static final long serialVersionUID = -3841713833632888447L;
	
	public AddResourcesWithBrowserTextAction(
			AddablePropertyValuesComponent component, boolean symmetric) {
		super(component, symmetric);		
	}

	@Override
	protected Collection selectResourcesByType(OWLModel owlModel,
			Collection clses) {
		return new DefaultSelectionDialogFactory().
			selectResourcesWithBrowserTextByType(component, owlModel, clses, "Select Resources..");
	
	}
	

}
