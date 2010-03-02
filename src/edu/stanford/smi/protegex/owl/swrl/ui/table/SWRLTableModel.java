package edu.stanford.smi.protegex.owl.swrl.ui.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import edu.stanford.smi.protege.event.KnowledgeBaseAdapter;
import edu.stanford.smi.protege.event.KnowledgeBaseEvent;
import edu.stanford.smi.protege.event.KnowledgeBaseListener;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.util.Disposable;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.model.NamespaceUtil;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.event.ClassAdapter;
import edu.stanford.smi.protegex.owl.model.event.ClassListener;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLFactory;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLImp;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLNames;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.owltable.SymbolTableModel;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class SWRLTableModel extends AbstractTableModel implements Disposable, SymbolTableModel 
{
  public final static int COL_ENABLED = 0;
  public final static int COL_NAME = 1;
  public final static int COL_EXPRESSION = 2;

  public final static int COL_COUNT = 3;
  
  private List<SWRLImp> imps = new ArrayList<SWRLImp>();
  private RDFResource rdfResource;
  private OWLModel owlModel;
  private SWRLFactory factory;
  
  public SWRLTableModel(OWLModel owlModel) 
  {
    this.owlModel = owlModel;
    factory = new SWRLFactory(owlModel);
  
    for (Object o : factory.getImps()) if (o instanceof  SWRLImp) imps.add((SWRLImp)o);
    
    sortImps();
    initListeners();
  }

  public SWRLTableModel(RDFResource resource) 
  {
    this.rdfResource = resource;
    owlModel = resource.getOWLModel();
    factory = new SWRLFactory(owlModel);
    addReferencingImps(resource);
    sortImps();
    initListeners();
  }   

  public int getColumnCount() { return COL_COUNT; }
  public Icon getIcon(RDFResource resource) { return ProtegeUI.getIcon(resource);  }
  public SWRLImp getImp(int row) { return imps.get(row); }
  public void setImp(int row, SWRLImp imp) { imps.remove(row); imps.add(row, imp); }
  public RDFProperty getPredicate(int row) { return null; }
  public RDFResource getRDFResource(int row) { return getImp(row); }
  public RDFResource getSubject() { return null; }
  public int getSymbolColumnIndex() { return COL_EXPRESSION; }
  public int getRowCount() { return imps.size(); }
  public int indexOf(SWRLImp imp) { return imps.indexOf(imp); }

  public void dispose() { 
      owlModel.getRDFSNamedClass(SWRLNames.Cls.IMP).removeClassListener(clsListener); 
      ((KnowledgeBase) owlModel).removeKnowledgeBaseListener(kbListener);
  }
   
  public Class<?> getColumnClass(int column) 
  {
    if (column == COL_ENABLED) return Boolean.class;
    else return super.getColumnClass(column);
  } // getColumnClass

  public String getColumnName(int column) 
  {
    if (column == COL_ENABLED)  return "Enabled";
    else if (column == COL_NAME)  return "Name";
    else if (column == COL_EXPRESSION) return "Expression";
    else return null;
  } // getColumnName

  public Object getValueAt(int rowIndex, int columnIndex) 
  {
    if (columnIndex == getSymbolColumnIndex()) return getImp(rowIndex).getBrowserText();
    else if (columnIndex == COL_NAME) return NamespaceUtil.getPrefixedName(owlModel, getImp(rowIndex).getName());
    else if (columnIndex == COL_ENABLED) return new Boolean(getImp(rowIndex).isEnabled());
    else return null;
  } // getValueAt
  
  public boolean isCellEditable(int rowIndex, int columnIndex) 
  {
    if (columnIndex == COL_ENABLED || columnIndex == COL_NAME || columnIndex == COL_EXPRESSION) {
      SWRLImp imp = getImp(rowIndex);
      return imp.isEditable();
    } else return false;
  } // isCellEditable
  
  public void setValueAt(Object aValue, int rowIndex, int columnIndex) 
  {
    SWRLImp imp = getImp(rowIndex);
    if (columnIndex == COL_EXPRESSION) {
      String text = (String) aValue;
      try {
        imp.setExpression(text);
        if (!isSuitable(imp)) {
          ProtegeUI.getModalDialogFactory().showMessageDialog(owlModel, "The replacing rule no longer fits the selection\n" +
                                                              "criteria of this rules list, and will therefore no\n" +
                                                              "longer be visible here.  But no reason to panic: It\n" +
                                                              "should still show up on the SWRL tab.");
        }
      } catch (Exception ex) {
    	  Log.getLogger().warning("Exception caught defining rule " + ex);
      }
    } else if (columnIndex == COL_NAME) {
      String newName = (String) aValue;
      if (owlModel.isValidResourceName(newName, imp)) {
        RDFResource resource = owlModel.getRDFResource(newName);
        if (resource != null) {
          if (!imp.equals(resource)) {
            ProtegeUI.getModalDialogFactory().showErrorMessageDialog(owlModel, "The name " + newName + " is already used in this ontology.");
          }
        } else {
            imp = (SWRLImp)imp.rename(newName);
            setImp(rowIndex, imp);
        }
      }
      else ProtegeUI.getModalDialogFactory().showErrorMessageDialog(owlModel, newName + " is not a valid rule name.");
    } else if (columnIndex == COL_ENABLED) {
      Boolean enabled = (Boolean)aValue;
      if (enabled.booleanValue()) imp.enable(); 
      else imp.disable();
    } // if
  } // setValueAt

  public void setRowOf(SWRLImp imp, int index) 
  {
    int oldIndex = imps.indexOf(imp);
    imps.remove(oldIndex);
    fireTableRowsDeleted(oldIndex, oldIndex);
    imps.add(index, imp);
    fireTableRowsInserted(index, index);
  }

  public void enableAll() 
  { 
	  for (SWRLImp imp :imps) imp.enable();

    fireTableRowsUpdated(0, getRowCount());
  }

  public void disableAll() 
  { 
    for (SWRLImp imp :imps) imp.disable();
 
    fireTableRowsUpdated(0, getRowCount());
  } 
  
  private void addReferencingImps(RDFResource rdfResource) 
  {
    for (SWRLImp imp : factory.getImps()) 
      if (isSuitable(imp)) imps.add(imp);
  } // addReferencingImps

  private int getRowFor(SWRLImp imp) 
  {
    final String impName = imp.getName();
    int i = 0;
    while (i < imps.size() && impName.compareToIgnoreCase(getImp(i).getName()) >= 0) {
      i++;
    }
    return i;
  }
  
  private void initListeners() { 
      owlModel.getRDFSNamedClass(SWRLNames.Cls.IMP).addClassListener(clsListener); 
      ((KnowledgeBase) owlModel).addKnowledgeBaseListener(kbListener);
  }
  
  private boolean isSuitable(SWRLImp imp) {
    if (rdfResource == null) {
      return true;
    }
    else {
      Set<RDFResource> set = imp.getReferencedInstances();
      return set.contains(rdfResource);
    }
  }

  private void perhapsAdd(SWRLImp imp) {
    if (isSuitable(imp)) {
      int row = getRowFor(imp);
      imps.add(row, imp);
      fireTableRowsInserted(row, row);
    }
  }
  
  private void perhapsRemove(SWRLImp imp) {
    int row = imps.indexOf(imp);
    if (row >= 0) {
      imps.remove(row);
      fireTableRowsDeleted(row, row);
    }
  }
  
  private void sortImps() {
    Collections.sort(imps, new Comparator<SWRLImp>() {
        public int compare(SWRLImp a, SWRLImp b) {
          return a.getName().compareToIgnoreCase(b.getName());
        }
      });
  }

  private ClassListener clsListener = new ClassAdapter() 
    {
      public void instanceAdded(RDFSClass cls, RDFResource instance) {
        final SWRLImp newImp = (SWRLImp) instance;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
              perhapsAdd(newImp);
            }
          });
      }
      public void instanceRemoved(RDFSClass cls, RDFResource instance) { perhapsRemove((SWRLImp) instance);  }
    };
    
  private KnowledgeBaseListener kbListener = new KnowledgeBaseAdapter() 
    {
      @Override
      public void frameReplaced(KnowledgeBaseEvent event) {
          if (event.getNewFrame() instanceof SWRLImp) {
              perhapsRemove((SWRLImp) event.getFrame());
              perhapsAdd((SWRLImp) event.getNewFrame());
          }
      };
    };

} 
