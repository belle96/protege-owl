
package edu.stanford.smi.protegex.owl.swrl.owlapi.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.stanford.smi.protegex.owl.model.NamespaceUtil;
import edu.stanford.smi.protegex.owl.model.OWLAllDifferent;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLNames;
import edu.stanford.smi.protegex.owl.model.RDFIndividual;
import edu.stanford.smi.protegex.owl.model.RDFList;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.RDFSLiteral;
import edu.stanford.smi.protegex.owl.model.triplestore.TripleStoreModel;
import edu.stanford.smi.protegex.owl.swrl.bridge.ArgumentFactory;
import edu.stanford.smi.protegex.owl.swrl.bridge.Atom;
import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInAtom;
import edu.stanford.smi.protegex.owl.swrl.bridge.ClassArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.ClassAtom;
import edu.stanford.smi.protegex.owl.swrl.bridge.DataPropertyArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.DataRangeAtom;
import edu.stanford.smi.protegex.owl.swrl.bridge.DataValueArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.DatavaluedPropertyAtom;
import edu.stanford.smi.protegex.owl.swrl.bridge.DifferentIndividualsAtom;
import edu.stanford.smi.protegex.owl.swrl.bridge.IndividualArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.IndividualPropertyAtom;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLDataValue;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLDataValueFactory;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLPropertyPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.bridge.ObjectPropertyArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.PropertyArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.SWRLRule;
import edu.stanford.smi.protegex.owl.swrl.bridge.SameIndividualAtom;
import edu.stanford.smi.protegex.owl.swrl.bridge.VariableArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.BuiltInException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.InvalidPropertyNameException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.OWLConversionFactoryException;
import edu.stanford.smi.protegex.owl.swrl.bridge.impl.BuiltInAtomImpl;
import edu.stanford.smi.protegex.owl.swrl.bridge.impl.ClassAtomImpl;
import edu.stanford.smi.protegex.owl.swrl.bridge.impl.DataValueArgumentImpl;
import edu.stanford.smi.protegex.owl.swrl.bridge.impl.DatavaluedPropertyAtomImpl;
import edu.stanford.smi.protegex.owl.swrl.bridge.impl.DifferentIndividualsAtomImpl;
import edu.stanford.smi.protegex.owl.swrl.bridge.impl.IndividualPropertyAtomImpl;
import edu.stanford.smi.protegex.owl.swrl.bridge.impl.OWLDataValueImpl;
import edu.stanford.smi.protegex.owl.swrl.bridge.impl.SWRLRuleImpl;
import edu.stanford.smi.protegex.owl.swrl.bridge.impl.SameIndividualAtomImpl;
import edu.stanford.smi.protegex.owl.swrl.bridge.xsd.XSDAnyURI;
import edu.stanford.smi.protegex.owl.swrl.bridge.xsd.XSDDate;
import edu.stanford.smi.protegex.owl.swrl.bridge.xsd.XSDDateTime;
import edu.stanford.smi.protegex.owl.swrl.bridge.xsd.XSDDuration;
import edu.stanford.smi.protegex.owl.swrl.bridge.xsd.XSDTime;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLFactory;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLClass;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLClassAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLClassPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLDataFactory;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLDataProperty;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLDataPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLDifferentIndividualsAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLLiteral;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLNamedIndividual;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLObjectPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLOntology;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLProperty;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLSameIndividualAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLSomeValuesFrom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLSubClassAxiom;
import edu.stanford.smi.protegex.owl.swrl.parser.SWRLParseException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.DataValueConversionException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.SQWRLException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.impl.DataValueImpl;
import edu.stanford.smi.protegex.owl.swrl.util.SWRLOWLUtil;

/**
 * Class to convert between OWLAPI-like entities and Protege-OWL entities.
 */
public class OWLOntologyImpl implements OWLOntology
{
  private OWLModel owlModel;
  private SWRLFactory swrlFactory;
  private OWLDataFactory owlFactory;
  private OWLDataValueFactory owlDataValueFactory;
  private ArgumentFactory argumentFactory;
  private Map<String, OWLClass> classes;
  private Map<String, OWLObjectProperty> objectProperties;
  private Map<String, OWLDataProperty> dataProperties;
  private Map<String, OWLNamedIndividual> individuals;

  public OWLOntologyImpl(OWLModel owlModel) 
  { 
    this.owlModel = owlModel;
    this.owlFactory = new OWLDataFactoryImpl();
    swrlFactory = new SWRLFactory(owlModel);
    
    argumentFactory = ArgumentFactory.getFactory();
    
    classes = new HashMap<String, OWLClass>();
    objectProperties = new HashMap<String, OWLObjectProperty>();
    dataProperties = new HashMap<String, OWLDataProperty>();
    individuals = new HashMap<String, OWLNamedIndividual>();
  }
  
  public boolean containsClassInSignature(String classURI, boolean includesImportsClosure) { return SWRLOWLUtil.isOWLClass(owlModel, classURI); }
  public boolean containsObjectPropertyInSignature(String propertyURI, boolean includesImportsClosure) { return SWRLOWLUtil.isOWLObjectProperty(owlModel, propertyURI); }
  public boolean containsDataPropertyInSignature(String propertyURI, boolean includesImportsClosure) { return SWRLOWLUtil.isOWLDataProperty(owlModel, propertyURI); }
  public boolean containsIndividualInSignature(String individualURI, boolean includesImportsClosure) { return SWRLOWLUtil.isOWLIndividual(owlModel, individualURI); }
  public boolean isOWLNamedIndividualOfClass(String individualURI, String classURI) { return SWRLOWLUtil.isOWLIndividualOfType(owlModel, individualURI, classURI); }
  public boolean isSWRLBuiltIn(String builtInURI) { return SWRLOWLUtil.isSWRLBuiltIn(owlModel, builtInURI); }
  public String createNewResourceURI(String prefix) { return SWRLOWLUtil.createNewResourceName(owlModel, prefix); }
  
  public Set<SWRLRule> getSWRLRules() throws OWLConversionFactoryException, SQWRLException, BuiltInException
  {
    Collection<edu.stanford.smi.protegex.owl.swrl.model.SWRLImp> imps = swrlFactory.getImps();
    Set<SWRLRule> result = new HashSet<SWRLRule>();
    
    for (edu.stanford.smi.protegex.owl.swrl.model.SWRLImp imp : imps) {
      if (imp.isEnabled()) {
        SWRLRule rule = getSWRLRule(imp.getName());
        result.add(rule);
      } // if
    } // while

    return result;
  } // getSWRLRules

  public SWRLRule createSWRLRule(String ruleName, String ruleText) throws OWLConversionFactoryException, SWRLParseException
  {
  	swrlFactory.createImp(ruleName, ruleText);
  	return getSWRLRule(ruleName);
  } 
  
  public SWRLRule getSWRLRule(String ruleName) throws OWLConversionFactoryException
  {
    List<Atom> bodyAtoms = new ArrayList<Atom>();
    List<Atom> headAtoms = new ArrayList<Atom>();
    edu.stanford.smi.protegex.owl.swrl.model.SWRLImp imp = swrlFactory.getImp(ruleName);

    if (imp == null) throw new OWLConversionFactoryException("invalid rule name: " + ruleName);

    Iterator iterator = imp.getBody().getValues().iterator();
    while (iterator.hasNext()) {
    	edu.stanford.smi.protegex.owl.swrl.model.SWRLAtom swrlAtom = (edu.stanford.smi.protegex.owl.swrl.model.SWRLAtom)iterator.next();
      bodyAtoms.add(convertSWRLAtom(swrlAtom));
    } // while 

    iterator = imp.getHead().getValues().iterator();
    while (iterator.hasNext()) {
    	edu.stanford.smi.protegex.owl.swrl.model.SWRLAtom swrlAtom = (edu.stanford.smi.protegex.owl.swrl.model.SWRLAtom)iterator.next();
      headAtoms.add(convertSWRLAtom(swrlAtom));
    } // while 

    return new SWRLRuleImpl(imp.getPrefixedName(), bodyAtoms, headAtoms);
  } 

