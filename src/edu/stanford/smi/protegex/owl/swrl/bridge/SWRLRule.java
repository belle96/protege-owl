
package edu.stanford.smi.protegex.owl.swrl.bridge;

import java.util.List;
import java.util.Set;

import edu.stanford.smi.protegex.owl.swrl.sqwrl.impl.SQWRLResultImpl;

/**
 * Interface representing a SWRL rule
 */
public interface SWRLRule extends OWLAxiom, OWLEntity
{
  void setURI(String uri);
  
  List<Atom> getHeadAtoms();
  List<Atom> getBodyAtoms();
  
  List<BuiltInAtom> getBuiltInAtomsFromHead();
  List<BuiltInAtom> getBuiltInAtomsFromHead(Set<String> builtInNames);
  
  List<BuiltInAtom> getBuiltInAtomsFromBody();
  List<BuiltInAtom> getBuiltInAtomsFromBody(Set<String> builtInNames);

  void appendAtomsToBody(List<Atom> atom);

  boolean isSQWRL();
  boolean usesSQWRLCollections();
  SQWRLResultImpl getSQWRLResult();

  List<Atom> getSQWRLPhase1BodyAtoms();
  List<Atom> getSQWRLPhase2BodyAtoms();
  
  String getRuleText();
  void setRuleText(String text);
  String getRuleGroupName();
  void setRuleGroupName(String ruleGroupName);
  boolean isEnabled();
  void setEnabled(Boolean enable);
  
} // SWRLRule
