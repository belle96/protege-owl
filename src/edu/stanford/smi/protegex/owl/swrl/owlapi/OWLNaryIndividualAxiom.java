
package edu.stanford.smi.protegex.owl.swrl.owlapi;

import java.util.Set;

public interface OWLNaryIndividualAxiom extends OWLIndividualAxiom
{
  Set<OWLNamedIndividual> getIndividuals();
} // OWLNaryIndividualAxiom
