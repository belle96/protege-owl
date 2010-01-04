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
public class OWLDatatypePropertySubpropertyPane extends OWLSubpropertyPane {

	public OWLDatatypePropertySubpropertyPane(OWLModel owlModel) {
		super(owlModel);
	}


	protected OWLPropertySubpropertyRoot createRoot() {
		return new OWLDatatypePropertySubpropertyRoot(getOWLModel());
	}


	protected String getHeaderLabel() {
		return "Datatype Properties";
	}


	protected Icon getHeaderIcon() {
		return OWLIcons.getImageIcon("OWLDatatypeProperty");
	}


	protected Collection getActions() {
		ArrayList actions = new ArrayList();
		actions.add(getCreateOWLDatatypePropertyAction());
		Action createSubPropertyAction = getCreateSubpropertyAction();
		createSubPropertyAction.putValue(Action.SMALL_ICON, OWLIcons.getCreatePropertyIcon("DatatypeSubProperty"));
		actions.add(createSubPropertyAction);
		getDeletePropertyAction().putValue(Action.SMALL_ICON, OWLIcons.getDeleteIcon("OWLDatatypeProperty"));
		return actions;
	}
}

