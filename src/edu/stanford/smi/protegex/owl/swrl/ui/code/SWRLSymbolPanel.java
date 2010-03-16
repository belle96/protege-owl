package edu.stanford.smi.protegex.owl.swrl.ui.code;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;

import edu.stanford.smi.protege.ui.FrameComparator;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSDatatype;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLBuiltin;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLFactory;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLNames;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable;
import edu.stanford.smi.protegex.owl.swrl.parser.SWRLParseException;
import edu.stanford.smi.protegex.owl.swrl.parser.SWRLParser;
import edu.stanford.smi.protegex.owl.swrl.ui.icons.SWRLIcons;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.code.SymbolPanel;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.resourceselection.ResourceSelectionAction;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class SWRLSymbolPanel extends SymbolPanel 
{
  private Action andAction;
  private ResourceSelectionAction builtinAction;
  private Action createVariableAction;
  private Action datatypeAction;
  private Action differentFromAction;
  private Action impAction;
  private Action insertVariableAction;
  private JButton insertVariableButton, createVariableButton, insertXMLSchemaButton, insertAndButton, insertImpButton,
    insertOpenParenthesisButton, insertCloseParenthesisButton, insertOpenBracketsButton, insertCloseBracketsButton,
    insertSameAsButton, insertDifferentFromButton, insertBuiltInButton;
  private Action openBracketsAction;
  private Action closeBracketsAction;
  private Action openParenthesisAction;
  private Action closeParenthesisAction;
  private Action sameAsAction;
  
  public SWRLSymbolPanel(OWLModel owlModel, boolean closeable, boolean draggable) 
  {
    super(owlModel, closeable, draggable, true);
  }

  protected String getDisplayErrorMessage(Throwable ex) {
    if (ex instanceof SWRLParseException) {
      return ((SWRLParseException) ex).getMessage();
    }
    else {
      return "" + ex;
    }
  }

  public Dimension getPreferredSize() {
    Dimension pref = super.getPreferredSize();
    return new Dimension(500, pref.height);
  }
  
  @Override
  public void setEnabled(boolean isEnabled)
  {
  	super.setEnabled(isEnabled);
  	insertVariableButton.setEnabled(isEnabled); 
  	createVariableButton.setEnabled(isEnabled); 
  	insertXMLSchemaButton.setEnabled(isEnabled);
  	insertAndButton.setEnabled(isEnabled);
  	insertImpButton.setEnabled(isEnabled);
    insertOpenParenthesisButton.setEnabled(isEnabled);
    insertCloseParenthesisButton.setEnabled(isEnabled);
    insertOpenBracketsButton.setEnabled(isEnabled);
    insertCloseBracketsButton.setEnabled(isEnabled);
    insertSameAsButton.setEnabled(isEnabled);
    insertDifferentFromButton.setEnabled(isEnabled);
    insertBuiltInButton.setEnabled(false);
  }

  private Collection<String> getUsedVariableNames() {
    String text = getSymbolEditor().getText();
    Collection<String> result = new HashSet<String>();
    for (int i = 0; i < text.length(); i++) {
      if (text.charAt(i) == '?') {
        String varName = getVariableName(text, i);
        if (varName.length() > 0) {
          result.add(varName);
        }
      }
    }
    return result;
  }
  
  private String getVariableName(String text, int beginIndex) {
    int index = beginIndex + 1;
    while (index < text.length() &&
           Character.isJavaIdentifierPart(text.charAt(index))) {
      index++;
    }
    return text.substring(beginIndex + 1, index);
  }

  protected void initMiddleBar(JToolBar toolBar) 
  {
    datatypeAction = new InsertXMLSchemaDatatypeAction();
    insertXMLSchemaButton = addButton(toolBar, datatypeAction);
    
    andAction = new AbstractAction("Insert conjunction (" + SWRLParser.AND_CHAR + ")",
                                   SWRLIcons.getInsertAndIcon()) {
        public void actionPerformed(ActionEvent e) {
          getSymbolEditor().insertText("  " + SWRLParser.AND_CHAR + "  ");
        }
      };
    insertAndButton = addButton(toolBar, andAction);
    
    impAction = new AbstractAction("Insert implication (" + SWRLParser.IMP_CHAR + ")",
                                   SWRLIcons.getInsertImpIcon()) {
        public void actionPerformed(ActionEvent e) {
          getSymbolEditor().insertText("  " + SWRLParser.IMP_CHAR + "  ");
        }
      };
    insertImpButton = addButton(toolBar, impAction);
    
    toolBar.addSeparator();
    
    openParenthesisAction = new AbstractAction("Insert open parenthesis: (",
                                               SWRLIcons.getOpenParenthesis()) {
        public void actionPerformed(ActionEvent e) {
          getSymbolEditor().insertText("(", 1);
        }
      };
    insertOpenParenthesisButton = addButton(toolBar, openParenthesisAction);
    
        closeParenthesisAction = new AbstractAction("Insert close parenthesis: )",
                                                    SWRLIcons.getCloseParenthesis()) {
            public void actionPerformed(ActionEvent e) {
              getSymbolEditor().insertText(")", 1);
            }
          };
        insertCloseParenthesisButton = addButton(toolBar, closeParenthesisAction);
        
        toolBar.addSeparator();

        openBracketsAction = new AbstractAction("Insert open brackets: [",
                                                SWRLIcons.getOpenBrackets()) {
            public void actionPerformed(ActionEvent e) {
              getSymbolEditor().insertText("[", 1);
            }
          };
        insertOpenBracketsButton = addButton(toolBar, openBracketsAction);
        
        closeBracketsAction = new AbstractAction("Insert close brackets: ]",
                                                 SWRLIcons.getCloseBrackets()) {
            public void actionPerformed(ActionEvent e) {
                getSymbolEditor().insertText("]", 1);
            }
          };
          insertCloseBracketsButton = addButton(toolBar, closeBracketsAction);
  }
  
  
  protected void initTopBar(JToolBar toolBar) 
  {
    classAction.activateComboBox(addButton(toolBar, classAction));
    propertyAction.activateComboBox(addButton(toolBar, propertyAction));
    individiualAction.activateComboBox(addButton(toolBar, individiualAction));
    toolBar.addSeparator();
    createVariableAction = new CreateVariableAction();
    createVariableButton = addButton(toolBar, createVariableAction);
    insertVariableAction = new InsertVariableAction();
    insertVariableButton = addButton(toolBar, insertVariableAction);
    
    toolBar.addSeparator();
    
    builtinAction = new InsertBuiltinAction();
    insertBuiltInButton = addButton(toolBar, builtinAction);
    builtinAction.activateComboBox(insertBuiltInButton);
    
    differentFromAction = new AbstractAction("Insert differentFrom",
                                             SWRLIcons.getDifferentFromIcon()) {
        public void actionPerformed(ActionEvent e) {
          getSymbolEditor().insertText("differentFrom(", 14);
        }
      };
    insertDifferentFromButton = addButton(toolBar, differentFromAction);
    sameAsAction = new AbstractAction("Insert sameAs",
                                      SWRLIcons.getSameAsIcon()) {
        public void actionPerformed(ActionEvent e) {
          getSymbolEditor().insertText("sameAs(", 7);
        }
      };
    insertSameAsButton = addButton(toolBar, sameAsAction);
    
    toolBar.addSeparator();
  }

  private void insertVariable(ActionEvent e) {
    Set<String> vars = new HashSet<String>(getUsedVariableNames());
    final Collection instances = getOWLModel().getRDFSNamedClass(SWRLNames.Cls.VARIABLE).getInstances(true);
    for (Iterator it = instances.iterator(); it.hasNext();) {
      RDFResource resource = (RDFResource) it.next();
      vars.add(resource.getName());
    }
    String[] symbols = (String[]) vars.toArray(new String[0]);
    Arrays.sort(symbols);
    
    JPopupMenu menu = new JPopupMenu();
    for (int i = 0; i < symbols.length; i++) {
            String symbol = symbols[i];
            final JMenuItem item = new JMenuItem(symbol, SWRLIcons.getVariableIcon());
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                  String text = (String) item.getText();
                  getSymbolEditor().insertText("?" + text);
                }
              });
            menu.add(item);
    }
    if (menu.getComponentCount() > 0) {
      menu.show(insertVariableButton, 0, insertVariableButton.getHeight());
    }
  }

  private class InsertBuiltinAction extends ResourceSelectionAction {
    
    InsertBuiltinAction() { super("Insert builtin...", SWRLIcons.getBuiltinIcon()); }

    public void resourceSelected(RDFResource resource) 
    {
      String str = resource.getBrowserText() + "(";
      getSymbolEditor().insertText(str);
    }

    public Collection getSelectableResources() 
    {
      java.util.List<SWRLBuiltin> result = new ArrayList<SWRLBuiltin>(new SWRLFactory(getOWLModel()).getBuiltins());
      Collections.sort(result, new FrameComparator());
      return result;
    }

    public RDFResource pickResource() {
      Collection frames = getSelectableResources();
      return ProtegeUI.getSelectionDialogFactory().selectResourceFromCollection(SWRLSymbolPanel.this, getOWLModel(), frames,
                                                                                "Select the builtin to insert");
    }
  }
  
  private class CreateVariableAction extends AbstractAction 
  {
    CreateVariableAction() {
      super("Insert new variable",
            OWLIcons.getCreateIcon(SWRLIcons.VARIABLE, SWRLIcons.class));
    }
    
    
    public void actionPerformed(ActionEvent e) {
      String variableName = getNextVariableName();
      if (variableName != null) {
        getSymbolEditor().insertText("?" + variableName);
      }
    }
    
    private String getNextVariableName() {
      Collection<String> usedNames = getUsedVariableNames();
      Collection<String> existingVariableNames = new ArrayList<String>();
      OWLModel owlModel = getOWLModel();
      SWRLFactory factory = new SWRLFactory(owlModel);
      final Collection<SWRLVariable> variables = factory.getVariables();
      for (SWRLVariable variable : variables)
       existingVariableNames.add(variable.getName());
      
      String chars = "xyzabcdefghijklmnopqrstuvwXYZABCDEFGHIJKLMNOPQRSTUVW";
      for (int i = 0; i < chars.length(); i++) {
        char c = chars.charAt(i);
        String name = "" + c;
                if (!usedNames.contains(name)) {
                  if (owlModel.getRDFResource(name) == null || owlModel.getRDFResource(name) instanceof SWRLVariable) {
                    return name;
                  }
                }
      }
      return null;
    }
  }
  
  private class InsertVariableAction extends AbstractAction 
  {  
    InsertVariableAction() { super("Insert existing variable...", OWLIcons.getAddIcon(SWRLIcons.VARIABLE, SWRLIcons.class)); }

    public void actionPerformed(ActionEvent e) { insertVariable(e); }
  }

  private class InsertXMLSchemaDatatypeAction extends AbstractAction 
  {
    InsertXMLSchemaDatatypeAction() { super("Insert XML Schema datatype", OWLIcons.getImageIcon(OWLIcons.XSD_DATATYPE)); }

    public void actionPerformed(ActionEvent e) 
    {
      OWLModel owlModel = getOWLModel();
      Collection<RDFSDatatype> datatypes = owlModel.getRDFSDatatypes();
      RDFResource datatype = ProtegeUI.getSelectionDialogFactory().selectResourceFromCollection(SWRLSymbolPanel.this, owlModel,
                                                                                                datatypes, 
                                                                                                "Select the XML Schema datatype to insert");
      if (datatype != null) { getSymbolEditor().insertText(datatype.getLocalName()); }
    }
  }
}
