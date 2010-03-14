
package edu.stanford.smi.protegex.owl.swrl.owlapi.impl;

import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLClass;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLClassAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLIndividual;

public class OWLClassAssertionAxiomImpl implements OWLClassAssertionAxiom
{
  private OWLIndividual individual;
  private OWLClass description;

  public OWLClassAssertionAxiomImpl(OWLIndividual individual, OWLClass description)
  {
    this.individual = individual;
    this.description = description;
  } // OWLClassAssertionAxiomImpl

  public OWLClass getDescription() { return description; }
  public OWLIndividual getIndividual() { return individual; }

  public boolean equals(Object obj)
  {
    if (this == obj) return true;
    if ((obj == null) || (obj.getClass() != this.getClass())) return false;
    OWLClassAssertionAxiomImpl impl = (OWLClassAssertionAxiomImpl)obj;
    return (super.equals((OWLClassAssertionAxiomImpl)impl) &&
            (description != null && impl.description != null && description.equals(impl.description)) &&
            (individual != null && impl.individual != null && individual.equals(impl.individual)));
  } // equals

  public int hashCode()
  {
    int hash = 49;
    hash = hash + super.hashCode();
    hash = hash + (null == description ? 0 : description.hashCode());
    hash = hash + (null == individual ? 0 : individual.hashCode());
    return hash;
  } // hashCode

  public String toString() { return "" + getDescription() + "(" + getIndividual() + ")"; }

} // OWLClassAssertionAxiom
