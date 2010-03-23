package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.stanford.smi.protegex.owl.swrl.bridge.Atom;
import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInAtom;
import edu.stanford.smi.protegex.owl.swrl.bridge.ClassAtom;
import edu.stanford.smi.protegex.owl.swrl.bridge.DataValueArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLAxiomProcessor;
import edu.stanford.smi.protegex.owl.swrl.bridge.SWRLRule;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.BuiltInException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.SQWRLNames;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.DataValueConversionException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.InvalidQueryNameException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.SQWRLException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.impl.SQWRLResultImpl;

public class OWLAxiomProcessorImpl implements OWLAxiomProcessor 
{ 
  private HashMap<String, SWRLRule> rules, queries;
  
  private Map<String, Set<String>> referencedOWLClassURIMap, referencedOWLPropertyURIMap, referencedOWLIndividualURIMap;
  private Map<String, Set<String>> referencedVariableNameMap;
  
  private Map<String, SQWRLResultImpl> sqwrlResultMap;
  private Map<String, String> ruleGroupNameMap;
  private Map<String, Boolean> hasSQWRLBuiltInsMap, hasSQWRLCollectionBuiltInsMap, enabledMap;
  private Map<String, Map<String, List<BuiltInArgument>>> collectionGroupArgumentsMap;

	public OWLAxiomProcessorImpl() { reset(); }
	
	public void reset()
	{
		rules = new HashMap<String, SWRLRule>();
		queries = new HashMap<String, SWRLRule>();
		
		referencedOWLClassURIMap = new HashMap<String, Set<String>>();
		referencedOWLPropertyURIMap = new HashMap<String, Set<String>>();
		referencedOWLIndividualURIMap = new HashMap<String, Set<String>>();
		referencedVariableNameMap = new HashMap<String, Set<String>>();
		
		sqwrlResultMap = new HashMap<String, SQWRLResultImpl>();
		ruleGroupNameMap = new HashMap<String, String>();

		hasSQWRLBuiltInsMap = new HashMap<String, Boolean>();
		hasSQWRLCollectionBuiltInsMap = new HashMap<String, Boolean>();
		enabledMap = new HashMap<String, Boolean>();

		collectionGroupArgumentsMap = new HashMap<String, Map<String, List<BuiltInArgument>>>();
	}
	
	public void process(Set<SWRLRule> rulesAndQueries) throws BuiltInException
	{
		for (SWRLRule ruleOrQuery : rulesAndQueries) process(ruleOrQuery);
	}
	
	public void process(SWRLRule ruleOrQuery) throws BuiltInException
  {
        
  	for (Atom atom : ruleOrQuery.getBodyAtoms()) processSWRLAtom(ruleOrQuery, atom, false);
  	for (Atom atom : ruleOrQuery.getHeadAtoms()) processSWRLAtom(ruleOrQuery, atom, true);

    buildReferencedVariableNames(ruleOrQuery);
    processUnboundBuiltInArguments(ruleOrQuery); 
    processSQWRLBuiltIns(ruleOrQuery);
    processBuiltInArgumentDependencies(ruleOrQuery);
    
  	if (hasSQWRLBuiltIns(ruleOrQuery)) queries.put(ruleOrQuery.getURI(), ruleOrQuery);
  	rules.put(ruleOrQuery.getURI(), ruleOrQuery);
  } 

  public boolean isSQWRLQuery(String uri) 
  { 
  	return (hasSQWRLBuiltInsMap.containsKey(uri) && hasSQWRLBuiltInsMap.get(uri)) ||
  	       (hasSQWRLCollectionBuiltInsMap.containsKey(uri) && hasSQWRLCollectionBuiltInsMap.get(uri));
  }
  
  public boolean usesSQWRLCollections(SWRLRule ruleOrQuery) 
  { 
  	String uri = ruleOrQuery.getURI();
    
  	return hasSQWRLCollectionBuiltInsMap.containsKey(uri) && hasSQWRLCollectionBuiltInsMap.get(uri);
  }
  
  public String getRuleGroupName(String uri) 
  { 
  	if (ruleGroupNameMap.containsKey(uri)) return ruleGroupNameMap.get(uri);
  	else return "";
  } 
  
  public void setRuleGroupName(String uri, String ruleGroupName) 
  {
  	ruleGroupNameMap.put(uri,	ruleGroupName);
  	// TODO: set annotation
  }
  
  public boolean isEnabled(String uri) 
  {    
  	return enabledMap.containsKey(uri) && enabledMap.get(uri);
  }
  
  public void setEnabled(String uri, boolean isEnabled) 
  {
  	enabledMap.put(uri ,isEnabled);
  	// TODO: set annotation
  }
  
  public Set<String> getReferencedOWLClassURIs() 
  {
  	Set<String> result = new HashSet<String>();
  	
  	for (Set<String> referencedOWLClassURIs : referencedOWLClassURIMap.values())
  		result.addAll(referencedOWLClassURIs);
  	
  	return result;
  }

