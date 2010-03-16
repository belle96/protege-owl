package edu.stanford.smi.protegex.owl.swrl.ui.code;

import javax.swing.UIManager;

import edu.stanford.smi.protege.model.Frame;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.swrl.parser.SWRLIncompleteRuleException;
import edu.stanford.smi.protegex.owl.swrl.parser.SWRLParser;
import edu.stanford.smi.protegex.owl.ui.code.SymbolErrorDisplay;
import edu.stanford.smi.protegex.owl.ui.code.SymbolTextArea;
import edu.stanford.smi.protegex.owl.ui.code.SymbolTextField;

/**
 * A SymbolTextArea with special support for editing SWRL expressions.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class SWRLTextArea extends SymbolTextArea 
{
  private SWRLParser parser;
  private SWRLSymbolPanel symbolPanel;

  public SWRLTextArea(OWLModel owlModel, SWRLSymbolPanel errorDisplay) 
  {
    super(owlModel, errorDisplay, new SWRLResourceNameMatcher(), new SWRLSyntaxConverter(owlModel));
    symbolPanel = errorDisplay;
    parser = new SWRLParser(owlModel);
    setFont(UIManager.getFont("TextArea.font"));
    SWRLTextField.initKeymap(this);
  }
  
  @Override
  public void setEnabled(boolean isEnabled)
  {
  	super.setEnabled(isEnabled);
  	symbolPanel.setEnabled(isEnabled);
  }
  
  protected void checkUniCodeExpression(String uniCodeText) throws Throwable 
  {
    try {
      parser.parse(uniCodeText);
    }  catch (SWRLIncompleteRuleException e) {
      // Ignore incomplete rules on input checking. (Unlike SymbolTextField, SymbolTextArea only calls checkUniCodeExpression when it
      // is checking an expression for errors, not when it is determining if an expression can be saved.
    } // try
  }

  public void reformatText() 
  {
    String text = getText();
    text = reformatText(text);
    setText(text);
  } // reformatText
  
  public static String reformatText(String text) 
  {
    // text = text.replaceAll("" + SWRLParser.IMP_CHAR, "\n" + SWRLParser.IMP_CHAR + " ");
    
    return text;
  } // reformatText
  
  protected void acceptSelectedFrame() 
  {
    String text = getText();
    int pos = getCaretPosition();
    int i = pos - 1;
    while (i >= 0 && (SymbolTextField.isIdChar(text.charAt(i)) || text.charAt(i) == '?')) {
      i--;
    }
    String prefix = text.substring(i + 1, pos);
    
    extendPartialName(prefix, ((Frame) getComboBox().getSelectedItem()).getBrowserText());
    updateErrorDisplay();
    closeComboBox();
  }
} 
