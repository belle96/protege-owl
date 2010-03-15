
package edu.stanford.smi.protegex.owl.swrl.bridge;

import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.TargetSWRLRuleEngineException;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLAxiom;

import java.util.List;

/**
 * This interface defines the methods that must be provided by an implementation of a SWRL rule engine.<p>
 *
 * A rule engine can communicate with the bridge using the SWRLRuleEngineBridge interface. The engine can use the bridge 
 * to, for example, infer axioms or to invoke built-ins.<p>
 *
 * Detailed documentation for this mechanism can be found <a href="http://protege.cim3.net/cgi-bin/wiki.pl?SWRLRuleEngineBridgeFAQ">here</a>.
 */
public interface TargetSWRLRuleEngine
{
  /**
   * Initialize the target engine. Supply it with the bridge.
   */
  void initialize(SWRLRuleEngineBridge bridge) throws TargetSWRLRuleEngineException;

  /**
   * Reset the rule engine.
   */
  void resetRuleEngine() throws TargetSWRLRuleEngineException;
  
  /**
   * Run the rule engine.
   */
  void runRuleEngine() throws TargetSWRLRuleEngineException;

  /**
   * Define a target rule engine representation of an OWL axiom. SWRL rules are a type of OWL axiom.
   */
  void defineOWLAxiom(OWLAxiom axiom) throws TargetSWRLRuleEngineException;
  
  /**
   * Define a rule engine representation of one set of the arguments generated by a built-in that binds one or more of its arguments. <p>
   *
   * For example, if tbox:isSubClassOf(?x, Person) is called with unbound argument ?x and Person has subclasses Male and Female then
   * two arguments bindings (Male, Person) and (Female, Person) will be generated by the built-in bridge and passed to this method. 
   */
  void defineBuiltInArgumentBinding(String ruleName, String builtInName, int builtInIndex, List<BuiltInArgument> arguments) throws TargetSWRLRuleEngineException;
}
