
// TODO: very long - needs serious refactoring

package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInAtom;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLClass;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLClassAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLClassPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLConversionFactory;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLDataFactory;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLDataProperty;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLDataPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLDataValue;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLDeclarationAxiom;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLDifferentIndividualsAxiom;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLEntity;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLIndividual;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLObjectPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLProperty;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLPropertyPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLSameIndividualAxiom;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLSomeValuesFrom;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLSubClassAxiom;
import edu.stanford.smi.protegex.owl.swrl.bridge.SWRLRule;
import edu.stanford.smi.protegex.owl.swrl.bridge.VariableAtomArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.VariableBuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.BuiltInException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.DataValueConversionException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.OWLConversionFactoryException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.OWLFactoryException;
import edu.stanford.smi.protegex.owl.swrl.bridge.xsd.XSDType;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.SQWRLException;

public class OWLDataFactoryImpl implements OWLDataFactory
{
  private Map<String, OWLClass> classes = new HashMap<String, OWLClass>();
  private Map<String, OWLIndividual> individuals = new HashMap<String, OWLIndividual>();
  private Map<String, OWLObjectProperty> objectProperties = new HashMap<String, OWLObjectProperty>();
  private Map<String, OWLDataProperty> dataProperties = new HashMap<String, OWLDataProperty>();

  private OWLModel owlModel;
  private OWLConversionFactory conversionFactory;

  public OWLDataFactoryImpl() { owlModel = null; }

  public OWLDataFactoryImpl(OWLModel owlModel) 
  { 
    this.owlModel = owlModel; 
    conversionFactory = new OWLConversionFactoryImpl(owlModel, this);
  } // OWLFactoryImpl

  public Set<SWRLRule> getSWRLRules() throws OWLFactoryException
  {
    Set<SWRLRule> result = new HashSet<SWRLRule>();
    
    if (hasOWLModel()) {
      try {
        result = conversionFactory.getSWRLRules();
      } catch (OWLConversionFactoryException e) {
    	  throw new OWLFactoryException("conversion exception getting SWRL rule or SQWRL query: " + e.getMessage());
      } catch (SQWRLException e) {
    	  throw new OWLFactoryException("SQWRL exception getting query: " + e.getMessage());
      } catch (BuiltInException e) {
    	  throw new OWLFactoryException("built-in exception getting SWRL rule or SQWRL query: " + e.getMessage());      
      } // try
    } // if

    return result;
  } // getSWRLRules

  public SWRLRule getSWRLRule(String ruleName) throws OWLFactoryException
  {
    SWRLRule result = null;
    
    if (hasOWLModel()) {
      try {
        result = conversionFactory.getSWRLRule(ruleName);
      } catch (OWLConversionFactoryException e) {
    	  throw new OWLFactoryException("conversion exception getting SWRL rule or SQWRL query: " + e.getMessage());
      } catch (SQWRLException e) {
    	  throw new OWLFactoryException("SQWRL exception getting query: " + e.getMessage());
      } catch (BuiltInException e) {
    	  throw new OWLFactoryException("built-in exception getting SWRL rule or SQWRL query: " + e.getMessage());      
      } // try
    } // if

    return result;
  } // getSWRLRule

  // Basic OWL entities

  public OWLClass getOWLClass() 
  {
    return conversionFactory.getOWLClass();
  } // getOWLClass

  public OWLClass getOWLClass(String classURI)
  { 
    OWLClass owlClass = null;
    
    if (classes.containsKey(classURI)) owlClass = classes.get(classURI);
    else {
      if (hasOWLModel()) {
        try {
          owlClass = conversionFactory.getOWLClass(classURI);
        } catch (OWLConversionFactoryException e) {
          owlClass = new OWLClassImpl(classURI); 
        } // try
      } else owlClass = new OWLClassImpl(classURI); 
      classes.put(classURI, owlClass);
    } // if
    
    return owlClass; 
  } // getOWLClass
    
  public OWLIndividual getOWLIndividual(String individualURI)
  {
    OWLIndividual owlIndividual = null;
    
    if (individuals.containsKey(individualURI)) owlIndividual = individuals.get(individualURI);
    else {
      if (hasOWLModel()) {
        try {
          owlIndividual = conversionFactory.getOWLIndividual(individualURI);
        } catch (OWLConversionFactoryException e) {
          owlIndividual = new OWLIndividualImpl(individualURI); 
        } // try
      } else owlIndividual = new OWLIndividualImpl(individualURI); 
      individuals.put(individualURI, owlIndividual);
    } // if
    
    return owlIndividual; 
  } // getOWLIndividual

  public OWLObjectProperty getOWLObjectProperty(String propertyURI)
  {
    OWLObjectProperty property;
    
    if (objectProperties.containsKey(propertyURI)) property = objectProperties.get(propertyURI);
    else {
      if (hasOWLModel()) {
        try {
          property = conversionFactory.getOWLObjectProperty(propertyURI);
        } catch (OWLConversionFactoryException e) {
          property = new OWLObjectPropertyImpl(propertyURI); 
        } // try
      } else property = new OWLObjectPropertyImpl(propertyURI); 
      objectProperties.put(propertyURI, property);
    } // if
    
    return property; 
  } // getOWLObjectProperty

