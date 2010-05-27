
package edu.stanford.smi.protegex.owl.swrl.owlapi.impl;

import edu.stanford.smi.protegex.owl.swrl.bridge.OWLPropertyPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLNamedIndividual;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLProperty;

public class OWLPropertyPropertyAssertionAxiomImpl extends OWLPropertyAssertionAxiomImpl implements OWLPropertyPropertyAssertionAxiom
{
  private OWLProperty object;

  public OWLPropertyPropertyAssertionAxiomImpl(OWLNamedIndividual subject, OWLProperty property, OWLProperty object)
  {
    super(subject, property);
    this.object = object;
  } // OWLPropertyPropertyAssertionAxiomImpl

  public OWLProperty getObject() { return object; }

  public boolean equals(Object obj)
  {
    if (this == obj) return true;
    if ((obj == null) || (obj.getClass() != this.getClass())) return false;
    OWLPropertyPropertyAssertionAxiomImpl impl = (OWLPropertyPropertyAssertionAxiomImpl)obj;
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

} // OWLPropertyPropertyAssertionAxiomImpl
