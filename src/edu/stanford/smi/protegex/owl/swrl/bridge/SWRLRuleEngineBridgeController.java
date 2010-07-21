package edu.stanford.smi.protegex.owl.swrl.bridge;

import java.util.Set;

import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLNamedIndividual;

/**
 * This interface provides access methods to retrieve knowledge inferred by a rule engine.
 */
public interface SWRLRuleEngineBridgeController 
{
  int getNumberOfInferredOWLIndividuals();
  int getNumberOfInferredOWLAxioms();

  Set<OWLNamedIndividual> getInferredOWLIndividuals();
  Set<OWLAxiom> getInferredOWLAxioms();
}
