
package edu.stanford.smi.protegex.owl.swrl.bridge.builtins.temporal;

import java.util.List;
import java.util.Set;

import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLDataValue;
import edu.stanford.smi.protegex.owl.swrl.bridge.SWRLBuiltInBridge;
import edu.stanford.smi.protegex.owl.swrl.bridge.builtins.AbstractSWRLBuiltInLibrary;
import edu.stanford.smi.protegex.owl.swrl.bridge.builtins.temporal.exceptions.TemporalException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.BuiltInException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.InvalidBuiltInArgumentException;
import edu.stanford.smi.protegex.owl.swrl.bridge.xsd.XSDDateTime;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLDataPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLLiteral;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLNamedIndividual;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLObjectPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLPropertyAssertionAxiom;

/**
 ** Implementation library for SWRL temporal built-ins. See <a href="http://protege.cim3.net/cgi-bin/wiki.pl?SWRLTemporalBuiltIns">here</a>
 ** for documentation on this built-in library.
 */
public class SWRLBuiltInLibraryImpl extends AbstractSWRLBuiltInLibrary
{
  public static final String TemporalLibraryName = "SWRLTemporalBuiltIns";
  
  public static final String Prefix = "temporal:";
  public static final String Namespace = "http://swrl.stanford.edu/ontologies/built-ins/3.3/temporal.owl#";
  
  private static String TemporalEquals = Prefix + "equals";
  private static String TemporalAfter = Prefix + "after";
  private static String TemporalBefore = Prefix + "before";
  private static String TemporalMeets = Prefix + "meets";
  private static String TemporalMetBy = Prefix + "metBy";
  private static String TemporalOverlaps = Prefix + "overlaps";
  private static String TemporalOverlappedBy = Prefix + "overlappedBy";
  private static String TemporalContains = Prefix + "contains";
  private static String TemporalDuring = Prefix + "during";
  private static String TemporalStarts = Prefix + "starts";
  private static String TemporalStartedBy = Prefix + "startedBy";
  private static String TemporalFinishes = Prefix + "finishes";
  private static String TemporalFinishedBy = Prefix + "finishedBy";
  private static String TemporalIntersects = Prefix + "intersects";

  private static String ExtendedPropositionClassName = Namespace + "ExtendedProposition";
  private static String ValidInstantClassName = Namespace + "ValidInstant";
  private static String ValidPeriodClassName = Namespace + "ValidPeriod";
  private static String GranularityClassName = Namespace + "Granularity";
  private static String HasValidTimePropertyName = Namespace + "hasValidTime";
  private static String HasTimePropertyName = Namespace + "hasTime";
  private static String HasStartTimePropertyName = Namespace + "hasStartTime";
  private static String HasFinishTimePropertyName = Namespace + "hasFinishTime";

  private Temporal temporal;
  
  public SWRLBuiltInLibraryImpl() 
  { 
    super(TemporalLibraryName); 
  } 

  public void reset()
  {
    XSDDatetimeStringProcessor d = new XSDDatetimeStringProcessor();
    temporal = new Temporal(d);
  }

  public boolean equals(List<BuiltInArgument> arguments) throws BuiltInException { return temporalOperation(TemporalEquals, arguments); }
  public boolean before(List<BuiltInArgument> arguments) throws BuiltInException { return temporalOperation(TemporalBefore, arguments); }
  public boolean after(List<BuiltInArgument> arguments) throws BuiltInException { return temporalOperation(TemporalAfter, arguments); }
  public boolean meets(List<BuiltInArgument> arguments) throws BuiltInException { return temporalOperation(TemporalMeets, arguments); }
  public boolean metBy(List<BuiltInArgument> arguments) throws BuiltInException { return temporalOperation(TemporalMetBy, arguments); }
  public boolean overlaps(List<BuiltInArgument> arguments) throws BuiltInException { return temporalOperation(TemporalOverlaps, arguments); }
  public boolean overlappedBy(List<BuiltInArgument> arguments) throws BuiltInException { return temporalOperation(TemporalOverlappedBy, arguments); }
  public boolean contains(List<BuiltInArgument> arguments) throws BuiltInException { return temporalOperation(TemporalContains, arguments); }
  public boolean during(List<BuiltInArgument> arguments) throws BuiltInException { return temporalOperation(TemporalDuring, arguments); }
  public boolean starts(List<BuiltInArgument> arguments) throws BuiltInException { return temporalOperation(TemporalStarts, arguments); }
  public boolean startedBy(List<BuiltInArgument> arguments) throws BuiltInException { return temporalOperation(TemporalStartedBy, arguments); }
  public boolean finishes(List<BuiltInArgument> arguments) throws BuiltInException { return temporalOperation(TemporalFinishes, arguments); }
  public boolean finishedBy(List<BuiltInArgument> arguments) throws BuiltInException { return temporalOperation(TemporalFinishedBy, arguments); }

