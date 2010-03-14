
// TODO: way too big - needs to be refactored
// TODO: DataRange
// TODO: remove all Protege-OWL specific code (primarily be removing all references to OWLConversionFactory).

package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.swrl.bridge.Atom;
import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLConversionFactory;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLDataValue;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLDataValueFactory;
import edu.stanford.smi.protegex.owl.swrl.bridge.RuleAndQueryProcessor;
import edu.stanford.smi.protegex.owl.swrl.bridge.SWRLBuiltInBridge;
import edu.stanford.smi.protegex.owl.swrl.bridge.SWRLRule;
import edu.stanford.smi.protegex.owl.swrl.bridge.SWRLRuleEngineBridge;
import edu.stanford.smi.protegex.owl.swrl.bridge.TargetSWRLRuleEngine;
import edu.stanford.smi.protegex.owl.swrl.bridge.builtins.BuiltInLibraryManager;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.BuiltInException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.DataValueConversionException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.OWLConversionFactoryException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.OWLFactoryException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.SWRLBuiltInBridgeException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.SWRLRuleEngineBridgeException;
import edu.stanford.smi.protegex.owl.swrl.exceptions.SWRLRuleEngineException;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLClass;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLClassAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLDataFactory;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLDataPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLDeclarationAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLDifferentIndividualsAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLEntity;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLIndividual;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLObjectPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLProperty;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLSameIndividualAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLSubClassAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.impl.OWLConversionFactoryImpl;
import edu.stanford.smi.protegex.owl.swrl.owlapi.impl.OWLDataFactoryImpl;
import edu.stanford.smi.protegex.owl.swrl.parser.SWRLParseException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.SQWRLResult;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.InvalidQueryNameException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.SQWRLException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.impl.SQWRLResultImpl;

/**
 ** This class provides an implementation of some of the core functionality required by SWRL rule engine and built-in bridges. Detailed
 ** documentation for these bridges can be found <a href="http://protege.cim3.net/cgi-bin/wiki.pl?SWRLRuleEngineBridgeFAQ">here</a>.
 */
public abstract class AbstractSWRLRuleEngineBridge implements SWRLRuleEngineBridge, SWRLBuiltInBridge, TargetSWRLRuleEngine
{
  protected OWLDataFactory activeOWLFactory, injectedOWLFactory;
  protected OWLDataValueFactory owlDataValueFactory;
  protected OWLConversionFactory conversionFactory;
  protected OWLModel owlModel; 

  private HashMap<String, SWRLRule> importedSWRLRules; 

  // Imported classes, properties, and individuals
  private HashMap<String, OWLClass> importedOWLClasses;
  private HashMap<String, OWLIndividual> importedOWLIndividuals;
  private Set<String> importedOWLObjectPropertyURIs, importedOWLDataPropertyURIs;
  private Set<OWLAxiom> importedOWLAxioms;

  // Inferred individuals and property assertion axioms
  private Map<String, OWLIndividual> inferredOWLIndividuals;
  private Set<OWLAxiom> inferredOWLAxioms; 

  // Injected entities
  private HashMap<String, OWLClass> injectedOWLClasses;
  private HashMap<String, OWLIndividual> injectedOWLIndividuals;
  private Set<OWLAxiom> injectedOWLAxioms;

  // All entities
  private Map<String, Map<String, Set<OWLPropertyAssertionAxiom>>> allOWLPropertyAssertionAxioms; // individualURI <propertyURI, axiom>
  private Map<String, OWLIndividual> allOWLIndividuals; 

  // URIs of classes, properties, and individuals that have been exported to target rule engine
  private Set<String> exportedOWLClassURIs, exportedOWLIndividualURIs; 

  // URIs of classes, properties and individuals explicitly referred to in SWRL rules. These are filled in as the SWRL rules are imported
  // and are used to determine the relevant OWL knowledge to import.
  private Set<String> referencedOWLClassURIs, referencedOWLPropertyURIs, referencedOWLIndividualURIs;
  
  private RuleAndQueryProcessor ruleAndQueryProcessor;

  protected AbstractSWRLRuleEngineBridge(OWLModel owlModel) throws SWRLRuleEngineBridgeException
  {
    this.owlModel = owlModel;
    activeOWLFactory = new OWLDataFactoryImpl(owlModel);
    injectedOWLFactory = new OWLDataFactoryImpl();
    conversionFactory = new OWLConversionFactoryImpl(owlModel, activeOWLFactory);
    owlDataValueFactory = OWLDataValueFactory.create();
    ruleAndQueryProcessor = new RuleAndQueryProcessorImpl();
    initialize();
    BuiltInLibraryManager.invokeAllBuiltInLibrariesResetMethod(this);
  }

  /**
   ** Load rules and knowledge from OWL into bridge. All existing bridge rules and knowledge will first be cleared and the associated rule
   ** engine will be reset.
   */
  public void importSWRLRulesAndOWLKnowledge() throws SWRLRuleEngineBridgeException
  {
    importSWRLRulesAndOWLKnowledge(new HashSet<String>());
  } 

  /**
   * Load rules from a particular rule group and associated knowledge from OWL into bridge. All existing bridge rules and knowledge will
   * first be cleared and the associated rule engine will be reset.
   */
  public void importSWRLRulesAndOWLKnowledge(String ruleGroupName) throws SWRLRuleEngineException
  {
    Set<String> ruleGroupNames = new HashSet<String>();
    ruleGroupNames.add(ruleGroupName);
    importSWRLRulesAndOWLKnowledge(ruleGroupNames);
  } // importSWRLRulesAndOWLKnowledge

  /**
   * Load rules from all the named rule groups and associated knowledge from OWL into bridge. All existing bridge rules and knowledge will
   * first be cleared and the associated rule engine will be reset.
   */
  public void importSWRLRulesAndOWLKnowledge(Set<String> ruleGroupNames) throws SWRLRuleEngineBridgeException
  {
    try {
      importSWRLRules(false); // Fills in referencedClassURIs, referencedIndividualURIs, referencedPropertyURIs
      importReferencedOWLKnowledge();
      exportSWRLRulesAndOWLKnowledge();
    } catch (OWLConversionFactoryException e) {
      throw new SWRLRuleEngineBridgeException("error importing SWRL rules and OWL knowledge: " + e.getMessage());
    } // try

  } // importSWRLRulesAndOWLKnowledge
  
