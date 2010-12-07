package edu.stanford.smi.protegex.owl.swrl.bridge;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.TargetSWRLRuleEngineException;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLClass;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLClassAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLClassPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLDataPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLDeclarationAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLDifferentIndividualsAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLEntity;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLLiteral;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLNamedIndividual;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLObjectPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLProperty;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLSameIndividualAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLSubClassAxiom;

/**
 * Default implementation of an OWL axiom convertor that takes OWL axioms and converts them to a native rule engine implementation. 
 * This implementation defines some of the standard tasks that will likely be common to many rule engines.   
 * 
 * A target rule engine implementation should implement the abstract methods. As can be seen, the bridge currently only supports a
 * small number of OWL axioms.  
 */
public abstract class DefaultOWLAxiomConvertor extends DefaultConvertor implements OWLAxiomConvertor 
{
  protected abstract void defineOWLClassDeclaration(String classURI) throws TargetSWRLRuleEngineException;
  protected abstract void defineOWLIndividualDeclaration(String individualURI) throws TargetSWRLRuleEngineException;
  protected abstract void defineOWLPropertyDeclaration(String propertyURI) throws TargetSWRLRuleEngineException;
  
  protected abstract void defineOWLClassAssertionAxiom(String classURI, String individualURI) throws TargetSWRLRuleEngineException;
  protected abstract void defineOWLObjectPropertyAssertionAxiom(String propertyURI, String subjectIndividualURI, String objectIndividualURI) 
  	throws TargetSWRLRuleEngineException;
  protected abstract void defineOWLDataPropertyAssertionAxiom(String propertyURI, String subjectIndividualURI, OWLLiteral objectLiteral) 
  	throws TargetSWRLRuleEngineException;

  protected abstract void defineOWLSameIndividualAxiom(String individual1URI, String individual2URI) throws TargetSWRLRuleEngineException;
  protected abstract void defineOWLDifferentIndividualsAxiom(String individual1URI, String individual2URI) throws TargetSWRLRuleEngineException;
  
  protected abstract void defineOWLClassPropertyAssertion(String propertyURI, String subjectIndividualURI, String objectValueClassURI) 
		throws TargetSWRLRuleEngineException;
  protected abstract void defineOWLPropertyPropertyAssertion(String propertyURI, String subjectIndividualURI, String objectValuePropertyURI) 
		throws TargetSWRLRuleEngineException;
  
  public DefaultOWLAxiomConvertor(SWRLRuleEngineBridge bridge) { super(bridge); }
	
  public void generateOWLAxiom(OWLAxiom axiom) throws TargetSWRLRuleEngineException
  {
  	if (axiom instanceof OWLDataPropertyAssertionAxiom) {
  		OWLDataPropertyAssertionAxiom owlDatatypePropertyAssertionAxiom = (OWLDataPropertyAssertionAxiom)axiom;
  		generateOWLDataPropertyAssertionAxiom(owlDatatypePropertyAssertionAxiom);
	    } else if (axiom instanceof OWLObjectPropertyAssertionAxiom) {
	      OWLObjectPropertyAssertionAxiom owlObjectPropertyAssertionAxiom = (OWLObjectPropertyAssertionAxiom)axiom;
	      generateOWLObjectPropertyAssertionAxiom(owlObjectPropertyAssertionAxiom);
	    } else if (axiom instanceof OWLClassPropertyAssertionAxiom) {
	      OWLClassPropertyAssertionAxiom owlClassPropertyAssertionAxiom = (OWLClassPropertyAssertionAxiom)axiom;
	      generateOWLClassPropertyAssertionAxiom(owlClassPropertyAssertionAxiom);
	    } else if (axiom instanceof OWLPropertyPropertyAssertionAxiom) {
	      OWLPropertyPropertyAssertionAxiom owlPropertyPropertyAssertionAxiom = (OWLPropertyPropertyAssertionAxiom)axiom;
	      generateOWLPropertyPropertyAssertionAxiom(owlPropertyPropertyAssertionAxiom);
	    } else if (axiom instanceof OWLSameIndividualAxiom) {
	      OWLSameIndividualAxiom owlSameIndividualAxiom = (OWLSameIndividualAxiom)axiom;
	      generateOWLSameIndividualAxiom(owlSameIndividualAxiom);
	    } else if (axiom instanceof OWLDifferentIndividualsAxiom) {
	      OWLDifferentIndividualsAxiom owlDifferentIndividualsAxiom = (OWLDifferentIndividualsAxiom)axiom;
	      generateOWLDifferentIndividualsAxiom(owlDifferentIndividualsAxiom);
	    } else if (axiom instanceof OWLClassAssertionAxiom) {
	      OWLClassAssertionAxiom owlClassAssertionAxiom = (OWLClassAssertionAxiom)axiom;
	      generateOWLClassAssertionAxiom(owlClassAssertionAxiom);
	    } else if (axiom instanceof OWLDeclarationAxiom) {
	      OWLDeclarationAxiom owlDeclarationAxiom = (OWLDeclarationAxiom)axiom;
	      generateOWLDeclarationAxiom(owlDeclarationAxiom);
	    } else if (axiom instanceof OWLSubClassAxiom) {
	      OWLSubClassAxiom owlSubClassAxiom = (OWLSubClassAxiom)axiom;
	      generateOWLSubclassAxiom(owlSubClassAxiom);
	    } else throw new TargetSWRLRuleEngineException("OWL axiom " + axiom.getClass() + " not supported");
  }
  	
