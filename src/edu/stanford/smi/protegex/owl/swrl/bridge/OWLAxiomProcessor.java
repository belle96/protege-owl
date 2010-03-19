
package edu.stanford.smi.protegex.owl.swrl.bridge;

import java.util.List;
import java.util.Set;

import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.BuiltInException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.SQWRLException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.impl.SQWRLResultImpl;

public interface OWLAxiomProcessor 
{ 
	void process(Set<SWRLRule> rulesAndQueries) throws BuiltInException;
	void process(SWRLRule ruleOrQuery) throws BuiltInException;
	void reset();
	
	Set<String> getReferencedOWLClassURIs();
	Set<String> getReferencedOWLPropertyURIs();
	Set<String> getReferencedOWLIndividualURIs();
	
	void addReferencedIndividualURI(String uri);

	Set<String> getReferencedOWLClassURIs(SWRLRule ruleOrQuery);
	Set<String> getReferencedOWLPropertyURIs(SWRLRule ruleOrQuery);
	Set<String> getReferencedOWLIndividualURIs(SWRLRule ruleOrQuery);

  List<BuiltInAtom> getBuiltInAtomsFromHead(SWRLRule ruleOrQuery);
  List<BuiltInAtom> getBuiltInAtomsFromHead(SWRLRule ruleOrQuery, Set<String> builtInNames);
  List<BuiltInAtom> getBuiltInAtomsFromBody(SWRLRule ruleOrQuery);
  List<BuiltInAtom> getBuiltInAtomsFromBody(SWRLRule ruleOrQuery, Set<String> builtInNames);

  boolean isSQWRLQuery(String uri);
  SQWRLResultImpl getSQWRLResult(String uri) throws SQWRLException;
  SQWRLResultImpl getSQWRLUnpreparedResult(String uri) throws SQWRLException;
  List<Atom> getSQWRLPhase1BodyAtoms(SWRLRule query);
  List<Atom> getSQWRLPhase2BodyAtoms(SWRLRule query);
  boolean usesSQWRLCollections(SWRLRule query);
  
  String getRuleGroupName(String uri);
  void setRuleGroupName(String uri, String ruleGroupName);

  boolean isEnabled(String uri);
  void setEnabled(String uri, boolean isEnabled);
}