  public String uri2PrefixedName(String uri)
  {
  	return conversionFactory.uri2PrefixedName(uri);
  }
  
  public String name2URI(String prefixedName)
  {
  	return conversionFactory.prefixedName2URI(prefixedName);
  }

  private void importReferencedOWLKnowledge() throws SWRLRuleEngineBridgeException
  {
    try {
      importOWLClassesByName(referencedOWLClassURIs); // Import all (directly or indirectly) referenced classes.
      importOWLPropertyAssertionAxiomsByName(referencedOWLPropertyURIs); // Import property assertion axioms for (directly or indirectly) referenced properties
      importOWLIndividualsByName(referencedOWLIndividualURIs); // Import all directly referenced individuals.
      importAllOWLIndividualsOfClassesByName(referencedOWLClassURIs); // Import all individuals that are members of imported classes.
      importAxioms(); // Import some other axioms (only owl:sameAs, owl:differentFrom, and owl:allDifferent for the moment).
    } catch (OWLConversionFactoryException e) {
      throw new SWRLRuleEngineBridgeException("error importing SWRL rules and OWL knowledge: " + e.getMessage());
    } // try

  } // importReferencedOWLKnowledge

  private void importSWRLRules(boolean allowSQWRLQueries) throws SWRLRuleEngineBridgeException
  {
    try {
      for (SWRLRule rule : activeOWLFactory.getSWRLRules()) importSWRLRule(rule);    	  
    } catch (OWLFactoryException e) {
      throw new SWRLRuleEngineBridgeException("factory error importing rules: " + e.getMessage());
    } // try
  }

  private void importSQWRLQuery(String queryName) throws SWRLRuleEngineBridgeException
  {
    try {
      SWRLRule rule = activeOWLFactory.getSWRLRule(queryName);
      if (rule.isSQWRL()) importSWRLRule(rule);
    } catch (OWLFactoryException e) {
       throw new SWRLRuleEngineBridgeException("factory error importing SQWRL query " + queryName + ": " + e.getMessage());
    } // try
  } // importSWRLRules

  private void importSWRLRule(SWRLRule rule) throws SWRLRuleEngineBridgeException
  {
  	try {
      importedSWRLRules.put(rule.getURI(), rule);
        
      for (Atom atom : rule.getBodyAtoms()) processSWRLAtom(atom, false);
      for (Atom atom : rule.getHeadAtoms()) processSWRLAtom(atom, true);
    } catch (SQWRLException e) {
      throw new SWRLRuleEngineBridgeException("SQWRL error importing rules: " + e.getMessage());
    } catch (BuiltInException e) {
      throw new SWRLRuleEngineBridgeException("built-in error importing rules: " + e.getMessage());
    } // try
  } // importSWRLRule

  private void processSWRLAtom(Atom atom, boolean isConsequent) throws SWRLRuleEngineBridgeException
  {
    if (atom.hasReferencedClasses()) referencedOWLClassURIs.addAll(atom.getReferencedClassURIs());
    if (atom.hasReferencedProperties()) referencedOWLPropertyURIs.addAll(atom.getReferencedPropertyURIs());
    if (atom.hasReferencedIndividuals()) referencedOWLIndividualURIs.addAll(atom.getReferencedIndividualURIs());
  } // processSWRLAtom

  /**
   ** Send rules and knowledge stored in bridge to a rule engine.
   */
  private void exportSWRLRulesAndOWLKnowledge() throws SWRLRuleEngineBridgeException, OWLConversionFactoryException
  {
    exportClasses(); // Classes should be exported before rules because rules usually use class definitions.
    exportIndividuals();
    exportAxioms();
    exportSWRLRules();
  } // exportSWRLRulesAndOWLKnowledge

   /**
   ** Run the rule engine.
   */
  public void run() throws SWRLRuleEngineBridgeException
  {
    runRuleEngine();
  } // run

  /**
   ** Write knowledge inferred by rule engine back to OWL.
   */
  public void writeInferredKnowledge2OWL() throws SWRLRuleEngineBridgeException
  {
    try {
      // Order of creation is important here.
      writeInjectedClasses(); // Create any OWL classes generated by built-ins in rules. 
      writeInjectedIndividuals(); // Create any OWL individuals generated by built-ins in rules. 
      writeInjectedAxioms(); // Create any OWL axioms generated by built-ins in rules. 
      
      writeInferredIndividuals2OWL();
      writeInferredAxioms2OWL();
    } catch (OWLConversionFactoryException e) {
      throw new SWRLRuleEngineBridgeException("error writing inferred knowledge to OWL: " + e.getMessage());
    } // try
  } // writeInferredKnowledge2OWL

  /**
   ** Clear all knowledge from bridge.
   */
  public void reset() throws SWRLRuleEngineBridgeException
  {
    resetRuleEngine(); // Reset the underlying rule engine

    BuiltInLibraryManager.invokeAllBuiltInLibrariesResetMethod(this);

    initialize();
  } // resetBridge

  /**
   ** Load rules and knowledge from OWL into bridge, send them to a rule engine, run the rule engine, and write any inferred knowledge back
   ** to OWL.
   */
  public void infer(Set<String> ruleGroupNames) throws SWRLRuleEngineBridgeException
  {
    reset();
    importSWRLRulesAndOWLKnowledge(ruleGroupNames);
    run();
    writeInferredKnowledge2OWL();
  } // infer

  public void infer(String ruleGroupName) throws SWRLRuleEngineBridgeException
  {
    Set<String> ruleGroupNames = new HashSet<String>();
    ruleGroupNames.add(ruleGroupName);
    infer(ruleGroupNames);
  } // infer