  public Set<String> getReferencedOWLPropertyURIs() 
  {
  	Set<String> result = new HashSet<String>();
  	
  	for (Set<String> referencedOWLPropertyURIs : referencedOWLPropertyURIMap.values())
  		result.addAll(referencedOWLPropertyURIs);
  	
  	return result;
  }

  public void addReferencedIndividualURI(String uri) 
  {
  	// Use the empty string to index indirectly referenced URIs
  	if (referencedOWLIndividualURIMap.containsKey("")) referencedOWLIndividualURIMap.get("").add(uri);
  	else {
  		Set<String> uris = new HashSet<String>();
  		uris.add(uri);
  		referencedOWLIndividualURIMap.put("", uris);
  	}
  }
  
  public Set<String> getReferencedOWLIndividualURIs() 
  {
  	Set<String> result = new HashSet<String>();
  	
  	for (Set<String> referencedOWLIndividualURIs : referencedOWLIndividualURIMap.values())
  		result.addAll(referencedOWLIndividualURIs);
  	
  	return result;
  }

  public Set<String> getReferencedOWLClassURIs(SWRLRule ruleOrQuery) { return referencedOWLClassURIMap.get(ruleOrQuery.getURI()); }
	public Set<String> getReferencedOWLPropertyURIs(SWRLRule ruleOrQuery) { return referencedOWLPropertyURIMap.get(ruleOrQuery.getURI()); }
	public Set<String> getReferencedOWLIndividualURIs(SWRLRule ruleOrQuery) { return referencedOWLIndividualURIMap.get(ruleOrQuery.getURI()); }
	 
  /**
   *  Get the results from a SQWRL query.
   */
  public SQWRLResultImpl getSQWRLResult(String uri) throws SQWRLException
  {
    SQWRLResultImpl result;

    if (!queries.containsKey(uri)) throw new InvalidQueryNameException(uri);

    result = sqwrlResultMap.get(uri);

    if (!result.isPrepared()) result.prepared();

    return result;
  }

  /**
   *  Get the results from a SQWRL query.
   */
  public SQWRLResultImpl getSQWRLUnpreparedResult(String uri) throws SQWRLException
  {
    if (!queries.containsKey(uri)) throw new InvalidQueryNameException(uri);

    return sqwrlResultMap.get(uri);
  }

  public boolean hasSQWRLBuiltIns(SWRLRule ruleOrQuery) 
  { 
  	String uri = ruleOrQuery.getURI();

  	return hasSQWRLBuiltInsMap.containsKey(uri) && hasSQWRLBuiltInsMap.get(uri);
  }

  public boolean hasSQWRLCollectionBuiltIns(SWRLRule ruleOrQuery) 
  { 
  	String uri = ruleOrQuery.getURI();

  	return hasSQWRLCollectionBuiltInsMap.containsKey(uri) && hasSQWRLCollectionBuiltInsMap.get(uri);
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
  }

  /**
   * Find all built-in atoms with unbound arguments and tell them which of their arguments are unbound.
   * See <a href="http://protege.cim3.net/cgi-bin/wiki.pl?SWRLBuiltInBridge#nid88T">here</a> for a discussion of the role of this method.
   */
  private void processUnboundBuiltInArguments(SWRLRule ruleOrQuery)
  {
    List<BuiltInAtom> bodyBuiltInAtoms = new ArrayList<BuiltInAtom>();
    List<Atom> bodyNonBuiltInAtoms = new ArrayList<Atom>();
    List<Atom> finalBodyAtoms = new ArrayList<Atom>();
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
    ruleOrQuery.setBodyAtoms(finalBodyAtoms);
    finalBodyAtoms.addAll(bodyBuiltInAtoms);
  } 

  // For every built-in, record the variables it depends from preceding atoms (directly and indirectly). 
  // Should be called after processBuiltInArguments and processSQWRLArguments.
  private void processBuiltInArgumentDependencies(SWRLRule ruleOrQuery) throws BuiltInException
  {
  	Map<String, Set<Set<String>>> pathMap = new HashMap<String, Set<Set<String>>>();
  	Set<String> rootVariableNames = new HashSet<String>();
  	
    for (Atom atom : ruleOrQuery.getBodyAtoms()) {
    	Set<String> thisAtomReferencedVariableNames = new HashSet<String>(atom.getReferencedVariableNames());

    	buildPaths(atom, rootVariableNames, pathMap);
    	
    	if (atom instanceof BuiltInAtom) {
    		BuiltInAtom builtInAtom = (BuiltInAtom)atom;
    		
    		if (builtInAtom.isSQWRLGroupCollection()) continue;
    		if (builtInAtom.isSQWRLCollectionOperation()) break;
    		
    		if (builtInAtom.hasReferencedVariables()) {
        	Set<String> dependsOnVariableNames = new HashSet<String>();
        	
        	for (String rootVariableName : pathMap.keySet()) {
        		for (Set<String> path : pathMap.get(rootVariableName)) {
        			if (!Collections.disjoint(path, thisAtomReferencedVariableNames)) { 
        			  dependsOnVariableNames.addAll(path);
        			  dependsOnVariableNames.add(rootVariableName);
        		  } // if
        		} // for
        	} // for
      
        	if (!dependsOnVariableNames.isEmpty()) {
          	dependsOnVariableNames.removeAll(thisAtomReferencedVariableNames); // Remove our own variables
          	/* TODO: Need to think about correct operation of this
          	if (builtInAtom.isSQWRLMakeCollection()) {
          		String collectionName = builtInAtom.getArgumentVariableName(0); // First argument is the collection name
          		if (collectionGroupArgumentsMap.containsKey(collectionName)) {
          			List<BuiltInArgument> groupArguments = collectionGroupArgumentsMap.get(collectionName);
          			Set<String> groupVariableNames = getVariableNames(groupArguments);
          			if (!groupVariableNames.isEmpty() && !dependsOnVariableNames.containsAll(groupVariableNames)) 
          				throw new BuiltInException("all group arguments must be on variable path for corresponding collection make");
          		} // if
          	} // if
          	 */
          	builtInAtom.setDependsOnVariableNames(dependsOnVariableNames);
        	} // if
    		} // if
    	} // if
    } // for
  }

