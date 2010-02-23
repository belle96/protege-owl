
// TODO: built-ins for URIs, and lists are not implemented.

package edu.stanford.smi.protegex.owl.swrl.bridge.builtins.swrlb;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.lang.StringUtils;

import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.MultiArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.builtins.AbstractSWRLBuiltInLibrary;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.BuiltInException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.BuiltInNotImplementedException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.InvalidBuiltInArgumentException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.InvalidBuiltInNameException;
import edu.stanford.smi.protegex.owl.swrl.bridge.xsd.XSDTimeUtil;

/**
 ** Implementations library for the core SWRL built-in methods. These built-ins are defined <a
 ** href="http://www.daml.org/2004/04/swrl/builtins.html">here</a> and are documented <a
 ** href="http://protege.cim3.net/cgi-bin/wiki.pl?CoreSWRLBuiltIns">here</a>.
 **
 ** See <a href="http://protege.cim3.net/cgi-bin/wiki.pl?SWRLBuiltInBridge">here</a> for documentation on defining SWRL built-in libraries.
 **
 ** Built-ins for lists and URIs not yet implemented.
 */
public class SWRLBuiltInLibraryImpl extends AbstractSWRLBuiltInLibrary
{
  private static String SWRLBLibraryName = "SWRLCoreBuiltIns";
  private static String SWRLBPrefix = "swrlb:";
  private DateFormat dateFormat;

  private static String SWRLB_ADD = SWRLBPrefix + "add";
  private static String SWRLB_SUBTRACT = SWRLBPrefix + "subtract";
  private static String SWRLB_MULTIPLY = SWRLBPrefix + "multiply";
  private static String SWRLB_DIVIDE = SWRLBPrefix + "divide";
  private static String SWRLB_INTEGER_DIVIDE = SWRLBPrefix + "integerDivide";
  private static String SWRLB_MOD = SWRLBPrefix + "mod";
  private static String SWRLB_POW = SWRLBPrefix + "pow";
  private static String SWRLB_UNARY_PLUS = SWRLBPrefix + "unaryPlus";
  private static String SWRLB_UNARY_MINUS = SWRLBPrefix + "unaryMinus";
  private static String SWRLB_ABS = SWRLBPrefix + "abs";
  private static String SWRLB_CEILING = SWRLBPrefix + "ceiling";
  private static String SWRLB_FLOOR = SWRLBPrefix + "floor";
  private static String SWRLB_ROUND = SWRLBPrefix + "round";
  private static String SWRLB_ROUND_HALF_TO_EVEN = SWRLBPrefix + "roundHalfToEven";
  private static String SWRLB_SIN = SWRLBPrefix + "sin";
  private static String SWRLB_COS = SWRLBPrefix + "cos";
  private static String SWRLB_TAN = SWRLBPrefix + "tan";

  public SWRLBuiltInLibraryImpl() 
  { 
    super(SWRLBLibraryName); 
  } // SWRLBuiltInLibraryImpl

  public void reset() {}
  
  // Built-ins for comparison, defined in Section 8.1. of http://www.daml.org/2004/04/swrl/builtins.html.

  public boolean greaterThan(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    checkNumberOfArgumentsEqualTo(2, arguments.size());

    if (isArgumentAString(0, arguments)) {   
      String s1 = getArgumentAsAString(0, arguments);
      if (isArgumentAString(1, arguments)) {
        String s2 = getArgumentAsAString(1, arguments);
        result = s1.compareTo(s2) > 0;
      } else throw new InvalidBuiltInArgumentException(1, "expecting string argument for comparison, got '" + getArgumentAsAString(1, arguments) + "'");
    } else if (isArgumentNumeric(0, arguments)) {
      if (isArgumentNumeric(1, arguments)) result = compareTwoNumericArguments(arguments) > 0;
      else throw new InvalidBuiltInArgumentException(1, "expecting numeric argument for comparison, got '" + getArgumentAsAString(1, arguments) + "'");
    } else throw new InvalidBuiltInArgumentException(0, "expecting string or numeric argument for comparison, got '" + getArgumentAsAString(0, arguments) + "'");

    return result;
  } // greaterThan

  public boolean lessThan(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    checkNumberOfArgumentsEqualTo(2, arguments.size());

    if (isArgumentAString(0, arguments)) {   
      String s1 = getArgumentAsAString(0, arguments);
      if (isArgumentAString(1, arguments)) {
        String s2 = getArgumentAsAString(1, arguments);
        result = s1.compareTo(s2) < 0;
      } else throw new InvalidBuiltInArgumentException(1, "expecting string argument for comparison, got '" + getArgumentAsAString(1, arguments) + "'");
    } else if (isArgumentNumeric(0, arguments)) {
      if (isArgumentNumeric(1, arguments)) result = compareTwoNumericArguments(arguments) < 0;
      else throw new InvalidBuiltInArgumentException(1, "expecting numeric argument for comparison, got '" + getArgumentAsAString(1, arguments) + "'");
    } else throw new InvalidBuiltInArgumentException(0, "expecting string or numeric argument for comparison, got '" + getArgumentAsAString(0, arguments) + "'");

    return result;
  } // lessThan

