
package edu.stanford.smi.protegex.owl.swrl.util;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.hp.hpl.jena.util.FileUtils;

import edu.stanford.smi.protege.util.URIUtilities;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.OWLClass;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.OWLProperty;
import edu.stanford.smi.protegex.owl.model.OWLSomeValuesFrom;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.RDFSDatatype;
import edu.stanford.smi.protegex.owl.model.RDFSLiteral;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.model.classparser.AmbiguousNameException;
import edu.stanford.smi.protegex.owl.model.classparser.OWLClassParseException;
import edu.stanford.smi.protegex.owl.model.classparser.OWLClassParser;
import edu.stanford.smi.protegex.owl.model.classparser.ParserUtils;
import edu.stanford.smi.protegex.owl.model.impl.OWLUtil;
import edu.stanford.smi.protegex.owl.model.util.ImportHelper;
import edu.stanford.smi.protegex.owl.swrl.exceptions.SWRLOWLUtilException;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable;

/**
 * Class that wraps some common Protege-OWL API methods and throws meaningful exceptions when errors are encountered.
 *
 * Covers a fairly arbitrary method set.
 */
public class SWRLOWLUtil
{
  public static JenaOWLModel createJenaOWLModel(String owlFileName) throws SWRLOWLUtilException
  {
    JenaOWLModel owlModel = null;

    try {
      owlModel = ProtegeOWL.createJenaOWLModelFromURI(new File(owlFileName).toURI().toString());
    } catch (Exception e) {
      throwException("error opening OWL file " + owlFileName + ": " + e.getMessage());
    } // try

    return owlModel;
  }  // createJenaOWLModel

  public static JenaOWLModel createJenaOWLModel() throws SWRLOWLUtilException
  {
    JenaOWLModel owlModel = null;

    try {
      owlModel = ProtegeOWL.createJenaOWLModel();
    } catch (Exception e) {
      throw new SWRLOWLUtilException("error creating Jena OWL model: " + e.getMessage());
    } // try

    return owlModel;
  } // createJenaOWLModel

  public static void importOWLFile(JenaOWLModel owlModel, String importOWLFileName) throws SWRLOWLUtilException
  {
    try {
      ImportHelper importHelper = new ImportHelper(owlModel);
      URI importUri = URIUtilities.createURI(new File(importOWLFileName).toURI().toString());
      importHelper.addImport(importUri);
      importHelper.importOntologies(false);
    } catch (Exception e) {
      throwException("error importing OWL file " + importOWLFileName + ": " + e.getMessage());
    } // try
  } // importOWLFile

  public static void setPrefix(OWLModel owlModel, String prefix, String namespace) throws SWRLOWLUtilException
  {
    try {
      owlModel.getNamespaceManager().setPrefix(new URI(namespace), prefix);
    } catch (URISyntaxException e) {
      throwException("error setting prefix " + prefix + " for namespace " + namespace + ": " + e.getMessage());
    } // try
  } // setPrefix

  public static void writeJenaOWLModel2File(JenaOWLModel owlModel, String outputOWLFileName) throws SWRLOWLUtilException
  {
    ArrayList errors = new ArrayList();
    URI outputURI = URIUtilities.createURI(new File(outputOWLFileName).toURI().toString());
    owlModel.save(outputURI, FileUtils.langXMLAbbrev, errors);
    if (errors.size() != 0) throwException("error creating output OWL file " + outputOWLFileName + ": " + errors);
  } // writeJenaOWLModel2File
  
  public static OWLNamedClass createOWLNamedClass(OWLModel owlModel, String className) throws SWRLOWLUtilException
  {
    RDFResource resource;
    OWLNamedClass cls;

    checkIfIsValidClassName(owlModel, className);
   
    resource = owlModel.getRDFResource(className);
    
    if (resource != null) {
      if (resource instanceof OWLNamedClass) cls = (OWLNamedClass)resource;
      else throw new SWRLOWLUtilException("class " + className + " is not an OWL named class");
    } else cls = owlModel.createOWLNamedClass(className);

    return cls;
  } // createOWLNamedClass

  public static OWLIndividual createOWLIndividual(OWLModel owlModel, String individualURI) throws SWRLOWLUtilException
  {
    OWLIndividual individual = owlModel.getOWLIndividual(individualURI);

    if (individual == null) individual = createIndividual(owlModel, individualURI);

    return individual;
  } // createOWLIndividual

  public static RDFSNamedClass createRDFSNamedClass(OWLModel owlModel, String className) 
  {
    RDFSNamedClass cls;

    cls = owlModel.getRDFSNamedClass(className);

    if (cls == null) cls = owlModel.createRDFSNamedClass(className);

    return cls;
  } // createRDFSNamedClass

  public static OWLNamedClass createOWLNamedClass(OWLModel owlModel) 
  {
    return owlModel.createOWLNamedClass(null);
  } // createOWLNamedClass

  public static OWLObjectProperty createOWLObjectProperty(OWLModel owlModel, String propertyName) 
  {
    OWLObjectProperty property = owlModel.getOWLObjectProperty(propertyName);

    if (property == null) property = owlModel.createOWLObjectProperty(propertyName);

    return property;
  } // createOWLObjectProperty

  public static OWLObjectProperty createOWLObjectProperty(OWLModel owlModel) 
  {
    return owlModel.createOWLObjectProperty(null);
  } // createOWLObjectProperty

  public static OWLDatatypeProperty createOWLDatatypeProperty(OWLModel owlModel, String propertyName) 
  {
    OWLDatatypeProperty property = owlModel.getOWLDatatypeProperty(propertyName);

    if (property == null) property = owlModel.createOWLDatatypeProperty(propertyName);

    return property;
  } // createOWLDatatypeProperty

  public static RDFSNamedClass createRDFSNamedClassUsingLabelAnnotation(OWLModel owlModel, String labelText, boolean allowDuplicates) 
  {
    RDFSNamedClass cls = null;
    Collection resources = owlModel.getMatchingResources(owlModel.getRDFSLabelProperty(), "*" + labelText, -1);

    if (!allowDuplicates && resources != null && !resources.isEmpty()) {
      for (Object resource : resources) {
        if (resource instanceof OWLNamedClass) { 
          RDFSNamedClass candidateClass = (OWLNamedClass)resource; 
          // Verify that label is really labelText. Wildcard needed above to get around language encoding causes possible multiple match
          for (Object value : candidateClass.getPropertyValues(owlModel.getRDFSLabelProperty())) { 
            if (value instanceof String) {
              String stringValue = (String)value;
              if (stringValue.equalsIgnoreCase(labelText)) return candidateClass; // Pick the first matching one
            } else if (value instanceof RDFSLiteral) {
              RDFSLiteral literalValue = (RDFSLiteral)value;
              if (literalValue.getString().equalsIgnoreCase(labelText)) return candidateClass; // Pick the first matching one
            } // if
          } // for
        } // if        
      } // for
    } // if
    
    if (cls == null) cls = owlModel.createRDFSNamedClass(null); // We may not have found a matching class above.

    if (!cls.hasPropertyValue(owlModel.getRDFSLabelProperty(), labelText)) cls.addPropertyValue(owlModel.getRDFSLabelProperty(), labelText);

    return cls;
  } 

  // TODO: case senstivity option
  public static OWLNamedClass createOWLNamedClassUsingLabelAnnotation(OWLModel owlModel, String labelText, boolean allowDuplicates) 
  {
    OWLNamedClass cls = null;
    Collection resources = owlModel.getMatchingResources(owlModel.getRDFSLabelProperty(), "*" + labelText, -1);

    if (!allowDuplicates && resources != null && !resources.isEmpty()) {
      for (Object resource : resources) {
        if (resource instanceof OWLNamedClass) { 
          OWLNamedClass candidateClass = (OWLNamedClass)resource; 
          // Verify that label is really labelText. Wildcard needed above to get around language encoding causes possible multiple match
          for (Object value : candidateClass.getPropertyValues(owlModel.getRDFSLabelProperty())) { 
            if (value instanceof String) {
              String stringValue = (String)value;
              if (stringValue.equalsIgnoreCase(labelText)) return candidateClass; // Pick the first matching one
            } else if (value instanceof RDFSLiteral) {
              RDFSLiteral literalValue = (RDFSLiteral)value;
              if (literalValue.getString().equalsIgnoreCase(labelText)) return candidateClass; // Pick the first matching one
            } // if
          } // for
        } // if        
      } // for
    } // if
    
    if (cls == null) cls = owlModel.createOWLNamedClass(null); // We may not have found a matching class above.

    if (!cls.hasPropertyValue(owlModel.getRDFSLabelProperty(), labelText)) cls.addPropertyValue(owlModel.getRDFSLabelProperty(), labelText);

    return cls;
  } // createOWLNamedClassUsingLabelAnnotation

  public static OWLObjectProperty createOWLObjectPropertyUsingLabelAnnotation(OWLModel owlModel, String labelText, boolean allowDuplicates) 
  {
    OWLObjectProperty property = null;
    Collection resources = owlModel.getMatchingResources(owlModel.getRDFSLabelProperty(), "*" + labelText, -1);

    if (!allowDuplicates && resources != null && !resources.isEmpty()) {
      for (Object resource : resources) {
        if (resource instanceof OWLNamedClass) { 
          OWLObjectProperty candidateProperty = (OWLObjectProperty)resource; 
          // Verify that label is really labelText. Wildcard needed above to get around language encoding causes possible multiple match
          for (Object value : candidateProperty.getPropertyValues(owlModel.getRDFSLabelProperty())) { 
            if (value instanceof String) {
              String stringValue = (String)value;
              if (stringValue.equalsIgnoreCase(labelText)) return candidateProperty; // Pick the first matching one
            } else if (value instanceof RDFSLiteral) {
              RDFSLiteral literalValue = (RDFSLiteral)value;
              if (literalValue.getString().equalsIgnoreCase(labelText)) return candidateProperty; // Pick the first matching one
            } // if
          } // for
        } // if        
      } // for
    } // if
    
    if (property == null) property = owlModel.createOWLObjectProperty(null); // We may not have found a matching property above.

    if (!property.hasPropertyValue(owlModel.getRDFSLabelProperty(), labelText)) property.addPropertyValue(owlModel.getRDFSLabelProperty(), labelText);

    return property;
  } // createOWLObjectPropertyUsingLabelAnnotation

