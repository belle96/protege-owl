
package edu.stanford.smi.protegex.owl.swrl.bridge;

import java.util.Set;

/**
 * Interface representing an instance of an OWL property
 */
public interface OWLProperty extends OWLEntity
{
  Set<OWLClass> getDomainClasses();
  Set<OWLClass> getRangeClasses();
  Set<OWLProperty> getSuperProperties();
  Set<OWLProperty> getSubProperties();
  Set<OWLProperty> getEquivalentProperties();
} // OWLProperty
