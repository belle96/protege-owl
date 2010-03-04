
package edu.stanford.smi.protegex.owl.swrl.bridge;

import java.util.List;
import java.util.Set;

import edu.stanford.smi.protegex.owl.swrl.sqwrl.SQWRLResult;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.SQWRLException;

public interface RuleAndQueryProcessor 
{ 
	Set<String> getReferencedOWLClassURIs();
	Set<String> getReferencedOWLProeprtyURIs();
	Set<String> getReferencedOWLIndividualURIs();

  List<BuiltInAtom> getBuiltInAtomsFromHead(String ruleOrQueryURI);
  List<BuiltInAtom> getBuiltInAtomsFromHead(String ruleOrQueryURI, Set<String> builtInNames);
  List<BuiltInAtom> getBuiltInAtomsFromBody(String ruleOrQueryURI);
  List<BuiltInAtom> getBuiltInAtomsFromBody(String ruleOrQueryURI, Set<String> builtInNames);

  boolean isSQWRLQuery(String ruleOrQueryURI);
  boolean usesSQWRLCollections(String ruleOrQueryURI);
  SQWRLResult getSQWRLResult(String queryURI) throws SQWRLException;

  List<Atom> getSQWRLPhase1BodyAtoms(String queryURI);
  List<Atom> getSQWRLPhase2BodyAtoms(String queryURI);
  
  String getGroupName(String ruleOrQueryURI);
  void setRuleGroupName(String ruleOrQueryURI, String ruleGroupName);
  boolean isEnabled(String ruleOrQueryURI);
  void setEnabled(String ruleOrQueryURI, Boolean enable);
}
