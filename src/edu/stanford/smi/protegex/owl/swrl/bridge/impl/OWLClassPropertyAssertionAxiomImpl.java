
package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import edu.stanford.smi.protegex.owl.swrl.bridge.OWLClass;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLClassPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLIndividual;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLProperty;

public class OWLClassPropertyAssertionAxiomImpl extends OWLPropertyAssertionAxiomImpl implements OWLClassPropertyAssertionAxiom
{
  private OWLClass object;

  public OWLClassPropertyAssertionAxiomImpl(OWLIndividual subject, OWLProperty property, OWLClass object)
  {
    super(subject, property);
    this.object = object;
  } // OWLClassPropertyAssertionAxiomImpl

  public OWLClass getObject() { return object; }

  public boolean equals(Object obj)
  {
    if (this == obj) return true;
    if ((obj == null) || (obj.getClass() != this.getClass())) return false;
    OWLClassPropertyAssertionAxiomImpl impl = (OWLClassPropertyAssertionAxiomImpl)obj;
    return (super.equals((OWLPropertyAssertionAxiomImpl)impl) &&
            (object != null && impl.object != null && object.equals(impl.object)));
  } // equals

  public int hashCode()
  {
    int hash = 49;
    hash = hash + super.hashCode();
    hash = hash + (null == object ? 0 : object.hashCode());
    return hash;
  } // hashCode

  public String toString() { return "" + getProperty() + "(" + getSubject() + ", " + object + ")"; }

} // OWLClassPropertyAssertionAxiomImpl
