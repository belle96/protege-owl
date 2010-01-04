package edu.stanford.smi.protegex.owl.ui.metrics.lang;

import edu.stanford.smi.protegex.owl.model.util.DLExpressivityChecker;

import java.util.HashMap;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Oct 22, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class DLExpressivityExplanation {

	private static HashMap map;

	static {
		map = new HashMap();
		map.put(DLExpressivityChecker.FL0, "Allows concept conjuction (intersection) and full universal quantification.");
		map.put(DLExpressivityChecker.FL_MINUS, "Allows concept intersection, full universal quantification and " +
		                                        "atomic negation.");
		map.put(DLExpressivityChecker.AL, "Allows concept intersection, full universal quantification, atomic " +
		                                  "negation and limited existential quantification (i.e. existential " +
		                                  "restrictions with fillers limited to owl:Thing)");
		map.put(DLExpressivityChecker.C, "Complex concept negation (e.g. not(A or B)).  Note that ALC allows disjunction " +
		                                 "and full existential quantification, which can be represented with conjuction and " +
		                                 "full negation, and universal quantification and full negation respectively.");
		map.put(DLExpressivityChecker.U, "Concept disjunction (Union classes e.g. A or B)");
		map.put(DLExpressivityChecker.E, "Full existential quantification (i.e. Existential restrictions with " +
		                                 "fillers not limited to owl:Thing)");
		map.put(DLExpressivityChecker.N, "Number restrictions (cardinality restrictions, also includes functional properties)");
		map.put(DLExpressivityChecker.Q, "Qualified number restrictions (qualified cardinality restrictions)");
		map.put(DLExpressivityChecker.H, "Role (property) hierarchy");
		map.put(DLExpressivityChecker.I, "Inverse roles (properties which have inverses specified, or properties " +
		                                 "that are symmetric)");
		map.put(DLExpressivityChecker.O, "Nominals (Singleton sets, oneOf, eg {a} - these are also used in hasValue " +
		                                 "restrictions)");
		map.put(DLExpressivityChecker.F, "Functional roles (properties)");
		map.put(DLExpressivityChecker.S, "Abbreviation for ALC with transitive roles.  ALC allows concept intersection, " +
		                                 "full negation, full universal quantification, full existential quantification, " +
		                                 "and concept disjunction.");
		map.put(DLExpressivityChecker.DATATYPE, "Datatypes");
	}

	public static String getExplanation(String feature) {
		return (String) map.get(feature);
	}

}

