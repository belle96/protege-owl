
package edu.stanford.smi.protegex.owl.swrl.sqwrl.ui;

import edu.stanford.smi.protegex.owl.swrl.bridge.*;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.*;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.*;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.*;
import edu.stanford.smi.protegex.owl.swrl.bridge.ui.*;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.swrl.model.*;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.ui.icons.QueryIcons;

import javax.swing.*;
import java.awt.*;

public class SQWRLQueryTab extends JTabbedPane implements SWRLPluginGUIAdapter
{
  static {
    // Register this plugin with the plugin manager so that the SWRL tab can activate and deactivate it.
    BridgePluginManager.registerPlugin("SQWRLQueryTab", "Activate/deactivate SQWRLQueryTab", QueryIcons.getQueryIcon(), new SQWRLQueryTab());
  } // static

  private SWRLRuleEngineBridge bridge;

  public Container getPluginGUI() { return this; }

  public Container createPluginGUI(OWLModel owlModel)
  {
    // TODO: Eventually pop up window to select an engine from a list.

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
  } // createPluginGUI

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
