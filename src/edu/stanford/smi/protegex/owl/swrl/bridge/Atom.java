
package edu.stanford.smi.protegex.owl.swrl.bridge;

import java.util.Set;

/*
** Base class representing information about atoms in a SWRL rule
*/
public interface Atom
{
  boolean hasReferencedClasses();
  boolean hasReferencedProperties();
  boolean hasReferencedIndividuals();
  boolean hasReferencedVariables();
  Set<String> getReferencedClassNames();
  Set<String> getReferencedPropertyNames();
  Set<String> getReferencedIndividualNames();
  Set<String> getReferencedVariableNames();
} // Atom