  public void infer() throws SWRLRuleEngineBridgeException
  {
    Set<String> ruleGroupNames = new HashSet<String>();
    infer(ruleGroupNames);
  } // infer
  
  public SQWRLResult runSQWRLQuery(String queryName, String queryText) throws SQWRLException, SWRLParseException
  { 
  	createSQWRLQuery(queryName, queryText);
  	return runSQWRLQuery(queryName);
  } // runSQWRLQuery
  
  public void createSQWRLQuery(String queryName, String queryText) throws SQWRLException, SWRLParseException
  {
  	try {
  		conversionFactory.createSWRLRule(queryName, queryText);
  	} catch (OWLConversionFactoryException e) {
  	  throw new SQWRLException("error creating SQWRL query: " + e.getMessage());
  	} catch (BuiltInException e) {
  	  throw new SQWRLException("error creating SQWRL query: " + e.getMessage());
  	} // try
  }

  public SQWRLResult runSQWRLQuery(String queryName) throws SQWRLException
  {
    SQWRLResult result = null;
  	
    try {
      reset();
      importSQWRLQuery(queryName);
      importSWRLRules(false); // Fills in referencedClassURIs, referencedIndividualURIs, referencedPropertyURIs.
      importReferencedOWLKnowledge();
      exportSWRLRulesAndOWLKnowledge();
      run();
      result = getSQWRLResult(queryName);
    } catch (OWLConversionFactoryException e) {
      throw new SQWRLException("conversion error running SQWRL query: " + e.getMessage());
    } catch (SWRLRuleEngineBridgeException e) {
      throw new SQWRLException("error running SQWRL queries: " + e.getMessage());
    } // try
    
    return result;
  } // runSQWRLQuery

  public void runSQWRLQueries() throws SQWRLException
  {
    try {
      reset();
      importSWRLRules(true); // Fills in referencedClassURIs, referencedIndividualURIs, referencedPropertyURIs
      importReferencedOWLKnowledge();
      exportSWRLRulesAndOWLKnowledge();
      run();
    } catch (OWLConversionFactoryException e) {
      throw new SQWRLException("conversion error running SQWRL queries: " + e.getMessage());
    } catch (SWRLRuleEngineBridgeException e) {
      throw new SQWRLException("error running SQWRL queries: " + e.getMessage());
    } // try
  } // runSQWRLQueries

  /**
   *  Get the results from a SQWRL query.
   */
  public SQWRLResult getSQWRLResult(String queryURI) throws SQWRLException
  {
    SQWRLResultImpl result;

    if (!importedSWRLRules.containsKey(queryURI)) throw new InvalidQueryNameException(queryURI);

    result = importedSWRLRules.get(queryURI).getSQWRLResult();

    if (!result.isPrepared()) result.prepared();

    return result;
  } // getSQWRLResult

  public SWRLRule getSWRLRule(String ruleURI) throws SWRLBuiltInBridgeException
  {
    if (!importedSWRLRules.containsKey(ruleURI)) throw new SWRLBuiltInBridgeException("invalid rule name: " + ruleURI);

    return importedSWRLRules.get(ruleURI);
  } // getRule

  public OWLDataFactory getOWLDataFactory() { return activeOWLFactory; }
  public OWLDataValueFactory getOWLDataValueFactory() { return owlDataValueFactory; }
  public OWLModel getOWLModel() { return owlModel; } // TODO: Protege  dependency - remove

  // Convenience methods to display bridge activity
  public int getNumberOfImportedSWRLRules() { return importedSWRLRules.size(); }
  public int getNumberOfImportedClasses() { return importedOWLClasses.size(); }
  public int getNumberOfImportedIndividuals() { return importedOWLIndividuals.size(); }
  public int getNumberOfImportedAxioms() { return importedOWLAxioms.size(); }
  public int getNumberOfInferredIndividuals() { return inferredOWLIndividuals.keySet().size(); }
  public int getNumberOfInferredAxioms() { return inferredOWLAxioms.size(); }
  public int getNumberOfInjectedClasses() { return injectedOWLClasses.size(); }
  public int getNumberOfInjectedIndividuals() { return injectedOWLIndividuals.size(); }
  public int getNumberOfInjectedAxioms() { return injectedOWLAxioms.size(); }

    public Set<OWLIndividual> getOWLIndividuals() { return new HashSet<OWLIndividual>(allOWLIndividuals.values()); }

  public boolean isInjectedOWLClass(String classURI) { return injectedOWLClasses.containsKey(classURI); }
  public boolean isInjectedOWLIndividual(String individualURI) { return injectedOWLIndividuals.containsKey(individualURI); }
  public boolean isInjectedOWLAxiom(OWLAxiom axiom) { return injectedOWLAxioms.contains(axiom); }

  // Convenience methods to display the contents of the bridge
  public Set<SWRLRule> getImportedSWRLRules() { return new HashSet<SWRLRule>(importedSWRLRules.values()); }

  public Set<OWLClass> getImportedClasses() { return new HashSet<OWLClass>(importedOWLClasses.values()); }
  public Set<OWLIndividual> getImportedIndividuals() { return new HashSet<OWLIndividual>(importedOWLIndividuals.values()); }
  public Set<OWLAxiom> getImportedAxioms() { return importedOWLAxioms; }

  public Set<OWLIndividual> getInferredIndividuals() { return new HashSet<OWLIndividual>(inferredOWLIndividuals.values()); }
  public Set<OWLAxiom> getInferredAxioms() { return inferredOWLAxioms; }

  public Set<OWLClass> getInjectedClasses() { return new HashSet<OWLClass>(injectedOWLClasses.values()); }
  public Set<OWLIndividual> getInjectedIndividuals() { return new HashSet<OWLIndividual>(injectedOWLIndividuals.values()); }
  public Set<OWLAxiom> getInjectedAxioms() { return injectedOWLAxioms; }

