
// TODO: way too big - needs to be refactored
// TODO: DataRange
// TODO: remove all Protege-OWL specific code (primarily be removing all references to OWLConversionFactory).

package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.stanford.smi.protegex.owl.model.OWLAllDifferent;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFIndividual;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.swrl.bridge.Atom;
import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.MultiArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLAxiom;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLClass;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLClassAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLConversionFactory;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLDataPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLDataValue;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLDeclarationAxiom;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLDifferentIndividualsAxiom;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLEntity;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLDataFactory;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLIndividual;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLObjectPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLProperty;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLSubClassAxiom;
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
import edu.stanford.smi.protegex.owl.swrl.sqwrl.SQWRLResult;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.InvalidQueryNameException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.SQWRLException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.impl.ResultImpl;
import edu.stanford.smi.protegex.owl.swrl.util.SWRLOWLUtil;

/**
 ** This class provides an implementation of some of the core functionality required by SWRL rule engine and built-in bridges. Detailed
 ** documentation for these bridges can be found <a href="http://protege.cim3.net/cgi-bin/wiki.pl?SWRLRuleEngineBridgeFAQ">here</a>.
 */
public abstract class AbstractSWRLRuleEngineBridge implements SWRLRuleEngineBridge, SWRLBuiltInBridge, TargetSWRLRuleEngine
{
  protected OWLDataFactory activeOWLFactory, injectedOWLFactory;
  protected OWLConversionFactory conversionFactory;
  protected OWLModel owlModel; 

  private HashMap<String, SWRLRule> importedSWRLRules; 

  // Imported classes, properties, and individuals
  private HashMap<String, OWLClass> importedClasses;
  private HashMap<String, OWLIndividual> importedIndividuals;
  private Set<String> importedObjectPropertyNames, importedDatatypePropertyNames;
  private Set<OWLAxiom> importedAxioms;

  // Inferred individuals and property assertion axioms
  private Map<String, OWLIndividual> inferredIndividuals;
  private Set<OWLAxiom> inferredAxioms; 

  // Injected entities
  private HashMap<String, OWLClass> injectedClasses;
  private HashMap<String, OWLIndividual> injectedIndividuals;
  private Set<OWLAxiom> injectedAxioms;

  // All entities
  private Map<String, Map<String, Set<OWLPropertyAssertionAxiom>>> allOWLPropertyAssertionAxioms; // individualURI <propertyURI, axiom>
  private Map<String, OWLIndividual> allOWLIndividuals; 

  // Names of classes, properties, and individuals that have been exported to target rule engine
  private Set<String> exportedClassNames, exportedIndividualNames; 

  // Names of classes, properties and individuals explicitly referred to in SWRL rules. These are filled in as the SWRL rules are imported
  // and are used to determine the relevant OWL knowledge to import.
  private Set<String> referencedClassNames, referencedPropertyNames, referencedIndividualNames;

  protected AbstractSWRLRuleEngineBridge(OWLModel owlModel) throws SWRLRuleEngineBridgeException
  {
    this.owlModel = owlModel;
    activeOWLFactory = new OWLFactoryImpl(owlModel);
    injectedOWLFactory = new OWLFactoryImpl();
    conversionFactory = new OWLConversionFactoryImpl(owlModel, activeOWLFactory);
    initialize();
    BuiltInLibraryManager.invokeAllBuiltInLibrariesResetMethod(this);
  } // AbstractSWRLRuleEngineBridge

  /**
   ** Load rules and knowledge from OWL into bridge. All existing bridge rules and knowledge will first be cleared and the associated rule
   ** engine will be reset.
   */
  public void importSWRLRulesAndOWLKnowledge() throws SWRLRuleEngineBridgeException
  {
    importSWRLRulesAndOWLKnowledge(new HashSet<String>());
  } // importSWRLRulesAndOWLKnowledge

  /**
   ** Load rules from a particular rule group and associated knowledge from OWL into bridge. All existing bridge rules and knowledge will
   ** first be cleared and the associated rule engine will be reset.
   */
  public void importSWRLRulesAndOWLKnowledge(String ruleGroupName) throws SWRLRuleEngineException
  {
    Set<String> ruleGroupNames = new HashSet<String>();
    ruleGroupNames.add(ruleGroupName);
    importSWRLRulesAndOWLKnowledge(ruleGroupNames);
  } // importSWRLRulesAndOWLKnowledge

  /**
   ** Load rules from all the named rule groups and associated knowledge from OWL into bridge. All existing bridge rules and knowledge will
   ** first be cleared and the associated rule engine will be reset.
   */
  public void importSWRLRulesAndOWLKnowledge(Set<String> ruleGroupNames) throws SWRLRuleEngineBridgeException
  {
    try {
      importSWRLRules(); // Fills in referencedClassNames, referencedIndividualNames, referencedPropertyNames
      importOWLClassesByName(referencedClassNames); // Import all (directly or indirectly) referenced classes.
      importOWLPropertyAssertionAxioms(referencedPropertyNames); // Import property assertion axioms for (directly or indirectly) referenced properties
      importOWLIndividuals(referencedIndividualNames); // Import all directly referenced individuals.
      importAllOWLIndividualsOfClasses(referencedClassNames); // Import all individuals that are members of imported classes.
      importAxioms(); // Import some other axioms (only owl:sameAs, owl:differentFrom, and owl:allDifferent for the moment).
      
      exportSWRLRulesAndOWLKnowledge();
    } catch (OWLConversionFactoryException e) {
      throw new SWRLRuleEngineBridgeException("error importing SWRL rules and OWL knowledge: " + e.getMessage());
    } // try

  } // importSWRLRulesAndOWLKnowledge

