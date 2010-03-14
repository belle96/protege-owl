
package edu.stanford.smi.protegex.owl.swrl.owlapi.impl;

import edu.stanford.smi.protegex.owl.swrl.bridge.*;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLIndividual;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLProperty;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLPropertyAssertionAxiom;

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