  public static OWLDatatypeProperty createOWLDataPropertyUsingLabelAnnotation(OWLModel owlModel, String labelText, boolean allowDuplicates) 
  {
    OWLDatatypeProperty property = null;
    Collection resources = owlModel.getMatchingResources(owlModel.getRDFSLabelProperty(), "*" + labelText, -1);

    if (!allowDuplicates && resources != null && !resources.isEmpty()) {
      for (Object resource : resources) {
        if (resource instanceof OWLNamedClass) { 
          OWLDatatypeProperty candidateProperty = (OWLDatatypeProperty)resource; 
          // Verify that label is really labelText. Wildcard needed above to get around language encoding causes possible multiple match
          for (Object value : candidateProperty.getPropertyValues(owlModel.getRDFSLabelProperty())) { 
            if (value instanceof String) {
              String stringValue = (String)value;
              if (stringValue.equalsIgnoreCase(labelText)) return candidateProperty; // Pick the first matching one
            } else if (value instanceof RDFSLiteral) {
              RDFSLiteral literalValue = (RDFSLiteral)value;
              if (literalValue.getString().equalsIgnoreCase(labelText)) return candidateProperty; // Pick the first matching one
            } // if
          } // for
        } // if        
      } // for
    } // if
    
    if (property == null) property = owlModel.createOWLDatatypeProperty(null); // We may not have found a matching property above.

    if (!property.hasPropertyValue(owlModel.getRDFSLabelProperty(), labelText)) property.addPropertyValue(owlModel.getRDFSLabelProperty(), labelText);

    return property;
  } // createOWLDataPropertyUsingLabelAnnotation

  public static OWLIndividual createOWLIndividualUsingLabelAnnotation(OWLModel owlModel, String labelText, boolean allowDuplicates) 
   throws SWRLOWLUtilException
  {
    OWLIndividual individual = null;
    Collection resources = owlModel.getMatchingResources(owlModel.getRDFSLabelProperty(), "*" + labelText, -1);

    if (!allowDuplicates && resources != null && !resources.isEmpty()) {
      for (Object resource : resources) {
        if (resource instanceof OWLNamedClass) { 
          OWLIndividual candidateIndividual = (OWLIndividual)resource; 
          // Verify that label is really labelText. Wildcard needed above to get around language encoding causes possible multiple match
          for (Object value : candidateIndividual.getPropertyValues(owlModel.getRDFSLabelProperty())) { 
            if (value instanceof String) {
              String stringValue = (String)value;
              if (stringValue.equalsIgnoreCase(labelText)) return candidateIndividual; // Pick the first matching one
            } else if (value instanceof RDFSLiteral) {
              RDFSLiteral literalValue = (RDFSLiteral)value;
              if (literalValue.getString().equalsIgnoreCase(labelText)) return candidateIndividual; // Pick the first matching one
            } // if
          } // for
        } // if        
      } // for
    } // if
    
    if (individual == null) individual = createOWLIndividual(owlModel); // We may not have found a matching individual above.

    if (!individual.hasPropertyValue(owlModel.getRDFSLabelProperty(), labelText)) individual.addPropertyValue(owlModel.getRDFSLabelProperty(), labelText);

    return individual;
  } // createOWLIndividualUsingLabelAnnotation

  public static Set<OWLIndividual> getMatchingIndividuals(OWLModel owlModel, String propertyName, String matchString) 
    throws SWRLOWLUtilException
  {
    RDFProperty property = getOWLProperty(owlModel, propertyName, true);
    Collection matchingResources = owlModel.getMatchingResources(property, matchString, -1);
    Set<OWLIndividual> matchingIndividuals = new HashSet<OWLIndividual>();

    for (Object o : matchingResources) if (o instanceof OWLIndividual) matchingIndividuals.add((OWLIndividual)o);

    return matchingIndividuals;
  } // getMatchingIndividuals

  public static Set<OWLIndividual> getMatchingIndividualsOfClass(OWLModel owlModel, String className, String propertyName, String matchString) 
    throws SWRLOWLUtilException
  {
    Set<OWLIndividual> matchingIndividuals = new HashSet<OWLIndividual>();

    for (OWLIndividual owlIndividual : getMatchingIndividuals(owlModel, propertyName, matchString))
      if (isOWLIndividualOfClass(owlModel, owlIndividual, className)) matchingIndividuals.add(owlIndividual);

    return matchingIndividuals;
  } // getMatchingIndividualsOfClass    

  public static boolean isOWLNamedClass(OWLModel owlModel, String className)
  {
    RDFResource resource = owlModel.getRDFResource(className);

    return resource instanceof OWLNamedClass;
  } 

  public static boolean isOWLClass(OWLModel owlModel, String className)
  {
    RDFResource resource = owlModel.getRDFResource(className);

    return resource != null && resource instanceof OWLClass;
  } // isOWLClass

  public static OWLNamedClass getOWLNamedClass(OWLModel owlModel, String className) throws SWRLOWLUtilException
  {
    OWLNamedClass cls = owlModel.getOWLNamedClass(className);

    if (cls == null) throw new SWRLOWLUtilException("invalid OWL named class " + className);

    return cls;
  } // getOWLNamedClass

  public static OWLClass getOWLClass(OWLModel owlModel, String className) throws SWRLOWLUtilException
  {
    RDFResource resource = owlModel.getRDFResource(className);

    if (resource == null || !(resource instanceof OWLClass)) throw new SWRLOWLUtilException("invalid OWL class " + className);

    return (OWLClass)resource;
  } // getOWLClass
  
  public static RDFResource getRDFResource(OWLModel owlModel, String resourceName)
  {
	return owlModel.getRDFResource(resourceName);
  } // getRDFResource

  public static RDFSClass getRDFSClass(OWLModel owlModel, String className) throws SWRLOWLUtilException
  {
    RDFResource resource = owlModel.getRDFResource(className);

    if (resource == null || !(resource instanceof RDFSClass)) throw new SWRLOWLUtilException("invalid RDFS class " + className);

    return (RDFSClass)resource;
  } // getRDFSClass

  public static OWLClass getOWLClassDescription(OWLModel owlModel, String descriptionClassName) throws SWRLOWLUtilException
  {
    RDFResource resource = owlModel.getRDFResource(descriptionClassName);

    if (resource == null || !(resource instanceof OWLClass)) 
      throw new SWRLOWLUtilException("invalid OWL class description " + descriptionClassName);

    return (OWLClass)resource;
  } // getOWLClassDescription

  public static OWLIndividual createIndividual(OWLModel owlModel, String individualName) throws SWRLOWLUtilException
  {
    return createIndividualOfClass(owlModel, getOWLThingClass(owlModel), individualName);
  } // createIndividualOfClass

  public static OWLIndividual createOWLIndividual(OWLModel owlModel) throws SWRLOWLUtilException
  {
    return (OWLIndividual)getOWLThingClass(owlModel).createInstance(null);
  } // createOWLIndividual

  public static OWLIndividual createIndividualOfClass(OWLModel owlModel, String className)
    throws SWRLOWLUtilException
  {
    return createIndividualOfClass(owlModel, className, null);
  } // createIndividualOfClass

  public static OWLIndividual createIndividualOfClass(OWLModel owlModel, String className, String individualName)
    throws SWRLOWLUtilException
  {
    OWLNamedClass cls = getNamedClass(owlModel, className);

    return createIndividualOfClass(owlModel, cls, individualName);
  } // createIndividualOfClass

  public static OWLIndividual createIndividualOfClass(OWLModel owlModel, OWLClass cls)
    throws SWRLOWLUtilException
  {
    return createIndividualOfClass(owlModel, cls, null);
  } // createIndividualOfClass

  public static OWLIndividual createIndividualOfClass(OWLModel owlModel, OWLClass cls, String individualName)
    throws SWRLOWLUtilException
  {
    RDFResource resource = null;
    OWLIndividual individual = null;

    if (individualName != null) resource = owlModel.getRDFResource(individualName);

    if (resource == null) {
      individual = (OWLIndividual)cls.createInstance(individualName);
      if (!individual.hasRDFType(cls, true)) individual.setRDFType(cls);
    } else {
      if (resource instanceof OWLIndividual) {
        individual = (OWLIndividual)resource;
        if (!individual.hasRDFType(cls, true)) individual.addRDFType(cls);
      } else throwException("could not create individual " + individualName + " because another resource of that name already exists");
    } // if

    return individual;
  } // createIndividualOfClass

  public static OWLNamedClass getNamedClass(OWLModel owlModel, String className) throws SWRLOWLUtilException
  {
    return getOWLNamedClass(owlModel, className, true);
  } // getNamedClass

  public static OWLClass getClass(OWLModel owlModel, String className) throws SWRLOWLUtilException
  {
    return getOWLClass(owlModel, className, true);
  } // getClass

  public static boolean isOWLIndividualOfClass(OWLModel owlModel, OWLIndividual individual, String className) 
  {
    RDFResource cls = owlModel.getRDFResource(className);

    return (cls instanceof OWLNamedClass) && individual.hasRDFType((OWLNamedClass)cls, true);
  } 

  public static boolean isOWLIndividualOfClass(OWLModel owlModel, String individualName, String className) 
  {
    RDFResource cls = owlModel.getRDFResource(className);
    RDFResource individual = owlModel.getRDFResource(individualName);

    return (cls instanceof OWLNamedClass) && (individual instanceof OWLIndividual) && individual.hasRDFType((OWLNamedClass)cls, true);
  }

  public static void setClass(OWLModel owlModel, String individualName, String className) throws SWRLOWLUtilException
  {
    OWLClass cls = (OWLClass)owlModel.getCls(className);
    OWLIndividual individual = getIndividual(owlModel, individualName);

    if (!individual.hasRDFType(cls, true)) individual.setRDFType(cls);
  } 

  public static void addClass(OWLModel owlModel, String individualName, String className) throws SWRLOWLUtilException
  {
    OWLClass cls = (OWLClass)owlModel.getCls(className);
    OWLIndividual individual = getIndividual(owlModel, individualName);

    if (!individual.hasRDFType(cls, true)) individual.addRDFType(cls);
  } // addClass

  public static void addClass(OWLIndividual individual, OWLClass cls) throws SWRLOWLUtilException
  {
    if (!individual.hasRDFType(cls, true)) individual.addRDFType(cls);
  } // addClass

  public static String getFullName(OWLModel owlModel, String name) throws SWRLOWLUtilException
  {
    String result = OWLUtil.getInternalFullName(owlModel, name, true);

    if (result == null) throw new SWRLOWLUtilException("cannot get full name for resource " + name);

    return result;
  } // getFullName

