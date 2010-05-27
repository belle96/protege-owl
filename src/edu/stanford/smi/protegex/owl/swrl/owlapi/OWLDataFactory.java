
package edu.stanford.smi.protegex.owl.swrl.owlapi;

import java.util.List;
import java.util.Set;

import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInAtom;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLPropertyPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.bridge.SWRLRule;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.OWLFactoryException;

/**
 * Factory to create OWLAPI-like entities. Provides a rough starting point for a port to the OWLAPI.  
 */
public interface OWLDataFactory
{
  Set<SWRLRule> getSWRLRules() throws OWLFactoryException;
  SWRLRule getSWRLRule(String ruleName) throws OWLFactoryException;
  BuiltInAtom getSWRLBuiltInAtom(String builtInURI, String builtInPrefixedName, List<BuiltInArgument> arguments);

  OWLClass getOWLClass(String classURI);
  OWLNamedIndividual getOWLIndividual(String individualURI);
  OWLObjectProperty getOWLObjectProperty(String propertyURI);
  OWLDataProperty getOWLDataProperty(String propertyURI);
  
  OWLDataPropertyAssertionAxiom getOWLDataPropertyAssertionAxiom(OWLNamedIndividual subject, OWLProperty property, OWLLiteral object);
  OWLObjectPropertyAssertionAxiom getOWLObjectPropertyAssertionAxiom(OWLNamedIndividual subject, OWLProperty property, OWLNamedIndividual object);
  OWLDifferentIndividualsAxiom getOWLDifferentIndividualsAxiom(OWLNamedIndividual individual1, OWLNamedIndividual individual2);
  OWLDifferentIndividualsAxiom getOWLDifferentIndividualsAxiom(Set<OWLNamedIndividual> individuals);
  OWLSameIndividualAxiom getOWLSameIndividualAxiom(OWLNamedIndividual individual1, OWLNamedIndividual individual2);
  OWLClassAssertionAxiom getOWLClassAssertionAxiom(OWLNamedIndividual individual, OWLClass description);
  OWLSubClassAxiom getOWLSubClassAxiom(OWLClass subClass, OWLClass superClass);
  OWLSomeValuesFrom getOWLSomeValuesFrom(OWLClass owlClass, OWLProperty onProperty, OWLClass someValuesFrom);
  OWLDeclarationAxiom getOWLDeclarationAxiom(OWLEntity owlEntity);
  
  OWLTypedLiteral getOWLTypedLiteral(int value);
  OWLTypedLiteral getOWLTypedLiteral(float value);
  OWLTypedLiteral getOWLTypedLiteral(double value);
  OWLTypedLiteral getOWLTypedLiteral(boolean value);
  OWLTypedLiteral getOWLTypedLiteral(String value);
  
  // The following do not have corresponding methods in the OWLAPI.
  OWLClassPropertyAssertionAxiom getOWLClassPropertyAssertionAxiom(OWLNamedIndividual subject, OWLProperty property, OWLClass object);
  OWLPropertyPropertyAssertionAxiom getOWLPropertyPropertyAssertionAxiom(OWLNamedIndividual subject, OWLProperty property, OWLProperty object);
}