  public OWLDataProperty getOWLDataProperty(String propertyURI)
  {
    OWLDataProperty property;
    
    if (dataProperties.containsKey(propertyURI)) property = dataProperties.get(propertyURI);
    else {
      if (hasOWLModel()) {
        try {
          property = conversionFactory.getOWLDataProperty(propertyURI);
        } catch (OWLConversionFactoryException e) {
          property = new OWLDataPropertyImpl(propertyURI); 
        } // try
      } else property = new OWLDataPropertyImpl(propertyURI); 
      dataProperties.put(propertyURI, property);
    } // if
    
    return property; 
  } // getOWLDataProperty

  public OWLDataValue getOWLDataValue(String s) { return new OWLDataValueImpl(s); }
  public OWLDataValue getOWLDataValue(Number n) { return new OWLDataValueImpl(n); }
  public OWLDataValue getOWLDataValue(boolean b){ return new OWLDataValueImpl(b); }
  public OWLDataValue getOWLDataValue(Boolean b){ return new OWLDataValueImpl(b); }
  public OWLDataValue getOWLDataValue(int i) { return new OWLDataValueImpl(i); }
  public OWLDataValue getOWLDataValue(long l) { return new OWLDataValueImpl(l); }
  public OWLDataValue getOWLDataValue(float f) { return new OWLDataValueImpl(f); }
  public OWLDataValue getOWLDataValue(double d){ return new OWLDataValueImpl(d); }
  public OWLDataValue getOWLDataValue(short s) { return new OWLDataValueImpl(s); }
  public OWLDataValue getOWLDataValue(Byte b) { return new OWLDataValueImpl(b); }
  public OWLDataValue getOWLDataValue(XSDType xsd) { return new OWLDataValueImpl(xsd); }
  public OWLDataValue getOWLDataValue(Object o) throws DataValueConversionException { return new OWLDataValueImpl(o); } 

  // OWL axioms
  public OWLDataPropertyAssertionAxiom getOWLDataPropertyAssertionAxiom(OWLIndividual subject, OWLProperty property, OWLDataValue object) { return new OWLDatatypePropertyAssertionAxiomImpl(subject, property, object); }
  public OWLObjectPropertyAssertionAxiom getOWLObjectPropertyAssertionAxiom(OWLIndividual subject, OWLProperty property, OWLIndividual object)  { return new OWLObjectPropertyAssertionAxiomImpl(subject, property, object); }
  public OWLDifferentIndividualsAxiom getOWLDifferentIndividualsAxiom(OWLIndividual individual1, OWLIndividual individual2) { return new OWLDifferentIndividualsAxiomImpl(individual1, individual2); }
  public OWLDifferentIndividualsAxiom getOWLDifferentIndividualsAxiom(Set<OWLIndividual> individuals) { return new OWLDifferentIndividualsAxiomImpl(individuals); }
  public OWLSameIndividualAxiom getOWLSameIndividualAxiom(OWLIndividual individual1, OWLIndividual individual2) { return new OWLSameIndividualAxiomImpl(individual1, individual2); }
  public OWLClassPropertyAssertionAxiom getOWLClassPropertyAssertionAxiom(OWLIndividual subject, OWLProperty property, OWLClass object)  { return new OWLClassPropertyAssertionAxiomImpl(subject, property, object); } // OWL Full
  public OWLPropertyPropertyAssertionAxiom getOWLPropertyPropertyAssertionAxiom(OWLIndividual subject, OWLProperty property, OWLProperty object)  { return new OWLPropertyPropertyAssertionAxiomImpl(subject, property, object); } // OWL Full
  public OWLClassAssertionAxiom getOWLClassAssertionAxiom(OWLIndividual individual, OWLClass description)  { return new OWLClassAssertionAxiomImpl(individual, description); } // TODO: should be OWLDescription
  public OWLSubClassAxiom getOWLSubClassAxiom(OWLClass subClass, OWLClass superClass)  { return new OWLSubClassAxiomImpl(subClass, superClass); } // TODO: should be OWLDescription

  public OWLDeclarationAxiom getOWLDeclarationAxiom(OWLEntity owlEntity) { return new OWLDeclarationAxiomImpl(owlEntity); }
  public OWLSomeValuesFrom getOWLSomeValuesFrom(OWLClass owlClass, OWLProperty onProperty, OWLClass someValuesFrom)  { return new OWLSomeValuesFromImpl(owlClass, onProperty, someValuesFrom); } // TODO: should be OWLDescription

  // Arguments to atoms and built-ins
  public VariableAtomArgument getSWRLVariableAtomArgument(String variableName) { return new VariableAtomArgumentImpl(variableName); }
  public VariableBuiltInArgument getSWRLVariableBuiltInArgument(String variableName) { return new VariableBuiltInArgumentImpl(variableName); }
  public BuiltInArgument getSWRLBuiltInArgument(String variableName) { return new BuiltInArgumentImpl(variableName); }

  public BuiltInAtom getSWRLBuiltInAtom(String builtInURI, String builtInPrefixedName, List<BuiltInArgument> arguments) 
  { 
    return new BuiltInAtomImpl(builtInURI, builtInPrefixedName, arguments); 
  } // getSWRLBuiltInAtom

  private boolean hasOWLModel() { return owlModel != null; }

} // OWLDataFactoryImpl