  public boolean equal(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    checkNumberOfArgumentsEqualTo(2, arguments.size());

    if (hasUnboundArguments(arguments)) 
      throw new InvalidBuiltInArgumentException(0, "comparison built-ins do not support argument binding");

    if (isArgumentABoolean(0, arguments)) {
      boolean b1 = getArgumentAsABoolean(0, arguments);
      if (isArgumentABoolean(1, arguments)) {
        boolean b2 = getArgumentAsABoolean(1, arguments);
        result = b1 == b2;
      } else result = false;
    } else if (isArgumentAString(0, arguments)) {   
      String s1 = getArgumentAsAString(0, arguments);
      if (isArgumentAString(1, arguments)) {
        String s2 = getArgumentAsAString(1, arguments);
        result = s1.compareTo(s2) == 0;
      } else result = false;
    } else if (isArgumentNumeric(0, arguments)) {
      if (isArgumentNumeric(1, arguments)) result = compareTwoNumericArguments(arguments) == 0;
      else throw new InvalidBuiltInArgumentException(1, "expecting numeric argument for comparison, got '" + getArgumentAsAString(1, arguments) + "'");
    } else throw new InvalidBuiltInArgumentException(0, "expecting string, numeric or boolean argument for comparison, got '" + getArgumentAsAString(0, arguments) + "'");

    return result;
  } // equal

  public boolean notEqual(List<BuiltInArgument> arguments) throws BuiltInException
  {
    return !equal(arguments);
  } // notEqual

  public boolean lessThanOrEqual(List<BuiltInArgument> arguments) throws BuiltInException
  {
    return equal(arguments) || lessThan(arguments);
  } // lessThanOrEqual

  public boolean greaterThanOrEqual(List<BuiltInArgument> arguments) throws BuiltInException
  {
    return equal(arguments) || greaterThan(arguments);
  } // greaterThanOrEqual

  // Math Built-ins, defined in Section 8.2. of http://www.daml.org/2004/04/swrl/builtins.html.
  
  public boolean add(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsAtLeast(2, arguments.size());

    return mathOperation(SWRLB_ADD, arguments); 
  } // add

  public boolean subtract(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(3, arguments.size());

    return mathOperation(SWRLB_SUBTRACT, arguments);
  } // subtract

  public boolean multiply(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsAtLeast(2, arguments.size());

    return mathOperation(SWRLB_MULTIPLY, arguments);
  } // multiply

  public boolean divide(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(3, arguments.size());

    return mathOperation(SWRLB_DIVIDE, arguments);
  } // divide

  public boolean integerDivide(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(3, arguments.size());

    return mathOperation(SWRLB_INTEGER_DIVIDE, arguments);
  } // integerDivide

  public boolean mod(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(3, arguments.size());

    return mathOperation(SWRLB_MOD, arguments);
  } // mod

  public boolean pow(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(3, arguments.size());

    return mathOperation(SWRLB_POW, arguments);
  } // pow

  public boolean unaryPlus(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(2, arguments.size());

    return mathOperation(SWRLB_UNARY_PLUS, arguments);
  } // unaryPlus

  public boolean unaryMinus(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(2, arguments.size());

    return mathOperation(SWRLB_UNARY_MINUS, arguments);
  } // unaryMinus

  public boolean abs(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(2, arguments.size());

    return mathOperation(SWRLB_ABS, arguments);
  } // abs

  public boolean ceiling(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(2, arguments.size());

    return mathOperation(SWRLB_CEILING, arguments);
  } // ceiling

  public boolean floor(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(2, arguments.size());

    return mathOperation(SWRLB_FLOOR, arguments);
  } // floor

  public boolean round(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(2, arguments.size());

    return mathOperation(SWRLB_ROUND, arguments);
  } // round

  public boolean roundHalfToEven(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(2, arguments.size());

    return mathOperation(SWRLB_ROUND_HALF_TO_EVEN, arguments);
  } // roundHalfToEven

  public boolean sin(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(2, arguments.size());

    return mathOperation(SWRLB_SIN, arguments);
  } // sin

  public boolean cos(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(2, arguments.size());

    return mathOperation(SWRLB_COS, arguments);
  } // cos

  public boolean tan(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(2, arguments.size());

    return mathOperation(SWRLB_TAN, arguments);
  } // tan

  // Built-ins for Booleans. cf. Section 8.3 of http://www.daml.org/2004/04/swrl/builtins.html

  public boolean booleanNot(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result;

    checkNumberOfArgumentsEqualTo(2, arguments.size());
    checkForUnboundNonFirstArguments(arguments);

    if (isUnboundArgument(0, arguments)) {
      if (!areAllArgumentsBooleans(arguments.subList(1, arguments.size())))
        throw new InvalidBuiltInArgumentException(1, "expecting a Boolean");

      boolean operationResult = !getArgumentAsABoolean(1, arguments);
      arguments.get(0).setBuiltInResult(createDataValueArgument(operationResult)); // Bind the result to the first parameter
      result = true;
    } else {
      if (!areAllArgumentsBooleans(arguments))
        throw new InvalidBuiltInArgumentException("expecting all Boolean arguments");

      result = !equal(arguments);
    } // if
    return result;
  } // booleanNot

  // Built-ins for Strings. cf. Section 8.4 of http://www.daml.org/2004/04/swrl/builtins.html
  
  public boolean stringEqualIgnoreCase(List<BuiltInArgument> arguments) throws BuiltInException
  {
    String argument1, argument2;

    checkNumberOfArgumentsEqualTo(2, arguments.size());
    checkForUnboundArguments(arguments);

    argument1 = getArgumentAsAString(0, arguments);
    argument2 = getArgumentAsAString(1, arguments);

    return argument1.equalsIgnoreCase(argument2);
  } // stringEqualIgnoreCase