  /**
   * Called by a rule engine to infer an OWL axiom into the bridge
   */
  public void inferOWLAxiom(OWLAxiom axiom) throws SWRLRuleEngineBridgeException
  { 
    if (!inferredOWLAxioms.contains(axiom)) {
      inferredOWLAxioms.add(axiom); 
      if (axiom instanceof OWLPropertyAssertionAxiom) cacheOWLPropertyAssertionAxiom((OWLPropertyAssertionAxiom)axiom);
      else if (axiom instanceof OWLClassAssertionAxiom) {
        OWLClassAssertionAxiom owlClassAssertionAxiom = (OWLClassAssertionAxiom)axiom;
        inferOWLIndividual(owlClassAssertionAxiom.getIndividual(), owlClassAssertionAxiom.getDescription());
      } // if
    } // if
  } // inferOWLAxiom

  private void inferOWLIndividual(OWLIndividual owlIndividual, OWLClass owlClass) throws SWRLRuleEngineBridgeException 
  {
    String individualURI = owlIndividual.getURI();

    if (inferredOWLIndividuals.containsKey(individualURI)) inferredOWLIndividuals.get(individualURI).addType(owlClass);
    else if (injectedOWLIndividuals.containsKey(individualURI)) injectedOWLIndividuals.get(individualURI).addType(owlClass);
    else {
      inferredOWLIndividuals.put(individualURI, owlIndividual); 
      cacheOWLIndividual(owlIndividual);
    } // if
  } // inferOWLIndividual

  /**
   * Invoke a SWRL built-in from a rule engine. <p>
   *
   * See <a href="http://protege.cim3.net/cgi-bin/wiki.pl?SWRLRuleEngineBridgeFAQ#nid6QF">here</a> for documentation.
   */
  public boolean invokeSWRLBuiltIn(String ruleName, String builtInURI, int builtInIndex, boolean isInConsequent, List<BuiltInArgument> arguments)
    throws BuiltInException
  {
    return BuiltInLibraryManager.invokeSWRLBuiltIn(this, this, ruleName, builtInURI, builtInIndex, isInConsequent, arguments);    
  } // invokeSWRLBuiltIn

  private void injectOWLDeclarationAxiom(OWLDeclarationAxiom axiom) throws SWRLBuiltInBridgeException
  {
    OWLEntity owlEntity = axiom.getEntity();

    if (owlEntity instanceof OWLClass) injectOWLClass(owlEntity.getURI());
    else if (owlEntity instanceof OWLIndividual) injectOWLIndividual((OWLIndividual)owlEntity);
    else throw new SWRLBuiltInBridgeException("error injecting OWLDeclarationAxiom - unknown entity type: " + owlEntity.getClass());
  } // injectOWLDeclarationAxiom

  public OWLClass injectOWLClass() throws SWRLBuiltInBridgeException
  {
    OWLClass owlClass = injectedOWLFactory.getOWLClass();
    String classURI = owlClass.getURI();

    if (!injectedOWLClasses.containsKey(classURI)) {
      injectedOWLClasses.put(classURI, owlClass);
      exportOWLClass(owlClass); // Export the class to the rule engine
    } // if

    return owlClass;
  } // injectOWLAnonymousClass

  public void injectOWLClass(String classURI) throws SWRLBuiltInBridgeException
  {
    checkOWLClassURI(classURI);

    if (!injectedOWLClasses.containsKey(classURI)) {
      OWLClass owlClass = injectedOWLFactory.getOWLClass(classURI);
      injectedOWLClasses.put(classURI, owlClass);
      exportOWLClass(owlClass); // Export the individual to the rule engine
    } // if
  } // injectOWLClass

  private void injectOWLSubClassAxiom(OWLSubClassAxiom axiom) throws SWRLBuiltInBridgeException
  {
    String subclassURI = axiom.getSubClass().getURI();
    String superclassURI = axiom.getSuperClass().getURI();
    injectedOWLFactory.getOWLClass(subclassURI);
    injectedOWLFactory.getOWLClass(superclassURI);

    injectedOWLClasses.put(subclassURI, axiom.getSubClass());
    injectedOWLClasses.put(superclassURI, axiom.getSuperClass());
  } // injectOWLSubClassAxiom

  /**
   * Method used to inject a bridge individual of type owl:Thing. This method will typically be invoked from within a built-in. An OWL
   * individual is not injected at this point - instead an object is generated for the individual in the bridge and the individual is
   * exported to the rule engine. The individual is given a unique name that can be used later if an OWL individual is injected for it.
   */
  public OWLIndividual injectOWLIndividual() throws SWRLBuiltInBridgeException
  {
    String individualURI = conversionFactory.createNewResourceName("SWRLInjected");
    OWLClass owlClass = injectedOWLFactory.getOWLClass(edu.stanford.smi.protegex.owl.model.OWLNames.Cls.THING);
    OWLIndividual owlIndividual = injectedOWLFactory.getOWLIndividual(individualURI);
    owlIndividual.addType(owlClass);
    injectOWLIndividual(owlIndividual);
    return owlIndividual;
  } // injectOWLIndividual
    
  public void injectOWLIndividual(OWLIndividual owlIndividual) throws SWRLBuiltInBridgeException
  {
    if (!injectedOWLIndividuals.containsKey(owlIndividual.getURI())) {
      injectedOWLIndividuals.put(owlIndividual.getURI(), owlIndividual); 
      cacheOWLIndividual(owlIndividual);
      exportOWLIndividual(owlIndividual); // Export the individual to the rule engine.
    } // if
  } // injectOWLIndividual

  public OWLIndividual injectOWLIndividualOfClass(OWLClass owlClass) throws SWRLBuiltInBridgeException
  {
    String individualURI = conversionFactory.createNewResourceName("SWRLInjected");
    OWLIndividual owlIndividual = injectedOWLFactory.getOWLIndividual(individualURI);
    owlIndividual.addType(owlClass);

    if (!importedOWLClasses.containsKey(owlClass.getURI())) exportOWLClass(owlClass);
   
    injectedOWLIndividuals.put(individualURI, owlIndividual); 
    cacheOWLIndividual(owlIndividual);
    exportOWLIndividual(owlIndividual); // Export the individual to the rule engine.

    return owlIndividual;
  } // injectOWLIndividualOfClass
  
