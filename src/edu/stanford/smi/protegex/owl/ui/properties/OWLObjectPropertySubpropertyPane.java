package edu.stanford.smi.protegex.owl.ui.properties;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Jan 17, 2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class OWLObjectPropertySubpropertyPane extends OWLSubpropertyPane {


	public OWLObjectPropertySubpropertyPane(OWLModel owlModel) {
		super(owlModel);
	}


	protected OWLPropertySubpropertyRoot createRoot() {
		return new OWLObjectPropertySubpropertyRoot(getOWLModel());
	}


	protected String getHeaderLabel() {
		return "Object properties";
	}


	protected Icon getHeaderIcon() {
		return OWLIcons.getImageIcon("OWLObjectProperty");
	}


	protected Collection getActions() {
		ArrayList actions = new ArrayList();
		actions.add(getCreateOWLObjectPropertyAction());
		actions.add(getCreateSubpropertyAction());
		getDeletePropertyAction().putValue(Action.SMALL_ICON, OWLIcons.getDeleteIcon("OWLObjectProperty"));
		return actions;
	}
}

