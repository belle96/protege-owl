
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

/**
 ** Factory to create instances of common bridge entities
 */
public class BridgeFactory
{
  private static transient final Logger log = Log.getLogger(BridgeFactory.class);
  private static HashMap<String, BridgeCreator> registeredBridges;

  static {
    registeredBridges = new HashMap<String, BridgeCreator>();
  } // static

  static {
    Class cls = PluginUtilities.forName("edu.stanford.smi.protegex.owl.swrl.bridge.jess.SWRLJessBridge", true);
    if (cls == null) System.err.println("SWRLJessBridge load failed - could not find class");
   } // static

  public static void registerBridge(String bridgeName, BridgeCreator bridgeCreator)
  {
    if (registeredBridges.containsKey(bridgeName)) {
      registeredBridges.remove(bridgeName);
      registeredBridges.put(bridgeName, bridgeCreator);
    } else registeredBridges.put(bridgeName, bridgeCreator);

    log.info("Rule engine '" + bridgeName + "' registered with the SWRLTab bridge.");
  } // registerBridge

  public static boolean isBridgeRegistered(String bridgeName) { return registeredBridges.containsKey(bridgeName); }
  public static Set<String> getRegisteredBridgeNames() { return registeredBridges.keySet(); }

  /**
   ** Create an instance of a rule engine - a random registered engine is returned. If no engine is registered, a
   ** NoRegisteredBridgesException is returned.
   */
  public static SWRLRuleEngineBridge createBridge(OWLModel owlModel) throws SWRLRuleEngineBridgeException
  {
    if (!registeredBridges.isEmpty()) return createBridge(registeredBridges.keySet().iterator().next(), owlModel);
    else throw new NoRegisteredBridgesException();
  } // createBridge

  /**
   ** Create an instance of a named rule engine. Throws an InvalidBridgeNameException if an engine of this name is not registered.
   */
  public static SWRLRuleEngineBridge createBridge(String bridgeName, OWLModel owlModel) throws SWRLRuleEngineBridgeException
  {
    SWRLRuleEngineBridge bridge = null;

    if (registeredBridges.containsKey(bridgeName)) {

      try {
        bridge = registeredBridges.get(bridgeName).create(owlModel);
      } catch (Throwable e) {
        throw new SWRLRuleEngineBridgeException("Error creating rule engine '" + bridgeName + "': " + e.getMessage());
      } // try

    } else throw new InvalidBridgeNameException(bridgeName);

    return bridge;
  } // createBridge

  public static void unregisterBridge(String bridgeName)
  {
    if (registeredBridges.containsKey(bridgeName)) registeredBridges.remove(bridgeName);
  } // unregisterBridge

  public interface BridgeCreator
  {
    SWRLRuleEngineBridge create(OWLModel owlModel) throws SWRLRuleEngineBridgeException;
  } // BridgeCreator

} // BridgeFactory