  public OWLDataPropertyAssertionAxiom injectOWLDataPropertyAssertionAxiom(OWLIndividual subject, OWLProperty property,
                                                                           OWLDataValue object) 
    throws SWRLBuiltInBridgeException
  {
    OWLDataPropertyAssertionAxiom axiom = injectedOWLFactory.getOWLDataPropertyAssertionAxiom(subject, property, object);
    injectOWLDatatypePropertyAssertionAxiom(axiom);
    return axiom;
  } // injectOWLDataPropertyAssertionAxiom

  public void injectOWLDatatypePropertyAssertionAxiom(OWLDataPropertyAssertionAxiom axiom) 
    throws SWRLBuiltInBridgeException
  {
    if (!injectedOWLAxioms.contains(axiom)) {
      injectedOWLAxioms.add(axiom);
      cacheOWLPropertyAssertionAxiom(axiom);
    } // if

    exportOWLAxiom(axiom); // Export the axiom to the rule engine.
  } // injectOWLDatatypePropertyAssertionAxiom

  public void injectOWLDataPropertyAssertionAxioms(Set<OWLDataPropertyAssertionAxiom> axioms)
    throws SWRLBuiltInBridgeException
  {
    for (OWLDataPropertyAssertionAxiom axiom : axioms) injectOWLDatatypePropertyAssertionAxiom(axiom);
  } // injectOWLDataPropertyAssertionAxioms

  public void injectOWLObjectPropertyAssertionAxiom(OWLObjectPropertyAssertionAxiom axiom)
    throws SWRLBuiltInBridgeException
  {
    if (!injectedOWLAxioms.contains(axiom)) {
      injectedOWLAxioms.add(axiom);
      cacheOWLPropertyAssertionAxiom(axiom);
    } // if

    exportOWLAxiom(axiom); // Export the axiom to the rule engine.
  } // injectOWLObjectPropertyAssertionAxiom

  public void injectOWLAxiom(OWLAxiom axiom) throws SWRLBuiltInBridgeException
  {
    if (!injectedOWLAxioms.contains(axiom)) {
      injectedOWLAxioms.add(axiom);

      if (axiom instanceof OWLPropertyAssertionAxiom) cacheOWLPropertyAssertionAxiom((OWLPropertyAssertionAxiom)axiom);
      else if (axiom instanceof OWLDeclarationAxiom) injectOWLDeclarationAxiom((OWLDeclarationAxiom)axiom);
      else if (axiom instanceof OWLSubClassAxiom) injectOWLSubClassAxiom((OWLSubClassAxiom)axiom);

      exportOWLAxiom(axiom); // Export the axiom to the rule engine.
    } // if
  } // injectOWLAxiom
  
  public boolean isOWLClass(String classURI) 
  { 
	 return importedOWLClasses.containsKey(classURI) || injectedOWLClasses.containsKey(classURI) ||
	        conversionFactory.containsClassReference(classURI);
  } // isOWLClass
  
  public boolean isOWLObjectProperty(String propertyURI) 
  { 
	  return importedOWLObjectPropertyURIs.contains(propertyURI) || conversionFactory.containsObjectPropertyReference(propertyURI);
  } // isOWLObjectProperty
  
  public boolean isOWLDataProperty(String propertyURI) 
  { 
	  return importedOWLDataPropertyURIs.contains(propertyURI) || conversionFactory.containsDataPropertyReference(propertyURI);
  } // isOWLDataProperty

  public boolean isOWLIndividual(String individualURI) 
  { 
	  return allOWLIndividuals.containsKey(individualURI) || conversionFactory.containsIndividualReference(individualURI);
  } // isOWLIndividual
  
  public boolean isOWLIndividualOfClass(String individualURI, String classURI)
  {
    boolean result = false; 
    
    if (allOWLIndividuals.containsKey(individualURI)) {
      OWLIndividual owlIndividual = allOWLIndividuals.get(individualURI);
      result = owlIndividual.hasType(classURI);
    } // if

    if (!result) result = conversionFactory.isOWLIndividualOfClass(individualURI, classURI);

    return result;
  } // isOWLIndividualOfClass

  public Set<OWLPropertyAssertionAxiom> getOWLPropertyAssertionAxioms(String individualURI, String propertyURI) 
    throws SWRLBuiltInBridgeException
  {
    if (allOWLPropertyAssertionAxioms.containsKey(individualURI)) {
      Map<String, Set<OWLPropertyAssertionAxiom>> propertyAxiomsMap = allOWLPropertyAssertionAxioms.get(individualURI);
      if (propertyAxiomsMap.containsKey(propertyURI)) return propertyAxiomsMap.get(propertyURI);
      else return new HashSet<OWLPropertyAssertionAxiom>();
    } else {
    	Set<OWLPropertyAssertionAxiom> result = null;
    	try {
    	  result = conversionFactory.getOWLPropertyAssertionAxioms(individualURI, propertyURI);
    	} catch (DataValueConversionException e) {
      	  throw new SWRLBuiltInBridgeException("error getting property assertion axiom for individual " + individualURI + 
      			                                   ", property " + propertyURI + ": " + e.getMessage(), e); 
        } catch (OWLConversionFactoryException e) {
      	  throw new SWRLBuiltInBridgeException("error getting property assertion axiom for individual " + individualURI + 
     			                                     ", property " + propertyURI + ": " + e.getMessage(), e);
        } // try
        return result;
    } // if
  } // getOWLPropertyAssertionAxioms

  /**
   ** Create OWL classes in model for the classes injected by built-ins during rule execution.
   */
  private void writeInjectedClasses() throws SWRLRuleEngineBridgeException, OWLConversionFactoryException
  {
    for (OWLClass owlClass: injectedOWLClasses.values()) conversionFactory.putOWLClass(owlClass);   
  } // writeInjectedClasses

