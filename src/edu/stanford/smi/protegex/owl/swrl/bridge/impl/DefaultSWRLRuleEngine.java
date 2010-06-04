
package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import java.util.HashSet;
import java.util.Set;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.swrl.SWRLRuleEngine;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLDataValueFactory;
import edu.stanford.smi.protegex.owl.swrl.bridge.SWRLBuiltInBridgeController;
import edu.stanford.smi.protegex.owl.swrl.bridge.SWRLProcessor;
import edu.stanford.smi.protegex.owl.swrl.bridge.SWRLRule;
import edu.stanford.smi.protegex.owl.swrl.bridge.SWRLRuleEngineBridgeController;
import edu.stanford.smi.protegex.owl.swrl.bridge.TargetSWRLRuleEngine;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.OWLConversionFactoryException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.OWLFactoryException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.TargetSWRLRuleEngineException;
import edu.stanford.smi.protegex.owl.swrl.exceptions.SWRLRuleEngineException;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLClass;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLDataFactory;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLDeclarationAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLNamedIndividual;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLOntology;
import edu.stanford.smi.protegex.owl.swrl.owlapi.impl.OWLOntologyImpl;
import edu.stanford.smi.protegex.owl.swrl.parser.SWRLParseException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.SQWRLResult;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.SQWRLException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.impl.SQWRLResultImpl;

/**
 * This class provides an implementation of some of the core functionality required by SWRL rule engine. Detailed
 * documentation for this mechanism can be found <a href="http://protege.cim3.net/cgi-bin/wiki.pl?SWRLRuleEngineBridgeFAQ">here</a>.
 */
public class DefaultSWRLRuleEngine implements SWRLRuleEngine
{
	private OWLOntology activeOntology;
  private SWRLProcessor swrlProcessor;
  private TargetSWRLRuleEngine targetRuleEngine;
  private SWRLBuiltInBridgeController builtInBridgeController;
  private SWRLRuleEngineBridgeController ruleEngineBridgeController;
  private OWLDataFactory activeOWLFactory;
  private OWLDataValueFactory owlDataValueFactory; 

  // URIs of classes and individuals that have been exported to target rule engine
  private Set<String> exportedOWLClassURIs, exportedOWLIndividualURIs; 
  
  public DefaultSWRLRuleEngine(OWLOntology activeOntology, SWRLProcessor swrlProcessor, TargetSWRLRuleEngine targetRuleEngine, 
  		                         SWRLRuleEngineBridgeController ruleEngineBridgeController, SWRLBuiltInBridgeController builtInBridgeController) 
  throws SWRLRuleEngineException
  {
  	this.activeOntology = activeOntology;
    this.swrlProcessor = swrlProcessor;
    this.targetRuleEngine = targetRuleEngine;
    this.builtInBridgeController = builtInBridgeController;
    this.ruleEngineBridgeController = ruleEngineBridgeController;

    owlDataValueFactory = OWLDataValueFactory.create();
    
    initialize();   
  }

  /**
   * Load rules and knowledge from OWL into bridge. All existing bridge rules and knowledge will first be cleared and the associated rule
   * engine will be reset.
   */
  public void importSWRLRulesAndOWLKnowledge() throws SWRLRuleEngineException
  {
  	swrlProcessor.importOWLAxioms();
  	
  	exportOWLAxioms();
  }

  private void importSQWRLQuery(String queryName) throws SWRLRuleEngineException
  {
    try {
      SWRLRule rule = activeOWLFactory.getSWRLRule(queryName);
      swrlProcessor.process(rule);
    } catch (OWLFactoryException e) {
    	throw new SWRLRuleEngineException("factory error importing SQWRL query " + queryName + ": " + e.getMessage());
    } // try
  } 
  
  /**
   * Run the rule engine.
   */
  public void run() throws SWRLRuleEngineException
  {
    getRuleEngine().runRuleEngine();
  } 

  /**
   * Clear all knowledge from bridge.
   */
  public void reset() throws SWRLRuleEngineException
  {
    getRuleEngine().resetRuleEngine(); // Reset the target rule engine
    builtInBridgeController.reset();
    swrlProcessor.reset();
    initialize();
  } 

