
package edu.stanford.smi.protegex.owl.swrl.bridge;

import java.util.Set;

import edu.stanford.smi.protegex.owl.swrl.sqwrl.ObjectValue;

public interface OWLIndividual extends OWLEntity, OWLPropertyValue, IndividualArgument, ObjectValue
{
  String getIndividualName();
  String getPrefixedIndividualName();

  Set<OWLClass> getDefiningClasses();
  Set<OWLClass> getDefiningSuperclasses();
  Set<OWLClass> getDefiningEquivalentClasses();
  Set<OWLClass> getDefiningEquivalentClassSuperclasses();
  Set<OWLIndividual> getSameAsIndividuals();

  void addDefiningClass(OWLClass owlClass);
  boolean hasClass(String className);
} // OWLIndividual
