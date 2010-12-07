
package edu.stanford.smi.protegex.owl.swrl.bridge;

import java.util.List;
import java.util.Set;

import edu.stanford.smi.protegex.owl.swrl.exceptions.SWRLRuleEngineException;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLDeclarationAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.SWRLAtom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.SWRLBuiltInAtom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.SWRLRule;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.SQWRLException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.impl.SQWRLResultImpl;

/*
 * This interface defines a processor that imports SWRL rules and SQWRL queries from an active ontology and determines the OWL axioms
 * necessary to process those rules or queries. These axioms can then be transferred to a target rule engine for processing. 
 * An implementation of this class will attempt to ensure that only necessary axioms are imported.     
 */
public interface OWLAxiomProcessor 
{ 
	void reset();
	
	void processSWRLRules() throws SWRLRuleEngineException;
	void processSQWRLQuery(String queryName) throws SWRLRuleEngineException;
	
	int getNumberOfReferencedSWRLRules();
	int getNumberOfReferencedOWLAxioms();
  int getNumberOfReferencedOWLClassDeclarationAxioms();
	int getNumberOfReferencedOWLPropertyDeclarationAxioms();
  int getNumberOfReferencedOWLIndividualDeclarationAxioms();
  
  Set<SWRLRule> getReferencedSWRLRules();
  Set<OWLDeclarationAxiom> getReferencedOWLDeclarationAxioms();
  Set<OWLDeclarationAxiom> getReferencedOWLClassDeclarationsAxioms();
  Set<OWLDeclarationAxiom> getReferencedOWLPropertyDeclarationAxioms();
  Set<OWLDeclarationAxiom> getReferencedOWLIndividualDeclarationAxioms();
  Set<OWLAxiom> getReferencedOWLAxioms();

  boolean isReferencedOWLClass(String uri);
  boolean isReferencedOWLIndividual(String uri);
  boolean isReferencedOWLObjectProperty(String uri);
  boolean isReferencedOWLDataProperty(String uri);
	
	Set<String> getReferencedOWLClassURIs();
	Set<String> getReferencedOWLPropertyURIs();
	Set<String> getReferencedOWLIndividualURIs();
	
	Set<String> getReferencedOWLClassURIs(SWRLRule ruleOrQuery);
	Set<String> getReferencedOWLPropertyURIs(SWRLRule ruleOrQuery);
	Set<String> getReferencedOWLIndividualURIs(SWRLRule ruleOrQuery);

  SWRLRule getSWRLRule(String ruleURI) throws SWRLRuleEngineException;

  boolean isSQWRLQuery(String uri);
  SWRLRule getSQWRLQuery(String queryURI) throws SQWRLException;
  Set<SWRLRule> getSQWRLQueries() throws SQWRLException;
  Set<String> getSQWRLQueryNames() throws SQWRLException;

  List<SWRLBuiltInAtom> getBuiltInAtomsFromHead(SWRLRule ruleOrQuery);
  List<SWRLBuiltInAtom> getBuiltInAtomsFromHead(SWRLRule ruleOrQuery, Set<String> builtInNames);
  List<SWRLBuiltInAtom> getBuiltInAtomsFromBody(SWRLRule ruleOrQuery);
  List<SWRLBuiltInAtom> getBuiltInAtomsFromBody(SWRLRule ruleOrQuery, Set<String> builtInNames);

  SQWRLResultImpl getSQWRLResult(String uri) throws SQWRLException;
  SQWRLResultImpl getSQWRLUnpreparedResult(String uri) throws SQWRLException;
  List<SWRLAtom> getSQWRLPhase1BodyAtoms(SWRLRule query);
  List<SWRLAtom> getSQWRLPhase2BodyAtoms(SWRLRule query);
  boolean usesSQWRLCollections(SWRLRule query);
  
  String getRuleGroupName(String uri);
  void setRuleGroupName(String uri, String ruleGroupName);

  boolean isEnabled(String uri);
  void setEnabled(String uri, boolean isEnabled);
}