  /** 
   * Incrementally build variable paths up to and including the current atom. 
   * 
   * Note: Sets of sets in Java require care because of hash code issues. The enclosed set should not be modified or the outer set may 
   * return inconsistent results.  
   */
  private void buildPaths(Atom atom, Set<String> rootVariableNames, Map<String, Set<Set<String>>> pathMap)
  {
  	Set<String> currentAtomReferencedVariableNames = atom.getReferencedVariableNames();
		Set<String> matchingRootVariableNames;
	
		if (currentAtomReferencedVariableNames.size() == 1) { // Make variable a root if we have not yet encountered it 
			String variableName = currentAtomReferencedVariableNames.iterator().next();
			if (getMatchingPaths(pathMap, variableName).isEmpty() && !rootVariableNames.contains(variableName)) {
				Set<Set<String>> paths = new HashSet<Set<String>>();
				pathMap.put(variableName, paths);
				rootVariableNames.add(variableName); 
			} // if
		} else if (currentAtomReferencedVariableNames.size() > 1) {
			Set<String> currentKnownAtomRootVariableNames = new HashSet<String>(currentAtomReferencedVariableNames);
			currentKnownAtomRootVariableNames.retainAll(rootVariableNames);
			
			if (!currentKnownAtomRootVariableNames.isEmpty()) { // At least one of atom's variables reference already known root(s)
				for (String rootVariableName : currentKnownAtomRootVariableNames) {
					Set<String> dependentVariables = new HashSet<String>(currentAtomReferencedVariableNames);
					dependentVariables.remove(rootVariableName);
					
	  			matchingRootVariableNames = getMatchingRootVariableNames(pathMap, dependentVariables);
	  			if (!matchingRootVariableNames.isEmpty()) { // Found existing path(s) that use these variables - add them to existing path(s)
	  				for (String matchingRootVariableName : matchingRootVariableNames) {
	  					Set<Set<String>> paths = pathMap.get(matchingRootVariableName);
	  					Set<Set<String>> matchedPaths = new HashSet<Set<String>>();
	  					for (Set<String> path : paths)
	  					  if (!Collections.disjoint(path, dependentVariables)) matchedPaths.add(path);
	  				  for (Set<String> matchedPath : matchedPaths) {
	  				  	Set<String> newPath = new HashSet<String>(matchedPath);   				  	
	  				  	newPath.addAll(dependentVariables);
	  				  	paths.remove(matchedPath); // Remove the original matched path for this root's path
	  				  	paths.add(Collections.unmodifiableSet(newPath)); // Add the updated path
	  				  } // for
	  				} // for
	  			} else { // Did not find an existing path for this root that uses these variables - add dependent variables as new path 
	  				Set<Set<String>> paths = pathMap.get(rootVariableName);
	  				paths.add(Collections.unmodifiableSet(dependentVariables));
	  			} //if
				} // for
			} else { // No known roots referenced by any of the atom's variables
				matchingRootVariableNames = getMatchingRootVariableNames(pathMap, currentAtomReferencedVariableNames);
				if (!matchingRootVariableNames.isEmpty()) { 
					// Found existing paths that use the atom's variables - add all the variables (none of which is a root) to each path
  				for (String matchingRootVariableName : matchingRootVariableNames) {
  					Set<Set<String>> paths = pathMap.get(matchingRootVariableName);
  					Set<Set<String>> matchedPaths = new HashSet<Set<String>>();

  					for (Set<String> path : paths) 
  					  if (!Collections.disjoint(path, currentAtomReferencedVariableNames)) matchedPaths.add(path);
  				  for (Set<String> matchedPath : matchedPaths) {  // Add the new variables to the matched path and add it to this root's path
  				  	Set<String> newPath = new HashSet<String>(matchedPath);   				  	
  				  	newPath.addAll(currentAtomReferencedVariableNames); // Add all the non-root variable names to this path
  				  	paths.remove(matchedPath); // Remove the original matched path
  				  	paths.add(Collections.unmodifiableSet(newPath)); // Add the updated path
  				  } // for
  				} // for
				} else { // No existing paths have variables from this atom - every variable becomes a root and depends on every other root variable
					for (String rootVariableName : currentAtomReferencedVariableNames) {
						Set<Set<String>> paths = new HashSet<Set<String>>();
						Set<String> dependentVariables = new HashSet<String>(currentAtomReferencedVariableNames);
						dependentVariables.remove(rootVariableName); // Remove the root from its own dependent variables
						paths.add(Collections.unmodifiableSet(dependentVariables));
						pathMap.put(rootVariableName, paths);
						rootVariableNames.add(rootVariableName);
					} // For
				} // if
			} // if
		} // if
  }