  public void generateOWLClassDeclaration(OWLClass owlClass) throws TargetSWRLRuleEngineException
  {
    Set<String> classURIs = new HashSet<String>();

    classURIs.add(owlClass.getURI()); // For SWRL Full

    for (String classURI : classURIs) {
    	defineOWLClassDeclaration(classURI);
    } // for
  } 

  public void generateOWLPropertyDeclaration(OWLProperty owlProperty) throws TargetSWRLRuleEngineException
  {
    Set<String> propertyURIs = new HashSet<String>();

    propertyURIs.add(owlProperty.getURI());  // For SWRL Full

    for (String propertyURI : propertyURIs) {
    	defineOWLPropertyDeclaration(propertyURI);
    } // for
  } 

  public void generateOWLIndividualDeclaration(OWLNamedIndividual individual) throws TargetSWRLRuleEngineException
  {
    String individualURI = individual.getURI();

    defineOWLIndividualDeclaration(individualURI);
  }
 
  // TODO: Here, we go beyond just declaring the supplied class assertion axiom but also declare the transitive closure of
  // the individual's classes and also the same and different individual axioms for that individual. This should not be 
  // happening here and should be taken care of by the generateOWLSubClassAssertion, generateOWLEquivalentClassAssertion,
  // generateOWLSameIndividualAxiom, and generateOWLDifferentIndividualsAxiom methods. However, the SWRLAndSQWRLProcessor
  // class does not currently generate these axioms.
  public void generateOWLClassAssertionAxiom(OWLClassAssertionAxiom axiom) throws TargetSWRLRuleEngineException
  {
    Set<OWLClass> definingClasses = new HashSet<OWLClass>();
    String individualURI = axiom.getIndividual().getURI();
    
    definingClasses.add(axiom.getDescription());

    calculateTransitiveClassClosure(axiom.getIndividual().getTypes(), definingClasses);
    
    for (OWLClass owlClass : definingClasses) {
      String classURI = owlClass.getURI();
    	defineOWLClassAssertionAxiom(classURI, individualURI);

      for (OWLNamedIndividual sameIndividual : axiom.getIndividual().getSameIndividuals()) {
      	String sameIndividualURI = sameIndividual.getURI();
      	defineOWLSameIndividualAxiom(individualURI, sameIndividualURI);
      	defineOWLClassAssertionAxiom(classURI, sameIndividualURI);
      } // for
      
      for (OWLNamedIndividual differentIndividual : axiom.getIndividual().getDifferentIndividuals()) {
      	String differentIndividualURI = differentIndividual.getURI();
      	defineOWLDifferentIndividualsAxiom(individualURI, differentIndividualURI);
        } // for
    } // for

    defineOWLSameIndividualAxiom(individualURI, individualURI);
  }

