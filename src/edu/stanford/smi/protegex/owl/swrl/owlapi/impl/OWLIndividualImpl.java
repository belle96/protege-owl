
package edu.stanford.smi.protegex.owl.swrl.owlapi.impl;

import java.util.HashSet;
import java.util.Set;

import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLClass;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLNamedIndividual;

/**
 * Class representing an OWL individual. 
 */
public class OWLIndividualImpl implements OWLNamedIndividual
{
  // NOTE: equals() method defined in this class

  private String individualURI;  
  private Set<OWLClass> definingClasses;
  private Set<OWLNamedIndividual> sameAsIndividuals, differentFromIndividuals;

  public OWLIndividualImpl(String individualURI)
  {
    initialize(individualURI);
  } 

  public String getURI() { return individualURI; }
  public Set<OWLClass> getTypes() { return definingClasses; }
  
  public void addType(OWLClass definingClass) { definingClasses.add(definingClass); }
  
  public void addSameAsIndividual(OWLNamedIndividual sameAsIndividual) { sameAsIndividuals.add(sameAsIndividual); }
  public void addDifferentFromIndividual(OWLNamedIndividual differentFromIndividual) { differentFromIndividuals.add(differentFromIndividual); }

  public Set<OWLNamedIndividual> getSameIndividuals() { return sameAsIndividuals; }
  public Set<OWLNamedIndividual> getDifferentIndividuals() { return differentFromIndividuals; }

  public boolean hasType(String classURI) 
  {
    for (OWLClass owlClass : definingClasses) if (owlClass.getURI().equals(classURI)) return true;

    return false;
  } 

  public String toString() { return getURI(); }


  public int compareTo(Object o)
  {
    return individualURI.compareTo(((OWLIndividualImpl)o).getURI());
  }

  private void initialize(String individualURI)
  {
    this.individualURI = individualURI;

    definingClasses = new HashSet<OWLClass>();
    sameAsIndividuals = new HashSet<OWLNamedIndividual>();
    differentFromIndividuals = new HashSet<OWLNamedIndividual>();
  } 

} 
