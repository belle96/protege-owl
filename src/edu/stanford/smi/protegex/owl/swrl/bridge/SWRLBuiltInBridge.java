
package edu.stanford.smi.protegex.owl.swrl.bridge;

import java.util.List;
import java.util.Set;

import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.SWRLBuiltInBridgeException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.SWRLRuleEngineBridgeException;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLClass;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLDataFactory;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLNamedIndividual;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLOntology;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.SQWRLException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.impl.SQWRLResultImpl;

/**
 * The SWRL Built-in Bridge defines the methods seen by built-in implementations at run time. Ideally, built-in implementations should only
 * use this interface to operate on the active ontology. However, some specialized libraries (e.g., abox and tbox) require direct access to
 * the active ontology so will use the provided getOWLModel method. It will be removed shortly.
 *
 * Detailed documentation for the SWRL rule engine bridge mechanism can be found <a
 * href="http://protege.cim3.net/cgi-bin/wiki.pl?SWRLRuleEngineBridgeFAQ">here</a>.
 */
public interface SWRLBuiltInBridge
{
  OWLOntology getActiveOntology();

  // The inject methods can be used by built-ins to inject new axioms into a bridge, which will also reflect them in the underlying
  // engine. Eventually collapse all inject methods into injectOWLAxiom.
  void injectOWLAxiom(OWLAxiom axiom) throws SWRLBuiltInBridgeException;

  // TODO: the following methods should be subsumed by injectOWLAxiom
  OWLClass injectOWLClassDeclaration() throws SWRLBuiltInBridgeException;
  void injectOWLClassDeclaration(String className) throws SWRLBuiltInBridgeException;
  OWLNamedIndividual injectOWLIndividualDeclaration() throws SWRLBuiltInBridgeException;
  void injectOWLIndividualDeclaration(OWLNamedIndividual owlIndividual) throws SWRLBuiltInBridgeException;
  OWLNamedIndividual injectOWLIndividualDeclaration(OWLClass owlClass) throws SWRLBuiltInBridgeException;

  boolean isOWLClass(String classURI);
  boolean isOWLObjectProperty(String propertyURI);
  boolean isOWLDataProperty(String propertyURI);
  boolean isOWLIndividual(String individualURI);
  boolean isOWLIndividualOfClass(String individualURI, String classURI);
  
  Set<OWLNamedIndividual> getOWLIndividuals();

  Set<OWLPropertyAssertionAxiom> getOWLPropertyAssertionAxioms(String individualURI, String propertyURI) throws SWRLBuiltInBridgeException;

  SQWRLResultImpl getSQWRLUnpreparedResult(String queryURI) throws SQWRLException;

  OWLDataFactory getOWLDataFactory();
  OWLDataValueFactory getOWLDataValueFactory();
  
  // TODO: temporary 
  boolean invokeSWRLBuiltIn(String ruleName, String builtInName, int builtInIndex, boolean isInConsequent, List<BuiltInArgument> arguments) 
    throws SWRLRuleEngineBridgeException;
} 
