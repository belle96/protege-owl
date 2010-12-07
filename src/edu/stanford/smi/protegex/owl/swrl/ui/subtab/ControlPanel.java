package edu.stanford.smi.protegex.owl.swrl.ui.subtab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import edu.stanford.smi.protegex.owl.swrl.SWRLRuleEngine;
import edu.stanford.smi.protegex.owl.swrl.exceptions.SWRLRuleEngineException;

public class ControlPanel extends JPanel 
{
	private String ruleEngineName;
	
	public ControlPanel(SWRLRuleEngine ruleEngine, String pluginName, String ruleEngineName) 
  {
    JTextArea textArea;
    JButton button;
    
    this.ruleEngineName = ruleEngineName;

    setLayout(new BorderLayout());

    textArea = createTextArea();
    JScrollPane scrollPane = new JScrollPane(textArea);
    scrollPane.setPreferredSize(new Dimension(900, 300));

    add(BorderLayout.CENTER, scrollPane);
    
    JPanel buttonsPanel = new JPanel(new FlowLayout());

    button = createButton("OWL+SWRL->" + ruleEngineName, "Translate SWRL rules and relevant OWL knowledge to rule engine", 
    		new ImportActionListener(ruleEngine, textArea, this));
    buttonsPanel.add(button);

    button = createButton("Run " + ruleEngineName, "Run the rule engine",	new RunActionListener(ruleEngine, textArea, this));
    
    buttonsPanel.add(button);

    button = createButton(ruleEngineName + "->OWL", "Translate asserted rule engine knowledge to OWL knowledge", 
    			new ExportActionListener(ruleEngine, textArea, this));
    buttonsPanel.add(button);

    add(BorderLayout.SOUTH, buttonsPanel);

    textArea.append("Press the \"OWL+SWRL->" + ruleEngineName +"\" button to transfer SWRL rules and relevant OWL knowledge to the rule engine.\n");
    textArea.append("Press the \"Run " + ruleEngineName +"\" button to run the rule engine.\n");
    textArea.append("Press the \"" + ruleEngineName + "->OWL\" button to transfer the inferred rule engine knowledge to OWL knowledge.\n\n");
    textArea.append("IMPORTANT: A significant limitation of the current implementation is that it does not represent all OWL\n");
    textArea.append("axioms when transferring knowledge from an OWL ontology to a rule engine. The exceptions are\n");
    textArea.append("declaration axioms, class and property assertion axioms, and owl:subClassOf, owl:subPropertyOf, \n");
    textArea.append("owl:equivalentClass, owl:equivalentProperty, owl:sameAs, owl:differentFrom, and owl:allDifferent axioms.\n");
    textArea.append("As a result, the rule engine inferencing mechanisms do not know about the remaining OWL axioms.\n");
    textArea.append("To ensure consistency, a reasoner should be run on an OWL knowledge base before SWRL rules and OWL\n");
    textArea.append("knowledge are transferred to a rule engine. Also, if inferred knowledge from rule engine is inserted back into an OWL\n");
    textArea.append("ontology, a reasoner should again be executed to ensure that the new knowledge does not\n");
    textArea.append("conflict with OWL axioms in that knowledge base.\n\n");
    textArea.append("cf. http://protege.cim3.net/cgi-bin/wiki.pl?SWRLRuleEngineBridgeFAQ#nid6QL for more details.\n");
  } 

  private JButton createButton(String text, String toolTipText, ActionListener listener)
  {
    JButton button = new JButton(text);
    button.setToolTipText(toolTipText);
    button.setPreferredSize(new Dimension(160, 30));
    button.addActionListener(listener);

    return button;
  } 

  private JTextArea createTextArea()
  {
    JTextArea textArea = new JTextArea(10, 80);
    textArea.setLineWrap(true);
    textArea.setBackground(Color.WHITE);
    textArea.setEditable(false);
    return textArea;
  } 

  private class ListenerBase
  {
    protected SWRLRuleEngine ruleEngine;
    protected JTextArea textArea;
    protected ControlPanel controlPanel;

    public ListenerBase(SWRLRuleEngine ruleEngine, JTextArea textArea, ControlPanel controlPanel)
    {
      this.ruleEngine = ruleEngine;
      this.textArea = textArea;
      this.controlPanel = controlPanel;
    }
  } 

  private class ImportActionListener extends ListenerBase implements ActionListener
  {
    public ImportActionListener(SWRLRuleEngine ruleEngine, JTextArea textArea, ControlPanel controlPanel) { super(ruleEngine, textArea, controlPanel); }

