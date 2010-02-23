
//TODO:   some owlFactory.getOWLIndividual(individual.getName()) should be convertOWLIndividual(owlIndividual) etc.

package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
import edu.stanford.smi.protegex.owl.swrl.bridge.Atom;
import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInAtom;
import edu.stanford.smi.protegex.owl.swrl.bridge.ClassAtom;
import edu.stanford.smi.protegex.owl.swrl.bridge.DataRangeAtom;
import edu.stanford.smi.protegex.owl.swrl.bridge.DatavaluedPropertyAtom;
import edu.stanford.smi.protegex.owl.swrl.bridge.DifferentIndividualsAtom;
import edu.stanford.smi.protegex.owl.swrl.bridge.IndividualPropertyAtom;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLAxiom;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLClass;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLClassAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLClassPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLConversionFactory;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLDataFactory;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLDataProperty;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLDataPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLDataValue;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLDifferentIndividualsAxiom;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLIndividual;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLObjectPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLProperty;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLPropertyPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLSameIndividualAxiom;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLSomeValuesFrom;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLSubClassAxiom;
import edu.stanford.smi.protegex.owl.swrl.bridge.SWRLRule;
import edu.stanford.smi.protegex.owl.swrl.bridge.SameIndividualAtom;
import edu.stanford.smi.protegex.owl.swrl.bridge.VariableAtomArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.BuiltInException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.DataValueConversionException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.InvalidPropertyNameException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.OWLConversionFactoryException;
import edu.stanford.smi.protegex.owl.swrl.bridge.xsd.XSDAnyURI;
import edu.stanford.smi.protegex.owl.swrl.bridge.xsd.XSDDate;
import edu.stanford.smi.protegex.owl.swrl.bridge.xsd.XSDDateTime;
import edu.stanford.smi.protegex.owl.swrl.bridge.xsd.XSDDuration;
import edu.stanford.smi.protegex.owl.swrl.bridge.xsd.XSDTime;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLAtom;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLBuiltinAtom;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLClassAtom;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLDataRangeAtom;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLDatavaluedPropertyAtom;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLDifferentIndividualsAtom;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLFactory;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLImp;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLIndividualPropertyAtom;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLSameIndividualAtom;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.SQWRLException;
import edu.stanford.smi.protegex.owl.swrl.util.SWRLOWLUtil;

/**
 * Class to insert and get OWLAPI-like entities into and from a Protege-OWL model.
 */
public class OWLConversionFactoryImpl implements OWLConversionFactory
{
  private OWLModel owlModel;
  private SWRLFactory swrlFactory;
  private OWLDataFactory owlFactory;

  public OWLConversionFactoryImpl(OWLModel owlModel, OWLDataFactory owlFactory) 
  { 
    this.owlModel = owlModel; 
    swrlFactory = new SWRLFactory(owlModel);
    this.owlFactory = owlFactory;
  } // OWLConversionFactoryImpl
  
  public boolean isOWLClass(String className) { return SWRLOWLUtil.isOWLClass(owlModel, className); }
  public boolean isOWLProperty(String propertyName) { return SWRLOWLUtil.isProperty(owlModel, propertyName); }
  public boolean isOWLObjectProperty(String propertyName) { return SWRLOWLUtil.isObjectProperty(owlModel, propertyName); }
  public boolean isOWLDataProperty(String propertyName) { return SWRLOWLUtil.isDatatypeProperty(owlModel, propertyName); }
  public boolean isOWLIndividual(String individualName) { return SWRLOWLUtil.isOWLIndividual(owlModel, individualName); }
  public boolean isOWLIndividualOfClass(String individualName, String className) { return SWRLOWLUtil.isIndividualOfClass(owlModel, individualName, className); }
  public boolean isSWRLBuiltIn(String builtInName) { return SWRLOWLUtil.isSWRLBuiltIn(owlModel, builtInName); }
  public String createNewResourceName(String prefix) { return SWRLOWLUtil.createNewResourceName(owlModel, prefix); }
  
  public Set<SWRLRule> getSWRLRules() throws OWLConversionFactoryException, SQWRLException, BuiltInException
  {
    Collection<SWRLImp> imps = swrlFactory.getImps();
    Set<SWRLRule> result = new HashSet<SWRLRule>();
    
    for (SWRLImp imp : imps) {
      if (imp.isEnabled()) {
        SWRLRule rule = getSWRLRule(imp.getName());
        result.add(rule);
      } // if
    } // while

    return result;
  } // getSWRLRules

  public SWRLRule getSWRLRule(String ruleURI) throws OWLConversionFactoryException, SQWRLException, BuiltInException
  {
    List<Atom> bodyAtoms = new ArrayList<Atom>();
    List<Atom> headAtoms = new ArrayList<Atom>();
    SWRLImp imp = swrlFactory.getImp(ruleURI);

    if (imp == null) throw new OWLConversionFactoryException("invalid rule name: '" + ruleURI + "'");

    Iterator iterator = imp.getBody().getValues().iterator();
    while (iterator.hasNext()) {
      SWRLAtom swrlAtom = (SWRLAtom)iterator.next();
      bodyAtoms.add(convertSWRLAtom(swrlAtom));
    } // while 

    iterator = imp.getHead().getValues().iterator();
    while (iterator.hasNext()) {
      SWRLAtom swrlAtom = (SWRLAtom)iterator.next();
      headAtoms.add(convertSWRLAtom(swrlAtom));
    } // while 

    return new SWRLRuleImpl(imp.getPrefixedName(), bodyAtoms, headAtoms);
  } // getSWRLRule

  public OWLClass getOWLClass() 
  { 
    String anonymousName = SWRLOWLUtil.getNextAnonymousResourceName(owlModel);

    return new OWLClassImpl(anonymousName, anonymousName);
  } // getOWLClass