  public boolean intersects(List<BuiltInArgument> arguments) throws BuiltInException { return temporalOperation(TemporalIntersects, arguments); }
  public boolean notIntersects(List<BuiltInArgument> arguments) throws BuiltInException { return !temporalOperation(TemporalIntersects, arguments); }

  public boolean notEquals(List<BuiltInArgument> arguments) throws BuiltInException { return !temporalOperation(TemporalEquals, arguments); }
  public boolean notBefore(List<BuiltInArgument> arguments) throws BuiltInException { return !temporalOperation(TemporalBefore, arguments); }
  public boolean notAfter(List<BuiltInArgument> arguments) throws BuiltInException { return !temporalOperation(TemporalAfter, arguments); }
  public boolean notMeets(List<BuiltInArgument> arguments) throws BuiltInException { return !temporalOperation(TemporalMeets, arguments); }
  public boolean notMetBy(List<BuiltInArgument> arguments) throws BuiltInException { return !temporalOperation(TemporalMetBy, arguments); }
  public boolean notOverlaps(List<BuiltInArgument> arguments) throws BuiltInException { return !temporalOperation(TemporalOverlaps, arguments); }
  public boolean notOverlappedBy(List<BuiltInArgument> arguments) throws BuiltInException { return !temporalOperation(TemporalOverlappedBy, arguments); }
  public boolean notContains(List<BuiltInArgument> arguments) throws BuiltInException { return !temporalOperation(TemporalContains, arguments); }
  public boolean notDuring(List<BuiltInArgument> arguments) throws BuiltInException { return !temporalOperation(TemporalDuring, arguments); }
  public boolean notStarts(List<BuiltInArgument> arguments) throws BuiltInException { return !temporalOperation(TemporalStarts, arguments); }
  public boolean notStartedBy(List<BuiltInArgument> arguments) throws BuiltInException { return !temporalOperation(TemporalStartedBy, arguments); }
  public boolean notFinishes(List<BuiltInArgument> arguments) throws BuiltInException { return !temporalOperation(TemporalFinishes, arguments); }
  public boolean notFinishedBy(List<BuiltInArgument> arguments) throws BuiltInException { return !temporalOperation(TemporalFinishedBy, arguments); }

  public boolean notDurationLessThan(List<BuiltInArgument> arguments) throws BuiltInException { return !durationLessThan(arguments); }
  public boolean notDurationLessThanOrEqualTo(List<BuiltInArgument> arguments) throws BuiltInException { return !durationLessThanOrEqualTo(arguments); }
  public boolean notDurationEqualTo(List<BuiltInArgument> arguments) throws BuiltInException { return !durationEqualTo(arguments); }
  public boolean notDurationGreaterThan(List<BuiltInArgument> arguments) throws BuiltInException { return !durationGreaterThan(arguments); }
  public boolean notDurationGreaterThanOrEqualTo(List<BuiltInArgument> arguments) throws BuiltInException { return !durationGreaterThanOrEqualTo(arguments); }

  /**
   * Accepts either three or four arguments. Returns true if the first duration argument is equal to the difference between two timestamps
   * at the granularity specified by the final argument. The timestamps are specified as either a mixture of two ValidInstant or datetime
   * arguments or in single ValidPeriod argument. If the duration argument is unbound, it is assigned to the time difference between the
   * two timestamps.
   */
  public boolean duration(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;
    long operationResult;
    int granularity, numberOfArguments = arguments.size();
    Period period;
    Instant i1, i2;

    checkNumberOfArgumentsInRange(3, 4, arguments.size());
    checkForUnboundNonFirstArguments(arguments);

    try {
      if (numberOfArguments == 3) {
        granularity = getBuiltInArgumentAsAGranularity(2, arguments);
        period = getArgumentAsAPeriod(1, arguments, granularity);
        operationResult = period.duration(granularity);
      } else { // 4 arguments
        granularity = getBuiltInArgumentAsAGranularity(3, arguments);
        i1 = getArgumentAsAnInstant(1, arguments, granularity);
        i2 = getArgumentAsAnInstant(2, arguments, granularity);
        operationResult = i1.duration(i2, granularity);
      } // if

      if (isUnboundArgument(0, arguments)) {
        arguments.get(0).setBuiltInResult(createDataValueArgument(operationResult)); // Bind the result to the first parameter
        result = true;
      } else {
        long argument1 = getArgumentAsALong(0, arguments);
        result = (argument1 == operationResult);
      } //if
    } catch (TemporalException e) {
      throw new BuiltInException(e.getMessage());
    } // try

    return result;
  } 

