
package edu.stanford.smi.protegex.owl.swrl.sqwrl.ui;

import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.swrl.bridge.BridgeFactory;
import edu.stanford.smi.protegex.owl.swrl.bridge.BridgePluginManager;
import edu.stanford.smi.protegex.owl.swrl.bridge.SWRLRuleEngineBridge;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.SWRLRuleEngineBridgeException;
import edu.stanford.smi.protegex.owl.swrl.bridge.ui.SWRLPluginGUIAdapter;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.ui.icons.QueryIcons;

public class SQWRLQueryTab extends JTabbedPane implements SWRLPluginGUIAdapter
{
  static {
    // Register this plugin with the plugin manager so that the SWRL tab can activate and deactivate it.
    BridgePluginManager.registerPlugin("SQWRLQueryTab", "Activate/deactivate SQWRLQueryTab", QueryIcons.getQueryIcon(), new SQWRLQueryTab());
  } // static

  private SWRLRuleEngineBridge bridge;

  public Container getPluginContainer() { return this; }

  public Container createPluginContainer(OWLModel owlModel)
  {
    try {
      bridge = BridgeFactory.createBridge(owlModel);
    } catch (SWRLRuleEngineBridgeException e) {
      System.err.println(e.toString());
      return makeErrorWindow(e.toString());
    } // try

    removeAll();

    SQWRLQueryControlPanel controlPanel = new SQWRLQueryControlPanel(bridge);
    addTab("SQWRLQueryTab", QueryIcons.getQueryIcon(), controlPanel, "Control Panel");

    return this;
  } // createPluginContainer

  private Container makeErrorWindow(String text) 
  { 
    removeAll();
    JPanel panel = new JPanel(false); 
    JLabel filler = new JLabel(text); 
    filler.setHorizontalAlignment(JLabel.CENTER); 
    panel.setLayout(new GridLayout(1, 1)); 
    panel.add(filler); 
    
    add(panel);

    return this;
  }  // makeErrorWindow
} // SQWRLQueryTab
