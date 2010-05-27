
package edu.stanford.smi.protegex.owl.swrl.bridge.builtins.ddm;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.IndividualArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.Mapper;
import edu.stanford.smi.protegex.owl.swrl.bridge.MultiArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLDataValue;
import edu.stanford.smi.protegex.owl.swrl.bridge.builtins.AbstractSWRLBuiltInLibrary;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.BuiltInException;
import edu.stanford.smi.protegex.owl.swrl.ddm.DDMFactory;
import edu.stanford.smi.protegex.owl.swrl.ddm.Database;
import edu.stanford.smi.protegex.owl.swrl.ddm.DatabaseConnection;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLClass;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLDataPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLNamedIndividual;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLObjectPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLProperty;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.DataValue;

/**
 */
public class SWRLBuiltInLibraryImpl extends AbstractSWRLBuiltInLibrary 
{
  private static String SWRLDDMLibraryName = "SWRLDDMBuiltIns";

  public SWRLBuiltInLibraryImpl() 
  { 
    super(SWRLDDMLibraryName); 
  } // SWRLBuiltInLibraryImpl

  public void reset() 
  {
  } // reset

  public boolean makeDatabase(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(7, arguments.size());

    String jdbcDriverName = getArgumentAsAString(1, arguments);
    String serverName = getArgumentAsAString(2, arguments);
    String databaseName = getArgumentAsAString(3, arguments);
    int portNumber  = getArgumentAsAnInteger(4, arguments);
    String userID = getArgumentAsAString(5, arguments);
    String password = getArgumentAsAString(6, arguments);
    Database database = DDMFactory.createDatabase(jdbcDriverName, serverName, databaseName, portNumber);

    try { 
      DatabaseConnection connection = DDMFactory.createDatabaseConnection(database, userID, password);
    } catch (SQLException e) {
      throw new BuiltInException("SQL exception when connecting to database: " + e.getMessage());
    } // try

    return true;
  } // makeDatabase

  public boolean mapOWLClass(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
    boolean result = false, isUnboundIndividualArgument;
    OWLClass owlClass = null;
    Set<OWLNamedIndividual> individuals = new HashSet<OWLNamedIndividual>();
    String classURI;
    Mapper mapper = null;

    checkNumberOfArgumentsAtLeast(2, arguments.size());

    classURI = getArgumentAsAClassURI(0, arguments);

    isUnboundIndividualArgument = isUnboundArgument(1, arguments);

    if (!isUnboundIndividualArgument) throw new BuiltInException("bound arguments not yet implemented, class = '" + classURI + "'");

    owlClass = getBuiltInBridge().getOWLDataFactory().getOWLClass(classURI);  
   
    individuals = mapper.mapOWLClass(owlClass);
    
    //if (!individuals.isEmpty()) getInvokingBridge().injectOWLIndividuals(individuals);
    if (isUnboundIndividualArgument) {
      MultiArgument multiArgument = createMultiArgument();
      for (OWLNamedIndividual individual : individuals) {
      	IndividualArgument argument = createIndividualArgument(individual.getURI());
      	multiArgument.addArgument(argument);
      }
      arguments.get(1).setBuiltInResult(multiArgument);
      result = !multiArgument.hasNoArguments();
    } else result = false;

    return result;
  } // mapOWLClass

  public boolean mapOWLObjectProperty(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
    boolean hasSubject, hasObject;
    OWLProperty owlProperty;
    OWLNamedIndividual subjectOWLIndividual = null, objectOWLIndividual = null;
    Set<OWLObjectPropertyAssertionAxiom> axioms = new HashSet<OWLObjectPropertyAssertionAxiom>();
    String propertyURI;
    Mapper mapper;

    checkNumberOfArgumentsAtLeast(1, arguments.size());
    checkForUnboundArguments(arguments);

    propertyURI = getArgumentAsAPropertyURI(0, arguments);

    hasSubject = (arguments.size() > 1);
    hasObject = (arguments.size() > 2);

    owlProperty = getBuiltInBridge().getOWLDataFactory().getOWLObjectProperty(propertyURI);
    
    if (hasSubject) {
      String subjectIndividualName = getArgumentAsAnIndividualURI(1, arguments);
      subjectOWLIndividual = getBuiltInBridge().getOWLDataFactory().getOWLIndividual(subjectIndividualName);
    } // if

    if (hasObject) {
      String objectIndividualName = getArgumentAsAnIndividualURI(2, arguments);
      objectOWLIndividual = getBuiltInBridge().getOWLDataFactory().getOWLIndividual(objectIndividualName);
    } // if    
    
    mapper = null;
    
    if (!mapper.isMapped(owlProperty)) return false;
    
    if (!hasSubject && !hasObject) axioms = mapper.mapOWLObjectProperty(owlProperty);
    else if (hasSubject && !hasObject) axioms = mapper.mapOWLObjectProperty(owlProperty, subjectOWLIndividual);
    else axioms = mapper.mapOWLObjectProperty(owlProperty, subjectOWLIndividual, objectOWLIndividual);
    
    for (OWLAxiom axiom : axioms) getBuiltInBridge().injectOWLAxiom(axiom);

    return !axioms.isEmpty();
  } // mapOWLObjectProperty

  public boolean mapOWLDatatypeProperty(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
    boolean hasSubject, hasValue;
    OWLProperty owlProperty;
    OWLNamedIndividual subjectOWLIndividual = null;
    DataValue dataValue = null;
    OWLDataValue owlDataValue;
    Set<OWLDataPropertyAssertionAxiom> axioms = new HashSet<OWLDataPropertyAssertionAxiom>();
    String propertyURI;
    Mapper mapper = null;

    checkNumberOfArgumentsAtLeast(1, arguments.size());
    checkForUnboundArguments(arguments);

    propertyURI = getArgumentAsAPropertyURI(0, arguments);

    hasSubject = (arguments.size() > 1) && isArgumentAnIndividual(1, arguments);
    hasValue = (arguments.size() > 2 || (arguments.size() > 1 && isArgumentADatatypeValue(1, arguments)));

    owlProperty = getBuiltInBridge().getOWLDataFactory().getOWLDataProperty(propertyURI);
    
    if (hasSubject) {
      String subjectIndividualURI = getArgumentAsAnIndividualURI(1, arguments);
      subjectOWLIndividual = getBuiltInBridge().getOWLDataFactory().getOWLIndividual(subjectIndividualURI);
    } // if
    if (hasValue) {
      if (hasSubject) dataValue = getArgumentAsADataValue(2, arguments);
      else dataValue = getArgumentAsADataValue(1, arguments);
    } // if
        
    owlDataValue = getBuiltInBridge().getOWLDataValueFactory().getOWLDataValue(dataValue);
    //if (!mapper.isMapped(owlProperty)) return false;
    
    if (!hasSubject && !hasValue) axioms = mapper.mapOWLDataProperty(owlProperty);
    else if (hasSubject && !hasValue) axioms = mapper.mapOWLDataProperty(owlProperty, subjectOWLIndividual);
    else if (!hasSubject && hasValue) {
    	axioms = mapper.mapOWLDataProperty(owlProperty, owlDataValue);
    } else {
    	axioms = mapper.mapOWLDataProperty(owlProperty, subjectOWLIndividual, owlDataValue);
    }
    
    for (OWLAxiom axiom : axioms)getBuiltInBridge().injectOWLAxiom(axiom);

    return !axioms.isEmpty();
  } // mapOWLDatatypeProperty

} // SWRLBuiltInLibraryImpl
