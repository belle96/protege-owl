
package edu.stanford.smi.protegex.owl.swrl.bridge;

import java.util.List;
import java.util.Set;

import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.BuiltInException;
import edu.stanford.smi.protegex.owl.swrl.exceptions.SWRLRuleEngineException;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLClass;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLNamedIndividual;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLProperty;
import edu.stanford.smi.protegex.owl.swrl.owlapi.SWRLAtom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.SWRLBuiltInAtom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.SWRLRule;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.SQWRLException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.impl.SQWRLResultImpl;

public interface SWRLProcessor 
{ 
	void importReferencedOWLAxioms() throws SWRLRuleEngineException;
	void importSWRLRulesAndOWLAxioms() throws SWRLRuleEngineException;
	void importSQWRLQueryAndOWLAxioms(String queryName) throws SWRLRuleEngineException;
	
	void process(SWRLRule ruleOrQuery) throws BuiltInException;
	void reset();
	
	int getNumberOfImportedSWRLRules();
	int getNumberOfImportedOWLClassDeclarations();
	int getNumberOfImportedOWLPropertyDeclarations();
  int getNumberOfImportedOWLIndividualDeclarations();
  int getNumberOfImportedOWLAxioms();
  
  SWRLRule getSWRLRule(String ruleURI) throws SWRLRuleEngineException;

  boolean isSQWRLQuery(String uri);
  SWRLRule getSQWRLQuery(String queryURI) throws SQWRLException;
  Set<SWRLRule> getSQWRLQueries() throws SQWRLException;
  Set<String> getSQWRLQueryNames() throws SQWRLException;

  Set<SWRLRule> getImportedSWRLRules();
  Set<OWLClass> getImportedOWLClassDeclarations();
  Set<OWLProperty> getImportedOWLPropertyDeclarations();
  Set<OWLNamedIndividual> getImportedOWLIndividualDeclarations();
  Set<OWLAxiom> getImportedOWLAxioms();
  
  boolean isImportedOWLClass(String uri);
  boolean isImportedOWLIndividual(String uri);
  boolean isImportedOWLObjectProperty(String uri);
  boolean isImportedOWLDataProperty(String uri);
	
	Set<String> getReferencedOWLClassURIs();
	Set<String> getReferencedOWLPropertyURIs();
	Set<String> getReferencedOWLIndividualURIs();
	
	Set<String> getReferencedOWLClassURIs(SWRLRule ruleOrQuery);
	Set<String> getReferencedOWLPropertyURIs(SWRLRule ruleOrQuery);
	Set<String> getReferencedOWLIndividualURIs(SWRLRule ruleOrQuery);

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
