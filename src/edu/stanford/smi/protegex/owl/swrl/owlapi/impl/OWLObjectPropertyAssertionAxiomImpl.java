
package edu.stanford.smi.protegex.owl.swrl.owlapi.impl;

import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLIndividual;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLObjectPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLProperty;

public class OWLObjectPropertyAssertionAxiomImpl extends OWLPropertyAssertionAxiomImpl implements OWLObjectPropertyAssertionAxiom
{
  private OWLIndividual object;

  public OWLObjectPropertyAssertionAxiomImpl(OWLIndividual subject, OWLProperty property, OWLIndividual object)
  {
    super(subject, property);
    this.object = object;
  } // OWLObjectPropertyAssertionAxiomImpl

  public OWLIndividual getObject() { return object; }

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
