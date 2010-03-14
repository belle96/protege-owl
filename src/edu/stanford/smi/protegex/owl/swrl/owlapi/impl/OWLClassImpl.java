
package edu.stanford.smi.protegex.owl.swrl.owlapi.impl;

import java.util.HashSet;
import java.util.Set;

import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLClass;

/**
 * Class representing an OWL named class
 */
public class OWLClassImpl implements OWLClass
{
  // equals() method defined in this class.
  private String classURI;
  private Set<OWLClass> superClasses, subClasses, equivalentClasses;
    
  // Constructor used when creating a OWLClass object to pass as a built-in argument 
  public OWLClassImpl(String classURI)
  {
    initialize(classURI);
  } // OWLClassImpl

  public void addSuperClass(OWLClass superclass) { superClasses.add(superclass); }
  public void addSubClass(OWLClass subClass) { subClasses.add(subClass); }
  public void addEquivalentClass(OWLClass equivalentClass) { equivalentClasses.add(equivalentClass); }

  public String getURI() { return classURI; }
  public Set<OWLClass> getTypes() { return superClasses; }
   
  public Set<OWLClass> getSuperClasses() { return superClasses; }
  public Set<OWLClass> getSubClasses() { return subClasses; }
  public Set<OWLClass> getEquivalentClasses() { return equivalentClasses; }

  public boolean isNamedClass() { return true; }

  public String toString() { return getURI(); }

  // We consider classes to be equal if they have the same name.
  public boolean equals(Object obj)
  {
    if(this == obj) return true;
    if((obj == null) || (obj.getClass() != this.getClass())) return false;
    OWLClassImpl impl = (OWLClassImpl)obj;
    return (getURI() == impl.getURI() || (getURI() != null && getURI().equals(impl.getURI())));
    } // equals

  public int hashCode()
  {
    int hash = 12;

    hash = hash + (null == getURI() ? 0 : getURI().hashCode());
    
    return hash;
  } // hashCode

  private void initialize(String classURI)
  {
    this.classURI = classURI;
    superClasses = new HashSet<OWLClass>();
    subClasses = new HashSet<OWLClass>();
    equivalentClasses = new HashSet<OWLClass>();
  } // initialize

} // OWLClassImpl
