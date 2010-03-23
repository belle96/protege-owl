
package edu.stanford.smi.protegex.owl.swrl.bridge.builtins;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.stanford.smi.protegex.owl.swrl.bridge.ArgumentFactory;
import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.ClassArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.CollectionArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.DataPropertyArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.DataValueArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.IndividualArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.MultiArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.ObjectPropertyArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.PropertyArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.SWRLBuiltInBridge;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.BuiltInException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.BuiltInLibraryException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.BuiltInMethodRuntimeException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.InvalidBuiltInArgumentException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.InvalidBuiltInArgumentNumberException;
import edu.stanford.smi.protegex.owl.swrl.bridge.xsd.XSDType;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.DataValue;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.DataValueConversionException;

/** 
 * A class that must be subclassed by a class implementing a library of SWRL built-in methods. See <a
 * href="http://protege.cim3.net/cgi-bin/wiki.pl?SWRLBuiltInBridge">here</a> for documentation.
 */
public abstract class AbstractSWRLBuiltInLibrary implements SWRLBuiltInLibrary
{
  private String libraryName;

  // Bridge, rule name, built-in index, and head or body location within rule of built-in currently invoking its associated Java
  // implementation. The invokingRuleName, invokingBuiltInIndex, and isInConsequent variables are valid only when a built-in currently being
  // invoked so should only be retrieved through their associated accessor methods from within a built-in; the invokingBridge method is valid
  // only in built-ins and in the reset method.
  private SWRLBuiltInBridge invokingBridge;
  private String invokingRuleName = "";
  private int invokingBuiltInIndex = -1;
  private boolean isInConsequent = false;
  private ArgumentFactory argumentFactory;
  private Long invocationPatternID;
  private Map<String, Long> invocationPatternMap;

  public AbstractSWRLBuiltInLibrary(String libraryName) 
  { 
  	 this.libraryName = libraryName;
     argumentFactory = ArgumentFactory.getFactory();
     invocationPatternID = 0L;
     invocationPatternMap = new HashMap<String, Long>();
  } // AbstractSWRLBuiltInLibrary

  public String getLibraryName() { return libraryName; }

  public SWRLBuiltInBridge getInvokingBridge() throws BuiltInLibraryException
  {
    if (invokingBridge == null) 
      throw new BuiltInLibraryException("invalid call to getInvokingBridge - should only be called from within a built-in");

    return invokingBridge;
  } // getInvokingBridge

  public String getInvokingRuleName() throws BuiltInLibraryException
  {
    if (invokingRuleName.equals("")) 
      throw new BuiltInLibraryException("invalid call to getInvokingRuleName - should only be called from within a built-in");

    return invokingRuleName;
  } // getInvokingRuleName

  public int getInvokingBuiltInIndex() throws BuiltInLibraryException
  {
    if (invokingBuiltInIndex == -1) 
      throw new BuiltInLibraryException("invalid call to getInvokingBuiltInIndex - should only be called from within a built-in");

    return invokingBuiltInIndex;
  } // getInvokingBuiltInIndex

  public boolean getIsInConsequent() throws BuiltInLibraryException
  {
    if (invokingBridge == null) 
      throw new BuiltInLibraryException("invalid call to getIsInConsequent - should only be called from within a built-in");

    return isInConsequent;
  } // getIsInConsequent

  public void checkThatInConsequent() throws BuiltInException
  {
    if (invokingBridge == null) 
      throw new BuiltInLibraryException("invalid call to checkThatInConsequent - should only be called from within a built-in");

    if (!isInConsequent) throw new BuiltInException("built-in can only be used in consequent");
  } // checkIfInConsequent

  public void checkThatInAntecedent() throws BuiltInException
  {
    if (invokingBridge == null) 
      throw new BuiltInLibraryException("invalid call to checkThatInAntecedent - should only be called from within a built-in");

    if (isInConsequent) throw new BuiltInException("built-in can only be used in antecedent");
  } // checkIfInAntecedent

  public abstract void reset() throws BuiltInLibraryException;

  public void invokeResetMethod(SWRLBuiltInBridge bridge) throws BuiltInLibraryException
  {
    synchronized (this) {
      invokingBridge = bridge;

      reset();
      
      invocationPatternID = 0L;
      invocationPatternMap = new HashMap<String, Long>();
      
      invokingBridge = null;
    } // synchronized
  } // invokeResetMethod

  public boolean invokeBuiltInMethod(Method method, SWRLBuiltInBridge bridge, String ruleName, 
                                     String prefix, String builtInMethodName, int builtInIndex, boolean isInConsequent,
                                     List<BuiltInArgument> arguments) 
    throws BuiltInException
  {
    Boolean result = null;
    String builtInName = prefix + ":" + builtInMethodName;

    synchronized(this) { // Only one built-in per library may be invoked simultaneously.
      invokingBridge = bridge; invokingRuleName = ruleName; invokingBuiltInIndex = builtInIndex; this.isInConsequent = isInConsequent;
      
      try { // Invoke the built-in method.
        result = (Boolean)method.invoke(this, new Object[] { arguments });
      } catch (InvocationTargetException e) { // The built-in implementation threw an exception.
        Throwable targetException = e.getTargetException();
        if (targetException instanceof BuiltInException) { // A BuiltInException was thrown by the built-in.
          throw new BuiltInException("exception thrown by built-in " + builtInName + " in rule " + ruleName + ": " 
                                     + targetException.getMessage(), targetException);
        } else if (targetException instanceof RuntimeException) { // A runtime exception was thrown by the built-in.
          throw new BuiltInMethodRuntimeException(ruleName, builtInName, targetException.getMessage(), targetException);
        } else throw new BuiltInException("unknown exception thrown by built-in " + builtInName + " in rule " + 
                                          ruleName + ": " + e.toString(), e);
      } catch (Throwable e) { // Should be one of IllegalAccessException or IllegalArgumentException
        throw new BuiltInLibraryException("internal built-in library exception when invoking built-in " + builtInName + " in rule " + 
                                          ruleName + ": " + e.getMessage(), e);        
      } // try
      
      invokingBridge = null; invokingRuleName = ""; invokingBuiltInIndex = -1; this.isInConsequent = false;
    } // synchronized

    return result.booleanValue();
  } // invokeBuiltInMethod

