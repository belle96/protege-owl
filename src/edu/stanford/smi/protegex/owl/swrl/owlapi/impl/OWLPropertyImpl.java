
package edu.stanford.smi.protegex.owl.swrl.owlapi.impl;

import java.util.HashSet;
import java.util.Set;

import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLClass;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLProperty;

/**
 * Class representing an OWL property
 */
public abstract class OWLPropertyImpl implements OWLProperty
{
  // There is an equals method defined on this class.
  private String propertyURI;
  private Set<OWLClass> domainClasses, rangeClasses;
  private Set<OWLProperty> superProperties, subProperties, equivalentProperties;
  
  public OWLPropertyImpl(String propertyURI) 
  {
    this.propertyURI = propertyURI;
    initialize();
  } 

  public void addDomainClass(OWLClass domainClass) { this.domainClasses.add(domainClass); }
  public void addRangeClass(OWLClass rangeClass) { this.rangeClasses.add(rangeClass); }
  public void addSuperProperty(OWLProperty superProperty) { this.superProperties.add(superProperty); }
  public void addSubProperty(OWLProperty subProperty) { this.subProperties.add(subProperty); }
  public void addEquivalentProperty(OWLProperty equivalentProperty) { this.equivalentProperties.add(equivalentProperty); }

  public String getURI() { return propertyURI; }
  public Set<OWLClass> getDomainClasses() { return domainClasses; }
  public Set<OWLClass> getRangeClasses() { return rangeClasses; }
  public Set<OWLProperty> getSuperProperties() { return superProperties; }
  public Set<OWLProperty> getSubProperties() { return subProperties; }
  public Set<OWLProperty> getEquivalentProperties() { return equivalentProperties; }
  
  public Set<OWLProperty> getTypes() 
  { 
  	Set<OWLProperty> types = new HashSet<OWLProperty>(superProperties);
  	types.add(this);
  	
  	return types;
  }

  public String toString() { return getURI(); }

  public boolean equals(Object obj)
  {
    if(this == obj) return true;
    if((obj == null) || (obj.getClass() != this.getClass())) return false;
    OWLPropertyImpl impl = (OWLPropertyImpl)obj;
    return (getURI() == impl.getURI() || (getURI() != null && getURI().equals(impl.getURI())));
  }

  public int hashCode()
  {
    int hash = 767;
  
    hash = hash + (null == getURI() ? 0 : getURI().hashCode());
  
    return hash;
  }

  private void initialize()
  {
    domainClasses = new HashSet<OWLClass>();
    rangeClasses = new HashSet<OWLClass>();
    superProperties = new HashSet<OWLProperty>();
    subProperties = new HashSet<OWLProperty>();
    equivalentProperties = new HashSet<OWLProperty>();
  } // initialize

} // OWLPropertyImpl
