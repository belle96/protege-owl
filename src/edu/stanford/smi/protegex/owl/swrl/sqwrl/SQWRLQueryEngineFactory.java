
package edu.stanford.smi.protegex.owl.swrl.sqwrl;

import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.SQWRLException;
import edu.stanford.smi.protegex.owl.swrl.bridge.BridgeFactory;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.SWRLRuleEngineBridgeException;

import edu.stanford.smi.protegex.owl.model.OWLModel;

/**
 * Factory for creating a SQWRL query engine
 */
public class SQWRLQueryEngineFactory
{
  public static SQWRLQueryEngine create(OWLModel owlModel) throws SQWRLException
  {
    SQWRLQueryEngine queryEngine = null;

    try {
      queryEngine = BridgeFactory.createBridge(owlModel);
    } catch (SWRLRuleEngineBridgeException e) {
      throw new SQWRLException("error creating SQWRL query engine: " + e.getMessage());
    } // try
    
    return queryEngine;
  } // create

  public static SQWRLQueryEngine create(String queryEngineName, OWLModel owlModel) throws SQWRLException
  {
    SQWRLQueryEngine queryEngine = null;

    try {
      queryEngine = BridgeFactory.createBridge(queryEngineName, owlModel);
    } catch (SWRLRuleEngineBridgeException e) {
      throw new SQWRLException("error creating SQWRL query engine '" + queryEngineName + "': " + e.getMessage());
    } // try
    
    return queryEngine;
  } // create

} // SQWRLQueryEngineFactory
