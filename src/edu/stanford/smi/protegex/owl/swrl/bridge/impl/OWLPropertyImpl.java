
package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import java.util.HashSet;
import java.util.Set;

import edu.stanford.smi.protegex.owl.swrl.bridge.OWLProperty;

/**
 * Class representing an OWL property
 */
public abstract class OWLPropertyImpl extends BuiltInArgumentImpl implements OWLProperty
{
  // There is an equals method defined on this class.
  private String propertyURI, prefixedPropertyName;
  private Set<String> domainClassURIs, rangeClassURIs, superPropertyURIs, subPropertyURIs, 
    equivalentPropertyURIs, equivalentPropertySuperPropertyURIs;
  
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
  
  public void setDomainClassURIs(Set<String> domainClassURIs) { this.domainClassURIs = domainClassURIs; }
  public void setRangeClassURIs(Set<String> rangeClassURIs) { this.rangeClassURIs = rangeClassURIs; }
  public void setSuperPropertyURIs(Set<String> superPropertyURIs) { this.superPropertyURIs = superPropertyURIs; }
  public void setSubPropertyURIs(Set<String> subPropertyURIs) { this.subPropertyURIs = subPropertyURIs; }
  public void setEquivalentPropertyURIs(Set<String> equivalentPropertyURIs) { this.equivalentPropertyURIs = equivalentPropertyURIs; }
  public void setEquivalentPropertySuperPropertyURIs(Set<String> equivalentPropertySuperPropertyURIs) { this.equivalentPropertySuperPropertyURIs = equivalentPropertySuperPropertyURIs; }

  public String getURI() { return propertyURI; }
  public String getPrefixedPropertyName() { return prefixedPropertyName; }
  public Set<String> getDomainClassURIs() { return domainClassURIs; }
  public Set<String> getRangeClassURIs() { return rangeClassURIs; }
  public Set<String> getSuperPropertyURIs() { return superPropertyURIs; }
  public Set<String> getSubPropertyURIs() { return subPropertyURIs; }
  public Set<String> getEquivalentPropertyURIs() { return equivalentPropertyURIs; }
  public Set<String> getEquivalentPropertySuperPropertyURIs() { return equivalentPropertySuperPropertyURIs; }

  public String toString() { return getPrefixedPropertyName(); }

  public boolean equals(Object obj)
  {
    if(this == obj) return true;
    if((obj == null) || (obj.getClass() != this.getClass())) return false;
    OWLPropertyImpl impl = (OWLPropertyImpl)obj;
    return (getURI() == impl.getURI() || (getURI() != null && getURI().equals(impl.getURI()))) && 
      (getPrefixedPropertyName() == impl.getPrefixedPropertyName() || (getPrefixedPropertyName() != null && getPrefixedPropertyName().equals(impl.getPrefixedPropertyName()))) && 
      (domainClassURIs == impl.domainClassURIs || (domainClassURIs != null && domainClassURIs.equals(impl.domainClassURIs))) &&
      (rangeClassURIs == impl.rangeClassURIs || (rangeClassURIs != null && rangeClassURIs.equals(impl.rangeClassURIs))) &&
      (subPropertyURIs == impl.subPropertyURIs || (subPropertyURIs != null && subPropertyURIs.equals(impl.subPropertyURIs))) &&
      (superPropertyURIs == impl.superPropertyURIs || (superPropertyURIs != null && superPropertyURIs.equals(impl.superPropertyURIs))) &&
      (equivalentPropertyURIs == impl.equivalentPropertyURIs || (equivalentPropertyURIs != null && equivalentPropertyURIs.equals(impl.equivalentPropertyURIs))) &&
      (equivalentPropertySuperPropertyURIs == impl.equivalentPropertySuperPropertyURIs || (equivalentPropertySuperPropertyURIs != null && equivalentPropertySuperPropertyURIs.equals(impl.equivalentPropertySuperPropertyURIs)));
  } // equals

  public int hashCode()
  {
    int hash = 767;
    hash = hash + (null == getURI() ? 0 : getURI().hashCode());
    hash = hash + (null == getPrefixedPropertyName() ? 0 : getPrefixedPropertyName().hashCode());
    hash = hash + (null == domainClassURIs ? 0 : domainClassURIs.hashCode());
    hash = hash + (null == rangeClassURIs ? 0 : rangeClassURIs.hashCode());
    hash = hash + (null == subPropertyURIs ? 0 : subPropertyURIs.hashCode());
    hash = hash + (null == superPropertyURIs ? 0 : superPropertyURIs.hashCode());
    hash = hash + (null == equivalentPropertyURIs ? 0 : equivalentPropertyURIs.hashCode());
    hash = hash + (null == equivalentPropertySuperPropertyURIs ? 0 : equivalentPropertySuperPropertyURIs.hashCode());
    return hash;
  } // hashCode

  public int compareTo(Object o)
  {
    return propertyURI.compareTo(((OWLPropertyImpl)o).getURI());
  } // compareTo

  private void initialize()
  {
    domainClassURIs = new HashSet<String>();
    rangeClassURIs = new HashSet<String>();
    superPropertyURIs = new HashSet<String>();
    subPropertyURIs = new HashSet<String>();
    equivalentPropertyURIs = new HashSet<String>();
    equivalentPropertySuperPropertyURIs = new HashSet<String>();
  } // initialize

} // OWLPropertyImpl