  public static Set<String> getRDFSLabels(OWLModel owlModel, String resourceName)
  {
  	return getRDFSLabels(owlModel, resourceName, "");
  } 
  
  public static Set<String> getRDFSLabels(OWLModel owlModel, String resourceName, String language)
  {
    RDFResource resource = owlModel.getRDFResource(resourceName);
    Set<String> result = new HashSet<String>();

    if (resource != null) {
    	for (Object label : resource.getPropertyValues(owlModel.getRDFSLabelProperty())) {
    		if (label instanceof String) result.add((String)label);
        else if (label instanceof RDFSLiteral) {
        	RDFSLiteral literal = (RDFSLiteral)label;
        	if (language.equals("") || (language.equals(literal.getLanguage())))
            result.add(literal.getString());
        } // if
    	} // for
    } // if
    return result;
  } // getRDFSLabels

  public static Set<String> getRDFSLabelLanguages(OWLModel owlModel, String resourceName)
  {
    RDFResource resource = owlModel.getRDFResource(resourceName);
    Set<String> result = new HashSet<String>();

    if (resource != null) {
    	for (Object label : resource.getPropertyValues(owlModel.getRDFSLabelProperty())) {
    		if (label instanceof RDFSLiteral) {
    			RDFSLiteral literal = (RDFSLiteral)label;	
    			result.add(literal.getLanguage());
    		} // if
    	} // for
    } // if
    return result;
  }
  
  public static void addRDFSLabel(RDFResource resource, String labelText) 
  {
    if (!resource.hasPropertyValue(resource.getOWLModel().getRDFSLabelProperty(), labelText)) 
    	resource.addPropertyValue(resource.getOWLModel().getRDFSLabelProperty(), labelText);
  } 

  public static OWLSomeValuesFrom getOWLSomeValuesFrom(OWLModel owlModel, String className) throws SWRLOWLUtilException
  {
    return (OWLSomeValuesFrom)owlModel.getOWLSomeValuesFromRestrictionClass().createInstance(className);    
  } 

  public static OWLIndividual getOWLIndividual(OWLModel owlModel, OWLNamedClass cls, boolean mustExist, int mustHaveExactlyN)
    throws SWRLOWLUtilException
  {
    OWLIndividual individual = null;
    Collection instances;
    Object firstInstance;

    if (mustExist && cls.getInstanceCount(true) == 0) throwException("no individuals of class " + cls.getPrefixedName() + " in ontology");
    else if (cls.getInstanceCount(true) != mustHaveExactlyN) 
      throwException("expecting exactly " + mustHaveExactlyN + " individuals of class " + cls.getPrefixedName() + " in ontology - got " +
                     cls.getInstanceCount(true) + "");

    instances = cls.getInstances();

    if (!instances.isEmpty()) {
      firstInstance = cls.getInstances(true).iterator().next();

      if (firstInstance instanceof OWLIndividual) return (OWLIndividual)firstInstance;
      else throw new SWRLOWLUtilException("instance of class " + cls.getPrefixedName() + " is not an OWL individual");
    } else return null;
  }

  public static Set<OWLIndividual> getAllOWLIndividuals(OWLModel owlModel) throws SWRLOWLUtilException
  {
    return new HashSet<OWLIndividual>(owlModel.getOWLIndividuals());
  }

  public static Set<OWLIndividual> getOWLIndividualsOfClass(OWLModel owlModel, String className) throws SWRLOWLUtilException
  {
    OWLNamedClass cls = getNamedClass(owlModel, className);

    return new HashSet<OWLIndividual>(getOWLIndividualsOfClass(cls));
  } 

  public static Set<OWLIndividual> getOWLIndividualsOfClass(OWLNamedClass cls) throws SWRLOWLUtilException
  {
    return new HashSet<OWLIndividual>(cls.getInstances(true));
  }

  public static OWLProperty getProperty(OWLModel owlModel, String propertyName) throws SWRLOWLUtilException
  {
    return getOWLProperty(owlModel, propertyName, true);
  } 

  public static OWLDatatypeProperty getOWLDataProperty(OWLModel owlModel, String propertyName, boolean mustExist) throws SWRLOWLUtilException
  {
    OWLDatatypeProperty property = owlModel.getOWLDatatypeProperty(propertyName);
    if (mustExist && property == null) throwException("no " + propertyName + " datatype property in ontology");

    return property;
  }

  public static OWLObjectProperty getOWLObjectProperty(OWLModel owlModel, String propertyName, boolean mustExist) throws SWRLOWLUtilException
  {
    OWLObjectProperty property = owlModel.getOWLObjectProperty(propertyName);

    if (mustExist && property == null) throwException("no " + propertyName + " object property in ontology");

    return property;
  }

  public static boolean isOWLEquivalentProperty(OWLModel owlModel, String propertyName1, String propertyName2, boolean mustExist) 
    throws SWRLOWLUtilException
  {
    OWLProperty property1 = getOWLProperty(owlModel, propertyName1, mustExist);
    OWLProperty property2 = getOWLProperty(owlModel, propertyName2, mustExist);

    return (property1 != null && property2 != null && property1.getEquivalentProperties().contains(property2));
  }

  public static boolean isOWLEquivalentClass(OWLModel owlModel, String className1, String className2, boolean mustExist) 
    throws SWRLOWLUtilException
  {
    OWLNamedClass class1 = getOWLNamedClass(owlModel, className1, mustExist);
    OWLNamedClass class2 = getOWLNamedClass(owlModel, className2, mustExist);

    return (class1 != null && class2 != null && class1.hasEquivalentClass(class2));
  } 

  public static boolean isOWLDisjointClass(OWLModel owlModel, String className1, String className2, boolean mustExist) 
    throws SWRLOWLUtilException
  {
    OWLNamedClass class1 = getOWLNamedClass(owlModel, className1, mustExist);
    OWLNamedClass class2 = getOWLNamedClass(owlModel, className2, mustExist);

    return (class1 != null && class2 != null && class1.getDisjointClasses().contains(class2));
  }

  public static boolean isOWLSubPropertyOf(OWLModel owlModel, String subPropertyName, String propertyName, boolean mustExist) 
    throws SWRLOWLUtilException
  {
    OWLProperty subProperty = getOWLProperty(owlModel, subPropertyName, mustExist);
    OWLProperty property = getOWLProperty(owlModel, propertyName, mustExist);

    return (subProperty != null && property != null && subProperty.isSubpropertyOf(property, true));
  } 

  public static boolean isOWLSuperPropertyOf(OWLModel owlModel, String superPropertyName, String propertyName, boolean mustExist) 
    throws SWRLOWLUtilException
  {
    OWLProperty superProperty = getOWLProperty(owlModel, superPropertyName, mustExist);
    OWLProperty property = getOWLProperty(owlModel, propertyName, mustExist);

    return (superProperty != null && property != null && property.isSubpropertyOf(superProperty, true)); // No isSuperpropertyOf call
  }

  public static boolean isOWLDirectSuperPropertyOf(OWLModel owlModel, String superPropertyName, String propertyName, boolean mustExist) 
    throws SWRLOWLUtilException
  {
    OWLProperty superProperty = getOWLProperty(owlModel, superPropertyName, mustExist);
    OWLProperty property = getOWLProperty(owlModel, propertyName, mustExist);

    return (superProperty != null && property != null && property.isSubpropertyOf(superProperty, false)); // No isSuperpropertyOf call
  } // isDirectSuperPropertyOf

  public static boolean isOWLDirectSubPropertyOf(OWLModel owlModel, String subPropertyName, String propertyName, boolean mustExist) 
    throws SWRLOWLUtilException
  {
    OWLProperty subProperty = getOWLProperty(owlModel, subPropertyName, mustExist);
    OWLProperty property = getOWLProperty(owlModel, propertyName, mustExist);

    return (subProperty != null && property != null && subProperty.isSubpropertyOf(property, false));
  } 

  public static boolean isOWLDirectSubClassOf(OWLModel owlModel, String subClassName, String className, boolean mustExist) 
    throws SWRLOWLUtilException
  {
    OWLNamedClass subClass = getOWLNamedClass(owlModel, subClassName, mustExist);
    OWLNamedClass cls = getOWLNamedClass(owlModel, className, mustExist);

    return (subClass != null && cls != null && subClass.isSubclassOf(cls));
  } 

  public static boolean isOWLSubClassOf(OWLModel owlModel, String subClassName, String className, boolean mustExist) 
    throws SWRLOWLUtilException
  {
    OWLNamedClass subClass = getOWLNamedClass(owlModel, subClassName, mustExist);
    OWLNamedClass cls = getOWLNamedClass(owlModel, className, mustExist);

    return (subClass != null && cls != null && cls.getSubclasses().contains(subClass)); // TODO: uses deprecated getSubclasses
  }

  public static void addOWLSuperClass(OWLModel owlModel, String subClassName, String superClassName) throws SWRLOWLUtilException
  {
    OWLClass subClass = getOWLClass(owlModel, subClassName);
    OWLClass superClass = getOWLClass(owlModel, superClassName);

    subClass.addSuperclass(superClass);
  } 

  public static void addOWLSuperClass(OWLClass subClass, OWLClass superClass) 
  {
    subClass.addSuperclass(superClass);
  }

  public static void addRDFSSuperClass(RDFSClass subClass, RDFSClass superClass) 
  {
    subClass.addSuperclass(superClass);
  } 
    
  public static boolean isOWLDirectSuperClassOf(OWLModel owlModel, String superClassName, String className, boolean mustExist) 
    throws SWRLOWLUtilException
  {
    OWLNamedClass superClass = getOWLNamedClass(owlModel, superClassName, mustExist);
    OWLNamedClass cls = getOWLNamedClass(owlModel, className, mustExist);

    return (superClass != null && cls != null && cls.isSubclassOf(superClass)); // No isSuperclassOf call
  } 

  public static boolean isOWLSuperClassOf(OWLModel owlModel, String superClassName, String className) 
    throws SWRLOWLUtilException
  {
    return isOWLSuperClassOf(owlModel, superClassName, className, true);
  }

  public static boolean isOWLSuperClassOf(OWLModel owlModel, String superClassName, String className, boolean mustExist) 
    throws SWRLOWLUtilException
  {
    OWLNamedClass superClass = getOWLNamedClass(owlModel, superClassName, mustExist);
    OWLNamedClass cls = getOWLNamedClass(owlModel, className, mustExist);

    return (superClass != null && cls != null && cls.getSuperclasses(true).contains(superClass));
  } 