  /**
   * Write knowledge inferred by rule engine back to OWL.
   */
  public void writeInferredKnowledge2OWL() throws SWRLRuleEngineException
  {
  	// Order of creation is important here.
  	writeInjectedOWLClassDeclarations(); // Write any OWL classes generated by built-ins in rules. 
  	writeInjectedOWLIndividualDeclarations(); // Write any OWL individuals generated by built-ins in rules. 
  	writeInjectedOWLAxioms(); // Write any OWL axioms generated by built-ins in rules. 
  	writeInferredOWLIndividualDeclarations();
  	writeInferredOWLAxioms();
  } 

  /**
   * Load rules and knowledge from OWL into bridge, send them to a rule engine, run the rule engine, and write any inferred knowledge back
   * to OWL.
   */
  public void infer() throws SWRLRuleEngineException
  {
    reset();
    importSWRLRulesAndOWLKnowledge();
    run();
    writeInferredKnowledge2OWL();
  } 
  
  public SQWRLResult runSQWRLQuery(String queryName, String queryText) throws SQWRLException, SWRLParseException
  { 
  	createSQWRLQuery(queryName, queryText);
  	return runSQWRLQuery(queryName);
  }
  
  public void createSQWRLQuery(String queryName, String queryText) throws SQWRLException, SWRLParseException
  {
  	try {
  		activeOntology.createSWRLRule(queryName, queryText);
  	} catch (OWLConversionFactoryException e) {
  	  throw new SQWRLException("error creating SQWRL query: " + e.getMessage());
  	} // try
  }

  public SQWRLResult runSQWRLQuery(String queryName) throws SQWRLException
  {
    SQWRLResult result = null;
  	
    try {
      reset();
      importSQWRLQuery(queryName);
      swrlProcessor.importOWLAxioms();
      exportOWLAxioms();
      run();
      result = getSQWRLResult(queryName);
    } catch (SWRLRuleEngineException e) {
      throw new SQWRLException("error running SQWRL queries: " + e.getMessage());
    } // try
    
    return result;
  }

  public void runSQWRLQueries() throws SQWRLException
  {
    try {
      reset();
      swrlProcessor.importOWLAxioms();
      exportOWLAxioms();
      run();
    } catch (SWRLRuleEngineException e) {
      throw new SQWRLException("error running SQWRL queries: " + e.getMessage());
    } // try
  }

  /**
   *  Get the results from a SQWRL query.
   */
  public SQWRLResultImpl getSQWRLResult(String queryURI) throws SQWRLException
  {
  	return swrlProcessor.getSQWRLResult(queryURI);
  }

  public SQWRLResultImpl getSQWRLUnpreparedResult(String queryURI) throws SQWRLException
  {
  	return swrlProcessor.getSQWRLUnpreparedResult(queryURI);
  }

  public SWRLRule getSWRLRule(String ruleURI) throws SWRLRuleEngineException
  {
  	return swrlProcessor.getSWRLRule(ruleURI);
  }

  // Convenience methods to display bridge activity
  public int getNumberOfImportedSWRLRules() { return swrlProcessor.getNumberOfImportedSWRLRules(); }
  public int getNumberOfImportedOWLClasses() { return swrlProcessor.getNumberOfImportedOWLClasses(); }
  public int getNumberOfImportedOWLIndividuals() { return swrlProcessor.getNumberOfImportedOWLIndividuals(); }
  public int getNumberOfImportedOWLAxioms()  { return swrlProcessor.getNumberOfImportedOWLAxioms(); }
  
  public int getNumberOfInferredOWLIndividuals() { return ruleEngineBridgeController.getNumberOfInferredOWLIndividuals(); }
  public int getNumberOfInferredOWLAxioms() { return ruleEngineBridgeController.getNumberOfInferredOWLAxioms(); }

