
package edu.stanford.smi.protegex.owl.swrl.bridge;

import java.util.Set;

import edu.stanford.smi.protegex.owl.swrl.sqwrl.ObjectValue;

public interface OWLIndividual extends OWLEntity, OWLPropertyValue, IndividualArgument, ObjectValue
{
  Set<OWLClass> getDefiningClasses();
  Set<OWLClass> getDefiningSuperclasses();
  Set<OWLClass> getDefiningEquivalentClasses();
  Set<OWLClass> getDefiningEquivalentClassSuperclasses();
  Set<OWLIndividual> getSameAsIndividuals();

  void addDefiningClass(OWLClass owlClass);
  boolean hasDefiningClass(String classURI);
} // OWLIndividual
