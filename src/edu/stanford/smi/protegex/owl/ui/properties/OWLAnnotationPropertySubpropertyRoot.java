package edu.stanford.smi.protegex.owl.ui.properties;

import edu.stanford.smi.protege.ui.FrameComparator;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.ui.widget.OWLUI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Jan 17, 2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class OWLAnnotationPropertySubpropertyRoot extends OWLPropertySubpropertyRoot {

	public OWLAnnotationPropertySubpropertyRoot(OWLModel owlModel) {
		super(owlModel, getTopLevelObjectProperties(owlModel));
	}

	public static Collection getTopLevelObjectProperties(OWLModel owlModel) {
		Collection properties = owlModel.getOWLAnnotationProperties();
		for(Iterator it = properties.iterator(); it.hasNext(); ) {
			RDFProperty  curProp = (RDFProperty) it.next();
			if(curProp.getSuperpropertyCount() > 0 || curProp.isSystem()) {
				it.remove();
			}
		}
		
		return properties;
	}
	
	public boolean isSuitable(RDFProperty rdfProperty) {
		return rdfProperty.isAnnotationProperty();
	}
}

