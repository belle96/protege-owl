
package edu.stanford.smi.protegex.owl.swrl.bridge;

import edu.stanford.smi.protegex.owl.swrl.sqwrl.ClassValue;

import java.util.Set;

// A named OWL class

public interface OWLClass extends OWLDescription, OWLEntity, ClassArgument, ClassValue
{
  String getPrefixedClassName();

  Set<String> getSuperclassNames();
  Set<String> getDirectSuperClassNames();
  Set<String> getDirectSubClassNames();
  Set<String> getEquivalentClassNames();
  Set<String> getEquivalentClassSuperclassNames();
} // OWLClass
