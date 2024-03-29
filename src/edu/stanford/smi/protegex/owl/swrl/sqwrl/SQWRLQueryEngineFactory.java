
package edu.stanford.smi.protegex.owl.swrl.sqwrl;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.swrl.bridge.SWRLRuleEngineFactory;
import edu.stanford.smi.protegex.owl.swrl.exceptions.SWRLRuleEngineException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.SQWRLException;

/**
 * Factory for creating a SQWRL query engine
 */
public class SQWRLQueryEngineFactory
{
  public static SQWRLQueryEngine create(OWLModel owlModel) throws SQWRLException
  {
    SQWRLQueryEngine queryEngine = null;

    try {
      queryEngine = SWRLRuleEngineFactory.create(owlModel);
    } catch (SWRLRuleEngineException e) {
      throw new SQWRLException("error creating SQWRL query engine: " + e.getMessage());
    } // try
    
    return queryEngine;
  }

  public static SQWRLQueryEngine create(String pluginName, OWLModel owlModel) throws SQWRLException
  {
    SQWRLQueryEngine queryEngine = null;

    try {
      queryEngine = SWRLRuleEngineFactory.create(pluginName, owlModel);
    } catch (SWRLRuleEngineException e) {
      throw new SQWRLException("error creating SQWRL query engine using '" + pluginName + "': " + e.getMessage());
    } // try
    
    return queryEngine;
  }
}
