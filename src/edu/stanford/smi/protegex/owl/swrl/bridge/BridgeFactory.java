
// TODO: should probably a more specific exception - not SWRLRuleEngineBridgeException

package edu.stanford.smi.protegex.owl.swrl.bridge;

import java.util.HashMap;
import java.util.Set;
import java.util.logging.Logger;

import edu.stanford.smi.protege.plugin.PluginUtilities;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.InvalidBridgeNameException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.NoRegisteredBridgesException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.SWRLRuleEngineBridgeException;
import edu.stanford.smi.protegex.owl.swrl.bridge.impl.DefaultSWRLRuleEngineBridge;

/**
 * Factory to create instances of bridges
 */
public class BridgeFactory
{
  private static transient final Logger log = Log.getLogger(BridgeFactory.class);
  private static HashMap<String, TargetSWRLRuleEngineCreator> registeredRuleEngines;

  static {
    registeredRuleEngines = new HashMap<String, TargetSWRLRuleEngineCreator>();
  } // static

  static {
    Class<?> cls = PluginUtilities.forName("edu.stanford.smi.protegex.owl.swrl.bridge.jess.JessSWRLRuleEngine", true);
    if (cls == null) System.err.println("SWRLJessBridge load failed - could not find class");
   } // static

  public static void registerRuleEngine(String ruleEngineName, TargetSWRLRuleEngineCreator bridgeCreator)
  {
    if (registeredRuleEngines.containsKey(ruleEngineName)) {
      registeredRuleEngines.remove(ruleEngineName);
      registeredRuleEngines.put(ruleEngineName, bridgeCreator);
    } else registeredRuleEngines.put(ruleEngineName, bridgeCreator);

    log.info("Rule engine '" + ruleEngineName + "' registered with the SWRLTab bridge.");
  }

  public static boolean isRuleEngineRegistered(String ruleEngineName) { return registeredRuleEngines.containsKey(ruleEngineName); }
  public static Set<String> getRegisteredBridgeNames() { return registeredRuleEngines.keySet(); }

  /**
   * Create an instance of a bridge - a random registered engine is returned. If no engine is registered, a
   * NoRegisteredBridgesException is returned.
   */
  public static SWRLRuleEngineBridge createBridge(OWLModel owlModel) throws SWRLRuleEngineBridgeException
  {
    if (!registeredRuleEngines.isEmpty()) return createBridge(registeredRuleEngines.keySet().iterator().next(), owlModel);
    else throw new NoRegisteredBridgesException();
  } 

  /**
   * Create a bridge wrapping a named rule engine. Throws an InvalidBridgeNameException if an engine of this name is not registered.
   */
  public static SWRLRuleEngineBridge createBridge(String bridgeName, OWLModel owlModel) throws SWRLRuleEngineBridgeException
  {
    SWRLRuleEngineBridge bridge = null;
    TargetSWRLRuleEngine ruleEngine = null;

    if (registeredRuleEngines.containsKey(bridgeName)) {

      try {
      	bridge = new DefaultSWRLRuleEngineBridge(owlModel);
        ruleEngine = registeredRuleEngines.get(bridgeName).create(bridge);
        bridge.setTargetRuleEngine(ruleEngine);
      } catch (Throwable e) {
        throw new SWRLRuleEngineBridgeException("Error creating rule engine '" + bridgeName + "': " + e.getMessage());
      } // try

    } else throw new InvalidBridgeNameException(bridgeName);

    return bridge;
  } 

  public static void unregisterBridge(String bridgeName)
  {
    if (registeredRuleEngines.containsKey(bridgeName)) registeredRuleEngines.remove(bridgeName);
  } 

  public interface TargetSWRLRuleEngineCreator
  {
    TargetSWRLRuleEngine create(SWRLRuleEngineBridge bridge) throws SWRLRuleEngineBridgeException;
  } 

} 
