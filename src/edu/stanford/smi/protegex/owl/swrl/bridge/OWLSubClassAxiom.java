
package edu.stanford.smi.protegex.owl.swrl.bridge;

public interface OWLSubClassAxiom extends OWLAxiom
{
  // TODO: should be OWLDescription for both
  OWLClass getSubClass(); 
  OWLClass getSuperClass();
} // OWLClassAssertionAxiom
