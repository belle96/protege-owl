
package edu.stanford.smi.protegex.owl.swrl.ui.subtab;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.table.AbstractTableModel;

import edu.stanford.smi.protegex.owl.swrl.SWRLRuleEngine;

public class ImportedAxiomsPanel extends JPanel 
{
  private SWRLRuleEngine ruleEngine;
  private ImportedAxiomsModel importedAxiomsModel;
  private JTable table;

  public ImportedAxiomsPanel(SWRLRuleEngine ruleEngine) 
  {
    this.ruleEngine = ruleEngine;

    importedAxiomsModel = new ImportedAxiomsModel();
    table = new JTable(importedAxiomsModel);

    setLayout(new BorderLayout());
    JScrollPane scrollPane = new JScrollPane(table);
    JViewport viewPort = scrollPane.getViewport();
    viewPort.setBackground(table.getBackground());

    add(BorderLayout.CENTER, scrollPane);
  } 

  public void validate() { importedAxiomsModel.fireTableDataChanged(); super.validate(); }

  private class ImportedAxiomsModel extends AbstractTableModel
  {
    public int getRowCount() { return ruleEngine.getNumberOfImportedOWLAxioms(); }
    public int getColumnCount() { return 1; }
    public String getColumnName(int column) { return "Imported Axioms"; }

    public Object getValueAt(int row, int column) 
    { 
      Object result = null;

      if (row < 0 || row >= getRowCount()) result = new String("OUT OF BOUNDS");
      else result =  ruleEngine.getImportedOWLAxioms().toArray()[row];

      return result;
    }

  }
}