  public int getNumberOfInjectedOWLClasses() { return builtInBridgeController.getNumberOfInjectedOWLClassDeclarations(); }
  public int getNumberOfInjectedOWLIndividuals() { return builtInBridgeController.getNumberOfInjectedOWLIndividualDeclarations(); }
  public int getNumberOfInjectedOWLAxioms() { return builtInBridgeController.getNumberOfInjectedOWLAxioms(); }

  public boolean isInjectedOWLClass(String classURI) { return builtInBridgeController.isInjectedOWLClass(classURI); }
  public boolean isInjectedOWLIndividual(String individualURI) { return builtInBridgeController.isInjectedOWLIndividual(individualURI); }
  public boolean isInjectedOWLAxiom(OWLAxiom axiom) { return builtInBridgeController.isInjectedOWLAxiom(axiom); }

  // Convenience methods to display the contents of the bridge
  public Set<SWRLRule> getImportedSWRLRules() { return swrlProcessor.getImportedSWRLRules(); }
  public Set<OWLClass> getImportedOWLClassDeclarations() { return swrlProcessor.getImportedOWLClassDeclarations(); }
  public Set<OWLNamedIndividual> getImportedOWLIndividualDeclarations() { return swrlProcessor.getImportedOWLIndividualDeclarations(); }
  public Set<OWLAxiom> getImportedOWLAxioms() { return swrlProcessor.getImportedOWLAxioms(); }

  public Set<OWLNamedIndividual> getInferredOWLIndividuals(){ return ruleEngineBridgeController.getInferredOWLIndividuals(); }
  public Set<OWLAxiom> getInferredOWLAxioms() { return ruleEngineBridgeController.getInferredOWLAxioms(); }

  public Set<OWLAxiom> getInjectedOWLAxioms() { return getInjectedOWLAxioms(); }
  public Set<OWLClass> getInjectedOWLClassDeclarations() { return builtInBridgeController.getInjectedOWLClassDeclarations(); }
  public Set<OWLNamedIndividual> getInjectedOWLIndividualDeclarations() { return builtInBridgeController.getInjectedOWLIndividualDeclarations(); }

  public String uri2PrefixedName(String uri)
  {
  	return activeOntology.uri2PrefixedName(uri);
  }
  
  public String name2URI(String prefixedName)
  {
  	return activeOntology.prefixedName2URI(prefixedName);
  }

  public OWLDataFactory getOWLDataFactory() { return activeOWLFactory; }
  public OWLDataValueFactory getOWLDataValueFactory() { return owlDataValueFactory; }

  /**
   * Create OWL classes in model for the classes injected by built-ins during rule execution.
   */
  private void writeInjectedOWLClassDeclarations() throws SWRLRuleEngineException
  {
  	for (OWLClass owlClass: builtInBridgeController.getInjectedOWLClassDeclarations()) {
  		try {
  			activeOntology.writeOWLClassDeclaration(owlClass);
  		} catch (OWLConversionFactoryException e) {
  			throw new SWRLRuleEngineException("error writing injected class " + owlClass + ": " + e.getMessage());
  		} // try
  	} // for
  } 

  /**
   * Create OWL individuals in model for the individuals injected by built-ins during rule execution.
   */
  private void writeInjectedOWLIndividualDeclarations() throws SWRLRuleEngineException
  {
    for (OWLNamedIndividual owlIndividual: builtInBridgeController.getInjectedOWLIndividualDeclarations()) {
    	try {
    		activeOntology.writeOWLIndividualDeclaration(owlIndividual);
  		} catch (OWLConversionFactoryException e) {
  			throw new SWRLRuleEngineException("error writing injected individual " + owlIndividual + ": " + e.getMessage());
  		} // try
    } // for
  } 

  /**
   * Create OWL axioms in model for the axioms injected by built-ins during rule execution.
   */
  private void writeInjectedOWLAxioms() throws SWRLRuleEngineException
  {
    for (OWLAxiom axiom : builtInBridgeController.getInjectedOWLAxioms()) {
    	try {
    		activeOntology.writeOWLAxiom(axiom);
  		} catch (OWLConversionFactoryException e) {
  			throw new SWRLRuleEngineException("error writing injected axiom " + axiom + ": " + e.getMessage());
  		} // try
    } // for
  } 
  