  public void checkNumberOfArgumentsEqualTo(int expecting, int actual) 
  throws InvalidBuiltInArgumentNumberException
	{
	  if (expecting != actual) throw new InvalidBuiltInArgumentNumberException(expecting, actual);
	} // checkNumberOfArgumentsEqualTo
	
	public void checkNumberOfArgumentsAtLeast(int expectingAtLeast, int actual) 
	  throws InvalidBuiltInArgumentNumberException
	{
	  if (actual < expectingAtLeast) throw new InvalidBuiltInArgumentNumberException(expectingAtLeast, actual, "at least");
	} // checkNumberOfArgumentsAtLeast
	
	public void checkNumberOfArgumentsAtMost(int expectingAtMost, int actual) 
	  throws InvalidBuiltInArgumentNumberException
	{
	  if (actual > expectingAtMost) throw new InvalidBuiltInArgumentNumberException(expectingAtMost, actual, "at most");
	} // checkNumberOfArgumentsAtMost
	
	public void checkNumberOfArgumentsInRange(int expectingAtLeast, int expectingAtMost, int actual) 
	  throws InvalidBuiltInArgumentNumberException
	{
	  if (actual > expectingAtMost || actual < expectingAtLeast)
	    throw new InvalidBuiltInArgumentNumberException(expectingAtMost, actual, expectingAtLeast + " to");
	} // checkNumberOfArgumentsInRange
	
	public void checkThatAllArgumentsAreDataValues(List<BuiltInArgument> arguments) throws BuiltInException
	{
	  for (int argumentNumber = 0; argumentNumber < arguments.size(); argumentNumber++) 
	    checkThatArgumentIsADataValue(argumentNumber, arguments);
	} // checkThatAllArgumentsAreDataValues
	
	public void checkThatAllArgumentsAreNumeric(List<BuiltInArgument> arguments) throws BuiltInException
	{
	  for (int argumentNumber = 0; argumentNumber < arguments.size(); argumentNumber++) 
	    checkThatArgumentIsNumeric(argumentNumber, arguments);
	} // checkThatAllArgumentsAreNumeric
	
	public void checkThatAllArgumentsAreIntegers(List<BuiltInArgument> arguments) throws BuiltInException
	{
	  for (int argumentNumber = 0; argumentNumber < arguments.size(); argumentNumber++) 
	    checkThatArgumentIsAnInteger(argumentNumber, arguments);
	} // checkThatAllArgumentsAreIntegers
	
	public boolean areAllArgumentsShorts(List<BuiltInArgument> arguments) throws BuiltInException
	{
	  for (int argumentNumber = 0; argumentNumber < arguments.size(); argumentNumber++) 
	    if (!isArgumentAShort(argumentNumber, arguments)) return false;
	  return true;
	} // areAllArgumentsShorts
	
	public boolean areAllArgumentsIntegers(List<BuiltInArgument> arguments) throws BuiltInException
	{
	  for (int argumentNumber = 0; argumentNumber < arguments.size(); argumentNumber++) 
	    if (!isArgumentAnInteger(argumentNumber, arguments)) return false;
	  return true;
	} // areAllArgumentsIntegers
	
	public boolean areAllArgumentsLongs(List<BuiltInArgument> arguments) throws BuiltInException
	{
	  for (int argumentNumber = 0; argumentNumber < arguments.size(); argumentNumber++) 
	    if (!isArgumentALong(argumentNumber, arguments)) return false;
	  return true;
	} // areAllArgumentsLongs
	
	public boolean areAllArgumentsFloats(List<BuiltInArgument> arguments) throws BuiltInException
	{
	  for (int argumentNumber = 0; argumentNumber < arguments.size(); argumentNumber++) 
	    if (!isArgumentAFloat(argumentNumber, arguments)) return false;
	  return true;
	} // areAllArgumentsFloats
	
	public boolean areAllArgumentsDoubles(List<BuiltInArgument> arguments) throws BuiltInException
	{
	  for (int argumentNumber = 0; argumentNumber < arguments.size(); argumentNumber++) 
	    if (!isArgumentADouble(argumentNumber, arguments)) return false;
	  return true;
	} // areAllArgumentsDoubles
	
	public boolean isArgumentConvertableToDouble(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  return (isArgumentNumeric(argumentNumber, arguments));
	} // isArgumentConvertableToDouble
	
	public boolean isArgumentConvertableToFloat(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  return (isArgumentNumeric(argumentNumber, arguments) && isArgumentAShort(argumentNumber, arguments) &&
	          isArgumentAnInteger(argumentNumber, arguments) && isArgumentALong(argumentNumber, arguments) &&
	          isArgumentAFloat(argumentNumber, arguments));
	} // isArgumentConvertableToFloat
	
	public boolean isArgumentConvertableToLong(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  return (isArgumentNumeric(argumentNumber, arguments) && isArgumentAShort(argumentNumber, arguments) &&
	          isArgumentAnInteger(argumentNumber, arguments) && isArgumentALong(argumentNumber, arguments));
	} // isArgumentConvertableToLong
	
	public boolean isArgumentConvertableToInteger(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  return (isArgumentNumeric(argumentNumber, arguments) && isArgumentAShort(argumentNumber, arguments) &&
	          isArgumentAnInteger(argumentNumber, arguments));
	} // isArgumentConvertableToInteger
	
	public boolean isArgumentConvertableToShort(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  return (isArgumentNumeric(argumentNumber, arguments) && isArgumentAShort(argumentNumber, arguments));
	} // isArgumentConvertableToShort
	
