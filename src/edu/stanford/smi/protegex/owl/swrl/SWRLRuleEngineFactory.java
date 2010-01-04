
package edu.stanford.smi.protegex.owl.swrl;

import edu.stanford.smi.protegex.owl.swrl.exceptions.SWRLRuleEngineException;
import edu.stanford.smi.protegex.owl.swrl.bridge.BridgeFactory;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.SWRLRuleEngineBridgeException;

import edu.stanford.smi.protegex.owl.model.OWLModel;

/**
 ** Factory for creating SWRL rule engines
 */
public class SWRLRuleEngineFactory
{
  public static SWRLRuleEngine create(OWLModel owlModel) throws SWRLRuleEngineException
  {
    SWRLRuleEngine ruleEngine = null;

    try {
      ruleEngine = BridgeFactory.createBridge(owlModel);
    } catch (SWRLRuleEngineBridgeException e) {
      throw new SWRLRuleEngineException("error creating SWRL rule engine: " + e.getMessage());
    } // try
    
    return ruleEngine;
  } // create

  public static SWRLRuleEngine create(String ruleEngineName, OWLModel owlModel) throws SWRLRuleEngineException
  {
    SWRLRuleEngine ruleEngine = null;

    try {
      ruleEngine = BridgeFactory.createBridge(ruleEngineName, owlModel);
    } catch (SWRLRuleEngineBridgeException e) {
      throw new SWRLRuleEngineException("error creating SWRL rule engine '" + ruleEngineName + "': " + e.getMessage());
    } // try
    
    return ruleEngine;
  } // create

} // SWRLRuleEngineFactory