  public static int getNumberOfOWLIndividualsOfClass(OWLModel owlModel, String className) throws SWRLOWLUtilException
  {
    return getNumberOfOWLIndividualsOfClass(owlModel, className, true);
  }

  public static int getNumberOfOWLIndividualsOfClass(OWLModel owlModel, String className, boolean mustExist) throws SWRLOWLUtilException
  {
    OWLNamedClass cls = getOWLNamedClass(owlModel, className, mustExist);
    int numberOfIndividuals = 0;

    if (cls != null) numberOfIndividuals = cls.getInstances(true).size();

    return numberOfIndividuals;
  } 

  public static int getNumberOfDirectOWLInstancesOfClass(OWLModel owlModel, String className, boolean mustExist) throws SWRLOWLUtilException
  {
    OWLNamedClass cls = getOWLNamedClass(owlModel, className, mustExist);
    int numberOfIndividuals = 0;

    if (cls != null) numberOfIndividuals = cls.getInstances(false).size();

    return numberOfIndividuals;
  } //  getNumberOfDirectInstancesOfClass

  public static boolean isConsistentOWLClass(OWLModel owlModel, String className, boolean mustExist) throws SWRLOWLUtilException
  {
    OWLNamedClass cls = getOWLNamedClass(owlModel, className, mustExist);

    return cls != null && cls.isConsistent();
  }

  public static Set<OWLNamedClass> getOWLDomainClasses(OWLModel owlModel, String propertyName)
    throws SWRLOWLUtilException
  {
    return getOWLDomainClasses(owlModel, propertyName, true, true);
  } 

  public static Set<OWLNamedClass> getOWLDomainClasses(OWLModel owlModel, String propertyName, boolean mustExist)
    throws SWRLOWLUtilException
  {
    return getOWLDomainClasses(owlModel, propertyName, mustExist, true);
  }

  public static Set<OWLNamedClass> getDirectOWLDomainClasses(OWLModel owlModel, String propertyName)
    throws SWRLOWLUtilException
  {
    return getOWLDomainClasses(owlModel, propertyName, true, false);
  }

  public static Set<OWLNamedClass> getDirectOWLDomainClasses(OWLModel owlModel, String propertyName, boolean mustExist)
    throws SWRLOWLUtilException
  {
    return getOWLDomainClasses(owlModel, propertyName, mustExist, false);
  }

  private static Set<OWLNamedClass> getOWLDomainClasses(OWLModel owlModel, String propertyName, boolean mustExist, 
                                                        boolean includingSuperproperties) 
    throws SWRLOWLUtilException
  {
    OWLProperty property = getOWLProperty(owlModel, propertyName, mustExist);
    Set<OWLNamedClass> result = new HashSet<OWLNamedClass>();
    Collection domainClasses = property.getUnionDomain(includingSuperproperties);
    Iterator iterator;

    if (domainClasses == null) return result;
    
    iterator = domainClasses.iterator();
    while (iterator.hasNext()) {
      Object o = iterator.next();
      if (o instanceof OWLNamedClass) result.add((OWLNamedClass)o);
    } // while
    
    return result;
  } 

  public static void removeOWLThingSuperclass(OWLModel owlModel, OWLClass owlClass)
  {
    if (owlClass.getSuperclasses(false).contains(getOWLThingClass(owlModel))) owlClass.removeSuperclass(getOWLThingClass(owlModel));
  } // removeOWLThingSuperclass

  public static Set<OWLNamedClass> getOWLRangeClasses(OWLModel owlModel, String propertyName, boolean mustExist)
    throws SWRLOWLUtilException
  {
    return getOWLRangeClasses(owlModel, propertyName, mustExist, true);
  }

  public static Set<OWLNamedClass> getOWLRangeClasses(OWLModel owlModel, String propertyName)
    throws SWRLOWLUtilException
  {
    return getOWLRangeClasses(owlModel, propertyName, true, true);
  }

  public static Set<OWLNamedClass> getOWLDirectRangeClasses(OWLModel owlModel, String propertyName, boolean mustExist)
    throws SWRLOWLUtilException
  {
    return getOWLRangeClasses(owlModel, propertyName, mustExist, false);
  }

  public static Set<OWLNamedClass> getOWLDirectRangeClasses(OWLModel owlModel, String propertyName)
    throws SWRLOWLUtilException
  {
    return getOWLRangeClasses(owlModel, propertyName, true, false);
  } 

  private static Set<OWLNamedClass> getOWLRangeClasses(OWLModel owlModel, String propertyName, boolean mustExist, 
                                                       boolean includingSuperproperties) 
    throws SWRLOWLUtilException
  {
    OWLProperty property = getOWLProperty(owlModel, propertyName, mustExist);
    Set<OWLNamedClass> result = new HashSet<OWLNamedClass>();
    Collection rangeClasses = property.getUnionRangeClasses(); // TODO: no includingSuperproperties argument supported
    Iterator iterator;

    if (rangeClasses == null) return result;
    
    iterator = rangeClasses.iterator();
    while (iterator.hasNext()) {
      RDFResource resource = (RDFResource)iterator.next();
      if (resource instanceof OWLNamedClass) result.add((OWLNamedClass)resource);
    } // while
    
    return result;
  }

  public static boolean isInOWLPropertyDomain(OWLModel owlModel, String propertyName, String className, boolean mustExist) 
    throws SWRLOWLUtilException
  {
    OWLProperty property = getOWLProperty(owlModel, propertyName, mustExist);
    OWLNamedClass cls = getOWLNamedClass(owlModel, className, mustExist);

    return (property != null && cls != null && property.getDomains(true).contains(cls));
  } 

  public static boolean isInDirectOWLPropertyDomain(OWLModel owlModel, String propertyName, String className, boolean mustExist) 
    throws SWRLOWLUtilException
  {
    OWLProperty property = getOWLProperty(owlModel, propertyName, mustExist);
    OWLNamedClass cls = getOWLNamedClass(owlModel, className, mustExist);

    return (property != null && cls != null && property.getDomains(false).contains(cls));
  } // isInDirectPropertyDomain

  public static boolean isInPropertyRange(OWLModel owlModel, String propertyName, String className, boolean mustExist) 
    throws SWRLOWLUtilException
  {
    OWLProperty property = getOWLProperty(owlModel, propertyName, mustExist);
    OWLNamedClass cls = getOWLNamedClass(owlModel, className, mustExist);

    return (property != null && cls != null && property.getRanges(true).contains(cls));
  } // isInPropertyRange

  public static boolean isInDirectPropertyRange(OWLModel owlModel, String propertyName, String className, boolean mustExist) 
    throws SWRLOWLUtilException
  {
    OWLProperty property = getOWLProperty(owlModel, propertyName, mustExist);
    OWLNamedClass cls = getOWLNamedClass(owlModel, className, mustExist);

    return (property != null && cls != null && property.getRanges(false).contains(cls));
  } // isInDirectPropertyRange

  public static boolean isOWLObjectProperty(OWLModel owlModel, String propertyName, boolean mustExist) throws SWRLOWLUtilException
  {
    OWLProperty property = getOWLProperty(owlModel, propertyName, mustExist);

    return (property != null && property.isObjectProperty());
  }

  public static boolean isOWLIndividual(OWLModel owlModel, String individualName)
  {
    RDFResource resource = owlModel.getRDFResource(individualName);

    return (resource != null && resource instanceof OWLIndividual);
  } // isOWLIndividual

  public static boolean isOWLObjectProperty(OWLModel owlModel, String propertyName)
  {
    RDFResource resource = owlModel.getRDFResource(propertyName);

    return resource instanceof OWLObjectProperty;
  } 

  public static boolean isOWLDataProperty(OWLModel owlModel, String propertyName, boolean mustExist) throws SWRLOWLUtilException
  {
    OWLProperty property = getOWLProperty(owlModel, propertyName, mustExist);

    return (property != null && !property.isObjectProperty());
  } // isOWLDataProperty

  public static boolean isOWLDataProperty(OWLModel owlModel, String propertyName)
  {
    RDFResource resource = owlModel.getRDFResource(propertyName);

    return resource instanceof OWLDatatypeProperty;
  } // isOWLDataProperty

  public static boolean isOWLTransitiveProperty(OWLModel owlModel, String propertyName, boolean mustExist) throws SWRLOWLUtilException
  {
    OWLProperty property = getOWLProperty(owlModel, propertyName, mustExist);

    return (property != null && property instanceof OWLObjectProperty && ((OWLObjectProperty)property).isTransitive());
  } // isOWLTransitiveProperty

  public static boolean isOWLTransitiveProperty(OWLModel owlModel, String propertyName) throws SWRLOWLUtilException
  {
    return isOWLTransitiveProperty(owlModel, propertyName, true);
  } // isOWLTransitiveProperty

  public static boolean isOWLSymmetricProperty(OWLModel owlModel, String propertyName, boolean mustExist) throws SWRLOWLUtilException
  {
    OWLProperty property = getOWLProperty(owlModel, propertyName, mustExist);

    return (property != null && property instanceof OWLObjectProperty && ((OWLObjectProperty)property).isSymmetric());
  } // isOWLSymmetricProperty

  public static boolean isOWLFunctionalProperty(OWLModel owlModel, String propertyName, boolean mustExist) throws SWRLOWLUtilException
  {
    OWLProperty property = getOWLProperty(owlModel, propertyName, mustExist);

    return property != null && property.isFunctional();
  } // isOWLFunctionalProperty

  public static boolean isAnnotationProperty(OWLModel owlModel, String propertyName, boolean mustExist) throws SWRLOWLUtilException
  {
    OWLProperty property = getOWLProperty(owlModel, propertyName, mustExist);

    return property != null && property.isAnnotationProperty();
  } // isAnnotationProperty

  public static boolean isInverseFunctionalProperty(OWLModel owlModel, String propertyName, boolean mustExist) throws SWRLOWLUtilException
  {
    OWLProperty property = getOWLProperty(owlModel, propertyName, mustExist);

    return property != null && property.isInverseFunctional();
  } // isInverseFunctionalProperty

  public static boolean isAnonymousResourceName(OWLModel owlModel, String resourceName) throws SWRLOWLUtilException
  {
    return owlModel.isAnonymousResourceName(resourceName);
  } // isAnonymousResourceName

