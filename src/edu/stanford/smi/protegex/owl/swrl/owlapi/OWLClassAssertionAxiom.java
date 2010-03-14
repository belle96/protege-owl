
package edu.stanford.smi.protegex.owl.swrl.owlapi;

public interface OWLClassAssertionAxiom extends OWLAxiom
{
  OWLClass getDescription(); // TODO: should be OWLDescription
  OWLIndividual getIndividual();
} // OWLClassAssertionAxiom
