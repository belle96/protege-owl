
package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import edu.stanford.smi.protegex.owl.swrl.bridge.*;

public abstract class OWLPropertyAssertionAxiomImpl implements OWLPropertyAssertionAxiom 
{
  private OWLIndividual subject;
  private OWLProperty property;

  public OWLPropertyAssertionAxiomImpl(OWLIndividual subject, OWLProperty property)
  {
    this.subject = subject;
    this.property = property;
  } // OWLPropertyAssertionAxiomImpl

  public OWLIndividual getSubject() { return subject; }
  public OWLProperty getProperty() { return property; }

  public boolean equals(Object obj)
  {
    if (this == obj) return true;
    if ((obj == null) || (obj.getClass() != this.getClass())) return false;
    OWLPropertyAssertionAxiomImpl impl = (OWLPropertyAssertionAxiomImpl)obj;
    return ((subject != null && impl.subject != null && subject.equals(impl.subject)) &&
            (property != null && impl.property != null && property.equals(impl.property)));
  } // equals

  public int hashCode()
  {
    int hash = 45;
    hash = hash + (null == subject ? 0 : subject.hashCode());
    hash = hash + (null == property ? 0 : property.hashCode());
    return hash;
  } // hashCode

} // OWLPropertyAssertionAxiomImpl