  public OWLClass createOWLClass() 
  { 
    String anonymousURI = SWRLOWLUtil.getNextAnonymousResourceName(owlModel);

    return new OWLClassImpl(anonymousURI);
  }

  public OWLClass getOWLClass(String classURI) throws OWLConversionFactoryException
  { 
  	OWLClassImpl owlClassImpl;
 
  	if (classes.containsKey(classURI)) return classes.get(classURI);
  	else {
  	  edu.stanford.smi.protegex.owl.model.OWLNamedClass owlNamedClass = SWRLOWLUtil.createOWLNamedClass(owlModel, classURI);
      owlClassImpl = new OWLClassImpl(classURI);
      classes.put(classURI, owlClassImpl);
            
      if (!classURI.equals(OWLNames.Cls.THING)) {
      	 for (String superClassURI : SWRLOWLUtil.rdfResources2OWLNamedClassURIs(owlNamedClass.getNamedSuperclasses(false)))
      	   owlClassImpl.addSuperClass(getOWLClass(superClassURI));
      	 for (String subClassURI : SWRLOWLUtil.rdfResources2OWLNamedClassURIs(owlNamedClass.getNamedSubclasses(false)))
      	   owlClassImpl.addSubClass(getOWLClass(subClassURI));
      	 for (String equivalentClassURI : SWRLOWLUtil.rdfResources2OWLNamedClassNames(owlNamedClass.getEquivalentClasses()))
      	   owlClassImpl.addEquivalentClass(getOWLClass(equivalentClassURI));

      } // if
  	} // if

    return owlClassImpl;
  }
  
  public OWLNamedIndividual getOWLIndividual(String individualURI) throws OWLConversionFactoryException
  { 
  	OWLIndividualImpl owlIndividual;
  	
  	if (individuals.containsKey(individualURI)) return individuals.get(individualURI);
  	else {
  		edu.stanford.smi.protegex.owl.model.OWLIndividual individual = SWRLOWLUtil.createOWLIndividual(owlModel, individualURI);
  	  owlIndividual = new OWLIndividualImpl(individualURI);
  	  individuals.put(individualURI, owlIndividual);
  	  
      buildDefiningClasses(owlIndividual, individual);
      buildSameAsIndividuals(owlIndividual, individual);
      buildDifferentFromIndividuals(owlIndividual, individual);
  	} // if
    return owlIndividual; 
  }

  public OWLObjectProperty getOWLObjectProperty(String propertyURI) throws OWLConversionFactoryException
  {
  	OWLObjectPropertyImpl owlObjectProperty;
  	
  	if (objectProperties.containsKey(propertyURI)) return objectProperties.get(propertyURI);
  	else {
  		edu.stanford.smi.protegex.owl.model.OWLObjectProperty property = SWRLOWLUtil.createOWLObjectProperty(owlModel, propertyURI);
      owlObjectProperty = new OWLObjectPropertyImpl(propertyURI);
      objectProperties.put(propertyURI,	owlObjectProperty);

      initializeProperty(owlObjectProperty, property);
  	} // if
  	
    return owlObjectProperty;
  }

  public OWLDataProperty getOWLDataProperty(String propertyURI) throws OWLConversionFactoryException
  { 
  	OWLDataPropertyImpl owlDataProperty;
  	
  	if (dataProperties.containsKey(propertyURI)) return dataProperties.get(propertyURI);
  	else {
  	  edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty property = SWRLOWLUtil.createOWLDatatypeProperty(owlModel, propertyURI);
      owlDataProperty = new OWLDataPropertyImpl(propertyURI);
      dataProperties.put(propertyURI, owlDataProperty);

      initializeProperty(owlDataProperty, property);     
  	} // if

    return owlDataProperty;
  } 

  public void writeOWLClassDeclaration(OWLClass owlClass) throws OWLConversionFactoryException
  {
    String classURI = owlClass.getURI();
    edu.stanford.smi.protegex.owl.model.OWLClass cls, superclass;

    if (SWRLOWLUtil.isOWLNamedClass(owlModel, classURI)) cls = SWRLOWLUtil.getOWLNamedClass(owlModel, classURI);
    else cls = SWRLOWLUtil.getOWLNamedClass(owlModel, classURI);
    
    for (OWLClass superClass : owlClass.getSuperClasses()) {
    	String superClassURI = superClass.getURI();
      if (SWRLOWLUtil.isOWLNamedClass(owlModel, superClassURI)) superclass = SWRLOWLUtil.getOWLNamedClass(owlModel, superClassURI);
      else superclass = SWRLOWLUtil.getOWLNamedClass(owlModel, superClassURI);
      
      if (!cls.isSubclassOf(superclass)) cls.addSuperclass(superclass);
    } // for
  }

  public void writeOWLIndividualDeclaration(OWLNamedIndividual owlIndividual) throws OWLConversionFactoryException
  {
    String individualURI = owlIndividual.getURI();
    edu.stanford.smi.protegex.owl.model.OWLIndividual individual;
    
    if (SWRLOWLUtil.isIndividual(owlModel, individualURI)) individual = SWRLOWLUtil.getIndividual(owlModel, individualURI);
    else individual = SWRLOWLUtil.createIndividual(owlModel, individualURI);
    
    for (OWLClass owlClass : owlIndividual.getTypes()) {
      edu.stanford.smi.protegex.owl.model.RDFSClass cls = SWRLOWLUtil.getOWLNamedClass(owlModel, owlClass.getURI());

      if (!individual.hasRDFType(cls)) { 
        if (individual.hasRDFType(SWRLOWLUtil.getOWLThingClass(owlModel))) individual.setRDFType(cls);
        else individual.addRDFType(cls);
      } // if
    } // for
  }

  public void writeOWLAxiom(OWLAxiom axiom) throws OWLConversionFactoryException
  {
    if (axiom instanceof OWLClassAssertionAxiom) write2OWLModel((OWLClassAssertionAxiom)axiom);
    else if (axiom instanceof OWLClassPropertyAssertionAxiom) write2OWLModel((OWLClassPropertyAssertionAxiom)axiom);
    else if (axiom instanceof OWLDataPropertyAssertionAxiom) write2OWLModel((OWLDataPropertyAssertionAxiom)axiom);
    else if (axiom instanceof OWLObjectPropertyAssertionAxiom) write2OWLModel((OWLObjectPropertyAssertionAxiom)axiom);
    else if (axiom instanceof OWLPropertyPropertyAssertionAxiom) write2OWLModel((OWLPropertyPropertyAssertionAxiom)axiom);
    else if (axiom instanceof OWLSomeValuesFrom) write2OWLModel((OWLSomeValuesFrom)axiom);
    else if (axiom instanceof OWLSubClassAxiom) write2OWLModel((OWLSubClassAxiom)axiom);
    else throw new OWLConversionFactoryException("unsupported OWL axiom: " + axiom);
  } // putOWLAxiom

  public boolean isValidURI(String uri)
  {
    return SWRLOWLUtil.isValidURI(uri); 
  }

  public Set<OWLNamedIndividual> getAllOWLIndividualsOfClass(String classURI) throws OWLConversionFactoryException
  {
    RDFSClass rdfsClass = SWRLOWLUtil.getRDFSNamedClass(owlModel, classURI);
    Collection instances = new ArrayList();
    Set<OWLNamedIndividual> result = new HashSet<OWLNamedIndividual>();

    if (rdfsClass != null) instances.addAll(rdfsClass.getInstances(true));

    Iterator iterator = instances.iterator();
    while (iterator.hasNext()) {
      Object o = iterator.next();
      if (o instanceof edu.stanford.smi.protegex.owl.model.OWLIndividual) { 
        edu.stanford.smi.protegex.owl.model.OWLIndividual individual = (edu.stanford.smi.protegex.owl.model.OWLIndividual)o;
        result.add(getOWLIndividual(individual.getURI()));
      } 
    } // while
    return result;
  } // getAllOWLIndividualsOfClass  
  
