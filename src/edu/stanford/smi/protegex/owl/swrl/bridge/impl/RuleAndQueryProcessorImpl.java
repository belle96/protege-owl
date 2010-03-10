package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.stanford.smi.protegex.owl.swrl.bridge.Atom;
import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInAtom;
import edu.stanford.smi.protegex.owl.swrl.bridge.ClassAtom;
import edu.stanford.smi.protegex.owl.swrl.bridge.RuleAndQueryProcessor;
import edu.stanford.smi.protegex.owl.swrl.bridge.SWRLRule;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.BuiltInException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.SQWRLResult;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.InvalidQueryNameException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.SQWRLException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.impl.SQWRLResultImpl;

public class RuleAndQueryProcessorImpl implements RuleAndQueryProcessor 
{ 
  private HashMap<String, SWRLRule> rules, queries;
  
  // URIs of classes, properties and individuals explicitly referred to in SWRL rules. These are filled in as the SWRL rules are imported
  // and are used to determine the relevant OWL knowledge to import.
  private Set<String> referencedOWLClassURIs, referencedOWLPropertyURIs, referencedOWLIndividualURIs;

	public RuleAndQueryProcessorImpl() 
	{
		rules = new HashMap<String, SWRLRule>();
		queries = new HashMap<String, SWRLRule>();
		
		referencedOWLClassURIs = new HashSet<String>();
		referencedOWLPropertyURIs = new HashSet<String>();
		referencedOWLIndividualURIs = new HashSet<String>();
	}
	
	public Set<String> getReferencedOWLClassURIs() { return referencedOWLClassURIs; }
	public Set<String> getReferencedOWLProeprtyURIs() { return referencedOWLPropertyURIs; }
	public Set<String> getReferencedOWLIndividualURIs() { return referencedOWLIndividualURIs; }
	
	// TODO:
  public List<BuiltInAtom> getBuiltInAtomsFromHead(String ruleOrQueryURI) { return null; }
  public List<BuiltInAtom> getBuiltInAtomsFromHead(String ruleOrQueryURI, Set<String> builtInNames)  { return null; }
  public List<BuiltInAtom> getBuiltInAtomsFromBody(String ruleOrQueryURI)  { return null; }
  public List<BuiltInAtom> getBuiltInAtomsFromBody(String ruleOrQueryURI, Set<String> builtInNames)  { return null; }
  public List<Atom> getSQWRLPhase1BodyAtoms(String queryURI) { return null; }
  public List<Atom> getSQWRLPhase2BodyAtoms(String queryURI) { return null; }
  public boolean isSQWRLQuery(String ruleOrQueryURI) { return false; }
  public boolean usesSQWRLCollections(String ruleOrQueryURI)  { return false; }
 
  public String getGroupName(String ruleOrQueryURI) { return null; }
  public void setRuleGroupName(String ruleOrQueryURI, String ruleGroupName) {}
  public boolean isEnabled(String ruleOrQueryURI) { return false; }
  public void setEnabled(String ruleOrQueryURI, Boolean enable) {}
  
  /**
   *  Get the results from a SQWRL query.
   */
  public SQWRLResult getSQWRLResult(String queryURI) throws SQWRLException
  {
    SQWRLResultImpl result;

    if (!queries.containsKey(queryURI)) throw new InvalidQueryNameException(queryURI);

    result = queries.get(queryURI).getSQWRLResult();

    if (!result.isPrepared()) result.prepared();

    return result;
  }

  public List<Atom> getSQWRLPhase1BodyAtoms(SWRLRule query)
  {
    List<Atom> result = new ArrayList<Atom>();

    for (Atom atom : query.getBodyAtoms()) {
      if (atom instanceof BuiltInAtom) {
    	BuiltInAtom builtInAtom = (BuiltInAtom)atom;	
    	if (builtInAtom.usesSQWRLCollectionResults() || builtInAtom.isSQWRLGroupCollection()) continue;
      } // if
      result.add(atom);
    } // for

    return result;
  }

  public List<Atom> getSQWRLPhase2BodyAtoms(SWRLRule query)
  {
    List<Atom> result = new ArrayList<Atom>();

    for (Atom atom : query.getBodyAtoms()) {
    	if (atom instanceof BuiltInAtom) {
    	  BuiltInAtom builtInAtom = (BuiltInAtom)atom;
    	  if (builtInAtom.isSQWRLMakeCollection() || builtInAtom.isSQWRLGroupCollection()) continue;
      } // if
      result.add(atom);
    } // for

    return result;
  } // getSQWRLPhase2BodyAtoms

