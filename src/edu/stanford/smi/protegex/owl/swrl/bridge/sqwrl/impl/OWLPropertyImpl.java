
package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import java.util.HashSet;
import java.util.Set;

import edu.stanford.smi.protegex.owl.swrl.bridge.OWLProperty;

/**
 ** Class representing an OWL property
 */
public abstract class OWLPropertyImpl extends BuiltInArgumentImpl implements OWLProperty
{
  // There is an equals method defined on this class.
  private String propertyURI, prefixedPropertyName;
  private Set<String> domainClassNames, rangeClassNames, superPropertyNames, subPropertyNames, 
    equivalentPropertyNames, equivalentPropertySuperPropertyNames;
  
  // Constructor used when creating a OWLPropertyImpl object to pass as a built-in argument
  public OWLPropertyImpl(String propertyURI) 
  {
    this.propertyURI = propertyURI;
    prefixedPropertyName = propertyURI;
    initialize();
  } // OWLPropertyImpl

  public OWLPropertyImpl(String propertyURI, String prefixedPropertyName) 
  {
    this.propertyURI = propertyURI;
    this.prefixedPropertyName = prefixedPropertyName;
    initialize();
  } // OWLPropertyImpl
  
  public void setDomainClassNames(Set<String> domainClassNames) { this.domainClassNames = domainClassNames; }
  public void setRangeClassNames(Set<String> rangeClassNames) { this.rangeClassNames = rangeClassNames; }
  public void setSuperPropertyNames(Set<String> superPropertyNames) { this.superPropertyNames = superPropertyNames; }
  public void setSubPropertyNames(Set<String> subPropertyNames) { this.subPropertyNames = subPropertyNames; }
  public void setEquivalentPropertyNames(Set<String> equivalentPropertyNames) { this.equivalentPropertyNames = equivalentPropertyNames; }
  public void setEquivalentPropertySuperPropertyNames(Set<String> equivalentPropertySuperPropertyNames) { this.equivalentPropertySuperPropertyNames = equivalentPropertySuperPropertyNames; }

  public String getURI() { return propertyURI; }
  public String getPrefixedPropertyName() { return prefixedPropertyName; }
  public Set<String> getDomainClassNames() { return domainClassNames; }
  public Set<String> getRangeClassNames() { return rangeClassNames; }
  public Set<String> getSuperPropertyNames() { return superPropertyNames; }
  public Set<String> getSubPropertyNames() { return subPropertyNames; }
  public Set<String> getEquivalentPropertyNames() { return equivalentPropertyNames; }
  public Set<String> getEquivalentPropertySuperPropertyNames() { return equivalentPropertySuperPropertyNames; }
  
  public String getRepresentation() { return getPrefixedPropertyName(); }

  public String toString() { return getPrefixedPropertyName(); }

  public boolean equals(Object obj)
  {
    if(this == obj) return true;
    if((obj == null) || (obj.getClass() != this.getClass())) return false;
    OWLPropertyImpl impl = (OWLPropertyImpl)obj;
    return (getURI() == impl.getURI() || (getURI() != null && getURI().equals(impl.getURI()))) && 
      (getPrefixedPropertyName() == impl.getPrefixedPropertyName() || (getPrefixedPropertyName() != null && getPrefixedPropertyName().equals(impl.getPrefixedPropertyName()))) && 
      (domainClassNames == impl.domainClassNames || (domainClassNames != null && domainClassNames.equals(impl.domainClassNames))) &&
      (rangeClassNames == impl.rangeClassNames || (rangeClassNames != null && rangeClassNames.equals(impl.rangeClassNames))) &&
      (subPropertyNames == impl.subPropertyNames || (subPropertyNames != null && subPropertyNames.equals(impl.subPropertyNames))) &&
      (superPropertyNames == impl.superPropertyNames || (superPropertyNames != null && superPropertyNames.equals(impl.superPropertyNames))) &&
      (equivalentPropertyNames == impl.equivalentPropertyNames || (equivalentPropertyNames != null && equivalentPropertyNames.equals(impl.equivalentPropertyNames))) &&
      (equivalentPropertySuperPropertyNames == impl.equivalentPropertySuperPropertyNames || (equivalentPropertySuperPropertyNames != null && equivalentPropertySuperPropertyNames.equals(impl.equivalentPropertySuperPropertyNames)));
  } // equals

  public int hashCode()
  {
    int hash = 767;
    hash = hash + (null == getURI() ? 0 : getURI().hashCode());
    hash = hash + (null == getPrefixedPropertyName() ? 0 : getPrefixedPropertyName().hashCode());
    hash = hash + (null == domainClassNames ? 0 : domainClassNames.hashCode());
    hash = hash + (null == rangeClassNames ? 0 : rangeClassNames.hashCode());
    hash = hash + (null == subPropertyNames ? 0 : subPropertyNames.hashCode());
    hash = hash + (null == superPropertyNames ? 0 : superPropertyNames.hashCode());
    hash = hash + (null == equivalentPropertyNames ? 0 : equivalentPropertyNames.hashCode());
    hash = hash + (null == equivalentPropertySuperPropertyNames ? 0 : equivalentPropertySuperPropertyNames.hashCode());
    return hash;
  } // hashCode

  public int compareTo(Object o)
  {
    return propertyURI.compareTo(((OWLPropertyImpl)o).getURI());
  } // compareTo

  private void initialize()
  {
    domainClassNames = new HashSet<String>();
    rangeClassNames = new HashSet<String>();
    superPropertyNames = new HashSet<String>();
    subPropertyNames = new HashSet<String>();
    equivalentPropertyNames = new HashSet<String>();
    equivalentPropertySuperPropertyNames = new HashSet<String>();
  } // initialize

} // OWLPropertyImpl