  /**
   ** Create OWL individuals in model for the individuals injected by built-ins during rule execution.
   */
  private void writeInjectedIndividuals() throws SWRLRuleEngineBridgeException, OWLConversionFactoryException
  {
    for (OWLIndividual owlIndividual: injectedOWLIndividuals.values()) conversionFactory.putOWLIndividual(owlIndividual);
  } // writeInjectedIndividuals

  /**
   ** Create OWL axioms in model for the axioms injected by built-ins during rule execution.
   */
  private void writeInjectedAxioms() throws SWRLRuleEngineBridgeException, OWLConversionFactoryException
  {
    for (OWLAxiom axiom : injectedOWLAxioms) conversionFactory.putOWLAxiom(axiom);
  } // writeInjectedAxioms
  
  private void importOWLClass(String classURI) throws SWRLRuleEngineBridgeException, OWLConversionFactoryException
  {  
    if (conversionFactory.couldBeOWLNamedClass(classURI)) {
     OWLClass owlClass = conversionFactory.getOWLClass(classURI);

     if (!importedOWLClasses.containsKey(classURI)) {
    	 importedOWLClasses.put(classURI, owlClass);
    	 importOWLClasses(owlClass.getSuperClasses());
    	 importOWLClasses(owlClass.getSubClasses());
    	 importOWLClasses(owlClass.getEquivalentClasses());
     } // if
    } // if
  } // importOWLClass

  private void importOWLClassesByName(Set<String> classURIs) throws SWRLRuleEngineBridgeException, OWLConversionFactoryException
  {
    for (String classURI : classURIs) importOWLClass(classURI);
  } // importOWLClassesByName
  
  private void importOWLClasses(Set<OWLClass> classes) throws SWRLRuleEngineBridgeException, OWLConversionFactoryException
  {
    for (OWLClass owlClass : classes) importOWLClass(owlClass.getURI());
  } // importOWLClasses

  private void importAllOWLIndividualsOfClassesByName(Set<String> classURIs) throws SWRLRuleEngineBridgeException, OWLConversionFactoryException
  {
    for (String classURI : classURIs) importAllOWLIndividualsOfClass(classURI);
  } // importAllOWLIndividualsOfClasses

  private void importAllOWLIndividualsOfClass(String classURI) throws SWRLRuleEngineBridgeException, OWLConversionFactoryException
  {
	 try {
		 for (OWLIndividual individual : conversionFactory.getAllOWLIndividualsOfClass(classURI)) {
			 importOWLIndividual(individual.getURI());
		 } // for
	 } catch (OWLConversionFactoryException e) {
		 throw new SWRLBuiltInBridgeException("error importing OWL same individuals axioms: " + e);
	}
  } // importAllOWLIndividualsOfClass
  
  private void importOWLPropertyAssertionAxiomsByName(Set<String> propertyURIs) throws SWRLRuleEngineBridgeException, OWLConversionFactoryException
  {
    for (String propertyURI : propertyURIs) importOWLPropertyAssertionAxioms(propertyURI);
  } // importOWLPropertyAssertionAxioms

  private void importOWLPropertyAssertionAxioms(Set<OWLProperty> properties) throws SWRLRuleEngineBridgeException, OWLConversionFactoryException
  {
    for (OWLProperty property : properties) importOWLPropertyAssertionAxioms(property.getURI());
  } // importOWLPropertyAssertionAxioms

  private void importOWLPropertyAssertionAxioms(String propertyURI) throws SWRLRuleEngineBridgeException, OWLConversionFactoryException
  {  	
	  if (!(importedOWLObjectPropertyURIs.contains(propertyURI) || importedOWLDataPropertyURIs.contains(propertyURI))) {
		  Set<OWLPropertyAssertionAxiom> axioms = conversionFactory.getOWLPropertyAssertionAxioms(propertyURI);
		  
		  importedOWLAxioms.addAll(axioms);
        
		  for (OWLPropertyAssertionAxiom axiom : axioms) {
			  String subjectURI = axiom.getSubject().getURI();
			  OWLProperty property = axiom.getProperty();
			  
			  cacheOWLPropertyAssertionAxiom(axiom);
			  
			  if (!referencedOWLIndividualURIs.contains(subjectURI)) referencedOWLIndividualURIs.add(subjectURI);

			  if (axiom instanceof OWLObjectPropertyAssertionAxiom) {
				  OWLObjectPropertyAssertionAxiom objectPropertyAssertionAxiom = (OWLObjectPropertyAssertionAxiom)axiom;
				  String objectURI = objectPropertyAssertionAxiom.getObject().getURI();
				  if (!referencedOWLIndividualURIs.contains(objectURI)) referencedOWLIndividualURIs.add(objectURI);
				  importedOWLObjectPropertyURIs.add(propertyURI);
			  } else importedOWLDataPropertyURIs.add(propertyURI);

			  importOWLClasses(property.getDomainClasses());
			  importOWLClasses(property.getRangeClasses());
        
			  importOWLPropertyAssertionAxioms(property.getSuperProperties());
			  importOWLPropertyAssertionAxioms(property.getSuperProperties());
			  importOWLPropertyAssertionAxioms(property.getEquivalentProperties());
		  } // for
      	} // if
  } // importOWLPropertyAssertionAxioms

  private void importOWLIndividualsByName(Set<String> individualURIs) throws SWRLRuleEngineBridgeException, OWLConversionFactoryException
  {
    for (String individualURI : individualURIs) importOWLIndividual(individualURI);
  } // importOWLIndividuals

  private void importOWLIndividual(String individualURI) throws SWRLRuleEngineBridgeException, OWLConversionFactoryException
  {
    if (!importedOWLIndividuals.containsKey(individualURI)) {
      OWLIndividual owlIndividual = activeOWLFactory.getOWLIndividual(individualURI);
      importedOWLIndividuals.put(individualURI, owlIndividual);
      cacheOWLIndividual(owlIndividual);
      importOWLClasses(owlIndividual.getTypes());
    } // if
  } // importOWLIndividual