  public boolean durationLessThan(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsInRange(3, 4, arguments.size());
    checkForUnboundArguments(arguments);
    long argument1, operationResult;
    List<BuiltInArgument> newArguments = copyArguments(arguments);
    
    argument1 = getArgumentAsALong(0, arguments);

    newArguments.get(0).setUnbound();
    duration(newArguments);
    operationResult = getArgumentAsALong(0, newArguments);

    return argument1 < operationResult;
  }     

  public boolean durationLessThanOrEqualTo(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsInRange(3, 4, arguments.size());
    checkForUnboundArguments(arguments);
    long argument1, operationResult;
    List<BuiltInArgument> newArguments = copyArguments(arguments);
    
    argument1 = getArgumentAsALong(0, arguments);

    newArguments.get(0).setUnbound();
    duration(newArguments);
    operationResult = getArgumentAsALong(0, newArguments);

    return argument1 <= operationResult;
  } // durationLessThanOrEqualTo

  public boolean durationEqualTo(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsInRange(3, 4, arguments.size());
    checkForUnboundArguments(arguments);
    long argument1, operationResult;
    List<BuiltInArgument> newArguments = copyArguments(arguments);
    
    argument1 = getArgumentAsALong(0, arguments);

    newArguments.get(0).setUnbound();
    duration(newArguments);
    operationResult = getArgumentAsALong(0, newArguments);

    return argument1 == operationResult;
  } // durationLessThan    

  public boolean durationGreaterThan(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsInRange(3, 4, arguments.size());
    checkForUnboundArguments(arguments);
    long argument1, operationResult;
    List<BuiltInArgument> newArguments = copyArguments(arguments);
    
    argument1 = getArgumentAsALong(0, arguments);

    newArguments.get(0).setUnbound();
    duration(newArguments);
    operationResult = getArgumentAsALong(0, newArguments);

    return argument1 > operationResult;
  } // durationGreaterThan

  public boolean durationGreaterThanOrEqualTo(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsInRange(3, 4, arguments.size());
    checkForUnboundArguments(arguments);
    long argument1, operationResult;
    List<BuiltInArgument> newArguments = copyArguments(arguments);
    
    argument1 = getArgumentAsALong(0, arguments);

    newArguments.get(0).setUnbound();
    duration(newArguments);
    operationResult = getArgumentAsALong(0, newArguments);

    return argument1 >= operationResult;
  } // durationGreaterThanOrEqualTo

  /**
   ** Returns true if the first timestamp argument is equal to the second timestamps argument plus the third count argument at the
   ** granularity specified by the fourth argument. The timestamps are specified as either a ValidInstant, or xsd:dateTime
   ** arguments. If the first argument is unbound, it is assigned the result of the addition.
   */
  public boolean add(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    checkNumberOfArgumentsEqualTo(4, arguments.size());
    checkForUnboundNonFirstArguments(arguments);

    try {
      long granuleCount = getArgumentAsAnInteger(2, arguments);
      int granularity = getBuiltInArgumentAsAGranularity(3, arguments);
      Instant operationResult = getArgumentAsAnInstant(1, arguments, granularity);

      operationResult.addGranuleCount(granuleCount, granularity);

      if (isUnboundArgument(0, arguments)) {
        arguments.get(0).setBuiltInResult(createDataValueArgument(new XSDDateTime(operationResult.toString()))); // Bind the result to the first parameter
        result = true;
      } else {
        Instant argument1 = getArgumentAsAnInstant(0, arguments, granularity);
        result = (argument1.equals(operationResult, Temporal.FINEST));
      } //if
    } catch (TemporalException e) {
      throw new BuiltInException(e.getMessage());
    } // try

    return result;
  } // add

