
package edu.stanford.smi.protegex.owl.swrl.sqwrl.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.table.AbstractTableModel;

import edu.stanford.smi.protegex.owl.swrl.sqwrl.ClassValue;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.DataValue;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.IndividualValue;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.PropertyValue;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.SQWRLQueryEngine;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.SQWRLResult;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.SQWRLResultValue;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.InvalidQueryNameException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.SQWRLException;

public class SQWRLQueryResultPanel extends JPanel 
{
  private String queryName;
  private JTable table;
  private SQWRLQueryEngine queryEngine;
  private SQWRLResult result = null;
  private SQWRLQueryControlPanel controlPanel;
  private SQWRLQueryResultModel swrlQueryResultModel;
  private static File currentDirectory = null;

  public SQWRLQueryResultPanel(SQWRLQueryEngine queryEngine, String queryName, SQWRLResult result, SQWRLQueryControlPanel controlPanel) 
  {
    this.queryEngine = queryEngine;
    this.queryName = queryName;
    this.result = result;
    this.controlPanel = controlPanel;
    
    swrlQueryResultModel = new SQWRLQueryResultModel();
    table = new JTable(swrlQueryResultModel);
    
    setLayout(new BorderLayout());
    
    JPanel buttonsPanel = new JPanel(new FlowLayout());
    
    JButton saveResultButton = createButton("Save as CSV...", "Save the result as a CSV file...", new SaveResultActionListener());
    buttonsPanel.add(saveResultButton);

    JButton runQueriesButton = createButton("Rerun", "Rerun this SQWRL query", new RunQueriesActionListener());
    buttonsPanel.add(runQueriesButton);
    
    JButton closeTabButton = createButton("Close", "Close the tab for this query", new CloseTabActionListener());
    buttonsPanel.add(closeTabButton);
        
    JScrollPane scrollPane = new JScrollPane(table);
    JViewport viewPort = scrollPane.getViewport();
    viewPort.setBackground(table.getBackground());
    
    add(BorderLayout.CENTER, scrollPane);
    add(BorderLayout.SOUTH, buttonsPanel);
  }
  
  public void validate() { swrlQueryResultModel.fireTableStructureChanged(); super.validate(); }
  
  private class RunQueriesActionListener implements ActionListener
  {
    public void actionPerformed(ActionEvent event) 
    {
      result = null;

      try {
        result = queryEngine.runSQWRLQuery(queryName);
        
        if (result == null || result.getNumberOfRows() == 0) {
          controlPanel.appendText("No result returned for SQWRL query '" + queryName + "' - closing tab.\n");
          controlPanel.removeResultPanel(queryName);
        } else validate();
      } catch (InvalidQueryNameException e) {
        controlPanel.appendText("Invalid query name '" + queryName + "'.\n");
      } catch (SQWRLException e) {
        controlPanel.appendText("Exception running SQWRL query '" + queryName + "': " + e.getMessage() + "\n");
      } // try
     
      /*
      if (result == null) {
        controlPanel.removeAllPanels();
        controlPanel.appendText("Closing all result tabs.\n");
      } // if
      */
    } // try
  }
  
  private class CloseTabActionListener implements ActionListener
  {
    public void actionPerformed(ActionEvent event) 
    {
      controlPanel.removeResultPanel(queryName);
      controlPanel.appendText("'" + queryName + "' tab closed.\n");
    }
  }
  
  private class SaveResultActionListener implements ActionListener
  {
    private JFileChooser chooser;

    public SaveResultActionListener()
    {
      chooser = new JFileChooser();
      chooser.setCurrentDirectory(currentDirectory);
    }

    public void actionPerformed(ActionEvent event) 
    {
      saveResults();
    }

    private void saveResults() 
    {
      int returnValue = chooser.showOpenDialog(controlPanel);
      File selectedFile = null;
      FileWriter writer = null;
      int numberOfColumns;
      
      try {
        if (returnValue == JFileChooser.APPROVE_OPTION) {
          selectedFile = chooser.getSelectedFile();
          currentDirectory = chooser.getCurrentDirectory();
          writer = new FileWriter(selectedFile);
          result = queryEngine.getSQWRLResult(queryName);

          if (result != null) {
            numberOfColumns = result.getNumberOfColumns();
            
            for (int i = 0; i < numberOfColumns; i++) {
              if (i != 0) writer.write(", ");
              writer.write(result.getColumnName(i));
            } // for
            writer.write("\n");
            
            while (result.hasNext()) {
              for (int i = 0; i < numberOfColumns; i++) {
                SQWRLResultValue value = result.getValue(i);
                if (i != 0) writer.write(", ");
                if (value instanceof DataValue && ((DataValue)value).isString()) writer.write("\"" + value + "\"");
                else writer.write("" + value);
              } // for
              writer.write("\n");
              result.next();
            } // while
            result.reset();
            writer.close();
            controlPanel.appendText("Sucessfully saved results of query " + queryName + " to CSV file " + selectedFile.getPath() + ".\n");
          } // if
        } // if
      } catch (Throwable e) {
        JOptionPane.showMessageDialog(null, "Error saving file " + selectedFile.getPath() + ": " + e.getMessage(), "Error saving file",
                                      JOptionPane.ERROR_MESSAGE);
        // TODO: findbugs - stream not closed on all paths
      } // try
    }
  } 
  
  private JButton createButton(String text, String toolTipText, ActionListener listener)
  {
    JButton button = new JButton(text);

    button.setToolTipText(toolTipText);
    button.setPreferredSize(new Dimension(160, 30));
    button.addActionListener(listener);

    return button;
  }

  private class SQWRLQueryResultModel extends AbstractTableModel
  {
    public int getRowCount() 
    { 
      int count = 0;
      
      try {
        count = (result == null) ? 0 : result.getNumberOfRows(); 
      } catch (SQWRLException e) {}

      return count;
    } 
    
    public int getColumnCount() 
    {
      int count = 0;
      
      try {
        count = (result == null) ? 0 : result.getNumberOfColumns(); 
      } catch (SQWRLException e) {}

      return count;
    }
    
    public String getColumnName(int columnIndex) 
    {
      String columnName = "";

      try {
        columnName = (result == null) ? "" : result.getColumnName(columnIndex); 
      } catch (SQWRLException e) {}
      
      return columnName;
    }

    public Object getValueAt(int row, int column) 
    { 
      String representation = "";
      
      try { 
        SQWRLResultValue value = (result == null) ? null : result.getValue(column, row);
        if (value instanceof IndividualValue) {
          IndividualValue objectValue = (IndividualValue)value;
          representation += queryEngine.uri2PrefixedName(objectValue.getURI());
        } else if (value instanceof DataValue) {
          DataValue datatypeValue = (DataValue)value;
          representation += datatypeValue.toString();
        } else if (value instanceof ClassValue) {
          ClassValue classValue = (ClassValue)value;
          representation += queryEngine.uri2PrefixedName(classValue.getURI());
        } else if (value instanceof PropertyValue) {
          PropertyValue propertyValue = (PropertyValue)value;
          representation += queryEngine.uri2PrefixedName(propertyValue.getURI());
        } // if
      } catch (SQWRLException e) {}

      return representation;
    }
  }
}