  @SuppressWarnings("unused") // Used by commented-out group argument checking in processBuiltInArgumentDependencies
  private Set<String> getVariableNames(List<BuiltInArgument> arguments) 
  {
  	Set<String> variableNames = new HashSet<String>();
  
  	for (BuiltInArgument argument : arguments)
  		if (argument.isVariable())
  			variableNames.add(argument.getVariableName());
  	
  	return variableNames;
  }

  private Set<String> getMatchingPaths(Map<String, Set<Set<String>>> pathMap, String variableName)
  { 
  	return getMatchingRootVariableNames(pathMap, Collections.singleton(variableName));
  }
  
  private Set<String> getMatchingRootVariableNames(Map<String, Set<Set<String>>> pathMap, Set<String> variableNames)
  {
    Set<String> matchingRootVariableNames = new HashSet<String>();
  	
  	for (String rootVariableName : pathMap.keySet()) {
  		Set<Set<String>> pathsWithSameRoot = pathMap.get(rootVariableName); 
  		for (Set<String> path : pathsWithSameRoot)
  		 if (!Collections.disjoint(path, variableNames))
  			 matchingRootVariableNames.add(rootVariableName);
  	} // for
  		
  	return matchingRootVariableNames;
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
    
    result.addAll(bodyClassAtoms);
    result.addAll(bodyNonClassNonBuiltInAtoms);
    
    return result;
  } 