	public boolean isShortMostPreciseArgument(List<BuiltInArgument> arguments) throws BuiltInException
	{
	  for (int argumentNumber = 0; argumentNumber < arguments.size(); argumentNumber++) 
	    if (isArgumentAnInteger(argumentNumber, arguments) || isArgumentALong(argumentNumber, arguments) || 
	        isArgumentAFloat(argumentNumber, arguments) || isArgumentADouble(argumentNumber, arguments)) return false;
	  return true;
	} // isShortMostPreciseArgument
	
	public boolean isIntegerMostPreciseArgument(List<BuiltInArgument> arguments) throws BuiltInException
	{
	  for (int argumentNumber = 0; argumentNumber < arguments.size(); argumentNumber++) 
	    if (isArgumentALong(argumentNumber, arguments) || isArgumentAFloat(argumentNumber, arguments) ||
	        isArgumentADouble(argumentNumber, arguments)) return false;
	  return true;
	} // isIntegerMostPreciseArgument
	
	public boolean isLongMostPreciseArgument(List<BuiltInArgument> arguments) throws BuiltInException
	{
	  for (int argumentNumber = 0; argumentNumber < arguments.size(); argumentNumber++) 
	    if (isArgumentADouble(argumentNumber, arguments)|| isArgumentAFloat(argumentNumber, arguments)) return false;
	  return true;
	} // isLongMostPreciseArgument
	
	public boolean isFloatMostPreciseArgument(List<BuiltInArgument> arguments) throws BuiltInException
	{
	  for (int argumentNumber = 0; argumentNumber < arguments.size(); argumentNumber++) 
	    if (isArgumentADouble(argumentNumber, arguments)) return false;
	  return true;
	} // isFloatMostPreciseArgument
	
	public boolean areAllArgumentsBooleans(List<BuiltInArgument> arguments) throws BuiltInException
	{
	  for (int argumentNumber = 0; argumentNumber < arguments.size(); argumentNumber++) 
	    if (!isArgumentABoolean(argumentNumber, arguments)) return false;
	  return true;
	} // areAllArgumentsBooleans
	
	public boolean areAllArgumentDataValues(List<BuiltInArgument> arguments) throws BuiltInException
	{
	  for (int argumentNumber = 0; argumentNumber < arguments.size(); argumentNumber++) 
	    if (!isArgumentADataValue(argumentNumber, arguments)) return false;
	  return true;
	} // areAllArgumentsIntegers
	
	public boolean areAllArgumentsNumeric(List<BuiltInArgument> arguments) throws BuiltInException
	{
	  for (int argumentNumber = 0; argumentNumber < arguments.size(); argumentNumber++) 
	    if (!isArgumentNumeric(argumentNumber, arguments)) return false;
	  return true;
	} // areAllArgumentsNumeric
	
	public boolean areAllArgumentsStrings(List<BuiltInArgument> arguments) throws BuiltInException
	{
	  for (int argumentNumber = 0; argumentNumber < arguments.size(); argumentNumber++) 
	    if (!isArgumentAString(argumentNumber, arguments)) return false;
	  return true;
	} // areAllArgumentsStrings
	
	public boolean areAllArgumentsOfAnOrderedType(List<BuiltInArgument> arguments) throws BuiltInException
	{
	  for (int argumentNumber = 0; argumentNumber < arguments.size(); argumentNumber++) 
	    if (!isArgumentOfAnOrderedType(argumentNumber, arguments)) return false;
	  return true;
	} // areAllArgumentsOfAnOrderedType
	
	public void checkThatAllArgumentsAreFloats(List<BuiltInArgument> arguments) throws BuiltInException
	{
	  for (int argumentNumber = 0; argumentNumber < arguments.size(); argumentNumber++) 
	    checkThatArgumentIsAFloat(argumentNumber, arguments);
	} // checkThatAllArgumentsAreFloats
	
	public void checkThatAllArgumentsAreStrings(List<BuiltInArgument> arguments) throws BuiltInException
	{
	  for (int argumentNumber = 0; argumentNumber < arguments.size(); argumentNumber++) 
	    checkThatArgumentIsAString(argumentNumber, arguments);
	} // checkThatAllArgumentsAreStrings
	
	public void checkThatAllArgumentsAreOfAnOrderedType(List<BuiltInArgument> arguments) throws BuiltInException
	{
	  for (int argumentNumber = 0; argumentNumber < arguments.size(); argumentNumber++) 
	    checkThatArgumentIsOfAnOrderedType(argumentNumber, arguments);
	} // checkThatAllArgumentsAreOfAnOrderedType
	
	public void checkThatArgumentIsADataValue(BuiltInArgument argument) throws BuiltInException
	{
	
	  if (!(argument instanceof DataValueArgument)) throw new InvalidBuiltInArgumentException(makeInvalidArgumentTypeMessage(argument, "data value"));
	} // checkThatArgumentIsADataValue
	
	public void checkThatArgumentIsNumeric(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  if (!isArgumentNumeric(argumentNumber, arguments))
	    throw new InvalidBuiltInArgumentException(argumentNumber,
	                                              makeInvalidArgumentTypeMessage(arguments.get(argumentNumber), "numeric"));
	} // checkThatArgumentIsNumeric
	
	public void checkThatArgumentIsOfAnOrderedType(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  if (!isArgumentOfAnOrderedType(argumentNumber, arguments))
	    throw new InvalidBuiltInArgumentException(argumentNumber,
	                                              makeInvalidArgumentTypeMessage(arguments.get(argumentNumber), "ordered type"));
	} // checkThatArgumentIsOfAnOrderedType
	
	public boolean isArgumentOfAnOrderedType(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  return (isArgumentNumeric(argumentNumber, arguments) || isArgumentAString(argumentNumber, arguments));
	} // isArgumentOfAnOrderedType
	
