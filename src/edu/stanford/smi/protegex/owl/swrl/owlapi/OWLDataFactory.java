
package edu.stanford.smi.protegex.owl.swrl.owlapi;

import java.util.List;
import java.util.Set;

import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInAtom;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLDataValue;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLPropertyPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.bridge.SWRLRule;
import edu.stanford.smi.protegex.owl.swrl.bridge.VariableAtomArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.VariableBuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.OWLFactoryException;

/**
 * Factory to create OWLAPI-like entities. Provides a rough starting point for a port to the OWLAPI. 
 * 
 * We use a local OWLDataValue class instead of the OWLAPI OWLLiteral class for the moment.
 */
public interface OWLDataFactory
{
  Set<SWRLRule> getSWRLRules() throws OWLFactoryException;
  SWRLRule getSWRLRule(String ruleName) throws OWLFactoryException;
  BuiltInAtom getSWRLBuiltInAtom(String builtInURI, String builtInPrefixedName, List<BuiltInArgument> arguments);

  OWLClass getOWLClass();
  OWLClass getOWLClass(String classURI);
  
  OWLIndividual getOWLIndividual(String individualURI);
  
  OWLObjectProperty getOWLObjectProperty(String propertyURI);
  OWLDataProperty getOWLDataProperty(String propertyURI);
  
  OWLDataPropertyAssertionAxiom getOWLDataPropertyAssertionAxiom(OWLIndividual subject, OWLProperty property, OWLDataValue object);
  OWLObjectPropertyAssertionAxiom getOWLObjectPropertyAssertionAxiom(OWLIndividual subject, OWLProperty property, OWLIndividual object);
  OWLDifferentIndividualsAxiom getOWLDifferentIndividualsAxiom(OWLIndividual individual1, OWLIndividual individual2);
  OWLDifferentIndividualsAxiom getOWLDifferentIndividualsAxiom(Set<OWLIndividual> individuals);
  OWLSameIndividualAxiom getOWLSameIndividualAxiom(OWLIndividual individual1, OWLIndividual individual2);
  OWLClassAssertionAxiom getOWLClassAssertionAxiom(OWLIndividual individual, OWLClass description);
  OWLSubClassAxiom getOWLSubClassAxiom(OWLClass subClass, OWLClass superClass);
  OWLSomeValuesFrom getOWLSomeValuesFrom(OWLClass owlClass, OWLProperty onProperty, OWLClass someValuesFrom);
  OWLDeclarationAxiom getOWLDeclarationAxiom(OWLEntity owlEntity);

  VariableAtomArgument getSWRLVariableAtomArgument(String variableName);
  VariableBuiltInArgument getSWRLVariableBuiltInArgument(String variableName);
  BuiltInArgument getSWRLBuiltInArgument(String variableName);

  // The following do not have corresponding methods in the OWLAPI.
  OWLClassPropertyAssertionAxiom getOWLClassPropertyAssertionAxiom(OWLIndividual subject, OWLProperty property, OWLClass object);
  OWLPropertyPropertyAssertionAxiom getOWLPropertyPropertyAssertionAxiom(OWLIndividual subject, OWLProperty property, OWLProperty object);
} // OWLDataFactory
