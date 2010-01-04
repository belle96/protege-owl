package edu.stanford.smi.protegex.owlx.examples;

import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;

import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFSLiteral;


/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Jan 12, 2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class RDFSLabelExamples {

	public static void main(String [] args) {
		String URI = "http://www.co-ode.org/ontologies/pizza/2005/10/18/pizza.owl";
		try {
			OWLModel owlModel = ProtegeOWL.createJenaOWLModelFromURI(URI);
			OWLNamedClass cls = owlModel.getOWLNamedClass("CheeseTopping");
			// The ontology actually only had labels in Portuguese, so for
			// the purposes of this example, add in the English label
			cls.addLabel("CheeseTopping", "en");
			Collection values = cls.getLabels();
			for(Iterator it = values.iterator(); it.hasNext(); ) {
				RDFSLiteral rdfsLiteral = (RDFSLiteral) it.next();
				// Print out the Portuguese label
				if(rdfsLiteral.getLanguage().equals("pt")) {
					System.out.println(rdfsLiteral.getString());
				}
			}
		}
		catch(Exception e) {
                  Log.getLogger().log(Level.SEVERE, "Exception caught", e);
		}
	}
}