  // We only import rdfs:subClassOf, rdfs:subPropertyOf, owl:sameAs, owl:differentFrom, and owl:allDifferent, owl:equivalentProperty, and
  // owl:equivalentClass axioms at the moment. We support owl:equivalentProperty and owl:equivalentClass axioms indirectly through the
  // OWLIndividual class.
  private void importAxioms() throws SWRLRuleEngineBridgeException, OWLConversionFactoryException
  {
    importClassDescriptions(); // cf. http://www.w3.org/TR/owl-ref, Section  3.1
    importClassAxioms(); // cf. http://www.w3.org/TR/owl-ref, Section  3
    importPropertyAxioms(); // cf. http://www.w3.org/TR/owl-ref, Section  4 
    importIndividualAxioms(); // cf. http://www.w3.org/TR/owl-ref, Section  5
    importDataValuedPropertyAxioms(); // cf. http://www.w3.org/TR/owl-ref, Section 6
    importAnnotations(); // cf. http://www.w3.org/TR/owl-ref, Section 7
  } // importAxioms

  // cf. http://www.w3.org/TR/owl-ref, Section 3.2
  private void importClassAxioms() throws SWRLRuleEngineBridgeException, OWLConversionFactoryException
  {
    // rdfs:subClassOf
    importEquivalentClassAxioms();
    importDisjointWithAxioms();
  } // importClassAxioms

  // cf. http://www.w3.org/TR/owl-ref, Section 4
  private void importPropertyAxioms() throws SWRLRuleEngineBridgeException, OWLConversionFactoryException
  {
    //importRDFSchemaPropertyAxioms() - rdfs:subPropertyOf, rdfs:domain, rdfs:range
    importEquivalentPropertyAxioms();
    importInverseOfAxioms();
    importFunctionalPropertyAxioms();
    importInverseFunctionalPropertyAxioms();
    importTransitivePropertyAxioms();
    importSymmetricPropertyAxioms();
  } // importPropertyAxioms

  // cf. http://www.w3.org/TR/owl-ref, Section 5
  private void importIndividualAxioms() throws SWRLRuleEngineBridgeException
  {
    importOWLSameIndividualAxioms();
    importOWLDifferentIndividualsAxioms();
    importOWLAllDifferentsAxioms();
  } // importIndividualAxioms

  // cf. http://www.w3.org/TR/owl-ref, Section 3.1.1
  private void importClassEnumerationDescriptions() throws SWRLRuleEngineBridgeException {}

  // cf. http://www.w3.org/TR/owl-ref, Section 3.1.2
  private void importPropertyRestrictions() throws SWRLRuleEngineBridgeException
  {
    importCardinalityRestrictions();
    importMinCardinalityRestrictions();
    importMaxCardinalityRestrictions();
    importAllValuesFromRestrictions();
    importSomeValuesFromRestrictions();
    importHasValueRestrictions();
  } // importPropertyRestrictions

  private void importOWLSameIndividualAxioms() throws SWRLRuleEngineBridgeException
  {
	try {
		for (OWLSameIndividualAxiom axiom : conversionFactory.getSameIndividualAxioms()) {
			importedOWLAxioms.add(axiom);
			cacheOWLIndividuals(axiom.getIndividuals());
		} // for
	} catch (OWLConversionFactoryException e) {
		throw new SWRLBuiltInBridgeException("error importing OWL same individuals axioms: " + e);
	}
  } // importOWLSameIndividualAxioms

  private void importOWLDifferentIndividualsAxioms() throws SWRLRuleEngineBridgeException
  {
	try {
		for (OWLDifferentIndividualsAxiom axiom : conversionFactory.getOWLDifferentIndividualsAxioms()) {
			importedOWLAxioms.add(axiom);
			cacheOWLIndividuals(axiom.getIndividuals());
		} // for
	} catch (OWLConversionFactoryException e) {
		throw new SWRLBuiltInBridgeException("error importing OWL different individuals axioms: " + e);
	}
  } // importOWLDifferentIndividualsAxioms

  private void importOWLAllDifferentsAxioms() throws SWRLRuleEngineBridgeException
  {
      // importOWLDifferentIndividualsAxioms effectively subsumes this method.
  } // importOWLAllDifferentsAxioms
  
  private void exportSWRLRules() throws SWRLRuleEngineBridgeException
  {
    for (SWRLRule rule : importedSWRLRules.values()) defineOWLAxiom(rule);
  } // exportSWRLRules

  private void exportClasses() throws SWRLRuleEngineBridgeException
  {
    for (OWLClass owlClass : importedOWLClasses.values()) exportClass(owlClass);
  } // exportClasses

  private void exportClass(OWLClass owlClass) throws SWRLRuleEngineBridgeException
  {
    String classURI = owlClass.getURI();
    Set<OWLClass> superClasses = owlClass.getSuperClasses();

    if (!exportedOWLClassURIs.contains(classURI)) { // See if it is already defined.
      exportOWLClass(owlClass);
      exportedOWLClassURIs.add(classURI);

      if (!superClasses.isEmpty()) { // Superclasses must be defined before subclasses.
        for (OWLClass superClass : superClasses) {
          OWLClass superOWLClass = importedOWLClasses.get(superClass.getURI());
          exportClass(superOWLClass); 
        } // for
      } // if
    } // if
  } // exportClass

  private void exportIndividuals() throws SWRLRuleEngineBridgeException, OWLConversionFactoryException
  {
    for (OWLIndividual owlIndividual : importedOWLIndividuals.values()) {
      String individualURI = owlIndividual.getURI();
      if (!exportedOWLIndividualURIs.contains(individualURI)) {
        exportOWLIndividual(owlIndividual);
        exportedOWLIndividualURIs.add(individualURI);
      } // if
    } // for
  } // exportIndividuals

  private void exportAxioms() throws SWRLRuleEngineBridgeException, OWLConversionFactoryException
  {
    for (OWLAxiom  axiom: importedOWLAxioms) exportOWLAxiom(axiom);
  } // exportAxioms

