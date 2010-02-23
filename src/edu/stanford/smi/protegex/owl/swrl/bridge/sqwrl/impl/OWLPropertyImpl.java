
package edu.stanford.smi.protegex.owl.swrl.bridge.sqwrl.impl;

import java.util.HashSet;
import java.util.Set;

import edu.stanford.smi.protegex.owl.swrl.bridge.OWLClass;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLProperty;

/**
 * Class representing an OWL property
 */
public abstract class OWLPropertyImpl extends BuiltInArgumentImpl implements OWLProperty
{
  // There is an equals method defined on this class.
  private String propertyURI;
  private Set<OWLClass> domainClasses, rangeClasses;
  private Set<OWLProperty> superProperties, subProperties, equivalentProperties;
  
  // Constructor used when creating a OWLPropertyImpl object to pass as a built-in argument
  public OWLPropertyImpl(String propertyURI) 
  {
    this.propertyURI = propertyURI;
    initialize();
  } // OWLPropertyImpl

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

  public String toString() { return getURI(); }

  public boolean equals(Object obj)
  {
    if(this == obj) return true;
    if((obj == null) || (obj.getClass() != this.getClass())) return false;
    OWLPropertyImpl impl = (OWLPropertyImpl)obj;
    return (getURI() == impl.getURI() || (getURI() != null && getURI().equals(impl.getURI())));
  } // equals

  public int hashCode()
  {
    int hash = 767;
  
    hash = hash + (null == getURI() ? 0 : getURI().hashCode());
  
    return hash;
  } // hashCode

  private void initialize()
  {
    domainClasses = new HashSet<OWLClass>();
    rangeClasses = new HashSet<OWLClass>();
    superProperties = new HashSet<OWLProperty>();
    subProperties = new HashSet<OWLProperty>();
    equivalentProperties = new HashSet<OWLProperty>();
  } // initialize

} // OWLPropertyImpl
