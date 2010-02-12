
package edu.stanford.smi.protegex.owl.swrl.bridge.builtins;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLClass;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLDataValue;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLIndividual;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLProperty;
import edu.stanford.smi.protegex.owl.swrl.bridge.SWRLBuiltInBridge;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.BuiltInException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.BuiltInLibraryException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.InvalidBuiltInArgumentNumberException;

/** 
 * A class that defines methods that must be implemented by a built-in library. See <a
 * href="http://protege.cim3.net/cgi-bin/wiki.pl?SWRLBuiltInBridge">here</a> for documentation.
 *
 * The class AbstractSWRLBuiltInLibrary provides an implementation of this interface. 
 */
public interface SWRLBuiltInLibrary 
{
  String getLibraryName();
  void reset() throws BuiltInException;
  
  SWRLBuiltInBridge getInvokingBridge() throws BuiltInLibraryException;
  String getInvokingRuleName() throws BuiltInLibraryException;
  int getInvokingBuiltInIndex() throws BuiltInLibraryException;
  boolean getIsInConsequent() throws BuiltInLibraryException;
    
  void invokeResetMethod(SWRLBuiltInBridge bridge) throws BuiltInLibraryException;

  boolean invokeBuiltInMethod(Method method, SWRLBuiltInBridge bridge, String ruleName, 
                              String prefix, String builtInMethodName, int builtInIndex, boolean isInConsequent,
                              List<BuiltInArgument> arguments) throws BuiltInException;

    void checkThatInConsequent() throws BuiltInException;
    void checkThatInAntecedent() throws BuiltInException;
    void checkNumberOfArgumentsEqualTo(int expecting, int actual) throws InvalidBuiltInArgumentNumberException; 
	void checkNumberOfArgumentsAtLeast(int expectingAtLeast, int actual) throws InvalidBuiltInArgumentNumberException; 
	void checkNumberOfArgumentsAtMost(int expectingAtMost, int actual) throws InvalidBuiltInArgumentNumberException; 
	void checkNumberOfArgumentsInRange(int expectingAtLeast, int expectingAtMost, int actual) throws InvalidBuiltInArgumentNumberException; 
	void checkThatAllArgumentsAreLiterals(List<BuiltInArgument> arguments) throws BuiltInException; 
	void checkThatAllArgumentsAreNumeric(List<BuiltInArgument> arguments) throws BuiltInException; 
	void checkThatAllArgumentsAreIntegers(List<BuiltInArgument> arguments) throws BuiltInException; 
	
