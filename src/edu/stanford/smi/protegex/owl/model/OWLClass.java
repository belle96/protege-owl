package edu.stanford.smi.protegex.owl.model;

import java.util.Collection;

/**
 * The common base interface for OWLNamedClass and OWLAnonymousClass.
 * This can be used in method declarations if only OWL classes are valid.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface OWLClass extends RDFSClass {

	/**
	 * Gets the disjoint classes of this.
	 *
	 * @return a Collection of RDFSClass instances
	 */	
	Collection getDisjointClasses();
}
