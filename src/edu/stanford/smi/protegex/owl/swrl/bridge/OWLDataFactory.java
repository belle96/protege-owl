
package edu.stanford.smi.protegex.owl.swrl.bridge;

import java.util.List;
import java.util.Set;

import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.DataValueConversionException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.OWLFactoryException;
import edu.stanford.smi.protegex.owl.swrl.bridge.xsd.XSDType;

/**
 ** Factory to create OWLAPI-like entities. Provides a rough starting point for a port to the OWLAPI. 
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
  
  OWLDataValue getOWLDataValue(Object o) throws DataValueConversionException; // TODO: get rid of this
  OWLDataValue getOWLDataValue(String s);
  OWLDataValue getOWLDataValue(Number n);
  OWLDataValue getOWLDataValue(boolean b);
  OWLDataValue getOWLDataValue(int i);
  OWLDataValue getOWLDataValue(long l);
  OWLDataValue getOWLDataValue(float f);
  OWLDataValue getOWLDataValue(double d);
  OWLDataValue getOWLDataValue(short s);
  OWLDataValue getOWLDataValue(Byte b);
  OWLDataValue getOWLDataValue(XSDType xsd);

  OWLDataPropertyAssertionAxiom getOWLDataPropertyAssertionAxiom(OWLIndividual subject, OWLProperty property, OWLDataValue object);
  OWLObjectPropertyAssertionAxiom getOWLObjectPropertyAssertionAxiom(OWLIndividual subject, OWLProperty property, OWLIndividual object);
  OWLDifferentIndividualsAxiom getOWLDifferentIndividualsAxiom(OWLIndividual individual1, OWLIndividual individual2);
  OWLDifferentIndividualsAxiom getOWLDifferentIndividualsAxiom(Set<OWLIndividual> individuals);
  OWLSameIndividualsAxiom getOWLSameIndividualsAxiom(OWLIndividual individual1, OWLIndividual individual2);
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
