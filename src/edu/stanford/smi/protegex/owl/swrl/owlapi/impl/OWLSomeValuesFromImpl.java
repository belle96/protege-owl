
package edu.stanford.smi.protegex.owl.swrl.owlapi.impl;

import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLClass;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLProperty;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLSomeValuesFrom;

public class OWLSomeValuesFromImpl extends OWLRestrictionImpl implements OWLSomeValuesFrom 
{
  private OWLClass someValuesFrom;

  public OWLSomeValuesFromImpl(OWLClass owlClass, OWLProperty onProperty, OWLClass someValuesFrom)
  {
    super(owlClass, onProperty);
    this.someValuesFrom = someValuesFrom;
  } // OWLRestrictionImpl

  public OWLClass getSomeValuesFrom() { return someValuesFrom; }

  public String toString()
  {
    return "someValuesFrom(" + asOWLClass().getURI() + ", " + getProperty().getURI() + ", " + getSomeValuesFrom().getURI() + ")";
  } // toString

  public boolean equals(Object obj)
  {
    if(this == obj) return true;
    if((obj == null) || (obj.getClass() != this.getClass())) return false;
    OWLSomeValuesFromImpl impl = (OWLSomeValuesFromImpl)obj;
    return (asOWLClass().getURI() == impl.asOWLClass().getURI() || (asOWLClass().getURI() != null && asOWLClass().getURI().equals(impl.asOWLClass().getURI()))) &&
           (getProperty().getURI() == impl.getProperty().getURI() || (getProperty().getURI() != null && getProperty().getURI().equals(impl.getProperty().getURI()))) &&
           (getSomeValuesFrom().getURI() == impl.getSomeValuesFrom().getURI() || (getSomeValuesFrom().getURI() != null && getSomeValuesFrom().getURI().equals(impl.getSomeValuesFrom().getURI())));
  } // equals

  public int hashCode()
  {
    int hash = 232;

    hash = hash + (null == asOWLClass().getURI() ? 0 : asOWLClass().getURI().hashCode());
    hash = hash + (null == getProperty().getURI() ? 0 : getProperty().getURI().hashCode());
    hash = hash + (null == getSomeValuesFrom().getURI() ? 0 : getSomeValuesFrom().getURI().hashCode());

    return hash;
  } // hashCode

} // OWLSomeValuesFromImpl
