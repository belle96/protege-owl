package edu.stanford.smi.protegex.owl.swrl.ui.subtab;

import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.swrl.SWRLRuleEngine;
import edu.stanford.smi.protegex.owl.swrl.bridge.SWRLRuleEngineFactory;
import edu.stanford.smi.protegex.owl.swrl.bridge.ui.SWRLPluginGUIAdapter;
import edu.stanford.smi.protegex.owl.swrl.exceptions.SWRLRuleEngineException;
import edu.stanford.smi.protegex.owl.swrl.ui.icons.SWRLIcons;

public abstract class TargetRuleEngineSubTab extends JTabbedPane implements SWRLPluginGUIAdapter
{
  private SWRLRuleEngine ruleEngine;

  public Container getPluginContainer() { return this; }
  
  public Container createPluginContainer(OWLModel owlModel, String pluginName, String ruleEngineName)
  {
    try {
      ruleEngine = createRuleEngine(owlModel, pluginName);
    } catch (SWRLRuleEngineException e) {
      System.err.println(e.toString());
      return makeErrorWindow(e.toString());
    } // try

    removeAll();

    ControlPanel controlPanel = new ControlPanel(ruleEngine, pluginName, ruleEngineName);
    addTab(pluginName, getImpsIcon(), controlPanel, "Control Tab");

    RulesPanel rulesPanel = new RulesPanel(ruleEngine);
    addTab("Rules", getImpsIcon(), rulesPanel, "Rules Tab");

    ImportedClassDeclarationsPanel importedClassesPanel = new ImportedClassDeclarationsPanel(ruleEngine);
    addTab("Classes", getImpsIcon(), importedClassesPanel, "Imported OWL Class Declarations Tab");

    ImportedIndividualDeclarationsPanel importedIndividualsPanel = new ImportedIndividualDeclarationsPanel(ruleEngine);
    addTab("Individuals", getImpsIcon(), importedIndividualsPanel, "Imported OWL Individual Declarations Tab");

    ImportedAxiomsPanel importedRestrictionsPanel = new ImportedAxiomsPanel(ruleEngine);
    addTab("Axioms", getImpsIcon(), importedRestrictionsPanel, "Imported OWL Axioms Tab");

    InferredAxiomsPanel inferredAxiomsPanel = new InferredAxiomsPanel(ruleEngine);
    addTab("Inferred Axioms", getImpsIcon(), inferredAxiomsPanel, "Inferred OWL Axioms Tab");

    return this;
  }

  private SWRLRuleEngine createRuleEngine(OWLModel owlModel, String pluginName) throws SWRLRuleEngineException
  {
    return SWRLRuleEngineFactory.create(pluginName, owlModel);
  } 

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
  }

  private Icon getImpsIcon() { return SWRLIcons.getImpsIcon(); }
}