  public boolean couldBeOWLNamedClass(String classURI)
  {
	 RDFResource resource= SWRLOWLUtil.getRDFResource(owlModel, classURI);
	 
	 return (resource == null || resource instanceof OWLNamedClass);
  }
  
  public String uri2PrefixedName(String uri)
  {
  	 String result = NamespaceUtil.getPrefixedName(owlModel, uri);
  	 
  	 return result;
  } 
  
  public String prefixedName2URI(String prefixedName)
  {
  	String result = NamespaceUtil.getFullName(owlModel, prefixedName);
  	
  	return result;
  } 
  
  public static DataValueArgument convertRDFSLiteral2DataValueArgument(OWLModel owlModel, edu.stanford.smi.protegex.owl.model.RDFSLiteral literal) 
    throws OWLConversionFactoryException 
  { 
    edu.stanford.smi.protegex.owl.model.RDFSDatatype datatype = literal.getDatatype();
    DataValueArgument dataValueArgument = null;

    try {
      if ((datatype == owlModel.getXSDint()) || (datatype == owlModel.getXSDinteger()))
        dataValueArgument = new DataValueArgumentImpl(new DataValueImpl(literal.getInt()));
      else if (datatype == owlModel.getXSDshort()) 
        dataValueArgument = new DataValueArgumentImpl(new DataValueImpl(literal.getShort()));
      else if (datatype == owlModel.getXSDlong()) 
        dataValueArgument = new DataValueArgumentImpl(new DataValueImpl(literal.getLong()));
      else if (datatype == owlModel.getXSDboolean()) 
        dataValueArgument = new DataValueArgumentImpl(new DataValueImpl(literal.getBoolean()));
      else if (datatype == owlModel.getXSDfloat()) 
        dataValueArgument = new DataValueArgumentImpl(new DataValueImpl(literal.getFloat()));
      else if (datatype == owlModel.getXSDdouble()) 
        dataValueArgument = new DataValueArgumentImpl(new DataValueImpl(literal.getDouble()));
      else if ((datatype == owlModel.getXSDstring())) 
        dataValueArgument = new DataValueArgumentImpl(new DataValueImpl(literal.getString()));
      else if ((datatype == owlModel.getXSDtime())) 
        dataValueArgument = new DataValueArgumentImpl(new DataValueImpl(new XSDTime(literal.getString())));
      else if ((datatype == owlModel.getXSDanyURI())) 
        dataValueArgument = new DataValueArgumentImpl(new DataValueImpl(new XSDAnyURI(literal.getString())));
      else if ((datatype == owlModel.getXSDbyte())) 
        dataValueArgument = new DataValueArgumentImpl(new DataValueImpl(Byte.valueOf(literal.getString())));
      else if ((datatype == owlModel.getXSDduration())) 
        dataValueArgument = new DataValueArgumentImpl(new DataValueImpl(new XSDDuration(literal.getString())));
      else if ((datatype == owlModel.getXSDdateTime())) 
        dataValueArgument = new DataValueArgumentImpl(new DataValueImpl(new XSDDateTime(literal.getString())));
      else if ((datatype == owlModel.getXSDdate())) 
        dataValueArgument = new DataValueArgumentImpl(new DataValueImpl(new XSDDate(literal.getString())));
      else throw new OWLConversionFactoryException("cannot create an OWLDataValue object for RDFS literal " + literal.getString() +
                                                   " of type " + datatype);
    } catch (DataValueConversionException e) {
      throw new OWLConversionFactoryException("error creating an OWLDataValue object for RDFS literal value " + literal.getString() + 
                                              " with type " + datatype.getURI() + ": " + e.getMessage());
    } // try

    return dataValueArgument;
  }

  public static OWLLiteral convertRDFSLiteral2OWLLiteral(OWLModel owlModel, edu.stanford.smi.protegex.owl.model.RDFSLiteral literal) 
    throws OWLConversionFactoryException 
  { 
    edu.stanford.smi.protegex.owl.model.RDFSDatatype datatype = literal.getDatatype();
    OWLDataValue dataValue = null;

    try {
      if ((datatype == owlModel.getXSDint()) || (datatype == owlModel.getXSDinteger()))
        dataValue = new OWLDataValueImpl(literal.getInt());
      else if (datatype == owlModel.getXSDshort()) 
        dataValue = new OWLDataValueImpl(literal.getShort());
      else if (datatype == owlModel.getXSDlong()) 
        dataValue = new OWLDataValueImpl(literal.getLong());
      else if (datatype == owlModel.getXSDboolean()) 
        dataValue = new OWLDataValueImpl(literal.getBoolean());
      else if (datatype == owlModel.getXSDfloat()) 
        dataValue = new OWLDataValueImpl(literal.getFloat());
      else if (datatype == owlModel.getXSDdouble()) 
      dataValue = new OWLDataValueImpl(literal.getDouble());
      else if ((datatype == owlModel.getXSDstring())) 
        dataValue = new OWLDataValueImpl(literal.getString());
      else if ((datatype == owlModel.getXSDtime())) 
        dataValue = new OWLDataValueImpl(new XSDTime(literal.getString()));
      else if ((datatype == owlModel.getXSDanyURI())) 
        dataValue = new OWLDataValueImpl(new XSDAnyURI(literal.getString()));
      else if ((datatype == owlModel.getXSDbyte())) 
        dataValue = new OWLDataValueImpl(Byte.valueOf(literal.getString()));
      else if ((datatype == owlModel.getXSDduration())) 
        dataValue = new OWLDataValueImpl(new XSDDuration(literal.getString()));
      else if ((datatype == owlModel.getXSDdateTime())) 
        dataValue = new OWLDataValueImpl(new XSDDateTime(literal.getString()));
      else if ((datatype == owlModel.getXSDdate())) 
        dataValue = new OWLDataValueImpl(new XSDDate(literal.getString()));
      else throw new OWLConversionFactoryException("cannot create an OWLDataValue object for RDFS literal " + literal.getString() +
                                                   " of type '" + datatype);
    } catch (DataValueConversionException e) {
      throw new OWLConversionFactoryException("error creating an OWLDataValue object for RDFS literal value " + literal.getString() + 
                                              " with type " + datatype.getURI() + ": " + e.getMessage());
    } // try

    return dataValue;
  } // convertOWLDataValue

  private ClassAtom convertClassAtom(edu.stanford.smi.protegex.owl.swrl.model.SWRLClassAtom atom) throws OWLConversionFactoryException 
  { 
    String classURI = (atom.getClassPredicate() != null) ? atom.getClassPredicate().getURI() : null;
    ClassAtomImpl classAtom = new ClassAtomImpl(classURI);

    if (classURI == null) throw new OWLConversionFactoryException("empty class name in SWRLClassAtom: " + atom.getBrowserText());

    classAtom.addReferencedClassURI(classURI);
    
    if (atom.getArgument1() instanceof edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable) {
      edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable variable = (edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable)atom.getArgument1();
      VariableArgument argument1 = argumentFactory.createVariableArgument(variable.getLocalName());
      classAtom.setArgument1(argument1);
      classAtom.addReferencedVariableName(variable.getLocalName());
    } else if (atom.getArgument1() instanceof edu.stanford.smi.protegex.owl.model.OWLIndividual) {
      String individualArgumentURI = ((edu.stanford.smi.protegex.owl.model.OWLIndividual)atom.getArgument1()).getURI();
      IndividualArgument argument1 = argumentFactory.createIndividualArgument(individualArgumentURI);
      classAtom.setArgument1(argument1);
      classAtom.addReferencedIndividualURI(argument1.getURI());
    } else if (atom.getArgument1() instanceof edu.stanford.smi.protegex.owl.model.OWLNamedClass) {
      String classArgumentURI = ((edu.stanford.smi.protegex.owl.model.OWLNamedClass)atom.getArgument1()).getURI();
      ClassArgument argument1 = argumentFactory.createClassArgument(classArgumentURI);
      classAtom.setArgument1(argument1);
      classAtom.addReferencedClassURI(classURI);
    } else if (atom.getArgument1() instanceof edu.stanford.smi.protegex.owl.model.OWLObjectProperty) {
      String propertyArgumentURI = ((edu.stanford.smi.protegex.owl.model.OWLObjectProperty)atom.getArgument1()).getURI();
      ObjectPropertyArgument argument1 = argumentFactory.createObjectPropertyArgument(propertyArgumentURI);
      classAtom.setArgument1(argument1);
      classAtom.addReferencedPropertyURI(propertyArgumentURI);
    } else if (atom.getArgument1() instanceof edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty) {
      String propertyArgumentURI = ((edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty)atom.getArgument1()).getURI();
      DataPropertyArgument argument1 = argumentFactory.createDataPropertyArgument(propertyArgumentURI);
      classAtom.setArgument1(argument1);
      classAtom.addReferencedPropertyURI(propertyArgumentURI);
    } else throw new OWLConversionFactoryException("unexpected argument to class atom " + atom.getBrowserText() + "; expecting " +
    		                                           "variable or individual, got instance of " + atom.getArgument1().getClass());

    return classAtom;
  }