  private void exportSWRLRules() throws SWRLRuleEngineException
  {
    for (SWRLRule rule : swrlProcessor.getImportedSWRLRules()) 
    	getRuleEngine().defineOWLAxiom(rule);
  }

  private void exportOWLClassDeclarations() throws SWRLRuleEngineException
  {
    for (OWLClass owlClass : swrlProcessor.getImportedOWLClassDeclarations()) 
    	exportOWLClass(owlClass);
  }

  private void exportOWLClass(OWLClass owlClass) throws SWRLRuleEngineException
  {
    String classURI = owlClass.getURI();

    if (!exportedOWLClassURIs.contains(classURI)) { // See if it is already defined.
      exportOWLClassDeclaration(owlClass);
      exportedOWLClassURIs.add(classURI);
    } // if
  } 

  private void exportOWLIndividualDeclarations() throws SWRLRuleEngineException
  {
    for (OWLNamedIndividual owlIndividual : swrlProcessor.getImportedOWLIndividualDeclarations()) {
      String individualURI = owlIndividual.getURI();
      if (!exportedOWLIndividualURIs.contains(individualURI)) {
        exportOWLIndividualDeclaration(owlIndividual);
        exportedOWLIndividualURIs.add(individualURI);
      } // if
    } // for
  }

  private void exportOWLAxioms() throws SWRLRuleEngineException
  {
  	exportOWLClassDeclarations(); // Classes should be exported before rules because rules usually use class definitions.
  	exportOWLIndividualDeclarations();
  
    for (OWLAxiom axiom: swrlProcessor.getImportedOWLAxioms()) exportOWLAxiom(axiom);
    
  	exportSWRLRules();
  }

  private void writeInferredOWLIndividualDeclarations() throws SWRLRuleEngineException
  {
    for (OWLNamedIndividual owlIndividual : ruleEngineBridgeController.getInferredOWLIndividuals()) {
    	try {
    		activeOntology.writeOWLIndividualDeclaration(owlIndividual);
  		} catch (OWLConversionFactoryException e) {
  			throw new SWRLRuleEngineException("error writing inferred individual " + owlIndividual + ": " + e.getMessage());
  		} // try
    } // for
  }

  private void writeInferredOWLAxioms() throws SWRLRuleEngineException
  {
    for (OWLAxiom axiom : ruleEngineBridgeController.getInferredOWLAxioms())  {
    	try {
    		activeOntology.writeOWLAxiom(axiom);
  		} catch (OWLConversionFactoryException e) {
  			throw new SWRLRuleEngineException("error writing inferred axiom " + axiom + ": " + e.getMessage());
  		} // try
    } // for
  } 
  
  private void initialize()
  {  
    exportedOWLClassURIs = new HashSet<String>();
    exportedOWLIndividualURIs = new HashSet<String>();
  }
  
  private TargetSWRLRuleEngine getRuleEngine() throws SWRLRuleEngineException
  {
  	if (targetRuleEngine == null) throw new SWRLRuleEngineException("no target rule engine specified");
  	
  	return targetRuleEngine;
  }

  private void exportOWLClassDeclaration(OWLClass owlClass) throws SWRLRuleEngineException
  {
    OWLDeclarationAxiom axiom = activeOWLFactory.getOWLDeclarationAxiom(owlClass);
    exportOWLAxiom(axiom);
  } 

  private void exportOWLIndividualDeclaration(OWLNamedIndividual owlIndividual) throws SWRLRuleEngineException
  {
  	OWLDeclarationAxiom axiom = activeOWLFactory.getOWLDeclarationAxiom(owlIndividual);
    exportOWLAxiom(axiom);
   } 

  private void exportOWLAxiom(OWLAxiom owlAxiom) throws SWRLRuleEngineException
  {
    try {
      getRuleEngine().defineOWLAxiom(owlAxiom);
    } catch (TargetSWRLRuleEngineException e) {
      throw new SWRLRuleEngineException("error exporting OWL axiom " + owlAxiom + " to rule engine: " + e.getMessage());
    } // try
  }
}