
package edu.stanford.smi.protegex.owl.swrl.owlapi;

import java.util.Set;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.BuiltInException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.OWLConversionFactoryException;
import edu.stanford.smi.protegex.owl.swrl.parser.SWRLParseException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.DataValueConversionException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.SQWRLException;

/**
 * Interface that is very roughly equivalent to the OWLAPI interfaces OWLOntology and OWLOntologyManager. 
 * All SWRLTab code in Protege-OWL will eventually use this interface to interact with an OWL ontology, 
 * thus easing the port to the OWLAPI.
 * 
 * At present, this interface is not fully aligned with the OWLAPI.
 */
public interface OWLOntology
{
  boolean containsClassInSignature(String classURI, boolean includeImportsClosure);
  boolean containsObjectPropertyInSignature(String propertyURI, boolean includeImportsClosure);
  boolean containsDataPropertyInSignature(String propertyURI, boolean includeImportsClosure);
  boolean containsIndividualInSignature(String individualURI, boolean includeImportsClosure);

  Set<OWLSameIndividualAxiom> getSameIndividualAxioms() throws OWLConversionFactoryException;
  Set<OWLDifferentIndividualsAxiom> getOWLDifferentIndividualsAxioms() throws OWLConversionFactoryException;

  Set<OWLPropertyAssertionAxiom> getOWLPropertyAssertionAxioms(String individualURI, String propertyURI) throws OWLConversionFactoryException, DataValueConversionException;
  Set<OWLPropertyAssertionAxiom> getOWLPropertyAssertionAxioms(String propertyURI) throws OWLConversionFactoryException, DataValueConversionException;

  SWRLRule getSWRLRule(String ruleName) throws OWLConversionFactoryException;
	OWLClass getOWLClass(String classURI) throws OWLConversionFactoryException;
  OWLNamedIndividual getOWLIndividual(String individualURI) throws OWLConversionFactoryException;
  OWLObjectProperty getOWLObjectProperty(String propertyURI) throws OWLConversionFactoryException;
  OWLDataProperty getOWLDataProperty(String propertyURI) throws OWLConversionFactoryException;

  Set<OWLNamedIndividual> getAllOWLIndividualsOfClass(String classURI) throws OWLConversionFactoryException;
  
  Set<SWRLRule> getSWRLRules() throws OWLConversionFactoryException, SQWRLException, BuiltInException;
  
  boolean isOWLNamedIndividualOfClass(String individualURI, String classURI);
  boolean isSWRLBuiltIn(String builtInURI);
  boolean couldBeOWLNamedClass(String classURI);
  boolean isValidURI(String uri);
    
  String uri2PrefixedName(String uri); 
  String prefixedName2URI(String prefixedName);

  // Write methods
  void writeOWLClassDeclaration(OWLClass owlClass) throws OWLConversionFactoryException;
  void writeOWLIndividualDeclaration(OWLNamedIndividual owlIndividual) throws OWLConversionFactoryException;
  void writeOWLAxiom(OWLAxiom axiom) throws OWLConversionFactoryException;

  // Creation methods
  OWLClass createOWLClass();
  String createNewResourceURI(String prefix);
  SWRLRule createSWRLRule(String ruleName, String ruleText) throws OWLConversionFactoryException, SWRLParseException;
  
  void deleteSWRLRule(String ruleURI) throws OWLConversionFactoryException;
  
  OWLModel getOWLModel(); // TODO: Protege-OWL dependency
}
  
