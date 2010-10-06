package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.stanford.smi.protegex.owl.swrl.bridge.SWRLAtom;
import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInAtom;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLDataValue;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLDataValueFactory;
import edu.stanford.smi.protegex.owl.swrl.bridge.SWRLBuiltInBridge;
import edu.stanford.smi.protegex.owl.swrl.bridge.SWRLBuiltInBridgeController;
import edu.stanford.smi.protegex.owl.swrl.bridge.SWRLProcessor;
import edu.stanford.smi.protegex.owl.swrl.bridge.SWRLRule;
import edu.stanford.smi.protegex.owl.swrl.bridge.SWRLRuleEngineBridge;
import edu.stanford.smi.protegex.owl.swrl.bridge.SWRLRuleEngineBridgeController;
import edu.stanford.smi.protegex.owl.swrl.bridge.TargetSWRLRuleEngine;
import edu.stanford.smi.protegex.owl.swrl.bridge.builtins.BuiltInLibraryManager;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.BuiltInException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.OWLConversionFactoryException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.SWRLBuiltInBridgeException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.SWRLRuleEngineBridgeException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.TargetSWRLRuleEngineException;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLClass;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLClassAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLDataFactory;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLDataPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLDeclarationAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLEntity;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLNamedIndividual;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLObjectPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLOntology;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLProperty;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLSubClassAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.PrefixManager;
import edu.stanford.smi.protegex.owl.swrl.owlapi.impl.OWLDataFactoryImpl;
import edu.stanford.smi.protegex.owl.swrl.owlapi.impl.PrefixManagerImpl;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.DataValueConversionException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.SQWRLException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.impl.SQWRLResultImpl;

/**
 * Default implementation of a SWRL rule engine and built-in bridge.
 */
public class DefaultSWRLBridge implements SWRLRuleEngineBridge, SWRLBuiltInBridge, SWRLBuiltInBridgeController, SWRLRuleEngineBridgeController 
{
	private TargetSWRLRuleEngine targetRuleEngine;
	private SWRLProcessor swrlProcessor;
	private OWLDataFactory dataFactory;
	private OWLDataFactory injectedOWLFactory;
	private OWLDataValueFactory dataValueFactory;
	private OWLOntology activeOntology;
  private PrefixManager prefixManager;
	
  private Map<String, OWLNamedIndividual> inferredOWLIndividualDeclarations;
  private Set<OWLAxiom> inferredOWLAxioms; 

  private HashMap<String, OWLClass> injectedOWLClassDeclarations;
  private HashMap<String, OWLNamedIndividual> injectedOWLIndividualDeclarations;
  private Set<OWLAxiom> injectedOWLAxioms;

  private Map<String, Map<String, Set<OWLPropertyAssertionAxiom>>> allOWLPropertyAssertionAxioms; // individualURI <propertyURI, axiom>
  private Map<String, OWLNamedIndividual> allOWLIndividualDeclarations; 

	public DefaultSWRLBridge(OWLOntology activeOntology, SWRLProcessor swrlProcessor) throws SWRLBuiltInBridgeException 
	{
		this.activeOntology = activeOntology;
		this.swrlProcessor = swrlProcessor;
		this.targetRuleEngine = null;
      
    dataFactory = new OWLDataFactoryImpl();
    injectedOWLFactory = new OWLDataFactoryImpl();   
    dataValueFactory = OWLDataValueFactory.create();
    prefixManager = new PrefixManagerImpl(activeOntology);
    
    initialize();
      
    reset();
	}
	
	public void reset() throws SWRLBuiltInBridgeException	{ BuiltInLibraryManager.invokeAllBuiltInLibrariesResetMethod(this); }
	
  public void setTargetRuleEngine(TargetSWRLRuleEngine targetRuleEngine) { this.targetRuleEngine = targetRuleEngine; }
	
  public int getNumberOfInjectedOWLClassDeclarations() { return injectedOWLClassDeclarations.size(); }
  public int getNumberOfInjectedOWLIndividualDeclarations() { return injectedOWLIndividualDeclarations.size(); }
  public int getNumberOfInjectedOWLAxioms() { return injectedOWLAxioms.size(); }

