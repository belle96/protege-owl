
package edu.stanford.smi.protegex.owl.swrl.owlapi.impl;

import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLNamedIndividual;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLObjectPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLProperty;

public class OWLObjectPropertyAssertionAxiomImpl extends OWLPropertyAssertionAxiomImpl implements OWLObjectPropertyAssertionAxiom
{
  private OWLNamedIndividual object;

  public OWLObjectPropertyAssertionAxiomImpl(OWLNamedIndividual subject, OWLProperty property, OWLNamedIndividual object)
  {
    super(subject, property);
    this.object = object;
  } // OWLObjectPropertyAssertionAxiomImpl

  public OWLNamedIndividual getObject() { return object; }

  public boolean equals(Object obj)
  {
    if (this == obj) return true;
    if ((obj == null) || (obj.getClass() != this.getClass())) return false;
    OWLObjectPropertyAssertionAxiomImpl impl = (OWLObjectPropertyAssertionAxiomImpl)obj;
    return (super.equals((OWLPropertyAssertionAxiomImpl)impl) &&
            (object != null && impl.object != null && object.equals(impl.object)));
  } // equals

  public int hashCode()
  {
    int hash = 45;
    hash = hash + super.hashCode();
    hash = hash + (null == object ? 0 : object.hashCode());
    return hash;
  } // hashCode

  public String toString() { return "" + getProperty() + "(" + getSubject() + ", " + object + ")"; }

} // OWLObjectPropertyAssertionAxiomImpl