	boolean areAllArgumentsShorts(List<BuiltInArgument> arguments)throws BuiltInException;
	boolean areAllArgumentsIntegers(List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean areAllArgumentsLongs(List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean areAllArgumentsFloats(List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean areAllArgumentsDoubles(List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean isArgumentConvertableToDouble(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean isArgumentConvertableToFloat(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean isArgumentConvertableToLong(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean isArgumentConvertableToInteger(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean isArgumentConvertableToShort(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean isShortMostPreciseArgument(List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean isIntegerMostPreciseArgument(List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean isLongMostPreciseArgument(List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean isFloatMostPreciseArgument(List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean areAllArgumentsBooleans(List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean areAllArgumentLiterals(List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean areAllArgumentsNumeric(List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean areAllArgumentsStrings(List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean areAllArgumentsOfAnOrderedType(List<BuiltInArgument> arguments) throws BuiltInException; 
	
	void checkThatAllArgumentsAreFloats(List<BuiltInArgument> arguments) throws BuiltInException; 
	void checkThatAllArgumentsAreStrings(List<BuiltInArgument> arguments) throws BuiltInException; 
	void checkThatAllArgumentsAreOfAnOrderedType(List<BuiltInArgument> arguments) throws BuiltInException; 
	void checkThatArgumentIsALiteral(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	void checkThatArgumentIsALiteral(BuiltInArgument argument) throws BuiltInException; 
	void checkThatArgumentIsNumeric(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	void checkThatArgumentIsOfAnOrderedType(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean isArgumentOfAnOrderedType(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean isArgumentAnIndividual(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean isArgumentADatatypeValue(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	void checkThatArgumentIsAnIndividual(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	void checkThatArgumentIsAnOWLDatatypeValue(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException;  
	String getArgumentAsAnIndividualURI(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	OWLIndividual getArgumentAsAnOWLIndividual(int argumentNumber,	List<BuiltInArgument> arguments) throws BuiltInException; 
	String getArgumentAsAClassURI(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	OWLClass getArgumentAsAnOWLClass(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	OWLProperty getArgumentAsAnOWLProperty(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	OWLDataValue getArgumentAsAnOWLDatatypeValue(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	String getArgumentAsAResourceURI(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	String getArgumentAsAPropertyURI(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	void checkArgumentNumber(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean isArgumentNumeric(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean isArgumentNonNumeric(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	void checkThatArgumentIsNonNumeric(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	void checkThatArgumentIsAnInteger(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean isArgumentAnInteger(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	int getArgumentAsAnInteger(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	int getArgumentAsAPositiveInteger(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean isArgumentAShort(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	short getArgumentAsAShort(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean isArgumentALiteral(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean isArgumentAProperty(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	void checkThatArgumentIsAProperty(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	void checkThatArgumentIsAClassPropertyOrIndividual(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean isArgumentAClassPropertyOrIndividual(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean isArgumentAClass(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	void checkThatArgumentIsAClass(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	OWLDataValue getArgumentAsALiteral(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	OWLDataValue getArgumentAsALiteral(BuiltInArgument argument) throws BuiltInException; 
	void checkThatArgumentIsALong(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean isArgumentALong(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	long getArgumentAsALong(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	long getArgumentAsAPositiveLong(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	void checkThatArgumentIsAFloat(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean isArgumentAFloat(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	float getArgumentAsAFloat(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	float getArgumentAsAFloat(BuiltInArgument argument) throws BuiltInException; 
	void checkThatArgumentIsADouble(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean isArgumentADouble(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	double getArgumentAsADouble(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	double getArgumentAsADouble(BuiltInArgument argument) throws BuiltInException; 
	void checkThatArgumentIsABoolean(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean isArgumentABoolean(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean getArgumentAsABoolean(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	void checkThatArgumentIsAString(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean isArgumentAString(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	String getArgumentAsAString(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean hasUnboundArguments(List<BuiltInArgument> arguments) throws BuiltInException; 
	void checkThatAllArgumentsAreBound(List<BuiltInArgument> arguments) throws BuiltInException; 
	void checkThatArgumentIsBound(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean isUnboundArgument(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 

	/**
	 ** Get 0-offset position of first unbound argument; return -1 if no unbound arguments are found.
	 */
	int getFirstUnboundArgument(List<BuiltInArgument> arguments) throws BuiltInException; 
	void checkForUnboundArguments(List<BuiltInArgument> arguments) throws BuiltInException; 
	void checkForUnboundArguments(List<BuiltInArgument> arguments, String message) throws BuiltInException; 
	void checkForNonVariableArguments(List<BuiltInArgument> arguments, String message) throws BuiltInException; 
	void checkForUnboundNonFirstArguments(List<BuiltInArgument> arguments) throws BuiltInException; 
	String getVariableName(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 

	/**
	 ** Take an bound Argument object with types ClassArgument, PropertyArgument, IndividualArgument, or DatatypeValueArgument and return it as a
	 ** property value representation. Class, property and individual argument are represented by strings containing their class, property or
	 ** individual names, respectively; literal objects are represented by the appropriate Java type. Primitive XSD datatypes that do not have
	 ** a corresponding Java type are not yet supported.
	 */
	Object getArgumentAsAPropertyValue(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 

	/**
	 ** Create a string that represents a unique invocation pattern for a built-in for a bridge/rule/built-in/arguments combination.  
	 */
	String createInvocationPattern(SWRLBuiltInBridge invokingBridge, String invokingRuleName, int invokingBuiltInIndex,
                                   boolean isInConsequent, List<BuiltInArgument> arguments) throws BuiltInException; 

	void checkForUnboundArguments(String ruleName, String builtInName, List<BuiltInArgument> arguments) throws BuiltInException; 

	List<BuiltInArgument> copyArguments(List<BuiltInArgument> arguments) throws BuiltInException; 

	boolean processResultArgument(List<BuiltInArgument> arguments, int argumentNumber,
                                  Collection<BuiltInArgument> resultArguments) throws BuiltInException; 
	boolean processResultArgument(List<BuiltInArgument> arguments, int argumentNumber,
                                  BuiltInArgument resultArgument) throws BuiltInException; 
	boolean processResultArgument(List<BuiltInArgument> arguments, int argumentNumber,
                                  OWLDataValue resultArgument) throws BuiltInException; 
	boolean processResultArgument(List<BuiltInArgument> arguments, int argumentNumber,
                                  short resultArgument) throws BuiltInException; 
	boolean processResultArgument(List<BuiltInArgument> arguments, int argumentNumber, 
                                  int resultArgument) throws BuiltInException; 
	boolean processResultArgument(List<BuiltInArgument> arguments, int argumentNumber, 
                                  long resultArgument) throws BuiltInException; 
	boolean processResultArgument(List<BuiltInArgument> arguments, int argumentNumber, 
                                  float resultArgument) throws BuiltInException; 
	boolean processResultArgument(List<BuiltInArgument> arguments, int argumentNumber, 
                                  double resultArgument) throws BuiltInException; 
	boolean processResultArgument(List<BuiltInArgument> arguments, int argumentNumber, 
                                  byte resultArgument) throws BuiltInException; 
	boolean processResultArgument(List<BuiltInArgument> arguments, int argumentNumber, 
                                  String resultArgument) throws BuiltInException;
	
	String getOWLDatatypePropertyValueAsAString(SWRLBuiltInBridge bridge, String individualName, String propertyName) 
        throws BuiltInException; 
	int getOWLDatatypePropertyValueAsAnInteger(SWRLBuiltInBridge bridge, String individualName, String propertyName) 
        throws BuiltInException; 

} // SWRLBuiltInLibrary