  //TODO: too long- refactor
  private void processSQWRLHeadBuiltIns(SWRLRule query) throws DataValueConversionException, SQWRLException, BuiltInException
  {
     List<String> selectedVariableNames = new ArrayList<String>();
     SQWRLResultImpl sqwrlResult = sqwrlResultMap.get(query.getURI());

     processBuiltInIndexes(query);

     for (BuiltInAtom builtInAtom : getBuiltInAtomsFromHead(query, SQWRLNames.getHeadBuiltInNames())) {
       String builtInName = builtInAtom.getBuiltInURI();
       hasSQWRLBuiltInsMap.put(query.getURI(), true);
          
       for (BuiltInArgument argument : builtInAtom.getArguments()) {
         boolean isArgumentAVariable = argument.isVariable();
         String variableName = null, columnName;
         int argumentIndex = 0, columnIndex;

         if (SQWRLNames.isSQWRLHeadSelectionBuiltIn(builtInName) || SQWRLNames.isSQWRLHeadAggregationBuiltIn(builtInName)) {
	         if (isArgumentAVariable) { variableName = argument.getVariableName(); selectedVariableNames.add(variableName); }
	                 
	         if (builtInName.equalsIgnoreCase(SQWRLNames.Select)) {
	           if (isArgumentAVariable) columnName = "?" + variableName;
	           else columnName = "[" + argument + "]";
	           sqwrlResult.addColumn(columnName);
	         } else if (builtInName.equalsIgnoreCase(SQWRLNames.SelectDistinct)) {
	           if (isArgumentAVariable) columnName = "?" + variableName; else columnName = "[" + argument + "]";
	           sqwrlResult.addColumn(columnName); sqwrlResult.setIsDistinct();
	         } else if (builtInName.equalsIgnoreCase(SQWRLNames.Count)) {
	           if (isArgumentAVariable) columnName = "count(?" + variableName + ")"; else columnName = "[" + argument + "]";
	           sqwrlResult.addAggregateColumn(columnName, SQWRLNames.CountAggregateFunction);
	         } else if (builtInName.equalsIgnoreCase(SQWRLNames.CountDistinct)) {
	           if (isArgumentAVariable) columnName = "countDistinct(?" + variableName + ")"; else columnName = "[" + argument + "]";
	           sqwrlResult.addAggregateColumn(columnName, SQWRLNames.CountDistinctAggregateFunction);
	         } else if (builtInName.equalsIgnoreCase(SQWRLNames.Min)) {
	           if (isArgumentAVariable) columnName = "min(?" + variableName + ")"; else columnName = "min[" + argument + "]";
	           sqwrlResult.addAggregateColumn(columnName, SQWRLNames.MinAggregateFunction);
	         } else if (builtInName.equalsIgnoreCase(SQWRLNames.Max)) {
	           if (isArgumentAVariable) columnName = "max(?" + variableName + ")"; else columnName = "max[" + argument + "]";
	           sqwrlResult.addAggregateColumn(columnName, SQWRLNames.MaxAggregateFunction);
	         } else if (builtInName.equalsIgnoreCase(SQWRLNames.Sum)) {
	           if (isArgumentAVariable) columnName = "sum(?" + variableName + ")"; else columnName = "sum[" + argument + "]";
	           sqwrlResult.addAggregateColumn(columnName, SQWRLNames.SumAggregateFunction);
	         } else if (builtInName.equalsIgnoreCase(SQWRLNames.Median)) {
	           if (isArgumentAVariable) columnName = "median(?" + variableName + ")"; else columnName = "median[" + argument + "]";
	           sqwrlResult.addAggregateColumn(columnName, SQWRLNames.MedianAggregateFunction);
	         } else if (builtInName.equalsIgnoreCase(SQWRLNames.Avg)) {
	           if (isArgumentAVariable) columnName = "avg(?" + variableName + ")"; else columnName = "avg[" + argument + "]";
	           sqwrlResult.addAggregateColumn(columnName, SQWRLNames.AvgAggregateFunction);
	         } else if (builtInName.equalsIgnoreCase(SQWRLNames.OrderBy)) {
	           if (!isArgumentAVariable) throw new SQWRLException("only variables allowed for ordered columns - found " + argument);
	           columnIndex = selectedVariableNames.indexOf(variableName);
	           if (columnIndex != -1) sqwrlResult.addOrderByColumn(columnIndex, true);
	           else throw new SQWRLException("variable ?" + variableName + " must be selected before it can be ordered");
	         } else if (builtInName.equalsIgnoreCase(SQWRLNames.OrderByDescending)) {
	           if (!isArgumentAVariable) throw new SQWRLException("only variables allowed for ordered columns - found " + argument);
	           columnIndex = selectedVariableNames.indexOf(variableName);
	           if (columnIndex != -1) sqwrlResult.addOrderByColumn(columnIndex, false);
	           else throw new SQWRLException("variable ?" + variableName + " must be selected before it can be ordered");
	         } else if (builtInName.equalsIgnoreCase(SQWRLNames.ColumnNames)) {
	           if (argument instanceof DataValueArgument && ((DataValueArgument)argument).getDataValue().isString()) {
	             DataValueArgument dataValueArgument = (DataValueArgument)argument;
	             sqwrlResult.addColumnDisplayName(dataValueArgument.getDataValue().getString());
	           } else throw new SQWRLException("only string literals allowed as column names - found " + argument);
	         } // if
	         argumentIndex++;
         } // if
       } // for
         
       if (SQWRLNames.isSQWRLHeadSlicingBuiltIn(builtInName)) {
      	 if (!sqwrlResult.isOrdered() && !builtInName.equals(SQWRLNames.Limit)) throw new SQWRLException("slicing operator used without an order clause");
        	 
      	 if (builtInName.equalsIgnoreCase(SQWRLNames.Least) || builtInName.equalsIgnoreCase(SQWRLNames.First)) {
      		 if (!builtInAtom.getArguments().isEmpty()) throw new SQWRLException("first or least do not accept arguments");
      		 sqwrlResult.setFirst();
      	 } else if (builtInName.equalsIgnoreCase(SQWRLNames.NotLeast) || builtInName.equalsIgnoreCase(SQWRLNames.NotFirst)) {
        		 if (!builtInAtom.getArguments().isEmpty()) throw new SQWRLException("not first or least do not accept arguments");
        		 sqwrlResult.setNotFirst();
      	 } else if (builtInName.equalsIgnoreCase(SQWRLNames.Greatest) || builtInName.equalsIgnoreCase(SQWRLNames.Last)) {
      		 if (!builtInAtom.getArguments().isEmpty()) throw new SQWRLException("greatest or last do not accept arguments");
      		 sqwrlResult.setLast();
      	 } else if (builtInName.equalsIgnoreCase(SQWRLNames.NotGreatest) || builtInName.equalsIgnoreCase(SQWRLNames.NotLast)) {
      		 if (!builtInAtom.getArguments().isEmpty()) throw new SQWRLException("not greatest or last do not accept arguments");
      		 sqwrlResult.setNotLast();
      	 } else {
      		 BuiltInArgument nArgument = builtInAtom.getArguments().get(0);
      		 int n;
      		 
        	 if (nArgument instanceof DataValueArgument && ((DataValueArgument)nArgument).getDataValue().isLong()) {
             n = (int)((DataValueArgument)nArgument).getDataValue().getLong();
             if (n < 1) throw new SQWRLException("nth argument to slicing operator " + builtInName + " must be a positive integer");
        	 } else throw new SQWRLException("expecing integer to slicing operator " + builtInName);

      		 if (builtInAtom.getArguments().size() == 1) {
	        		 if (builtInName.equalsIgnoreCase(SQWRLNames.Limit)) sqwrlResult.setLimit(n);
	             else if (builtInName.equalsIgnoreCase(SQWRLNames.Nth)) sqwrlResult.setNth(n);
	             else if (builtInName.equalsIgnoreCase(SQWRLNames.NotNth)) sqwrlResult.setNotNth(n);
	             else if (builtInName.equalsIgnoreCase(SQWRLNames.FirstN) || builtInName.equalsIgnoreCase(SQWRLNames.LeastN)) sqwrlResult.setFirst(n);
	             else if (builtInName.equalsIgnoreCase(SQWRLNames.LastN) || builtInName.equalsIgnoreCase(SQWRLNames.GreatestN)) sqwrlResult.setLast(n);
	             else if (builtInName.equalsIgnoreCase(SQWRLNames.NotLastN) || builtInName.equalsIgnoreCase(SQWRLNames.NotGreatestN)) sqwrlResult.setNotLast(n);
	             else if (builtInName.equalsIgnoreCase(SQWRLNames.NotFirstN) || builtInName.equalsIgnoreCase(SQWRLNames.NotLeastN)) sqwrlResult.setNotFirst(n);
	             else throw new SQWRLException("unknown slicing operator " + builtInName);
      	 } else if (builtInAtom.getArguments().size() == 2) {
	      		 BuiltInArgument sliceArgument = builtInAtom.getArguments().get(1);
	      		 int sliceSize;
	      		 
	        	 if (sliceArgument instanceof DataValueArgument && ((DataValueArgument)sliceArgument).getDataValue().isLong()) {
	             sliceSize = (int)((DataValueArgument)sliceArgument).getDataValue().getLong();
	             if (sliceSize < 1) throw new SQWRLException("slice size argument to slicing operator " + builtInName + " must be a positive integer");
	        	 } else throw new SQWRLException("expecing integer to slicing operator " + builtInName);
	        	 
	        	 if (builtInName.equalsIgnoreCase(SQWRLNames.NthSlice)) sqwrlResult.setNthSlice(n, sliceSize);
	        	 else if (builtInName.equalsIgnoreCase(SQWRLNames.NotNthSlice)) sqwrlResult.setNotNthSlice(n, sliceSize);
	        	 else if (builtInName.equalsIgnoreCase(SQWRLNames.NthLastSlice) ||
     			 		  builtInName.equalsIgnoreCase(SQWRLNames.NthGreatestSlice)) sqwrlResult.setNthLastSlice(n, sliceSize);
	        	 else if (builtInName.equalsIgnoreCase(SQWRLNames.NotNthLastSlice) ||
     			 		  builtInName.equalsIgnoreCase(SQWRLNames.NotNthGreatestSlice)) sqwrlResult.setNotNthLastSlice(n, sliceSize);
	        	 else throw new SQWRLException("unknown slicing operator " + builtInName);    			
      	 } else throw new SQWRLException("unknown slicing operator " + builtInName);
      	 } // if
       } // if
     } // for       
  } 

