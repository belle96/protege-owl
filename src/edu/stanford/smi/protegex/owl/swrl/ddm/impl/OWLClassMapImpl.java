
package edu.stanford.smi.protegex.owl.swrl.ddm.impl;

import edu.stanford.smi.protegex.owl.swrl.ddm.*;

import edu.stanford.smi.protegex.owl.swrl.bridge.OWLClass;

public class OWLClassMapImpl implements OWLClassMap
{
  private OWLClass owlClass;
  private PrimaryKey primaryKey;

  public OWLClassMapImpl(OWLClass owlClass, PrimaryKey primaryKey)
  {
    this.owlClass = owlClass;
    this.primaryKey = primaryKey;
  } // OWLClassMapImpl

  public OWLClass getOWLClass() { return owlClass; }
  public PrimaryKey getPrimaryKey() { return primaryKey; }
} // OWLClassMapImpl
