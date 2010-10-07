
package edu.stanford.smi.protegex.owl.swrl.bridge.ui;

import java.awt.Container;

import edu.stanford.smi.protegex.owl.model.OWLModel;

/**
 * This interface must be implemented by a plugin to provide a mechanism to create screen real estate on the SWTLTab for the plugin's GUI. A plugin
 * registers itself with the bridge using the registerPlugin method of the BridgePluginManager class. In addition to a plugin display name,
 * a tool tip string, and an icon, this method is expecting an instance of a class that implements this interface. The plugin manager uses
 * this implementation to get the GUI for the plugin, which will be displayed in the SWRLTab when the plugin is activated. <p>
 *
 * The createPluginContainer method is called once when the plugin is registered and is expecting a Java Swing component that is a subclass of a
 * Java AWT Container class. The getPluginContainer may be called repeatedly after registration and should return the Container instance
 * created on initialization.
 *
 * See <a href="http://protege.cim3.net/cgi-bin/wiki.pl?SWRLRuleEngineBridgeFAQ#nid6QJ">here</a> for a discussion on using this inferface.
 */
public interface SWRLPluginGUIAdapter
{
  Container createPluginContainer(OWLModel owlModel, String pluginName, String ruleEngineName);
  Container getPluginContainer();
}
