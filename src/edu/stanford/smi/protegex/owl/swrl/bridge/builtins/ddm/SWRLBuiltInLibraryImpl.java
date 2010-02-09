
package edu.stanford.smi.protegex.owl.swrl.bridge.builtins.ddm;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.stanford.smi.protegex.owl.swrl.bridge.ArgumentFactory;
import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.Mapper;
import edu.stanford.smi.protegex.owl.swrl.bridge.MultiArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLAxiom;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLClass;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLDataPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLDataValue;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLIndividual;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLObjectPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLProperty;
import edu.stanford.smi.protegex.owl.swrl.bridge.builtins.AbstractSWRLBuiltInLibrary;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.BuiltInException;
import edu.stanford.smi.protegex.owl.swrl.ddm.DDMFactory;
import edu.stanford.smi.protegex.owl.swrl.ddm.Database;
import edu.stanford.smi.protegex.owl.swrl.ddm.DatabaseConnection;

/**
 */
public class SWRLBuiltInLibraryImpl extends AbstractSWRLBuiltInLibrary 
{
  private static String SWRLDDMLibraryName = "SWRLDDMBuiltIns";

  private ArgumentFactory argumentFactory;

  public SWRLBuiltInLibraryImpl() 
  { 
    super(SWRLDDMLibraryName); 

    argumentFactory = ArgumentFactory.getFactory();
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
    Set<OWLIndividual> individuals = new HashSet<OWLIndividual>();
    String className;
    Mapper mapper = null;

    checkNumberOfArgumentsAtLeast(2, arguments.size());

    className = getArgumentAsAClassName(0, arguments);

    isUnboundIndividualArgument = isUnboundArgument(1, arguments);

    if (!isUnboundIndividualArgument) throw new BuiltInException("bound arguments not yet implemented, class = '" + className + "'");

    owlClass = getInvokingBridge().getOWLDataFactory().getOWLClass(className);  
   
    individuals = mapper.mapOWLClass(owlClass);
    
    //if (!individuals.isEmpty()) getInvokingBridge().injectOWLIndividuals(individuals);
    if (isUnboundIndividualArgument) {
      MultiArgument multiArgument = argumentFactory.createMultiArgument(getVariableName(1, arguments));
      for (OWLIndividual individual : individuals) multiArgument.addArgument(individual);
      arguments.set(1, multiArgument);
      result = !multiArgument.hasNoArguments();
    } else result = false;

    return result;
  } // mapOWLClass

  public boolean mapOWLObjectProperty(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
    boolean hasSubject, hasObject;
    OWLProperty owlProperty;
    OWLIndividual subjectOWLIndividual = null, objectOWLIndividual = null;
    Set<OWLObjectPropertyAssertionAxiom> axioms = new HashSet<OWLObjectPropertyAssertionAxiom>();
    String propertyName;
    Mapper mapper;

    checkNumberOfArgumentsAtLeast(1, arguments.size());
    checkForUnboundArguments(arguments);

    propertyName = getArgumentAsAPropertyName(0, arguments);

    hasSubject = (arguments.size() > 1);
    hasObject = (arguments.size() > 2);

    owlProperty = getInvokingBridge().getOWLDataFactory().getOWLObjectProperty(propertyName);
    
    if (hasSubject) {
      String subjectIndividualName = getArgumentAsAnIndividualName(1, arguments);
      subjectOWLIndividual = getInvokingBridge().getOWLDataFactory().getOWLIndividual(subjectIndividualName);
    } // if

    if (hasObject) {
      String objectIndividualName = getArgumentAsAnIndividualName(2, arguments);
      objectOWLIndividual = getInvokingBridge().getOWLDataFactory().getOWLIndividual(objectIndividualName);
    } // if    
    
    mapper = null;
    
    if (!mapper.isMapped(owlProperty)) return false;
    
    if (!hasSubject && !hasObject) axioms = mapper.mapOWLObjectProperty(owlProperty);
    else if (hasSubject && !hasObject) axioms = mapper.mapOWLObjectProperty(owlProperty, subjectOWLIndividual);
    else axioms = mapper.mapOWLObjectProperty(owlProperty, subjectOWLIndividual, objectOWLIndividual);
    
    for (OWLAxiom axiom : axioms) getInvokingBridge().injectOWLAxiom(axiom);

    return !axioms.isEmpty();
  } // mapOWLObjectProperty

  public boolean mapOWLDatatypeProperty(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
    boolean hasSubject, hasValue;
    OWLProperty owlProperty;
    OWLIndividual subjectOWLIndividual = null;
    OWLDataValue value = null;
    Set<OWLDataPropertyAssertionAxiom> axioms = new HashSet<OWLDataPropertyAssertionAxiom>();
    String propertyName;
    Mapper mapper = null;

    checkNumberOfArgumentsAtLeast(1, arguments.size());
    checkForUnboundArguments(arguments);

    propertyName = getArgumentAsAPropertyName(0, arguments);

    hasSubject = (arguments.size() > 1) && isArgumentAnIndividual(1, arguments);
    hasValue = (arguments.size() > 2 || (arguments.size() > 1 && isArgumentADatatypeValue(1, arguments)));

    owlProperty = getInvokingBridge().getOWLDataFactory().getOWLDataProperty(propertyName);
    
    if (hasSubject) {
      String subjectIndividualName = getArgumentAsAnIndividualName(1, arguments);
      subjectOWLIndividual = getInvokingBridge().getOWLDataFactory().getOWLIndividual(subjectIndividualName);
    } // if
    if (hasValue) {
      if (hasSubject) value = getArgumentAsAnOWLDatatypeValue(2, arguments);
      else value = getArgumentAsAnOWLDatatypeValue(1, arguments);
    } // if
        
    //if (!mapper.isMapped(owlProperty)) return false;
    
    if (!hasSubject && !hasValue) axioms = mapper.mapOWLDatatypeProperty(owlProperty);
    else if (hasSubject && !hasValue) axioms = mapper.mapOWLDatatypeProperty(owlProperty, subjectOWLIndividual);
    else if (!hasSubject && hasValue) axioms = mapper.mapOWLDatatypeProperty(owlProperty, value);
    else axioms = mapper.mapOWLDatatypeProperty(owlProperty, subjectOWLIndividual, value);
    
    for (OWLAxiom axiom : axioms)getInvokingBridge().injectOWLAxiom(axiom);

    return !axioms.isEmpty();
  } // mapOWLDatatypeProperty

} // SWRLBuiltInLibraryImpl