  public static OWLIndividual getIndividual(OWLModel owlModel, String individualName) throws SWRLOWLUtilException
  {
    return getOWLIndividual(owlModel, individualName, true);
  } 

  public static OWLIndividual getOWLIndividual(OWLModel owlModel, String individualName, boolean mustExist) throws SWRLOWLUtilException
  {
    RDFResource resource = owlModel.getRDFResource(individualName);

    if (mustExist && (resource == null || !(resource instanceof OWLIndividual))) 
      throwException("no individual named " + individualName + " in ontology");
        
    if (resource != null) {
      if (resource instanceof OWLIndividual) return (OWLIndividual)resource;
      else return null;
    } else return null;
  }

  public static OWLNamedClass getOWLNamedClass(OWLModel owlModel, String className, boolean mustExist) throws SWRLOWLUtilException
  {
    RDFResource resource = owlModel.getRDFResource(className);

    if (mustExist && (resource == null || !(resource instanceof OWLNamedClass))) 
      throwException("no class named " + className + " in ontology");
        
    if (resource != null) {
      if (resource instanceof OWLNamedClass) return (OWLNamedClass)resource;
      else return null;
    } else return null;
  }

  public static OWLClass getOWLClass(OWLModel owlModel, String className, boolean mustExist) throws SWRLOWLUtilException
  {
    RDFResource resource = owlModel.getRDFResource(className);

    if (mustExist && (resource == null || !(resource instanceof OWLClass))) 
      throwException("no class named " + className + " in ontology");
        
    if (resource != null) {
      if (resource instanceof OWLClass) return (OWLClass)resource;
      else return null;
    } else return null;
  }

  public static Set<OWLNamedClass> getOWLClassesOfIndividual(OWLModel owlModel, String individualName) throws SWRLOWLUtilException
  {
    return getOWLClassesOfIndividual(owlModel, individualName, true);
  }

  public static Set<OWLNamedClass> getOWLClassesOfIndividual(OWLModel owlModel, String individualName, boolean mustExist) 
    throws SWRLOWLUtilException  
  {
    OWLIndividual individual = getOWLIndividual(owlModel, individualName, mustExist);
    
    return individual == null ? new HashSet<OWLNamedClass>() : getClassesOfIndividual(owlModel, individual);
  }

  public static Set<OWLNamedClass> getClassesOfIndividual(OWLModel owlModel, OWLIndividual individual) throws SWRLOWLUtilException
  {
    Set<OWLNamedClass> result = new HashSet<OWLNamedClass>();
    Collection types = individual.getRDFTypes();
    Iterator iterator = types.iterator();
    while (iterator.hasNext()) {
      RDFResource resource = (RDFResource)iterator.next();
      if (resource instanceof OWLNamedClass) result.add((OWLNamedClass)resource); // Ignore anonymous classes
    } // while

    return result;
  } 

  public static String getNextAnonymousResourceName(OWLModel owlModel)
  {
    return owlModel.getNextAnonymousResourceName();
  } 

  public static boolean isOWLNamedClass(OWLModel owlModel, String className, boolean mustExist) throws SWRLOWLUtilException
  {
    return(getOWLNamedClass(owlModel, className, mustExist) != null);
  } 

  public static boolean isOWLProperty(OWLModel owlModel, String propertyName) 
  {
    RDFResource resource = owlModel.getRDFResource(propertyName);

    return resource instanceof OWLProperty;
  } 

  public static boolean isOWLProperty(OWLModel owlModel, String propertyName, boolean mustExist) throws SWRLOWLUtilException
  {
    return (getOWLProperty(owlModel, propertyName, mustExist) != null);
  }

  public static Collection<OWLNamedClass> getUserDefinedOWLNamedClasses(OWLModel owlModel) 
  { 
    return new ArrayList<OWLNamedClass>(owlModel.getUserDefinedOWLNamedClasses());
  }

  public static Collection<OWLProperty> getUserDefinedOWLProperties(OWLModel owlModel) 
  { 
    return new ArrayList<OWLProperty>(owlModel.getUserDefinedOWLProperties());
  } 
  
  public static Collection<OWLProperty> getUserDefinedOWLObjectProperties(OWLModel owlModel) 
  { 
    return new ArrayList<OWLProperty>(owlModel.getUserDefinedOWLObjectProperties());
  } 

  public static Collection<OWLProperty> getUserDefinedOWLDatatypeProperties(OWLModel owlModel) 
  { 
    return new ArrayList<OWLProperty>(owlModel.getUserDefinedOWLDatatypeProperties());
  } // getUserDefinedOWLDatatypeProperties

  public static boolean isIndividual(OWLModel owlModel, String individualName, boolean mustExist) throws SWRLOWLUtilException
  {
    return (getOWLIndividual(owlModel, individualName, mustExist) != null);
  } // isIndividualName

  public static boolean isSWRLVariable(OWLModel owlModel, String individualName, boolean mustExist) throws SWRLOWLUtilException
  {
    return (getOWLIndividual(owlModel, individualName, mustExist) != null &&
            getOWLIndividual(owlModel, individualName, mustExist) instanceof SWRLVariable);
  } // isIndividualName

  public static boolean isIndividual(OWLModel owlModel, String individualName) throws SWRLOWLUtilException
  {
    return (getOWLIndividual(owlModel, individualName, false) != null);
  } // isIndividual

  public static int getNumberOfPropertyValues(OWLModel owlModel, String individualName, String propertyName, boolean mustExist) 
    throws SWRLOWLUtilException
  { 
    OWLProperty property = getOWLProperty(owlModel, propertyName, mustExist); // Will throw an exception if mustExist is true.
    OWLIndividual individual = getOWLIndividual(owlModel, individualName, mustExist); // Will throw an exception if mustExist is true.

    return individual.getPropertyValues(property).size();
  } // getNumberOfPropertyValues

  public static Set<OWLProperty> getOWLPropertiesOfIndividual(OWLModel owlModel, String individualName) 
    throws SWRLOWLUtilException
  { 
    OWLIndividual individual = getOWLIndividual(owlModel, individualName, true);
    HashSet<OWLProperty> properties = new HashSet<OWLProperty>();
    Collection rdfProperties = individual.getRDFProperties();

    Iterator iterator = rdfProperties.iterator();
    while (iterator.hasNext()) {
      RDFProperty property = (RDFProperty)iterator.next();
      if (property instanceof OWLProperty) properties.add((OWLProperty)property);
    } // while

    return properties;
  } 

  public static Set<OWLProperty> getPossibleOWLPropertiesOfIndividual(OWLModel owlModel, String individualName) 
    throws SWRLOWLUtilException
  { 
    OWLIndividual individual = getOWLIndividual(owlModel, individualName, true);
    HashSet<OWLProperty> properties = new HashSet<OWLProperty>();
    Collection rdfProperties = individual.getPossibleRDFProperties();

    Iterator iterator = rdfProperties.iterator();
    while (iterator.hasNext()) {
      RDFProperty property = (RDFProperty)iterator.next();
      if (property instanceof OWLProperty) properties.add((OWLProperty)property);
    } // while

    return properties;
  } 

  public static String getURI(OWLModel owlModel, String resourceName) throws SWRLOWLUtilException
  {
    RDFResource resource = owlModel.getRDFResource(resourceName);
    
    if (resource == null) throwException("invalid resource " + resourceName);

    return resource.getURI();
  } 

  public static int getNumberOfOWLPropertyValues(OWLModel owlModel, String individualName, 
                                                  String propertyName, Object propertyValue, boolean mustExist) 
    throws SWRLOWLUtilException
  { 
    OWLProperty property = getOWLProperty(owlModel, propertyName, mustExist); // Will throw an exception if mustExist is true
    OWLIndividual individual = getOWLIndividual(owlModel, individualName, mustExist); // Will throw an exception if mustExist is true
    int numberOfPropertyValues = 0;

    if (propertyValue == null) throwException("null value for property " + propertyName + " for OWL individual " + individualName);

    for (Object value : individual.getPropertyValues(property)) {
      if (value instanceof RDFResource) {
        RDFResource resource = (RDFResource)value;
        String name = resource.getName();
        if (name.equals(propertyValue)) numberOfPropertyValues++;
      } else {
        if (value.equals(propertyValue)) numberOfPropertyValues++;
      } // if
    } // for

    return numberOfPropertyValues;
  } // getNumberOfPropertyValues

  public static void addOWLPropertyValue(OWLModel owlModel, OWLIndividual individual, String propertyName, Object propertyValue) 
    throws SWRLOWLUtilException
  {
    OWLProperty property = owlModel.getOWLProperty(propertyName);

    if (individual == null) throwException("invalid individual name " + individual.getPrefixedName());
    if (property == null) throwException("no " + propertyName + " property in ontology");
    if (propertyValue == null) throwException("null value for property " + propertyName + " for OWL individual " + individual.getPrefixedName());

    if (!individual.hasPropertyValue(property, propertyValue)) individual.addPropertyValue(property, propertyValue);
  } // addPropertyValue

  public static void addPropertyValue(OWLModel owlModel, OWLIndividual individual, OWLProperty property, Object propertyValue) 
    throws SWRLOWLUtilException
  {
    if (individual == null) throwException("invalid individual name " + individual.getPrefixedName());
    if (property == null) throwException("null property for individual " + individual.getPrefixedName());
    if (propertyValue == null) throwException("null value for property " + property.getPrefixedName() + " for OWL individual " + individual.getPrefixedName());

    if (!individual.hasPropertyValue(property, propertyValue)) individual.addPropertyValue(property, propertyValue);
  } // addPropertyValue

