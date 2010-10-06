
package edu.stanford.smi.protegex.owl.swrl.owlapi;

import java.util.List;

/**
 * Interface representing a SWRL rule
 */
public interface SWRLRule extends OWLAxiom, OWLEntity
{
  List<SWRLAtom> getHeadAtoms();
  List<SWRLAtom> getBodyAtoms();
  
  // TODO: these will not be in the OWLAPI.
  void setBodyAtoms(List<SWRLAtom> atom);
  void appendAtomsToBody(List<SWRLAtom> atom);
  String getRuleText();
  void setRuleText(String text);
  String getRuleGroupName();
  boolean isEnabled();
  void setEnabled(boolean isEnabled);
}