  public OWLClass getOWLClass(String classURI) throws OWLConversionFactoryException
  { 
    edu.stanford.smi.protegex.owl.model.OWLNamedClass owlNamedClass = SWRLOWLUtil.createOWLNamedClass(owlModel, classURI);
    String prefixedClassName = owlNamedClass.getPrefixedName();
    OWLClassImpl owlClass = new OWLClassImpl(classURI, prefixedClassName);

    if (!classURI.equals(OWLNames.Cls.THING)) {
      owlClass.setSuperclassNames(SWRLOWLUtil.rdfResources2Names(owlNamedClass.getNamedSuperclasses(true)));
      owlClass.setDirectSuperClassNames(SWRLOWLUtil.rdfResources2Names(owlNamedClass.getNamedSuperclasses()));
      owlClass.setDirectSubClassNames(SWRLOWLUtil.rdfResources2Names(owlNamedClass.getNamedSubclasses()));
      owlClass.setEquivalentClassNames(SWRLOWLUtil.rdfResources2OWLNamedClassNames(owlNamedClass.getEquivalentClasses()));
      Set<String> equivalentClassSuperclassNames = new HashSet<String>();

      for (String equivalentClassName : owlClass.getEquivalentClassNames()) {
        edu.stanford.smi.protegex.owl.model.OWLNamedClass equivalentClass = SWRLOWLUtil.getOWLNamedClass(owlModel, equivalentClassName);
       
        Iterator equivalentClassSuperClassesIterator = equivalentClass.getNamedSuperclasses(true).iterator();
        while (equivalentClassSuperClassesIterator.hasNext()) {
          Object o = equivalentClassSuperClassesIterator.next();
          if (o instanceof edu.stanford.smi.protegex.owl.model.OWLNamedClass) { // Ignore anonymous classes
            edu.stanford.smi.protegex.owl.model.OWLNamedClass equivalentClassSuperclass = (edu.stanford.smi.protegex.owl.model.OWLNamedClass)o;
            equivalentClassSuperclassNames.add(equivalentClassSuperclass.getName());
          } // if
        } /// while
      } // for

      owlClass.setEquivalentClassSuperclassNames(equivalentClassSuperclassNames);
    } // if

    return owlClass;
  } // getOWLClass
  
  public OWLIndividual getOWLIndividual(String individualURI) throws OWLConversionFactoryException
  { 
    edu.stanford.smi.protegex.owl.model.OWLIndividual individual = SWRLOWLUtil.createOWLIndividual(owlModel, individualURI);
    OWLIndividualImpl owlIndividual = new OWLIndividualImpl(individualURI, individual.getPrefixedName());

    buildDefiningClasses(owlIndividual, individual);
    buildDefiningSuperclasses(owlIndividual, individual);
    buildDefiningEquivalentClasses(owlIndividual, individual);
    buildSameAsIndividuals(owlIndividual, individual);

    return owlIndividual; 
  } // getOWLIndividual

  public OWLObjectProperty getOWLObjectProperty(String propertyURI) throws OWLConversionFactoryException
  {
    edu.stanford.smi.protegex.owl.model.OWLObjectProperty property = SWRLOWLUtil.createOWLObjectProperty(owlModel, propertyURI);
    String prefixedPropertyName = property.getPrefixedName();
    OWLObjectPropertyImpl owlObjectProperty = new OWLObjectPropertyImpl(propertyURI, prefixedPropertyName);

    initializeProperty(owlObjectProperty, property);

    return owlObjectProperty;
  } // getOWLObjectProperty

  public OWLDataProperty getOWLDataProperty(String propertyURI) throws OWLConversionFactoryException
  { 
    edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty property = SWRLOWLUtil.createOWLDatatypeProperty(owlModel, propertyURI);
    String prefixedPropertyName = property.getPrefixedName();
    OWLDataPropertyImpl owlDataProperty = new OWLDataPropertyImpl(propertyURI, prefixedPropertyName);

    initializeProperty(owlDataProperty, property);

    return owlDataProperty;
  } // getOWLDataProperty

  public void putOWLClass(OWLClass owlClass) throws OWLConversionFactoryException
  {
    String classURI = owlClass.getURI();
    edu.stanford.smi.protegex.owl.model.OWLClass cls, superclass;

    if (SWRLOWLUtil.isClass(owlModel, classURI)) cls = SWRLOWLUtil.getOWLNamedClass(owlModel, classURI);
    else cls = SWRLOWLUtil.getOWLNamedClass(owlModel, classURI);
    
    for (String superclassName : owlClass.getSuperclassNames()) {
        if (SWRLOWLUtil.isClass(owlModel, superclassName)) superclass = SWRLOWLUtil.getOWLNamedClass(owlModel, superclassName);
        else superclass = SWRLOWLUtil.getOWLNamedClass(owlModel, superclassName);
        
        if (!cls.isSubclassOf(superclass)) cls.addSuperclass(superclass);
    } // for
  } // putOWLClass

  public void putOWLIndividual(OWLIndividual owlIndividual) throws OWLConversionFactoryException
  {
    String individualURI = owlIndividual.getURI();
    edu.stanford.smi.protegex.owl.model.OWLIndividual individual;
    
    if (SWRLOWLUtil.isIndividual(owlModel, individualURI)) individual = SWRLOWLUtil.getIndividual(owlModel, individualURI);
    else individual = SWRLOWLUtil.createIndividual(owlModel, individualURI);
    
    for (OWLClass owlClass : owlIndividual.getDefiningClasses()) {
      edu.stanford.smi.protegex.owl.model.RDFSClass cls = SWRLOWLUtil.getOWLNamedClass(owlModel, owlClass.getURI());

      if (!individual.hasRDFType(cls)) { 
        if (individual.hasRDFType(SWRLOWLUtil.getOWLThingClass(owlModel))) individual.setRDFType(cls);
        else individual.addRDFType(cls);
      } // if
    } // for
  } // putOWLIndividual