  public static void addPropertyValue(OWLModel owlModel, String subjectName, String propertyName, String propertyValue) 
    throws SWRLOWLUtilException
  {
    RDFResource subject = null;
    OWLProperty property = getOWLProperty(owlModel, propertyName);

    if (subjectName.startsWith("'")) subjectName = subjectName.substring(1, subjectName.length() - 1); 

    if (isOWLIndividual(owlModel, subjectName)) subject = getOWLIndividual(owlModel, subjectName);
    else if (isOWLClass(owlModel, subjectName)) subject = getOWLClass(owlModel, subjectName);
    else throw new SWRLOWLUtilException("invalid subject name " + subjectName + "; must be OWLClass or OWLIndividual");

    if (subject == null) throwException("invalid subject name " + subjectName);
    if (property == null) throwException("invalid property name " + propertyName);
    if (propertyValue == null) throwException("null value for property " + propertyName + " for subject " + subjectName);

    if (property.isObjectProperty()) {
      if (isOWLIndividual(owlModel, propertyValue)) {
        OWLIndividual objectIndividual = getOWLIndividual(owlModel, propertyValue);
        if (!subject.hasPropertyValue(property, objectIndividual)) subject.addPropertyValue(property, objectIndividual);
      } else if (isOWLNamedClass(owlModel, propertyValue)) {
        OWLClass objectClass = getOWLNamedClass(owlModel, propertyValue);
        if (!subject.hasPropertyValue(property, objectClass)) subject.addPropertyValue(property, objectClass);
      } else throw new SWRLOWLUtilException("invalid property value " + propertyValue + " for object property " + propertyName + 
                                            " for subject " + subjectName + "; must be class or individual name");

    } else { // TODO: deals only with strings
      if (!subject.hasPropertyValue(property, propertyValue)) subject.addPropertyValue(property, propertyValue);
    } // if
  } // addPropertyValue

  public static RDFResource getObjectPropertyValue(OWLIndividual individual, OWLProperty property) throws SWRLOWLUtilException
  { 
    Object o = individual.getPropertyValue(property);

    if (!(o instanceof RDFResource)) throw new SWRLOWLUtilException("value " + o + " of object property " + property.getPrefixedName() + 
                                                                    " associated with individual " + individual.getPrefixedName() + 
                                                                    " is not a valid object value");

    return (RDFResource)o;
  }

  public static Set<RDFResource> getObjectPropertyValues(OWLModel owlModel, String individualName, String propertyName) throws SWRLOWLUtilException
  { 
    return getObjectPropertyValues(owlModel, individualName, propertyName, true);
  } 

  public static Set<RDFResource> getObjectPropertyValues(OWLModel owlModel, OWLIndividual individual, String propertyName) throws SWRLOWLUtilException
  { 
    return getObjectPropertyValues(owlModel, individual.getName(), propertyName, true);
  } 

  public static Set<OWLIndividual> getOWLObjectPropertyIndividualValues(OWLModel owlModel, OWLIndividual individual, String propertyName,
                                                                         String expectedInstanceClassName) throws SWRLOWLUtilException
  {
    Set<OWLIndividual> individuals = new HashSet<OWLIndividual>();

    for (RDFResource value : getObjectPropertyValues(owlModel, individual, propertyName)) {
      if (!(value instanceof OWLIndividual)) 
        throw new SWRLOWLUtilException("value " + value + " for property " + propertyName + " associated with individual " +
                                       individual.getPrefixedName() + " is not an OWL individual");

      OWLIndividual individualValue = (OWLIndividual)value;
      if (!isOWLIndividualOfClass(owlModel, individualValue, expectedInstanceClassName))
        throw new SWRLOWLUtilException("object " + individual.getPrefixedName() + " value for property " + propertyName + " associated with individual " +
                                       individual.getPrefixedName() + " is not of type " + expectedInstanceClassName);

      individuals.add(individualValue);
    } // for
    return individuals;
  } 

  public static OWLIndividual getOWLObjectPropertyIndividualValue(OWLModel owlModel, OWLIndividual individual, String propertyName, 
                                                                  String expectedInstanceClassName) throws SWRLOWLUtilException
  {
    RDFResource value = getObjectPropertyValue(owlModel, individual, propertyName);

    if (!(value instanceof OWLIndividual))
      throw new SWRLOWLUtilException("invalid value for " + propertyName + " property associated with individual " + 
                                     individual.getPrefixedName() + "'; found " + value + ", expecting individual");
    OWLIndividual individualValue = (OWLIndividual)value;
    if (!isOWLIndividualOfClass(owlModel, individualValue, expectedInstanceClassName))
      throw new SWRLOWLUtilException("object " + individualValue.getPrefixedName() + " value for property " + propertyName + " associated with individual " + 
                                     individual.getPrefixedName() + " is not of type " +  expectedInstanceClassName);
    
    return individualValue;
  } // getObjectPropertyIndividualValue

  public static OWLProperty getOWLObjectPropertyPropertyValue(OWLModel owlModel, OWLIndividual individual, String propertyName) 
    throws SWRLOWLUtilException
  {
    RDFResource value = getObjectPropertyValue(owlModel, individual, propertyName);

    if (!(value instanceof OWLProperty))
      throw new SWRLOWLUtilException("invalid type for " + propertyName + " property associated with individual " + 
                                     individual.getPrefixedName() + "; found " + value + ", expecting property");
    
    return (OWLProperty)value;
  } 

  public static Set<RDFResource> getObjectPropertyValues(OWLModel owlModel, OWLIndividual individual, String propertyName, boolean mustExist) throws SWRLOWLUtilException
  { 
    return getObjectPropertyValues(owlModel, individual.getName(), propertyName, mustExist);
  } // getObjectPropertyValues

  public static Set<RDFResource> getObjectPropertyValues(OWLModel owlModel, String individualName, String propertyName, boolean mustExist) 
    throws SWRLOWLUtilException
  { 
    OWLIndividual individual = getOWLIndividual(owlModel, individualName, mustExist); // Will throw an exception if mustExist is true.
    OWLProperty property = getOWLProperty(owlModel, propertyName, mustExist); // Will throw an exception if mustExist is true.
    Set<RDFResource> result = new HashSet<RDFResource>();

    if (individual != null && property != null) {
      Iterator iterator = individual.getPropertyValues(property).iterator();
      while (iterator.hasNext()) {
        Object o = iterator.next();
        if (o instanceof RDFResource) result.add((RDFResource)o);
      } // while
    } // if
    return result;
  } // getObjectPropertyValues

  public static RDFResource getObjectPropertyValue(OWLModel owlModel, OWLIndividual individual, String propertyName)
    throws SWRLOWLUtilException
  {
    return getObjectPropertyValue(owlModel, individual, propertyName, true);
  } 

  public static RDFResource getObjectPropertyValue(OWLModel owlModel, OWLIndividual individual, String propertyName, boolean mustExist)
    throws SWRLOWLUtilException
  {
    return getObjectPropertyValue(owlModel, individual.getName(), propertyName, mustExist);
  } 

  public static RDFResource getObjectPropertyValue(OWLModel owlModel, String individualName, String propertyName)
    throws SWRLOWLUtilException
  {
    return getObjectPropertyValue(owlModel, individualName, propertyName, true);
  } 

  public static RDFResource getObjectPropertyValue(OWLModel owlModel, String individualName, String propertyName, boolean mustExist)
    throws SWRLOWLUtilException
  { 
    OWLIndividual individual = getOWLIndividual(owlModel, individualName, mustExist); // Will throw an exception if mustExist is true.
    OWLProperty property = getOWLProperty(owlModel, propertyName, true);
    Object propertyValue = (property == null && individual == null ? null : individual.getPropertyValue(property));

    if (mustExist && property == null) {
      throwException("no property " + propertyName + " associated with individual " + individual.getPrefixedName());
    } // if

    if (!(propertyValue instanceof RDFResource)) 
      throw new SWRLOWLUtilException("value " + propertyValue + " of object property " + propertyName + 
                                     " associated with individual " + individual.getPrefixedName() + " is not a valid object value");


    return (RDFResource)propertyValue;
  } 

  public static Object getDatavaluedPropertyValue(OWLModel owlModel, String individualName, String propertyName)
    throws SWRLOWLUtilException
  { 
    return getDatavaluedPropertyValue(owlModel, individualName, propertyName, true);
  } 

  public static Object getDatavaluedPropertyValue(OWLModel owlModel, String individualName, String propertyName, boolean mustExist)
    throws SWRLOWLUtilException
  { 
    OWLIndividual individual = getOWLIndividual(owlModel, individualName, mustExist); // Will throw an exception if mustExist is true.
    OWLProperty property = getOWLProperty(owlModel, propertyName, mustExist); // Will throw an exception if mustExist is true.
    Object propertyValue = (individual == null || property == null) ? null : individual.getPropertyValue(property);

    if (mustExist && propertyValue == null)
      throwException("no property '" + propertyName + "' associated with individual '" + individualName + "'");

    return propertyValue;
  } 

  public static Object getDatavaluedPropertyValue(OWLModel owlModel, OWLIndividual individual, String propertyName, boolean mustExist)
    throws SWRLOWLUtilException
  { 
    return getDatavaluedPropertyValue(owlModel, individual.getName(), propertyName, mustExist);
  } 

  public static Object getDatavaluedPropertyValue(OWLModel owlModel, OWLIndividual individual, OWLProperty property, boolean mustExist)
    throws SWRLOWLUtilException
  { 
    return getDatavaluedPropertyValue(owlModel, individual.getName(), property.getName(), mustExist);
  } 

  public static Set<Object> getDatavaluedPropertyValues(OWLModel owlModel, OWLIndividual individual, String propertyName, boolean mustExist)
    throws SWRLOWLUtilException
  { 
    OWLProperty property = getOWLProperty(owlModel, propertyName, true);
    Collection propertyValues = (property == null ? null :individual.getPropertyValues(property));
    Set<Object> result = new HashSet<Object>();

    if (property.isObjectProperty()) 
      throwException("expecting datatype property '" + propertyName + "' for '" + individual.getPrefixedName() + "'");

    if (mustExist && propertyValues == null) {
      throwException("no property '" + propertyName + "' associated with individual '" + individual.getPrefixedName() + "'");
    } // if

    if (propertyValues != null) {
      Iterator iterator = propertyValues.iterator();
      while (iterator.hasNext()) result.add(iterator.next());
    } // if

    return result;
  } 

  public static Set<Object> getDatavaluedPropertyValues(OWLModel owlModel, String individualName, String propertyName, boolean mustExist)
    throws SWRLOWLUtilException
  {
    OWLIndividual individual = getIndividual(owlModel, individualName);

    return getDatavaluedPropertyValues(owlModel, individual, propertyName, mustExist);
  } 

  public static Set<Object> getDatavaluedPropertyValues(OWLModel owlModel, String individualName, String propertyName)
    throws SWRLOWLUtilException
  {
    return getDatavaluedPropertyValues(owlModel, individualName, propertyName, true);
  }

