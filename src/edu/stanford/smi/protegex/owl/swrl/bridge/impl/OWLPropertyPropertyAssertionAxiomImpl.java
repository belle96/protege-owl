
package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import edu.stanford.smi.protegex.owl.swrl.bridge.OWLIndividual;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLProperty;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLPropertyPropertyAssertionAxiom;

public class OWLPropertyPropertyAssertionAxiomImpl extends OWLPropertyAssertionAxiomImpl implements OWLPropertyPropertyAssertionAxiom
{
  private OWLProperty object;

  public OWLPropertyPropertyAssertionAxiomImpl(OWLIndividual subject, OWLProperty property, OWLProperty object)
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