  public boolean stringConcat(List<BuiltInArgument> arguments) throws BuiltInException
  {
    String operationResult = "";
    boolean result;

    checkNumberOfArgumentsAtLeast(2, arguments.size());
    checkForUnboundNonFirstArguments(arguments);

    for (int argumentNumber = 1; argumentNumber < arguments.size(); argumentNumber++) { // Exception thrown if argument is not a literal.
      operationResult = operationResult.concat(getArgumentAsADataValue(argumentNumber, arguments).toString());
    } // for

    if (isUnboundArgument(0, arguments)) {
      arguments.get(0).setBuiltInResult(createDataValueArgument(operationResult)); // Bind the result to the first parameter
      result = true;
    } else {
      String argument1 = getArgumentAsAString(0, arguments);
      result =  argument1.equals(operationResult);
    } //if

    return result;
  } // stringConcat

  public boolean substring(List<BuiltInArgument> arguments) throws BuiltInException
  {
    String argument2, operationResult;
    int startIndex, length;
    boolean result;

    checkNumberOfArgumentsAtLeast(3, arguments.size());
    checkNumberOfArgumentsAtMost(4, arguments.size());
    checkForUnboundNonFirstArguments(arguments);

    argument2 = getArgumentAsAString(1, arguments);
    startIndex = getArgumentAsAnInteger(2, arguments);

    if (arguments.size() == 4) {
      length = getArgumentAsAnInteger(3, arguments);
      operationResult = argument2.substring(startIndex, length);
    } else operationResult = argument2.substring(startIndex);

    if (hasUnboundArguments(arguments)) {
      arguments.get(0).setBuiltInResult(createDataValueArgument(operationResult)); // Bind the result to the first parameter
      result = true;
    } else {
      String argument1 = getArgumentAsAString(0, arguments);
      result =  argument1.equals(operationResult);
    } //if
    return result;
  } // substring

  public boolean stringLength(List<BuiltInArgument> arguments) throws BuiltInException
  {
    String argument2;
    boolean result;
    int operationResult;

    checkNumberOfArgumentsEqualTo(2, arguments.size());
    checkForUnboundNonFirstArguments(arguments);

    argument2 = getArgumentAsAString(1, arguments);
    operationResult = argument2.length();

    if (hasUnboundArguments(arguments)) {
      arguments.get(0).setBuiltInResult(createDataValueArgument(operationResult)); // Bind the result to the first parameter
      result = true;
    } else {
      String argument1 = getArgumentAsAString(0, arguments);
      result = argument1.length() == operationResult;
    } //if
    return result;
  } // stringLength

  public boolean upperCase(List<BuiltInArgument> arguments) throws BuiltInException
  {
    String argument2, operationResult;
    boolean result;

    checkNumberOfArgumentsEqualTo(2, arguments.size());
    checkForUnboundNonFirstArguments(arguments);

    argument2 = getArgumentAsAString(1, arguments);
    operationResult = argument2.toUpperCase();

    if (hasUnboundArguments(arguments)) {
      arguments.get(0).setBuiltInResult(createDataValueArgument(operationResult)); // Bind the result to the first parameter
      result = true;
    } else {
      String argument1 = getArgumentAsAString(0, arguments);
      result = argument1.equals(operationResult);
    } //if
    return result;
  } // upperCase

  public boolean lowerCase(List<BuiltInArgument> arguments) throws BuiltInException
  {
    String argument2, operationResult;
    boolean result;

    checkNumberOfArgumentsEqualTo(2, arguments.size());
    checkForUnboundNonFirstArguments(arguments);

    argument2 = getArgumentAsAString(1, arguments);

    operationResult = argument2.toLowerCase();

    if (hasUnboundArguments(arguments)) {
      arguments.get(0).setBuiltInResult(createDataValueArgument(operationResult)); // Bind the result to the first parameter
      result = true;
    } else {
      String argument1 = getArgumentAsAString(0, arguments);
      result = argument1.equals(operationResult);
    } //if
    return result;
  } // lowerCase

  public boolean contains(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(2, arguments.size());
    checkForUnboundArguments(arguments);

    String argument1 = getArgumentAsAString(0, arguments);
    String argument2 = getArgumentAsAString(1, arguments);

    return argument1.lastIndexOf(argument2) != -1;
  } // contains

  public boolean containsIgnoreCase(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(2, arguments.size());
    checkForUnboundArguments(arguments);

    String argument1 = getArgumentAsAString(0, arguments);
    String argument2 = getArgumentAsAString(1, arguments);

    return argument1.toLowerCase().lastIndexOf(argument2.toLowerCase()) != -1;
  } // containsIgnoreCase

  public boolean startsWith(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(2, arguments.size());
    checkForUnboundArguments(arguments);

    String argument1 = getArgumentAsAString(0, arguments);
    String argument2 = getArgumentAsAString(1, arguments);

    return argument1.startsWith(argument2);
  } // startsWith

  public boolean endsWith(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(2, arguments.size());
    checkForUnboundArguments(arguments);

    String argument1 = getArgumentAsAString(0, arguments);
    String argument2 = getArgumentAsAString(1, arguments);

    return argument1.endsWith(argument2);
  } // endsWith

  public boolean translate(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result;

    checkNumberOfArgumentsEqualTo(4, arguments.size());

    String argument2 = getArgumentAsAString(1, arguments);
    String argument3 = getArgumentAsAString(2, arguments);
    String argument4 = getArgumentAsAString(3, arguments);
    String operationResult = StringUtils.replaceChars(argument2, argument3, argument4);

    if (isUnboundArgument(0, arguments)) {
      arguments.get(0).setBuiltInResult(createDataValueArgument(operationResult)); 
      result = true;
    } else {
      String argument1 = getArgumentAsAString(0, arguments);
      result = argument1.equals(operationResult);
    } // if
    
    return result;
  } // translate

