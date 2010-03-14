
package edu.stanford.smi.protegex.owl.swrl.owlapi.impl;

import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLClass;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLProperty;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLRestriction;

public abstract class OWLRestrictionImpl implements OWLRestriction
{
  private OWLClass owlClass;
  private OWLProperty onProperty;

  public OWLRestrictionImpl(OWLClass owlClass, OWLProperty onProperty)
  {
    this.owlClass = owlClass;
    this.onProperty = onProperty;
  } // OWLRestrictionImpl

  public OWLClass asOWLClass() { return owlClass; }
  public OWLProperty getProperty() { return onProperty; }
} // OWLRestrictionImpl
