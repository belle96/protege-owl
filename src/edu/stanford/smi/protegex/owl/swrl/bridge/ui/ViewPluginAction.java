
package edu.stanford.smi.protegex.owl.swrl.bridge.ui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.swrl.bridge.BridgePluginManager;
import edu.stanford.smi.protegex.owl.swrl.ui.tab.SWRLTab;

public class ViewPluginAction extends AbstractAction 
{
  private static final long serialVersionUID = 2115603073256549503L;
  private String pluginName;
  private OWLModel owlModel;
  private SWRLTab swrlTab;

  public ViewPluginAction(String pluginName, String tip, Icon icon, SWRLTab swrlTab, OWLModel owlModel) 
  {
    super(tip, icon);
    
    this.pluginName = pluginName;
    this.owlModel = owlModel;
    this.swrlTab = swrlTab;
  } // ViewPluginAction
  
  public void actionPerformed(ActionEvent e) 
  {
    if (BridgePluginManager.isVisible(pluginName)) BridgePluginManager.hidePlugin(pluginName);
    else BridgePluginManager.showPlugin(pluginName, swrlTab, owlModel);
  } // actionPerformed
} // ViewPluginAction
