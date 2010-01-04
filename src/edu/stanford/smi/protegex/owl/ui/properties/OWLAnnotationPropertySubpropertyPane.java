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
public class OWLAnnotationPropertySubpropertyPane extends OWLSubpropertyPane {

	public OWLAnnotationPropertySubpropertyPane(OWLModel owlModel) {
		super(owlModel);
	}


	protected String getHeaderLabel() {
		return "Annotation Properties";
	}


	protected Icon getHeaderIcon() {
		return OWLIcons.getImageIcon("AnnotationOWLDatatypeProperty");
	}


	protected OWLPropertySubpropertyRoot createRoot() {
		return new OWLAnnotationPropertySubpropertyRoot(getOWLModel());
	}


	protected Collection getActions() {
		ArrayList actions = new ArrayList();
		actions.add(getCreateAnnotationOWLObjectPropertyAction());
		actions.add(getCreateAnnotationOWLDatatypePropertyAction());
		actions.add(getCreateAnnotationPropertyAction());
		actions.add(getCreateSubpropertyAction());
		return actions;
	}
}

