
package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import edu.stanford.smi.protegex.owl.swrl.bridge.OWLEntity;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLDeclarationAxiom;

public class OWLDeclarationAxiomImpl implements OWLDeclarationAxiom
{
  private OWLEntity owlEntity;

  public OWLDeclarationAxiomImpl(OWLEntity owlEntity) { this.owlEntity = owlEntity; }

  public OWLEntity getEntity() { return owlEntity; }
} // OWLDeclarationAxiomImpl