  private IndividualPropertyAtom convertIndividualPropertyAtom(edu.stanford.smi.protegex.owl.swrl.model.SWRLIndividualPropertyAtom atom)
    throws OWLConversionFactoryException 
  { 
    String propertyURI = (atom.getPropertyPredicate() != null) ? atom.getPropertyPredicate().getURI() : null;
    IndividualPropertyAtomImpl individualPropertyAtom = new IndividualPropertyAtomImpl(propertyURI);

    if (propertyURI == null) 
    	throw new OWLConversionFactoryException("empty property name in SWRLIndividualPropertyAtom: " + atom.getBrowserText());
    
    individualPropertyAtom.addReferencedPropertyURI(propertyURI);

    if (atom.getArgument1() instanceof edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable) {
      edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable variable = (edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable)atom.getArgument1();
      VariableArgument argument1 = argumentFactory.createVariableArgument(variable.getLocalName());
      individualPropertyAtom.setArgument1(argument1);
      individualPropertyAtom.addReferencedVariableName(variable.getLocalName());
    } else if (atom.getArgument1() instanceof edu.stanford.smi.protegex.owl.model.OWLIndividual) {
      edu.stanford.smi.protegex.owl.model.OWLIndividual individual = (edu.stanford.smi.protegex.owl.model.OWLIndividual)atom.getArgument1();
      IndividualArgument argument1 = argumentFactory.createIndividualArgument(individual.getURI());
      individualPropertyAtom.setArgument1(argument1);
      individualPropertyAtom.addReferencedIndividualURI(argument1.getURI());
    } else throw new OWLConversionFactoryException("unexpected first argument to individual property atom " + atom.getBrowserText() + 
                                                   " - expecting variable or individual, got instance of " + atom.getArgument1().getClass());

    if (atom.getArgument2() instanceof edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable) {
      edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable variable = (edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable)atom.getArgument2();
      VariableArgument argument2 = argumentFactory.createVariableArgument(variable.getLocalName());
      individualPropertyAtom.setArgument2(argument2);
      individualPropertyAtom.addReferencedVariableName(variable.getLocalName());
    } else if (atom.getArgument2() instanceof edu.stanford.smi.protegex.owl.model.OWLIndividual) {
      edu.stanford.smi.protegex.owl.model.OWLIndividual individual = (edu.stanford.smi.protegex.owl.model.OWLIndividual)atom.getArgument2();
      IndividualArgument argument2 = argumentFactory.createIndividualArgument(individual.getURI());
      individualPropertyAtom.setArgument2(argument2);
      individualPropertyAtom.addReferencedIndividualURI(argument2.getURI());
    } else if (atom.getArgument2() instanceof edu.stanford.smi.protegex.owl.model.OWLNamedClass) {
      edu.stanford.smi.protegex.owl.model.OWLNamedClass cls = (edu.stanford.smi.protegex.owl.model.OWLNamedClass)atom.getArgument2();
      ClassArgument argument2 = argumentFactory.createClassArgument(cls.getURI());
      individualPropertyAtom.setArgument2(argument2);
      individualPropertyAtom.addReferencedClassURI(argument2.getURI());
    } else if (atom.getArgument2() instanceof edu.stanford.smi.protegex.owl.model.OWLProperty) {
      edu.stanford.smi.protegex.owl.model.OWLProperty property = (edu.stanford.smi.protegex.owl.model.OWLProperty)atom.getArgument2();
      PropertyArgument argument2;
      String propertyArgumentURI = ((edu.stanford.smi.protegex.owl.model.OWLObjectProperty)property).getURI();
      if (property.isObjectProperty()) argument2 = argumentFactory.createObjectPropertyArgument(propertyArgumentURI);
      else argument2 = argumentFactory.createDataPropertyArgument(propertyArgumentURI);
      individualPropertyAtom.setArgument2(argument2);
      individualPropertyAtom.addReferencedPropertyURI(propertyArgumentURI);
    } else throw new OWLConversionFactoryException("unexpected second argument to individual property atom " + atom.getBrowserText() + 
                                                   " - expecting variable or individual, got instance of " + atom.getArgument2().getClass());

    return individualPropertyAtom; 
  } 

  private DatavaluedPropertyAtom convertDatavaluedPropertyAtom(edu.stanford.smi.protegex.owl.swrl.model.SWRLDatavaluedPropertyAtom atom) 
    throws OWLConversionFactoryException 
  { 
    String propertyURI = (atom.getPropertyPredicate() != null) ? atom.getPropertyPredicate().getURI() : null;
    DatavaluedPropertyAtomImpl datavaluedPropertyAtom = new DatavaluedPropertyAtomImpl(propertyURI);

    if (propertyURI == null) 
    	throw new OWLConversionFactoryException("empty property name in SWRLDatavaluedPropertyAtom: " + atom.getBrowserText());

    datavaluedPropertyAtom.addReferencedPropertyURI(propertyURI);

    if (atom.getArgument1() instanceof edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable) {
      edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable variable = (edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable)atom.getArgument1();
      VariableArgument argument1 = argumentFactory.createVariableArgument(variable.getLocalName());
      datavaluedPropertyAtom.setArgument1(argument1);
      datavaluedPropertyAtom.addReferencedVariableName(variable.getLocalName());
    } else if (atom.getArgument1() instanceof edu.stanford.smi.protegex.owl.model.OWLIndividual) {
      edu.stanford.smi.protegex.owl.model.OWLIndividual individual = (edu.stanford.smi.protegex.owl.model.OWLIndividual)atom.getArgument1();
      IndividualArgument argument1 = argumentFactory.createIndividualArgument(individual.getURI());
      datavaluedPropertyAtom.setArgument1(argument1);
      datavaluedPropertyAtom.addReferencedIndividualURI(argument1.getURI());
    } else throw new OWLConversionFactoryException("unexpected argument first to datavalued property atom '" + atom.getBrowserText() + 
                                         "' - expecting variable or individual, got instance of " + atom.getArgument1().getClass());

    if (atom.getArgument2() instanceof edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable) {
      edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable variable = (edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable)atom.getArgument2();
      VariableArgument argument2 = argumentFactory.createVariableArgument(variable.getLocalName());
      datavaluedPropertyAtom.setArgument2(argument2);
      datavaluedPropertyAtom.addReferencedVariableName(variable.getLocalName());
    } else if (atom.getArgument2() instanceof RDFSLiteral) {
      DataValueArgument argument2 = convertRDFSLiteral2DataValueArgument(owlModel, (RDFSLiteral)atom.getArgument2());
      datavaluedPropertyAtom.setArgument2(argument2);
    } else throw new OWLConversionFactoryException("unexpected second to datavalued property atom " + atom.getBrowserText()  + 
                                                   " - expecting variable or literal, got instance of " + atom.getArgument2().getClass());
    
    return datavaluedPropertyAtom; 
  } // convertDatavaluedPropertyAtom