  public void putOWLAxiom(OWLAxiom axiom) throws OWLConversionFactoryException
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
  } // isValidURI

  public Set<OWLIndividual> getAllOWLIndividualsOfClass(String classURI) throws OWLConversionFactoryException
  {
    RDFSClass rdfsClass = SWRLOWLUtil.getRDFSNamedClass(owlModel, classURI);
    Collection instances = new ArrayList();
    Set<OWLIndividual> result = new HashSet<OWLIndividual>();

    if (rdfsClass != null) instances.addAll(rdfsClass.getInstances(true));

    Iterator iterator = instances.iterator();
    while (iterator.hasNext()) {
      Object o = iterator.next();
      if (o instanceof edu.stanford.smi.protegex.owl.model.OWLIndividual) { 
        edu.stanford.smi.protegex.owl.model.OWLIndividual individual = (edu.stanford.smi.protegex.owl.model.OWLIndividual)o;
        result.add(getOWLIndividual(individual.getName()));
      } 
    } // while
    return result;
  } // getAllOWLIndividualsOfClass  
  
  public boolean couldBeOWLNamedClass(String classURI)
  {
	 RDFResource  resource= SWRLOWLUtil.getRDFResource(owlModel, classURI);
	 
	 return (resource == null || resource instanceof OWLNamedClass);
  } // couldBeOWLNamedClass
  
  private OWLDataValue convertOWLDataValue(edu.stanford.smi.protegex.owl.model.RDFSLiteral literal) throws OWLConversionFactoryException 
  { 
    edu.stanford.smi.protegex.owl.model.RDFSDatatype datatype = literal.getDatatype();
    OWLDataValueImpl owlDataValue = null;

    try {
      if ((datatype == owlModel.getXSDint()) || (datatype == owlModel.getXSDinteger()))
        owlDataValue = new OWLDataValueImpl(literal.getInt());
      else if (datatype == owlModel.getXSDshort()) 
        owlDataValue = new OWLDataValueImpl(literal.getShort());
      else if (datatype == owlModel.getXSDlong()) 
        owlDataValue = new OWLDataValueImpl(literal.getLong());
      else if (datatype == owlModel.getXSDboolean()) 
        owlDataValue = new OWLDataValueImpl(literal.getBoolean());
      else if (datatype == owlModel.getXSDfloat()) 
        owlDataValue = new OWLDataValueImpl(literal.getFloat());
      else if (datatype == owlModel.getXSDdouble()) 
      owlDataValue = new OWLDataValueImpl(literal.getDouble());
      else if ((datatype == owlModel.getXSDstring())) 
        owlDataValue = new OWLDataValueImpl(literal.getString());
      else if ((datatype == owlModel.getXSDtime())) 
        owlDataValue = new OWLDataValueImpl(new XSDTime(literal.getString()));
      else if ((datatype == owlModel.getXSDanyURI())) 
        owlDataValue = new OWLDataValueImpl(new XSDAnyURI(literal.getString()));
      else if ((datatype == owlModel.getXSDbyte())) 
        owlDataValue = new OWLDataValueImpl(Byte.valueOf(literal.getString()));
      else if ((datatype == owlModel.getXSDduration())) 
        owlDataValue = new OWLDataValueImpl(new XSDDuration(literal.getString()));
      else if ((datatype == owlModel.getXSDdateTime())) 
        owlDataValue = new OWLDataValueImpl(new XSDDateTime(literal.getString()));
      else if ((datatype == owlModel.getXSDdate())) 
        owlDataValue = new OWLDataValueImpl(new XSDDate(literal.getString()));
      else throw new OWLConversionFactoryException("cannot create an OWLDataValue object for RDFS literal '" + literal.getString()
                                         + "' of type '" + datatype + "'");
    } catch (DataValueConversionException e) {
      throw new OWLConversionFactoryException("error creating an OWLDataValue object for RDFS literal value '" + literal.getString() + 
                                    "' with type " + datatype.getName() + ": " + e.getMessage());
    } // try

    return owlDataValue;
  } // convertOWLDataValue

  private ClassAtom convertClassAtom(edu.stanford.smi.protegex.owl.swrl.model.SWRLClassAtom atom) throws OWLConversionFactoryException 
  { 
    String classURI = (atom.getClassPredicate() != null) ? atom.getClassPredicate().getName() : null;
    String prefixedClassName = (atom.getClassPredicate() != null) ? atom.getClassPredicate().getPrefixedName() : null;
    ClassAtomImpl classAtom = new ClassAtomImpl(classURI, prefixedClassName);

    if (classURI == null) throw new OWLConversionFactoryException("empty class name in SWRLClassAtom: " + atom.getBrowserText());

    classAtom.addReferencedClassName(classURI);
    
    if (atom.getArgument1() instanceof edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable) {
      edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable variable = (edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable)atom.getArgument1();
      VariableAtomArgument argument1 = owlFactory.getSWRLVariableAtomArgument(variable.getPrefixedName());
      classAtom.setArgument1(argument1);
      classAtom.addReferencedVariableName(variable.getPrefixedName());
    } else if (atom.getArgument1() instanceof edu.stanford.smi.protegex.owl.model.OWLIndividual) {
      String individualName = ((edu.stanford.smi.protegex.owl.model.OWLIndividual)atom.getArgument1()).getName();
      OWLIndividual argument1 = owlFactory.getOWLIndividual(individualName);
      classAtom.setArgument1(argument1);
      classAtom.addReferencedIndividualName(argument1.getURI());
    } else if (atom.getArgument1() instanceof edu.stanford.smi.protegex.owl.model.OWLNamedClass) {
      String className = ((edu.stanford.smi.protegex.owl.model.OWLNamedClass)atom.getArgument1()).getName();
      OWLClass argument1 = owlFactory.getOWLClass(className);
      classAtom.setArgument1(argument1);
      classAtom.addReferencedClassName(className);
    } else if (atom.getArgument1() instanceof edu.stanford.smi.protegex.owl.model.OWLObjectProperty) {
      String propertyName = ((edu.stanford.smi.protegex.owl.model.OWLObjectProperty)atom.getArgument1()).getName();
      OWLObjectProperty argument1 = owlFactory.getOWLObjectProperty(propertyName);
      classAtom.setArgument1(argument1);
      classAtom.addReferencedPropertyName(propertyName);
    } else if (atom.getArgument1() instanceof edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty) {
      String propertyName = ((edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty)atom.getArgument1()).getName();
      OWLDataProperty argument1 = owlFactory.getOWLDataProperty(propertyName);
      classAtom.setArgument1(argument1);
      classAtom.addReferencedPropertyName(propertyName);
    } else throw new OWLConversionFactoryException("unexpected argument to class atom '" + atom.getBrowserText() + "'; expecting " +
    		                                        "variable or individual, got instance of '" + atom.getArgument1().getClass() + "'");

    return classAtom;
  } // convertClassAtom

  private IndividualPropertyAtom convertIndividualPropertyAtom(edu.stanford.smi.protegex.owl.swrl.model.SWRLIndividualPropertyAtom atom)
    throws OWLConversionFactoryException 
  { 
    String propertyURI = (atom.getPropertyPredicate() != null) ? atom.getPropertyPredicate().getName() : null;
    String prefixedPropertyName = (atom.getPropertyPredicate() != null) ? atom.getPropertyPredicate().getPrefixedName() : null;
    IndividualPropertyAtomImpl individualPropertyAtom = new IndividualPropertyAtomImpl(propertyURI, prefixedPropertyName);

    if (propertyURI == null) 
    	throw new OWLConversionFactoryException("empty property name in SWRLIndividualPropertyAtom: " + atom.getBrowserText());
    
    individualPropertyAtom.addReferencedPropertyName(propertyURI);

    if (atom.getArgument1() instanceof edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable) {
      edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable variable = (edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable)atom.getArgument1();
      VariableAtomArgument argument1 = owlFactory.getSWRLVariableAtomArgument(variable.getPrefixedName());
      individualPropertyAtom.setArgument1(argument1);
      individualPropertyAtom.addReferencedVariableName(variable.getPrefixedName());
    } else if (atom.getArgument1() instanceof edu.stanford.smi.protegex.owl.model.OWLIndividual) {
      edu.stanford.smi.protegex.owl.model.OWLIndividual individual = (edu.stanford.smi.protegex.owl.model.OWLIndividual)atom.getArgument1();
      OWLIndividual argument1 = owlFactory.getOWLIndividual(individual.getName());
      individualPropertyAtom.setArgument1(argument1);
      individualPropertyAtom.addReferencedIndividualName(argument1.getURI());
    } else throw new OWLConversionFactoryException("unexpected first argument to individual property atom '" + atom.getBrowserText() + 
                                         "' - expecting variable or individual, got instance of " + atom.getArgument1().getClass());

    if (atom.getArgument2() instanceof edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable) {
      edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable variable = (edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable)atom.getArgument2();
      VariableAtomArgument argument2 = owlFactory.getSWRLVariableAtomArgument(variable.getPrefixedName());
      individualPropertyAtom.setArgument2(argument2);
      individualPropertyAtom.addReferencedVariableName(variable.getPrefixedName());
    } else if (atom.getArgument2() instanceof edu.stanford.smi.protegex.owl.model.OWLIndividual) {
      edu.stanford.smi.protegex.owl.model.OWLIndividual individual = (edu.stanford.smi.protegex.owl.model.OWLIndividual)atom.getArgument2();
      OWLIndividual argument2 = owlFactory.getOWLIndividual(individual.getName());
      individualPropertyAtom.setArgument2(argument2);
      individualPropertyAtom.addReferencedIndividualName(argument2.getURI());
    } else if (atom.getArgument2() instanceof edu.stanford.smi.protegex.owl.model.OWLNamedClass) {
      edu.stanford.smi.protegex.owl.model.OWLNamedClass cls = (edu.stanford.smi.protegex.owl.model.OWLNamedClass)atom.getArgument2();
      OWLClass argument2 = owlFactory.getOWLClass(cls.getName());
      individualPropertyAtom.setArgument2(argument2);
      individualPropertyAtom.addReferencedClassName(argument2.getURI());
    } else if (atom.getArgument2() instanceof edu.stanford.smi.protegex.owl.model.OWLProperty) {
      edu.stanford.smi.protegex.owl.model.OWLProperty property = (edu.stanford.smi.protegex.owl.model.OWLProperty)atom.getArgument2();
      OWLProperty argument2;
      String propertyName = ((edu.stanford.smi.protegex.owl.model.OWLObjectProperty)property).getName();
      if (property.isObjectProperty()) argument2 = owlFactory.getOWLObjectProperty(propertyName);
      else  argument2 = owlFactory.getOWLDataProperty(propertyName);
      individualPropertyAtom.setArgument2(argument2);
      individualPropertyAtom.addReferencedPropertyName(propertyName);
    } else throw new OWLConversionFactoryException("unexpected second argument to individual property atom '" + atom.getBrowserText() + 
                                         "' - expecting variable or individual, got instance of " + atom.getArgument2().getClass());

    return individualPropertyAtom; 
  } // convertIndividualPropertyAtom

  private DatavaluedPropertyAtom convertDatavaluedPropertyAtom(edu.stanford.smi.protegex.owl.swrl.model.SWRLDatavaluedPropertyAtom atom) 
    throws OWLConversionFactoryException 
  { 
    String propertyURI = (atom.getPropertyPredicate() != null) ? atom.getPropertyPredicate().getName() : null;
    String prefixedPropertyName = (atom.getPropertyPredicate() != null) ? atom.getPropertyPredicate().getPrefixedName() : null;
    DatavaluedPropertyAtomImpl datavaluedPropertyAtom = new DatavaluedPropertyAtomImpl(propertyURI, prefixedPropertyName);

    if (propertyURI == null) 
    	throw new OWLConversionFactoryException("empty property name in SWRLDatavaluedPropertyAtom: " + atom.getBrowserText());

    datavaluedPropertyAtom.addReferencedPropertyName(propertyURI);

    if (atom.getArgument1() instanceof edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable) {
      edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable variable = (edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable)atom.getArgument1();
      VariableAtomArgument argument1 = owlFactory.getSWRLVariableAtomArgument(variable.getPrefixedName());
      datavaluedPropertyAtom.setArgument1(argument1);
      datavaluedPropertyAtom.addReferencedVariableName(variable.getPrefixedName());
    } else if (atom.getArgument1() instanceof edu.stanford.smi.protegex.owl.model.OWLIndividual) {
      edu.stanford.smi.protegex.owl.model.OWLIndividual individual = (edu.stanford.smi.protegex.owl.model.OWLIndividual)atom.getArgument1();
      OWLIndividual argument1 = owlFactory.getOWLIndividual(individual.getName());
      datavaluedPropertyAtom.setArgument1(argument1);
      datavaluedPropertyAtom.addReferencedIndividualName(argument1.getURI());
    } else throw new OWLConversionFactoryException("unexpected argument first to datavalued property atom '" + atom.getBrowserText() + 
                                         "' - expecting variable or individual, got instance of " + atom.getArgument1().getClass());

    if (atom.getArgument2() instanceof edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable) {
      edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable variable = (edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable)atom.getArgument2();
      VariableAtomArgument argument2 = owlFactory.getSWRLVariableAtomArgument(variable.getPrefixedName());
      datavaluedPropertyAtom.setArgument2(argument2);
      datavaluedPropertyAtom.addReferencedVariableName(variable.getPrefixedName());
    } else if (atom.getArgument2() instanceof RDFSLiteral) {
      OWLDataValue argument2 = convertOWLDataValue((RDFSLiteral)atom.getArgument2());
      datavaluedPropertyAtom.setArgument2(argument2);
    } else throw new OWLConversionFactoryException("unexpected second to datavalued property atom '" + atom.getBrowserText()  + 
                                         "' - expecting variable or literal, got instance of " + atom.getArgument2().getClass());
    
    return datavaluedPropertyAtom; 
  } // convertDatavaluedPropertyAtom

  private SameIndividualAtom convertSameIndividualAtom(edu.stanford.smi.protegex.owl.swrl.model.SWRLSameIndividualAtom atom) throws OWLConversionFactoryException 
  { 
    SameIndividualAtomImpl sameIndividualAtom = new SameIndividualAtomImpl();

    if (atom.getArgument1() instanceof edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable) {
      edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable variable = (edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable)atom.getArgument1();
      VariableAtomArgument argument1 = owlFactory.getSWRLVariableAtomArgument(variable.getPrefixedName());
      sameIndividualAtom.setArgument1(argument1);
      sameIndividualAtom.addReferencedVariableName(variable.getPrefixedName());
    } else if (atom.getArgument1() instanceof edu.stanford.smi.protegex.owl.model.OWLIndividual) {
      edu.stanford.smi.protegex.owl.model.OWLIndividual individual = (edu.stanford.smi.protegex.owl.model.OWLIndividual)atom.getArgument1();
      OWLIndividual argument1 = owlFactory.getOWLIndividual(individual.getName());
      sameIndividualAtom.setArgument1(argument1);
      sameIndividualAtom.addReferencedIndividualName(individual.getName());
    } else throw new OWLConversionFactoryException("unexpected first argument to atom '" + atom.getBrowserText() + 
                                         "' - expecting variable or individual, got instance of " + atom.getArgument1().getClass() + ".");

    if (atom.getArgument2() instanceof edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable) {
      edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable variable = (edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable)atom.getArgument2();
      VariableAtomArgument argument2 = owlFactory.getSWRLVariableAtomArgument(variable.getPrefixedName());
      sameIndividualAtom.setArgument2(argument2);
      sameIndividualAtom.addReferencedVariableName(variable.getPrefixedName());
    } else if (atom.getArgument2() instanceof edu.stanford.smi.protegex.owl.model.OWLIndividual) {
      edu.stanford.smi.protegex.owl.model.OWLIndividual individual = (edu.stanford.smi.protegex.owl.model.OWLIndividual)atom.getArgument2();
      OWLIndividual argument2 = owlFactory.getOWLIndividual(individual.getName());
      sameIndividualAtom.setArgument2(argument2);
      sameIndividualAtom.addReferencedIndividualName(individual.getName());
    } else throw new OWLConversionFactoryException("unexpected second argument to atom '" + atom.getBrowserText() + 
                                         "' - expecting variable or individual, got instance of " + atom.getArgument2().getClass() + ".");


    return sameIndividualAtom; 
  } // convertSameIndividualAtom

  private DifferentIndividualsAtom convertDifferentIndividualsAtom(edu.stanford.smi.protegex.owl.swrl.model.SWRLDifferentIndividualsAtom atom) 
    throws OWLConversionFactoryException 
  { 
    DifferentIndividualsAtomImpl differentIndividualsAtom = new DifferentIndividualsAtomImpl();

    if (atom.getArgument1() instanceof edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable) {
      edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable variable = (edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable)atom.getArgument1();
      VariableAtomArgument argument1 = owlFactory.getSWRLVariableAtomArgument(variable.getPrefixedName());
      differentIndividualsAtom.setArgument1(argument1);
      differentIndividualsAtom.addReferencedVariableName(variable.getPrefixedName());
    } else if (atom.getArgument1() instanceof edu.stanford.smi.protegex.owl.model.OWLIndividual) {
      edu.stanford.smi.protegex.owl.model.OWLIndividual individual = (edu.stanford.smi.protegex.owl.model.OWLIndividual)atom.getArgument1();
      OWLIndividual argument1 = owlFactory.getOWLIndividual(individual.getName());
      differentIndividualsAtom.setArgument1(argument1);
      differentIndividualsAtom.addReferencedIndividualName(individual.getName());
    } else throw new OWLConversionFactoryException("unexpected first argument to atom '" + atom.getBrowserText() + 
                                         "' - expecting variable or individual, got instance of " + atom.getArgument1().getClass() + ".");

    if (atom.getArgument2() instanceof edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable) {
      edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable variable = (edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable)atom.getArgument2();
      VariableAtomArgument argument2 = owlFactory.getSWRLVariableAtomArgument(variable.getPrefixedName());
      differentIndividualsAtom.setArgument2(argument2);
      differentIndividualsAtom.addReferencedVariableName(variable.getPrefixedName());
    } else if (atom.getArgument2() instanceof edu.stanford.smi.protegex.owl.model.OWLIndividual) {
      edu.stanford.smi.protegex.owl.model.OWLIndividual individual = (edu.stanford.smi.protegex.owl.model.OWLIndividual)atom.getArgument2();
      OWLIndividual argument2 = owlFactory.getOWLIndividual(individual.getName());
      differentIndividualsAtom.setArgument2(argument2);
      differentIndividualsAtom.addReferencedIndividualName(individual.getName());
    } else throw new OWLConversionFactoryException("unexpected second argument to atom '" + atom.getBrowserText() + 
                                         "' - expecting variable or individual, got instance of " + atom.getArgument2().getClass() + ".");


    return differentIndividualsAtom; 
  } // convertDifferentIndividualsAtom

  private BuiltInAtom convertBuiltInAtom(edu.stanford.smi.protegex.owl.swrl.model.SWRLBuiltinAtom atom) throws OWLConversionFactoryException  
  { 
    String builtInName = (atom.getBuiltin() != null) ? atom.getBuiltin().getName() : null;
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
        arguments.add(owlFactory.getSWRLVariableBuiltInArgument(variable.getPrefixedName()));
        builtInAtom.addReferencedVariableName(variable.getPrefixedName());
      } else if (o instanceof edu.stanford.smi.protegex.owl.model.OWLIndividual) {
        edu.stanford.smi.protegex.owl.model.OWLIndividual individual = (edu.stanford.smi.protegex.owl.model.OWLIndividual)o;
        arguments.add(getOWLIndividual(individual.getName()));
        builtInAtom.addReferencedIndividualName(individual.getName());
      } else  if (o instanceof edu.stanford.smi.protegex.owl.model.OWLNamedClass) {
        edu.stanford.smi.protegex.owl.model.OWLNamedClass cls = (edu.stanford.smi.protegex.owl.model.OWLNamedClass)o;
        arguments.add(getOWLClass(cls.getName()));
        builtInAtom.addReferencedClassName(cls.getName());
      } else  if (o instanceof edu.stanford.smi.protegex.owl.model.OWLProperty) {
        edu.stanford.smi.protegex.owl.model.OWLProperty property = (edu.stanford.smi.protegex.owl.model.OWLProperty)o;
        String propertyName = ((edu.stanford.smi.protegex.owl.model.OWLObjectProperty)property).getName();
      if (property.isObjectProperty()) arguments.add(owlFactory.getOWLObjectProperty(propertyName));
      else  arguments.add(owlFactory.getOWLDataProperty(propertyName));
      builtInAtom.addReferencedPropertyName(propertyName);
      } else  if (o instanceof RDFSLiteral) arguments.add(convertOWLDataValue((RDFSLiteral)o));
      else  if (o instanceof Number) arguments.add(owlFactory.getOWLDataValue((Number)o));
      else  if (o instanceof String) arguments.add(owlFactory.getOWLDataValue((String)o));
      else  if (o instanceof Boolean) arguments.add(owlFactory.getOWLDataValue((Boolean)o));
      else throw new OWLConversionFactoryException("unknown type for argument '" + o + "' to built-in '" + builtInPrefixedName + "'");
    } // while

    builtInAtom.setBuiltInArguments(arguments);

    return builtInAtom; 
  } // convertBuiltInAtom

  private void write2OWLModel(OWLClassAssertionAxiom axiom) throws OWLConversionFactoryException
  {
    String classURI = axiom.getDescription().getURI();
    String individualURI = axiom.getIndividual().getURI();
    SWRLOWLUtil.addClass(owlModel, individualURI, classURI);
  } // write2OWLModel

  private void write2OWLModel(OWLClassPropertyAssertionAxiom axiom) throws OWLConversionFactoryException
  {
    String propertyURI = axiom.getProperty().getURI();
    String subjectIndividualName = axiom.getSubject().getURI();
    String objectClassName = axiom.getObject().getURI();
    edu.stanford.smi.protegex.owl.model.RDFProperty property = SWRLOWLUtil.getOWLProperty(owlModel, propertyURI);
    edu.stanford.smi.protegex.owl.model.OWLIndividual subjectIndividual;
    edu.stanford.smi.protegex.owl.model.OWLNamedClass objectClass = null;
    
    if (property == null) throw new OWLConversionFactoryException("invalid property name: " + propertyURI);
    
    subjectIndividual = SWRLOWLUtil.getOWLIndividual(owlModel, subjectIndividualName);
    if (subjectIndividual == null) throw new OWLConversionFactoryException("invalid individual name '" + subjectIndividualName + "'");

    objectClass = SWRLOWLUtil.getOWLNamedClass(owlModel, objectClassName); 

    if (!subjectIndividual.hasPropertyValue(property, objectClass, false)) subjectIndividual.addPropertyValue(property, objectClass);
  } // write2OWLModel

  private void write2OWLModel(OWLDataPropertyAssertionAxiom axiom) throws OWLConversionFactoryException
  {
    edu.stanford.smi.protegex.owl.model.OWLIndividual subjectIndividual;
    String propertyURI = axiom.getProperty().getURI();
    String subjectIndividualName = axiom.getSubject().getURI();
    edu.stanford.smi.protegex.owl.model.RDFProperty property = SWRLOWLUtil.getOWLProperty(owlModel, propertyURI);
    edu.stanford.smi.protegex.owl.model.RDFSDatatype rangeDatatype = property.getRangeDatatype();
    Object objectValue;

    if (property == null) throw new OWLConversionFactoryException("invalid property name '" + propertyURI + "'");
    
    subjectIndividual = SWRLOWLUtil.getOWLIndividual(owlModel, subjectIndividualName);
    if (subjectIndividual == null) throw new OWLConversionFactoryException("invalid individual name '" + subjectIndividualName + "'");

    if (rangeDatatype == null) {
      if (axiom.getObject().isString()) objectValue = axiom.getObject().toString();
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
    String subjectIndividualName = axiom.getSubject().getURI();
    String objectIndividualName = axiom.getObject().getURI();
    edu.stanford.smi.protegex.owl.model.RDFProperty property = SWRLOWLUtil.getOWLProperty(owlModel, propertyURI);
    
    if (property == null) throw new OWLConversionFactoryException("invalid property name '" + propertyURI + "'");
    
    subjectIndividual = SWRLOWLUtil.getOWLIndividual(owlModel, subjectIndividualName);
    if (subjectIndividual == null) throw new OWLConversionFactoryException("invalid individual name '" + subjectIndividualName + "'");
 
    objectIndividual = SWRLOWLUtil.getOWLIndividual(owlModel, objectIndividualName); 
    if (objectIndividual == null) throw new OWLConversionFactoryException("invalid individual name '" + objectIndividualName + "'");

    if (!subjectIndividual.hasPropertyValue(property, objectIndividual, false)) subjectIndividual.addPropertyValue(property, objectIndividual);
  } // write2OWLModel

  private void write2OWLModel(OWLPropertyPropertyAssertionAxiom axiom) throws OWLConversionFactoryException
  {
    String propertyURI = axiom.getProperty().getURI();
    String subjectIndividualName = axiom.getSubject().getURI();
    String objectPropertyName = axiom.getObject().getURI();
    edu.stanford.smi.protegex.owl.model.RDFProperty property = SWRLOWLUtil.getOWLProperty(owlModel, propertyURI);
    edu.stanford.smi.protegex.owl.model.OWLIndividual subjectIndividual;
    edu.stanford.smi.protegex.owl.model.OWLProperty objectProperty;
    
    if (property == null) throw new OWLConversionFactoryException("invalid property name '" + propertyURI + "'");
    
    subjectIndividual = SWRLOWLUtil.getOWLIndividual(owlModel, subjectIndividualName);
    if (subjectIndividual == null) throw new OWLConversionFactoryException("invalid individual name '" + subjectIndividualName + "'");

    objectProperty = SWRLOWLUtil.getOWLProperty(owlModel, objectPropertyName); 
    if (objectProperty == null) throw new OWLConversionFactoryException("invalid individual name '" + objectPropertyName + "'");

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
    String subClassName = axiom.getSubClass().getURI();
    String superClassName = axiom.getSuperClass().getURI();
    SWRLOWLUtil.addSuperClass(owlModel, subClassName, superClassName);
  } // write2OWLModel
  
  private DataRangeAtom convertDataRangeAtom(edu.stanford.smi.protegex.owl.swrl.model.SWRLDataRangeAtom atom) 
    throws OWLConversionFactoryException 
  { 
    throw new OWLConversionFactoryException("SWRL data range atoms not implemented.");
  } // convertDataRangeAtom

  private Atom convertSWRLAtom(SWRLAtom swrlAtom) throws OWLConversionFactoryException
  {
    Atom atom;
    
    if (swrlAtom instanceof SWRLClassAtom) {
      atom = convertClassAtom((SWRLClassAtom)swrlAtom);
    } else if (swrlAtom instanceof SWRLDatavaluedPropertyAtom) {
      atom = convertDatavaluedPropertyAtom((SWRLDatavaluedPropertyAtom)swrlAtom);
    } else if (swrlAtom instanceof SWRLIndividualPropertyAtom) {
      atom = convertIndividualPropertyAtom((SWRLIndividualPropertyAtom)swrlAtom);
    } else if (swrlAtom instanceof SWRLSameIndividualAtom) {
      atom = convertSameIndividualAtom((SWRLSameIndividualAtom)swrlAtom);
    } else if (swrlAtom instanceof SWRLDifferentIndividualsAtom) {
      atom = convertDifferentIndividualsAtom((SWRLDifferentIndividualsAtom)swrlAtom);
    } else if (swrlAtom instanceof SWRLBuiltinAtom) {
      atom = convertBuiltInAtom((SWRLBuiltinAtom)swrlAtom);
    } else if (swrlAtom instanceof SWRLDataRangeAtom) 
      atom = convertDataRangeAtom((SWRLDataRangeAtom)swrlAtom);
    else throw new OWLConversionFactoryException("invalid SWRL atom: " + swrlAtom.getBrowserText());

    return atom;
  } // convertSWRLAtom

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
      if (subject != null && !localSubject.getName().equals(subject.getName())) continue; // If subject supplied, ensure it is same
      edu.stanford.smi.protegex.owl.model.OWLIndividual subjectIndividual = (edu.stanford.smi.protegex.owl.model.OWLIndividual)localSubject;
      
      for (Object object : localSubject.getPropertyValues(property)) {
        
        if (property.hasObjectRange()) { // Object property
          OWLObjectProperty objectProperty = owlFactory.getOWLObjectProperty(propertyURI);

          if (object instanceof edu.stanford.smi.protegex.owl.model.OWLIndividual) {
            edu.stanford.smi.protegex.owl.model.OWLIndividual objectIndividual = (edu.stanford.smi.protegex.owl.model.OWLIndividual)object;
            OWLIndividual subjectOWLIndividual = owlFactory.getOWLIndividual(subjectIndividual.getName());
            OWLIndividual objectOWLIndividual = owlFactory.getOWLIndividual(objectIndividual.getName());
            axiom = owlFactory.getOWLObjectPropertyAssertionAxiom(subjectOWLIndividual, objectProperty, objectOWLIndividual);
            propertyAssertions.add(axiom);
          } else if (object instanceof edu.stanford.smi.protegex.owl.model.OWLNamedClass) { // This will be OWL Full
            edu.stanford.smi.protegex.owl.model.OWLNamedClass objectClass = (edu.stanford.smi.protegex.owl.model.OWLNamedClass)object;
            OWLIndividual subjectOWLIndividual = owlFactory.getOWLIndividual(subjectIndividual.getName());
            OWLClass objectPropertyClassValue = owlFactory.getOWLClass(objectClass.getName());
            axiom = owlFactory.getOWLClassPropertyAssertionAxiom(subjectOWLIndividual, objectProperty, objectPropertyClassValue);
            propertyAssertions.add(axiom);
          } else if (object instanceof edu.stanford.smi.protegex.owl.model.OWLProperty) { // This will be OWL Full
            edu.stanford.smi.protegex.owl.model.OWLProperty objectPropertyValue = (edu.stanford.smi.protegex.owl.model.OWLProperty)object;
            OWLIndividual subjectOWLIndividual = owlFactory.getOWLIndividual(subjectIndividual.getName());
            OWLProperty objectPropertyPropertyValue;
            if (objectPropertyValue.isObjectProperty()) objectPropertyPropertyValue = owlFactory.getOWLObjectProperty(objectPropertyValue.getName());
            else objectPropertyPropertyValue = owlFactory.getOWLDataProperty(objectPropertyValue.getName());
            axiom = owlFactory.getOWLPropertyPropertyAssertionAxiom(subjectOWLIndividual, objectProperty, objectPropertyPropertyValue);
            propertyAssertions.add(axiom);                
          } // if
        } else { // DataProperty
          OWLIndividual subjectOWLIndividual = owlFactory.getOWLIndividual(subjectIndividual.getName());
          RDFSLiteral literal = owlModel.asRDFSLiteral(object);
          OWLDataValue datatypeValue = convertOWLDataValue(literal);
          OWLDataProperty datatypeProperty = owlFactory.getOWLDataProperty(propertyURI);
          axiom = owlFactory.getOWLDataPropertyAssertionAxiom(subjectOWLIndividual, datatypeProperty, datatypeValue);
          propertyAssertions.add(axiom);
        } // if
      } // for
    } // while
      
    return propertyAssertions;
  } // getOWLPropertyAssertionAxioms

  // TODO: This is incredibly inefficient. Need to use method in the OWLModel to get individuals with a particular property.
  public Set<OWLSameIndividualAxiom> getOWLSameIndividualAxioms() throws OWLConversionFactoryException
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
          result.add(owlFactory.getOWLSameIndividualAxiom(owlFactory.getOWLIndividual(individual1.getName()), 
        		  		                                  owlFactory.getOWLIndividual(individual2.getName())));
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
		  if (!(object1 instanceof OWLIndividual)) continue; // Deal only with OWL individuals (could return metaclass, for example)
		  edu.stanford.smi.protegex.owl.model.OWLIndividual individual1 = (edu.stanford.smi.protegex.owl.model.OWLIndividual)object1;
		  if (individual1.hasPropertyValue(differentFromProperty)) {
			  Collection individuals = (Collection)individual1.getPropertyValues(differentFromProperty);
			  Iterator individualsIterator2 = individuals.iterator();
			  while (individualsIterator2.hasNext()) {
				  Object object2 = individualsIterator2.next();
				  if (!(object2 instanceof edu.stanford.smi.protegex.owl.model.OWLIndividual)) continue;
				  edu.stanford.smi.protegex.owl.model.OWLIndividual individual2 = (edu.stanford.smi.protegex.owl.model.OWLIndividual)object2;
				  result.add(owlFactory.getOWLDifferentIndividualsAxiom(owlFactory.getOWLIndividual(individual1.getName()), 
	                                                                    owlFactory.getOWLIndividual(individual2.getName())));
			  } // while
		  } // if
	  } // while
	  
	  if (!allDifferents.isEmpty()) {
		  Iterator allDifferentsIterator = allDifferents.iterator();
		  while (allDifferentsIterator.hasNext()) {
			  OWLAllDifferent owlAllDifferent = (OWLAllDifferent)allDifferentsIterator.next();
			  
			  if (owlAllDifferent.getDistinctMembers().size() != 0) {
				  OWLDifferentIndividualsAxiom axiom;
				  Set<OWLIndividual> individuals = new HashSet<OWLIndividual>();
	          
				  Iterator individualsIterator = owlAllDifferent.getDistinctMembers().iterator();
				  while (individualsIterator.hasNext()) {
					  RDFIndividual individual = (RDFIndividual)individualsIterator.next();
					  if (individual instanceof edu.stanford.smi.protegex.owl.model.OWLIndividual) { // Ignore non OWL individuals
						  String individualName = ((edu.stanford.smi.protegex.owl.model.OWLIndividual)individual).getName();
						  OWLIndividual owlIndividual = owlFactory.getOWLIndividual(individualName);
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
  {
    owlPropertyImpl.setDomainClassNames(SWRLOWLUtil.rdfResources2Names(property.getUnionDomain(true)));
    owlPropertyImpl.setRangeClassNames(SWRLOWLUtil.rdfResources2Names(property.getUnionRangeClasses()));
    owlPropertyImpl.setSuperPropertyNames(SWRLOWLUtil.rdfResources2Names(property.getSuperproperties(true)));
    owlPropertyImpl.setSubPropertyNames(SWRLOWLUtil.rdfResources2Names(property.getSubproperties(true)));
    owlPropertyImpl.setEquivalentPropertyNames(SWRLOWLUtil.rdfResources2Names(property.getEquivalentProperties()));
    Set<String> equivalentPropertySuperPropertyNames = new HashSet<String>();

    for (String equivalentPropertyName : owlPropertyImpl.getEquivalentPropertyNames()) {
      edu.stanford.smi.protegex.owl.model.RDFProperty equivalentProperty = SWRLOWLUtil.getOWLProperty(owlModel, equivalentPropertyName);
      Iterator equivalentPropertySuperPropertiesIterator = equivalentProperty.getSuperproperties(true).iterator();
      while (equivalentPropertySuperPropertiesIterator.hasNext()) {
        edu.stanford.smi.protegex.owl.model.RDFProperty equivalentPropertySuperProperty = (edu.stanford.smi.protegex.owl.model.RDFProperty)equivalentPropertySuperPropertiesIterator.next();
        equivalentPropertySuperPropertyNames.add(equivalentPropertySuperProperty.getName());
      } /// while
    } // for

    owlPropertyImpl.setEquivalentPropertySuperPropertyNames(equivalentPropertySuperPropertyNames);
  } // initializeProperty

  private void buildDefiningClasses(OWLIndividualImpl owlIndividualImpl, edu.stanford.smi.protegex.owl.model.OWLIndividual individual) 
    throws OWLConversionFactoryException
  { 
    for (Object o : individual.getRDFTypes()) {
      RDFSClass cls = (RDFSClass)o; 
      if (!cls.isAnonymous() && cls instanceof edu.stanford.smi.protegex.owl.model.OWLNamedClass) {
        String className = ((edu.stanford.smi.protegex.owl.model.OWLNamedClass)cls).getName();
        owlIndividualImpl.addDefiningClass(getOWLClass(className));
      } // if
    } // for
  } // buildDefiningClasses

  private void buildDefiningSuperclasses(OWLIndividualImpl owlIndividualImpl, edu.stanford.smi.protegex.owl.model.OWLIndividual individual) 
    throws OWLConversionFactoryException
  {
    Iterator definingClassesIterator = individual.getRDFTypes().iterator(); // Could be more than one defining type
    while (definingClassesIterator.hasNext()) {
      RDFSClass definingClass = (RDFSClass)definingClassesIterator.next();
      Iterator superClassesIterator = definingClass.getNamedSuperclasses(true).iterator();
      while (superClassesIterator.hasNext()) {
        RDFSClass cls = (RDFSClass)superClassesIterator.next();
        if (cls instanceof edu.stanford.smi.protegex.owl.model.OWLNamedClass) {
          String superClassName = ((edu.stanford.smi.protegex.owl.model.OWLNamedClass)cls).getName();
          OWLClass superClass = owlFactory.getOWLClass(superClassName);
          if (!owlIndividualImpl.getDefiningSuperclasses().contains(superClass)) owlIndividualImpl.addDefiningSuperclass(superClass);
        } // if
      } // while
    } // while
  } // buildDefiningSuperclasses

  private void buildDefiningEquivalentClasses(OWLIndividualImpl owlIndividualImpl, edu.stanford.smi.protegex.owl.model.OWLIndividual individual) 
    throws OWLConversionFactoryException
  {
    Iterator definingClassesIterator = individual.getRDFTypes().iterator(); // Could be more than one defining type
    while (definingClassesIterator.hasNext()) {
      RDFSClass definingClass = (RDFSClass)definingClassesIterator.next();
      Iterator equivalentClassesIterator = definingClass.getEquivalentClasses().iterator();
      while (equivalentClassesIterator.hasNext()) {
        RDFSClass cls1 = (RDFSClass)equivalentClassesIterator.next();
        if (cls1 instanceof edu.stanford.smi.protegex.owl.model.OWLNamedClass) {
          String class1Name = ((edu.stanford.smi.protegex.owl.model.OWLNamedClass)cls1).getName();
          OWLClass equivalentClass = owlFactory.getOWLClass(class1Name);
          if (!owlIndividualImpl.getDefiningEquivalentClasses().contains(equivalentClass)) {
            Iterator equivalentClassesSuperclassesIterator = cls1.getNamedSuperclasses(true).iterator();
            while (equivalentClassesSuperclassesIterator.hasNext()) {
              RDFSClass cls2 = (RDFSClass)equivalentClassesSuperclassesIterator.next();
              if (cls2 instanceof edu.stanford.smi.protegex.owl.model.OWLNamedClass) {
                String class2Name = ((edu.stanford.smi.protegex.owl.model.OWLNamedClass)cls2).getName();
                OWLClass equivalentClassSuperclass = owlFactory.getOWLClass(class2Name);
                if (!owlIndividualImpl.getDefiningEquivalentClassSuperclasses().contains(equivalentClassSuperclass)) 
                  owlIndividualImpl.addDefiningEquivalentClassSuperclass(equivalentClassSuperclass);
              } // if
            } // if
            owlIndividualImpl.addDefiningEquivalentClass(equivalentClass);
          } // if
        } // if
      } // while
    } // while
  } // buildDefiningEquivalentClasses

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
        owlIndividualImpl.addSameAsIndividual(owlFactory.getOWLIndividual(sameAsIndividual.getName()));
      } // while
    } // if
  } // buildSameAsIndividuals

} // OWLConversionFactoryImpl