  public boolean substringAfter(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result;

    checkNumberOfArgumentsEqualTo(3, arguments.size());

    String argument2 = getArgumentAsAString(1, arguments);
    String argument3 = getArgumentAsAString(2, arguments);
    String operationResult = StringUtils.substringAfter(argument2, argument3);

    if (isUnboundArgument(0, arguments)) {
      arguments.get(0).setBuiltInResult(createDataValueArgument(operationResult)); 
      result = true;
    } else {
      String argument1 = getArgumentAsAString(0, arguments);
      result = argument1.equals(operationResult);
    } // if
    
    return result;
  } // substringAfter

  public boolean substringBefore(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result;

    checkNumberOfArgumentsEqualTo(3, arguments.size());

    String argument2 = getArgumentAsAString(1, arguments);
    String argument3 = getArgumentAsAString(2, arguments);
    String operationResult = StringUtils.substringBefore(argument2, argument3);

    if (isUnboundArgument(0, arguments)) {
      arguments.get(0).setBuiltInResult(createDataValueArgument(operationResult)); 
      result = true;
    } else {
      String argument1 = getArgumentAsAString(0, arguments);
      result = argument1.equals(operationResult);
    } // if

    return result;
  } // substringBefore

  public boolean matches(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    checkNumberOfArgumentsEqualTo(2, arguments.size());
    checkForUnboundArguments(arguments);

    String argument1 = getArgumentAsAString(0, arguments);
    String argument2 = getArgumentAsAString(1, arguments);

    try {
      result = Pattern.matches(argument2, argument1);
    } catch (PatternSyntaxException e) {
      throw new InvalidBuiltInArgumentException(1, "invalid regular expression '" + argument2 + "': " + e.getMessage());
    } // try

    return result;
  } // matches

  public boolean replace(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    checkNumberOfArgumentsEqualTo(4, arguments.size());
    checkForUnboundNonFirstArguments(arguments);

    String input = getArgumentAsAString(1, arguments);
    String regex = getArgumentAsAString(2, arguments);
    String replacement = getArgumentAsAString(3, arguments);

    Pattern p = Pattern.compile(regex);
    Matcher m = p.matcher(input);
    String operationResult = m.replaceAll(replacement);

    if (isUnboundArgument(0, arguments)) {
      arguments.get(0).setBuiltInResult(createDataValueArgument(operationResult)); 
      result = true;
    } else {
      String output = getArgumentAsAString(0, arguments);
      result = output.equals(operationResult);
    } // if

    return result;
  } // replace

  public boolean normalizeSpace(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    checkNumberOfArgumentsEqualTo(2, arguments.size());
    checkForUnboundNonFirstArguments(arguments);

    String input = getArgumentAsAString(1, arguments);

    Pattern p = Pattern.compile("\\s+");
    Matcher m = p.matcher(input);
    String operationResult = m.replaceAll(" ").trim();

    if (isUnboundArgument(0, arguments)) {
      arguments.get(0).setBuiltInResult(createDataValueArgument(operationResult)); 
      result = true;
    } else {
      String output = getArgumentAsAString(0, arguments);
      result = output.equals(operationResult);
    } // if

    return result;
  } // normalizeSpace

  public boolean tokenize(List<BuiltInArgument> arguments) throws BuiltInException
  {
    StringTokenizer tokenizer;
    String inputString, delimeters;
    boolean result = false;

    if (!isUnboundArgument(0, arguments)) throw new InvalidBuiltInArgumentException(0, "unexpected bound argument found");

    checkNumberOfArgumentsEqualTo(3, arguments.size());
    checkForUnboundNonFirstArguments(arguments);

    inputString = getArgumentAsAString(1, arguments);
    delimeters = getArgumentAsAString(2, arguments);

    tokenizer = new StringTokenizer(inputString.trim(), delimeters);
    
    MultiArgument multiArgument = createMultiArgument(getVariableName(0, arguments));
    while (tokenizer.hasMoreTokens()) {
      String token = tokenizer.nextToken();
      multiArgument.addArgument(createDataValueArgument(token));
    } // while

    arguments.get(0).setBuiltInResult(multiArgument);
    result = !multiArgument.hasNoArguments();

    return result;
  } // tokenize

  // Built-ins for date, time and duration.

  public boolean yearMonthDuration(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(3, arguments.size());
    int year = getArgumentAsAnInteger(1, arguments);
    int month = getArgumentAsAnInteger(2, arguments);
    org.apache.axis.types.Duration duration = new org.apache.axis.types.Duration();

    duration.setYears(year);
    duration.setMonths(month);

    return processResultArgument(arguments, 0, duration.toString());
  } // yearMonthDuration

  @SuppressWarnings("deprecation")
  public boolean dayTimeDuration(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(5, arguments.size());
    int days =  getArgumentAsAnInteger(1, arguments);
    int hours =  getArgumentAsAnInteger(2, arguments);
    int minutes =  getArgumentAsAnInteger(3, arguments);
    int seconds =  getArgumentAsAnInteger(4, arguments);
    org.apache.axis.types.Duration duration = new org.apache.axis.types.Duration();

    duration.setDays(days);
    duration.setHours(hours);
    duration.setMinutes(minutes);
    duration.setSeconds(seconds);

    return processResultArgument(arguments, 0, duration.toString());
  } // dayTimeDuration