  private SameIndividualAtom convertSameIndividualAtom(edu.stanford.smi.protegex.owl.swrl.model.SWRLSameIndividualAtom atom) throws OWLConversionFactoryException 
  { 
    SameIndividualAtomImpl sameIndividualAtom = new SameIndividualAtomImpl();

    if (atom.getArgument1() instanceof edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable) {
      edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable variable = (edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable)atom.getArgument1();
      VariableArgument argument1 = argumentFactory.createVariableArgument(variable.getLocalName());
      sameIndividualAtom.setArgument1(argument1);
      sameIndividualAtom.addReferencedVariableName(variable.getLocalName());
    } else if (atom.getArgument1() instanceof edu.stanford.smi.protegex.owl.model.OWLIndividual) {
      edu.stanford.smi.protegex.owl.model.OWLIndividual individual = (edu.stanford.smi.protegex.owl.model.OWLIndividual)atom.getArgument1();
      IndividualArgument argument1 = argumentFactory.createIndividualArgument(individual.getURI());
      sameIndividualAtom.setArgument1(argument1);
      sameIndividualAtom.addReferencedIndividualURI(individual.getURI());
    } else throw new OWLConversionFactoryException("unexpected first argument to atom '" + atom.getBrowserText() + 
                                         "' - expecting variable or individual, got instance of " + atom.getArgument1().getClass() + ".");

    if (atom.getArgument2() instanceof edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable) {
      edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable variable = (edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable)atom.getArgument2();
      VariableArgument argument2 = argumentFactory.createVariableArgument(variable.getLocalName());
      sameIndividualAtom.setArgument2(argument2);
      sameIndividualAtom.addReferencedVariableName(variable.getLocalName());
    } else if (atom.getArgument2() instanceof edu.stanford.smi.protegex.owl.model.OWLIndividual) {
      edu.stanford.smi.protegex.owl.model.OWLIndividual individual = (edu.stanford.smi.protegex.owl.model.OWLIndividual)atom.getArgument2();
      IndividualArgument argument2 = argumentFactory.createIndividualArgument(individual.getURI());
      sameIndividualAtom.setArgument2(argument2);
      sameIndividualAtom.addReferencedIndividualURI(individual.getURI());
    } else throw new OWLConversionFactoryException("unexpected second argument to atom " + atom.getBrowserText() + 
                                                   " - expecting variable or individual, got instance of " + atom.getArgument2().getClass());


    return sameIndividualAtom; 
  } // convertSameIndividualAtom

  private DifferentIndividualsAtom convertDifferentIndividualsAtom(edu.stanford.smi.protegex.owl.swrl.model.SWRLDifferentIndividualsAtom atom) 
    throws OWLConversionFactoryException 
  { 
    DifferentIndividualsAtomImpl differentIndividualsAtom = new DifferentIndividualsAtomImpl();

    if (atom.getArgument1() instanceof edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable) {
      edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable variable = (edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable)atom.getArgument1();
      VariableArgument argument1 = argumentFactory.createVariableArgument(variable.getLocalName());
      differentIndividualsAtom.setArgument1(argument1);
      differentIndividualsAtom.addReferencedVariableName(variable.getLocalName());
    } else if (atom.getArgument1() instanceof edu.stanford.smi.protegex.owl.model.OWLIndividual) {
      edu.stanford.smi.protegex.owl.model.OWLIndividual individual = (edu.stanford.smi.protegex.owl.model.OWLIndividual)atom.getArgument1();
      IndividualArgument argument1 = argumentFactory.createIndividualArgument(individual.getURI());
      differentIndividualsAtom.setArgument1(argument1);
      differentIndividualsAtom.addReferencedIndividualURI(individual.getURI());
    } else throw new OWLConversionFactoryException("unexpected first argument to atom " + atom.getBrowserText() + 
                                                   " - expecting variable or individual, got instance of " + atom.getArgument1().getClass());

    if (atom.getArgument2() instanceof edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable) {
      edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable variable = (edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable)atom.getArgument2();
      VariableArgument argument2 = argumentFactory.createVariableArgument(variable.getLocalName());
      differentIndividualsAtom.setArgument2(argument2);
      differentIndividualsAtom.addReferencedVariableName(variable.getLocalName());
    } else if (atom.getArgument2() instanceof edu.stanford.smi.protegex.owl.model.OWLIndividual) {
      edu.stanford.smi.protegex.owl.model.OWLIndividual individual = (edu.stanford.smi.protegex.owl.model.OWLIndividual)atom.getArgument2();
      IndividualArgument argument2 = argumentFactory.createIndividualArgument(individual.getURI());
      differentIndividualsAtom.setArgument2(argument2);
      differentIndividualsAtom.addReferencedIndividualURI(individual.getURI());
    } else throw new OWLConversionFactoryException("unexpected second argument to atom " + atom.getBrowserText() + 
                                                   " - expecting variable or individual, got instance of " + atom.getArgument2().getClass());


    return differentIndividualsAtom; 
  } // convertDifferentIndividualsAtom

  private BuiltInAtom convertBuiltInAtom(edu.stanford.smi.protegex.owl.swrl.model.SWRLBuiltinAtom atom) throws OWLConversionFactoryException  
  { 
    String builtInName = (atom.getBuiltin() != null) ? atom.getBuiltin().getURI() : null;
    String builtInPrefixedName = (atom.getBuiltin() != null) ? atom.getBuiltin().getPrefixedName() : null;
    BuiltInAtomImpl builtInAtom = new BuiltInAtomImpl(builtInName, builtInPrefixedName); 
    List<BuiltInArgument> arguments = new ArrayList<BuiltInArgument>();
    RDFList rdfList = atom.getArguments();

    if (builtInName == null) throw new OWLConversionFactoryException("empty built-in name in SWRLBuiltinAtom: " + atom.getBrowserText());

    Iterator iterator = rdfList.getValues().iterator();
    while (iterator.hasNext()) {
      Object o = iterator.next();
      if (o instanceof edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable) {
        edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable variable = (edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable)o;
        arguments.add(argumentFactory.createVariableArgument(variable.getLocalName()));
        builtInAtom.addReferencedVariableName(variable.getLocalName());
      } else if (o instanceof edu.stanford.smi.protegex.owl.model.OWLIndividual) {
        edu.stanford.smi.protegex.owl.model.OWLIndividual individual = (edu.stanford.smi.protegex.owl.model.OWLIndividual)o;
        arguments.add(argumentFactory.createIndividualArgument(individual.getURI()));
        builtInAtom.addReferencedIndividualURI(individual.getURI());
      } else  if (o instanceof edu.stanford.smi.protegex.owl.model.OWLNamedClass) {
        edu.stanford.smi.protegex.owl.model.OWLNamedClass cls = (edu.stanford.smi.protegex.owl.model.OWLNamedClass)o;
        arguments.add(argumentFactory.createClassArgument(cls.getURI()));
        builtInAtom.addReferencedClassURI(cls.getURI());
      } else  if (o instanceof edu.stanford.smi.protegex.owl.model.OWLProperty) {
        edu.stanford.smi.protegex.owl.model.OWLProperty property = (edu.stanford.smi.protegex.owl.model.OWLProperty)o;
        String propertyArgumentURI = ((edu.stanford.smi.protegex.owl.model.OWLObjectProperty)property).getURI();
        if (property.isObjectProperty()) arguments.add(argumentFactory.createObjectPropertyArgument(propertyArgumentURI));
        else  arguments.add(argumentFactory.createDataPropertyArgument(propertyArgumentURI));
        builtInAtom.addReferencedPropertyURI(propertyArgumentURI);
      } else if (o instanceof RDFSLiteral) arguments.add(convertRDFSLiteral2DataValueArgument(owlModel, (RDFSLiteral)o));
      else {
      	try {
      	  arguments.add(argumentFactory.createDataValueArgument(o));
      	} catch (DataValueConversionException e) {
          throw new OWLConversionFactoryException("error converting argument to built-in " + builtInPrefixedName + 
          		                                    " with value " + o + " of unknown type " + o.getClass() + ": " + e.getMessage());
      	} // try
      } // if
    } // while

    builtInAtom.setBuiltInArguments(arguments);

    return builtInAtom; 
  } // convertBuiltInAtom

