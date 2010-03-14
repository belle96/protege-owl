
package edu.stanford.smi.protegex.owl.swrl.bridge.builtins;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.ClassArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.DataPropertyArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.DataValueArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.IndividualArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.MultiArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.ObjectPropertyArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.SWRLBuiltInBridge;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.BuiltInException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.BuiltInLibraryException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.InvalidBuiltInArgumentNumberException;
import edu.stanford.smi.protegex.owl.swrl.bridge.xsd.XSDType;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.DataValue;

/** 
 * A class that defines methods that must be implemented by a built-in library. See <a
 * href="http://protege.cim3.net/cgi-bin/wiki.pl?SWRLBuiltInBridge">here</a> for documentation.<p>
 * 
 * Also includes an array of methods for processing built-in arguments.<p>
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
    
  void invokeResetMethod(SWRLBuiltInBridge bridge) throws BuiltInLibraryException;

  boolean invokeBuiltInMethod(Method method, SWRLBuiltInBridge bridge, String ruleName, 
                              String prefix, String builtInMethodName, int builtInIndex, boolean isInConsequent,
                              List<BuiltInArgument> arguments) throws BuiltInException;

  // Antecedent or consequent handling
  void checkThatInConsequent() throws BuiltInException;
  void checkThatInAntecedent() throws BuiltInException;
  boolean getIsInConsequent() throws BuiltInLibraryException;

  // Variable name handling
	String getVariableName(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
  
  // Unbound argument handling
  boolean hasUnboundArguments(List<BuiltInArgument> arguments) throws BuiltInException; 
	void checkThatAllArgumentsAreBound(List<BuiltInArgument> arguments) throws BuiltInException; 
	void checkThatArgumentIsBound(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean isUnboundArgument(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	void checkForUnboundArguments(String ruleName, String builtInName, List<BuiltInArgument> arguments) throws BuiltInException;
	
	/**
	 * Get 0-offset position of first unbound argument; return -1 if no unbound arguments are found.
	 */
	int getFirstUnboundArgument(List<BuiltInArgument> arguments) throws BuiltInException; 
	void checkForUnboundArguments(List<BuiltInArgument> arguments) throws BuiltInException; 
	void checkForUnboundArguments(List<BuiltInArgument> arguments, String message) throws BuiltInException; 
	void checkForNonVariableArguments(List<BuiltInArgument> arguments, String message) throws BuiltInException; 
	void checkForUnboundNonFirstArguments(List<BuiltInArgument> arguments) throws BuiltInException; 

	// Argument counting
  void checkNumberOfArgumentsEqualTo(int expecting, int actual) throws InvalidBuiltInArgumentNumberException; 
	void checkNumberOfArgumentsAtLeast(int expectingAtLeast, int actual) throws InvalidBuiltInArgumentNumberException; 
	void checkNumberOfArgumentsAtMost(int expectingAtMost, int actual) throws InvalidBuiltInArgumentNumberException; 
	void checkNumberOfArgumentsInRange(int expectingAtLeast, int expectingAtMost, int actual) throws InvalidBuiltInArgumentNumberException;
	void checkArgumentNumber(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	
	// Named argument handling
	String getArgumentAsAURI(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException;
	void checkThatArgumentIsAClassPropertyOrIndividual(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean isArgumentAClassPropertyOrIndividual(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	
	// Class argument handling
	String getArgumentAsAClassURI(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	ClassArgument getArgumentAsAClass(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean isArgumentAClass(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	void checkThatArgumentIsAClass(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	
	// Individual argument handling
	boolean isArgumentAnIndividual(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException;  
	void checkThatArgumentIsAnIndividual(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException;
	String getArgumentAsAnIndividualURI(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
  IndividualArgument getArgumentAsAnIndividual(int argumentNumber,	List<BuiltInArgument> arguments) throws BuiltInException; 
	
  // Property argument handling
  boolean isArgumentAProperty(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException;
  boolean isArgumentAnObjectProperty(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException;
  boolean isArgumentADataProperty(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException;
  String getArgumentAsAPropertyURI(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException;
  ObjectPropertyArgument getArgumentAsAnObjectProperty(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException;
	DataPropertyArgument getArgumentAsADataProperty(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	void checkThatArgumentIsAProperty(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	void checkThatArgumentIsAnObjectProperty(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
  void checkThatArgumentIsADataProperty(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
		
	// Data value argument handling
  void checkThatAllArgumentsAreDataValues(List<BuiltInArgument> arguments) throws BuiltInException;
	boolean areAllArgumentDataValues(List<BuiltInArgument> arguments) throws BuiltInException; 
	void checkThatArgumentIsADataValue(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException;  
	boolean isArgumentADataValue(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException;
	
	DataValue getArgumentAsADataValue(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	DataValue getArgumentAsADataValue(BuiltInArgument argument) throws BuiltInException; 

  // Primitive type argument handling

	// Boolean
	boolean areAllArgumentsBooleans(List<BuiltInArgument> arguments) throws BuiltInException;  
	void checkThatArgumentIsABoolean(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean isArgumentABoolean(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean getArgumentAsABoolean(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	
	// Strings
	void checkThatAllArgumentsAreStrings(List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean isArgumentAString(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	String getArgumentAsAString(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean areAllArgumentsStrings(List<BuiltInArgument> arguments) throws BuiltInException; 
	void checkThatArgumentIsAString(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	
	// Ordered typed
	void checkThatArgumentIsOfAnOrderedType(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean isArgumentOfAnOrderedType(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException;
	boolean areAllArgumentsOfAnOrderedType(List<BuiltInArgument> arguments) throws BuiltInException;
	
	// Numeric
	boolean isArgumentNumeric(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean isArgumentNonNumeric(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	void checkThatAllArgumentsAreOfAnOrderedType(List<BuiltInArgument> arguments) throws BuiltInException; 
	void checkThatArgumentIsNumeric(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	void checkThatAllArgumentsAreNumeric(List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean areAllArgumentsNumeric(List<BuiltInArgument> arguments) throws BuiltInException; 
	void checkThatArgumentIsNonNumeric(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	
	// Shorts
	boolean areAllArgumentsShorts(List<BuiltInArgument> arguments)throws BuiltInException;
	boolean isShortMostPreciseArgument(List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean isArgumentConvertableToShort(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean isArgumentAShort(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	short getArgumentAsAShort(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	void checkThatArgumentIsALong(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	
	// Integers
	boolean isIntegerMostPreciseArgument(List<BuiltInArgument> arguments) throws BuiltInException; 
	void checkThatAllArgumentsAreIntegers(List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean areAllArgumentsIntegers(List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean isArgumentConvertableToInteger(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	void checkThatArgumentIsAnInteger(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean isArgumentAnInteger(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	int getArgumentAsAnInteger(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	int getArgumentAsAPositiveInteger(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	
	// Longs
	boolean isLongMostPreciseArgument(List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean areAllArgumentsLongs(List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean isArgumentConvertableToLong(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean isArgumentALong(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	long getArgumentAsALong(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	long getArgumentAsAPositiveLong(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	
	// Floats
	boolean isFloatMostPreciseArgument(List<BuiltInArgument> arguments) throws BuiltInException; 
	void checkThatAllArgumentsAreFloats(List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean areAllArgumentsFloats(List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean isArgumentConvertableToFloat(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	void checkThatArgumentIsAFloat(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean isArgumentAFloat(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	float getArgumentAsAFloat(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	float getArgumentAsAFloat(BuiltInArgument argument) throws BuiltInException; 
	
	// Doubles
	boolean areAllArgumentsDoubles(List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean isArgumentConvertableToDouble(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	void checkThatArgumentIsADouble(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	boolean isArgumentADouble(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	double getArgumentAsADouble(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException; 
	double getArgumentAsADouble(BuiltInArgument argument) throws BuiltInException; 
	
	/**
	 * Create a string that represents a unique invocation pattern for a built-in for a bridge/rule/built-in/arguments combination.  
	 */
	String createInvocationPattern(SWRLBuiltInBridge invokingBridge, String invokingRuleName, int invokingBuiltInIndex,
                                   boolean isInConsequent, List<BuiltInArgument> arguments) throws BuiltInException; 

	List<BuiltInArgument> copyArguments(List<BuiltInArgument> arguments) throws BuiltInException; 
	Object getArgumentAsAPropertyValue(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException;
	
	// Result argument handling
	boolean processResultArgument(List<BuiltInArgument> arguments, int resultArgumentNumber,
                                Collection<BuiltInArgument> resultArguments) throws BuiltInException; 
	boolean processResultArgument(List<BuiltInArgument> arguments, int resultArgumentNumber,
                                BuiltInArgument resultArgument) throws BuiltInException; 
	boolean processResultArgument(List<BuiltInArgument> arguments, int resultArgumentNumber,
                                DataValueArgument resultArgument) throws BuiltInException; 
	boolean processResultArgument(List<BuiltInArgument> arguments, int resultArgumentNumber,
                                short resultArgument) throws BuiltInException; 
	boolean processResultArgument(List<BuiltInArgument> arguments, int resultArgumentNumber, 
                                int resultArgument) throws BuiltInException; 
	boolean processResultArgument(List<BuiltInArgument> arguments, int resultArgumentNumber, 
                                long resultArgument) throws BuiltInException; 
	boolean processResultArgument(List<BuiltInArgument> arguments, int resultArgumentNumber, 
                                float resultArgument) throws BuiltInException; 
	boolean processResultArgument(List<BuiltInArgument> arguments, int resultArgumentNumber, 
                                double resultArgument) throws BuiltInException; 
	boolean processResultArgument(List<BuiltInArgument> arguments, int resultArgumentNumber, 
                                byte resultArgument) throws BuiltInException; 
	boolean processResultArgument(List<BuiltInArgument> arguments, int resultArgumentNumber, 
                                String resultArgument) throws BuiltInException;
	
	// Argument creation handling
  ClassArgument createClassArgument(String classURI);
  IndividualArgument createIndividualArgument(String individualURI);
  ObjectPropertyArgument createObjectPropertyArgument(String propertyURI);
  DataPropertyArgument createDataPropertyArgument(String propertyURI);

  DataValueArgument createDataValueArgument(String s);
  DataValueArgument createDataValueArgument(boolean b);
  DataValueArgument createDataValueArgument(int i);
  DataValueArgument createDataValueArgument(long l);
  DataValueArgument createDataValueArgument(float f);
  DataValueArgument createDataValueArgument(double d);
  DataValueArgument createDataValueArgument(short s); 
  DataValueArgument createDataValueArgument(Byte b);
  DataValueArgument createDataValueArgument(XSDType xsd);

  MultiArgument createMultiArgument();
  MultiArgument createMultiArgument(List<BuiltInArgument> arguments);
}