  private void processSQWRLBuiltIns(SWRLRule query) throws DataValueConversionException, SQWRLException, BuiltInException
  { 
    Set<String> collectionNames = new HashSet<String>();
    Set<String> cascadedUnboundVariableNames = new HashSet<String>();
    SQWRLResultImpl sqwrlResult = new SQWRLResultImpl();
    sqwrlResultMap.put(query.getURI(), sqwrlResult);
    
    processSQWRLHeadBuiltIns(query);
    processSQWRLCollectionMakeBuiltIns(query, collectionNames); // Find all make collection built-ins
    processSQWRLCollectionGroupByBuiltIns(query, collectionNames); // Find the group arguments for each collection
    processSQWRLCollectionMakeGroupArguments(query, collectionNames); // Add the group arguments to the make built-ins for its collection
    processSQWRLCollectionOperationBuiltIns(query, collectionNames, cascadedUnboundVariableNames);
    processBuiltInsThatUseSQWRLCollectionOperationResults(query, cascadedUnboundVariableNames);
    
    sqwrlResult.configured();
    sqwrlResult.openRow();
    
    if (hasSQWRLCollectionBuiltIns(query)) sqwrlResult.setIsDistinct(); 
  } 

  // Process all make collection built-ins.
  private void processSQWRLCollectionMakeBuiltIns(SWRLRule query, Set<String> collectionNames) 
    throws SQWRLException, BuiltInException
  {
    for (BuiltInAtom builtInAtom : getBuiltInAtomsFromBody(query, SQWRLNames.getCollectionMakeBuiltInNames())) {
      String collectionName = builtInAtom.getArgumentVariableName(0); // First argument is the collection name
      hasSQWRLCollectionBuiltInsMap.put(query.getURI(), true);
       
      if (!collectionNames.contains(collectionName)) collectionNames.add(collectionName);
    } // for
  } 

