package edu.stanford.smi.protegex.owl.swrl.ui.subtab;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.table.AbstractTableModel;

import edu.stanford.smi.protegex.owl.swrl.SWRLRuleEngine;

public class RulesPanel extends JPanel 
{
  private SWRLRuleEngine ruleEngine;
  private RulesModel rulesModel;
  private JTable table;

  public RulesPanel(SWRLRuleEngine ruleEngine) 
  {
    this.ruleEngine = ruleEngine;

    rulesModel = new RulesModel();
    table = new JTable(rulesModel);

    setLayout(new BorderLayout());

    JScrollPane scrollPane = new JScrollPane(table);
    JViewport viewPort = scrollPane.getViewport();
    viewPort.setBackground(table.getBackground());

    add(BorderLayout.CENTER, scrollPane);
  }

  public void validate() { rulesModel.fireTableDataChanged(); super.validate(); }

  private class RulesModel extends AbstractTableModel
  {
    public int getRowCount() { return ruleEngine.getNumberOfImportedSWRLRules(); }
    public int getColumnCount() { return 1; }
    public String getColumnName(int column) { return "Imported Rules and Queries"; }

    public Object getValueAt(int row, int column) 
    { 
      Object result = null;

      if (row < 0 || row >= getRowCount()) result = new String("OUT OF BOUNDS");
      else result =  ruleEngine.getImportedSWRLRules().toArray()[row];

      return result;
    } 
  } 
} 
