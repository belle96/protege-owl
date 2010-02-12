
package edu.stanford.smi.protegex.owl.swrl.bridge;

import edu.stanford.smi.protegex.owl.swrl.sqwrl.ClassValue;

import java.util.Set;

public interface OWLClass extends OWLDescription, OWLEntity, ClassArgument, ClassValue
{
  Set<String> getSuperclassURIs();
  Set<String> getDirectSuperClassURIs();
  Set<String> getDirectSubClassURIs();
  Set<String> getEquivalentClassURIs();
  Set<String> getEquivalentClassSuperclassURIs();
} // OWLClass
