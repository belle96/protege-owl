
package edu.stanford.smi.protegex.owl.swrl.bridge;

import java.util.HashMap;
import java.util.Set;
import java.util.logging.Logger;

import edu.stanford.smi.protege.plugin.PluginUtilities;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.swrl.SWRLRuleEngine;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.InvalidSWRLRuleEngineNameException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.NoRegisteredRuleEnginesException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.SWRLRuleEngineBridgeException;
import edu.stanford.smi.protegex.owl.swrl.bridge.impl.DefaultSWRLBridge;
import edu.stanford.smi.protegex.owl.swrl.bridge.impl.DefaultSWRLRuleEngine;
import edu.stanford.smi.protegex.owl.swrl.bridge.impl.SWRLProcessorImpl;
import edu.stanford.smi.protegex.owl.swrl.exceptions.SWRLRuleEngineException;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLOntology;
import edu.stanford.smi.protegex.owl.swrl.owlapi.impl.OWLOntologyImpl;

/**
 * Factory to create instances of bridges
 */
public class SWRLRuleEngineFactory
{
  private static transient final Logger log = Log.getLogger(SWRLRuleEngineFactory.class);
  private static HashMap<String, TargetSWRLRuleEngineCreator> registeredSWRLRuleEngines;

  static {
    registeredSWRLRuleEngines = new HashMap<String, TargetSWRLRuleEngineCreator>();
  } // static

  static {
    Class<?> cls = PluginUtilities.forName("edu.stanford.smi.protegex.owl.swrl.bridge.jess.JessSWRLRuleEngine", true);
    if (cls == null) System.err.println("SWRLJessBridge load failed - could not find class");
   } // static

  public static void registerRuleEngine(String pluginName, TargetSWRLRuleEngineCreator ruleEngineCreator)
  {
    if (registeredSWRLRuleEngines.containsKey(pluginName)) {
      registeredSWRLRuleEngines.remove(pluginName);
      registeredSWRLRuleEngines.put(pluginName, ruleEngineCreator);
    } else registeredSWRLRuleEngines.put(pluginName, ruleEngineCreator);

    log.info("Rule engine '" + pluginName + "' registered with the SWRLTab.");
  }

  public static boolean isRuleEngineRegistered(String pluginName) { return registeredSWRLRuleEngines.containsKey(pluginName); }
  public static Set<String> getRegisteredRuleEngineNames() { return registeredSWRLRuleEngines.keySet(); }

  /**
   * Create an instance of a rule engine - a random registered engine is returned. If no engine is registered, a
   * NoRegisteredRuleEnginesException is returned.
   */
  public static SWRLRuleEngine create(OWLModel owlModel) throws SWRLRuleEngineException
  {
    if (!registeredSWRLRuleEngines.isEmpty()) 
    	return create(registeredSWRLRuleEngines.keySet().iterator().next(), owlModel);
    else throw new NoRegisteredRuleEnginesException();
  } 

  /**
   * Create a rule engine. Throws an InvalidSWRLRuleEngineNameException if an engine of this name is not registered.
   */
  public static SWRLRuleEngine create(String pluginName, OWLModel owlModel) throws SWRLRuleEngineException
  {
  	SWRLRuleEngine ruleEngine = null;
    DefaultSWRLBridge bridge = null;
    TargetSWRLRuleEngine targetRuleEngine = null;

    if (registeredSWRLRuleEngines.containsKey(pluginName)) {

      try {
      	OWLOntology activeOntology = new OWLOntologyImpl(owlModel);
      	SWRLProcessor swrlProcessor = new SWRLProcessorImpl(activeOntology); 
      	bridge = new DefaultSWRLBridge(activeOntology, swrlProcessor);
        targetRuleEngine = registeredSWRLRuleEngines.get(pluginName).create(bridge);
        bridge.setTargetRuleEngine(targetRuleEngine);
        ruleEngine = new DefaultSWRLRuleEngine(activeOntology, swrlProcessor, targetRuleEngine, bridge, bridge);       
      } catch (Throwable e) {
        throw new SWRLRuleEngineException("Error creating rule engine '" + pluginName + "': " + e.getMessage());
      } // try

    } else 
    	throw new InvalidSWRLRuleEngineNameException(pluginName);

    return ruleEngine;
  } 

  public static void unregisterSWRLRuleEngine(String bridgeName)
  {
    if (registeredSWRLRuleEngines.containsKey(bridgeName)) registeredSWRLRuleEngines.remove(bridgeName);
  } 

  public interface TargetSWRLRuleEngineCreator
  {
    TargetSWRLRuleEngine create(SWRLRuleEngineBridge bridge) throws SWRLRuleEngineBridgeException;
  } 
} 
