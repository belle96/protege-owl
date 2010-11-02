package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.SWRLAndSQWRLProcessor;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.BuiltInException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.OWLConversionFactoryException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.OWLFactoryException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.SWRLBuiltInBridgeException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.SWRLRuleEngineBridgeException;
import edu.stanford.smi.protegex.owl.swrl.exceptions.SWRLRuleEngineException;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLClass;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLDataFactory;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLDifferentIndividualsAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLNamedIndividual;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLObjectPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLOntology;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLProperty;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLSameIndividualAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.SWRLAtom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.SWRLBuiltInAtom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.SWRLClassAtom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.SWRLLiteralArgument;
import edu.stanford.smi.protegex.owl.swrl.owlapi.SWRLRule;
import edu.stanford.smi.protegex.owl.swrl.owlapi.impl.OWLDataFactoryImpl;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.SQWRLNames;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.DataValueConversionException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.InvalidQueryNameException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.SQWRLException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.impl.SQWRLResultImpl;

public class SWRLAndSQWRLProcessorImpl implements SWRLAndSQWRLProcessor 
{ 
	private OWLOntology activeOntology;
	private OWLDataFactory dataFactory;
	
  private HashMap<String, SWRLRule> rules, queries;
  
  private Map<String, Set<String>> referencedOWLClassURIMap, referencedOWLPropertyURIMap, referencedOWLIndividualURIMap;
  private Map<String, Set<String>> referencedVariableNameMap;
  
  private Map<String, SQWRLResultImpl> sqwrlResultMap;
  private Map<String, String> ruleGroupNameMap;
  private Map<String, Boolean> hasSQWRLBuiltInsMap, hasSQWRLCollectionBuiltInsMap, enabledMap;
  private Map<String, Map<String, List<BuiltInArgument>>> collectionGroupArgumentsMap;

  // Imported classes, properties, and individuals
  private HashMap<String, SWRLRule> importedSWRLRules; 
  private HashMap<String, OWLClass> importedOWLClassDeclarations;
  private HashMap<String, OWLProperty> importedOWLPropertyDeclarations;
  private HashMap<String, OWLNamedIndividual> importedOWLIndividualDeclarations;
  private Set<String> importedOWLObjectPropertyURIs, importedOWLDataPropertyURIs;
  private Set<OWLAxiom> importedOWLAxioms;

  // All entities
  private Map<String, Map<String, Set<OWLPropertyAssertionAxiom>>> allOWLPropertyAssertionAxioms; // individualURI <propertyURI, axiom>
  private Map<String, OWLNamedIndividual> allOWLIndividuals; 

	public SWRLAndSQWRLProcessorImpl(OWLOntology activeOntology) 
	{
		this.activeOntology = activeOntology;
		this.dataFactory = new OWLDataFactoryImpl(activeOntology);
		reset(); 
	}
	
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
		
		importedSWRLRules = new HashMap<String, SWRLRule>();

		importedOWLClassDeclarations = new HashMap<String, OWLClass>();
		importedOWLPropertyDeclarations = new HashMap<String, OWLProperty>();
    importedOWLIndividualDeclarations = new HashMap<String, OWLNamedIndividual>();
    
    importedOWLAxioms = new HashSet<OWLAxiom>(); 
    importedOWLObjectPropertyURIs = new HashSet<String>();
    importedOWLDataPropertyURIs = new HashSet<String>();
    importedOWLAxioms = new HashSet<OWLAxiom>();