  public static int getOWLDatavaluedPropertyValueAsInteger(OWLModel owlModel, OWLIndividual individual, String propertyName, boolean mustExist)
    throws SWRLOWLUtilException
  {
    String s = getDatavaluedPropertyValueAsString(owlModel, individual, propertyName, mustExist);
    int result = -1;

    try {
      result = Integer.parseInt(s);
    } catch (NumberFormatException e) {
      throwException("cannot convert property value '" + s + "' of property '" + propertyName + 
                     "' associated with individual '" + individual.getPrefixedName() + "' to integer");
    } // try
    return result;
  }

  public static int getDatavaluedPropertyValueAsInteger(OWLModel owlModel, String individualName, String propertyName, boolean mustExist)
    throws SWRLOWLUtilException
  {
    return getOWLDatavaluedPropertyValueAsInteger(owlModel, getIndividual(owlModel, individualName), propertyName, mustExist);
  }

  public static int getOWLDatavaluedPropertyValueAsInteger(OWLModel owlModel, OWLIndividual individual, String propertyName)
    throws SWRLOWLUtilException
  {
    return getOWLDatavaluedPropertyValueAsInteger(owlModel, individual, propertyName, true);
  }

  public static int getDatavaluedPropertyValueAsInteger(OWLModel owlModel, String individualName, String propertyName)
    throws SWRLOWLUtilException
  {
    return getOWLDatavaluedPropertyValueAsInteger(owlModel, getIndividual(owlModel, individualName), propertyName, true);
  } 

  public static long getOWLDatavaluedPropertyValueAsLong(OWLModel owlModel, OWLIndividual individual, String propertyName, boolean mustExist)
    throws SWRLOWLUtilException
  {
    String s = getDatavaluedPropertyValueAsString(owlModel, individual, propertyName, mustExist);
    long result = -1;

    try {
      result = Long.parseLong(s);
    } catch (NumberFormatException e) {
      throw new SWRLOWLUtilException("cannot convert property value '" + s + "' of property '" + propertyName + 
                                     "' associated with individual '" + individual.getPrefixedName() + "' to long");
    } // try
    return result;
  }

  public static long getDatavaluedPropertyValueAsLong(OWLModel owlModel, String individualName, String propertyName, boolean mustExist)
    throws SWRLOWLUtilException
  {
    return getOWLDatavaluedPropertyValueAsLong(owlModel, getIndividual(owlModel, individualName), propertyName, mustExist);
  }

  public static long getDatavaluedPropertyValueAsLong(OWLModel owlModel, OWLIndividual individual, String propertyName)
    throws SWRLOWLUtilException
  {
    return getOWLDatavaluedPropertyValueAsLong(owlModel, individual, propertyName, true);
  } 

  public static long getDatavaluedPropertyValueAsLong(OWLModel owlModel, String individualName, String propertyName)
    throws SWRLOWLUtilException
  {
    return getOWLDatavaluedPropertyValueAsLong(owlModel, getIndividual(owlModel, individualName), propertyName, true);
  }

  public static String getDatavaluedPropertyValueAsString(OWLModel owlModel, OWLIndividual individual, String propertyName)
    throws SWRLOWLUtilException
  { 
    return getDatavaluedPropertyValueAsString(owlModel, individual, propertyName, true);
  }

  public static String getDatavaluedPropertyValueAsString(OWLModel owlModel, OWLIndividual individual, String propertyName, boolean mustExist)
    throws SWRLOWLUtilException
  {
    OWLProperty property = getOWLProperty(owlModel, propertyName, mustExist);

    if (property == null) return null;

    return getDatavaluedPropertyValueAsString(owlModel, individual, property, mustExist);
  }

  public static String getDatavaluedPropertyValueAsString(OWLModel owlModel, String individualName, String propertyName, 
                                                          boolean mustExist, String defaultValue)
    throws SWRLOWLUtilException
  {
    return getDatavaluedPropertyValueAsString(owlModel, getIndividual(owlModel, individualName), propertyName, mustExist, defaultValue);
  } 

  public static String getDatavaluedPropertyValueAsString(OWLModel owlModel, String individualName, String propertyName)
    throws SWRLOWLUtilException
  {
    return getDatavaluedPropertyValueAsString(owlModel, getIndividual(owlModel, individualName), propertyName, true);
  } 

  public static String getDatavaluedPropertyValueAsString(OWLModel owlModel, String individualName, String propertyName, 
                                                          boolean mustExist)
    throws SWRLOWLUtilException
  {
    return getDatavaluedPropertyValueAsString(owlModel, getIndividual(owlModel, individualName), propertyName, mustExist);
  } 
    
  public static String getDatavaluedPropertyValueAsString(OWLModel owlModel, OWLIndividual individual, String propertyName, 
                                                          boolean mustExist, String defaultValue)
    throws SWRLOWLUtilException
  {
    OWLProperty property = getOWLProperty(owlModel, propertyName, mustExist);
    String propertValueAsString;

    if (property == null) return defaultValue;

    propertValueAsString = getDatavaluedPropertyValueAsString(owlModel, individual, property, mustExist);

    return propertValueAsString == null ? defaultValue : propertValueAsString;
  }

  public static String getDatavaluedPropertyValueAsString(OWLModel owlModel, OWLIndividual individual, OWLProperty property, boolean mustExist)
    throws SWRLOWLUtilException
  {
    Object propertyValue = getDatavaluedPropertyValue(owlModel, individual, property, mustExist);
    String result = null;

    if (propertyValue instanceof Boolean) {
      Boolean b = (Boolean)propertyValue;
      if (b.booleanValue()) result = "true"; else result = "false";
    } if (propertyValue == null) {
      result = null;
    } else result = propertyValue.toString();

    return result;
  }

  public static Boolean getDatavaluedPropertyValueAsBoolean(OWLModel owlModel, String individualName, String propertyName, boolean mustExist)
    throws SWRLOWLUtilException
  {
    return getDatavaluedPropertyValueAsBoolean(owlModel, getIndividual(owlModel, individualName), propertyName, mustExist);
  }

  public static Boolean getDatavaluedPropertyValueAsBoolean(OWLModel owlModel, String individualName, String propertyName)
    throws SWRLOWLUtilException
  {
    return getDatavaluedPropertyValueAsBoolean(owlModel, getIndividual(owlModel, individualName), propertyName, true);
  } // getDatavaluedPropertyValueAsBoolean

  public static Boolean getDatavaluedPropertyValueAsBoolean(OWLModel owlModel, OWLIndividual individual, String propertyName, boolean mustExist)
    throws SWRLOWLUtilException
  {
    Object propertyValue = getDatavaluedPropertyValue(owlModel, individual, propertyName, mustExist);

    if (propertyValue == null) return null;

    if (!(propertyValue instanceof Boolean)) {
      throwException("property value for '" + propertyName + "' associated with individual '" + individual.getPrefixedName() 
                     + "' is not a Boolean");
    } // if

    return (Boolean)propertyValue;
  } 

  public static Boolean getDatavaluedPropertyValueAsBoolean(OWLModel owlModel, OWLIndividual individual, OWLProperty property, boolean mustExist)
    throws SWRLOWLUtilException
  {
    Object propertyValue = getDatavaluedPropertyValue(owlModel, individual, property, mustExist);

    if (propertyValue == null) return null;

    if (!(propertyValue instanceof Boolean))
      throwException("property value for " + property.getPrefixedName() + " in individual " + individual.getPrefixedName() + " is not a Boolean");

    return (Boolean)propertyValue;
  } // getDatavaluedPropertyValueAsBoolean

  public static Collection getDatavaluedPropertyValueAsCollection(OWLModel owlModel, OWLIndividual individual, String propertyName)
    throws SWRLOWLUtilException
  {
    return getDatavaluedPropertyValueAsCollection(owlModel, individual, propertyName, true);
  } // getDatavaluedPropertyValueAsCollection

  public static Collection getDatavaluedPropertyValueAsCollection(OWLModel owlModel, String individualName, String propertyName)
    throws SWRLOWLUtilException
  {
    return getDatavaluedPropertyValueAsCollection(owlModel, getIndividual(owlModel, individualName), propertyName, true);
  } // getDatavaluedPropertyValueAsCollection

  public static Collection getDatavaluedPropertyValueAsCollection(OWLModel owlModel, OWLIndividual individual, String propertyName,
                                                                  boolean mustExist) throws SWRLOWLUtilException
  {
    Object propertyValue = getDatavaluedPropertyValue(owlModel, individual, propertyName, mustExist);
    Collection result = new ArrayList();

    if (propertyValue == null) return result;

    if (propertyValue instanceof RDFSLiteral) result.add(propertyValue);
    else if (propertyValue instanceof Collection) result = (Collection)propertyValue;
    else throwException("property value for '" + propertyName + "' associated with individual '" + individual.getPrefixedName() + 
                        "' is not a Collection");

    return result;
  } // getDatavaluedPropertyValueAsCollection

  public static Collection getDatavaluedPropertyValueAsCollection(OWLModel owlModel, OWLIndividual individual, 
                                                                  OWLProperty property, boolean mustExist) throws SWRLOWLUtilException
  {
    Object propertyValue = getDatavaluedPropertyValue(owlModel, individual, property, mustExist);
    Collection result = new ArrayList();

    if (propertyValue == null) return result;

    if (propertyValue instanceof RDFSLiteral) result.add(propertyValue);
    else if (propertyValue instanceof Collection) result = (Collection)propertyValue;
    else throwException("property value for '" + property.getPrefixedName() + "' associated with individual '" + individual.getPrefixedName() + 
                        "' is not a Collection");

    return result;
  } // getDatavaluedPropertyValueAsCollection

  public static List<OWLNamedClass> getDirectSubClassesOf(OWLModel owlModel, String className) throws SWRLOWLUtilException
  { 
    OWLNamedClass cls = getNamedClass(owlModel, className);

    return new ArrayList<OWLNamedClass>(cls.getNamedSubclasses(false));
  } // getDirectSubClassesOf

  public static List<OWLNamedClass> getSubClassesOf(OWLModel owlModel, String className) throws SWRLOWLUtilException
  { 
    OWLNamedClass cls = getNamedClass(owlModel, className);

    return new ArrayList<OWLNamedClass>(cls.getNamedSubclasses(true));
  } // getSubClassesOf

  public static List<OWLNamedClass> getDirectSuperClassesOf(OWLModel owlModel, String className) throws SWRLOWLUtilException
  { 
    OWLNamedClass cls = getNamedClass(owlModel, className);

    return new ArrayList<OWLNamedClass>(cls.getNamedSuperclasses(false));
  } // getDirectSuperClassesOf