  private boolean temporalOperation(String operation, List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;
    int numberOfArguments = arguments.size();

    checkNumberOfArgumentsInRange(2, 4, numberOfArguments);
    checkForUnboundArguments(arguments);

    try {
      boolean hasGranularityArgument = isBuiltInArgumentAGranularity(numberOfArguments - 1, arguments);
      boolean has2nd3rdInstantArguments = hasGranularityArgument ? (numberOfArguments > 3) : (numberOfArguments > 2);
      int granularity = hasGranularityArgument ? getBuiltInArgumentAsAGranularity(numberOfArguments - 1, arguments) : Temporal.FINEST;
      Period p1 = getArgumentAsAPeriod(0, arguments, granularity);
      Period p2 = has2nd3rdInstantArguments ? getTwoInstantArgumentsAsAPeriod(1, 2, arguments, granularity) : getArgumentAsAPeriod(1, arguments, granularity);

      if (operation.equals(TemporalEquals)) result = p1.equals(p2, granularity);
      else if (operation.equals(TemporalBefore)) result = p1.before(p2, granularity);
      else if (operation.equals(TemporalAfter)) result = p1.after(p2, granularity);
      else if (operation.equals(TemporalMeets)) result = p1.meets(p2, granularity);
      else if (operation.equals(TemporalMetBy)) result = p1.met_by(p2, granularity);
      else if (operation.equals(TemporalOverlaps)) result = p1.overlaps(p2, granularity);
      else if (operation.equals(TemporalOverlappedBy)) result = p1.overlapped_by(p2, granularity);
      else if (operation.equals(TemporalContains)) result = p1.contains(p2, granularity);
      else if (operation.equals(TemporalDuring)) result = p1.during(p2, granularity);
      else if (operation.equals(TemporalStarts)) result = p1.starts(p2, granularity);
      else if (operation.equals(TemporalStartedBy)) result = p1.started_by(p2, granularity);
      else if (operation.equals(TemporalFinishes)) result = p1.finishes(p2, granularity);
      else if (operation.equals(TemporalFinishedBy)) result = p1.finished_by(p2, granularity);
      else if (operation.equals(TemporalIntersects)) result = p1.intersects(p2, granularity);
      else throw new BuiltInException("internal error - unknown temporal operator '" + operation + "'");
    } catch (TemporalException e) {
      throw new BuiltInException(e.getMessage());
    } // try

    return result;
  } // duration

  private Period getTwoInstantArgumentsAsAPeriod(int firstArgumentNumber, int secondArgumentNumber, 
                                                 List<BuiltInArgument> arguments, int granularity) 
    throws BuiltInException, TemporalException
  {
    Instant i1, i2;
    Period result;

    if (firstArgumentNumber >= arguments.size()) throw new InvalidBuiltInArgumentException(firstArgumentNumber, "out of range");
    if (secondArgumentNumber >= arguments.size()) throw new InvalidBuiltInArgumentException(secondArgumentNumber, "out of range");

    i1 = getArgumentAsAnInstant(firstArgumentNumber, arguments, granularity);
    i2 = getArgumentAsAnInstant(secondArgumentNumber, arguments, granularity);
    result = new Period(temporal, i1, i2, granularity);

    return result;
  }

  private Period getArgumentAsAPeriod(int argumentNumber, List<BuiltInArgument> arguments, int granularity) 
    throws BuiltInException, TemporalException
  {
    Period result = null;

    if (isArgumentADataValue(argumentNumber, arguments)) {
      String datetimeString = getArgumentAsAString(argumentNumber, arguments);
      result = new Period(temporal, datetimeString, datetimeString, granularity);
    } else if (isArgumentAnIndividual(argumentNumber, arguments)) {
      String individualURI = getArgumentAsAnIndividualURI(argumentNumber, arguments);
      if (getBuiltInBridge().isOWLIndividualOfClass(individualURI, ValidInstantClassName)) {
        Instant instant = convertValidInstantIndividual2Instant(individualURI, granularity);
        result = new Period(temporal, instant, granularity);
      } else if (getBuiltInBridge().isOWLIndividualOfClass(individualURI,  ValidPeriodClassName)) {
        result = convertValidPeriodIndividual2Period(individualURI, granularity);
      } else if (getBuiltInBridge().isOWLIndividualOfClass(individualURI,  ExtendedPropositionClassName)) {
        result = convertExtendedPropositionIndividual2Period(individualURI, granularity);
      } else throw new InvalidBuiltInArgumentException(argumentNumber, "individual " + individualURI + " is not a " +
                                                       ValidInstantClassName + " or " + ValidPeriodClassName + " or " +
                                                       ExtendedPropositionClassName);
    } else throw new InvalidBuiltInArgumentException(argumentNumber, "expecting an XSD datetime or " +
                                                     ValidInstantClassName + " or " + ValidPeriodClassName + " or " +
                                                     ExtendedPropositionClassName + " individual, got " + arguments.get(argumentNumber));
    return result;
  }

