
package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import java.util.List;

import edu.stanford.smi.protegex.owl.swrl.bridge.SWRLAtom;
import edu.stanford.smi.protegex.owl.swrl.bridge.SWRLRule;

/**
 * Class implementing a SWRL rule or SQWRL query.
 */
public class SWRLRuleImpl implements SWRLRule
{
  private String ruleURI;
  private List<SWRLAtom> bodyAtoms, headAtoms;
  
  public SWRLRuleImpl(String ruleURI, List<SWRLAtom> bodyAtoms, List<SWRLAtom> headAtoms)
  {
    this.ruleURI = ruleURI;
    this.bodyAtoms = bodyAtoms;
    this.headAtoms = headAtoms;
  } 

  public String getURI() { return ruleURI; }

  public List<SWRLAtom> getHeadAtoms() { return headAtoms; }
  public List<SWRLAtom> getBodyAtoms() { return bodyAtoms; }

  public void setURI(String ruleURI) { this.ruleURI = ruleURI; }
  public void setRuleText(String text) {}
  public String getRuleGroupName() { return ""; }
    
  public void appendAtomsToBody(List<SWRLAtom> atoms) { bodyAtoms.addAll(atoms); }
  public void setBodyAtoms(List<SWRLAtom> atoms) { bodyAtoms = atoms; }
  public String toString() { return ruleURI; }
  public boolean isEnabled() { return true; } // TODO - used only in SWRLRuleGroupTreeTableModel 
  public void setEnabled(boolean isEnabled) {} // TODO - used only in SWRLRuleGroupTreeTableModel
  
  public String getRuleText()
  {
    String result = "";
    boolean isFirst = true;

    for (SWRLAtom atom : getBodyAtoms()) {
      if (!isFirst) result += " ^ ";
      result += "" + atom;
      isFirst = false;
    } // for
    
    result += " -> ";

    isFirst = true;
    for (SWRLAtom atom : getHeadAtoms()) {
      if (!isFirst) result += " ^ ";
      result += "" + atom;
      isFirst = false;
    } // for

    return result;
  }
} 