  public boolean dateTime(List<BuiltInArgument> arguments) throws BuiltInException
  {
    String operationResult;

    checkNumberOfArgumentsEqualTo(8, arguments.size());

    int year =  getArgumentAsAnInteger(1, arguments);
    int month =  getArgumentAsAnInteger(2, arguments);
    int days =  getArgumentAsAnInteger(3, arguments);
    int hours =  getArgumentAsAnInteger(4, arguments);
    int minutes =  getArgumentAsAnInteger(5, arguments);
    int seconds =  getArgumentAsAnInteger(6, arguments);
    String timeZone  =  getArgumentAsAString(7, arguments);
    Calendar calendar = new GregorianCalendar();

    calendar.set(year, month, days, hours, minutes, seconds);
    calendar.setTimeZone(TimeZone.getTimeZone(timeZone));

    operationResult = "" + calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + "T" +
                      calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND) + calendar.getTimeZone().getID();

    return processResultArgument(arguments, 0, operationResult);
  } // dateTime

  public boolean date(List<BuiltInArgument> arguments) throws BuiltInException
  {
    String operationResult;

    checkNumberOfArgumentsEqualTo(5, arguments.size());

    int year =  getArgumentAsAnInteger(1, arguments);
    int month =  getArgumentAsAnInteger(2, arguments);
    int days =  getArgumentAsAnInteger(3, arguments);
    String timeZone  =  getArgumentAsAString(4, arguments);
    Calendar calendar = new GregorianCalendar();

    calendar.set(year, month, days);
    calendar.setTimeZone(TimeZone.getTimeZone(timeZone));

    operationResult = "" + calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + calendar.getTimeZone().getID();

    return processResultArgument(arguments, 0, operationResult);
  } // date

  public boolean time(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(5, arguments.size());

    int hours =  getArgumentAsAnInteger(1, arguments);
    int minutes =  getArgumentAsAnInteger(2, arguments);
    int seconds =  getArgumentAsAnInteger(3, arguments);
    String timeZone  =  getArgumentAsAString(4, arguments);
    String operationResult = "" + hours + ":" + minutes + ":" + seconds + timeZone;

    return processResultArgument(arguments, 0, operationResult);
  } // time

  public boolean addYearMonthDurations(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsAtLeast(3, arguments.size());

    org.apache.axis.types.Duration operationDuration = new org.apache.axis.types.Duration();

    for (int i = 1; i < arguments.size(); i++) {
      org.apache.axis.types.Duration duration = getArgumentAsADuration(i, arguments);
      operationDuration = XSDTimeUtil.addYearMonthDurations(operationDuration, duration);
    } // for

    return processResultArgument(arguments, 0, operationDuration.toString());
  } // addYearMonthDurations

  public boolean subtractYearMonthDurations(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(3, arguments.size());

    org.apache.axis.types.Duration duration2 = getArgumentAsADuration(1, arguments);
    org.apache.axis.types.Duration duration3 = getArgumentAsADuration(2, arguments);
    org.apache.axis.types.Duration operationDuration = XSDTimeUtil.subtractYearMonthDurations(duration2, duration3);

    return processResultArgument(arguments, 0, operationDuration.toString());
  } // subtractYearMonthDurations

  public boolean multiplyYearMonthDuration(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(3, arguments.size());

    org.apache.axis.types.Duration duration2 = getArgumentAsADuration(1, arguments);
    org.apache.axis.types.Duration duration3 = getArgumentAsADuration(2, arguments);
    org.apache.axis.types.Duration operationDuration = XSDTimeUtil.multiplyYearMonthDurations(duration2, duration3);

    return processResultArgument(arguments, 0, operationDuration.toString());
  } // multiplyYearMonthDuration

  public boolean divideYearMonthDurations(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(3, arguments.size());

    org.apache.axis.types.Duration duration2 = getArgumentAsADuration(1, arguments);
    org.apache.axis.types.Duration duration3 = getArgumentAsADuration(2, arguments);
    org.apache.axis.types.Duration operationDuration = XSDTimeUtil.divideYearMonthDurations(duration2, duration3);

    return processResultArgument(arguments, 0, operationDuration.toString());
  } // divideYearMonthDurations

  public boolean addDayTimeDurations(List<BuiltInArgument> arguments) throws BuiltInException
  {
    org.apache.axis.types.Duration operationDuration = new org.apache.axis.types.Duration();

    checkNumberOfArgumentsAtLeast(3, arguments.size());

    for (int i = 1; i < arguments.size(); i++) {
      org.apache.axis.types.Duration duration = getArgumentAsADuration(i, arguments);
      operationDuration = XSDTimeUtil.addDayTimeDurations(operationDuration, duration);      
    } // for

    return processResultArgument(arguments, 0, operationDuration.toString());
  } // addDayTimeDurations

  public boolean subtractDayTimeDurations(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(3, arguments.size());

    org.apache.axis.types.Duration duration2 = getArgumentAsADuration(1, arguments);
    org.apache.axis.types.Duration duration3 = getArgumentAsADuration(2, arguments);
    org.apache.axis.types.Duration operationDuration = XSDTimeUtil.subtractDayTimeDurations(duration2, duration3);

    return processResultArgument(arguments, 0, operationDuration.toString());
  } // subtractDayTimeDurations

  public boolean multiplyDayTimeDurations(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(3, arguments.size());

    org.apache.axis.types.Duration duration2 = getArgumentAsADuration(1, arguments);
    org.apache.axis.types.Duration duration3 = getArgumentAsADuration(2, arguments);
    org.apache.axis.types.Duration operationDuration = XSDTimeUtil.multiplyDayTimeDurations(duration2, duration3);

    return processResultArgument(arguments, 0, operationDuration.toString());
  } // multiplyDayTimeDurations

  public boolean divideDayTimeDuration(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(3, arguments.size());

    org.apache.axis.types.Duration duration2 = getArgumentAsADuration(1, arguments);
    org.apache.axis.types.Duration duration3 = getArgumentAsADuration(2, arguments);
    org.apache.axis.types.Duration operationDuration = XSDTimeUtil.divideDayTimeDurations(duration2, duration3);

    return processResultArgument(arguments, 0, operationDuration.toString());
  } // divideDayTimeDuration

  public boolean subtractDates(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(3, arguments.size());

    java.util.Date date2 = getArgumentAsADate(1, arguments);
    java.util.Date date3 = getArgumentAsADate(2, arguments);
    org.apache.axis.types.Duration operationDuration = XSDTimeUtil.subtractDates(date2, date3);

    return processResultArgument(arguments, 0, operationDuration.toString());
  } // subtractDates

  public boolean subtractTimes(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(3, arguments.size());

    org.apache.axis.types.Time time2 = getArgumentAsATime(1, arguments);
    org.apache.axis.types.Time time3 = getArgumentAsATime(2, arguments);
    org.apache.axis.types.Duration operationDuration = XSDTimeUtil.subtractTimes(time2, time3);

    return processResultArgument(arguments, 0, operationDuration.toString());
  } // subtractTimes

  public boolean addYearMonthDurationToDateTime(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(3, arguments.size());

    java.util.Date date = getArgumentAsADate(1, arguments);
    org.apache.axis.types.Duration duration = getArgumentAsADuration(2, arguments);
    java.util.Date operationDateTime = XSDTimeUtil.addYearMonthDurationToDateTime(date, duration);

    return processResultArgument(arguments, 0, XSDTimeUtil.date2XSDDateTimeString(operationDateTime));
  } // addYearMonthDurationToDateTime

  public boolean subtractYearMonthDurationFromDateTime(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(3, arguments.size());

    java.util.Date date = getArgumentAsADate(1, arguments);
    org.apache.axis.types.Duration duration = getArgumentAsADuration(2, arguments);
    java.util.Date operationDateTime = XSDTimeUtil.subtractYearMonthDurationFromDateTime(date, duration);
 
    return processResultArgument(arguments, 0, XSDTimeUtil.date2XSDDateTimeString(operationDateTime));
  } // subtractYearMonthDurationFromDateTime

  public boolean addDayTimeDurationToDateTime(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(3, arguments.size());

    java.util.Date date = getArgumentAsADate(1, arguments);
    org.apache.axis.types.Duration duration = getArgumentAsADuration(2, arguments);
    java.util.Date operationDateTime = XSDTimeUtil.addDayTimeDurationToDateTime(date, duration);
 
    return processResultArgument(arguments, 0, XSDTimeUtil.date2XSDDateTimeString(operationDateTime));
  } // addDayTimeDurationToDateTime

  public boolean subtractDayTimeDurationFromDateTime(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(3, arguments.size());

    java.util.Date date = getArgumentAsADate(1, arguments);
    org.apache.axis.types.Duration duration = getArgumentAsADuration(2, arguments);
    java.util.Date operationDateTime = XSDTimeUtil.subtractDayTimeDurationFromDateTime(date, duration);
 
    return processResultArgument(arguments, 0, XSDTimeUtil.date2XSDDateTimeString(operationDateTime));
  } // subtractDayTimeDurationFromDateTime

  public boolean addYearMonthDurationToDate(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(3, arguments.size());

    java.util.Date date = getArgumentAsADate(1, arguments);
    org.apache.axis.types.Duration duration = getArgumentAsADuration(2, arguments);
    java.util.Date operationDateTime = XSDTimeUtil.addYearMonthDurationToDate(date, duration);
 
    return processResultArgument(arguments, 0, XSDTimeUtil.date2XSDDateTimeString(operationDateTime));
  } // addYearMonthDurationToDate

  public boolean subtractYearMonthDurationFromDate(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(3, arguments.size());

    java.util.Date date = getArgumentAsADate(1, arguments);
    org.apache.axis.types.Duration duration = getArgumentAsADuration(2, arguments);
    java.util.Date operationDateTime = XSDTimeUtil.subtractYearMonthDurationFromDate(date, duration);
 
    return processResultArgument(arguments, 0, XSDTimeUtil.date2XSDDateTimeString(operationDateTime));
  } // subtractYearMonthDurationFromDate

  public boolean addDayTimeDurationToDate(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(3, arguments.size());

    java.util.Date date = getArgumentAsADate(1, arguments);
    org.apache.axis.types.Duration duration = getArgumentAsADuration(2, arguments);
    java.util.Date operationDateTime = XSDTimeUtil.addDayTimeDurationToDateTime(date, duration);
 
    return processResultArgument(arguments, 0, XSDTimeUtil.date2XSDDateTimeString(operationDateTime));
  } // addDayTimeDurationToDate

  public boolean subtractDayTimeDurationFromDate(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(3, arguments.size());

    java.util.Date date = getArgumentAsADate(1, arguments);
    org.apache.axis.types.Duration duration = getArgumentAsADuration(2, arguments);
    java.util.Date operationDateTime = XSDTimeUtil.subtractDayTimeDurationFromDateTime(date, duration);
 
    return processResultArgument(arguments, 0, XSDTimeUtil.date2XSDDateTimeString(operationDateTime));
  } // subtractDayTimeDurationFromDate

  public boolean addDayTimeDurationToTime(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(3, arguments.size());

    org.apache.axis.types.Time time = getArgumentAsATime(1, arguments);
    org.apache.axis.types.Duration duration = getArgumentAsADuration(2, arguments);
    org.apache.axis.types.Time operationTime = XSDTimeUtil.addDayTimeDurationToTime(time, duration);

    return processResultArgument(arguments, 0, operationTime.toString());
  } // addDayTimeDurationToTime

  public boolean subtractDayTimeDurationFromTime(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(3, arguments.size());

    org.apache.axis.types.Time time = getArgumentAsATime(1, arguments);
    org.apache.axis.types.Duration duration = getArgumentAsADuration(2, arguments);
    org.apache.axis.types.Time operationTime = XSDTimeUtil.subtractDayTimeDurationFromTime(time, duration);

    return processResultArgument(arguments, 0, operationTime.toString());
  } // subtractDayTimeDurationFromTime

  public boolean subtractDateTimesYieldingYearMonthDuration(List<BuiltInArgument> arguments) throws BuiltInException
  {
   java.util.Date date1 = getArgumentAsADate(1, arguments);
   java.util.Date date2 = getArgumentAsADate(2, arguments);
   org.apache.axis.types.Duration operationDuration = XSDTimeUtil.subtractDateTimesYieldingYearMonthDuration(date1, date2);
 
   return processResultArgument(arguments, 0, operationDuration.toString());
  } // subtractDateTimesYieldingYearMonthDuration

  public boolean subtractDateTimesYieldingDayTimeDuration(List<BuiltInArgument> arguments) throws BuiltInException
  {
    java.util.Date date1 = getArgumentAsADate(1, arguments);
    java.util.Date date2 = getArgumentAsADate(2, arguments);
    org.apache.axis.types.Duration operationDuration = XSDTimeUtil.subtractDateTimesYieldingDayTimeDuration(date1, date2);
    
   return processResultArgument(arguments, 0, operationDuration.toString());
  } // subtractDateTimesYieldingDayTimeDuration 

  // Built-ins for URIs

  public boolean resolveURI(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // resolveURI

  public boolean anyURI(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // anyURI

  // Built-ins for Lists

  public boolean listConcat(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // listConcat

  public boolean listIntersection(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // listIntersection

  public boolean listSubtraction(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // listSubtraction

  public boolean member(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // member 

  public boolean length(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // length

  public boolean first(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // first

  public boolean rest(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // rest

  public boolean sublist(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // sublist

  public boolean empty(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // empty

  // Private methods

  private int compareTwoNumericArguments(List<BuiltInArgument> arguments) throws BuiltInException
  {
    int result = 0;

    checkThatAllArgumentsAreNumeric(arguments);

    if (isShortMostPreciseArgument(arguments)) {
      short s1 = getArgumentAsAShort(0, arguments);
      short s2 = getArgumentAsAShort(1, arguments);
      if (s1 < s2) result = -1; else if (s1 > s2) result =  1; else result = 0;
    } else if (isIntegerMostPreciseArgument(arguments)) {
      int i1 = getArgumentAsAnInteger(0, arguments);
      int i2 = getArgumentAsAnInteger(1, arguments);
      if (i1 < i2) result = -1; else if (i1 > i2) result = 1; else result = 0;
    } else if (isLongMostPreciseArgument(arguments)) {
      long l1 = getArgumentAsALong(0, arguments);
      long l2 = getArgumentAsALong(1, arguments); 
      if (l1 < l2) result = -1; else if (l1 > l2) result =  1; else result = 0;
    } else if (isFloatMostPreciseArgument(arguments)) {
      float f1 = getArgumentAsAFloat(0, arguments);
      float f2 = getArgumentAsAFloat(1, arguments); 
      if (f1 < f2) result = -1; else if (f1 > f2) result = 1; else result = 0;
    } else {
      double d1 = getArgumentAsADouble(0, arguments);
      double d2 = getArgumentAsADouble(1, arguments); 
      if (d1 < d2) result = -1; else if (d1 > d2) result =  1; else result = 0;
    } // if

    return result;
  } // equal

  private boolean mathOperation(String builtInName, List<BuiltInArgument> arguments) throws BuiltInException
  {
    int argumentNumber;
    double argument1 = 0.0, argument2, argument3, operationResult = 0.0; 
    boolean result = false, hasUnbound1stArgument = false;

    checkForUnboundNonFirstArguments(arguments); // Only supports binding of first argument

    if (isUnboundArgument(0, arguments)) hasUnbound1stArgument = true;

    // Argument number checking will have been performed by invoking method.
    if (!hasUnbound1stArgument) argument1 = getArgumentAsADouble(0, arguments);
    argument2 = getArgumentAsADouble(1, arguments);

    if (builtInName.equalsIgnoreCase(SWRLB_ADD)) {
      operationResult = 0.0;
      for (argumentNumber = 1; argumentNumber < arguments.size(); argumentNumber++) {
        operationResult += getArgumentAsADouble(argumentNumber, arguments);
      } // for
    } else if (builtInName.equalsIgnoreCase(SWRLB_MULTIPLY)) {
      operationResult = 1.0;
      for (argumentNumber = 1; argumentNumber < arguments.size(); argumentNumber++) {
        operationResult *= getArgumentAsADouble(argumentNumber, arguments);
      } // for
    } else if (builtInName.equalsIgnoreCase(SWRLB_SUBTRACT)) {
      argument3 = getArgumentAsADouble(2, arguments);
      operationResult = argument2 - argument3;
    } else if (builtInName.equalsIgnoreCase(SWRLB_DIVIDE)) {
      argument3 = getArgumentAsADouble(2, arguments);
      operationResult = (argument2 / argument3);
    } else if (builtInName.equalsIgnoreCase(SWRLB_INTEGER_DIVIDE)) {
      argument3 = getArgumentAsADouble(2, arguments);
      if (argument3 == 0) throw new InvalidBuiltInArgumentException(2, "zero passed as divisor");
      if (argument3 >= 0) operationResult = argument2 + argument3 + 1 / argument3;
      else operationResult = argument2 / argument3;
    } else if (builtInName.equalsIgnoreCase(SWRLB_MOD)) {
      argument3 = getArgumentAsADouble(2, arguments);
      operationResult = argument2 % argument3;
    } else if (builtInName.equalsIgnoreCase(SWRLB_POW)) {
      argument3 = getArgumentAsADouble(2, arguments);
      operationResult = java.lang.Math.pow(argument2, argument3);
    } else if (builtInName.equalsIgnoreCase(SWRLB_UNARY_PLUS)) operationResult = argument2;
    else if (builtInName.equalsIgnoreCase(SWRLB_UNARY_MINUS)) operationResult = -argument2;
    else if (builtInName.equalsIgnoreCase(SWRLB_ABS)) operationResult = java.lang.Math.abs(argument2);
    else if (builtInName.equalsIgnoreCase(SWRLB_CEILING)) operationResult = java.lang.Math.ceil(argument2);
    else if (builtInName.equalsIgnoreCase(SWRLB_FLOOR)) operationResult = java.lang.Math.floor(argument2);
    else if (builtInName.equalsIgnoreCase(SWRLB_ROUND)) operationResult = java.lang.Math.rint(argument2);
    else if (builtInName.equalsIgnoreCase(SWRLB_ROUND_HALF_TO_EVEN)) operationResult = java.lang.Math.rint(argument2);
    else if (builtInName.equalsIgnoreCase(SWRLB_SIN)) operationResult = java.lang.Math.sin(argument2);
    else if (builtInName.equalsIgnoreCase(SWRLB_COS)) operationResult = java.lang.Math.cos(argument2);
    else if (builtInName.equalsIgnoreCase(SWRLB_TAN)) operationResult = java.lang.Math.tan(argument2);
    else throw new InvalidBuiltInNameException(builtInName);
    
    if (hasUnbound1stArgument) { // Bind the result to the first argument.
      List<BuiltInArgument> boundArguments = arguments.subList(1, arguments.size());

      if (builtInName.equalsIgnoreCase(SWRLB_SIN) ||
          builtInName.equalsIgnoreCase(SWRLB_COS) || builtInName.equalsIgnoreCase(SWRLB_TAN))
        arguments.get(0).setBuiltInResult(createDataValueArgument(operationResult));
      else if (isShortMostPreciseArgument(boundArguments))
        arguments.get(0).setBuiltInResult(createDataValueArgument((short)operationResult)); 
      else if (isIntegerMostPreciseArgument(boundArguments))
        arguments.get(0).setBuiltInResult(createDataValueArgument((int)operationResult));
      else if (isFloatMostPreciseArgument(boundArguments))
        arguments.get(0).setBuiltInResult(createDataValueArgument((float)operationResult));
      else if (isLongMostPreciseArgument(boundArguments))
        arguments.get(0).setBuiltInResult(createDataValueArgument((long)operationResult));
      else arguments.get(0).setBuiltInResult(createDataValueArgument(operationResult));

      result = true;
    } else result = (argument1 == operationResult);

    return result;
  } // mathOperation

  private org.apache.axis.types.Duration getArgumentAsADuration(int argumentNumber, List<BuiltInArgument> arguments) 
    throws BuiltInException
  {
    org.apache.axis.types.Duration result = null;
    String argument = "";

    try {
      argument = getArgumentAsAString(argumentNumber, arguments);
      result = new org.apache.axis.types.Duration(argument);
    } catch (IllegalArgumentException e) {
      throw new BuiltInException("invalid xsd:duration '" + argument + "': " + e.getMessage());
    } // try

    return result;
  } // getArgumentAsADuration

  private java.util.Date getArgumentAsADate(int argumentNumber,  List<BuiltInArgument> arguments) throws BuiltInException
  {
    java.util.Date result = null;
    String argument = "";

    try {
      argument = getArgumentAsAString(argumentNumber, arguments);
      result = dateFormat.parse(argument); 
    } catch (ParseException e) {
      throw new BuiltInException("invalid xsd:date '" + argument + "': " + e.getMessage());
    } // try

    return result;
  } // getArgumentAsADate

  private org.apache.axis.types.Time getArgumentAsATime(int argumentNumber,  List<BuiltInArgument> arguments) throws BuiltInException
  {
    org.apache.axis.types.Time  result = null;
    String argument = "";

    try {
      argument = getArgumentAsAString(argumentNumber, arguments);
      result = new org.apache.axis.types.Time(argument); 
    } catch (NumberFormatException e) {
      throw new BuiltInException("invalid xsd:time '" + argument + "': " + e.getMessage());
    } // try

    return result;
  } // getArgumentAsATime

} // SWRLBuiltInLibraryImpl