    public void actionPerformed(ActionEvent event) 
    {
      try {
      	long startTime = System.currentTimeMillis();
        ruleEngine.reset();
        ruleEngine.importSWRLRulesAndOWLKnowledge();

        textArea.setText("");
        textArea.append("SWRL rule and relevant OWL knowledge successfully converted to rule engine knowledge.\n");
        textArea.append("Number of SWRL rules exported to rule engine: " + ruleEngine.getNumberOfImportedSWRLRules() + "\n");
        textArea.append("Number of OWL class declarations exported to rule engine: " + ruleEngine.getNumberOfImportedOWLClasses() + "\n");
        textArea.append("Number of OWL individual declarations exported to rule engine: " + ruleEngine.getNumberOfImportedOWLIndividuals() + "\n");
        textArea.append("Number of other OWL axioms exported to rule engine: " + ruleEngine.getNumberOfImportedOWLAxioms() + "\n");
        textArea.append("The transfer took " + (System.currentTimeMillis() - startTime) + " millisecond(s).\n");
        textArea.append("Press the \"Run " + ruleEngineName + "\" button to run the rule engine.\n");
      } catch (SWRLRuleEngineException e) {
        textArea.append("Exception importing SWRL rules and OWL knowledge: " + e.toString() + "\n");
      } // try

      controlPanel.getParent().validate();
    } 
  } 

  private class RunActionListener extends ListenerBase implements ActionListener
  {
    public RunActionListener(SWRLRuleEngine ruleEngine, JTextArea textArea, ControlPanel controlPanel) { super(ruleEngine, textArea, controlPanel); }

    public void actionPerformed(ActionEvent event) 
    {
      try {
      	long startTime = System.currentTimeMillis();
        ruleEngine.run();

        textArea.setText("");
        textArea.append("Successful execution of rule engine.\n");
        textArea.append("Number of inferred axioms: " + ruleEngine.getNumberOfInferredOWLAxioms() + "\n");
        if (ruleEngine.getNumberOfInjectedOWLIndividuals() != 0) 
          textArea.append("Number of individual declarations injected by built-ins: " + ruleEngine.getNumberOfInjectedOWLIndividuals() + "\n");
        if (ruleEngine.getNumberOfInjectedOWLClasses() != 0) 
          textArea.append("Number of classes declarations injected by built-ins: " + ruleEngine.getNumberOfInjectedOWLClasses() + "\n");
        if (ruleEngine.getNumberOfInjectedOWLAxioms() != 0) 
          textArea.append("Number of axioms injected by built-ins: " + ruleEngine.getNumberOfInjectedOWLAxioms() + "\n");
        textArea.append("The process took " + (System.currentTimeMillis() - startTime) + " millisecond(s).\n");
        textArea.append("Look at the \"Inferred Axioms\" tab to see the inferred axioms.\n");
        textArea.append("Press the \"Jess->OWL\" button to translate the asserted facts to OWL knowledge.\n");
      } catch (SWRLRuleEngineException e) { 
        textArea.append("Exception running rule engine: " + e.getMessage() + "\n");
      } // try

      controlPanel.getParent().validate();
    } 
  }  

  private class ExportActionListener extends ListenerBase implements ActionListener
  {
    public ExportActionListener(SWRLRuleEngine ruleEngine, JTextArea textArea, ControlPanel controlPanel) { super(ruleEngine, textArea, controlPanel); }

    public void actionPerformed(ActionEvent event) 
    {
      try {
      	long startTime = System.currentTimeMillis();
        ruleEngine.writeInferredKnowledge2OWL();

        textArea.setText("");
        textArea.append("Successfully transferred inferred facts to OWL model.\n");
        if (ruleEngine.getNumberOfInjectedOWLIndividuals() != 0) 
          textArea.append("Number of individual declarations injected by built-ins: " + ruleEngine.getNumberOfInjectedOWLIndividuals() + "\n");
        if (ruleEngine.getNumberOfInjectedOWLClasses() != 0) 
          textArea.append("Number of classe declarations injected by built-ins: " + ruleEngine.getNumberOfInjectedOWLClasses() + "\n");
        if (ruleEngine.getNumberOfInjectedOWLAxioms() != 0) 
          textArea.append("Number of axioms injected by built-ins: " + ruleEngine.getNumberOfInjectedOWLAxioms() + "\n");
        textArea.append("Number of axioms inferred: " + ruleEngine.getNumberOfInferredOWLAxioms() + "\n");
        textArea.append("The process took " + (System.currentTimeMillis() - startTime) + " millisecond(s).\n");
      } catch (SWRLRuleEngineException e) {
        textArea.append("Exception exporting knowledge to OWL: " + e.toString() + "\n");
      } // try
      controlPanel.getParent().validate();
    }
  } 
}
