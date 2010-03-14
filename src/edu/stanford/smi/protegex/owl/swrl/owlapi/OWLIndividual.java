
package edu.stanford.smi.protegex.owl.swrl.owlapi;

import java.util.Set;

public interface OWLIndividual extends OWLEntity, OWLPropertyValue
{
  Set<OWLClass> getTypes();
  
  Set<OWLIndividual> getSameIndividuals();
  Set<OWLIndividual> getDifferentIndividuals();

  void addType(OWLClass owlClass);
  boolean hasType(String classURI);
} // OWLIndividual
