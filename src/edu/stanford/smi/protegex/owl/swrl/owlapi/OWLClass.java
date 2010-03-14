
package edu.stanford.smi.protegex.owl.swrl.owlapi;

import java.util.Set;

public interface OWLClass extends OWLDescription, OWLEntity
{
	Set<OWLClass> getTypes();
	Set<OWLClass> getSuperClasses();
	Set<OWLClass> getSubClasses();
  Set<OWLClass> getEquivalentClasses();
} // OWLClass