	public boolean isArgumentAnIndividual(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  checkArgumentNumber(argumentNumber, arguments);
	
	  return arguments.get(argumentNumber) instanceof IndividualArgument;
	} // isArgumentAnIndividual
	
	public boolean isArgumentADatatypeValue(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  checkArgumentNumber(argumentNumber, arguments);
	
	  return arguments.get(argumentNumber) instanceof DataValueArgument;
	} // isArgumentADatatypeValue
	
	public void checkThatArgumentIsAnIndividual(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  if (!isArgumentAnIndividual(argumentNumber, arguments)) {
	    throw new InvalidBuiltInArgumentException(argumentNumber, 
	                                              makeInvalidArgumentTypeMessage(arguments.get(argumentNumber), "individual"));
	  } // if
	} // checkThatArgumentIsAnIndividual
	
	public void checkThatArgumentIsADataValue(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  if (!isArgumentADatatypeValue(argumentNumber, arguments)) {
	    throw new InvalidBuiltInArgumentException(argumentNumber, 
	                                              makeInvalidArgumentTypeMessage(arguments.get(argumentNumber), "data value"));
	  } // if
	} // checkThatArgumentIsADataValue
	
	public String getArgumentAsAnIndividualURI(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  checkThatArgumentIsAnIndividual(argumentNumber, arguments);
	
	  return ((IndividualArgument)arguments.get(argumentNumber)).getURI();
	} // getArgumentAsAnIndividualURI
	
	public IndividualArgument getArgumentAsAnIndividual(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  checkThatArgumentIsAnIndividual(argumentNumber, arguments);
	
	  return (IndividualArgument)arguments.get(argumentNumber);
	} // getArgumentAsAnIndividual
	
	public String getArgumentAsAClassURI(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  checkThatArgumentIsAClass(argumentNumber, arguments);
	
	  return ((ClassArgument)arguments.get(argumentNumber)).getURI();
	} // getArgumentAsAClassURI
	
	public ClassArgument getArgumentAsAClass(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  checkThatArgumentIsAClass(argumentNumber, arguments);
	
	  return (ClassArgument)arguments.get(argumentNumber);
	} // getArgumentAsAClass
	
	public PropertyArgument getArgumentAsAProperty(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  checkThatArgumentIsAProperty(argumentNumber, arguments);
	
	  return (PropertyArgument)arguments.get(argumentNumber);
	} // getArgumentAsAProperty
	
	public ObjectPropertyArgument getArgumentAsAnObjectProperty(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  checkThatArgumentIsAnObjectProperty(argumentNumber, arguments);
	
	  return (ObjectPropertyArgument)arguments.get(argumentNumber);
	} // getArgumentAsAProperty
	
	public DataPropertyArgument getArgumentAsADataProperty(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  checkThatArgumentIsADataProperty(argumentNumber, arguments);
	
	  return (DataPropertyArgument)arguments.get(argumentNumber);
	} // getArgumentAsAProperty
	
	public String getArgumentAsAURI(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  String uri = "";
	
	  checkThatArgumentIsAClassPropertyOrIndividual(argumentNumber, arguments);
	
	  if (isArgumentAClass(argumentNumber, arguments)) uri = ((ClassArgument)arguments.get(argumentNumber)).getURI();
	  else if (isArgumentAProperty(argumentNumber, arguments)) uri = ((PropertyArgument)arguments.get(argumentNumber)).getURI();
	  else if (isArgumentAnIndividual(argumentNumber, arguments)) uri = ((IndividualArgument)arguments.get(argumentNumber)).getURI();
	
	  return uri;
	} // getArgumentAsAResourceURI
	
	public String getArgumentAsAPropertyURI(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  checkThatArgumentIsAProperty(argumentNumber, arguments);
	
	  return ((PropertyArgument)arguments.get(argumentNumber)).getURI();
	} // getArgumentAsAPropertyURI
	
	public void checkArgumentNumber(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  if ((argumentNumber < 0) || (argumentNumber >= arguments.size()))
	    throw new BuiltInException("(0-offset) argument number #" + argumentNumber + " is out of bounds");
	} // checkArgumentNumber
	
	public boolean isArgumentNumeric(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  if (isArgumentADataValue(argumentNumber, arguments)) return getArgumentAsADataValue(argumentNumber, arguments).isNumeric();
	  else return false;
	} // isArgumentNumeric
	
	public boolean isArgumentNonNumeric(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  if (isArgumentADataValue(argumentNumber, arguments))
	    return !getArgumentAsADataValue(argumentNumber, arguments).isNumeric();
	  else return false;
	} // isArgumentNonNumeric
	
	public void checkThatArgumentIsNonNumeric(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  if (!isArgumentNonNumeric(argumentNumber, arguments))
	    throw new InvalidBuiltInArgumentException(argumentNumber, 
	                                              makeInvalidArgumentTypeMessage(arguments.get(argumentNumber), "non-numeric"));
	} // checkThatArgumentIsNonNumeric
	
	// Integers
	
	public void checkThatArgumentIsAnInteger(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  if (!isArgumentAnInteger(argumentNumber, arguments))
	    throw new InvalidBuiltInArgumentException(argumentNumber,
	                                              makeInvalidArgumentTypeMessage(arguments.get(argumentNumber), "integer"));
	} // checkThatArgumentIsAnInteger
	
	public boolean isArgumentAnInteger(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  if (isArgumentADataValue(argumentNumber, arguments)) return (getArgumentAsADataValue(argumentNumber, arguments).isInteger());
	  else return false;
	} // isArgumentAnInteger
	
	public int getArgumentAsAnInteger(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  return getArgumentAsADataValue(argumentNumber, arguments).getInt(); // Will throw DatatypeConversionException if invalid.
	} // getArgumentAsAnInteger
	
