
package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import edu.stanford.smi.protegex.owl.swrl.bridge.OWLSomeValuesFrom;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLClass;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLProperty;

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
    return "someValuesFrom(" + asOWLClass().getClassName() + ", " + getProperty().getPropertyName() + ", " + getSomeValuesFrom().getClassName() + ")";
  } // toString

  public boolean equals(Object obj)
  {
    if(this == obj) return true;
    if((obj == null) || (obj.getClass() != this.getClass())) return false;
    OWLSomeValuesFromImpl impl = (OWLSomeValuesFromImpl)obj;
    return (asOWLClass().getClassName() == impl.asOWLClass().getClassName() || (asOWLClass().getClassName() != null && asOWLClass().getClassName().equals(impl.asOWLClass().getClassName()))) &&
           (getProperty().getPropertyName() == impl.getProperty().getPropertyName() || (getProperty().getPropertyName() != null && getProperty().getPropertyName().equals(impl.getProperty().getPropertyName()))) &&
           (getSomeValuesFrom().getClassName() == impl.getSomeValuesFrom().getClassName() || (getSomeValuesFrom().getClassName() != null && getSomeValuesFrom().getClassName().equals(impl.getSomeValuesFrom().getClassName())));
  } // equals

  public int hashCode()
  {
    int hash = 232;

    hash = hash + (null == asOWLClass().getClassName() ? 0 : asOWLClass().getClassName().hashCode());
    hash = hash + (null == getProperty().getPropertyName() ? 0 : getProperty().getPropertyName().hashCode());
    hash = hash + (null == getSomeValuesFrom().getClassName() ? 0 : getSomeValuesFrom().getClassName().hashCode());

    return hash;
  } // hashCode

} // OWLSomeValuesFromImpl