  // We store the group arguments for each collection specified in the make operation; these arguments are later appended to the collection
  // operation built-ins
  private void processSQWRLCollectionGroupByBuiltIns(SWRLRule ruleOrQuery, Set<String> collectionNames) 
    throws SQWRLException, BuiltInException
  {
    for (BuiltInAtom builtInAtom : getBuiltInAtomsFromBody(ruleOrQuery, SQWRLNames.getCollectionGroupByBuiltInNames())) {
      String collectionName = builtInAtom.getArgumentVariableName(0); // The first argument is the collection name.
      List<BuiltInArgument> builtInArguments = builtInAtom.getArguments();
      List<BuiltInArgument> groupArguments = builtInArguments.subList(1, builtInArguments.size());
      String uri = ruleOrQuery.getURI();
      Map<String, List<BuiltInArgument>> collectionGroupArguments;

      hasSQWRLCollectionBuiltInsMap.put(uri, true);
    
      if (builtInAtom.getNumberOfArguments() < 2) throw new SQWRLException("groupBy must have at least two arguments");
      if (!collectionNames.contains(collectionName)) throw new SQWRLException("groupBy applied to undefined collection ?" + collectionName);
      if (collectionGroupArgumentsMap.containsKey(collectionName)) throw new SQWRLException("groupBy specified more than once for same collection ?" + collectionName);
      if (hasUnboundArgument(groupArguments)) throw new SQWRLException("unbound group argument passed to groupBy for collection ?" + collectionName);
        
      if (collectionGroupArgumentsMap.containsKey(uri))
      	collectionGroupArguments = collectionGroupArgumentsMap.get(uri);
      else {
      	collectionGroupArguments = new HashMap<String, List<BuiltInArgument>>();
        collectionGroupArgumentsMap.put(uri, collectionGroupArguments);
      } // if
      
      collectionGroupArguments.put(collectionName, groupArguments); // Store group arguments.          
    } // for
  }

  private void processSQWRLCollectionMakeGroupArguments(SWRLRule ruleOrQuery, Set<String> collectionNames)
    throws SQWRLException, BuiltInException
  {
    for (BuiltInAtom builtInAtom : getBuiltInAtomsFromBody(ruleOrQuery, SQWRLNames.getCollectionMakeBuiltInNames())) {
      String collectionName = builtInAtom.getArgumentVariableName(0); // First argument is the collection name
      Map<String, List<BuiltInArgument>> collectionGroupArguments;
      String uri = ruleOrQuery.getURI();
       
      if (!collectionNames.contains(collectionName)) throw new SQWRLException("groupBy applied to undefined collection ?" + collectionName);
      
      if (collectionGroupArgumentsMap.containsKey(uri))	collectionGroupArguments = collectionGroupArgumentsMap.get(uri);
      else {
      	collectionGroupArguments = new HashMap<String, List<BuiltInArgument>>();
        collectionGroupArgumentsMap.put(uri, collectionGroupArguments);
      } // if

      if (collectionGroupArguments.containsKey(collectionName))  
    		builtInAtom.addArguments(collectionGroupArguments.get(collectionName)); // Append each collections's group arguments to make built-in.     
    } // for
  } 

  private void processSQWRLCollectionOperationBuiltIns(SWRLRule ruleOrQuery,  Set<String> collectionNames, Set<String> cascadedUnboundVariableNames) 
    throws SQWRLException, BuiltInException
  {
  	for (BuiltInAtom builtInAtom : getBuiltInAtomsFromBody(ruleOrQuery, SQWRLNames.getCollectionOperationBuiltInNames())) {
  		List<BuiltInArgument> allOperandCollectionGroupArguments = new ArrayList<BuiltInArgument>(); // The group arguments from the operand collections
      Map<String, List<BuiltInArgument>> collectionGroupArguments;
      String uri = ruleOrQuery.getURI();
  		List<String> variableNames;
  		
  		builtInAtom.setUsesSQWRLCollectionResults();

  		if (builtInAtom.hasUnboundArguments()) { // Keep track of built-in's unbound arguments so that we can mark dependent built-ins.
  			Set<String> unboundVariableNames = builtInAtom.getUnboundArgumentVariableNames();
  			cascadedUnboundVariableNames.addAll(unboundVariableNames);
  		} // if
  		
  	  // Append the group arguments to built-ins for each of its collection arguments; also append group arguments
  	  // to collections created by operation built-ins.
  		if (builtInAtom.isSQWRLCollectionCreateOperation()) variableNames = builtInAtom.getArgumentsVariableNamesExceptFirst();
  		else variableNames = builtInAtom.getArgumentsVariableNames();

      if (collectionGroupArgumentsMap.containsKey(uri))	collectionGroupArguments = collectionGroupArgumentsMap.get(uri);
      else {
      	collectionGroupArguments = new HashMap<String, List<BuiltInArgument>>();
        collectionGroupArgumentsMap.put(uri, collectionGroupArguments);
      } // if

      for (String variableName : variableNames) {
      	if (collectionNames.contains(variableName) && collectionGroupArguments.containsKey(variableName)) { // The variable refers to a grouped collection.
      		builtInAtom.addArguments(collectionGroupArguments.get(variableName)); // Append each collections's group arguments to built-in.
      		allOperandCollectionGroupArguments.addAll(collectionGroupArguments.get(variableName));
      	} // if
      } // for
      	
      if (builtInAtom.isSQWRLCollectionCreateOperation()) { // If a collection is created we need to record it and store necessary group arguments. 
      	String createdCollectionName = builtInAtom.getArgumentVariableName(0); // The first argument is the collection name.
      		
      	if (!collectionNames.contains(createdCollectionName)) collectionNames.add(createdCollectionName);
      		
      	if (!allOperandCollectionGroupArguments.isEmpty()) 
      			collectionGroupArguments.put(createdCollectionName, allOperandCollectionGroupArguments); // Store group arguments from all operand collections.
      } // if
    } // for
  }

