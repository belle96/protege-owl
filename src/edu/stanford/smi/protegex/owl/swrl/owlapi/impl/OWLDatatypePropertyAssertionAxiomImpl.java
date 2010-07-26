
package edu.stanford.smi.protegex.owl.swrl.owlapi.impl;

import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLDataPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLLiteral;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLNamedIndividual;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLProperty;

public class OWLDatatypePropertyAssertionAxiomImpl extends OWLPropertyAssertionAxiomImpl implements OWLDataPropertyAssertionAxiom
{
  private OWLLiteral object;

  public OWLDatatypePropertyAssertionAxiomImpl(OWLNamedIndividual subject, OWLProperty property, OWLLiteral object)
  {
    super(subject, property);
    this.object = object;
  }

  public OWLLiteral getObject() { return object; }

  public boolean equals(Object obj)
  {
    if (this == obj) return true;
    if ((obj == null) || (obj.getClass() != this.getClass())) return false;
    OWLDatatypePropertyAssertionAxiomImpl impl = (OWLDatatypePropertyAssertionAxiomImpl)obj;
    return (super.equals((OWLPropertyAssertionAxiomImpl)impl) &&
            (object != null && impl.object != null && object.equals(impl.object)));
  }

  public int hashCode()
  {
    int hash = 45;
    hash = hash + super.hashCode();
    hash = hash + (null == object ? 0 : object.hashCode());
    return hash;
  }
  
  public String toString() 
  { 
    String result = "" + getProperty() + "(" + getSubject() + ", ";

    if (object.isOWLStringLiteral()) result += "\"" + object + "\"";
    else result += "" + object;

    result += ")"; 

    return result;
  }
}
