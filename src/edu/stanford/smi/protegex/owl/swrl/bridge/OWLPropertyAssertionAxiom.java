
package edu.stanford.smi.protegex.owl.swrl.bridge;

public interface OWLPropertyAssertionAxiom extends OWLAxiom
{
  OWLIndividual getSubject();
  OWLProperty getProperty();
} // OWLPropertyAssertionAxiom
