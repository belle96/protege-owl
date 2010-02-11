
package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import java.util.HashSet;
import java.util.Set;

import edu.stanford.smi.protegex.owl.swrl.bridge.OWLClass;

/**
 ** Class representing an OWL named class
 */
public class OWLClassImpl extends BuiltInArgumentImpl implements OWLClass
{
  // equals() method defined in this class.
  private String classURI, prefixedClassName;
  private Set<String> superclassURIs, directSuperClassURIs, directSubClassURIs, equivalentClassURIs, equivalentClassSuperclassURIs;
    
  // Constructor used when creating a OWLClass object to pass as a built-in argument 
  public OWLClassImpl(String classURI)
  {
    initialize(classURI, classURI);
  } // OWLClassImpl

  // Constructor used when creating a OWLClass object from a built-in
  public OWLClassImpl(String classURI, String superclassURI)
  {
    initialize(classURI, superclassURI);
    superclassURIs.add(superclassURI);
  } // OWLClassImpl

  public void setSuperclassURIs(Set<String> superclassURIs) { this.superclassURIs = superclassURIs; }
  public void setDirectSuperClassURIs(Set<String> directSuperClassURIs) { this.directSuperClassURIs = directSuperClassURIs; }
  public void setDirectSubClassURIs(Set<String> directSubClassURIs) { this.directSubClassURIs = directSubClassURIs; }
  public void setEquivalentClassURIs(Set<String> equivalentClassURIs) { this.equivalentClassURIs = equivalentClassURIs; }
  public void setEquivalentClassSuperclassURIs(Set<String> equivalentClassSuperclassURIs) { this.equivalentClassSuperclassURIs = equivalentClassSuperclassURIs; }

  public String getURI() { return classURI; }
  public String getPrefixedClassName() { return prefixedClassName; }
  public Set<String> getSuperclassURIs() { return superclassURIs; }
  public Set<String> getDirectSuperClassURIs() { return directSuperClassURIs; }
  public Set<String> getDirectSubClassURIs() { return directSubClassURIs; }
  public Set<String> getEquivalentClassURIs() { return equivalentClassURIs; }
  public Set<String> getEquivalentClassSuperclassURIs() { return equivalentClassSuperclassURIs; }

  public boolean isNamedClass() { return true; }

  public String toString() { return getPrefixedClassName(); }

  // We consider classes to be equal if they have the same name.
  public boolean equals(Object obj)
  {
    if(this == obj) return true;
    if((obj == null) || (obj.getClass() != this.getClass())) return false;
    OWLClassImpl impl = (OWLClassImpl)obj;
    return (getURI() == impl.getURI() || (getURI() != null && getURI().equals(impl.getURI()))) &&
           (getPrefixedClassName() == impl.getPrefixedClassName() || (getPrefixedClassName() != null && getPrefixedClassName().equals(impl.getPrefixedClassName()))) &&
           (superclassURIs != null && impl.superclassURIs != null && superclassURIs.equals(impl.superclassURIs)) &&
           (directSuperClassURIs != null && impl.directSuperClassURIs != null && directSuperClassURIs.equals(impl.directSuperClassURIs)) &&
           (directSubClassURIs != null && impl.directSubClassURIs != null && directSubClassURIs.equals(impl.directSubClassURIs)) &&
           (equivalentClassURIs != null && impl.equivalentClassURIs != null && equivalentClassURIs.equals(impl.equivalentClassURIs)) &&
           (equivalentClassSuperclassURIs != null && impl.equivalentClassSuperclassURIs != null && equivalentClassSuperclassURIs.equals(impl.equivalentClassSuperclassURIs));
  } // equals

  public int hashCode()
  {
    int hash = 12;

    hash = hash + (null == getURI() ? 0 : getURI().hashCode());
    hash = hash + (null == getPrefixedClassName() ? 0 : getPrefixedClassName().hashCode());
    hash = hash + (null == getSuperclassURIs() ? 0 : getSuperclassURIs().hashCode());
    hash = hash + (null == getDirectSuperClassURIs() ? 0 : getDirectSuperClassURIs().hashCode());
    hash = hash + (null == getDirectSubClassURIs() ? 0 : getDirectSubClassURIs().hashCode());
    hash = hash + (null == getEquivalentClassURIs() ? 0 : getEquivalentClassURIs().hashCode());
    hash = hash + (null == getEquivalentClassSuperclassURIs() ? 0 : getEquivalentClassSuperclassURIs().hashCode());

    return hash;
  } // hashCode

  public int compareTo(Object o)
  {
    return classURI.compareTo(((OWLClassImpl)o).getURI());
  } // compareTo

  private void initialize(String classURI, String prefixedClassName)
  {
    this.classURI = classURI;
    this.prefixedClassName = prefixedClassName;
    superclassURIs = new HashSet<String>();
    directSuperClassURIs = new HashSet<String>();
    directSubClassURIs = new HashSet<String>();
    equivalentClassURIs = new HashSet<String>();
    equivalentClassSuperclassURIs = new HashSet<String>();
  } // initialize

} // OWLClassImpl
