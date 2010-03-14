
package edu.stanford.smi.protegex.owl.swrl.bridge;

import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLProperty;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLPropertyAssertionAxiom;

// This is an OWL Full entity.

public interface OWLPropertyPropertyAssertionAxiom extends OWLPropertyAssertionAxiom
{
  OWLProperty getObject();
} // OWLPropertyPropertyAssertionAxiom
