
package edu.stanford.smi.protegex.owl.swrl.sqwrl.ui;

import edu.stanford.smi.protegex.owl.swrl.sqwrl.*;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.*;

import edu.stanford.smi.protegex.owl.swrl.model.*;
import edu.stanford.smi.protegex.owl.swrl.bridge.*;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.*;
import edu.stanford.smi.protegex.owl.swrl.ui.icons.SWRLIcons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class SQWRLQueryControlPanel extends JPanel 
{
  private SQWRLQueryEngine queryEngine;
  private HashMap<String, SQWRLQueryResultPanel> resultPanels;
  private JTextArea textArea;
  private static int MaximumOpenResultPanels = 8;

  public SQWRLQueryControlPanel(SQWRLQueryEngine queryEngine) 
  {
    JPanel panel;
    JButton button;

    this.queryEngine = queryEngine;

    resultPanels = new HashMap<String, SQWRLQueryResultPanel>();

    setLayout(new BorderLayout());

    textArea = createTextArea();
    JScrollPane scrollPane = new JScrollPane(textArea);
    scrollPane.setPreferredSize(new Dimension(900, 300));

    add(BorderLayout.CENTER, scrollPane);
    
    panel = new JPanel(new FlowLayout());

    button = createButton("Run", "Run all SWRL rules and SQWRL queries", new RunActionListener(textArea, this));
    panel.add(button);

    add(BorderLayout.SOUTH, panel);

    textArea.append("\nSee http://protege.cim3.net/cgi-bin/wiki.pl?SQWRLQueryTab for documentation.\n\n");
    textArea.append("Executing queries in this tab does not modify the ontology.\n\n");
    textArea.append("Select a SQWRL query from the list above and press the 'Run' button.\n");
    textArea.append("If the selected query generates a result, the result will appear in a new sub tab.\n\n");
  } // SQWRLQueryControlPanel

  public void appendText(String text)
  {
    textArea.append(text);
  } // appendText

  public void removeResultPanel(String queryName)
  {
    if (resultPanels.containsKey(queryName)) {
      SQWRLQueryResultPanel resultPanel = resultPanels.get(queryName);
      resultPanels.remove(queryName);
      ((JTabbedPane)getParent()).remove(resultPanel);
      ((JTabbedPane)getParent()).setSelectedIndex(0);
    } // if
  } // if

  public void removeAllPanels()
  {
    for (SQWRLQueryResultPanel resultPanel : resultPanels.values()) ((JTabbedPane)getParent()).remove(resultPanel);
    resultPanels = new HashMap<String, SQWRLQueryResultPanel>();
  } // if

  private JButton createButton(String text, String toolTipText, ActionListener listener)
  {
    JButton button = new JButton(text);

    button.setToolTipText(toolTipText);
    button.setPreferredSize(new Dimension(160, 30));
    button.addActionListener(listener);

    return button;
  } // createButton

  private JLabel createLabel(String text)
  {
    JLabel label = new JLabel(text);

    label.setPreferredSize(new Dimension(160, 30));

    return label;
  } // createLabel

  private JTextArea createTextArea()
  {
    JTextArea textArea = new JTextArea(10, 80);
    textArea.setLineWrap(true);
    textArea.setBackground(Color.WHITE);
    textArea.setEditable(false);
    return textArea;
  } // createTextArea

  private class ListenerBase
  {
    protected JTextArea textArea;
    protected SQWRLQueryControlPanel controlPanel;

    public ListenerBase(JTextArea textArea, SQWRLQueryControlPanel controlPanel)
    {
      this.textArea = textArea;
      this.controlPanel = controlPanel;
    } // ListenerBase
  } // ListenerBase

  private class RunActionListener extends ListenerBase implements ActionListener
  {
    public RunActionListener(JTextArea textArea, SQWRLQueryControlPanel controlPanel) 
    { super(textArea, controlPanel); }
    
    public void actionPerformed(ActionEvent event) 
    {
      SQWRLQueryResultPanel resultPanel;
      String queryName = "";
      SQWRLResult result = null;
      
      if (resultPanels.size() == MaximumOpenResultPanels) {
        textArea.append("A maximum of " + MaximumOpenResultPanels + " result tabs may be open at once. ");
        textArea.append("Please close an existing tab to display results for the selected rule.\n");
      } else {
	try {
          queryEngine.runSQWRLQueries();
        
          queryName = BridgePluginManager.getSelectedRuleName();
          
          if (queryName == null || queryName.equals("")) textArea.append("No SQWRL query selected.\n");
          else {
            result = queryEngine.getSQWRLResult(queryName);
            if (result == null || result.getNumberOfRows() == 0) {
              textArea.append("Query '" + queryName + "' did not generate any result.\n");
              if  (resultPanels.containsKey(queryName)) {
                resultPanel = resultPanels.get(queryName);
                resultPanels.remove(resultPanel);
                ((JTabbedPane)getParent()).remove(resultPanel);
              } // if
            } else { // A result was returned
              textArea.append("See the '" + queryName + "' tab to review results of the SQWRL query.\n");
              
              if  (resultPanels.containsKey(queryName)) resultPanel = resultPanels.get(queryName); // Existing tab found
              else { // Create new tab
                resultPanel = new SQWRLQueryResultPanel(queryEngine, queryName, result, controlPanel);
                resultPanels.put(queryName, resultPanel);
                ((JTabbedPane)getParent()).addTab(queryName, SWRLIcons.getImpsIcon(), resultPanel, "Result Panel for query '" + queryName + "'");
              } // if
              resultPanel.validate();
              controlPanel.getParent().validate();
            } // if
          } // if
        } catch (InvalidQueryNameException e) {
          textArea.append("Invalid query name '" + queryName + "'.\n");
	} catch (SQWRLException e) {
          if (queryName.equals("")) textArea.append("Exception running SQWRL queries: " + e.getMessage() + "\n");
          else textArea.append("Exception when running SQWRL query '" + queryName + "': " + e.getMessage() + "\n");
	} // try
      } // if
    } // ActionPerformed
  } // RunActionListener

} // SQWRLQueryControlPanel