  /**
   * Find all built-in atoms with unbound arguments and tell them which of their arguments are unbound.
   * See <a href="http://protege.cim3.net/cgi-bin/wiki.pl?SWRLBuiltInBridge#nid88T">here</a> for a discussion of the role of this method.
   */
  private void processUnboundArguments(SWRLRule ruleOrQuery) throws SQWRLException, BuiltInException
  {
  	List<BuiltInAtom> bodyBuiltInAtoms = new ArrayList<BuiltInAtom>();
  	List<Atom> finalBodyAtoms = new ArrayList<Atom>();
    List<Atom> bodyNonBuiltInAtoms = new ArrayList<Atom>();
    Set<String> variableNamesUsedByNonBuiltInBodyAtoms = new HashSet<String>(); // By definition, these will always be bound.
    Set<String> variableNamesBoundByBuiltIns = new HashSet<String>(); // Names of variables bound by built-ins in this rule
 
    // Process the body atoms and build up list of (1) built-in body atoms, and (2) the variables used by non-built body in atoms.
    for (Atom atom : ruleOrQuery.getBodyAtoms()) {
      if (atom instanceof BuiltInAtom) bodyBuiltInAtoms.add((BuiltInAtom)atom);
      else {
        bodyNonBuiltInAtoms.add(atom); variableNamesUsedByNonBuiltInBodyAtoms.addAll(atom.getReferencedVariableNames());
      } // if
    } // for

    // Process the body built-in atoms and determine if they bind any of their arguments.
    for (BuiltInAtom builtInAtom : bodyBuiltInAtoms) { // Read through built-in arguments and determine which are unbound.
    	Set<String> dependsOnVariableNames = new HashSet<String>(variableNamesUsedByNonBuiltInBodyAtoms); // TODO: Need to work this out properly.
    	dependsOnVariableNames.removeAll(builtInAtom.getArgumentsVariableNames());
      
    	builtInAtom.setDependsOnVariableNames(dependsOnVariableNames);
    	
    	for (BuiltInArgument argument : builtInAtom.getArguments()) {
        if (argument.isVariable()) {
          String argumentVariableName = argument.getVariableName();

          // If a variable argument is not used by any non built-in body atom or is not bound by another body built-in atom it will therefore be
          // unbound when this built-in is called. We thus set this built-in argument to unbound. If a built-in binds an argument, all later
          // built-ins (proceeding from left to right) will be passed the bound value of this variable during rule execution.
          if (!variableNamesUsedByNonBuiltInBodyAtoms.contains(argumentVariableName) &&
              !variableNamesBoundByBuiltIns.contains(argumentVariableName)) {
            argument.setUnbound(); // Tell the built-in that it is expected to bind this argument.
            variableNamesBoundByBuiltIns.add(argumentVariableName); // Flag this as a bound variable for later built-ins.
          } // if
        } // if
      } // for
    } // for

    // If we have built-in atoms, construct a new body with built-in atoms moved to the end of the list. Some rule engines (e.g., Jess)
    // expect variables used as parameters to functions to have been defined before their use in a left to right fashion.
    finalBodyAtoms = processBodyNonBuiltInAtoms(bodyNonBuiltInAtoms);
    finalBodyAtoms.addAll(bodyBuiltInAtoms);
    ruleOrQuery.setBodyAtoms(finalBodyAtoms);
  }

  /**
   * Build up a list of body class atoms and non class, non built-in atoms. 
   */
  private List<Atom> processBodyNonBuiltInAtoms(List<Atom> bodyNonBuiltInAtoms)
  {
    List<Atom> bodyClassAtoms = new ArrayList<Atom>(); 
    List<Atom> bodyNonClassNonBuiltInAtoms = new ArrayList<Atom>();
    List<Atom> result = new ArrayList<Atom>();

    for (Atom atom : bodyNonBuiltInAtoms) {
      if (atom instanceof ClassAtom) bodyClassAtoms.add(atom);
      else bodyNonClassNonBuiltInAtoms.add(atom);
    } // for
    
    result.addAll(bodyNonClassNonBuiltInAtoms);
    result.addAll(bodyClassAtoms);

    return result;
  }

  private void processRulesAndQueries(Set<SWRLRule> rulesAndQueries)
  {

  }
  
  private void processSWRLRule(SWRLRule rule)
  {
  	rules.put(rule.getURI(), rule);
        
  	for (Atom atom : rule.getBodyAtoms()) processSWRLAtom(atom, false);
  	for (Atom atom : rule.getHeadAtoms()) processSWRLAtom(atom, true);
  } 

  private void processSQWRLQuery(SWRLRule query)
  {
  	queries.put(query.getURI(), query);
        
  	for (Atom atom : query.getBodyAtoms()) processSWRLAtom(atom, false);
  	for (Atom atom : query.getHeadAtoms()) processSWRLAtom(atom, true);
  } 

  private void processSWRLAtom(Atom atom, boolean isConsequent)
  {
    if (atom.hasReferencedClasses()) referencedOWLClassURIs.addAll(atom.getReferencedClassURIs());
    if (atom.hasReferencedProperties()) referencedOWLPropertyURIs.addAll(atom.getReferencedPropertyURIs());
    if (atom.hasReferencedIndividuals()) referencedOWLIndividualURIs.addAll(atom.getReferencedIndividualURIs());
  } 

}