  public boolean isInjectedOWLClass(String classURI) { return injectedOWLClassDeclarations.containsKey(classURI); }
  public boolean isInjectedOWLIndividual(String individualURI) { return injectedOWLIndividualDeclarations.containsKey(individualURI); }
  public boolean isInjectedOWLAxiom(OWLAxiom axiom) { return injectedOWLAxioms.contains(axiom); }

  public Set<OWLClass> getInjectedOWLClassDeclarations() { return new HashSet<OWLClass>(injectedOWLClassDeclarations.values()); }
  public Set<OWLNamedIndividual> getInjectedOWLIndividualDeclarations() { return new HashSet<OWLNamedIndividual>(injectedOWLIndividualDeclarations.values()); }
  public Set<OWLAxiom> getInjectedOWLAxioms() { return injectedOWLAxioms; }

  public Set<OWLAxiom> getInferredOWLAxioms() { return inferredOWLAxioms; }
  public Set<OWLNamedIndividual> getInferredOWLIndividuals() { return new HashSet<OWLNamedIndividual>(inferredOWLIndividualDeclarations.values()); }
  public int getNumberOfInferredOWLAxioms() { return inferredOWLAxioms.size(); }
  public int getNumberOfInferredOWLIndividuals() { return inferredOWLIndividualDeclarations.size(); }
	public boolean isSQWRLQuery(SWRLRule query ) { return swrlProcessor.isSQWRLQuery(query.getURI()); }
	
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
  }
  
  private void inferOWLIndividual(OWLNamedIndividual owlIndividual, OWLClass owlClass) throws SWRLRuleEngineBridgeException 
  {
    String individualURI = owlIndividual.getURI();

    if (inferredOWLIndividualDeclarations.containsKey(individualURI)) inferredOWLIndividualDeclarations.get(individualURI).addType(owlClass);
    else if (injectedOWLIndividualDeclarations.containsKey(individualURI)) injectedOWLIndividualDeclarations.get(individualURI).addType(owlClass);
    else {
      inferredOWLIndividualDeclarations.put(individualURI, owlIndividual); 
      cacheOWLIndividual(owlIndividual);
    } // if
  } 

  public PrefixManager getPrefixManager() { return prefixManager; }

	public boolean invokeSWRLBuiltIn(String ruleName, String builtInName, int builtInIndex, boolean isInConsequent, List<BuiltInArgument> arguments)
	  throws BuiltInException
	{ 
	  return BuiltInLibraryManager.invokeSWRLBuiltIn(targetRuleEngine, this, ruleName, builtInName, builtInIndex, isInConsequent, arguments); 
 	}
			
	public List<BuiltInAtom> getBuiltInAtomsFromBody(SWRLRule ruleOrQuery, Set<String> builtInNames) 
    { return swrlProcessor.getBuiltInAtomsFromBody(ruleOrQuery, builtInNames); }
	public List<BuiltInAtom> getBuiltInAtomsFromHead(SWRLRule ruleOrQuery, Set<String> builtInNames) 
    { return swrlProcessor.getBuiltInAtomsFromHead(ruleOrQuery, builtInNames); }
	  
  // The inject methods can be used by built-ins to inject new axioms into a bridge, which will also reflect them in the underlying
  // engine. Eventually collapse all inject methods into injectOWLAxiom.
  public void injectOWLAxiom(OWLAxiom axiom) throws SWRLBuiltInBridgeException
  {
    if (!injectedOWLAxioms.contains(axiom)) {
      injectedOWLAxioms.add(axiom);

      if (axiom instanceof OWLPropertyAssertionAxiom) cacheOWLPropertyAssertionAxiom((OWLPropertyAssertionAxiom)axiom);
      else if (axiom instanceof OWLDeclarationAxiom) injectOWLDeclarationAxiom((OWLDeclarationAxiom)axiom);
      else if (axiom instanceof OWLSubClassAxiom) injectOWLSubClassAxiom((OWLSubClassAxiom)axiom);

      exportOWLAxiom(axiom); // Export the axiom to the rule engine.
    } // if
  }

  public String uri2PrefixedName(String uri)
  {
  	return activeOntology.uri2PrefixedName(uri);
  }
  
  public String name2URI(String prefixedName)
  {
  	return activeOntology.prefixedName2URI(prefixedName);
  }

  private void injectOWLDeclarationAxiom(OWLDeclarationAxiom axiom) throws SWRLBuiltInBridgeException
  {
    OWLEntity owlEntity = axiom.getEntity();

    if (owlEntity instanceof OWLClass) injectOWLClassDeclaration(owlEntity.getURI());
    else if (owlEntity instanceof OWLNamedIndividual) injectOWLIndividualDeclaration((OWLNamedIndividual)owlEntity);
    else throw new SWRLBuiltInBridgeException("error injecting OWLDeclarationAxiom - unknown entity type: " + owlEntity.getClass());
  }

  public OWLClass injectOWLClassDeclaration() throws SWRLBuiltInBridgeException
  {
    OWLClass owlClass = activeOntology.createOWLClass();
    String classURI = owlClass.getURI();

    if (!injectedOWLClassDeclarations.containsKey(classURI)) {
      injectedOWLClassDeclarations.put(classURI, owlClass);
      exportOWLClassDeclaration(owlClass); // Export the class to the rule engine
    } // if

    return owlClass;
  }

  public void injectOWLClassDeclaration(String classURI) throws SWRLBuiltInBridgeException
  {
    checkOWLClassURI(classURI);

    if (!injectedOWLClassDeclarations.containsKey(classURI)) {
      OWLClass owlClass = injectedOWLFactory.getOWLClass(classURI);
      injectedOWLClassDeclarations.put(classURI, owlClass);
      exportOWLClassDeclaration(owlClass); // Export the individual to the rule engine
    } // if
  }

  private void injectOWLSubClassAxiom(OWLSubClassAxiom axiom) throws SWRLBuiltInBridgeException
  {
    String subclassURI = axiom.getSubClass().getURI();
    String superclassURI = axiom.getSuperClass().getURI();
    injectedOWLFactory.getOWLClass(subclassURI);
    injectedOWLFactory.getOWLClass(superclassURI);

    injectedOWLClassDeclarations.put(subclassURI, axiom.getSubClass());
    injectedOWLClassDeclarations.put(superclassURI, axiom.getSuperClass());
  }

  /**
   * Method used to inject a bridge individual of type owl:Thing. This method will typically be invoked from within a built-in. An OWL
   * individual is not injected at this point - instead an object is generated for the individual in the bridge and the individual is
   * exported to the rule engine. The individual is given a unique name that can be used later if an OWL individual is injected for it.
   */
  public OWLNamedIndividual injectOWLIndividualDeclaration() throws SWRLBuiltInBridgeException
  {
    String individualURI = activeOntology.createNewResourceURI("SWRLInjected");
    OWLClass owlClass = injectedOWLFactory.getOWLClass(edu.stanford.smi.protegex.owl.model.OWLNames.Cls.THING);
    OWLNamedIndividual owlIndividual = injectedOWLFactory.getOWLIndividual(individualURI);
    owlIndividual.addType(owlClass);
    injectOWLIndividualDeclaration(owlIndividual);
    return owlIndividual;
  }
    
  public void injectOWLIndividualDeclaration(OWLNamedIndividual owlIndividual) throws SWRLBuiltInBridgeException
  {
    if (!injectedOWLIndividualDeclarations.containsKey(owlIndividual.getURI())) {
      injectedOWLIndividualDeclarations.put(owlIndividual.getURI(), owlIndividual); 
      cacheOWLIndividual(owlIndividual);
      exportOWLIndividualDeclaration(owlIndividual); // Export the individual to the rule engine.
    } // if
  } 

  public OWLNamedIndividual injectOWLIndividualDeclaration(OWLClass owlClass) throws SWRLBuiltInBridgeException
  {
    String individualURI = activeOntology.createNewResourceURI("SWRLInjected");
    OWLNamedIndividual owlIndividual = injectedOWLFactory.getOWLIndividual(individualURI);
    owlIndividual.addType(owlClass);

    if (!swrlProcessor.isImportedOWLClass(owlClass.getURI())) exportOWLClassDeclaration(owlClass);
   
    injectedOWLIndividualDeclarations.put(individualURI, owlIndividual); 
    cacheOWLIndividual(owlIndividual);
    exportOWLIndividualDeclaration(owlIndividual); // Export the individual to the rule engine.

    return owlIndividual;
  } 
  
  public OWLDataPropertyAssertionAxiom injectOWLDataPropertyAssertionAxiom(OWLNamedIndividual subject, OWLProperty property,
                                                                           OWLDataValue object) 
    throws SWRLBuiltInBridgeException
  {
    OWLDataPropertyAssertionAxiom axiom = injectedOWLFactory.getOWLDataPropertyAssertionAxiom(subject, property, object);
    injectOWLDatatypePropertyAssertionAxiom(axiom);
    return axiom;
  }

  public void injectOWLDatatypePropertyAssertionAxiom(OWLDataPropertyAssertionAxiom axiom) 
    throws SWRLBuiltInBridgeException
  {
    if (!injectedOWLAxioms.contains(axiom)) {
      injectedOWLAxioms.add(axiom);
      cacheOWLPropertyAssertionAxiom(axiom);
    } // if

    exportOWLAxiom(axiom); // Export the axiom to the rule engine.
  }

  public void injectOWLDataPropertyAssertionAxioms(Set<OWLDataPropertyAssertionAxiom> axioms)
    throws SWRLBuiltInBridgeException
  {
    for (OWLDataPropertyAssertionAxiom axiom : axioms) injectOWLDatatypePropertyAssertionAxiom(axiom);
  }

  public void injectOWLObjectPropertyAssertionAxiom(OWLObjectPropertyAssertionAxiom axiom)
    throws SWRLBuiltInBridgeException
  {
    if (!injectedOWLAxioms.contains(axiom)) {
      injectedOWLAxioms.add(axiom);
      cacheOWLPropertyAssertionAxiom(axiom);
    } // if

    exportOWLAxiom(axiom); // Export the axiom to the rule engine.
  }

  public boolean isOWLClass(String classURI) 
  { 
	 return swrlProcessor.isImportedOWLClass(classURI) || injectedOWLClassDeclarations.containsKey(classURI) ||
	        activeOntology.containsClassInSignature(classURI, true);
  }
  
  public boolean isOWLObjectProperty(String propertyURI) 
  { 
	  return swrlProcessor.isImportedOWLObjectProperty(propertyURI) || activeOntology.containsObjectPropertyInSignature(propertyURI, true);
  }
  
  public boolean isOWLDataProperty(String propertyURI) 
  { 
	  return swrlProcessor.isImportedOWLDataProperty(propertyURI) || activeOntology.containsDataPropertyInSignature(propertyURI, true);
  }

  public boolean isOWLIndividual(String individualURI) 
  { 
	  return swrlProcessor.isImportedOWLIndividual(individualURI) || activeOntology.containsIndividualInSignature(individualURI, true);
  }
  
  public boolean isOWLIndividualOfClass(String individualURI, String classURI)
  {
    boolean result = false; 
    
    if (allOWLIndividualDeclarations.containsKey(individualURI)) {
      OWLNamedIndividual owlIndividual = allOWLIndividualDeclarations.get(individualURI);
      result = owlIndividual.hasType(classURI);
    } // if

    if (!result) result = activeOntology.isOWLNamedIndividualOfClass(individualURI, classURI);

    return result;
  } 
  
  public Set<OWLNamedIndividual> getOWLIndividuals() { return new HashSet<OWLNamedIndividual>(allOWLIndividualDeclarations.values()); }

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
	  	  result = activeOntology.getOWLPropertyAssertionAxioms(individualURI, propertyURI);
	  	} catch (DataValueConversionException e) {
	    	  throw new SWRLBuiltInBridgeException("error getting property assertion axiom for individual " + individualURI + 
	    			                                   ", property " + propertyURI + ": " + e.getMessage(), e); 
	      } catch (OWLConversionFactoryException e) {
	    	  throw new SWRLBuiltInBridgeException("error getting property assertion axiom for individual " + individualURI + 
	   			                                     ", property " + propertyURI + ": " + e.getMessage(), e);
	      } // try
	      return result;
	  } // if
  } 

  public OWLOntology getActiveOntology() { return activeOntology; }
  public OWLDataFactory getOWLDataFactory() { return dataFactory; }
  public OWLDataValueFactory getOWLDataValueFactory() { return dataValueFactory; }

  public boolean isSQWRLQuery(String uri) { return swrlProcessor.isSQWRLQuery(uri); }
  public SQWRLResultImpl getSQWRLResult(String uri) throws SQWRLException { return swrlProcessor.getSQWRLResult(uri); }
  public SQWRLResultImpl getSQWRLUnpreparedResult(String uri) throws SQWRLException { return swrlProcessor.getSQWRLUnpreparedResult(uri); }
  public List<SWRLAtom> getSQWRLPhase1BodyAtoms(SWRLRule query) { return swrlProcessor.getSQWRLPhase1BodyAtoms(query); }
  public List<SWRLAtom> getSQWRLPhase2BodyAtoms(SWRLRule query) { return swrlProcessor.getSQWRLPhase2BodyAtoms(query); }
  public boolean usesSQWRLCollections(SWRLRule query) { return swrlProcessor.usesSQWRLCollections(query); }

  private void initialize()
  {  
    inferredOWLIndividualDeclarations = new HashMap<String, OWLNamedIndividual>(); 
    inferredOWLAxioms = new HashSet<OWLAxiom>(); 

    allOWLPropertyAssertionAxioms = new HashMap<String, Map<String, Set<OWLPropertyAssertionAxiom>>>();
    allOWLIndividualDeclarations = new HashMap<String, OWLNamedIndividual>();

    injectedOWLClassDeclarations = new HashMap<String, OWLClass>();
    injectedOWLIndividualDeclarations = new HashMap<String, OWLNamedIndividual>();
    injectedOWLAxioms = new HashSet<OWLAxiom>();
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

  public void cacheOWLIndividual(OWLNamedIndividual owlIndividual)
  {
    String individualURI = owlIndividual.getURI();

    if (!allOWLIndividualDeclarations.containsKey(individualURI)) allOWLIndividualDeclarations.put(individualURI, owlIndividual);
  }  

  public void cacheOWLIndividuals(Set<OWLNamedIndividual> individuals)
  {
    for (OWLNamedIndividual individual: individuals) cacheOWLIndividual(individual);
  }

  private void checkOWLClassURI(String classURI) throws SWRLBuiltInBridgeException
  {
    if (!activeOntology.isValidURI(classURI))
    	throw new SWRLBuiltInBridgeException("attempt to inject class with invalid URI " + classURI);
  }

  private void exportOWLClassDeclaration(OWLClass owlClass) throws SWRLBuiltInBridgeException
  {
    OWLDeclarationAxiom axiom = dataFactory.getOWLDeclarationAxiom(owlClass);
    exportOWLAxiom(axiom);
  } 

  private void exportOWLIndividualDeclaration(OWLNamedIndividual owlIndividual) throws SWRLBuiltInBridgeException
  {
  	OWLDeclarationAxiom axiom = dataFactory.getOWLDeclarationAxiom(owlIndividual);
    exportOWLAxiom(axiom);
   } 

  private void exportOWLAxiom(OWLAxiom owlAxiom) throws SWRLBuiltInBridgeException
  {
    try {
      targetRuleEngine.defineOWLAxiom(owlAxiom);
    } catch (TargetSWRLRuleEngineException e) {
      throw new SWRLBuiltInBridgeException("error exporting OWL axiom to target rule engine " + owlAxiom + ": " + e.getMessage());
    } // try
  }
}