  private void write2OWLModel(OWLClassAssertionAxiom axiom) throws OWLConversionFactoryException
  {
    String classURI = axiom.getDescription().getURI();
    String individualURI = axiom.getIndividual().getURI();
    SWRLOWLUtil.addType(owlModel, individualURI, classURI);
  } // write2OWLModel

  private void write2OWLModel(OWLClassPropertyAssertionAxiom axiom) throws OWLConversionFactoryException
  {
    String propertyURI = axiom.getProperty().getURI();
    String subjectIndividualURI = axiom.getSubject().getURI();
    String objectClassURI = axiom.getObject().getURI();
    edu.stanford.smi.protegex.owl.model.RDFProperty property = SWRLOWLUtil.getOWLProperty(owlModel, propertyURI);
    edu.stanford.smi.protegex.owl.model.OWLIndividual subjectIndividual;
    edu.stanford.smi.protegex.owl.model.OWLNamedClass objectClass = null;
    
    if (property == null) throw new OWLConversionFactoryException("invalid property name: " + propertyURI);
    
    subjectIndividual = SWRLOWLUtil.getOWLIndividual(owlModel, subjectIndividualURI);
    if (subjectIndividual == null) throw new OWLConversionFactoryException("invalid individual URI " + subjectIndividualURI);

    objectClass = SWRLOWLUtil.getOWLNamedClass(owlModel, objectClassURI); 

    if (!subjectIndividual.hasPropertyValue(property, objectClass, false)) subjectIndividual.addPropertyValue(property, objectClass);
  } // write2OWLModel
 
  private void write2OWLModel(OWLDataPropertyAssertionAxiom axiom) throws OWLConversionFactoryException
  {
    edu.stanford.smi.protegex.owl.model.OWLIndividual subjectIndividual;
    String propertyURI = axiom.getProperty().getURI();
    String subjectIndividualURI = axiom.getSubject().getURI();
    edu.stanford.smi.protegex.owl.model.RDFProperty property = SWRLOWLUtil.getOWLProperty(owlModel, propertyURI);
    edu.stanford.smi.protegex.owl.model.RDFSDatatype rangeDatatype = property.getRangeDatatype();
    Object objectValue;

    if (property == null) throw new OWLConversionFactoryException("invalid property URI " + propertyURI);
    
    subjectIndividual = SWRLOWLUtil.getOWLIndividual(owlModel, subjectIndividualURI);
    if (subjectIndividual == null) throw new OWLConversionFactoryException("invalid individual URI " + subjectIndividualURI);

    if (rangeDatatype == null) {
    	OWLDataValue dataValue = owlDataValueFactory.getOWLDataValue(axiom.getObject());
      if (dataValue.isString()) objectValue = dataValue.toString();
      else objectValue = axiom.getObject().toString();
    } else objectValue = owlModel.createRDFSLiteral(axiom.getObject().toString(), rangeDatatype);   

    if (!subjectIndividual.hasPropertyValue(property, objectValue, false)) subjectIndividual.addPropertyValue(property, objectValue);    
  } // write2OWLModel

  private void write2OWLModel(OWLDifferentIndividualsAxiom axiom, edu.stanford.smi.protegex.owl.model.OWLModel owlModel) 
    throws OWLConversionFactoryException
  {
    // TODO:
  } // write2OWLModel

  private void write2OWLModel(OWLObjectPropertyAssertionAxiom axiom) throws OWLConversionFactoryException
  {
    edu.stanford.smi.protegex.owl.model.OWLIndividual subjectIndividual, objectIndividual;
    String propertyURI = axiom.getProperty().getURI();
    String subjectIndividualURI = axiom.getSubject().getURI();
    String objectIndividualURI = axiom.getObject().getURI();
    edu.stanford.smi.protegex.owl.model.RDFProperty property = SWRLOWLUtil.getOWLProperty(owlModel, propertyURI);
    
    if (property == null) throw new OWLConversionFactoryException("invalid property URI" + propertyURI);
    
    subjectIndividual = SWRLOWLUtil.getOWLIndividual(owlModel, subjectIndividualURI);
    if (subjectIndividual == null) throw new OWLConversionFactoryException("invalid subject individual URI " + subjectIndividualURI);
 
    objectIndividual = SWRLOWLUtil.getOWLIndividual(owlModel, objectIndividualURI); 
    if (objectIndividual == null) throw new OWLConversionFactoryException("invalid object individual URI " + objectIndividualURI);

    if (!subjectIndividual.hasPropertyValue(property, objectIndividual, false)) subjectIndividual.addPropertyValue(property, objectIndividual);
  } // write2OWLModel

  private void write2OWLModel(OWLPropertyPropertyAssertionAxiom axiom) throws OWLConversionFactoryException
  {
    String propertyURI = axiom.getProperty().getURI();
    String subjectIndividualURI = axiom.getSubject().getURI();
    String objectPropertyURI = axiom.getObject().getURI();
    edu.stanford.smi.protegex.owl.model.RDFProperty property = SWRLOWLUtil.getOWLProperty(owlModel, propertyURI);
    edu.stanford.smi.protegex.owl.model.OWLIndividual subjectIndividual;
    edu.stanford.smi.protegex.owl.model.OWLProperty objectProperty;
    
    if (property == null) throw new OWLConversionFactoryException("invalid property URI " + propertyURI);
    
    subjectIndividual = SWRLOWLUtil.getOWLIndividual(owlModel, subjectIndividualURI);
    if (subjectIndividual == null) throw new OWLConversionFactoryException("invalid subject individual URI" + subjectIndividualURI);

    objectProperty = SWRLOWLUtil.getOWLProperty(owlModel, objectPropertyURI); 
    if (objectProperty == null) throw new OWLConversionFactoryException("invalid object individual URI" + objectPropertyURI);

    if (!subjectIndividual.hasPropertyValue(property, objectProperty, false)) subjectIndividual.addPropertyValue(property, objectProperty);
  } // write2OWLModel

  private void write2OWLModel(OWLSomeValuesFrom axiom) throws OWLConversionFactoryException
  {
    edu.stanford.smi.protegex.owl.model.OWLSomeValuesFrom someValuesFrom = SWRLOWLUtil.getOWLSomeValuesFrom(owlModel, axiom.asOWLClass().getURI());
    edu.stanford.smi.protegex.owl.model.OWLProperty property = SWRLOWLUtil.getOWLProperty(owlModel, axiom.getProperty().getURI());
    edu.stanford.smi.protegex.owl.model.RDFResource filler = SWRLOWLUtil.getClass(owlModel, axiom.getSomeValuesFrom().getURI());
    
    someValuesFrom.setOnProperty(property);
    someValuesFrom.setFiller(filler); 
  } // write2OWLModel

  private void write2OWLModel(OWLSubClassAxiom axiom) throws OWLConversionFactoryException
  {
    String subClassURI = axiom.getSubClass().getURI();
    String superClassURI = axiom.getSuperClass().getURI();
    SWRLOWLUtil.addOWLSuperClass(owlModel, subClassURI, superClassURI);
  } // write2OWLModel
  
  private DataRangeAtom convertDataRangeAtom(edu.stanford.smi.protegex.owl.swrl.model.SWRLDataRangeAtom atom) 
    throws OWLConversionFactoryException 
  { 
    throw new OWLConversionFactoryException("SWRL data range atoms not implemented.");
  } // convertDataRangeAtom

