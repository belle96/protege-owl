package edu.stanford.smi.protegex.owl.swrl.ui.subtab;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.table.AbstractTableModel;

import edu.stanford.smi.protegex.owl.swrl.SWRLRuleEngine;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLClass;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLNamedIndividual;

public class ImportedIndividualDeclarationsPanel extends JPanel 
{
  private SWRLRuleEngine ruleEngine;
  private ImportedIndividualDeclarationsModel importedIndividualsModel;
  private JTable table;

  public ImportedIndividualDeclarationsPanel(SWRLRuleEngine ruleEngine) 
  {
    this.ruleEngine = ruleEngine;

    importedIndividualsModel = new ImportedIndividualDeclarationsModel();
    table = new JTable(importedIndividualsModel);

    setLayout(new BorderLayout());
    JScrollPane scrollPane = new JScrollPane(table);
    JViewport viewPort = scrollPane.getViewport();
    viewPort.setBackground(table.getBackground());

    add(BorderLayout.CENTER, scrollPane);
  }

  public void validate() { importedIndividualsModel.fireTableDataChanged(); super.validate(); }

  private class ImportedIndividualDeclarationsModel extends AbstractTableModel
  {
    public int getRowCount() { return ruleEngine.getNumberOfImportedOWLIndividuals(); }
    public int getColumnCount() { return 1; }
    public String getColumnName(int column) { return "Imported Individuals"; }

    public Object getValueAt(int row, int column) 
    { 
      String result = "";

      if (row < 0 || row >= getRowCount()) result = new String("OUT OF BOUNDS");
      else {
        OWLNamedIndividual owlIndividual = (OWLNamedIndividual)ruleEngine.getImportedOWLIndividuals().toArray()[row];
        for (OWLClass owlClass : owlIndividual.getTypes()) 
          result += ruleEngine.uri2PrefixedName(owlClass.getURI()) + "(" + ruleEngine.uri2PrefixedName(owlIndividual.getURI()) + ") ";
      } // if

      return result;
    } 
  }
}
