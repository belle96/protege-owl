package edu.stanford.smi.protegex.owl.swrl.ui.subtab;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.table.AbstractTableModel;

import edu.stanford.smi.protegex.owl.swrl.SWRLRuleEngine;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLClass;

public class ImportedClassDeclarationsPanel extends JPanel 
{
  private SWRLRuleEngine ruleEngine;
  private ImportedClasseDeclarationsModel importedClassesModel;
  private JTable table;

  public ImportedClassDeclarationsPanel(SWRLRuleEngine ruleEngine) 
  {
    this.ruleEngine = ruleEngine;

    importedClassesModel = new ImportedClasseDeclarationsModel();
    table = new JTable(importedClassesModel);

    setLayout(new BorderLayout());

    JScrollPane scrollPane = new JScrollPane(table);
    JViewport viewPort = scrollPane.getViewport();
    viewPort.setBackground(table.getBackground());

    add(BorderLayout.CENTER, scrollPane);
  } 

  public void validate() { importedClassesModel.fireTableDataChanged(); super.validate(); }

  private class ImportedClasseDeclarationsModel extends AbstractTableModel
  {
    public int getRowCount() { return ruleEngine.getNumberOfImportedOWLClasses(); }
    public int getColumnCount() { return 1; }
    public String getColumnName(int column) { return "Imported OWL Classes"; }

    public Object getValueAt(int row, int column) 
    { 
      Object result = null;

      if (row < 0 || row >= getRowCount()) result = new String("OUT OF BOUNDS");
      else {
    	  String classURI =  ruleEngine.getImportedOWLClasses().toArray(new OWLClass[0])[row].getURI();
    	  result =  ruleEngine.uri2PrefixedName(classURI);
      } // if

      return result;
    } 
  }
}
