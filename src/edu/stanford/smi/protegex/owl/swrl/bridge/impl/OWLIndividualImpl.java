
package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import java.util.HashSet;
import java.util.Set;

import edu.stanford.smi.protegex.owl.swrl.bridge.OWLClass;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLIndividual;

/**
 * Class representing an OWL individual. 
 */
public class OWLIndividualImpl implements OWLIndividual
{
  // NOTE: equals() method defined in this class

  private String individualURI;  
  private Set<OWLClass> definingClasses;
  private Set<OWLIndividual> sameAsIndividuals, differentFromIndividuals;

  public OWLIndividualImpl(String individualURI)
  {
    initialize(individualURI);
  } // OWLIndividualImpl

  public String getURI() { return individualURI; }
  public Set<OWLClass> getTypes() { return definingClasses; }
  
  public void addType(OWLClass definingClass) { definingClasses.add(definingClass); }
  
  public void addSameAsIndividual(OWLIndividual sameAsIndividual) { sameAsIndividuals.add(sameAsIndividual); }
  public void addDifferentFromIndividual(OWLIndividual differentFromIndividual) { differentFromIndividuals.add(differentFromIndividual); }

  public Set<OWLIndividual> getSameIndividuals() { return sameAsIndividuals; }
  public Set<OWLIndividual> getDifferentIndividuals() { return differentFromIndividuals; }

  public boolean hasType(String classURI) 
  {
    for (OWLClass owlClass : definingClasses) if (owlClass.getURI().equals(classURI)) return true;

    return false;
  } // hasClass

  public String toString() { return getURI(); }

  // We consider individuals to be equal if they have the same name.
  public boolean equals(Object obj)
  {
    if(this == obj) return true;
    if((obj == null) || (obj.getClass() != this.getClass())) return false;
    OWLIndividualImpl impl = (OWLIndividualImpl)obj;
    return (getURI() == impl.getURI() || (getURI() != null && getURI().equals(impl.getURI())));
  } // equals

  public int hashCode()
  {
    int hash = 8;

    hash = hash + (null == getURI() ? 0 : getURI().hashCode());
    
    return hash;
  } // hashCode

  public int compareTo(Object o)
  {
    return individualURI.compareTo(((OWLIndividualImpl)o).getURI());
  } // compareTo

  private void initialize(String individualURI)
  {
    this.individualURI = individualURI;

    definingClasses = new HashSet<OWLClass>();
    sameAsIndividuals = new HashSet<OWLIndividual>();
    differentFromIndividuals = new HashSet<OWLIndividual>();
  } // initialize

} // OWLIndividualImpl
