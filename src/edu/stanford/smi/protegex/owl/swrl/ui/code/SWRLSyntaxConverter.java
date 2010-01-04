package edu.stanford.smi.protegex.owl.swrl.ui.code;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.swrl.parser.SWRLParser;
import edu.stanford.smi.protegex.owl.ui.code.OWLTextFormatter;
import edu.stanford.smi.protegex.owl.ui.code.SyntaxConverter;

import javax.swing.text.JTextComponent;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class SWRLSyntaxConverter implements SyntaxConverter 
{
  private OWLModel owlModel;
  private static Map map = new HashMap();
  
  static {
    map.put("->", "" + SWRLParser.IMP_CHAR);
  }

  public SWRLSyntaxConverter(OWLModel owlModel) { this.owlModel = owlModel; }
  public void convertSyntax(JTextComponent textComponent) { OWLTextFormatter.updateSyntax(textComponent, owlModel, map); }
} // SWRLSyntaxConverter