  private void processBuiltInsThatUseSQWRLCollectionOperationResults(SWRLRule ruleOrQuery, Set<String> cascadedUnboundVariableNames) 
    throws SQWRLException, BuiltInException
  {
    for (BuiltInAtom builtInAtom : getBuiltInAtomsFromBody(ruleOrQuery)) {
      if (!builtInAtom.isSQWRLBuiltIn()) { // Mark later non SQWRL built-ins that (directly or indirectly) use variables bound by collection operation built-ins.
      	if (builtInAtom.usesAtLeastOneVariableOf(cascadedUnboundVariableNames)) {
      		builtInAtom.setUsesSQWRLCollectionResults(); // Mark this built-in as dependent on collection built-in bindings.
      		if (builtInAtom.hasUnboundArguments())  // Cascade the dependency from this built-in to others using its arguments.
            cascadedUnboundVariableNames.addAll(builtInAtom.getUnboundArgumentVariableNames()); // Record its unbound variables too.
        } // if
      } // if
    } // for
  }

  private void buildReferencedVariableNames(SWRLRule ruleOrQuery)
  {
    String uri = ruleOrQuery.getURI();
    
    for (Atom atom : ruleOrQuery.getBodyAtoms()) 
    	if (referencedVariableNameMap.containsKey(uri)) referencedVariableNameMap.get(uri).addAll(atom.getReferencedVariableNames());
    	else referencedVariableNameMap.put(uri, new HashSet<String>(atom.getReferencedVariableNames()));
  }

  /**
   * Give each built-in a unique index proceeding from left to right.
   */
  private void processBuiltInIndexes(SWRLRule ruleOrQuery)
  {
    int builtInIndex = 0;

    for (BuiltInAtom builtInAtom : getBuiltInAtomsFromBody(ruleOrQuery)) builtInAtom.setBuiltInIndex(builtInIndex++);
    for (BuiltInAtom builtInAtom : getBuiltInAtomsFromHead(ruleOrQuery)) builtInAtom.setBuiltInIndex(builtInIndex++);
  }
  
  private boolean hasUnboundArgument(List<BuiltInArgument> arguments)
  {
  	for (BuiltInArgument argument : arguments) if (argument.isUnbound()) return true;
  	return false;
  }
 
  private void processSWRLAtom(SWRLRule ruleOrQuery, Atom atom, boolean isConsequent)
  {
  	String uri = ruleOrQuery.getURI();
  	
    if (atom.hasReferencedClasses()) 
    	if (referencedOWLClassURIMap.containsKey(uri)) referencedOWLClassURIMap.get(uri).addAll(atom.getReferencedClassURIs());
    	else referencedOWLClassURIMap.put(uri, atom.getReferencedClassURIs());

    if (atom.hasReferencedProperties()) 
    	if (referencedOWLPropertyURIMap.containsKey(uri)) referencedOWLPropertyURIMap.get(uri).addAll(atom.getReferencedPropertyURIs());
    	else referencedOWLPropertyURIMap.put(uri, atom.getReferencedPropertyURIs());

    if (atom.hasReferencedIndividuals()) 
    	if (referencedOWLIndividualURIMap.containsKey(uri)) referencedOWLIndividualURIMap.get(uri).addAll(atom.getReferencedIndividualURIs());
    	else referencedOWLIndividualURIMap.put(uri, atom.getReferencedIndividualURIs());
  } 

  private List<BuiltInAtom> getBuiltInAtoms(List<Atom> atoms, Set<String> builtInNames) 
  {
    List<BuiltInAtom> result = new ArrayList<BuiltInAtom>();
    
    for (Atom atom : atoms) {
      if (atom instanceof BuiltInAtom) {
        BuiltInAtom builtInAtom = (BuiltInAtom)atom;
        if (builtInNames.contains(builtInAtom.getBuiltInURI())) result.add(builtInAtom);
        } // if
    } // for
    return result;
  } 

  private List<BuiltInAtom> getBuiltInAtoms(List<Atom> atoms) 
  {
    List<BuiltInAtom> result = new ArrayList<BuiltInAtom>();
    
    for (Atom atom : atoms) if (atom instanceof BuiltInAtom) result.add((BuiltInAtom)atom);

    return result;
  }
  
  public List<BuiltInAtom> getBuiltInAtomsFromHead(SWRLRule ruleOrQuery) 
  { 
  	return getBuiltInAtoms(ruleOrQuery.getHeadAtoms()); 
  }
  
  public List<BuiltInAtom> getBuiltInAtomsFromHead(SWRLRule ruleOrQuery, Set<String> builtInNames) 
  { 
  	return getBuiltInAtoms(ruleOrQuery.getHeadAtoms(), builtInNames); 
  }

  public List<BuiltInAtom> getBuiltInAtomsFromBody(SWRLRule ruleOrQuery) 
  { 
  	return getBuiltInAtoms(ruleOrQuery.getBodyAtoms()); 
  }
  
  public List<BuiltInAtom> getBuiltInAtomsFromBody(SWRLRule ruleOrQuery, Set<String> builtInNames) 
  { 
  	return getBuiltInAtoms(ruleOrQuery.getBodyAtoms(), builtInNames); 
  }


}