    allOWLPropertyAssertionAxioms = new HashMap<String, Map<String, Set<OWLPropertyAssertionAxiom>>>();
    allOWLIndividuals = new HashMap<String, OWLNamedIndividual>();
	}

	public void importSWRLRulesAndOWLAxioms() throws SWRLRuleEngineException
	{
		reset();
	  importSWRLRules(); 
	  importReferencedOWLKnowledge();
	} 

	public void importSQWRLQueryAndOWLAxioms(String queryName) throws SWRLRuleEngineException
	{
		reset();
	  importSQWRLQuery(queryName); 
	  importReferencedOWLKnowledge();
	} 

	public void importReferencedOWLAxioms() throws SWRLRuleEngineException
	{ 
	  importReferencedOWLKnowledge();
	} 
	
  public SWRLRule getSQWRLQuery(String queryURI) throws SQWRLException
  {
    if (!queries.containsKey(queryURI)) throw new SQWRLException("invalid query name " + queryURI);

    return queries.get(queryURI);
  }

  public SWRLRule getSWRLRule(String ruleURI) throws SWRLRuleEngineException
  {
    if (!importedSWRLRules.containsKey(ruleURI)) throw new SWRLRuleEngineException("invalid rule name " + ruleURI);

    return importedSWRLRules.get(ruleURI);
  }

	public int getNumberOfImportedSWRLRules() { return importedSWRLRules.values().size(); }
	public int getNumberOfImportedOWLClassDeclarations() { return importedOWLClassDeclarations.values().size(); }
	public int getNumberOfImportedOWLPropertyDeclarations() { return importedOWLPropertyDeclarations.values().size(); }
  public int getNumberOfImportedOWLIndividualDeclarations() { return importedOWLIndividualDeclarations.values().size(); }
  public int getNumberOfImportedOWLAxioms() { return importedOWLAxioms.size(); }
  
  public Set<SWRLRule> getImportedSWRLRules() { return new HashSet<SWRLRule>(importedSWRLRules.values()); }
  public Set<OWLClass> getImportedOWLClassDeclarations() { return new HashSet<OWLClass>(importedOWLClassDeclarations.values()); }
  public Set<OWLProperty> getImportedOWLPropertyDeclarations() { return new HashSet<OWLProperty>(importedOWLPropertyDeclarations.values()); }
  public Set<OWLNamedIndividual> getImportedOWLIndividualDeclarations()  { return new HashSet<OWLNamedIndividual>(importedOWLIndividualDeclarations.values()); }
  public Set<OWLAxiom> getImportedOWLAxioms() { return new HashSet<OWLAxiom>(importedOWLAxioms); }
  
  public boolean isImportedOWLClass(String uri) { return importedOWLClassDeclarations.containsKey(uri); }
  public boolean isImportedOWLIndividual(String uri) { return importedOWLIndividualDeclarations.containsKey(uri); }
  public boolean isImportedOWLObjectProperty(String uri) { return importedOWLObjectPropertyURIs.contains(uri); }
  public boolean isImportedOWLDataProperty(String uri) { return importedOWLDataPropertyURIs.contains(uri); }
  
  public Set<OWLNamedIndividual> getAllOWLIndividuals() { return new HashSet<OWLNamedIndividual>(allOWLIndividuals.values()); } 

	public void process(SWRLRule ruleOrQuery) throws BuiltInException
  {
  	for (SWRLAtom atom : ruleOrQuery.getBodyAtoms()) processSWRLAtom(ruleOrQuery, atom, false);
  	for (SWRLAtom atom : ruleOrQuery.getHeadAtoms()) processSWRLAtom(ruleOrQuery, atom, true);

    buildReferencedVariableNames(ruleOrQuery);
    processUnboundBuiltInArguments(ruleOrQuery); 
    processSQWRLBuiltIns(ruleOrQuery);
    processBuiltInArgumentDependencies(ruleOrQuery);
    
  	if (isSQWRLQuery(ruleOrQuery)) 
  		queries.put(ruleOrQuery.getURI(), ruleOrQuery);
  	
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
  	if (referencedOWLIndividualURIMap.containsKey("")) 
  		referencedOWLIndividualURIMap.get("").add(uri);
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
   *  Get the results from a previously executed SQWRL query.
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

  public boolean isSQWRLQuery(SWRLRule ruleOrQuery) 
  { 
    return !getBuiltInAtomsFromHead(ruleOrQuery, SQWRLNames.getSQWRLBuiltInNames()).isEmpty() ||
    	     !getBuiltInAtomsFromBody(ruleOrQuery, SQWRLNames.getSQWRLBuiltInNames()).isEmpty();
  }

  public boolean hasSQWRLCollectionBuiltIns(SWRLRule ruleOrQuery) 
  { 
    return !getBuiltInAtomsFromBody(ruleOrQuery, SQWRLNames.getCollectionMakeBuiltInNames()).isEmpty();
  }

  public List<SWRLAtom> getSQWRLPhase1BodyAtoms(SWRLRule query)
  {
    List<SWRLAtom> result = new ArrayList<SWRLAtom>();

    for (SWRLAtom atom : query.getBodyAtoms()) {
      if (atom instanceof SWRLBuiltInAtom) {
    	SWRLBuiltInAtom builtInAtom = (SWRLBuiltInAtom)atom;	
    	if (builtInAtom.usesSQWRLCollectionResults() || builtInAtom.isSQWRLGroupCollection()) continue;
      } // if
      result.add(atom);
    } // for

    return result;
  }

  public List<SWRLAtom> getSQWRLPhase2BodyAtoms(SWRLRule query)
  {
    List<SWRLAtom> result = new ArrayList<SWRLAtom>();

    for (SWRLAtom atom : query.getBodyAtoms()) {
    	if (atom instanceof SWRLBuiltInAtom) {
    	  SWRLBuiltInAtom builtInAtom = (SWRLBuiltInAtom)atom;
    	  if (builtInAtom.isSQWRLMakeCollection() || builtInAtom.isSQWRLGroupCollection()) continue;
      } // if
      result.add(atom);
    } // for

    return result;
  }

  public Set<SWRLRule> getSQWRLQueries() throws SQWRLException
  {
  	Set<SWRLRule> sqwrlQueries = new HashSet<SWRLRule>();
  	
    try {
      for (SWRLRule ruleOrQuery : dataFactory.getSWRLRules()) 
      	if (isSQWRLQuery(ruleOrQuery))
      		sqwrlQueries.add(ruleOrQuery);    	  
    } catch (OWLFactoryException e) {
      throw new SQWRLException("factory error importing SQWRL queries: " + e.getMessage());
    } // try
    
    return sqwrlQueries;
  }

  public Set<String> getSQWRLQueryNames() throws SQWRLException
  {
  	Set<String> sqwrlQueryNames = new HashSet<String>();
  	
    try {
      for (SWRLRule ruleOrQuery : dataFactory.getSWRLRules()) 
      	if (isSQWRLQuery(ruleOrQuery))
      		sqwrlQueryNames.add(ruleOrQuery.getURI());    	  
    } catch (OWLFactoryException e) {
      throw new SQWRLException("factory error importing SQWRL queries: " + e.getMessage());
    } // try
    
    return sqwrlQueryNames;
  }

  private void importSWRLRules() throws SWRLRuleEngineException
  {
    try {
      for (SWRLRule rule : dataFactory.getSWRLRules()) 
      	if (!isSQWRLQuery(rule))
      		importSWRLRule(rule); // Ignore SQWRL queries    	  
    } catch (OWLFactoryException e) {
      throw new SWRLRuleEngineBridgeException("factory error importing rules: " + e.getMessage());
    } // try
  }

  private void importSQWRLQuery(String queryName) throws SWRLRuleEngineException
  {
    try {
      for (SWRLRule rule : dataFactory.getSWRLRules()) { 
      	if (isSQWRLQuery(rule) && !rule.getURI().equals(queryName))
      		continue; // Ignore SQWRL queries apart from the named one
      	importSWRLRule(rule);    	  
      } // for
    } catch (OWLFactoryException e) {
      throw new SWRLRuleEngineBridgeException("factory error importing rule " + queryName + ": " + e.getMessage());
    } // try
  }

  private void importSWRLRule(SWRLRule rule) throws SWRLRuleEngineBridgeException
  {
  	try {
      importedSWRLRules.put(rule.getURI(), rule);  
      process(rule);
    } catch (SQWRLException e) {
      throw new SWRLRuleEngineBridgeException("SQWRL error importing rules: " + e.getMessage());
    } catch (BuiltInException e) {
      throw new SWRLRuleEngineBridgeException("built-in error importing rules: " + e.getMessage());
    } // try
  }

  private void importReferencedOWLKnowledge() throws SWRLRuleEngineBridgeException
  {
  	// Import all (directly or indirectly) referenced classes.
  	importOWLClassesByName(getReferencedOWLClassURIs()); 
  	
  	// Import property assertion axioms for (directly or indirectly) referenced properties
  	importOWLPropertyAssertionAxiomsByName(getReferencedOWLPropertyURIs()); 
      
  	// Import all directly referenced individuals.
  	importOWLIndividualsByName(getReferencedOWLIndividualURIs()); 
      
  	// Import all individuals that are members of imported classes.
  	importAllOWLIndividualsOfClassesByName(getReferencedOWLClassURIs()); 
      
  	importAxioms(); // Import axioms 
  }

  /**
   * Find all built-in atoms with unbound arguments and tell them which of their arguments are unbound.
   * See <a href="http://protege.cim3.net/cgi-bin/wiki.pl?SWRLBuiltInBridge#nid88T">here</a> for a discussion of the role of this method.
   */
  private void processUnboundBuiltInArguments(SWRLRule ruleOrQuery)
  {
    List<SWRLBuiltInAtom> bodyBuiltInAtoms = new ArrayList<SWRLBuiltInAtom>();
    List<SWRLAtom> bodyNonBuiltInAtoms = new ArrayList<SWRLAtom>();
    List<SWRLAtom> finalBodyAtoms = new ArrayList<SWRLAtom>();
    Set<String> variableNamesUsedByNonBuiltInBodyAtoms = new HashSet<String>(); // By definition, these will always be bound.
    Set<String> variableNamesBoundByBuiltIns = new HashSet<String>(); // Names of variables bound by built-ins in this rule
   
    // Process the body atoms and build up list of (1) built-in body atoms, and (2) the variables used by non-built body in atoms.
    for (SWRLAtom atom : ruleOrQuery.getBodyAtoms()) {
      if (atom instanceof SWRLBuiltInAtom) bodyBuiltInAtoms.add((SWRLBuiltInAtom)atom);
      else {
        bodyNonBuiltInAtoms.add(atom); variableNamesUsedByNonBuiltInBodyAtoms.addAll(atom.getReferencedVariableNames());
      } // if
    } // for

    // Process the body built-in atoms and determine if they bind any of their arguments.
    for (SWRLBuiltInAtom builtInAtom : bodyBuiltInAtoms) { // Read through built-in arguments and determine which are unbound.   	
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
  	
    for (SWRLAtom atom : ruleOrQuery.getBodyAtoms()) {
    	Set<String> thisAtomReferencedVariableNames = new HashSet<String>(atom.getReferencedVariableNames());

    	buildPaths(atom, rootVariableNames, pathMap);
    	
    	if (atom instanceof SWRLBuiltInAtom) {
    		SWRLBuiltInAtom builtInAtom = (SWRLBuiltInAtom)atom;
    		
    		if (builtInAtom.isSQWRLGroupCollection()) continue;
    		if (builtInAtom.isSQWRLCollectionOperation()) break;
    		
    		if (builtInAtom.hasReferencedVariables()) {
        	Set<String> pathVariableNames = new HashSet<String>();
        	
        	for (String rootVariableName : pathMap.keySet()) {
        		for (Set<String> path : pathMap.get(rootVariableName)) {
        			if (!Collections.disjoint(path, thisAtomReferencedVariableNames)) { 
        			  pathVariableNames.addAll(path);
        			  pathVariableNames.add(rootVariableName);
        		  } // if
        		} // for
        	} // for
      
        	if (!pathVariableNames.isEmpty()) {
          	pathVariableNames.removeAll(thisAtomReferencedVariableNames); // Remove our own variables
          	/* TODO: Need to think about correct operation of this
          	if (builtInAtom.isSQWRLMakeCollection()) {
          		String collectionName = builtInAtom.getArgumentVariableName(0); // First argument is the collection name
          		if (collectionGroupArgumentsMap.containsKey(collectionName)) {
          			List<BuiltInArgument> groupArguments = collectionGroupArgumentsMap.get(collectionName);
          			Set<String> groupVariableNames = getVariableNames(groupArguments);
          			if (!groupVariableNames.isEmpty() && !pathVariableNames.containsAll(groupVariableNames)) 
          				throw new BuiltInException("all group arguments must be on path for corresponding collection make");
          		} // if
          	} // if
          	 */
          	builtInAtom.setPathVariableNames(pathVariableNames);
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
  private void buildPaths(SWRLAtom atom, Set<String> rootVariableNames, Map<String, Set<Set<String>>> pathMap)
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
  private List<SWRLAtom> processBodyNonBuiltInAtoms(List<SWRLAtom> bodyNonBuiltInAtoms)
  {
    List<SWRLAtom> bodyClassAtoms = new ArrayList<SWRLAtom>(); 
    List<SWRLAtom> bodyNonClassNonBuiltInAtoms = new ArrayList<SWRLAtom>();
    List<SWRLAtom> result = new ArrayList<SWRLAtom>();

    for (SWRLAtom atom : bodyNonBuiltInAtoms) {
      if (atom instanceof SWRLClassAtom) bodyClassAtoms.add(atom);
      else bodyNonClassNonBuiltInAtoms.add(atom);
    } // for
    
    result.addAll(bodyClassAtoms); // We arrange the class atoms first.
    result.addAll(bodyNonClassNonBuiltInAtoms);
    
    return result;
  } 

  //TODO: too long- refactor
  private void processSQWRLHeadBuiltIns(SWRLRule query) throws DataValueConversionException, SQWRLException, BuiltInException
  {
     List<String> selectedVariableNames = new ArrayList<String>();
     SQWRLResultImpl sqwrlResult = sqwrlResultMap.get(query.getURI());

     processBuiltInIndexes(query);

     for (SWRLBuiltInAtom builtInAtom : getBuiltInAtomsFromHead(query, SQWRLNames.getHeadBuiltInNames())) {
       String builtInName = builtInAtom.getPredicate();
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
	           if (argument instanceof SWRLLiteralArgument && ((SWRLLiteralArgument)argument).getLiteral().isString()) {
	             SWRLLiteralArgument dataValueArgument = (SWRLLiteralArgument)argument;
	             sqwrlResult.addColumnDisplayName(dataValueArgument.getLiteral().getString());
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
      		 
        	 if (nArgument instanceof SWRLLiteralArgument && ((SWRLLiteralArgument)nArgument).getLiteral().isLong()) {
             n = (int)((SWRLLiteralArgument)nArgument).getLiteral().getLong();
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
	      		 
	        	 if (sliceArgument instanceof SWRLLiteralArgument && ((SWRLLiteralArgument)sliceArgument).getLiteral().isLong()) {
	             sliceSize = (int)((SWRLLiteralArgument)sliceArgument).getLiteral().getLong();
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
    for (SWRLBuiltInAtom builtInAtom : getBuiltInAtomsFromBody(query, SQWRLNames.getCollectionMakeBuiltInNames())) {
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
    for (SWRLBuiltInAtom builtInAtom : getBuiltInAtomsFromBody(ruleOrQuery, SQWRLNames.getCollectionGroupByBuiltInNames())) {
      String collectionName = builtInAtom.getArgumentVariableName(0); // The first argument is the collection name.
      List<BuiltInArgument> builtInArguments = builtInAtom.getArguments();
      List<BuiltInArgument> groupArguments = builtInArguments.subList(1, builtInArguments.size());
      String uri = ruleOrQuery.getURI();
      Map<String, List<BuiltInArgument>> collectionGroupArguments;

      hasSQWRLCollectionBuiltInsMap.put(uri, true);
    
      if (builtInAtom.getArguments().size() < 2) throw new SQWRLException("groupBy must have at least two arguments");
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
    for (SWRLBuiltInAtom builtInAtom : getBuiltInAtomsFromBody(ruleOrQuery, SQWRLNames.getCollectionMakeBuiltInNames())) {
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
  	for (SWRLBuiltInAtom builtInAtom : getBuiltInAtomsFromBody(ruleOrQuery, SQWRLNames.getCollectionOperationBuiltInNames())) {
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
    for (SWRLBuiltInAtom builtInAtom : getBuiltInAtomsFromBody(ruleOrQuery)) {
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
    
    for (SWRLAtom atom : ruleOrQuery.getBodyAtoms()) 
    	if (referencedVariableNameMap.containsKey(uri)) referencedVariableNameMap.get(uri).addAll(atom.getReferencedVariableNames());
    	else referencedVariableNameMap.put(uri, new HashSet<String>(atom.getReferencedVariableNames()));
  }

  /**
   * Give each built-in a unique index proceeding from left to right.
   */
  private void processBuiltInIndexes(SWRLRule ruleOrQuery)
  {
    int builtInIndex = 0;

    for (SWRLBuiltInAtom builtInAtom : getBuiltInAtomsFromBody(ruleOrQuery)) builtInAtom.setBuiltInIndex(builtInIndex++);
    for (SWRLBuiltInAtom builtInAtom : getBuiltInAtomsFromHead(ruleOrQuery)) builtInAtom.setBuiltInIndex(builtInIndex++);
  }
  
  private boolean hasUnboundArgument(List<BuiltInArgument> arguments)
  {
  	for (BuiltInArgument argument : arguments) if (argument.isUnbound()) return true;
  	return false;
  }
 
  private void processSWRLAtom(SWRLRule ruleOrQuery, SWRLAtom atom, boolean isConsequent)
  {
  	String uri = ruleOrQuery.getURI();
  	
    if (atom.hasReferencedClasses()) 
    	if (referencedOWLClassURIMap.containsKey(uri)) 
    		referencedOWLClassURIMap.get(uri).addAll(atom.getReferencedClassURIs());
    	else referencedOWLClassURIMap.put(uri, atom.getReferencedClassURIs());

    if (atom.hasReferencedProperties()) 
    	if (referencedOWLPropertyURIMap.containsKey(uri)) 
    		referencedOWLPropertyURIMap.get(uri).addAll(atom.getReferencedPropertyURIs());
    	else referencedOWLPropertyURIMap.put(uri, atom.getReferencedPropertyURIs());

    if (atom.hasReferencedIndividuals()) 
    	if (referencedOWLIndividualURIMap.containsKey(uri)) 
    		referencedOWLIndividualURIMap.get(uri).addAll(atom.getReferencedIndividualURIs());
    	else referencedOWLIndividualURIMap.put(uri, atom.getReferencedIndividualURIs());
  } 

  private List<SWRLBuiltInAtom> getBuiltInAtoms(List<SWRLAtom> atoms, Set<String> builtInNames) 
  {
    List<SWRLBuiltInAtom> result = new ArrayList<SWRLBuiltInAtom>();
    
    for (SWRLAtom atom : atoms) {
      if (atom instanceof SWRLBuiltInAtom) {
        SWRLBuiltInAtom builtInAtom = (SWRLBuiltInAtom)atom;
        if (builtInNames.contains(builtInAtom.getPredicate())) result.add(builtInAtom);
        } // if
    } // for
    return result;
  } 

  private List<SWRLBuiltInAtom> getBuiltInAtoms(List<SWRLAtom> atoms) 
  {
    List<SWRLBuiltInAtom> result = new ArrayList<SWRLBuiltInAtom>();
    
    for (SWRLAtom atom : atoms) if (atom instanceof SWRLBuiltInAtom) result.add((SWRLBuiltInAtom)atom);

    return result;
  }
  
  public List<SWRLBuiltInAtom> getBuiltInAtomsFromHead(SWRLRule ruleOrQuery) 
  { 
  	return getBuiltInAtoms(ruleOrQuery.getHeadAtoms()); 
  }
  
  public List<SWRLBuiltInAtom> getBuiltInAtomsFromHead(SWRLRule ruleOrQuery, Set<String> builtInNames) 
  { 
  	return getBuiltInAtoms(ruleOrQuery.getHeadAtoms(), builtInNames); 
  }

  public List<SWRLBuiltInAtom> getBuiltInAtomsFromBody(SWRLRule ruleOrQuery) 
  { 
  	return getBuiltInAtoms(ruleOrQuery.getBodyAtoms()); 
  }
  
  public List<SWRLBuiltInAtom> getBuiltInAtomsFromBody(SWRLRule ruleOrQuery, Set<String> builtInNames) 
  { 
  	return getBuiltInAtoms(ruleOrQuery.getBodyAtoms(), builtInNames); 
  }

  private void importOWLClassesByName(Set<String> classURIs) throws SWRLRuleEngineBridgeException
  {
    for (String classURI : classURIs) importOWLClass(classURI);
  }
  
  private void importOWLClasses(Set<OWLClass> classes) throws SWRLRuleEngineBridgeException
  {
    for (OWLClass owlClass : classes) importOWLClass(owlClass.getURI());
  }

  private void importOWLClass(String classURI) throws SWRLRuleEngineBridgeException
  {  
  	try {
	    if (activeOntology.couldBeOWLNamedClass(classURI)) {
	     OWLClass owlClass = activeOntology.getOWLClass(classURI);
	
	     if (!importedOWLClassDeclarations.containsKey(classURI)) {
	    	 importedOWLClassDeclarations.put(classURI, owlClass);
	    	 importOWLClasses(owlClass.getSuperClasses());
	    	 importOWLClasses(owlClass.getSubClasses());
	    	 importOWLClasses(owlClass.getEquivalentClasses());
	     } // if
	    } // if
  	} catch (OWLConversionFactoryException e) {
  	  throw new SWRLRuleEngineBridgeException("error importing owl class " + classURI + ": " + e.getMessage());
  	}
  }

  private void importAllOWLIndividualsOfClassesByName(Set<String> classURIs) throws SWRLRuleEngineBridgeException
  {
    for (String classURI : classURIs) importAllOWLIndividualsOfClass(classURI);
  }

  private void importAllOWLIndividualsOfClass(String classURI) throws SWRLRuleEngineBridgeException
  {
	 try {
		 for (OWLNamedIndividual individual : activeOntology.getAllOWLIndividualsOfClass(classURI)) {
			 importOWLIndividual(individual.getURI());
		 } // for
	 } catch (OWLConversionFactoryException e) {
		 throw new SWRLRuleEngineBridgeException("error importing OWL same individuals axioms: " + e);
	 } // try
  }
  
  private void importOWLPropertyAssertionAxiomsByName(Set<String> propertyURIs) throws SWRLRuleEngineBridgeException
  {
    for (String propertyURI : propertyURIs) importOWLPropertyAssertionAxioms(propertyURI);
  }

  private void importOWLPropertyAssertionAxioms(Set<OWLProperty> properties) throws SWRLRuleEngineBridgeException
  {
    for (OWLProperty property : properties) importOWLPropertyAssertionAxioms(property.getURI());
  }

  private void importOWLPropertyAssertionAxioms(String propertyURI) throws SWRLRuleEngineBridgeException
  {  	
	  if (!(importedOWLObjectPropertyURIs.contains(propertyURI) || importedOWLDataPropertyURIs.contains(propertyURI))) {
	  	Set<OWLPropertyAssertionAxiom> axioms = null;
	  	
	  	try {
			  if (!importedOWLPropertyDeclarations.containsKey(propertyURI)) {
			  	if (activeOntology.containsDataPropertyInSignature(propertyURI, true))
			  		importedOWLPropertyDeclarations.put(propertyURI, activeOntology.getOWLDataProperty(propertyURI));
			  	else if (activeOntology.containsObjectPropertyInSignature(propertyURI, true))
			  		importedOWLPropertyDeclarations.put(propertyURI, activeOntology.getOWLObjectProperty(propertyURI));
			  	else throw new SWRLRuleEngineBridgeException("referenced property " + propertyURI + " not in active ontology");
			  } // if

	  		axioms = activeOntology.getOWLPropertyAssertionAxioms(propertyURI);
	 	 	} catch (OWLConversionFactoryException e) {
	 	 		throw new SWRLBuiltInBridgeException("error importing OWL property assertion axiom for property " + propertyURI + " :" + e);
	 	 	} // try	
		  
		  importedOWLAxioms.addAll(axioms);
		          
		  for (OWLPropertyAssertionAxiom axiom : axioms) {
			  String subjectURI = axiom.getSubject().getURI();
			  OWLProperty property = axiom.getProperty();
			  			  
			  cacheOWLPropertyAssertionAxiom(axiom);
			  
			  addReferencedIndividualURI(subjectURI);

			  if (axiom instanceof OWLObjectPropertyAssertionAxiom) {
				  OWLObjectPropertyAssertionAxiom objectPropertyAssertionAxiom = (OWLObjectPropertyAssertionAxiom)axiom;
				  String objectURI = objectPropertyAssertionAxiom.getObject().getURI();
				  addReferencedIndividualURI(objectURI);
				  importedOWLObjectPropertyURIs.add(propertyURI);
			  } else importedOWLDataPropertyURIs.add(propertyURI);

			  importOWLClasses(property.getDomainClasses());
			  importOWLClasses(property.getRangeClasses());
        
			  importOWLPropertyAssertionAxioms(property.getSuperProperties());
			  importOWLPropertyAssertionAxioms(property.getEquivalentProperties());
		  } // for
	  } // if
  }

  private void cacheOWLPropertyAssertionAxiom(OWLPropertyAssertionAxiom axiom)
  {
    String subjectURI = axiom.getSubject().getURI();
    String propertyURI = axiom.getProperty().getURI();
    Map<String, Set<OWLPropertyAssertionAxiom>> propertyAxiomsMap;
    Set<OWLPropertyAssertionAxiom> axiomSet;

    if (allOWLPropertyAssertionAxioms.containsKey(subjectURI)) 
      propertyAxiomsMap = allOWLPropertyAssertionAxioms.get(subjectURI);
    else {
      propertyAxiomsMap = new HashMap<String, Set<OWLPropertyAssertionAxiom>>();
      allOWLPropertyAssertionAxioms.put(subjectURI, propertyAxiomsMap);
    } // if

    if (propertyAxiomsMap.containsKey(propertyURI)) axiomSet = propertyAxiomsMap.get(propertyURI);
    else {
      axiomSet = new HashSet<OWLPropertyAssertionAxiom>();
      propertyAxiomsMap.put(propertyURI, axiomSet);
    } // if
    
    axiomSet.add(axiom);     
  }

  private void cacheOWLIndividual(OWLNamedIndividual owlIndividual)
  {
    String individualURI = owlIndividual.getURI();

    if (!allOWLIndividuals.containsKey(individualURI)) allOWLIndividuals.put(individualURI, owlIndividual);
  }  

  private void cacheOWLIndividuals(Set<OWLNamedIndividual> individuals)
  {
    for (OWLNamedIndividual individual: individuals) cacheOWLIndividual(individual);
  }

  private void importOWLIndividualsByName(Set<String> individualURIs) throws SWRLRuleEngineBridgeException
  {
    for (String individualURI : individualURIs) importOWLIndividual(individualURI);
  }

  private void importOWLIndividual(String individualURI) throws SWRLRuleEngineBridgeException
  {
    if (!importedOWLIndividualDeclarations.containsKey(individualURI)) {
      OWLNamedIndividual owlIndividual = dataFactory.getOWLIndividual(individualURI);
      importedOWLIndividualDeclarations.put(individualURI, owlIndividual);
      cacheOWLIndividual(owlIndividual);
      importOWLClasses(owlIndividual.getTypes());
    } // if
  }

  private void importAxioms() throws SWRLRuleEngineBridgeException
  {
    importOWLClassDescriptions(); // cf. http://www.w3.org/TR/owl-ref, Section  3.1
    importOWLClassAxioms(); // cf. http://www.w3.org/TR/owl-ref, Section  3
    importOWLPropertyAxioms(); // cf. http://www.w3.org/TR/owl-ref, Section  4 
    importOWLIndividualAxioms(); // cf. http://www.w3.org/TR/owl-ref, Section  5
    importOWLDataValuedPropertyAxioms(); // cf. http://www.w3.org/TR/owl-ref, Section 6
    importOWLAnnotations(); // cf. http://www.w3.org/TR/owl-ref, Section 7
  }

  // cf. http://www.w3.org/TR/owl-ref, Section 3.2
  private void importOWLClassAxioms() throws SWRLRuleEngineBridgeException
  {
    // rdfs:subClassOf
    importEquivalentClassAxioms();
    importDisjointWithAxioms();
  } 

  // cf. http://www.w3.org/TR/owl-ref, Section 4
  private void importOWLPropertyAxioms() throws SWRLRuleEngineBridgeException
  {
    //importRDFSchemaPropertyAxioms() - rdfs:subPropertyOf, rdfs:domain, rdfs:range
    importEquivalentPropertyAxioms();
    importInverseOfAxioms();
    importFunctionalPropertyAxioms();
    importInverseFunctionalPropertyAxioms();
    importTransitivePropertyAxioms();
    importSymmetricPropertyAxioms();
  }

  // cf. http://www.w3.org/TR/owl-ref, Section 5
  private void importOWLIndividualAxioms() throws SWRLRuleEngineBridgeException
  {
    importOWLSameIndividualAxioms();
    importOWLDifferentIndividualsAxioms();
    importOWLAllDifferentsAxioms();
  }

  // cf. http://www.w3.org/TR/owl-ref, Section 3.1.1
  private void importClassEnumerationDescriptions() throws SWRLRuleEngineBridgeException {}

  // cf. http://www.w3.org/TR/owl-ref, Section 3.1.2
  private void importPropertyRestrictions() throws SWRLRuleEngineBridgeException
  {
    importOWLCardinalityRestrictions();
    importOWLMinCardinalityRestrictions();
    importOWLMaxCardinalityRestrictions();
    importOWLAllValuesFromRestrictions();
    importOWLSomeValuesFromRestrictions();
    importOWLHasValueRestrictions();
  }

  private void importOWLSameIndividualAxioms() throws SWRLRuleEngineBridgeException
  {
		try {
			for (OWLSameIndividualAxiom axiom : activeOntology.getSameIndividualAxioms()) {
				importedOWLAxioms.add(axiom);
				cacheOWLIndividuals(axiom.getIndividuals());
			} // for
		} catch (OWLConversionFactoryException e) {
			throw new SWRLBuiltInBridgeException("error importing OWL same individuals axioms: " + e);
		}
  }

  private void importOWLDifferentIndividualsAxioms() throws SWRLRuleEngineBridgeException
  {
		try {
			for (OWLDifferentIndividualsAxiom axiom : activeOntology.getOWLDifferentIndividualsAxioms()) {
				importedOWLAxioms.add(axiom);
				cacheOWLIndividuals(axiom.getIndividuals());
			} // for
		} catch (OWLConversionFactoryException e) {
			throw new SWRLBuiltInBridgeException("error importing OWL different individuals axioms: " + e);
		}
  } 

  private void importOWLAllDifferentsAxioms() throws SWRLRuleEngineBridgeException
  {
    // importOWLDifferentIndividualsAxioms effectively subsumes this method.
  } 
  
  // cf. http://www.w3.org/TR/owl-ref, Section 6
  private void importOWLDataValuedPropertyAxioms() throws SWRLRuleEngineBridgeException {}

  // cf. http://www.w3.org/TR/owl-ref, Section 7
  private void importOWLAnnotations() throws SWRLRuleEngineBridgeException
  {
    // owl:versionInfo, rdfs:label, rdfs:comment, rdfs:seeAlso, rdfs:isDefinedBy 
  }
  
  private void importOWLClassDescriptions() throws SWRLRuleEngineBridgeException
  {
    importClassEnumerationDescriptions(); // 3.1.1
    importPropertyRestrictions(); // 3.1.2
    importIntersectionOfDescriptions(); // 3.1.3.1
    importUnionOfDescriptions(); // 3.1.3.2
    importComplementOfDescriptions(); // 3.1.3.3
  }

  // cf. http://www.w3.org/TR/owl-ref, Section 3.1.2
  private void importOWLCardinalityRestrictions() throws SWRLRuleEngineBridgeException {}
  // cf. http://www.w3.org/TR/owl-ref, Section 3.1.2
  private void importOWLMinCardinalityRestrictions() throws SWRLRuleEngineBridgeException {}
  // cf. http://www.w3.org/TR/owl-ref, Section 3.1.2
  private void importOWLMaxCardinalityRestrictions() throws SWRLRuleEngineBridgeException {}
  // cf. http://www.w3.org/TR/owl-ref, Section 3.1.2.1.1
  private void importOWLAllValuesFromRestrictions() throws SWRLRuleEngineBridgeException {}
  // cf. http://www.w3.org/TR/owl-ref, Section 3.1.2.1.2
  private void importOWLSomeValuesFromRestrictions() throws SWRLRuleEngineBridgeException {}
  // cf. http://www.w3.org/TR/owl-ref, Section 3.1.2.1.3
  private void importOWLHasValueRestrictions() throws SWRLRuleEngineBridgeException {}

  // cf. http://www.w3.org/TR/owl-ref, Section 3.1.3.1
  private void importIntersectionOfDescriptions() throws SWRLRuleEngineBridgeException {}
  // cf. http://www.w3.org/TR/owl-ref, Section 3.1.3.2
  private void importUnionOfDescriptions() throws SWRLRuleEngineBridgeException {}
  // cf. http://www.w3.org/TR/owl-ref, Section 3.1.3.3
  private void importComplementOfDescriptions() throws SWRLRuleEngineBridgeException {}

  // cf. http://www.w3.org/TR/owl-ref, Section 4.2.1
  private void importEquivalentPropertyAxioms() throws SWRLRuleEngineBridgeException {}
  // cf. http://www.w3.org/TR/owl-ref, Section 4.2.2
  private void importInverseOfAxioms() throws SWRLRuleEngineBridgeException {}
  // cf. http://www.w3.org/TR/owl-ref, Section 4.3.1
  private void importFunctionalPropertyAxioms() throws SWRLRuleEngineBridgeException {}
  // cf. http://www.w3.org/TR/owl-ref, Section 4.3.2
  private void importInverseFunctionalPropertyAxioms() throws SWRLRuleEngineBridgeException {}
  // cf. http://www.w3.org/TR/owl-ref, Section 4.4.1
  private void importTransitivePropertyAxioms() throws SWRLRuleEngineBridgeException {}
  // cf. http://www.w3.org/TR/owl-ref, Section 4.4.2
  private void importSymmetricPropertyAxioms() throws SWRLRuleEngineBridgeException {}

  private void importEquivalentClassAxioms() throws SWRLRuleEngineBridgeException {}
  private void importDisjointWithAxioms() throws SWRLRuleEngineBridgeException {}
}