  private Instant getArgumentAsAnInstant(int argumentNumber, List<BuiltInArgument> arguments, int granularity) 
    throws BuiltInException, TemporalException
  {
    Instant result = null;

    if (isArgumentADataValue(argumentNumber, arguments)) {
      String datetimeString = getArgumentAsAString(argumentNumber, arguments);
      result = new Instant(temporal, datetimeString, granularity);
    } else if (isArgumentAnIndividual(argumentNumber, arguments)) {
      String individualURI = getArgumentAsAnIndividualURI(argumentNumber, arguments);
      if (getBuiltInBridge().isOWLIndividualOfClass(individualURI, ValidInstantClassName)) {
        result = convertValidInstantIndividual2Instant(individualURI, granularity);
      } else if (getBuiltInBridge().isOWLIndividualOfClass(individualURI,  ExtendedPropositionClassName)) {
        result = convertExtendedPropositionIndividual2Instant(individualURI, granularity);
      } else throw new InvalidBuiltInArgumentException(argumentNumber, "individual " + individualURI + " is not a " + ValidInstantClassName +
      		                                             "or an " + ExtendedPropositionClassName);
    } else throw new InvalidBuiltInArgumentException(argumentNumber, "expecting an XSD datetime or " + ValidInstantClassName + " or " +
    							                                   ExtendedPropositionClassName + " individual, got " + arguments.get(argumentNumber));
    return result;
  }

  private int getBuiltInArgumentAsAGranularity(int argumentNumber, List<BuiltInArgument> arguments) 
    throws TemporalException, BuiltInException
  {
    String granularityName;
    int granularity = -1;

    if (isArgumentADataValue(argumentNumber, arguments)) {
      granularityName = getArgumentAsAString(argumentNumber, arguments);
      granularity = Temporal.getIntegerGranularityRepresentation(granularityName);
    } else if (isArgumentAnIndividual(argumentNumber, arguments)) {
      String individualURI = getArgumentAsAnIndividualURI(argumentNumber, arguments);
      if (getBuiltInBridge().isOWLIndividualOfClass(individualURI, GranularityClassName)) {
        int hashIndex = individualURI.indexOf('#');
        if (hashIndex == -1) granularityName = individualURI;
        else granularityName = individualURI.substring(hashIndex + 1, individualURI.length());
        granularity = Temporal.getIntegerGranularityRepresentation(granularityName);
      } else throw new InvalidBuiltInArgumentException(argumentNumber, "individual " + individualURI + " is not a " + GranularityClassName);
    } else throw new InvalidBuiltInArgumentException(argumentNumber, "expecting a " + GranularityClassName + " individual" +
                                                     ", got " + arguments.get(argumentNumber));

    return granularity;
  } 

