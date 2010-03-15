
// TODO: Jess rule engine and SWRL query tab are loaded explicitly here. We need a discovery mechanism using the manifest.
// TODO: A bit sloppy. GUI code should be refactored to ui subdirectory.

/**
 * This class provides mechanisms for SWRLTab plugins to register themselves and to get screen real estate under the SWRL editor in the
 * SWRL tab. <p>
 *
 * Documentation on using this class can be found <a
 * href="http://protege.stanford.edu/download/prerelease_javadoc_owl/edu/stanford/smi/protegex/owl/swrl/bridge/BridgePluginManager.html">here</a>.
 */
package edu.stanford.smi.protegex.owl.swrl.bridge;

import java.awt.Container;
import java.awt.GridLayout;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import edu.stanford.smi.protege.event.ProjectAdapter;
import edu.stanford.smi.protege.event.ProjectEvent;
import edu.stanford.smi.protege.event.ProjectListener;
import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.swrl.bridge.ui.SWRLPluginGUIAdapter;
import edu.stanford.smi.protegex.owl.swrl.ui.tab.SWRLTab;

public class BridgePluginManager
{
  private static transient final Logger log = Log.getLogger(BridgePluginManager.class);

  private static HashMap<String, PluginRegistration> registeredPlugins = new HashMap<String, PluginRegistration>();
  private static String visiblePluginName = "";
  private static String selectedRuleName = "";
  
  private static ProjectListener projectListener = new ProjectAdapter() 
  {
    @Override
    public void projectClosed(ProjectEvent event) 
    { 
      Project project = (Project)event.getSource();
      project.removeProjectListener(projectListener);
      visiblePluginName = "";
      selectedRuleName = "";
    } 
  };
  
  static {

    try { // TODO:  Hack until we can do a proper class load with the manifest
      Class.forName("jess.Rete");
      Class.forName("edu.stanford.smi.protegex.owl.swrl.bridge.jess.JessSWRLRuleEngine");
      Class.forName("edu.stanford.smi.protegex.owl.swrl.bridge.jess.ui.SWRLJessTab");
    } catch (ClassNotFoundException e) {
      log.info("Jess rule engine load failed: could not find jess.Rete - or an error occured on initialization");
      
    } // try

    try { // TODO:  Hack until we can do a proper class load with the manifest
      Class.forName("jess.Rete");
      Class.forName("edu.stanford.smi.protegex.owl.swrl.sqwrl.ui.SQWRLQueryTab");
    } catch (ClassNotFoundException e) {
      log.info("SQWRLQueryTab load failed: could not find jess.Rete - or an error occured on initialization");
    } // try

  } // static

  public static String getSelectedRuleName() { return selectedRuleName; }
  public static boolean hasSelectedRule() { return !selectedRuleName.equals(""); }
  public static void setSelectedRuleName(String ruleName) { selectedRuleName = ruleName; }

  public static Collection<PluginRegistration> getRegisteredPlugins() { return registeredPlugins.values(); }

  public static void hideVisiblePlugin() { hidePlugin(visiblePluginName, true); }
  public static boolean hidePlugin(String pluginName) { return hidePlugin(pluginName, false); }
  public static boolean isVisible(String pluginName) { return !visiblePluginName.equals("") && pluginName.equals(visiblePluginName); } 

  // Called by each plugin as it is loaded to inform the adapter of its presence
  public static void registerPlugin(String pluginName, String toolTip, Icon icon, SWRLPluginGUIAdapter guiAdapter)
  {
    if (registeredPlugins.containsKey(pluginName)) registeredPlugins.remove(pluginName);
    registeredPlugins.put(pluginName, new PluginRegistration(pluginName, toolTip, icon, guiAdapter));
    log.info("Plugin '" + pluginName + "' registered with the SWRLTab plugin manager.");
  }

  public static void unregisterPlugin(String pluginName)
  {
    if (registeredPlugins.containsKey(pluginName)) {
      if (isVisible(pluginName)) hidePlugin(pluginName, true);
      registeredPlugins.remove(pluginName);
    } // if
  }

  public static void showPlugin(String pluginName, SWRLTab swrlTab, OWLModel owlModel)
  {
    if (!isVisible(pluginName)) {
      if (hidePlugin(visiblePluginName)) { // Hide may fail if user does not confirm it.
        
        if (registeredPlugins.containsKey(pluginName)) {
          PluginRegistration registration = registeredPlugins.get(pluginName);
          Container pluginGUI = registration.getGUIAdapter().createPluginContainer(owlModel);

          registration.setOWLModel(owlModel); // Set the owlModel so that we can deregister ourselves on deactivation.
          
          if (pluginGUI != null) {
            swrlTab.setVisible(false); 
            swrlTab.setLayout(new GridLayout(2, 1));          
            swrlTab.add(pluginGUI);
            swrlTab.setVisible(true);
            visiblePluginName = pluginName;
          } else makeTextPanel(swrlTab, "Unable to activate the " + pluginName + " plugin.");
        } // if
      } // if
    } // if
    owlModel.getProject().addProjectListener(projectListener);
  }

  private static boolean hidePlugin(String pluginName, boolean force)
  {
    if (isVisible(pluginName)) {

      if (!force && (JOptionPane.showConfirmDialog(null, "Do you really want to close the " + pluginName + " plugin?", "Disable " + pluginName,
                                                   JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)) return false;

      if (registeredPlugins.containsKey(pluginName)) {
        PluginRegistration registration = registeredPlugins.get(pluginName);
        Container pluginGUI = registration.getGUIAdapter().getPluginContainer();
        SWRLTab swrlTab = (SWRLTab)pluginGUI.getParent();
        if (swrlTab != null) {
          swrlTab.setVisible(false); 
          swrlTab.remove(pluginGUI);
          swrlTab.reconfigure();
          swrlTab.setVisible(true);
        } // if
        if (registration.hasOWLModel()) registration.getOWLModel().getProject().removeProjectListener(projectListener); 
        visiblePluginName = "";
      } // if
    } // if
    return true;
  }
  
  public static class PluginRegistration
  {
    private String pluginName;
    private String toolTip;
    private SWRLPluginGUIAdapter guiAdapter;
    private Icon icon;
    private OWLModel owlModel;

    public PluginRegistration(String pluginName, String toolTip, Icon icon, SWRLPluginGUIAdapter guiAdapter)
    {
      this.pluginName = pluginName;
      this.toolTip = toolTip;
      this.guiAdapter = guiAdapter;
      this.icon = icon;
      owlModel = null; // An OWL model is supplied when a GUI associated with the plugin is activated.
    }

    public void setOWLModel(OWLModel owlModel) { this.owlModel = owlModel; }

    public String getPluginName() { return pluginName; }
    public String getToolTip() { return toolTip; }
    public SWRLPluginGUIAdapter getGUIAdapter() { return guiAdapter; }
    public Icon getIcon() { return icon; }
    public OWLModel getOWLModel() { return owlModel; } 
    public boolean hasOWLModel() { return owlModel != null; }
      
  } 

  private static void makeTextPanel(SWRLTab swrlTab, String text) 
  { 
    JPanel panel = new JPanel(false); 
    JLabel filler = new JLabel(text); 
    filler.setHorizontalAlignment(JLabel.CENTER); 
    panel.setLayout(new GridLayout(1, 1)); 
    panel.add(filler); 

    swrlTab.add(panel);
  }   
}
