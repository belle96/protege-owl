
package edu.stanford.smi.protegex.owl.swrl.bridge;

public interface OWLRestriction extends OWLDescription
{
  OWLClass asOWLClass();
  OWLProperty getProperty();
} // OWLRestriction
