package edu.stanford.smi.protegex.owl.swrl.ui.subtab;


import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.table.AbstractTableModel;

import edu.stanford.smi.protegex.owl.swrl.SWRLRuleEngine;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLDataValue;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLPropertyPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLClassPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLDataPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLObjectPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLPropertyAssertionAxiom;

public class InferredAxiomsPanel extends JPanel 
{
  private SWRLRuleEngine ruleEngine;
  private InferredAxiomsModel inferredAxiomsModel;
  private JTable table;

  public InferredAxiomsPanel(SWRLRuleEngine ruleEngine) 
  {
    this.ruleEngine = ruleEngine;

    inferredAxiomsModel = new InferredAxiomsModel();
    table = new JTable(inferredAxiomsModel);

    setLayout(new BorderLayout());

    JScrollPane scrollPane = new JScrollPane(table);
    JViewport viewPort = scrollPane.getViewport();
    viewPort.setBackground(table.getBackground());

    add(BorderLayout.CENTER, scrollPane);
  } 

  public void validate() { inferredAxiomsModel.fireTableDataChanged(); super.validate(); }
  
  private class InferredAxiomsModel extends AbstractTableModel
  {
    public int getRowCount() { return ruleEngine.getNumberOfInferredOWLAxioms(); }
    public int getColumnCount() { return 1; }
    public String getColumnName(int column) { return "Inferred Axioms"; }

    public Object getValueAt(int row, int column) 
    { 
      String result = "";

      if (row < 0 || row >= getRowCount()) result = new String("OUT OF BOUNDS");
      else {
        OWLAxiom axiom = (OWLAxiom)ruleEngine.getInferredOWLAxioms().toArray()[row];

        if (axiom instanceof OWLPropertyAssertionAxiom) {
          OWLPropertyAssertionAxiom propertyAssertionAxiom = (OWLPropertyAssertionAxiom)axiom;
          result = propertyAssertionAxiom.getProperty().getURI() + "(" + propertyAssertionAxiom.getSubject().getURI() + ", ";
          
          if (axiom instanceof OWLObjectPropertyAssertionAxiom) {
            OWLObjectPropertyAssertionAxiom objectAxiom = (OWLObjectPropertyAssertionAxiom)axiom;
            result += ruleEngine.uri2PrefixedName(objectAxiom.getObject().getURI());
          } else if (axiom instanceof OWLDataPropertyAssertionAxiom) {
            OWLDataPropertyAssertionAxiom dataAxiom = (OWLDataPropertyAssertionAxiom)axiom;
            OWLDataValue dataValue = ruleEngine.getOWLDataValueFactory().getOWLDataValue(dataAxiom.getObject());
            if (dataValue.isString() || dataValue.isXSDType()) result += "\"" + dataAxiom.getObject() + "\"";
            else result += dataAxiom.getObject();
          } else if (axiom instanceof OWLClassPropertyAssertionAxiom) {
            OWLClassPropertyAssertionAxiom classAxiom = (OWLClassPropertyAssertionAxiom)axiom;
            result += ruleEngine.uri2PrefixedName(classAxiom.getObject().getURI());
          } else if (axiom instanceof OWLPropertyPropertyAssertionAxiom) {
            OWLPropertyPropertyAssertionAxiom propertyAxiom = (OWLPropertyPropertyAssertionAxiom)axiom;
            result += ruleEngine.uri2PrefixedName(propertyAxiom.getObject().getURI());
          } // if
          result += ")";
        } else result = axiom.toString();
      } // if

      return result;
    } 
  } 
} 
