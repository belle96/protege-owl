
package edu.stanford.smi.protegex.owl.swrl;

import edu.stanford.smi.protegex.owl.swrl.bridge.SWRLRule;
import edu.stanford.smi.protegex.owl.swrl.exceptions.SWRLRuleEngineException;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLClass;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLIndividual;

import java.util.Set;

/**
 ** This interface defines the methods that must be provided by a SWRL rule engine.
 **
 */
public interface SWRLRuleEngine
{
  /**
   ** Load rules and knowledge from OWL into bridge, send them to a rule engine, run the rule engine, and write any inferred knowledge back
   ** to OWL.
   */
  void infer() throws SWRLRuleEngineException;

  /**
   ** Load rules and knowledge from OWL into bridge. All existing bridge rules and knowledge will first be cleared and the associated rule
   ** engine will be reset.
   */
  void importSWRLRulesAndOWLKnowledge() throws SWRLRuleEngineException;
  void importSWRLRulesAndOWLKnowledge(String ruleGroupName) throws SWRLRuleEngineException;
  void importSWRLRulesAndOWLKnowledge(Set<String> ruleGroupNames) throws SWRLRuleEngineException;

  /**
   ** Run the rule engine.
   */
  void run() throws SWRLRuleEngineException;

  /**
   ** Write knowledge inferred by rule engine back to OWL.
   */
  void writeInferredKnowledge2OWL() throws SWRLRuleEngineException;

  /**
   **  Clear all inferred and injected knowledge from rule engine, deleted asserted knowledge from the bridge, and leave imported bridge
   **  knowledge intact.
   */
  void reset() throws SWRLRuleEngineException;

  // Convenience methods to display bridge activity
  int getNumberOfImportedAxioms();
  int getNumberOfInferredAxioms();
  int getNumberOfInjectedAxioms();
  int getNumberOfImportedSWRLRules();
  int getNumberOfImportedClasses();
  int getNumberOfImportedIndividuals();
  int getNumberOfInferredIndividuals();
  int getNumberOfInjectedClasses();
  int getNumberOfInjectedIndividuals();

  Set<OWLAxiom> getImportedAxioms();
  Set<OWLAxiom> getInferredAxioms();
  Set<OWLAxiom> getInjectedAxioms();

  Set<SWRLRule> getImportedSWRLRules();
  Set<OWLClass> getImportedClasses();
  Set<OWLIndividual> getImportedIndividuals();
  Set<OWLIndividual> getInferredIndividuals();
  Set<OWLClass> getInjectedClasses();
  Set<OWLIndividual> getInjectedIndividuals();
} // SWRLRuleEngine
