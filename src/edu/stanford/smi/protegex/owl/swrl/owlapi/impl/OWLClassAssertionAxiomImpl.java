
package edu.stanford.smi.protegex.owl.swrl.owlapi.impl;

import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLClass;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLClassAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLNamedIndividual;

public class OWLClassAssertionAxiomImpl implements OWLClassAssertionAxiom
{
  private OWLNamedIndividual individual;
  private OWLClass description;

  public OWLClassAssertionAxiomImpl(OWLNamedIndividual individual, OWLClass description)
  {
    this.individual = individual;
    this.description = description;
  }

  public OWLClass getDescription() { return description; }
  public OWLNamedIndividual getIndividual() { return individual; }

  public boolean equals(Object obj)
  {
    if (this == obj) return true;
    if ((obj == null) || (obj.getClass() != this.getClass())) return false;
    OWLClassAssertionAxiomImpl impl = (OWLClassAssertionAxiomImpl)obj;
    return (super.equals((OWLClassAssertionAxiomImpl)impl) &&
            (description != null && impl.description != null && description.equals(impl.description)) &&
            (individual != null && impl.individual != null && individual.equals(impl.individual)));
  }

  public int hashCode()
  {
    int hash = 49;
    hash = hash + super.hashCode();
    hash = hash + (null == description ? 0 : description.hashCode());
    hash = hash + (null == individual ? 0 : individual.hashCode());
    return hash;
  }

  public String toString() { return "" + getDescription() + "(" + getIndividual() + ")"; }
}