	public int getArgumentAsAPositiveInteger(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  int i = getArgumentAsADataValue(argumentNumber, arguments).getInt(); // Will throw DatatypeConversionException if invalid.
	
	  if (i < 0) throw new InvalidBuiltInArgumentException(argumentNumber, makeInvalidArgumentTypeMessage(arguments.get(argumentNumber),
	                                                                                                      "expecting positive integer"));
	  return i;
	} // getArgumentAsAPositiveInteger
	
	// Shorts
	
	public boolean isArgumentAShort(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  if (isArgumentADataValue(argumentNumber, arguments)) 
	    return (getArgumentAsADataValue(argumentNumber, arguments).isShort());
	  else return false;
	} // isArgumentAShort
	
	public short getArgumentAsAShort(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  return getArgumentAsADataValue(argumentNumber, arguments).getShort(); // Will throw DatatypeConversionException if invalid.
	} // getArgumentAsAShort
	
	// BigDecimal
	
	public boolean isArgumentADataValue(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  checkThatArgumentIsBound(argumentNumber, arguments);
	
	  return (arguments.get(argumentNumber) instanceof DataValueArgument);
	} // isArgumentADataValue
	
	public boolean isArgumentAProperty(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  checkThatArgumentIsBound(argumentNumber, arguments);
	
	  return (arguments.get(argumentNumber) instanceof PropertyArgument);
	} // isArgumentAProperty
	
	public boolean isArgumentADataProperty(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  checkThatArgumentIsBound(argumentNumber, arguments);
	
	  return (arguments.get(argumentNumber) instanceof DataPropertyArgument);
	} // isArgumentADataProperty
	
	public boolean isArgumentAnObjectProperty(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  checkThatArgumentIsBound(argumentNumber, arguments);
	
	  return (arguments.get(argumentNumber) instanceof ObjectPropertyArgument);
	} // isArgumentAnObjectProperty
	
	public void checkThatArgumentIsAProperty(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  if (!isArgumentAProperty(argumentNumber, arguments))
	    throw new InvalidBuiltInArgumentException(argumentNumber,
	                                              makeInvalidArgumentTypeMessage(arguments.get(argumentNumber), "property"));
	} // checkThatArgumentIsAProperty
	
	public void checkThatArgumentIsAnObjectProperty(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  if (!isArgumentAnObjectProperty(argumentNumber, arguments))
	    throw new InvalidBuiltInArgumentException(argumentNumber,
	                                              makeInvalidArgumentTypeMessage(arguments.get(argumentNumber), "object property"));
	} // checkThatArgumentIsAnObjectProperty
	
	public void checkThatArgumentIsADataProperty(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  if (!isArgumentADataProperty(argumentNumber, arguments))
	    throw new InvalidBuiltInArgumentException(argumentNumber,
	                                              makeInvalidArgumentTypeMessage(arguments.get(argumentNumber), "data property"));
	} // checkThatArgumentIsAProperty
	
	public void checkThatArgumentIsAClassPropertyOrIndividual(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  if (!isArgumentAClassPropertyOrIndividual(argumentNumber, arguments))
	    throw new InvalidBuiltInArgumentException(argumentNumber,
	                                              makeInvalidArgumentTypeMessage(arguments.get(argumentNumber), "class, property, or individual"));
	} // checkThatArgumentIsAClassPropertyOrIndividual
	
	public boolean isArgumentAClassPropertyOrIndividual(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  return isArgumentAClass(argumentNumber, arguments) || isArgumentAProperty(argumentNumber, arguments) ||
	         isArgumentAnIndividual(argumentNumber, arguments);
	} // isArgumentAClassPropertyOrIndividual
	
	public boolean isArgumentAClass(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  checkThatArgumentIsBound(argumentNumber, arguments);
	
	  return (arguments.get(argumentNumber) instanceof ClassArgument);
	} // isArgumentAClass
	
	public void checkThatArgumentIsAClass(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  if (!isArgumentAClass(argumentNumber, arguments))
	    throw new InvalidBuiltInArgumentException(argumentNumber,
	                                              makeInvalidArgumentTypeMessage(arguments.get(argumentNumber), "class"));
	} // checkThatArgumentIsAClass
	
	public DataValue getArgumentAsADataValue(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  checkThatArgumentIsADataValue(argumentNumber, arguments);
	
	  return ((DataValueArgument)arguments.get(argumentNumber)).getDataValue();
	} // getArgumentAsADataValue
	
	public DataValue getArgumentAsADataValue(BuiltInArgument argument) throws BuiltInException
	{
	  checkThatArgumentIsADataValue(argument);
	
	  return ((DataValueArgument)argument).getDataValue();
	} // getArgumentAsADataValue
	
	// Longs
	
	public void checkThatArgumentIsALong(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  if (!isArgumentALong(argumentNumber, arguments))
	    throw new InvalidBuiltInArgumentException(argumentNumber, 
	                                              makeInvalidArgumentTypeMessage(arguments.get(argumentNumber), "long"));
	} // checkThatArgumentIsALong
	
	public boolean isArgumentALong(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  if (isArgumentADataValue(argumentNumber, arguments)) 
	    return (getArgumentAsADataValue(argumentNumber, arguments).isLong());
	  else return false;
	} // isArgumentALong
	
	public long getArgumentAsALong(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  return getArgumentAsADataValue(argumentNumber, arguments).getLong(); // Will throw DatatypeConversionException if invalid.
	} // getArgumentAsALong
	
	public long getArgumentAsAPositiveLong(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  long l = getArgumentAsADataValue(argumentNumber, arguments).getLong(); // Will throw DatatypeConversionException if invalid.
	
	  if (l < 0) throw new InvalidBuiltInArgumentException(argumentNumber, makeInvalidArgumentTypeMessage(arguments.get(argumentNumber),
	                                                                                                      "expecting positive long"));
	
	  return l;                                                                                                       
	} // getArgumentAsAPositiveLong
	
	// Floats
	
