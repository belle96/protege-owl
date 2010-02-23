
package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import edu.stanford.smi.protegex.owl.swrl.bridge.OWLRestriction;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLClass;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLProperty;

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
