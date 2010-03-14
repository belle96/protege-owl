
package edu.stanford.smi.protegex.owl.swrl.owlapi;

public interface OWLPropertyAssertionAxiom extends OWLAxiom
{
  OWLIndividual getSubject();
  OWLProperty getProperty();
} // OWLPropertyAssertionAxiom