	public void checkThatArgumentIsAFloat(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  if (!isArgumentAFloat(argumentNumber, arguments))
	    throw new InvalidBuiltInArgumentException(argumentNumber,
	                                              makeInvalidArgumentTypeMessage(arguments.get(argumentNumber), "float"));
	} // checkThatArgumentIsAFloat
	
	public boolean isArgumentAFloat(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  if (isArgumentADataValue(argumentNumber, arguments)) 
	    return (getArgumentAsADataValue(argumentNumber, arguments).isFloat());
	  else return false;
	} // isArgumentAFloat
	
	public float getArgumentAsAFloat(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  return getArgumentAsADataValue(argumentNumber, arguments).getFloat(); // Will throw DatatypeConversionException if invalid.
	} // getArgumentAsAFloat
	
	public float getArgumentAsAFloat(BuiltInArgument argument) throws BuiltInException
	{
	  return getArgumentAsADataValue(argument).getFloat(); // Will throw DatatypeConversionException if invalid.
	} // getArgumentAsAFloat
	
	// Double
	
	public void checkThatArgumentIsADouble(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  if (!isArgumentADouble(argumentNumber, arguments))
	    throw new InvalidBuiltInArgumentException(argumentNumber, 
	                                              makeInvalidArgumentTypeMessage(arguments.get(argumentNumber), "double"));
	} // checkThatArgumentIsADouble
	
	public boolean isArgumentADouble(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  if (isArgumentADataValue(argumentNumber, arguments)) 
	    return (getArgumentAsADataValue(argumentNumber, arguments).isDouble());
	  else return false;
	} // isArgumentADouble
	
	public double getArgumentAsADouble(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  checkArgumentNumber(argumentNumber, arguments);
	
	  return getArgumentAsADouble(arguments.get(argumentNumber));
	} // getArgumentAsADouble
	
	public double getArgumentAsADouble(BuiltInArgument argument) throws BuiltInException
	{
	  return getArgumentAsADataValue(argument).getDouble(); // Will throw DatatypeConversionException if invalid.
	} // getArgumentAsADouble
	
	// Booleans
	
	public void checkThatArgumentIsABoolean(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  if (!isArgumentABoolean(argumentNumber, arguments))
	    throw new InvalidBuiltInArgumentException(argumentNumber,
	                                              makeInvalidArgumentTypeMessage(arguments.get(argumentNumber), "boolean"));
	} // checkThatArgumentIsABoolean
	
	public boolean isArgumentABoolean(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  if (isArgumentADataValue(argumentNumber, arguments)) 
	    return (getArgumentAsADataValue(argumentNumber, arguments).isBoolean());
	  else return false;
	} // isArgumentABoolean
	
	public boolean getArgumentAsABoolean(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  checkThatArgumentIsABoolean(argumentNumber, arguments);
	
	  return getArgumentAsADataValue(argumentNumber, arguments).getBoolean();
	} // getArgumentAsABoolean
	
	// Strings
	
	public void checkThatArgumentIsAString(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  if (!isArgumentAString(argumentNumber, arguments))
	    throw new InvalidBuiltInArgumentException(argumentNumber, 
	                                              makeInvalidArgumentTypeMessage(arguments.get(argumentNumber), "string"));
	} // checkThatArgumentIsAString
	
	public boolean isArgumentAString(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  if (isArgumentADataValue(argumentNumber, arguments)) 
	  	return getArgumentAsADataValue(argumentNumber, arguments).isString();
	  else return false;
	} // isArgumentAString
	
	public String getArgumentAsAString(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	 checkThatArgumentIsAString(argumentNumber, arguments);
	
	  return getArgumentAsADataValue(argumentNumber, arguments).getString();
	} // getArgumentAsAString
	
	// Unbound argument processing methods.
	
	public boolean hasUnboundArguments(List<BuiltInArgument> arguments) throws BuiltInException
	{
	  for (BuiltInArgument argument : arguments) if (argument.isUnbound()) return true;
	
	  return false;
	} // hasUnboundArguments
	
	public void checkThatAllArgumentsAreBound(List<BuiltInArgument> arguments) throws BuiltInException
	{
	  if (hasUnboundArguments(arguments)) throw new BuiltInException("all arguments must be bound");
	} // checkThatAllArgumentsAreBound
	
	public void checkThatArgumentIsBound(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  if (isUnboundArgument(argumentNumber, arguments)) 
	    throw new BuiltInException("not expecting an unbound argument for (0-offset) argument #" + argumentNumber);
	} // checkThatArgumentIsBound
	
	public boolean isUnboundArgument(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  checkArgumentNumber(argumentNumber, arguments);
	
	  return arguments.get(argumentNumber).isUnbound();
	} // isUnboundArgument
	
	public boolean isBoundArgument(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  checkArgumentNumber(argumentNumber, arguments);
	
	  return arguments.get(argumentNumber).isBound();
	} // isBoundArgument
	
	/**
	 ** Get 0-offset position of first unbound argument; return -1 if no unbound arguments are found.
	 */
	public int getFirstUnboundArgument(List<BuiltInArgument> arguments) throws BuiltInException
	{
	  for (int index = 0; index < arguments.size(); index++) if (arguments.get(index).isUnbound()) return index;
	
	  return -1;
	} // getFirstUnboundArgument
	
	public void checkForUnboundArguments(List<BuiltInArgument> arguments) throws BuiltInException
	{
	  checkForUnboundArguments(arguments, "built-in does not support variable binding - unbound argument '" +
	                           getFirstUnboundArgument(arguments) + "'");
	} // checkForUnboundArguments
	
	public void checkForUnboundArguments(List<BuiltInArgument> arguments, String message) throws BuiltInException
	{
	  if (hasUnboundArguments(arguments))
	    throw new BuiltInException(message + " (0-offset) argument #" + getFirstUnboundArgument(arguments));
	} // checkForUnboundArguments
	
