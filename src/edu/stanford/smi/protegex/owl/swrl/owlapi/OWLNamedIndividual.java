
package edu.stanford.smi.protegex.owl.swrl.owlapi;

import java.util.Set;

public interface OWLNamedIndividual extends OWLEntity, OWLPropertyValue
{
  Set<OWLClass> getTypes();
  
  Set<OWLNamedIndividual> getSameIndividuals();
  Set<OWLNamedIndividual> getDifferentIndividuals();

  void addType(OWLClass owlClass);
  boolean hasType(String classURI);
}