  public static List<OWLNamedClass> getSuperClassesOf(OWLModel owlModel, String className) throws SWRLOWLUtilException
  { 
    OWLNamedClass cls = getNamedClass(owlModel, className);

    return new ArrayList<OWLNamedClass>(cls.getNamedSuperclasses(true));
  } // getSuperClassesOf

  public static List<OWLProperty> getDirectSubPropertiesOf(OWLModel owlModel, String propertyName) throws SWRLOWLUtilException
  { 
    OWLProperty property = getOWLProperty(owlModel, propertyName);

    return new ArrayList<OWLProperty>(property.getSubproperties(false));
  } // getDirectSubPropertiesOf

  public static List<OWLProperty> getSubPropertiesOf(OWLModel owlModel, String propertyName) throws SWRLOWLUtilException
  { 
    OWLProperty property = getOWLProperty(owlModel, propertyName);

    return new ArrayList<OWLProperty>(property.getSubproperties(true));
  } // getSubPropertiesOf

  public static List<OWLProperty> getDirectSuperPropertiesOf(OWLModel owlModel, String propertyName) throws SWRLOWLUtilException
  { 
    OWLProperty property = getOWLProperty(owlModel, propertyName);

    return new ArrayList<OWLProperty>(property.getSuperproperties(false));
  } // getDirectSuperPropertiesOf

  public static List<OWLProperty> getSuperPropertiesOf(OWLModel owlModel, String propertyName) throws SWRLOWLUtilException
  { 
    OWLProperty property = getOWLProperty(owlModel, propertyName);

    return new ArrayList<OWLProperty>(property.getSuperproperties(true));
  } // getSuperPropertiesOf

  public static Set<OWLProperty> getDomainProperties(OWLModel owlModel, String className, boolean transitive) throws SWRLOWLUtilException
  {
    OWLClass cls = getClass(owlModel, className);
    Set<OWLProperty> result = new HashSet<OWLProperty>();
    Collection domainProperties = cls.getUnionDomainProperties(transitive);

    // TODO: bug in Property.getUnionDomain that causes it to return non RDFResource objects so we need to work around it.
    // for (RDFResource resource : resources) result.add(resource.getName());

    if (domainProperties != null) {
      Iterator iterator = cls.getUnionDomainProperties().iterator();
      while (iterator.hasNext()) {
        Object o = iterator.next();
        if (o instanceof OWLProperty) result.add((OWLProperty)o);
      } // while
    } // if

    return result;
  } // getDomainProperties

  public static Set<String> rdfResources2OWLNamedClassNames(Collection resources) 
  {
    Set<String> result = new HashSet<String>();
    
    Iterator iterator = resources.iterator();
    while (iterator.hasNext()) {
      Object o = iterator.next();
      if (o instanceof OWLNamedClass) result.add(((OWLNamedClass)o).getName());
    } // if

    return result;
    
  } // rdfResources2OWLNamedClassNames

  public static Set<String> rdfResources2URIs(Collection resources) 
  {
    Set<String> result = new HashSet<String>();
    
    Iterator iterator = resources.iterator();
    while (iterator.hasNext()) {
      Object o = iterator.next();
      if (o instanceof RDFResource) result.add(((RDFResource)o).getURI());
    } // if

    return result;
  } // rdfResources2Names            

  public static Set<String> rdfResources2OWLNamedClassURIs(Collection resources) 
  {
    Set<String> result = new HashSet<String>();
    
    Iterator iterator = resources.iterator();
    while (iterator.hasNext()) {
      Object o = iterator.next();
      if (o instanceof OWLNamedClass) result.add(((OWLNamedClass)o).getURI());
    } // if

    return result;
  } 

  public static Set<String> rdfResources2OWLPropertyURIs(Collection resources) 
  {
    Set<String> result = new HashSet<String>();
    
    Iterator iterator = resources.iterator();
    while (iterator.hasNext()) {
      Object o = iterator.next();
      if (o instanceof OWLProperty) result.add(((OWLProperty)o).getURI());
    } // if

    return result;
  } 

  public static boolean hasInconsistentClasses(OWLModel owlModel) 
  {
    return !owlModel.getInconsistentClasses().isEmpty();
  } 

  public static String createNewResourceName(OWLModel owlModel, String localNamePrefix) 
  {
    return owlModel.createNewResourceName(localNamePrefix);
  } // createNewResourceName

  public static RDFSDatatype getRDFSDatatype(OWLModel owlModel, String type) throws SWRLOWLUtilException
  {
    RDFSDatatype datatype = owlModel.getRDFSDatatypeByName(type);

    if (datatype == null) throw new SWRLOWLUtilException("error getting RDFSDatatype " + type);

    return datatype;
  } // getRDFSDatatype

  public static RDFSLiteral createRDFSLiteral(OWLModel owlModel, String value, RDFSDatatype datatype) 
    throws SWRLOWLUtilException
  {
    RDFSLiteral literal = owlModel.createRDFSLiteral(value, datatype);

    if (literal == null) 
      throw new SWRLOWLUtilException("error creating RDFSLiteral '" + value + "' of type '" + datatype + "'");

    return literal;
  } // createRDFSLiteral

  public static OWLClass createOWLClassDescription(OWLModel owlModel, String expression) throws SWRLOWLUtilException
  {
    OWLClassParser parser = owlModel.getOWLClassParser();
    OWLClass cls = null;

    try {
      cls = (OWLClass)parser.parseClass(owlModel, expression);
    } catch (OWLClassParseException e) {
      throw new SWRLOWLUtilException("OWL class expression " + expression + " not valid: " + e.getMessage());
    } // try

    return cls;
  } // createOWLClassDescription

  public static OWLNamedClass getOWLThingClass(OWLModel owlModel) 
  {
    return owlModel.getOWLThingClass();
  } // getOWLThingClass

  public static RDFProperty getOWLSameAsProperty(OWLModel owlModel)
  {
    return owlModel.getOWLSameAsProperty();
  } // getOWLSameAsProperty

  public static Collection getOWLAllDifferents(OWLModel owlModel)
  {
    return owlModel.getOWLAllDifferents();
  } // getOWLAllDifferents

  public static RDFProperty getOWLDifferentFromProperty(OWLModel owlModel)
  {
    return owlModel.getOWLDifferentFromProperty();
  } // getOWLDifferentFromProperty

  public static OWLProperty getOWLProperty(OWLModel owlModel, String propertyName)
  {
    return owlModel.getOWLProperty(propertyName);
  } // getOWLProperty

  public static OWLProperty getOWLProperty(OWLModel owlModel, String propertyName, boolean mustExist) throws SWRLOWLUtilException
  {
    OWLProperty property = owlModel.getOWLProperty(propertyName);

    if (mustExist && property == null) throw new SWRLOWLUtilException("no property named '" + propertyName + "' in ontology");

    return property;
  } 

  public static OWLIndividual getOWLIndividual(OWLModel owlModel, String individualName)
  {
    return owlModel.getOWLIndividual(individualName);
  } // getOWLIndividual

  public static RDFSNamedClass getRDFSNamedClass(OWLModel owlModel, String className)
  {
    return owlModel.getRDFSNamedClass(className);
  } // getRDFSNamedClass

  public static boolean isSWRLBuiltIn(OWLModel owlModel, String builtInName)
  {
    RDFResource resource = owlModel.getRDFResource(builtInName);
    return resource != null && resource.getProtegeType().getName().equals(edu.stanford.smi.protegex.owl.swrl.model.SWRLNames.Cls.BUILTIN);
  } // isSWRLBuiltIn

  public static boolean isValidClassName(OWLModel owlModel, String className)
  {
    return owlModel.isValidResourceName(className, owlModel.getRDFSNamedClassClass());
  } // isValidClassName

  public static void checkIfIsValidClassName(OWLModel owlModel, String className) throws SWRLOWLUtilException
  {
    if (!isValidClassName(owlModel, className)) throw new SWRLOWLUtilException("invalid name for named class " + className);
  } 

  public static boolean isValidURI(String uri)
  {
    return URIUtilities.isValidURI(uri);
  } 

  public static String getPrefixForResourceName(OWLModel owlModel, String resourceName)
  {
    return owlModel.getPrefixForResourceName(resourceName);
  } 

  public static String getLocalNameForURI(OWLModel owlModel, String uri)
  {
    return owlModel.getLocalNameForURI(uri);
  } 

  public static OWLNamedClass getOWLClassFromName(OWLModel owlModel, String name) throws SWRLOWLUtilException
  {
    String id = ParserUtils.dequoteIdentifier(name);
    OWLNamedClass cls = null;

    try {
      cls = ParserUtils.getOWLClassFromName(owlModel, id);
    } catch (AmbiguousNameException e) {
      throw new SWRLOWLUtilException("ambiguous class name " + name);
    } // if 

    return cls;
  } 

  public static OWLDatatypeProperty getOWLDatatypePropertyFromName(OWLModel owlModel, String name) throws SWRLOWLUtilException
  {
    String id = ParserUtils.dequoteIdentifier(name);
    OWLDatatypeProperty property = null;

    try {
      property = ParserUtils.getOWLDatatypePropertyFromName(owlModel, id);
    } catch (AmbiguousNameException e) {
      throw new SWRLOWLUtilException("ambiguous data property name " + name);
    } // if 

    return property;
  } 

  public static OWLObjectProperty getOWLObjectPropertyFromName(OWLModel owlModel, String name) throws SWRLOWLUtilException
  {
    String id = ParserUtils.dequoteIdentifier(name);
    OWLObjectProperty property = null;

    try {
      property = ParserUtils.getOWLObjectPropertyFromName(owlModel, id);
    } catch (AmbiguousNameException e) {
      throw new SWRLOWLUtilException("ambiguous object property name " + name);
    } // if 

    return property;
  } 

  public static OWLIndividual getOWLIndividualFromName(OWLModel owlModel, String name) throws SWRLOWLUtilException
  {
    String id = ParserUtils.dequoteIdentifier(name);
    OWLIndividual individual = null;

    try {
      individual = ParserUtils.getOWLIndividualFromName(owlModel, id);
    } catch (AmbiguousNameException e) {
      throw new SWRLOWLUtilException("ambiguous individual name " + name);
    } // if 

    return individual;
  }

  private static void throwException(String message) throws SWRLOWLUtilException
  {
    throw new SWRLOWLUtilException(message);
  } 
} 
