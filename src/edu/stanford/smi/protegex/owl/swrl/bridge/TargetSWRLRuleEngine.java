
package edu.stanford.smi.protegex.owl.swrl.bridge;

import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.SWRLRuleEngineBridgeException;

import java.util.List;

/**
 ** This interface defines the methods that must be provided by an implementation of a SWRL rule engine.<p>
 **
 ** A rule engine can communicate with the bridge using a SWRLRuleEngineBridge interface.
 **
 ** Detailed documentation for this mechanism can be found <a href="http://protege.cim3.net/cgi-bin/wiki.pl?SWRLRuleEngineBridgeFAQ">here</a>.
 */
public interface TargetSWRLRuleEngine
{
  void initialize(SWRLRuleEngineBridge bridge) throws SWRLRuleEngineBridgeException;

  void resetRuleEngine() throws SWRLRuleEngineBridgeException;
  void runRuleEngine() throws SWRLRuleEngineBridgeException;

  void defineSWRLRule(SWRLRule rule) throws SWRLRuleEngineBridgeException;
  void defineOWLClass(OWLClass owlClass) throws SWRLRuleEngineBridgeException;
  void defineOWLIndividual(OWLIndividual owlIndividual) throws SWRLRuleEngineBridgeException;
  void defineOWLAxiom(OWLAxiom axiom) throws SWRLRuleEngineBridgeException;
  void defineBuiltInBinding(String ruleName, String builtInName, int builtInIndex, List<BuiltInArgument> arguments) throws SWRLRuleEngineBridgeException;

} // TargetSWRLRuleEngine
