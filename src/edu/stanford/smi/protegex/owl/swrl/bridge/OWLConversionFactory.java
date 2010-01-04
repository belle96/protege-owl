
package edu.stanford.smi.protegex.owl.swrl.bridge;

import java.util.Set;

import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.BuiltInException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.DataValueConversionException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.OWLConversionFactoryException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.SQWRLException;

/**
 * Interface to insert and get OWLAPI-like entities into and from a Protege-OWL model.
 */
public interface OWLConversionFactory
{
  OWLClass getOWLClass();
  OWLClass getOWLClass(String classURI) throws OWLConversionFactoryException;
  OWLIndividual getOWLIndividual(String individualURI) throws OWLConversionFactoryException;
  OWLObjectProperty getOWLObjectProperty(String propertyURI) throws OWLConversionFactoryException;
  OWLDataProperty getOWLDataProperty(String propertyURI) throws OWLConversionFactoryException;
  SWRLRule getSWRLRule(String ruleURI) throws OWLConversionFactoryException, SQWRLException, BuiltInException;
  Set<SWRLRule> getSWRLRules() throws OWLConversionFactoryException, SQWRLException, BuiltInException;
  
  Set<OWLPropertyAssertionAxiom> getOWLPropertyAssertionAxioms(String individualURI, String propertyURI) throws OWLConversionFactoryException, DataValueConversionException;
  Set<OWLPropertyAssertionAxiom> getOWLPropertyAssertionAxioms(String propertyURI) throws OWLConversionFactoryException, DataValueConversionException;

  boolean isOWLClass(String className);
  boolean isOWLProperty(String propertyName);
  boolean isOWLObjectProperty(String propertyName);
  boolean isOWLDataProperty(String propertyName);
  boolean isOWLIndividual(String individualName);
  boolean isOWLIndividualOfClass(String individualName, String className);
  boolean isSWRLBuiltIn(String builtInName);
  String createNewResourceName(String prefix);
  boolean isValidURI(String uri);
  void putOWLClass(OWLClass owlClass) throws OWLConversionFactoryException;
  void putOWLIndividual(OWLIndividual owlIndividual) throws OWLConversionFactoryException;
  void putOWLAxiom(OWLAxiom axiom) throws OWLConversionFactoryException;
} // OWLConversionFactory
  
