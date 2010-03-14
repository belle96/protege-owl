
package edu.stanford.smi.protegex.owl.swrl.owlapi.impl;

import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLDeclarationAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLEntity;

public class OWLDeclarationAxiomImpl implements OWLDeclarationAxiom
{
  private OWLEntity owlEntity;

  public OWLDeclarationAxiomImpl(OWLEntity owlEntity) { this.owlEntity = owlEntity; }

  public OWLEntity getEntity() { return owlEntity; }
} // OWLDeclarationAxiomImpl
