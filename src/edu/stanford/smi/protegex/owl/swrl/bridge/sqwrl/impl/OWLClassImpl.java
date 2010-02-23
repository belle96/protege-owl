
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
  private Set<String> superclassNames, directSuperClassNames, directSubClassNames, equivalentClassNames, equivalentClassSuperclassNames;
    
  // Constructor used when creating a OWLClass object to pass as a built-in argument 
  public OWLClassImpl(String classURI)
  {
    initialize(classURI, classURI);
  } // OWLClassImpl

  // Constructor used when creating a OWLClass object from a built-in
  public OWLClassImpl(String classURI, String superclassURI)
  {
    initialize(classURI, superclassURI);
    superclassNames.add(superclassURI);
  } // OWLClassImpl

  public void setSuperclassNames(Set<String> superclassNames) { this.superclassNames = superclassNames; }
  public void setDirectSuperClassNames(Set<String> directSuperClassNames) { this.directSuperClassNames = directSuperClassNames; }
  public void setDirectSubClassNames(Set<String> directSubClassNames) { this.directSubClassNames = directSubClassNames; }
  public void setEquivalentClassNames(Set<String> equivalentClassNames) { this.equivalentClassNames = equivalentClassNames; }
  public void setEquivalentClassSuperclassNames(Set<String> equivalentClassSuperclassNames) { this.equivalentClassSuperclassNames = equivalentClassSuperclassNames; }

  public String getURI() { return classURI; }
  public String getPrefixedClassName() { return prefixedClassName; }
  public Set<String> getSuperclassNames() { return superclassNames; }
  public Set<String> getDirectSuperClassNames() { return directSuperClassNames; }
  public Set<String> getDirectSubClassNames() { return directSubClassNames; }
  public Set<String> getEquivalentClassNames() { return equivalentClassNames; }
  public Set<String> getEquivalentClassSuperclassNames() { return equivalentClassSuperclassNames; }

  public boolean isNamedClass() { return true; }
  public String getRepresentation() { return getPrefixedClassName(); }

  public String toString() { return getPrefixedClassName(); }

  // We consider classes to be equal if they have the same name.
  public boolean equals(Object obj)
  {
    if(this == obj) return true;
    if((obj == null) || (obj.getClass() != this.getClass())) return false;
    OWLClassImpl impl = (OWLClassImpl)obj;
    return (getURI() == impl.getURI() || (getURI() != null && getURI().equals(impl.getURI()))) &&
           (getPrefixedClassName() == impl.getPrefixedClassName() || (getPrefixedClassName() != null && getPrefixedClassName().equals(impl.getPrefixedClassName()))) &&
           (superclassNames != null && impl.superclassNames != null && superclassNames.equals(impl.superclassNames)) &&
           (directSuperClassNames != null && impl.directSuperClassNames != null && directSuperClassNames.equals(impl.directSuperClassNames)) &&
           (directSubClassNames != null && impl.directSubClassNames != null && directSubClassNames.equals(impl.directSubClassNames)) &&
           (equivalentClassNames != null && impl.equivalentClassNames != null && equivalentClassNames.equals(impl.equivalentClassNames)) &&
           (equivalentClassSuperclassNames != null && impl.equivalentClassSuperclassNames != null && equivalentClassSuperclassNames.equals(impl.equivalentClassSuperclassNames));
  } // equals

  public int hashCode()
  {
    int hash = 12;

    hash = hash + (null == getURI() ? 0 : getURI().hashCode());
    hash = hash + (null == getPrefixedClassName() ? 0 : getPrefixedClassName().hashCode());
    hash = hash + (null == getSuperclassNames() ? 0 : getSuperclassNames().hashCode());
    hash = hash + (null == getDirectSuperClassNames() ? 0 : getDirectSuperClassNames().hashCode());
    hash = hash + (null == getDirectSubClassNames() ? 0 : getDirectSubClassNames().hashCode());
    hash = hash + (null == getEquivalentClassNames() ? 0 : getEquivalentClassNames().hashCode());
    hash = hash + (null == getEquivalentClassSuperclassNames() ? 0 : getEquivalentClassSuperclassNames().hashCode());

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
    superclassNames = new HashSet<String>();
    directSuperClassNames = new HashSet<String>();
    directSubClassNames = new HashSet<String>();
    equivalentClassNames = new HashSet<String>();
    equivalentClassSuperclassNames = new HashSet<String>();
  } // initialize

} // OWLClassImpl