  private Atom convertSWRLAtom(edu.stanford.smi.protegex.owl.swrl.model.SWRLAtom swrlAtom) throws OWLConversionFactoryException
  {
    Atom atom;
    
    if (swrlAtom instanceof edu.stanford.smi.protegex.owl.swrl.model.SWRLClassAtom) {
      atom = convertClassAtom((edu.stanford.smi.protegex.owl.swrl.model.SWRLClassAtom)swrlAtom);
    } else if (swrlAtom instanceof edu.stanford.smi.protegex.owl.swrl.model.SWRLDatavaluedPropertyAtom) {
      atom = convertDatavaluedPropertyAtom((edu.stanford.smi.protegex.owl.swrl.model.SWRLDatavaluedPropertyAtom)swrlAtom);
    } else if (swrlAtom instanceof edu.stanford.smi.protegex.owl.swrl.model.SWRLIndividualPropertyAtom) {
      atom = convertIndividualPropertyAtom((edu.stanford.smi.protegex.owl.swrl.model.SWRLIndividualPropertyAtom)swrlAtom);
    } else if (swrlAtom instanceof edu.stanford.smi.protegex.owl.swrl.model.SWRLSameIndividualAtom) {
      atom = convertSameIndividualAtom((edu.stanford.smi.protegex.owl.swrl.model.SWRLSameIndividualAtom)swrlAtom);
    } else if (swrlAtom instanceof edu.stanford.smi.protegex.owl.swrl.model.SWRLDifferentIndividualsAtom) {
      atom = convertDifferentIndividualsAtom((edu.stanford.smi.protegex.owl.swrl.model.SWRLDifferentIndividualsAtom)swrlAtom);
    } else if (swrlAtom instanceof edu.stanford.smi.protegex.owl.swrl.model.SWRLBuiltinAtom) {
      atom = convertBuiltInAtom((edu.stanford.smi.protegex.owl.swrl.model.SWRLBuiltinAtom)swrlAtom);
    } else if (swrlAtom instanceof edu.stanford.smi.protegex.owl.swrl.model.SWRLDataRangeAtom) 
      atom = convertDataRangeAtom((edu.stanford.smi.protegex.owl.swrl.model.SWRLDataRangeAtom)swrlAtom);
    else throw new OWLConversionFactoryException("invalid SWRL atom: " + swrlAtom.getBrowserText());

    return atom;
  } 
  
  // Utility method to create a collection of OWL property assertion axioms for every subject/predicate combination for a particular OWL
  // property.  TODO: This is incredibly inefficient.

  public Set<OWLPropertyAssertionAxiom> getOWLPropertyAssertionAxioms(String propertyURI) 
    throws OWLConversionFactoryException, DataValueConversionException
  {
    return getOWLPropertyAssertionAxioms(null, propertyURI);
  } // getOWLPropertyAssertionAxioms

  public Set<OWLPropertyAssertionAxiom> getOWLPropertyAssertionAxioms(String subjectURI, String propertyURI) 
    throws OWLConversionFactoryException, DataValueConversionException
  {
    Set<OWLPropertyAssertionAxiom> propertyAssertions = new HashSet<OWLPropertyAssertionAxiom>();
    edu.stanford.smi.protegex.owl.model.OWLProperty property = SWRLOWLUtil.getOWLProperty(owlModel, propertyURI);
    edu.stanford.smi.protegex.owl.model.OWLIndividual subject = (subjectURI == null) ? null : SWRLOWLUtil.getOWLIndividual(owlModel, subjectURI);
    OWLPropertyAssertionAxiom axiom;

    if (property == null) throw new InvalidPropertyNameException(propertyURI);

    TripleStoreModel tsm = owlModel.getTripleStoreModel();
    Iterator<RDFResource> it = tsm.listSubjects(property);
    while (it.hasNext()) {
      RDFResource localSubject = it.next();
      if (!(localSubject instanceof edu.stanford.smi.protegex.owl.model.OWLIndividual)) continue; // Deal only with OWL individuals (could return metaclass, for example)
      if (subject != null && !localSubject.getURI().equals(subject.getURI())) continue; // If subject supplied, ensure it is same
      edu.stanford.smi.protegex.owl.model.OWLIndividual subjectIndividual = (edu.stanford.smi.protegex.owl.model.OWLIndividual)localSubject;
      
      for (Object object : localSubject.getPropertyValues(property)) {
        
        if (property.hasObjectRange()) { // Object property
          OWLObjectProperty objectProperty = owlFactory.getOWLObjectProperty(propertyURI);

          if (object instanceof edu.stanford.smi.protegex.owl.model.OWLIndividual) {
            edu.stanford.smi.protegex.owl.model.OWLIndividual objectIndividual = (edu.stanford.smi.protegex.owl.model.OWLIndividual)object;
            OWLNamedIndividual subjectOWLIndividual = owlFactory.getOWLIndividual(subjectIndividual.getURI());
            OWLNamedIndividual objectOWLIndividual = owlFactory.getOWLIndividual(objectIndividual.getURI());
            axiom = owlFactory.getOWLObjectPropertyAssertionAxiom(subjectOWLIndividual, objectProperty, objectOWLIndividual);
            propertyAssertions.add(axiom);
          } else if (object instanceof edu.stanford.smi.protegex.owl.model.OWLNamedClass) { // This will be OWL Full
            edu.stanford.smi.protegex.owl.model.OWLNamedClass objectClass = (edu.stanford.smi.protegex.owl.model.OWLNamedClass)object;
            OWLNamedIndividual subjectOWLIndividual = owlFactory.getOWLIndividual(subjectIndividual.getURI());
            OWLClass objectPropertyClassValue = owlFactory.getOWLClass(objectClass.getURI());
            axiom = owlFactory.getOWLClassPropertyAssertionAxiom(subjectOWLIndividual, objectProperty, objectPropertyClassValue);
            propertyAssertions.add(axiom);
          } else if (object instanceof edu.stanford.smi.protegex.owl.model.OWLProperty) { // This will be OWL Full
            edu.stanford.smi.protegex.owl.model.OWLProperty objectPropertyValue = (edu.stanford.smi.protegex.owl.model.OWLProperty)object;
            OWLNamedIndividual subjectOWLIndividual = owlFactory.getOWLIndividual(subjectIndividual.getURI());
            OWLProperty objectPropertyPropertyValue;
            if (objectPropertyValue.isObjectProperty()) objectPropertyPropertyValue = owlFactory.getOWLObjectProperty(objectPropertyValue.getURI());
            else objectPropertyPropertyValue = owlFactory.getOWLDataProperty(objectPropertyValue.getURI());
            axiom = owlFactory.getOWLPropertyPropertyAssertionAxiom(subjectOWLIndividual, objectProperty, objectPropertyPropertyValue);
            propertyAssertions.add(axiom);                
          } // if
        } else { // DataProperty
          OWLNamedIndividual subjectOWLIndividual = owlFactory.getOWLIndividual(subjectIndividual.getURI());
          RDFSLiteral rdfsLiteral = owlModel.asRDFSLiteral(object);
          OWLLiteral literal = convertRDFSLiteral2OWLLiteral(owlModel, rdfsLiteral);
          OWLDataProperty dataProperty = owlFactory.getOWLDataProperty(propertyURI);
          axiom = owlFactory.getOWLDataPropertyAssertionAxiom(subjectOWLIndividual, dataProperty, literal);
          propertyAssertions.add(axiom);
        } // if
      } // for
    } // while
      
    return propertyAssertions;
  } // getOWLPropertyAssertionAxioms

