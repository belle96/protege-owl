
package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import java.util.List;

import edu.stanford.smi.protegex.owl.swrl.bridge.Atom;
import edu.stanford.smi.protegex.owl.swrl.bridge.SWRLRule;

/**
 * Class implementing a SWRL rule
 */
public class SWRLRuleImpl implements SWRLRule
{
  private String ruleURI;
  private List<Atom> bodyAtoms, headAtoms;
  
  public SWRLRuleImpl(String ruleURI, List<Atom> bodyAtoms, List<Atom> headAtoms)
  {
    this.ruleURI = ruleURI;
    this.bodyAtoms = bodyAtoms;
    this.headAtoms = headAtoms;
  } 

  public String getURI() { return ruleURI; }

  public List<Atom> getHeadAtoms() { return headAtoms; }
  public List<Atom> getBodyAtoms() { return bodyAtoms; }

  public void setURI(String ruleURI) { this.ruleURI = ruleURI; }
  public void setRuleText(String text) {}
  public String getRuleGroupName() { return ""; }
    
  public void appendAtomsToBody(List<Atom> atoms) { bodyAtoms.addAll(atoms); }
  public void setBodyAtoms(List<Atom> atoms) { bodyAtoms = atoms; }
  public String toString() { return ruleURI; }
  public boolean isEnabled() { return true; }
  public void setEnabled(boolean isEnabled) {}
  
  public String getRuleText()
  {
    String result = "";
    boolean isFirst = true;

    for (Atom atom : getBodyAtoms()) {
      if (!isFirst) result += " ^ ";
      result += "" + atom;
      isFirst = false;
    } // for
    
    result += " -> ";

    isFirst = true;
    for (Atom atom : getHeadAtoms()) {
      if (!isFirst) result += " ^ ";
      result += "" + atom;
      isFirst = false;
    } // for

    return result;
  }
} 