  // TODO: Here, we go beyond just declaring the supplied property assertion axiom but also declare the transitive closure of
  // the properties and consider the same as relationships for the subject individuals.
  // This should not be happening here and should be taken care of by the generateOWLSubPropertyAssertion 
  // and generateOWLEquivalentPropertyAssertion methods. However, the SWRLAndSQWRLProcessor class does not currently generate these axioms.
  public void generateOWLDataPropertyAssertionAxiom(OWLDataPropertyAssertionAxiom axiom) throws TargetSWRLRuleEngineException
  {
    OWLProperty property = axiom.getProperty();
    Set<OWLProperty> definingProperties = new HashSet<OWLProperty>();
    OWLNamedIndividual subjectIndividual = axiom.getSubject();
    Set<OWLNamedIndividual> subjectIndividuals = new HashSet<OWLNamedIndividual>();
    OWLLiteral objectLiteral = axiom.getObject();

    definingProperties.add(property);
    calculateTransitivePropertyClosure(property.getSuperProperties(), definingProperties);

    subjectIndividuals.add(subjectIndividual);
    subjectIndividuals.addAll(subjectIndividual.getSameIndividuals());

    for (OWLProperty definingProperty : definingProperties) {
      String propertyURI = definingProperty.getURI();
      for (OWLNamedIndividual owlIndividual : subjectIndividuals) {
	    	String subjectIndividualURI = owlIndividual.getURI();
	    	defineOWLDataPropertyAssertionAxiom(propertyURI, subjectIndividualURI, objectLiteral);
      } // for     
    } /// for      
  } 

  // TODO: Here, we go beyond just declaring the supplied property assertion axiom but also declare the transitive closure of
  // the properties and consider the same as relationships for both subject and object individuals. 
  // This should not be happening here and should be taken care of by the generateOWLSubPropertyAssertion 
  // and generateOWLEquivalentPropertyAssertion methods. However, the SWRLAndSQWRLProcessor class does not currently generate these axioms.
  public void generateOWLObjectPropertyAssertionAxiom(OWLObjectPropertyAssertionAxiom axiom) throws TargetSWRLRuleEngineException
  {
    OWLProperty property = axiom.getProperty();
    Set<OWLProperty> definingProperties = new HashSet<OWLProperty>();
    OWLNamedIndividual subjectIndividual = axiom.getSubject();
    OWLNamedIndividual objectIndividual = axiom.getObject();
    Set<OWLNamedIndividual> subjectIndividuals = new HashSet<OWLNamedIndividual>();
    Set<OWLNamedIndividual> objectIndividuals = new HashSet<OWLNamedIndividual>();

    definingProperties.add(property);
    calculateTransitivePropertyClosure(property.getTypes(), definingProperties);

    subjectIndividuals.add(subjectIndividual);
    subjectIndividuals.addAll(subjectIndividual.getSameIndividuals());

    objectIndividuals.add(objectIndividual);
    objectIndividuals.addAll(objectIndividual.getSameIndividuals());

    for (OWLProperty definingProperty : definingProperties) {
      String propertyURI = definingProperty.getURI();
      for (OWLNamedIndividual owlIndividual1 : subjectIndividuals) {
    	String subjectIndividualURI = owlIndividual1.getURI();
        for (OWLNamedIndividual owlIndividual2 : objectIndividuals) {
          String objectIndividualURI = owlIndividual2.getURI();
          defineOWLObjectPropertyAssertionAxiom(propertyURI, subjectIndividualURI, objectIndividualURI);
        } // for
      } // for
    } // for
  }

  public void generateOWLClassPropertyAssertionAxiom(OWLClassPropertyAssertionAxiom axiom) throws TargetSWRLRuleEngineException
  {
    OWLProperty property = axiom.getProperty();
    Set<OWLProperty> definingProperties = new HashSet<OWLProperty>();
    OWLNamedIndividual subjectIndividual = axiom.getSubject();
    OWLClass objectClass = axiom.getObject();
    Set<OWLNamedIndividual> subjectIndividuals = new HashSet<OWLNamedIndividual>();
    Set<String> objectClassURIs = new HashSet<String>();

    definingProperties.add(property);
    calculateTransitivePropertyClosure(property.getTypes(), definingProperties);

    subjectIndividuals.add(subjectIndividual);
    subjectIndividuals.addAll(subjectIndividual.getSameIndividuals());

    objectClassURIs.add(objectClass.getURI());
    
    for (OWLProperty definingProperty : definingProperties) {
      String propertyURI =definingProperty.getURI();
      for (OWLNamedIndividual owlIndividual : subjectIndividuals) {
	    	String subjectIndividualURI = owlIndividual.getURI();
        for (String objectClassURI : objectClassURIs) {
  	    	defineOWLClassPropertyAssertion(propertyURI, subjectIndividualURI, objectClassURI);
        } // for
      } // for
    } // for
  } 

