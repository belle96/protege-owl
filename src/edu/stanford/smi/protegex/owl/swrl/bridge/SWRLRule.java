
package edu.stanford.smi.protegex.owl.swrl.bridge;

import java.util.List;

import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLEntity;

/**
 * Interface representing a SWRL rule
 */
public interface SWRLRule extends OWLAxiom, OWLEntity
{
  List<Atom> getHeadAtoms();
  List<Atom> getBodyAtoms();
  
  // TODO: these will not be in the OWLAPI.
  void setBodyAtoms(List<Atom> atom);
  void appendAtomsToBody(List<Atom> atom);
  String getRuleText();
  void setRuleText(String text);
  String getRuleGroupName();
  boolean isEnabled();
  void setEnabled(boolean isEnabled);
}
