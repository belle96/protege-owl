
package edu.stanford.smi.protegex.owl.swrl.owlapi;

public interface OWLPropertyAssertionAxiom extends OWLAxiom
{
  OWLNamedIndividual getSubject();
  OWLProperty getProperty();
} // OWLPropertyAssertionAxiom
