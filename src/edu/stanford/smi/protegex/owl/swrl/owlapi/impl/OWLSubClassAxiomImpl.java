
package edu.stanford.smi.protegex.owl.swrl.owlapi.impl;

import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLClass;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLSubClassAxiom;

public class OWLSubClassAxiomImpl implements OWLSubClassAxiom
{
  private OWLClass subClass, superClass;

  public OWLSubClassAxiomImpl(OWLClass subClass, OWLClass superClass)
  {
    this.subClass = subClass;
    this.superClass = superClass;
  } // OWLSubClassAxiomImpl

  public OWLClass getSubClass() { return subClass; }
  public OWLClass getSuperClass() { return superClass; }

  public boolean equals(Object obj)
  {
    if (this == obj) return true;
    if ((obj == null) || (obj.getClass() != this.getClass())) return false;
    OWLSubClassAxiomImpl impl = (OWLSubClassAxiomImpl)obj;
    return (super.equals((OWLSubClassAxiomImpl)impl) &&
            (subClass != null && impl.subClass != null && subClass.equals(impl.subClass)) &&
            (superClass != null && impl.superClass != null && superClass.equals(impl.superClass)));
  } // equals

  public int hashCode()
  {
    int hash = 49;
    hash = hash + super.hashCode();
    hash = hash + (null == subClass ? 0 : subClass.hashCode());
    hash = hash + (null == superClass ? 0 : superClass.hashCode());
    return hash;
  } // hashCode

  public String toString() { return "" + getSubClass() + " subclass of " + getSuperClass() + ""; }
} // OWLSubClassAxiomImpl