	public void checkForNonVariableArguments(List<BuiltInArgument> arguments, String message) throws BuiltInException
	{
	  for (BuiltInArgument argument : arguments) if (!argument.isVariable()) throw new BuiltInException(message + " '" + argument + "'");
	} // checkForNonVariableArguments
	
	public void checkForUnboundNonFirstArguments(List<BuiltInArgument> arguments) throws BuiltInException
	{
	  if (hasUnboundArguments(arguments.subList(1, arguments.size())))
	    throw new BuiltInException("built-in supports variable binding only for the first argument - " +
	                               "unbound variables used as other arguments");
	} // checkForUnboundArguments
	
	public String getVariableName(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  checkArgumentNumber(argumentNumber, arguments);
	
	  return arguments.get(argumentNumber).getVariableName(); // Will throw an exception if it does not contain a variable name
	} // getVariableName
	
	private String makeInvalidArgumentTypeMessage(BuiltInArgument argument, String expectedTypeName) throws BuiltInException
	{
	  String message = "expecting " + expectedTypeName + ", got ";
	  
	  if (argument.isUnbound()) message += "unbound argument with variable name '" + argument.getVariableName() + "'";
	  else {
	    if (argument instanceof ClassArgument) {
	      ClassArgument classArgument = (ClassArgument)argument;
	      message += "class with URI " + classArgument.getURI();
	    } else if (argument instanceof PropertyArgument) {
	      PropertyArgument propertyArgument = (PropertyArgument)argument;
	      message += "property with URI " + propertyArgument.getURI();
	    } else if (argument instanceof IndividualArgument) {
	      IndividualArgument individualArgument = (IndividualArgument)argument;
	      message += "individual with URI " + individualArgument.getURI();
	    } else if (argument instanceof DataValueArgument) {
	      DataValueArgument dataValue = (DataValueArgument)argument;
	      message += "data value with value " + dataValue.toString();
	    } else message += "unknown type " + argument.getClass();
	  } // if
	  
	  return message;
	} // makeInvalidArgumentTypeMessage
	
	/**
	 * Take an bound Argument object with types ClassArgument, PropertyArgument, IndividualArgument, or DataValueArgument and return it as a
	 * property value representation. Class, property and individual argumenst are represented by  their URIs; data value objects are 
	 * represented by the appropriate Java type. Primitive XSD datatypes that do not have a corresponding Java type are not yet supported.
	 */
	public Object getArgumentAsAPropertyValue(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  BuiltInArgument argument;
	  Object result = null;
	
	  checkThatArgumentIsBound(argumentNumber, arguments);
	
	  argument = arguments.get(argumentNumber);
	
	  if (argument instanceof ClassArgument) {
	    ClassArgument classArgument = (ClassArgument)argument;
	    result = classArgument.getURI();
	  } else if (argument instanceof PropertyArgument) {
	    PropertyArgument propertyArgument = (PropertyArgument)argument;
	    result = propertyArgument.getURI();
	  } else if (argument instanceof IndividualArgument) {
	    IndividualArgument individualArgument = (IndividualArgument)argument;
	    result = individualArgument.getURI();
	  } else if (argument instanceof DataValueArgument) {
	    DataValue dataValue = getArgumentAsADataValue(argument);
	    if (dataValue.isNumeric()) result = dataValue.getNumber();
	    else if (dataValue.isString()) result = dataValue.getString();
	    else throw new BuiltInException("data value with value " + dataValue.toString() + " not supported - strings and number data values only");
	  } else throw new BuiltInException("argument " + argument + " of unknown type " + argument.getClass());
	
	  return result;
	}
	
	/**
	 * Create a string that represents a key of a unique invocation pattern for a built-in for a bridge/rule/built-in/arguments combination.  
	 */
	public String createInvocationPattern(SWRLBuiltInBridge invokingBridge, String invokingRuleName, int invokingBuiltInIndex,
	                                       boolean isInConsequent, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  String pattern = "" + invokingBridge.hashCode() + "." + invokingRuleName + "." + invokingBuiltInIndex + "." + isInConsequent;
	  String result;
	
	  for (int i = 0; i < arguments.size(); i++) pattern += "." + getArgumentAsAPropertyValue(i, arguments);
	  
	  if (invocationPatternMap.containsKey(pattern)) result = invocationPatternMap.get(pattern).toString();
	  else {
	  	invocationPatternMap.put(pattern, invocationPatternID);
	  	result = invocationPatternID.toString();
	  	invocationPatternID++;
	  }
	
	  return result;
	}
	
	public void checkForUnboundArguments(String ruleName, String builtInName, List<BuiltInArgument> arguments) throws BuiltInException
	{
	  int argumentNumber = 0;
	
	  for (BuiltInArgument argument : arguments) {
	    if (argument.isUnbound())  throw new BuiltInException("built-in " + builtInName + " in rule " + ruleName + " " +
	                                                          "returned with unbound argument ?" + argument.getVariableName());
	    else if (argument instanceof MultiArgument && ((MultiArgument)argument).hasNoArguments())
	      throw new BuiltInException("built-in " + builtInName + " in rule " + ruleName + " " +
	                                 "returned with empty multi-argument ?" + argument.getVariableName());
	    argumentNumber++;
	  } // for
	} 
	
	public List<BuiltInArgument> copyArguments(List<BuiltInArgument> arguments) throws BuiltInException
	{
	  return new ArrayList<BuiltInArgument>(arguments);
	} 
	
