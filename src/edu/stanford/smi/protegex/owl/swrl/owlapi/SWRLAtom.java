
package edu.stanford.smi.protegex.owl.swrl.owlapi;

import java.util.Set;

/**
 * Base class representing information about SWRL atoms
 */
public interface SWRLAtom
{
  boolean hasReferencedClasses();
  boolean hasReferencedProperties();
  boolean hasReferencedIndividuals();
  boolean hasReferencedVariables();
  Set<String> getReferencedClassURIs();
  Set<String> getReferencedPropertyURIs();
  Set<String> getReferencedIndividualURIs();
  Set<String> getReferencedVariableNames();
}