  private void importSWRLRules() throws SWRLRuleEngineBridgeException
  {
    try {
      for (SWRLRule rule : activeOWLFactory.getSWRLRules()) {
        importedSWRLRules.put(rule.getRuleName(), rule);
        
        for (Atom atom : rule.getBodyAtoms()) processSWRLAtom(atom, false);
        for (Atom atom : rule.getHeadAtoms()) processSWRLAtom(atom, true);
      } // for
    } catch (SQWRLException e) {
      throw new SWRLRuleEngineBridgeException("SQWRL error importing rules: " + e.getMessage());
    } catch (BuiltInException e) {
        throw new SWRLRuleEngineBridgeException("built-in error importing rules: " + e.getMessage());
    } catch (OWLFactoryException e) {
       throw new SWRLRuleEngineBridgeException("factory error importing rules: " + e.getMessage());
    } // try

  } // importSWRLRules

  private void processSWRLAtom(Atom atom, boolean isConsequent) throws SWRLRuleEngineBridgeException
  {
    if (atom.hasReferencedClasses()) referencedClassNames.addAll(atom.getReferencedClassNames());
    if (atom.hasReferencedProperties()) referencedPropertyNames.addAll(atom.getReferencedPropertyNames());
    if (atom.hasReferencedIndividuals()) referencedIndividualNames.addAll(atom.getReferencedIndividualNames());
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

  public void runSQWRLQueries() throws SQWRLException
  {
    try {
      reset();
      importSWRLRulesAndOWLKnowledge();
      run();
    } catch (SWRLRuleEngineBridgeException e) {
      throw new SQWRLException("error running SQWRL queries: " + e.getMessage());
    } // try
  } // runSQWRLQueries

  /**
   **  Get the results from a SQWRL query.
   */
  public SQWRLResult getSQWRLResult(String queryName) throws SQWRLException
  {
    ResultImpl result;

    if (!importedSWRLRules.containsKey(queryName)) throw new InvalidQueryNameException(queryName);

    result = importedSWRLRules.get(queryName).getSQWRLResult();

    if (!result.isPrepared()) result.prepared();

    return result;
  } // getSQWRLResult

  public SWRLRule getSWRLRule(String ruleName) throws SWRLBuiltInBridgeException
  {
    if (!importedSWRLRules.containsKey(ruleName)) throw new SWRLBuiltInBridgeException("invalid rule name: " + ruleName);

    return importedSWRLRules.get(ruleName);
  } // getRule

  public OWLDataFactory getOWLDataFactory() { return activeOWLFactory; }
  public OWLModel getOWLModel() { return owlModel; } // TODO: Protege  dependency - remove

  // Convenience methods to display bridge activity
  public int getNumberOfImportedSWRLRules() { return importedSWRLRules.size(); }
  public int getNumberOfImportedClasses() { return importedClasses.size(); }
  public int getNumberOfImportedIndividuals() { return importedIndividuals.size(); }
  public int getNumberOfImportedAxioms() { return importedAxioms.size(); }
  public int getNumberOfInferredIndividuals() { return inferredIndividuals.keySet().size(); }
  public int getNumberOfInferredAxioms() { return inferredAxioms.size(); }
  public int getNumberOfInjectedClasses() { return injectedClasses.size(); }
  public int getNumberOfInjectedIndividuals() { return injectedIndividuals.size(); }
  public int getNumberOfInjectedAxioms() { return injectedAxioms.size(); }

    public Set<OWLIndividual> getOWLIndividuals() { return new HashSet<OWLIndividual>(allOWLIndividuals.values()); }

  public boolean isInjectedOWLClass(String className) { return injectedClasses.containsKey(className); }
  public boolean isInjectedOWLIndividual(String individualName) { return injectedIndividuals.containsKey(individualName); }
  public boolean isInjectedOWLAxiom(OWLAxiom axiom) { return injectedAxioms.contains(axiom); }

  // Convenience methods to display the contents of the bridge
  public Set<SWRLRule> getImportedSWRLRules() { return new HashSet<SWRLRule>(importedSWRLRules.values()); }

  public Set<OWLClass> getImportedClasses() { return new HashSet<OWLClass>(importedClasses.values()); }
  public Set<OWLIndividual> getImportedIndividuals() { return new HashSet<OWLIndividual>(importedIndividuals.values()); }
  public Set<OWLAxiom> getImportedAxioms() { return importedAxioms; }

  public Set<OWLIndividual> getInferredIndividuals() { return new HashSet<OWLIndividual>(inferredIndividuals.values()); }
  public Set<OWLAxiom> getInferredAxioms() { return inferredAxioms; }

  public Set<OWLClass> getInjectedClasses() { return new HashSet<OWLClass>(injectedClasses.values()); }
  public Set<OWLIndividual> getInjectedIndividuals() { return new HashSet<OWLIndividual>(injectedIndividuals.values()); }
  public Set<OWLAxiom> getInjectedAxioms() { return injectedAxioms; }

  /**
   ** Infer an OWL axiom from a rule engine
   */
  public void inferOWLAxiom(OWLAxiom axiom) throws SWRLRuleEngineBridgeException
  { 
    if (!inferredAxioms.contains(axiom)) {
      inferredAxioms.add(axiom); 
      if (axiom instanceof OWLPropertyAssertionAxiom) cacheOWLPropertyAssertionAxiom((OWLPropertyAssertionAxiom)axiom);
      else if (axiom instanceof OWLClassAssertionAxiom) {
        OWLClassAssertionAxiom owlClassAssertionAxiom = (OWLClassAssertionAxiom)axiom;
        inferOWLIndividual(owlClassAssertionAxiom.getIndividual(), owlClassAssertionAxiom.getDescription());
      } // if

    } // if
  } // inferOWLAxiom

  /**
   ** Assert an OWL individual from a rule engine
   */
  private void inferOWLIndividual(OWLIndividual owlIndividual, OWLClass owlClass) throws SWRLRuleEngineBridgeException 
  {
    String individualName = owlIndividual.getIndividualName();

    if (inferredIndividuals.containsKey(individualName)) inferredIndividuals.get(individualName).addDefiningClass(owlClass);
    else if (injectedIndividuals.containsKey(individualName)) injectedIndividuals.get(individualName).addDefiningClass(owlClass);
    else {
      inferredIndividuals.put(individualName, owlIndividual); 
      cacheOWLIndividual(owlIndividual);
    } // if
  } // inferOWLIndividual

  /**
   ** Invoke a SWRL built-in from a rule engine. <p>
   **
   ** See <a href="http://protege.cim3.net/cgi-bin/wiki.pl?SWRLRuleEngineBridgeFAQ#nid6QF">here</a> for documentaton.
   */
  public boolean invokeSWRLBuiltIn(String ruleName, String builtInName, int builtInIndex, boolean isInConsequent, List<BuiltInArgument> arguments)
    throws BuiltInException
  {
    boolean hasUnboundArguments = hasUnboundArguments(arguments);
    boolean result = BuiltInLibraryManager.invokeSWRLBuiltIn(this, ruleName, builtInName, builtInIndex, isInConsequent, arguments);
    
    if (result && hasUnboundArguments) {
      if(hasUnboundArguments(arguments))
        throw new BuiltInException("built-in " + builtInName + " in rule " + ruleName + " has unbound arguments");
      generateBuiltInBindings(ruleName, builtInName, builtInIndex, arguments); // Generate all possible bindings.
    } // if

    return result;
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
    String className = owlClass.getClassName();

    if (!injectedClasses.containsKey(className)) {
      injectedClasses.put(className, owlClass);
      exportOWLClass(owlClass); // Export the class to the rule engine
    } // if

    return owlClass;
  } // injectOWLAnonymousClass

  public void injectOWLClass(String classURI) throws SWRLBuiltInBridgeException
  {
    checkOWLClassURI(classURI);

    if (!injectedClasses.containsKey(classURI)) {
      OWLClass owlClass = injectedOWLFactory.getOWLClass(classURI);
      injectedClasses.put(classURI, owlClass);
      exportOWLClass(owlClass); // Export the individual to the rule engine
    } // if
  } // injectOWLClass

  private void injectOWLSubClassAxiom(OWLSubClassAxiom axiom) throws SWRLBuiltInBridgeException
  {
    String subclassURI = axiom.getSubClass().getURI();
    String superclassURI = axiom.getSuperClass().getURI();
    injectedOWLFactory.getOWLClass(subclassURI);
    injectedOWLFactory.getOWLClass(superclassURI);

    injectedClasses.put(subclassURI, axiom.getSubClass());
    injectedClasses.put(superclassURI, axiom.getSuperClass());
  } // injectOWLSubClassAxiom

  /**
   ** Method used to inject a bridge individual of type owl:Thing. This method will typically be invoked from within a built-in. An OWL
   ** individual is not injected at this point - instead an object is generated for the individual in the bridge and the individual is
   ** exported to the rule engine. The individual is given a unique name that can be used later if an OWL individual is injected for it.
   */
  public OWLIndividual injectOWLIndividual() throws SWRLBuiltInBridgeException
  {
    String individualURI = conversionFactory.createNewResourceName("SWRLInjected");
    OWLClass owlClass = injectedOWLFactory.getOWLClass(edu.stanford.smi.protegex.owl.model.OWLNames.Cls.THING);
    OWLIndividual owlIndividual = injectedOWLFactory.getOWLIndividual(individualURI);
    owlIndividual.addDefiningClass(owlClass);
    injectOWLIndividual(owlIndividual);
    return owlIndividual;
  } // injectOWLIndividual
    
  public void injectOWLIndividual(OWLIndividual owlIndividual) throws SWRLBuiltInBridgeException
  {
    if (!injectedIndividuals.containsKey(owlIndividual.getIndividualName())) {
      injectedIndividuals.put(owlIndividual.getIndividualName(), owlIndividual); 
      cacheOWLIndividual(owlIndividual);
      exportOWLIndividual(owlIndividual); // Export the individual to the rule engine.
    } // if
  } // injectOWLIndividual

  public OWLIndividual injectOWLIndividualOfClass(OWLClass owlClass) throws SWRLBuiltInBridgeException
  {
    String individualURI = conversionFactory.createNewResourceName("SWRLInjected");
    OWLIndividual owlIndividual = injectedOWLFactory.getOWLIndividual(individualURI);
    owlIndividual.addDefiningClass(owlClass);

    if (!importedClasses.containsKey(owlClass.getClassName())) exportOWLClass(owlClass);
   
    injectedIndividuals.put(individualURI, owlIndividual); 
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
    if (!injectedAxioms.contains(axiom)) {
      injectedAxioms.add(axiom);
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
    if (!injectedAxioms.contains(axiom)) {
      injectedAxioms.add(axiom);
      cacheOWLPropertyAssertionAxiom(axiom);
    } // if

    exportOWLAxiom(axiom); // Export the axiom to the rule engine.
  } // injectOWLObjectPropertyAssertionAxiom

  public void injectOWLAxiom(OWLAxiom axiom) throws SWRLBuiltInBridgeException
  {
    if (!injectedAxioms.contains(axiom)) {
      injectedAxioms.add(axiom);

      if (axiom instanceof OWLPropertyAssertionAxiom) cacheOWLPropertyAssertionAxiom((OWLPropertyAssertionAxiom)axiom);
      else if (axiom instanceof OWLDeclarationAxiom) injectOWLDeclarationAxiom((OWLDeclarationAxiom)axiom);
      else if (axiom instanceof OWLSubClassAxiom) injectOWLSubClassAxiom((OWLSubClassAxiom)axiom);

      exportOWLAxiom(axiom); // Export the axiom to the rule engine.
    } // if
  } // injectOWLAxiom
  
  public boolean isOWLClass(String className) 
  { 
	 return importedClasses.containsKey(className) || injectedClasses.containsKey(className) ||
	        conversionFactory.isOWLClass(className);
  } // isOWLClass
  
  public boolean isOWLObjectProperty(String propertyName) 
  { 
	return importedObjectPropertyNames.contains(propertyName) || conversionFactory.isOWLObjectProperty(propertyName);
  } // isOWLObjectProperty
  
  public boolean isOWLDataProperty(String propertyName) 
  { 
	  return importedDatatypePropertyNames.contains(propertyName) || conversionFactory.isOWLDataProperty(propertyName);
  } // isOWLDataProperty
  
  public boolean isOWLProperty(String propertyName) 
  { 
	  return importedObjectPropertyNames.contains(propertyName) || importedDatatypePropertyNames.contains(propertyName) ||
	         conversionFactory.isOWLProperty(propertyName);
  } // isOWLProperty
  
  public boolean isOWLIndividual(String individualName) 
  { 
	  return allOWLIndividuals.containsKey(individualName) || conversionFactory.isOWLIndividual(individualName);
  } // isOWLIndividual
  
  public boolean isOWLIndividualOfClass(String individualName, String classURI)
  {
    boolean result = false; 
    
    if (allOWLIndividuals.containsKey(individualName)) {
      OWLIndividual owlIndividual = allOWLIndividuals.get(individualName);
      result = owlIndividual.hasClass(classURI);
    } // if

    if (!result) result = conversionFactory.isOWLIndividualOfClass(individualName, classURI);

    return result;
  } // isOWLIndividualOfClass

  public Set<OWLPropertyAssertionAxiom> getOWLPropertyAssertionAxioms(String individualName, String propertyName) 
    throws SWRLBuiltInBridgeException
  {
    if (allOWLPropertyAssertionAxioms.containsKey(individualName)) {
      Map<String, Set<OWLPropertyAssertionAxiom>> propertyAxiomsMap = allOWLPropertyAssertionAxioms.get(individualName);
      if (propertyAxiomsMap.containsKey(propertyName)) return propertyAxiomsMap.get(propertyName);
      else return new HashSet<OWLPropertyAssertionAxiom>();
    } else {
    	Set<OWLPropertyAssertionAxiom> result = null;
    	try {
    	  result = conversionFactory.getOWLPropertyAssertionAxioms(individualName, propertyName);
    	} catch (DataValueConversionException e) {
      	  throw new SWRLBuiltInBridgeException("error getting property assertion axiom for individual '" + individualName + 
      			                                "', property '" + propertyName + "': " + e.getMessage(), e); 
        } catch (OWLConversionFactoryException e) {
      	  throw new SWRLBuiltInBridgeException("error getting property assertion axiom for individual '" + individualName + 
     			                               "', property '" + propertyName + "': " + e.getMessage(), e);
        } // try
        return result;
    } // if
  } // getOWLPropertyAssertionAxioms

  /**
   ** Create OWL classes in model for the classes injected by built-ins during rule execution.
   */
  private void writeInjectedClasses() throws SWRLRuleEngineBridgeException, OWLConversionFactoryException
  {
    for (OWLClass owlClass: injectedClasses.values()) conversionFactory.putOWLClass(owlClass);   
  } // writeInjectedClasses

  /**
   ** Create OWL individuals in model for the individuals injected by built-ins during rule execution.
   */
  private void writeInjectedIndividuals() throws SWRLRuleEngineBridgeException, OWLConversionFactoryException
  {
    for (OWLIndividual owlIndividual: injectedIndividuals.values()) conversionFactory.putOWLIndividual(owlIndividual);
  } // writeInjectedIndividuals

  /**
   ** Create OWL axioms in model for the axioms injected by built-ins during rule execution.
   */
  private void writeInjectedAxioms() throws SWRLRuleEngineBridgeException, OWLConversionFactoryException
  {
    for (OWLAxiom axiom : injectedAxioms) conversionFactory.putOWLAxiom(axiom);
  } // writeInjectedAxioms
  
  private void importOWLClass(String classURI) throws SWRLRuleEngineBridgeException, OWLConversionFactoryException
  {
    // TODO: workaround because owlModel.createOWLNamedClass() called in OWLClass does not always return an OWL named class.
    RDFSNamedClass rdfsNamedClass = SWRLOWLUtil.getRDFSNamedClass(owlModel, classURI);
    if (rdfsNamedClass != null && (rdfsNamedClass.isMetaclass() || rdfsNamedClass.isAnonymous())) return;

    if (!importedClasses.containsKey(classURI)) {
      OWLClass owlClass = activeOWLFactory.getOWLClass(classURI);
      importedClasses.put(classURI, owlClass);
      importOWLClassesByName(owlClass.getDirectSuperClassNames());
      importOWLClassesByName(owlClass.getDirectSubClassNames());
    } // if
  } // importOWLClass

  private void importOWLClassesByName(Set<String> classURIs) throws SWRLRuleEngineBridgeException, OWLConversionFactoryException
  {
    for (String classURI : classURIs) importOWLClass(classURI);
  } // importOWLClassesByName

  private void importOWLClasses(Set<OWLClass> classes) throws SWRLRuleEngineBridgeException, OWLConversionFactoryException
  {
    for (OWLClass owlClass : classes) importOWLClass(owlClass.getClassName());
  } // importOWLClasses

  private void importAllOWLIndividualsOfClasses(Set<String> classURIs) throws SWRLRuleEngineBridgeException, OWLConversionFactoryException
  {
    for (String classURI : classURIs) importAllOWLIndividualsOfClass(classURI);
  } // importAllOWLIndividualsOfClasses

  private void importAllOWLIndividualsOfClass(String classURI) throws SWRLRuleEngineBridgeException, OWLConversionFactoryException
  {
    RDFSClass rdfsClass = SWRLOWLUtil.getRDFSNamedClass(owlModel, classURI);
    Collection instances = new ArrayList();

    if (rdfsClass != null) {
      instances.addAll(rdfsClass.getInstances(true));
    } // if

    Iterator iterator = instances.iterator();
    while (iterator.hasNext()) {
      Object o = iterator.next();
      if (o instanceof edu.stanford.smi.protegex.owl.model.OWLIndividual) { 
        edu.stanford.smi.protegex.owl.model.OWLIndividual individual = (edu.stanford.smi.protegex.owl.model.OWLIndividual)o;
        importOWLIndividual(individual.getName());
      } else if (o instanceof edu.stanford.smi.protegex.owl.model.OWLNamedClass) { // This will be OWL Full
        edu.stanford.smi.protegex.owl.model.OWLNamedClass cls = (edu.stanford.smi.protegex.owl.model.OWLNamedClass)o;
        importOWLClass(cls.getName());
      } // if
    } // while
  } // importAllOWLIndividualsOfClass
  
  private void importOWLPropertyAssertionAxioms(Set<String> propertyNames) throws SWRLRuleEngineBridgeException, OWLConversionFactoryException
  {
    for (String propertyName : propertyNames) importOWLPropertyAssertionAxioms(propertyName);
  } // importOWLPropertyAssertionAxioms

  private void importOWLPropertyAssertionAxioms(String propertyName) throws SWRLRuleEngineBridgeException, OWLConversionFactoryException
  {
    edu.stanford.smi.protegex.owl.model.OWLProperty property = SWRLOWLUtil.getOWLProperty(owlModel, propertyName);

    if (property != null) {
      if (!(importedObjectPropertyNames.contains(propertyName) || importedDatatypePropertyNames.contains(propertyName))) {
        Set<OWLPropertyAssertionAxiom> axioms = conversionFactory.getOWLPropertyAssertionAxioms(propertyName);
        importedAxioms.addAll(axioms);
        
        for (OWLPropertyAssertionAxiom axiom : axioms) {
          String subjectName = axiom.getSubject().getIndividualName();
          cacheOWLPropertyAssertionAxiom(axiom);
          if (!referencedIndividualNames.contains(subjectName)) referencedIndividualNames.add(subjectName);

          if (axiom instanceof OWLObjectPropertyAssertionAxiom) {
            OWLObjectPropertyAssertionAxiom objectPropertyAssertionAxiom = (OWLObjectPropertyAssertionAxiom)axiom;
            String objectName = objectPropertyAssertionAxiom.getObject().getIndividualName();
            if (!referencedIndividualNames.contains(objectName)) referencedIndividualNames.add(objectName);
          } // if
        } // for
        
        if (property.isObjectProperty()) importedObjectPropertyNames.add(propertyName);
        else importedDatatypePropertyNames.add(propertyName);

        importOWLClassesByName(SWRLOWLUtil.rdfResources2Names(property.getUnionDomain()));
        importOWLClassesByName(SWRLOWLUtil.rdfResources2Names(property.getUnionRangeClasses()));
        
        importOWLPropertyAssertionAxioms(SWRLOWLUtil.rdfResources2Names(property.getSuperproperties(true)));
        importOWLPropertyAssertionAxioms(SWRLOWLUtil.rdfResources2Names(property.getSubproperties(true)));
        importOWLPropertyAssertionAxioms(SWRLOWLUtil.rdfResources2Names(property.getEquivalentProperties()));
      } // if
    } // if
  } // importOWLPropertyAssertionAxioms

  private void importOWLIndividuals(Set<String> individualNames) throws SWRLRuleEngineBridgeException, OWLConversionFactoryException
  {
    for (String individualName : individualNames) importOWLIndividual(individualName);
  } // importOWLIndividuals

  private void importOWLIndividual(String individualName) throws SWRLRuleEngineBridgeException, OWLConversionFactoryException
  {
    if (!importedIndividuals.containsKey(individualName)) {
      OWLIndividual owlIndividual = activeOWLFactory.getOWLIndividual(individualName);
      importedIndividuals.put(individualName, owlIndividual);
      cacheOWLIndividual(owlIndividual);
      importOWLClasses(owlIndividual.getDefiningClasses());
      importOWLClasses(owlIndividual.getDefiningSuperclasses());
      importOWLClasses(owlIndividual.getDefiningEquivalentClasses());
      importOWLClasses(owlIndividual.getDefiningEquivalentClassSuperclasses());
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

  private void importClassDescriptions() throws SWRLRuleEngineBridgeException, OWLConversionFactoryException
  {
    importClassEnumerationDescriptions(); // 3.1.1
    importPropertyRestrictions(); // 3.1.2
    importIntersectionOfDescriptions(); // 3.1.3.1
    importUnionOfDescriptions(); // 3.1.3.2
    importComplementOfDescriptions(); // 3.1.3.3
  } // importClassDescriptions

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
    importSameAsAxioms();
    importDifferentFromAxioms();
    importAllDifferentsAxioms();
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

  // cf. http://www.w3.org/TR/owl-ref, Section 6
  private void importDataValuedPropertyAxioms() throws SWRLRuleEngineBridgeException {}

  // cf. http://www.w3.org/TR/owl-ref, Section 7
  private void importAnnotations() throws SWRLRuleEngineBridgeException
  {
    // owl:versionInfo, rdfs:label, rdfs:comment, rdfs:seeAlso, rdfs:isDefinedBy 
  } // importAnnotations
  
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

  // TODO: This is incredibly inefficient. Need to use method in the OWLModel to get individuals with a particular property.
  private void importSameAsAxioms() throws SWRLRuleEngineBridgeException
  {
    RDFProperty sameAsProperty = SWRLOWLUtil.getOWLSameAsProperty(owlModel);
    RDFSClass owlThingCls = SWRLOWLUtil.getOWLThingClass(owlModel);

    Iterator individualsIterator1 = owlThingCls.getInstances(true).iterator();
    while (individualsIterator1.hasNext()) {
      Object object1 = individualsIterator1.next();
      if (!(object1 instanceof edu.stanford.smi.protegex.owl.model.OWLIndividual)) continue; // Deal only with OWL individuals (could return metaclass, for example)
      edu.stanford.smi.protegex.owl.model.OWLIndividual individual1 = (edu.stanford.smi.protegex.owl.model.OWLIndividual)object1;
      if (individual1.hasPropertyValue(sameAsProperty)) {
        Collection individuals = (Collection)individual1.getPropertyValues(sameAsProperty);
        Iterator individualsIterator2 = individuals.iterator();
        while (individualsIterator2.hasNext()) {
          Object object2 = individualsIterator2.next();
          if (!(object2 instanceof edu.stanford.smi.protegex.owl.model.OWLIndividual)) continue;
          edu.stanford.smi.protegex.owl.model.OWLIndividual individual2 = (edu.stanford.smi.protegex.owl.model.OWLIndividual)object2;
          importedAxioms.add(activeOWLFactory.getOWLSameIndividualsAxiom(activeOWLFactory.getOWLIndividual(individual1.getName()), 
                                                                         activeOWLFactory.getOWLIndividual(individual2.getName())));
        } // while
      } // if
    } // while
  } // importSameAsAxioms

  // TODO: This is incredibly inefficient (and almost duplicates previous method). Need to use method in the OWLModel to get individuals
  // with a particular property.
  private void importDifferentFromAxioms() throws SWRLRuleEngineBridgeException 
  {
    RDFProperty differentFromProperty = SWRLOWLUtil.getOWLDifferentFromProperty(owlModel);
    RDFSClass owlThingCls = SWRLOWLUtil.getOWLThingClass(owlModel);

    Iterator individualsIterator1 = owlThingCls.getInstances(true).iterator();
    while (individualsIterator1.hasNext()) {
      Object object1 = individualsIterator1.next();
      if (!(object1 instanceof OWLIndividual)) continue; // Deal only with OWL individuals (could return metaclass, for example)
      edu.stanford.smi.protegex.owl.model.OWLIndividual individual1 = (edu.stanford.smi.protegex.owl.model.OWLIndividual)object1;
      if (individual1.hasPropertyValue(differentFromProperty)) {
        Collection individuals = (Collection)individual1.getPropertyValues(differentFromProperty);
        Iterator individualsIterator2 = individuals.iterator();
        while (individualsIterator2.hasNext()) {
          Object object2 = individualsIterator2.next();
          if (!(object2 instanceof edu.stanford.smi.protegex.owl.model.OWLIndividual)) continue;
          edu.stanford.smi.protegex.owl.model.OWLIndividual individual2 = (edu.stanford.smi.protegex.owl.model.OWLIndividual)object2;
          importedAxioms.add(activeOWLFactory.getOWLDifferentIndividualsAxiom(activeOWLFactory.getOWLIndividual(individual1.getName()), 
                                                                              activeOWLFactory.getOWLIndividual(individual2.getName())));
        } // while
      } // if
    } // while
  } // importDifferentFromAxioms

  private void importAllDifferentsAxioms() throws SWRLRuleEngineBridgeException
  {
    Collection allDifferents = SWRLOWLUtil.getOWLAllDifferents(owlModel);

    if (!allDifferents.isEmpty()) {
      Iterator allDifferentsIterator = allDifferents.iterator();
      while (allDifferentsIterator.hasNext()) {
        OWLAllDifferent owlAllDifferent = (OWLAllDifferent)allDifferentsIterator.next();

        if (owlAllDifferent.getDistinctMembers().size() != 0) {
          OWLDifferentIndividualsAxiom owlDifferentIndividualsAxiom;
          Set<OWLIndividual> individuals = new HashSet<OWLIndividual>();
          
          Iterator individualsIterator = owlAllDifferent.getDistinctMembers().iterator();
          while (individualsIterator.hasNext()) {
            RDFIndividual individual = (RDFIndividual)individualsIterator.next();
            if (individual instanceof edu.stanford.smi.protegex.owl.model.OWLIndividual) { // Ignore non OWL individuals
              String individualName = ((edu.stanford.smi.protegex.owl.model.OWLIndividual)individual).getName();
              OWLIndividual owlIndividual = activeOWLFactory.getOWLIndividual(individualName);
              individuals.add(owlIndividual);
              cacheOWLIndividual(owlIndividual);
            } // if
          } // while
          owlDifferentIndividualsAxiom = activeOWLFactory.getOWLDifferentIndividualsAxiom(individuals);
          importedAxioms.add(owlDifferentIndividualsAxiom);
        } // if
        } // while
    } // if
  } // importAllDifferentsAxioms

  private void exportSWRLRules() throws SWRLRuleEngineBridgeException
  {
    for (SWRLRule rule : importedSWRLRules.values()) defineSWRLRule(rule);
  } // exportSWRLRules

  private void exportClasses() throws SWRLRuleEngineBridgeException
  {
    for (OWLClass owlClass : importedClasses.values()) exportClass(owlClass);
  } // exportClasses

  private void exportClass(OWLClass owlClass) throws SWRLRuleEngineBridgeException
  {
    String classURI = owlClass.getClassName();
    Set<String> superClassNames = owlClass.getDirectSuperClassNames();

    if (!exportedClassNames.contains(classURI)) { // See if it is already defined.
      exportOWLClass(owlClass);
      exportedClassNames.add(classURI);

      if (!superClassNames.isEmpty()) { // Superclasses must be defined before subclasses.
        for (String superClassName : superClassNames) {
          OWLClass superOWLClass = importedClasses.get(superClassName);
          exportClass(superOWLClass); 
        } // for
      } // if
    } // if
  } // exportClass

  private void exportIndividuals() throws SWRLRuleEngineBridgeException, OWLConversionFactoryException
  {
    for (OWLIndividual owlIndividual : importedIndividuals.values()) {
      String individualName = owlIndividual.getIndividualName();
      if (!exportedIndividualNames.contains(individualName)) {
        exportOWLIndividual(owlIndividual);
        exportedIndividualNames.add(individualName);
      } // if
    } // for
  } // exportIndividuals

  private void exportAxioms() throws SWRLRuleEngineBridgeException, OWLConversionFactoryException
  {
    for (OWLAxiom  axiom: importedAxioms) exportOWLAxiom(axiom);
  } // exportAxioms

  private void writeInferredIndividuals2OWL() throws SWRLRuleEngineBridgeException, OWLConversionFactoryException
  {
    for (OWLIndividual owlIndividual : inferredIndividuals.values()) conversionFactory.putOWLIndividual(owlIndividual);
  } // writeInferredIndividuals2OWL

  private void writeInferredAxioms2OWL() throws SWRLRuleEngineBridgeException, OWLConversionFactoryException
  {
    for (OWLAxiom axiom : inferredAxioms) conversionFactory.putOWLAxiom(axiom);
  } // writeInferredAxioms2OWL
  
  private void initialize()
  {
    importedSWRLRules = new HashMap<String, SWRLRule>();

    referencedClassNames = new HashSet<String>();
    referencedIndividualNames = new HashSet<String>();
    referencedPropertyNames = new HashSet<String>();

    importedClasses = new HashMap<String, OWLClass>();
    importedIndividuals = new HashMap<String, OWLIndividual>(); 
    importedAxioms = new HashSet<OWLAxiom>(); 
    importedObjectPropertyNames = new HashSet<String>();
    importedDatatypePropertyNames = new HashSet<String>();
    importedAxioms = new HashSet<OWLAxiom>();

    exportedClassNames = new HashSet<String>();
    exportedIndividualNames = new HashSet<String>();

    inferredIndividuals = new HashMap<String, OWLIndividual>(); 
    inferredAxioms = new HashSet<OWLAxiom>(); 

    injectedClasses = new HashMap<String, OWLClass>();
    injectedIndividuals = new HashMap<String, OWLIndividual>();
    injectedAxioms = new HashSet<OWLAxiom>();

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
    String subjectName = axiom.getSubject().getIndividualName();
    String propertyName = axiom.getProperty().getPropertyName();
    Map<String, Set<OWLPropertyAssertionAxiom>> propertyAxiomsMap;
    Set<OWLPropertyAssertionAxiom> axiomSet;

    if (allOWLPropertyAssertionAxioms.containsKey(subjectName)) 
      propertyAxiomsMap = allOWLPropertyAssertionAxioms.get(subjectName);
    else {
      propertyAxiomsMap = new HashMap<String, Set<OWLPropertyAssertionAxiom>>();
      allOWLPropertyAssertionAxioms.put(subjectName, propertyAxiomsMap);
    } // if

    if (propertyAxiomsMap.containsKey(propertyName)) axiomSet = propertyAxiomsMap.get(propertyName);
    else {
      axiomSet = new HashSet<OWLPropertyAssertionAxiom>();
      propertyAxiomsMap.put(propertyName, axiomSet);
    } // if
    
    axiomSet.add(axiom);     
  } // cacheOWLPropertyAssertionAxiom

  private void cacheOWLIndividual(OWLIndividual owlIndividual)
  {
    String individualName = owlIndividual.getIndividualName();

    if (!allOWLIndividuals.containsKey(individualName)) allOWLIndividuals.put(individualName, owlIndividual);
  } // cacheOWLIndividual  

  private void exportOWLClass(OWLClass owlClass) throws SWRLBuiltInBridgeException
  {
    try {
      defineOWLClass(owlClass);
    } catch (SWRLRuleEngineBridgeException e) {
      throw new SWRLBuiltInBridgeException("error exporting OWL class '" + owlClass.getPrefixedClassName() + "'");
    } // try
  } // exportOWLClass

  private void exportOWLIndividual(OWLIndividual owlIndividual) throws SWRLBuiltInBridgeException
  {
    try {
      defineOWLIndividual(owlIndividual);
    } catch (SWRLRuleEngineBridgeException e) {
      throw new SWRLBuiltInBridgeException("error exporting OWL individual '" + owlIndividual.getPrefixedIndividualName() + "'");
    } // try
  } // exportOWLIndividual

  private void exportOWLAxiom(OWLAxiom owlAxiom) throws SWRLBuiltInBridgeException
  {
    try {
      defineOWLAxiom(owlAxiom);
    } catch (SWRLRuleEngineBridgeException e) {
      throw new SWRLBuiltInBridgeException("error exporting OWL axiom " + owlAxiom + ": " + e.getMessage());
    } // try
  } // exportOWLAxiom

  // This method is called with a list of arguments that contain the results of a built-in that bound at least one of its
  // arguments. Some argument positions may contain multi-arguments, indicating that there is more than one pattern. If so, the 
  // method generates the cross product of all possible results.
  //
  // Warning: this mechanism is incorrect and only works properly when a single argument is unbound. If two or more
  // arguments are unbound and contain multi-arguments then the cross product is not correct. TODO: need to fix. 
  private void generateBuiltInBindings(String ruleName, String builtInName, int builtInIndex, List<BuiltInArgument> arguments)
    throws BuiltInException
  {
    List<Integer> multiArgumentIndexes = getMultiArgumentIndexes(arguments);
    
    if (multiArgumentIndexes.isEmpty()) // No multi-arguments - do a simple bind.
      exportBuiltInBinding(ruleName, builtInName, builtInIndex, arguments); 
    else { // Generate cross product of all possible bindings.
      List<Integer> multiArgumentCounts = new ArrayList<Integer>();
      List<Integer> multiArgumentSizes = new ArrayList<Integer>();
      List<BuiltInArgument> argumentsPattern;

      for (int i = 0; i < multiArgumentIndexes.size(); i++) multiArgumentCounts.add(Integer.valueOf(0));
      for (int i = 0; i < multiArgumentIndexes.size(); i++) {
        MultiArgument multiArgument = (MultiArgument)arguments.get(multiArgumentIndexes.get(i).intValue());
        multiArgumentSizes.add(Integer.valueOf(multiArgument.getNumberOfArguments()));
      } // for

      do {
        argumentsPattern = generateArgumentsPattern(arguments, multiArgumentCounts);
        exportBuiltInBinding(ruleName, builtInName, builtInIndex, argumentsPattern); 
      } while (!nextMultiArgumentCounts(multiArgumentCounts, multiArgumentSizes));
    } // if
  } // generateBuiltInBindings

  // Find indices of multi-arguments (if any) in a list of arguments.
  private List<Integer> getMultiArgumentIndexes(List<BuiltInArgument> arguments)
  {
    List<Integer> result = new ArrayList<Integer>();

    for (int i = 0; i < arguments.size(); i++) 
      if (arguments.get(i) instanceof MultiArgument) result.add(Integer.valueOf(i));

    return result;
  } // getMultiArgumentIndexes

  // Export a particular binding pattern to a target rule engine.  
  private void exportBuiltInBinding(String ruleName, String builtInName, int builtInIndex, List<BuiltInArgument> arguments) 
    throws BuiltInException
  {
    try {
      defineBuiltInBinding(ruleName, builtInName, builtInIndex, arguments);
    } catch (SWRLRuleEngineBridgeException e) {
      throw new BuiltInException("error exporting built-in binding: " + e.getMessage());
    } // try
  } // exportBuiltInBinding

  private static boolean nextMultiArgumentCounts(List<Integer> multiArgumentCounts, List<Integer> multiArgumentSizes)
    throws BuiltInException
  {
    if (multiArgumentSizes.isEmpty()) return true;
    
    if (nextMultiArgumentCounts(multiArgumentCounts.subList(1, multiArgumentCounts.size()), 
                                multiArgumentSizes.subList(1, multiArgumentSizes.size()))) {
      // No more permutations of rest of list so increment this count and if we are not at the end set rest of the list to begin at 0 again.
      int count = multiArgumentCounts.get(0);
      int size = multiArgumentSizes.get(0);
      
      if (++count == size) return true;

      multiArgumentCounts.set(0, Integer.valueOf(count));

      for (int i = 1; i < multiArgumentCounts.size(); i++) multiArgumentCounts.set(i, Integer.valueOf(0));
    } // if
    return false;
  } // nextMultiArgumentCounts

  private List<BuiltInArgument> generateArgumentsPattern(List<BuiltInArgument> arguments, List<Integer> multiArgumentCounts)
  {
    List<BuiltInArgument> result = new ArrayList<BuiltInArgument>();
    int multiArgumentIndex = 0;

    for (BuiltInArgument argument: arguments) {
      if (argument instanceof MultiArgument) {
        MultiArgument multiArgument = (MultiArgument)argument;
        result.add(multiArgument.getArguments().get((multiArgumentCounts.get(multiArgumentIndex).intValue())));
        multiArgumentIndex++;
      } else result.add(argument);
    } // for

    return result;
  } // generateArgumentsPattern  

  public boolean hasUnboundArguments(List<BuiltInArgument> arguments)
  {
    for (BuiltInArgument argument : arguments) if (argument.isUnbound()) return true;

    return false;
  } // hasUnboundArguments
  
} // AbstractSWRLRuleEngineBridge