  private void writeInferredIndividuals2OWL() throws SWRLRuleEngineBridgeException, OWLConversionFactoryException
  {
    for (OWLIndividual owlIndividual : inferredOWLIndividuals.values()) conversionFactory.putOWLIndividual(owlIndividual);
  } // writeInferredIndividuals2OWL

  private void writeInferredAxioms2OWL() throws SWRLRuleEngineBridgeException, OWLConversionFactoryException
  {
    for (OWLAxiom axiom : inferredOWLAxioms) conversionFactory.putOWLAxiom(axiom);
  } // writeInferredAxioms2OWL
  
  private void initialize()
  {
    importedSWRLRules = new HashMap<String, SWRLRule>();

    referencedOWLClassURIs = new HashSet<String>();
    referencedOWLIndividualURIs = new HashSet<String>();
    referencedOWLPropertyURIs = new HashSet<String>();

    importedOWLClasses = new HashMap<String, OWLClass>();
    importedOWLIndividuals = new HashMap<String, OWLIndividual>(); 
    importedOWLAxioms = new HashSet<OWLAxiom>(); 
    importedOWLObjectPropertyURIs = new HashSet<String>();
    importedOWLDataPropertyURIs = new HashSet<String>();
    importedOWLAxioms = new HashSet<OWLAxiom>();

    exportedOWLClassURIs = new HashSet<String>();
    exportedOWLIndividualURIs = new HashSet<String>();

    inferredOWLIndividuals = new HashMap<String, OWLIndividual>(); 
    inferredOWLAxioms = new HashSet<OWLAxiom>(); 

    injectedOWLClasses = new HashMap<String, OWLClass>();
    injectedOWLIndividuals = new HashMap<String, OWLIndividual>();
    injectedOWLAxioms = new HashSet<OWLAxiom>();

    allOWLPropertyAssertionAxioms = new HashMap<String, Map<String, Set<OWLPropertyAssertionAxiom>>>();
    allOWLIndividuals = new HashMap<String, OWLIndividual>();
  } // initialize  

  private void checkOWLClassURI(String classURI) throws SWRLBuiltInBridgeException
  {
    if (!conversionFactory.isValidURI(classURI))
    	throw new SWRLBuiltInBridgeException("attempt to inject class with invalid URI " + classURI);
  } // checkOWLClassURI

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
  } // cacheOWLPropertyAssertionAxiom

  private void cacheOWLIndividual(OWLIndividual owlIndividual)
  {
    String individualURI = owlIndividual.getURI();

    if (!allOWLIndividuals.containsKey(individualURI)) allOWLIndividuals.put(individualURI, owlIndividual);
  } // cacheOWLIndividual  

  private void cacheOWLIndividuals(Set<OWLIndividual> individuals)
  {
    for (OWLIndividual individual: individuals) cacheOWLIndividual(individual);
  } // cacheOWLIndividuals

  private void exportOWLClass(OWLClass owlClass) throws SWRLBuiltInBridgeException
  {
    OWLDeclarationAxiom axiom = activeOWLFactory.getOWLDeclarationAxiom(owlClass);
    exportOWLAxiom(axiom);
  } // exportOWLClass

  private void exportOWLIndividual(OWLIndividual owlIndividual) throws SWRLBuiltInBridgeException
  {
  	OWLDeclarationAxiom axiom = activeOWLFactory.getOWLDeclarationAxiom(owlIndividual);
    exportOWLAxiom(axiom);
   } // exportOWLIndividual

  private void exportOWLAxiom(OWLAxiom owlAxiom) throws SWRLBuiltInBridgeException
  {
    try {
      defineOWLAxiom(owlAxiom);
    } catch (SWRLRuleEngineBridgeException e) {
      throw new SWRLBuiltInBridgeException("error exporting OWL axiom " + owlAxiom + ": " + e.getMessage());
    } // try
  } // exportOWLAxiom

  // cf. http://www.w3.org/TR/owl-ref, Section 6
  private void importDataValuedPropertyAxioms() throws SWRLRuleEngineBridgeException {}

  // cf. http://www.w3.org/TR/owl-ref, Section 7
  private void importAnnotations() throws SWRLRuleEngineBridgeException
  {
    // owl:versionInfo, rdfs:label, rdfs:comment, rdfs:seeAlso, rdfs:isDefinedBy 
  } // importAnnotations
  
  private void importClassDescriptions() throws SWRLRuleEngineBridgeException, OWLConversionFactoryException
  {
    importClassEnumerationDescriptions(); // 3.1.1
    importPropertyRestrictions(); // 3.1.2
    importIntersectionOfDescriptions(); // 3.1.3.1
    importUnionOfDescriptions(); // 3.1.3.2
    importComplementOfDescriptions(); // 3.1.3.3
  } // importClassDescriptions

  // cf. http://www.w3.org/TR/owl-ref, Section 3.1.2
  private void importCardinalityRestrictions() throws SWRLRuleEngineBridgeException {}
  // cf. http://www.w3.org/TR/owl-ref, Section 3.1.2
  private void importMinCardinalityRestrictions() throws SWRLRuleEngineBridgeException {}
  // cf. http://www.w3.org/TR/owl-ref, Section 3.1.2
  private void importMaxCardinalityRestrictions() throws SWRLRuleEngineBridgeException {}
  // cf. http://www.w3.org/TR/owl-ref, Section 3.1.2.1.1
  private void importAllValuesFromRestrictions() throws SWRLRuleEngineBridgeException {}
  // cf. http://www.w3.org/TR/owl-ref, Section 3.1.2.1.2
  private void importSomeValuesFromRestrictions() throws SWRLRuleEngineBridgeException {}
  // cf. http://www.w3.org/TR/owl-ref, Section 3.1.2.1.3
  private void importHasValueRestrictions() throws SWRLRuleEngineBridgeException {}

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

} // AbstractSWRLRuleEngineBridge