  private boolean isBuiltInArgumentAGranularity(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
  {
    String granularityName;
    boolean result = false;

    if (isArgumentADataValue(argumentNumber, arguments)) {
      granularityName = getArgumentAsAString(argumentNumber, arguments);
      result = Temporal.isValidGranularityString(granularityName);
    } else if (isArgumentAnIndividual(argumentNumber, arguments)) {
      String individualURI = getArgumentAsAnIndividualURI(argumentNumber, arguments);
      result = getBuiltInBridge().isOWLIndividualOfClass(individualURI, GranularityClassName);
    } // if

    return result;
  } 

  private Instant convertValidInstantIndividual2Instant(String individualURI, int granularity) 
    throws BuiltInException, TemporalException
  {
    String datetimeString = getDataPropertyValueAsAString(getBuiltInBridge(), individualURI, HasTimePropertyName);

    return new Instant(temporal, datetimeString, granularity);
  } 

  private Period convertValidPeriodIndividual2Period(String individualURI, int granularity) 
    throws BuiltInException, TemporalException
 {
  	String startDatetimeString = getDataPropertyValueAsAString(getBuiltInBridge(), individualURI, HasStartTimePropertyName);
  	String finishDatetimeString = getDataPropertyValueAsAString(getBuiltInBridge(), individualURI, HasFinishTimePropertyName);

  	return new Period(temporal, startDatetimeString, finishDatetimeString, granularity);
 }

  private Period convertExtendedPropositionIndividual2Period(String extendedPropositionURI, int granularity) 
  	throws BuiltInException, TemporalException
  {
		String validTimeURI = getObjectPropertyValueAsURI(getBuiltInBridge(), extendedPropositionURI, HasValidTimePropertyName);
		Period result = null;
	
		if (getBuiltInBridge().isOWLIndividualOfClass(validTimeURI, ValidPeriodClassName)) 
	    result = convertValidPeriodIndividual2Period(validTimeURI, granularity);
	  else throw new BuiltInException("expecting valid period value for extended proposition " + extendedPropositionURI);
	    
		return result;
  }

  private Instant convertExtendedPropositionIndividual2Instant(String extendedPropositionURI, int granularity) 
  	throws BuiltInException, TemporalException
	{
		String validTimeURI = getObjectPropertyValueAsURI(getBuiltInBridge(), extendedPropositionURI, HasValidTimePropertyName);
		Instant result = null;
	
		if (getBuiltInBridge().isOWLIndividualOfClass(validTimeURI, ValidInstantClassName)) 
	    result = convertValidInstantIndividual2Instant(validTimeURI, granularity);
	  else throw new BuiltInException("expecting valid instant value for extended proposition " + extendedPropositionURI);
	    
		return result;
	}

  private String getObjectPropertyValueAsURI(SWRLBuiltInBridge bridge, String individualURI, String propertyURI)
    throws BuiltInException
  {
   Set<OWLPropertyAssertionAxiom> axioms = bridge.getOWLPropertyAssertionAxioms(individualURI, propertyURI);
   OWLPropertyAssertionAxiom axiom;
   OWLNamedIndividual subject;

   axiom = axioms.toArray(new OWLPropertyAssertionAxiom[0])[0]; // Pick the first one

   if (!(axiom instanceof OWLObjectPropertyAssertionAxiom))
     throw new BuiltInException("property " + propertyURI + " does not refer to an OWL object property assertion axiom");

   subject = ((OWLObjectPropertyAssertionAxiom)axiom).getObject();

   return subject.getURI();
 } 

  private String getDataPropertyValueAsAString(SWRLBuiltInBridge bridge, String individualURI, String propertyURI)
  	throws BuiltInException
  {
  	Set<OWLPropertyAssertionAxiom> axioms = bridge.getOWLPropertyAssertionAxioms(individualURI, propertyURI);
	   OWLPropertyAssertionAxiom axiom;
	   OWLLiteral value;
	
	   axiom = axioms.toArray(new OWLPropertyAssertionAxiom[0])[0]; // Pick the first one
	
	   if (!(axiom instanceof OWLDataPropertyAssertionAxiom))
	     throw new BuiltInException("property " + propertyURI + " does not refer to an OWL datavalued property assertion axiom");
	
	   value = ((OWLDataPropertyAssertionAxiom)axiom).getObject();
	
	   return value.toString();
	 } 

  @SuppressWarnings("unused")
  private int getDataPropertyValueAsAnInteger(SWRLBuiltInBridge bridge, String individualURI, String propertyURI)
    throws BuiltInException
  {
    Set<OWLPropertyAssertionAxiom> axioms = bridge.getOWLPropertyAssertionAxioms(individualURI, propertyURI);
    OWLPropertyAssertionAxiom axiom;
    OWLLiteral literal;
    OWLDataValue dataValue;

    axiom = axioms.toArray(new OWLPropertyAssertionAxiom[0])[0];

    if (!(axiom instanceof OWLDataPropertyAssertionAxiom))
      throw new BuiltInException("property " + propertyURI + " does not refer to an OWL datavalued property assertion axiom");

    literal = ((OWLDataPropertyAssertionAxiom)axiom).getObject();

    dataValue = getBuiltInBridge().getOWLDataValueFactory().getOWLDataValue(literal);
    
    return dataValue.getInt();
  }   
} 
