
package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import java.util.HashSet;
import java.util.Set;

import edu.stanford.smi.protegex.owl.swrl.SWRLRuleEngine;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLDataValueFactory;
import edu.stanford.smi.protegex.owl.swrl.bridge.SWRLBuiltInBridgeController;
import edu.stanford.smi.protegex.owl.swrl.bridge.SWRLAndSQWRLProcessor;
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
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLProperty;
import edu.stanford.smi.protegex.owl.swrl.owlapi.SWRLRule;
import edu.stanford.smi.protegex.owl.swrl.owlapi.impl.OWLDataFactoryImpl;
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
  private SWRLAndSQWRLProcessor swrlAndSQWRLProcessor;
  private TargetSWRLRuleEngine targetRuleEngine;
  private SWRLBuiltInBridgeController builtInBridgeController;
  private SWRLRuleEngineBridgeController ruleEngineBridgeController;
  private OWLDataFactory owlDataFactory;
  private OWLDataValueFactory owlDataValueFactory; 

  // URIs of classes and individuals that have been exported to target rule engine for declaration
  private Set<String> exportedOWLPropertyURIs, exportedOWLIndividualURIs; 
  
  public DefaultSWRLRuleEngine(OWLOntology activeOntology, SWRLAndSQWRLProcessor swrlAndSQWRLProcessor, TargetSWRLRuleEngine targetRuleEngine, 
  		                         SWRLRuleEngineBridgeController ruleEngineBridgeController, SWRLBuiltInBridgeController builtInBridgeController) 
  throws SWRLRuleEngineException
  {
  	this.activeOntology = activeOntology;
    this.swrlAndSQWRLProcessor = swrlAndSQWRLProcessor;
    this.targetRuleEngine = targetRuleEngine;
    this.builtInBridgeController = builtInBridgeController;
    this.ruleEngineBridgeController = ruleEngineBridgeController;

    owlDataFactory = new OWLDataFactoryImpl(activeOntology);
    owlDataValueFactory = OWLDataValueFactory.create();
    
    initialize();   
  }

  /**
   * Load rules and knowledge from OWL into bridge. All existing bridge rules and knowledge will first be cleared and the associated rule
   * engine will be reset.
   */
  public void importSWRLRulesAndOWLKnowledge() throws SWRLRuleEngineException
  {
  	swrlAndSQWRLProcessor.importSWRLRulesAndOWLAxioms();
  	
  	exportSWRLRulesAndOWLAxioms2TargetRuleEngine();
  }

  /**
   * Load named query, all enabled rules, and all relevant knowledge from OWL into bridge. All existing bridge rules and knowledge will 
   * first be cleared and the associated rule engine will be reset.
   */
  public void importSQWRLQueryAndOWLKnowledge(String queryName) throws SWRLRuleEngineException
  {
  	swrlAndSQWRLProcessor.importSQWRLQueryAndOWLAxioms(queryName);
  	
  	exportSWRLRulesAndOWLAxioms2TargetRuleEngine();
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
    swrlAndSQWRLProcessor.reset();
    initialize();
  } 

  /**
   * Write knowledge inferred by rule engine back to OWL.
   */
  public void writeInferredKnowledge2OWL() throws SWRLRuleEngineException
  {
  	// Order of creation is important here.
  	writeInjectedOWLClassDeclarations2ActiveOntology(); // Write any OWL classes generated by built-ins in rules. 
  	writeInjectedOWLIndividualDeclarations2ActiveOntology(); // Write any OWL individuals generated by built-ins in rules. 
  	writeInjectedOWLAxioms2ActiveOntology(); // Write any OWL axioms generated by built-ins in rules. 
  	writeInferredOWLIndividualDeclarations2ActiveOntology();
  	writeInferredOWLAxioms2ActiveOntology();
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

  /**
   * Run a named SQWRL query. SWRL rules will also be executed and any inferences produced by them will be available in the query.
   */
  public SQWRLResult runSQWRLQuery(String queryName) throws SQWRLException
  {
    SQWRLResult result = null;
    
    try {
      reset();
      importSQWRLQueryAndOWLKnowledge(queryName);
      run();
      result = getSQWRLResult(queryName);
    } catch (SWRLRuleEngineException e) {
      throw new SQWRLException("error running SQWRL queries: " + e.getMessage());
    } // try
    
    return result;
  }

  /**
   * Run a named SQWRL query without executing any SWRL rules in ontology.
   */
  public SQWRLResult runStandaloneSQWRLQuery(String queryName) throws SQWRLException
  {
    SQWRLResult result = null;
    
    try {
      reset();
      importSQWRLQuery(queryName);
      run();
      result = getSQWRLResult(queryName);
    } catch (SWRLRuleEngineException e) {
      throw new SQWRLException("error running SQWRL queries: " + e.getMessage());
    } // try
    
    return result;
  }

  /**
   *  Run all SQWRL queries.
   */
  public void runSQWRLQueries() throws SQWRLException
  {
    try {
      reset();
      importSWRLRulesAndOWLKnowledge();
      run();
    } catch (SWRLRuleEngineException e) {
      throw new SQWRLException("error running SQWRL queries: " + e.getMessage());
    } // try
  }

  /**
   *  Get the results of a previously executed SQWRL query.
   */
  public SQWRLResultImpl getSQWRLResult(String queryURI) throws SQWRLException
  {
  	return swrlAndSQWRLProcessor.getSQWRLResult(queryURI);
  }
  
  public void deleteSQWRLQuery(String queryURI) throws SQWRLException
  {
  	try {
  		activeOntology.deleteSWRLRule(queryURI);
  	} catch (OWLConversionFactoryException e) {
  		throw new SQWRLException("error deleting SQWRL query " + queryURI);
  	}
  }

  /**
   * Get all the enabled SQWRL queries in the ontology.
   */
  public Set<SWRLRule> getSQWRLQueries() throws SQWRLException
  {
  	return swrlAndSQWRLProcessor.getSQWRLQueries();
  }
  
  /**
   * Get the names of the SQWRL queries in the ontology.
   */
  public Set<String> getSQWRLQueryNames() throws SQWRLException
  {
  	return swrlAndSQWRLProcessor.getSQWRLQueryNames();
  }

  
  public SQWRLResultImpl getSQWRLUnpreparedResult(String queryURI) throws SQWRLException
  {
  	return swrlAndSQWRLProcessor.getSQWRLUnpreparedResult(queryURI);
  }

  public SWRLRule getSWRLRule(String ruleURI) throws SWRLRuleEngineException
  {
  	return swrlAndSQWRLProcessor.getSWRLRule(ruleURI);
  }

  // Convenience methods to display bridge activity
  public int getNumberOfImportedSWRLRules() { return swrlAndSQWRLProcessor.getNumberOfImportedSWRLRules(); }
  public int getNumberOfImportedOWLClasses() { return swrlAndSQWRLProcessor.getNumberOfImportedOWLClassDeclarations(); }
  public int getNumberOfImportedOWLIndividuals() { return swrlAndSQWRLProcessor.getNumberOfImportedOWLIndividualDeclarations(); }
  public int getNumberOfImportedOWLAxioms()  { return swrlAndSQWRLProcessor.getNumberOfImportedOWLAxioms(); }
  
  public int getNumberOfInferredOWLIndividuals() { return ruleEngineBridgeController.getNumberOfInferredOWLIndividuals(); }
  public int getNumberOfInferredOWLAxioms() { return ruleEngineBridgeController.getNumberOfInferredOWLAxioms(); }

  public int getNumberOfInjectedOWLClasses() { return builtInBridgeController.getNumberOfInjectedOWLClassDeclarations(); }
  public int getNumberOfInjectedOWLIndividuals() { return builtInBridgeController.getNumberOfInjectedOWLIndividualDeclarations(); }
  public int getNumberOfInjectedOWLAxioms() { return builtInBridgeController.getNumberOfInjectedOWLAxioms(); }

  public boolean isInjectedOWLClass(String classURI) { return builtInBridgeController.isInjectedOWLClass(classURI); }
  public boolean isInjectedOWLIndividual(String individualURI) { return builtInBridgeController.isInjectedOWLIndividual(individualURI); }
  public boolean isInjectedOWLAxiom(OWLAxiom axiom) { return builtInBridgeController.isInjectedOWLAxiom(axiom); }

  // Convenience methods to display the contents of the bridge
  public Set<SWRLRule> getImportedSWRLRules() { return swrlAndSQWRLProcessor.getImportedSWRLRules(); }
  public Set<OWLClass> getImportedOWLClasses() { return swrlAndSQWRLProcessor.getImportedOWLClassDeclarations(); }
  public Set<OWLNamedIndividual> getImportedOWLIndividuals() { return swrlAndSQWRLProcessor.getImportedOWLIndividualDeclarations(); }
  public Set<OWLAxiom> getImportedOWLAxioms() { return swrlAndSQWRLProcessor.getImportedOWLAxioms(); }

  public Set<OWLNamedIndividual> getReclassifiedOWLIndividuals(){ return ruleEngineBridgeController.getInferredOWLIndividuals(); }
  public Set<OWLAxiom> getInferredOWLAxioms() { return ruleEngineBridgeController.getInferredOWLAxioms(); }

  public Set<OWLAxiom> getInjectedOWLAxioms() { return getInjectedOWLAxioms(); }
  public Set<OWLClass> getInjectedOWLClasses() { return builtInBridgeController.getInjectedOWLClassDeclarations(); }
  public Set<OWLNamedIndividual> getInjectedOWLIndividuals() { return builtInBridgeController.getInjectedOWLIndividualDeclarations(); }

  public String uri2PrefixedName(String uri)
  {
  	return activeOntology.uri2PrefixedName(uri);
  }
  
  public String name2URI(String prefixedName)
  {
  	return activeOntology.prefixedName2URI(prefixedName);
  }

  public OWLDataFactory getOWLDataFactory() { return owlDataFactory; }
  public OWLDataValueFactory getOWLDataValueFactory() { return owlDataValueFactory; }

  private void importSQWRLQuery(String queryName) throws SWRLRuleEngineException
  {
    try {
      SWRLRule rule = owlDataFactory.getSWRLRule(queryName);
      swrlAndSQWRLProcessor.process(rule);
      swrlAndSQWRLProcessor.importReferencedOWLAxioms();
      exportSQWRLQuery2TargetRuleEngine(queryName);
      exportReferencedOWLAxioms2TargetRuleEngine();
    } catch (OWLFactoryException e) {
    	throw new SWRLRuleEngineException("factory error importing SQWRL query " + queryName + ": " + e.getMessage());
    } // try
  } 
  
  private void exportSWRLRules2TargetRuleEngine() throws SWRLRuleEngineException
  {
    for (SWRLRule rule : swrlAndSQWRLProcessor.getImportedSWRLRules()) 
    	exportSWRLRule2TargetRuleEngine(rule);
  }

  private void exportSWRLRule2TargetRuleEngine(SWRLRule rule) throws SWRLRuleEngineException
  {
    getRuleEngine().defineOWLAxiom(rule);
  }

  private void exportSQWRLQuery2TargetRuleEngine(String queryURI) throws SWRLRuleEngineException
  {
    getRuleEngine().defineOWLAxiom(swrlAndSQWRLProcessor.getSQWRLQuery(queryURI));
  }

  private void exportOWLClassDeclarationAxioms2TargetRuleEngine(Set<OWLClass> axioms) throws SWRLRuleEngineException
  {
    for (OWLClass owlClass : axioms) 
    	exportOWLClass2TargetRuleEngine(owlClass);
  }

  private void exportOWLPropertyDeclarationAxioms2TargetRuleEngine(Set<OWLProperty> axioms) throws SWRLRuleEngineException
  {
    for (OWLProperty owlProperty : axioms) 
    	exportOWLProperty2TargetRuleEngine(owlProperty);
  }

  private void exportOWLClass2TargetRuleEngine(OWLClass owlClass) throws SWRLRuleEngineException
  {
    String classURI = owlClass.getURI();

    if (!exportedOWLPropertyURIs.contains(classURI)) { // See if it is already defined.
      exportOWLClassDeclaration2TargetRuleEngine(owlClass);
      exportedOWLPropertyURIs.add(classURI);
    } // if
  } 

  private void exportOWLProperty2TargetRuleEngine(OWLProperty owlProperty) throws SWRLRuleEngineException
  {
    String propertyURI = owlProperty.getURI();

    if (!exportedOWLPropertyURIs.contains(propertyURI)) { // See if it is already defined.
      exportOWLPropertyDeclaration2TargetRuleEngine(owlProperty);
      exportedOWLPropertyURIs.add(propertyURI);
    } // if
  } 

  private void exportOWLIndividualDeclarationAxioms2TargetRuleEngine(Set<OWLNamedIndividual> individuals) throws SWRLRuleEngineException
  {
    for (OWLNamedIndividual owlIndividual : individuals) {
      String individualURI = owlIndividual.getURI();
      if (!exportedOWLIndividualURIs.contains(individualURI)) {
        exportOWLIndividualDeclaration2TargetRuleEngine(owlIndividual);
        exportedOWLIndividualURIs.add(individualURI);
      } // if
    } // for
  }

  private void exportSWRLRulesAndOWLAxioms2TargetRuleEngine() throws SWRLRuleEngineException
  {
  	exportOWLDeclarationAxioms2TargetRuleEngine();
  	
    for (OWLAxiom axiom: swrlAndSQWRLProcessor.getImportedOWLAxioms()) 
    	exportOWLAxiom2TargetRuleEngine(axiom);
    
  	exportSWRLRules2TargetRuleEngine();
  }

  private void exportReferencedOWLAxioms2TargetRuleEngine() throws SWRLRuleEngineException
  {
  	exportOWLDeclarationAxioms2TargetRuleEngine();  

  	for (OWLAxiom axiom: swrlAndSQWRLProcessor.getImportedOWLAxioms()) 
    	exportOWLAxiom2TargetRuleEngine(axiom);
  }

  private void exportOWLDeclarationAxioms2TargetRuleEngine() throws SWRLRuleEngineException
  {
  	exportOWLClassDeclarationAxioms2TargetRuleEngine(swrlAndSQWRLProcessor.getImportedOWLClassDeclarations());
  	exportOWLPropertyDeclarationAxioms2TargetRuleEngine(swrlAndSQWRLProcessor.getImportedOWLPropertyDeclarations()); 
  	exportOWLIndividualDeclarationAxioms2TargetRuleEngine(swrlAndSQWRLProcessor.getImportedOWLIndividualDeclarations());
  }
  
  private TargetSWRLRuleEngine getRuleEngine() throws SWRLRuleEngineException
  {
  	if (targetRuleEngine == null) throw new SWRLRuleEngineException("no target rule engine specified");
  	
  	return targetRuleEngine;
  }

  private void exportOWLClassDeclaration2TargetRuleEngine(OWLClass owlClass) throws SWRLRuleEngineException
  {
    OWLDeclarationAxiom axiom = owlDataFactory.getOWLDeclarationAxiom(owlClass);
    exportOWLAxiom2TargetRuleEngine(axiom);
  } 

  private void exportOWLPropertyDeclaration2TargetRuleEngine(OWLProperty owlProperty) throws SWRLRuleEngineException
  {
    OWLDeclarationAxiom axiom = owlDataFactory.getOWLDeclarationAxiom(owlProperty);
    exportOWLAxiom2TargetRuleEngine(axiom);
  } 

  private void exportOWLIndividualDeclaration2TargetRuleEngine(OWLNamedIndividual owlIndividual) throws SWRLRuleEngineException
  {
  	OWLDeclarationAxiom axiom = owlDataFactory.getOWLDeclarationAxiom(owlIndividual);
    exportOWLAxiom2TargetRuleEngine(axiom);
   } 

  private void exportOWLAxiom2TargetRuleEngine(OWLAxiom owlAxiom) throws SWRLRuleEngineException
  {
    try {
      getRuleEngine().defineOWLAxiom(owlAxiom);
    } catch (TargetSWRLRuleEngineException e) {
      throw new SWRLRuleEngineException("error exporting OWL axiom " + owlAxiom + " to rule engine: " + e.getMessage());
    } // try
  }

  private void writeInferredOWLIndividualDeclarations2ActiveOntology() throws SWRLRuleEngineException
  {
    for (OWLNamedIndividual owlIndividual : ruleEngineBridgeController.getInferredOWLIndividuals()) {
    	try {
    		activeOntology.writeOWLIndividualDeclaration(owlIndividual);
  		} catch (OWLConversionFactoryException e) {
  			throw new SWRLRuleEngineException("error writing inferred individual " + owlIndividual + ": " + e.getMessage());
  		} // try
    } // for
  }

  private void writeInferredOWLAxioms2ActiveOntology() throws SWRLRuleEngineException
  {
    for (OWLAxiom axiom : ruleEngineBridgeController.getInferredOWLAxioms())  {
    	try {
    		activeOntology.writeOWLAxiom(axiom);
  		} catch (OWLConversionFactoryException e) {
  			throw new SWRLRuleEngineException("error writing inferred axiom " + axiom + ": " + e.getMessage());
  		} // try
    } // for
  } 

  /**
   * Create OWL individuals in model for the individuals injected by built-ins during rule execution.
   */
  private void writeInjectedOWLIndividualDeclarations2ActiveOntology() throws SWRLRuleEngineException
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
  private void writeInjectedOWLAxioms2ActiveOntology() throws SWRLRuleEngineException
  {
    for (OWLAxiom axiom : builtInBridgeController.getInjectedOWLAxioms()) {
    	try {
    		activeOntology.writeOWLAxiom(axiom);
  		} catch (OWLConversionFactoryException e) {
  			throw new SWRLRuleEngineException("error writing injected axiom " + axiom + ": " + e.getMessage());
  		} // try
    } // for
  } 

  /**
   * Create OWL classes in model for the classes injected by built-ins during rule execution.
   */
  private void writeInjectedOWLClassDeclarations2ActiveOntology() throws SWRLRuleEngineException
  {
  	for (OWLClass owlClass: builtInBridgeController.getInjectedOWLClassDeclarations()) {
  		try {
  			activeOntology.writeOWLClassDeclaration(owlClass);
  		} catch (OWLConversionFactoryException e) {
  			throw new SWRLRuleEngineException("error writing injected class " + owlClass + ": " + e.getMessage());
  		} // try
  	} // for
  } 

  private void initialize()
  {  
    exportedOWLPropertyURIs = new HashSet<String>();
    exportedOWLIndividualURIs = new HashSet<String>();
  }
}