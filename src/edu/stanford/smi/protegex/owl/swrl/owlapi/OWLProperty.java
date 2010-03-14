
package edu.stanford.smi.protegex.owl.swrl.owlapi;

import java.util.Set;

/**
 * Interface representing an instance of an OWL property
 */
public interface OWLProperty extends OWLEntity
{
  Set<OWLClass> getDomainClasses();
  Set<OWLClass> getRangeClasses();
  Set<OWLProperty> getTypes();
  Set<OWLProperty> getSuperProperties();
  Set<OWLProperty> getSubProperties();
  Set<OWLProperty> getEquivalentProperties();
} // OWLProperty
