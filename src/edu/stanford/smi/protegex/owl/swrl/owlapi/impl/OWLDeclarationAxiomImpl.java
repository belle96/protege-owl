
package edu.stanford.smi.protegex.owl.swrl.owlapi.impl;

import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLClass;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLDataProperty;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLDeclarationAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLEntity;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLNamedIndividual;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLObjectProperty;

public class OWLDeclarationAxiomImpl implements OWLDeclarationAxiom
{
  private OWLEntity owlEntity;

  public OWLDeclarationAxiomImpl(OWLEntity owlEntity) { this.owlEntity = owlEntity; }

  public OWLEntity getEntity() { return owlEntity; }
  
  public String toString() 
  { 
  	String uri = owlEntity.getURI(); 
  
  	if (owlEntity instanceof OWLClass)
  		return "owl:Class(" + uri + ")";
  	else if (owlEntity instanceof OWLNamedIndividual)
  		return "owl:Individual(" + uri + ")";
  	else if (owlEntity instanceof OWLObjectProperty)
  		return "owl:ObjectProperty(" + uri + ")";
  	else if (owlEntity instanceof OWLDataProperty)
  		return "owl:DataProperty(" + uri + ")";
  	else return "UNKNOWN_DECLARATION_TYPE(" + uri + ")";
  }
  
  public boolean equals(Object obj)
  {
    if (this == obj) return true;
    if ((obj == null) || (obj.getClass() != this.getClass())) return false;
    OWLDeclarationAxiomImpl impl = (OWLDeclarationAxiomImpl)obj;
    return (super.equals((OWLDeclarationAxiomImpl)impl) &&
            (owlEntity != null && impl.owlEntity != null && owlEntity.equals(impl.owlEntity)));
  }

  public int hashCode()
  {
    int hash = 42;
    hash = hash + super.hashCode();
    hash = hash + (null == owlEntity ? 0 : owlEntity.hashCode());
    return hash;
  }

} 
