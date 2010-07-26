
package edu.stanford.smi.protegex.owl.swrl;

import java.util.Set;

import edu.stanford.smi.protegex.owl.swrl.bridge.OWLDataValueFactory;
import edu.stanford.smi.protegex.owl.swrl.bridge.SWRLRule;
import edu.stanford.smi.protegex.owl.swrl.exceptions.SWRLRuleEngineException;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLClass;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLNamedIndividual;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.SQWRLQueryEngine;

/**
 * This interface defines methods that must be provided by a SWRL rule engine.
 * 
 * Detailed documentation for this mechanism can be found <a href="http://protege.cim3.net/cgi-bin/wiki.pl?SWRLRuleEngineBridgeFAQ">here</a>.
 */
public interface SWRLRuleEngine extends SQWRLQueryEngine
{
  /**
   * Load rules and knowledge from OWL into bridge, send them to a rule engine, run the rule engine, and write any inferred knowledge back
   * to OWL.
   */
  void infer() throws SWRLRuleEngineException;

  /**
   * Load rules and knowledge from OWL into bridge. All existing bridge rules and knowledge will first be cleared and the associated rule
   * engine will be reset.
   */
  void importSWRLRulesAndOWLKnowledge() throws SWRLRuleEngineException;

  /**
   * Run the rule engine.
   */
  void run() throws SWRLRuleEngineException;

  /**
   * Write knowledge inferred by rule engine back to OWL.
   */
  void writeInferredKnowledge2OWL() throws SWRLRuleEngineException;

  /**
   *  Clear all inferred and injected knowledge from rule engine, deleted asserted knowledge from the bridge, and leave imported bridge
   *  knowledge intact.
   */
  void reset() throws SWRLRuleEngineException;

  // Convenience methods to display rule engine activity
  int getNumberOfImportedSWRLRules();
  int getNumberOfImportedOWLAxioms();
  int getNumberOfInferredOWLAxioms();
  int getNumberOfInjectedOWLAxioms();
  int getNumberOfImportedOWLClasses();
  int getNumberOfImportedOWLIndividuals();
  int getNumberOfInferredOWLIndividuals();
  int getNumberOfInjectedOWLClasses();
  int getNumberOfInjectedOWLIndividuals();

  Set<OWLAxiom> getImportedOWLAxioms();
  Set<OWLAxiom> getInferredOWLAxioms();
  Set<OWLAxiom> getInjectedOWLAxioms();
  Set<SWRLRule> getImportedSWRLRules();
  Set<OWLClass> getImportedOWLClasses();
  Set<OWLClass> getInjectedOWLClasses();
  Set<OWLNamedIndividual> getImportedOWLIndividuals();
  Set<OWLNamedIndividual> getReclassifiedOWLIndividuals();
  Set<OWLNamedIndividual> getInjectedOWLIndividuals();

  // TODO: temporary
  String uri2PrefixedName(String uri);
  String name2URI(String prefixedName);
  OWLDataValueFactory getOWLDataValueFactory();
}