	public boolean processResultArgument(List<BuiltInArgument> arguments, int resultArgumentNumber, Collection<BuiltInArgument> resultArguments) 
	  throws BuiltInException
	{
	  boolean result = false;
	
	  checkArgumentNumber(resultArgumentNumber, arguments);
	
	  if (isUnboundArgument(resultArgumentNumber, arguments)) {
	    MultiArgument multiArgument = argumentFactory.createMultiArgument();
	    for (BuiltInArgument argument : resultArguments) multiArgument.addArgument(argument);
	    arguments.get(resultArgumentNumber).setBuiltInResult(multiArgument);
	    result = !multiArgument.hasNoArguments();
	  } else {
	    BuiltInArgument argument = arguments.get(resultArgumentNumber);
	    result = resultArguments.contains(argument);
	  } //if
	  
	  return result;
	} 
	
	public boolean processResultArgument(List<BuiltInArgument> arguments, int resultArgumentNumber, BuiltInArgument resultArgument) 
	  throws BuiltInException
	{
	  boolean result = false;
	
	  checkArgumentNumber(resultArgumentNumber, arguments);
	
	  if (isUnboundArgument(resultArgumentNumber, arguments)) {
	    arguments.get(resultArgumentNumber).setBuiltInResult(resultArgument); 
	    result = true;
	  } else {
	    BuiltInArgument argument = arguments.get(resultArgumentNumber);
	    result = argument.equals(resultArgument);
	  } //if
	  
	  return result;
	} 
	
	public boolean processResultArgument(List<BuiltInArgument> arguments, int resultArgumentNumber, DataValueArgument resultArgument) throws BuiltInException
	{
	  boolean result = false;
	
	  checkArgumentNumber(resultArgumentNumber, arguments);
	
	  if (isUnboundArgument(resultArgumentNumber, arguments)) {
	    arguments.get(resultArgumentNumber).setBuiltInResult(resultArgument);
	    result = true;
	  } else {
	    DataValue dataValue = getArgumentAsADataValue(resultArgumentNumber, arguments);
	    result = dataValue.equals(resultArgument.getDataValue());
	  } //if
	  
	  return result;
	} 
	
	public boolean processResultArgument(List<BuiltInArgument> arguments, int resultArgumentNumber, short resultArgument) 
	  throws BuiltInException
	{
	  return processResultArgument(arguments, resultArgumentNumber, argumentFactory.createDataValueArgument(resultArgument));
	} 
	
	public boolean processResultArgument(List<BuiltInArgument> arguments, int resultArgumentNumber, 
	                                     int resultArgument) throws BuiltInException
	{
	  return processResultArgument(arguments, resultArgumentNumber, argumentFactory.createDataValueArgument(resultArgument));
	} 
	
	public boolean processResultArgument(List<BuiltInArgument> arguments, int resultArgumentNumber, long resultArgument) throws BuiltInException
	{
	  return processResultArgument(arguments, resultArgumentNumber, argumentFactory.createDataValueArgument(resultArgument));
	} 
	
	public boolean processResultArgument(List<BuiltInArgument> arguments, int resultArgumentNumber, float resultArgument) throws BuiltInException
	{
	  return processResultArgument(arguments, resultArgumentNumber, argumentFactory.createDataValueArgument(resultArgument));
	} 
	
	public boolean processResultArgument(List<BuiltInArgument> arguments, int resultArgumentNumber, 
	                                     double resultArgument) throws BuiltInException
	{
	  return processResultArgument(arguments, resultArgumentNumber, argumentFactory.createDataValueArgument(resultArgument));
	}
	
	public boolean processResultArgument(List<BuiltInArgument> arguments, int resultArgumentNumber, 
	                                     byte resultArgument) throws BuiltInException
	{
	  return processResultArgument(arguments, resultArgumentNumber, argumentFactory.createDataValueArgument(resultArgument));
	} 
	
	public boolean processResultArgument(List<BuiltInArgument> arguments, int resultArgumentNumber, 
	                                     String resultArgument) throws BuiltInException
	{
	  return processResultArgument(arguments, resultArgumentNumber, argumentFactory.createDataValueArgument(resultArgument));
	}

	public ClassArgument createClassArgument(String classURI) { return argumentFactory.createClassArgument(classURI); }
	public IndividualArgument createIndividualArgument(String individualURI) { return argumentFactory.createIndividualArgument(individualURI); }
	public ObjectPropertyArgument createObjectPropertyArgument(String propertyURI) { return argumentFactory.createObjectPropertyArgument(propertyURI); }
	public DataPropertyArgument createDataPropertyArgument(String propertyURI) { return argumentFactory.createDataPropertyArgument(propertyURI); }
	
	public DataValueArgument createDataValueArgument(DataValue dataValue) { return argumentFactory.createDataValueArgument(dataValue); }
	public DataValueArgument createDataValueArgument(String s) { return argumentFactory.createDataValueArgument(s); }
	public DataValueArgument createDataValueArgument(boolean b) { return argumentFactory.createDataValueArgument(b); }
	public DataValueArgument createDataValueArgument(int i) { return argumentFactory.createDataValueArgument(i); }
	public DataValueArgument createDataValueArgument(long l) { return argumentFactory.createDataValueArgument(l); }
	public DataValueArgument createDataValueArgument(float f) { return argumentFactory.createDataValueArgument(f); }
	public DataValueArgument createDataValueArgument(double d) { return argumentFactory.createDataValueArgument(d); }
	public DataValueArgument createDataValueArgument(short s) { return argumentFactory.createDataValueArgument(s); }
	public DataValueArgument createDataValueArgument(Byte b) { return argumentFactory.createDataValueArgument(b); }
	public DataValueArgument createDataValueArgument(XSDType xsd) { return argumentFactory.createDataValueArgument(xsd); }
	public DataValueArgument createDataValueArgument(Object o) throws DataValueConversionException { return argumentFactory.createDataValueArgument(o); }
	
	public MultiArgument createMultiArgument() { return argumentFactory.createMultiArgument(); }
	public MultiArgument createMultiArgument(List<BuiltInArgument> arguments) { return argumentFactory.createMultiArgument(arguments); }
	
	public CollectionArgument createCollectionArgument(String collectionID) { return argumentFactory.createCollectionArgument(collectionID); }

}