  public void generateOWLPropertyPropertyAssertionAxiom(OWLPropertyPropertyAssertionAxiom axiom) throws TargetSWRLRuleEngineException
  {
    OWLProperty property = axiom.getProperty();
    Set<OWLProperty> definingProperties = new HashSet<OWLProperty>();
    OWLNamedIndividual subjectIndividual = axiom.getSubject();
    OWLProperty objectProperty = axiom.getObject();
    Set<OWLNamedIndividual> subjectIndividuals = new HashSet<OWLNamedIndividual>();
    Set<String> objectPropertyURIs = new HashSet<String>();

    definingProperties.add(property);
    calculateTransitivePropertyClosure(property.getTypes(), definingProperties);

    subjectIndividuals.add(subjectIndividual);
    subjectIndividuals.addAll(subjectIndividual.getSameIndividuals());

    objectPropertyURIs.add(objectProperty.getURI());

    for (OWLProperty definingProperty : definingProperties) {
      String propertyURI = definingProperty.getURI();
      for (OWLNamedIndividual owlIndividual : subjectIndividuals) {
    	String subjectIndividualURI = owlIndividual.getURI();
        for (String objectPropertyURI : objectPropertyURIs) {
        	defineOWLPropertyPropertyAssertion(propertyURI, subjectIndividualURI, objectPropertyURI);
        } // for
      } // for
    } // for
  }

  public void generateOWLSameIndividualAxiom(OWLSameIndividualAxiom axiom) throws TargetSWRLRuleEngineException
  {
    String individualURI1 = axiom.getIndividual1().getURI();
    String individualURI2 = axiom.getIndividual2().getURI();
    
    defineOWLSameIndividualAxiom(individualURI1, individualURI2);
    defineOWLSameIndividualAxiom(individualURI2, individualURI1);
    defineOWLSameIndividualAxiom(individualURI1, individualURI1);
  } 

  public void generateOWLDifferentIndividualsAxiom(OWLDifferentIndividualsAxiom axiom) throws TargetSWRLRuleEngineException
  {
    List<String> allDifferentsSoFar = new ArrayList<String>();
    
    for (OWLNamedIndividual individual1 : axiom.getIndividuals()) {
      String individualURI1 = individual1.getURI();
      for (String individualURI2 : allDifferentsSoFar) {
      	defineOWLDifferentIndividualsAxiom(individualURI1, individualURI2);
      	defineOWLDifferentIndividualsAxiom(individualURI2, individualURI1);
      } // for
      allDifferentsSoFar.add(individualURI1);
    } // for
  } 

  public void generateOWLDeclarationAxiom(OWLDeclarationAxiom axiom) throws TargetSWRLRuleEngineException
	{
	  OWLEntity owlEntity = axiom.getEntity();
	
	  if (owlEntity instanceof OWLClass) generateOWLClassDeclaration((OWLClass)owlEntity);
	  else if (owlEntity instanceof OWLProperty) generateOWLPropertyDeclaration((OWLProperty)owlEntity);
	  else if (owlEntity instanceof OWLNamedIndividual) generateOWLIndividualDeclaration((OWLNamedIndividual)owlEntity);
	  else throw new TargetSWRLRuleEngineException("OWL declaration axiom with entity " + owlEntity.getClass() + " not supported");
	}

  public void generateOWLSubclassAxiom(OWLSubClassAxiom axiom) throws TargetSWRLRuleEngineException
  {
    OWLClass owlSubClass = axiom.getSubClass();
    OWLClass owlSuperClass = axiom.getSuperClass();

    generateOWLClassDeclaration(owlSubClass);
    generateOWLClassDeclaration(owlSuperClass);
    
    // TODO: we do nothing for the moment because generateOWLClassAssertionAxiomRepresentation effectively takes care of this axiom.
  }
  
  private void calculateTransitiveClassClosure(Set<OWLClass> classes, Set<OWLClass> closure)
  {
		for (OWLClass cls : classes) {
			if (!closure.contains(cls)) {
				closure.add(cls);
				calculateTransitiveClassClosure(cls.getSuperClasses(), closure);
				calculateTransitiveClassClosure(cls.getEquivalentClasses(), closure);
			} // if
		} // for
  } 

  private void calculateTransitivePropertyClosure(Set<OWLProperty> properties, Set<OWLProperty> closure)
  {
		for (OWLProperty property : properties) {
			if (!closure.contains(property)) {
				closure.add(property);
				calculateTransitivePropertyClosure(property.getSuperProperties(), closure);
				calculateTransitivePropertyClosure(property.getEquivalentProperties(), closure);
			} // if
		} // for
  } 
} 