  // TODO: This is incredibly inefficient. Need to use method in the OWLModel to get individuals with a particular property.
  public Set<OWLSameIndividualAxiom> getSameIndividualAxioms() throws OWLConversionFactoryException
  {
    RDFProperty sameAsProperty = SWRLOWLUtil.getOWLSameAsProperty(owlModel);
    RDFSClass owlThingCls = SWRLOWLUtil.getOWLThingClass(owlModel);
    Set<OWLSameIndividualAxiom> result = new HashSet<OWLSameIndividualAxiom>();

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
          result.add(owlFactory.getOWLSameIndividualAxiom(owlFactory.getOWLIndividual(individual1.getURI()), 
        		  		                                        owlFactory.getOWLIndividual(individual2.getURI())));
        } // while
      } // if
    } // while
    return result;
  } // getOWLSameIndividualAxioms
  
  public Set<OWLDifferentIndividualsAxiom> getOWLDifferentIndividualsAxioms() throws OWLConversionFactoryException
  {
	  RDFProperty differentFromProperty = SWRLOWLUtil.getOWLDifferentFromProperty(owlModel);
	  RDFSClass owlThingCls = SWRLOWLUtil.getOWLThingClass(owlModel);
	  Set<OWLDifferentIndividualsAxiom> result = new HashSet<OWLDifferentIndividualsAxiom>();
	  Collection allDifferents = SWRLOWLUtil.getOWLAllDifferents(owlModel);

	  Iterator individualsIterator1 = owlThingCls.getInstances(true).iterator();
	  while (individualsIterator1.hasNext()) {
		  Object object1 = individualsIterator1.next();
		  if (!(object1 instanceof OWLNamedIndividual)) continue; // Deal only with OWL individuals (could return metaclass, for example)
		  edu.stanford.smi.protegex.owl.model.OWLIndividual individual1 = (edu.stanford.smi.protegex.owl.model.OWLIndividual)object1;
		  if (individual1.hasPropertyValue(differentFromProperty)) {
			  Collection individuals = (Collection)individual1.getPropertyValues(differentFromProperty);
			  Iterator individualsIterator2 = individuals.iterator();
			  while (individualsIterator2.hasNext()) {
				  Object object2 = individualsIterator2.next();
				  if (!(object2 instanceof edu.stanford.smi.protegex.owl.model.OWLIndividual)) continue;
				  edu.stanford.smi.protegex.owl.model.OWLIndividual individual2 = (edu.stanford.smi.protegex.owl.model.OWLIndividual)object2;
				  result.add(owlFactory.getOWLDifferentIndividualsAxiom(owlFactory.getOWLIndividual(individual1.getURI()), 
	                                                              owlFactory.getOWLIndividual(individual2.getURI())));
			  } // while
		  } // if
	  } // while
	  
	  if (!allDifferents.isEmpty()) {
		  Iterator allDifferentsIterator = allDifferents.iterator();
		  while (allDifferentsIterator.hasNext()) {
			  OWLAllDifferent owlAllDifferent = (OWLAllDifferent)allDifferentsIterator.next();
			  
			  if (owlAllDifferent.getDistinctMembers().size() != 0) {
				  OWLDifferentIndividualsAxiom axiom;
				  Set<OWLNamedIndividual> individuals = new HashSet<OWLNamedIndividual>();
	          
				  Iterator individualsIterator = owlAllDifferent.getDistinctMembers().iterator();
				  while (individualsIterator.hasNext()) {
					  RDFIndividual individual = (RDFIndividual)individualsIterator.next();
					  if (individual instanceof edu.stanford.smi.protegex.owl.model.OWLIndividual) { // Ignore non OWL individuals
						  String individualURI = ((edu.stanford.smi.protegex.owl.model.OWLIndividual)individual).getURI();
						  OWLNamedIndividual owlIndividual = owlFactory.getOWLIndividual(individualURI);
						  individuals.add(owlIndividual);
					  } // if
				  } // while
				  axiom = owlFactory.getOWLDifferentIndividualsAxiom(individuals);
				  result.add(axiom);
			  } // if
		  } // while
	  } // if
	  
	  return result;
  } // getOWLDifferentIndividualsAxioms
  
  private void initializeProperty(OWLPropertyImpl owlPropertyImpl, edu.stanford.smi.protegex.owl.model.OWLProperty property)
    throws OWLConversionFactoryException
  {
  	 for (String domainClassURI : SWRLOWLUtil.rdfResources2OWLNamedClassURIs(property.getUnionDomain()))
  	  owlPropertyImpl.addDomainClass(getOWLClass(domainClassURI));
  	 for (String rangeClassURI : SWRLOWLUtil.rdfResources2OWLNamedClassURIs(property.getUnionRangeClasses()))
  	   owlPropertyImpl.addRangeClass(getOWLClass(rangeClassURI));
  	 for (String superPropertyURI : SWRLOWLUtil.rdfResources2OWLPropertyURIs(property.getSuperproperties(false)))
  	   if (property.isObjectProperty()) owlPropertyImpl.addSuperProperty(getOWLObjectProperty(superPropertyURI));
  	   else owlPropertyImpl.addSuperProperty(getOWLDataProperty(superPropertyURI));
  	 for (String subPropertyURI : SWRLOWLUtil.rdfResources2OWLPropertyURIs(property.getSubproperties(false)))
  	   if (property.isObjectProperty()) owlPropertyImpl.addSubProperty(getOWLObjectProperty(subPropertyURI));
  	   else owlPropertyImpl.addSubProperty(getOWLDataProperty(subPropertyURI));
  	 for (String equivalentPropertyURI : SWRLOWLUtil.rdfResources2OWLPropertyURIs(property.getEquivalentProperties()))
  	   if (property.isObjectProperty()) owlPropertyImpl.addEquivalentProperty(getOWLObjectProperty(equivalentPropertyURI));
  	   else owlPropertyImpl.addEquivalentProperty(getOWLDataProperty(equivalentPropertyURI));
  }

  private void buildDefiningClasses(OWLIndividualImpl owlIndividualImpl, edu.stanford.smi.protegex.owl.model.OWLIndividual individual) 
    throws OWLConversionFactoryException
  { 
    for (Object o : individual.getRDFTypes()) {
      RDFSClass cls = (RDFSClass)o; 
      if (!cls.isAnonymous() && cls instanceof edu.stanford.smi.protegex.owl.model.OWLNamedClass) {
        String classURI = ((edu.stanford.smi.protegex.owl.model.OWLNamedClass)cls).getURI();
        owlIndividualImpl.addType(getOWLClass(classURI));
      } // if
    } // for
  }

  private void buildSameAsIndividuals(OWLIndividualImpl owlIndividualImpl, edu.stanford.smi.protegex.owl.model.OWLIndividual individual) 
    throws OWLConversionFactoryException
 {
    edu.stanford.smi.protegex.owl.model.RDFProperty sameAsProperty = SWRLOWLUtil.getOWLSameAsProperty(owlModel);

    if (individual.hasPropertyValue(sameAsProperty)) {
      Collection individuals = (Collection)individual.getPropertyValues(sameAsProperty);
      Iterator individualsIterator = individuals.iterator();
      while (individualsIterator.hasNext()) {
        Object object = individualsIterator.next();
        if (!(object instanceof edu.stanford.smi.protegex.owl.model.OWLIndividual)) continue;
        edu.stanford.smi.protegex.owl.model.OWLIndividual sameAsIndividual = (edu.stanford.smi.protegex.owl.model.OWLIndividual)object;
        owlIndividualImpl.addSameAsIndividual(owlFactory.getOWLIndividual(sameAsIndividual.getURI()));
      } // while
    } // if
  } 

  private void buildDifferentFromIndividuals(OWLIndividualImpl owlIndividualImpl, edu.stanford.smi.protegex.owl.model.OWLIndividual individual) 
    throws OWLConversionFactoryException
  {
    edu.stanford.smi.protegex.owl.model.RDFProperty differentFromProperty = SWRLOWLUtil.getOWLDifferentFromProperty(owlModel);

    if (individual.hasPropertyValue(differentFromProperty)) {
      Collection individuals = (Collection)individual.getPropertyValues(differentFromProperty);
      Iterator individualsIterator = individuals.iterator();
      while (individualsIterator.hasNext()) {
        Object object = individualsIterator.next();
        if (!(object instanceof edu.stanford.smi.protegex.owl.model.OWLIndividual)) continue;
        edu.stanford.smi.protegex.owl.model.OWLIndividual differentFromIndividual = (edu.stanford.smi.protegex.owl.model.OWLIndividual)object;
        owlIndividualImpl.addDifferentFromIndividual(owlFactory.getOWLIndividual(differentFromIndividual.getURI()));
      } // while
    } // if
  }
  
  public OWLModel getOWLModel() { return owlModel; } // TODO: Protege-OWL dependency

}
