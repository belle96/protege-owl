
// TODO: a lot of cut-and-paste repetition here needs to be fixed.
// TODO: replace set operators min, max with least, greatest?
// TODO: need to optimize sorted sets so that they are not resorted unnecessarily

package edu.stanford.smi.protegex.owl.swrl.bridge.builtins.sqwrl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import edu.stanford.smi.protegex.owl.swrl.bridge.Argument;
import edu.stanford.smi.protegex.owl.swrl.bridge.ArgumentFactory;
import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.ClassArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.DataValueArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.IndividualArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLClass;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLDataValue;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLIndividual;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLProperty;
import edu.stanford.smi.protegex.owl.swrl.bridge.PropertyArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.builtins.AbstractSWRLBuiltInLibrary;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.BuiltInException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.InvalidBuiltInArgumentException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.ClassValue;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.DataValue;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.ObjectValue;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.PropertyValue;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.SQWRLNames;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.impl.ResultImpl;

/*
 * Implementation library for SQWRL built-ins. See <a href="http://protege.cim3.net/cgi-bin/wiki.pl?SQWRL">here</a> for documentation
 * on this built-in library.
 *
 *  
 */
public class SWRLBuiltInLibraryImpl extends AbstractSWRLBuiltInLibrary
{
  private Map<String, Set<BuiltInArgument>> sets;
  private Map<String, Integer> setGroupElementNumbersMap; // Set name to number of elements in group (which may be 0)
  private ArgumentFactory argumentFactory;
  
  public SWRLBuiltInLibraryImpl() { super(SQWRLNames.SQWRLBuiltInLibraryName); }
  
  public void reset()
  {
    sets = new HashMap<String, Set<BuiltInArgument>>();
    setGroupElementNumbersMap = new HashMap<String, Integer>(); 
    argumentFactory = ArgumentFactory.getFactory();
  } // reset
  
  public boolean select(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkForUnboundArguments(arguments);
    checkNumberOfArgumentsAtLeast(1, arguments.size());
    checkIfInConsequent();
    ResultImpl result = getResult(getInvokingRuleName());

    if (!result.isRowOpen()) result.openRow();

    int argumentIndex = 0;
    for (BuiltInArgument argument : arguments) {
      if (argument instanceof DataValueArgument) result.addRowData((DataValue)argument);
      else if (argument instanceof IndividualArgument) result.addRowData((ObjectValue)argument);
      else if (argument instanceof ClassArgument) result.addRowData((ClassValue)argument);
      else if (argument instanceof PropertyArgument) result.addRowData((PropertyValue)argument);
      else throw new InvalidBuiltInArgumentException(argumentIndex, "unknown type '" + argument.getClass() + "'");
      argumentIndex++;
    } // for
    
    return false;
  } // select

  public boolean selectDistinct(List<BuiltInArgument> arguments) throws BuiltInException
  {
	checkIfInConsequent();
    return select(arguments);
  } // selectDistinct
  
  public boolean count(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkForUnboundArguments(arguments);
    checkNumberOfArgumentsEqualTo(1, arguments.size());

    ResultImpl result = getResult(getInvokingRuleName());
    BuiltInArgument argument = arguments.get(0);

    if (!result.isRowOpen()) result.openRow();
    
    if (argument instanceof OWLDataValue) result.addRowData((DataValue)argument);
    else if (argument instanceof OWLIndividual) result.addRowData((ObjectValue)argument);
    else if (argument instanceof OWLClass) result.addRowData((ClassValue)argument);
    else if (argument instanceof OWLProperty) result.addRowData((PropertyValue)argument);
    else throw new InvalidBuiltInArgumentException(0, "unknown type '" + argument.getClass() + "'");
    
    return false;
  } // count

  public boolean countDistinct(List<BuiltInArgument> arguments) throws BuiltInException
  {
	checkIfInConsequent();
    return count(arguments);
  } // countDistinct

  // The use of columnNames, orderBy, orderByDescending is handled at initial processing in the SWRLRule object.  
  public boolean columnNames(List<BuiltInArgument> arguments) throws BuiltInException { return false; } 
  public boolean orderBy(List<BuiltInArgument> arguments) throws BuiltInException { return false; } 
  public boolean orderByDescending(List<BuiltInArgument> arguments) throws BuiltInException { return false; }

  public boolean makeSet(List<BuiltInArgument> arguments) throws BuiltInException
  {
	String setID = getSetIDInMake(arguments); // Get unique ID for set; does argument checking
	BuiltInArgument element = arguments.get(1); // The second argument is always the value
	Set<BuiltInArgument> set;
	
	checkIfInAntecedent();
	    
    if (sets.containsKey(setID)) set = sets.get(setID);
    else {  
        set = new HashSet<BuiltInArgument>(); sets.put(setID, set); 
    } // if

    set.add(element);
    // System.err.println("adding element " + element + " to set " + setID);

    if (isUnboundArgument(0, arguments)) arguments.get(0).setBuiltInResult(argumentFactory.createDataValueArgument(setID));

    return true;
  } // makeSet
  
  public boolean groupBy(List<BuiltInArgument> arguments) throws BuiltInException
  {
	checkIfInAntecedent();
	
	return true; // Should never be invoked - is a directive only and is processed by SQWRL processor
  } // groupBy

  public boolean isEmpty(List<BuiltInArgument> arguments) throws BuiltInException
  {
    String setID = getSetIDInSingleSetOperation(arguments, 0, 1); // Does argument checking
    
    checkIfInAntecedent();
    
    // System.err.println("isEmpty: arguments: " + arguments);
    // System.err.println("isEmpty: setID: " + setID);

    return getSet(setID).size() == 0;    
   } // isEmpty

  public boolean notEmpty(List<BuiltInArgument> arguments) throws BuiltInException
  {
    return !isEmpty(arguments);
  } // notEmpty

  public boolean size(List<BuiltInArgument> arguments) throws BuiltInException
  {
    String setID = getSetIDInSingleSetOperation(arguments, 1, 2); // Does argument checking
    long size = getSet(setID).size(); // Checks set ID validity
    
    checkIfInAntecedent();

    return processResultArgument(arguments, 0, size);
  } // size

  public boolean min(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (getIsInConsequent()) { // Simple SQWRL aggregation operator
      checkForUnboundArguments(arguments);
      checkNumberOfArgumentsEqualTo(1, arguments.size());
      
      ResultImpl resultImpl = getResult(getInvokingRuleName());
      BuiltInArgument argument = arguments.get(0);
      
      if (!resultImpl.isRowOpen()) resultImpl.openRow();
      
      if (argument instanceof OWLDataValue && ((OWLDataValue)argument).isNumeric()) resultImpl.addRowData((DataValue)argument);
      else throw new InvalidBuiltInArgumentException(0, "expecting numeric literal, got '" + argument + "'");
      
      result = true;
    } else { // SQWRL set operator
      String setID = getSetIDInSingleSetOperation(arguments, 1, 2); // Does argument checking
      Set<BuiltInArgument> set = getSet(setID); // Check setID validity
      
      if (set.isEmpty()) result = false;
      else {
    	OWLDataValue minValue = null;
        
    	for (BuiltInArgument element : set) {
          checkThatElementIsComparable(element);
          OWLDataValue value = (OWLDataValue)element;
          
          if (minValue == null) minValue = value;
          else if (value.compareTo(minValue) < 0) minValue = value; // TODO: fix this - can cause difficult-to-find class casts
        } // for
        
        // System.err.println("setMin: set: " + set);
        // System.err.println("setMin: min: " + minValue);

        result = processResultArgument(arguments, 0, minValue);
      } // if
    } // if
    return result;
  } // min

  public boolean max(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (getIsInConsequent()) { // Simple SQWRL aggregation operator
      checkForUnboundArguments(arguments);
      checkNumberOfArgumentsEqualTo(1, arguments.size());
      
      ResultImpl resultImpl = getResult(getInvokingRuleName());
      BuiltInArgument argument = arguments.get(0);
      
      if (!resultImpl.isRowOpen()) resultImpl.openRow();
      
      if (argument instanceof OWLDataValue && ((OWLDataValue)argument).isNumeric()) resultImpl.addRowData((DataValue)argument);
      else throw new InvalidBuiltInArgumentException(0, "expecting numeric literal, got '" + argument + "'");

      result = true;
    } else { // SQWRL set operator
      String setID = getSetIDInSingleSetOperation(arguments, 1, 2); // Does argument checking
      Set<BuiltInArgument> set = getSet(setID); // Checks setID validity
      
      if (set.isEmpty()) result = false;
      else {
        OWLDataValue maxValue = null, value;
        for (BuiltInArgument element : set) {
          checkThatElementIsComparable(element);
          value = (OWLDataValue)element;
          
          if (maxValue == null) maxValue = value;
          else if (value.compareTo(maxValue) > 0) maxValue = value; // TODO: fix this - can cause difficult-to-find class casts
        } // for
        
        result = processResultArgument(arguments, 0, maxValue);
      } // if
    } // if
    return result;
  } // max

  public boolean sum(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (getIsInConsequent()) { // Simple SQWRL aggregation operator
      checkForUnboundArguments(arguments);
      checkNumberOfArgumentsEqualTo(1, arguments.size());
      
      ResultImpl resultImpl = getResult(getInvokingRuleName());
      BuiltInArgument argument = arguments.get(0);
      
      if (!resultImpl.isRowOpen()) resultImpl.openRow();
      
      if (argument instanceof OWLDataValue && ((OWLDataValue)argument).isNumeric()) resultImpl.addRowData((DataValue)argument);
      else throw new InvalidBuiltInArgumentException(0, "expecting numeric literal, got '" + argument + "'");
      
      result = true;
    } else { // SQWRL set operator
      String setID = getSetIDInSingleSetOperation(arguments, 1, 2); // Does argument checking
      Set<BuiltInArgument> set = getSet(setID); // Checks setID validity
      
      if (set.isEmpty()) result = false;
      else {
        double sumValue = 0, value;
        for (BuiltInArgument element : set) {
          checkThatElementIsComparable(element);
          value = getArgumentAsADouble(element);
          sumValue += value;
        } // for
        
        result = processResultArgument(arguments, 0, sumValue);
      } // if
    } // if

    return result;
  } // sum

  public boolean avg(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (getIsInConsequent()) { // Simple SQWRL aggregation operator
      checkForUnboundArguments(arguments);
      checkNumberOfArgumentsEqualTo(1, arguments.size());
      
      ResultImpl resultImpl = getResult(getInvokingRuleName());
      Argument argument = arguments.get(0);
      
      if (!resultImpl.isRowOpen()) resultImpl.openRow();
      
      if (argument instanceof OWLDataValue && ((OWLDataValue)argument).isNumeric()) resultImpl.addRowData((DataValue)argument);
      else throw new InvalidBuiltInArgumentException(0, "expecting numeric literal, got '" + argument + "'");
    } else { // SQWRL set operator
      String setID = getSetIDInSingleSetOperation(arguments, 1, 2); // Does argument checking
      Set<BuiltInArgument> set = getSet(setID); // Checks setID validity
      
      if (set.isEmpty()) result = false;
      else {
        double avgValue, sumValue = 0, value;
        for (BuiltInArgument element : set) {
          checkThatElementIsComparable(element);
          value = getArgumentAsADouble(element);
          sumValue += value;
        } // for
        avgValue = sumValue / set.size();
        
        result = processResultArgument(arguments, 0, avgValue);
      } // if
    } // if

    return result;
  } // avg

  public boolean median(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
    boolean result = false;;

    if (getIsInConsequent()) { // Simple SQWRL aggregation operator
      throw new BuiltInException("not implemented");
    } else { // SQWRL set operator
      String setID = getSetIDInSingleSetOperation(arguments, 1, 2); // Does argument checking
      Set<BuiltInArgument> set = getSet(setID); // Checks setID validity
      
      if (set.isEmpty()) result = false;
      else {
        double[] valueArray = new double[set.size()];
        int count = 0, middle = set.size() / 2;
        double medianValue, value;

        for (BuiltInArgument element : set) {
          checkThatElementIsComparable(element);
          value = getArgumentAsADouble(element);
          valueArray[count++] = value;
        } // for
        
        Arrays.sort(valueArray);

        if (set.size() % 2 == 1) medianValue = valueArray[middle];
        else medianValue = (valueArray[middle - 1] + valueArray[middle]) / 2;
        
        result = processResultArgument(arguments, 0, medianValue);
      } // if
    } // if

    return result;
  } // median

    public boolean intersects(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
    String setName1 = getSetName(arguments, 0); 
    String setName2 = getSetName(arguments, 1);
    int set1NumberOfGroupElements = getSetNumberOfGroupElements(setName1);
    int set2NumberOfGroupElements = getSetNumberOfGroupElements(setName2);
    String setID1 = getSetIDInMultiSetOperation(arguments, 0, 2, 0, set1NumberOfGroupElements); // Does argument checking
    String setID2 = getSetIDInMultiSetOperation(arguments, 1, 2, set1NumberOfGroupElements, set2NumberOfGroupElements); // Does argument checking
    Set<BuiltInArgument> set1 = getSet(setID1);
    Set<BuiltInArgument> set2 = getSet(setID2);
    
    checkIfInAntecedent();

    for (BuiltInArgument element : set1) if (set2.contains(element)) return true;

    return false;
  } // intersects
  
  public boolean notIntersects(List<BuiltInArgument> arguments) throws BuiltInException
  { 
	  return !intersects(arguments);
  } // notIntersects
  
  public boolean intersection(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
	  String setName1 = getSetName(arguments, 1); 
	  String setName2 = getSetName(arguments, 2);
	    int set1NumberOfGroupElements = getSetNumberOfGroupElements(setName1);
	    int set2NumberOfGroupElements = getSetNumberOfGroupElements(setName2);
	    int setResultNumberOfGroupElements = set1NumberOfGroupElements + set2NumberOfGroupElements;
	    String setIDResult = getSetIDInMultiSetOperation(arguments, 0, 3, 0, setResultNumberOfGroupElements); // Does argument checking
	    String setID1 = getSetIDInMultiSetOperation(arguments, 1, 3, 0, set1NumberOfGroupElements); // Does argument checking
	    String setID2 = getSetIDInMultiSetOperation(arguments, 2, 3, 0 + set1NumberOfGroupElements, set2NumberOfGroupElements); // Does argument checking
	    Set<BuiltInArgument> set1 = getSet(setID1);
	    Set<BuiltInArgument> set2 = getSet(setID2);
	    Set<BuiltInArgument> intersection = new HashSet<BuiltInArgument>(set1);
	    
	    checkIfInAntecedent();
	    
	    intersection.retainAll(set2);

	    if (!sets.containsKey(setIDResult)) sets.put(setIDResult, intersection);

	    if (isUnboundArgument(0, arguments)) arguments.get(0).setBuiltInResult(argumentFactory.createDataValueArgument(setIDResult));

	    return true;
   } // intersection

  public boolean union(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
	  String setName1 = getSetName(arguments, 1); 
	  String setName2 = getSetName(arguments, 2);
	    int set1NumberOfGroupElements = getSetNumberOfGroupElements(setName1);
	    int set2NumberOfGroupElements = getSetNumberOfGroupElements(setName2);
	    int setResultNumberOfGroupElements = set1NumberOfGroupElements + set2NumberOfGroupElements;
	    String setIDResult = getSetIDInMultiSetOperation(arguments, 0, 3, 0, setResultNumberOfGroupElements); // Does argument checking
	    String setID1 = getSetIDInMultiSetOperation(arguments, 1, 3, 0, set1NumberOfGroupElements); // Does argument checking
	    String setID2 = getSetIDInMultiSetOperation(arguments, 2, 3, 0 + set1NumberOfGroupElements, set2NumberOfGroupElements); // Does argument checking
	    Set<BuiltInArgument> set1 = getSet(setID1);
	    Set<BuiltInArgument> set2 = getSet(setID2);
	    Set<BuiltInArgument> union = new HashSet<BuiltInArgument>(set1);
	    
	    checkIfInAntecedent();
	    
	    union.addAll(set2);

	    if (!sets.containsKey(setIDResult)) sets.put(setIDResult, union);

	    if (isUnboundArgument(0, arguments)) arguments.get(0).setBuiltInResult(argumentFactory.createDataValueArgument(setIDResult));

	    return true;
  } // union

  public boolean difference(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
	  String setName1 = getSetName(arguments, 1); 
	  String setName2 = getSetName(arguments, 2);
	  int set1NumberOfGroupElements = getSetNumberOfGroupElements(setName1);
	  int set2NumberOfGroupElements = getSetNumberOfGroupElements(setName2);
	  int setResultNumberOfGroupElements = set1NumberOfGroupElements + set2NumberOfGroupElements;
	  String setIDResult = getSetIDInMultiSetOperation(arguments, 0, 3, 0, setResultNumberOfGroupElements); // Does argument checking
	  String setID1 = getSetIDInMultiSetOperation(arguments, 1, 3, 0, set1NumberOfGroupElements); // Does argument checking
	  String setID2 = getSetIDInMultiSetOperation(arguments, 2, 3, 0 + set1NumberOfGroupElements, set2NumberOfGroupElements); // Does argument checking
	  Set<BuiltInArgument> set1 = getSet(setID1);
	  Set<BuiltInArgument> set2 = getSet(setID2);
	  Set<BuiltInArgument> difference = new HashSet<BuiltInArgument>(set1);
	    
	  checkIfInAntecedent();
	    
	  difference.removeAll(set2);

	  if (!sets.containsKey(setIDResult)) sets.put(setIDResult, difference);

	  if (isUnboundArgument(0, arguments)) arguments.get(0).setBuiltInResult(argumentFactory.createDataValueArgument(setIDResult));

	  return true;
  } // difference

  public boolean contains(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
    String setID = getSetIDInSingleSetOperation(arguments, 0, 2); // Does argument checking
    BuiltInArgument element = arguments.get(1);
        // System.err.println("sqwrl.contains: arguments: " + arguments);
    
    checkIfInAntecedent();
    
    return getSet(setID).contains(element);
  } // contains

  public boolean notContains(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
	checkIfInAntecedent();
	
    return !contains(arguments);
  } // notContains

  public boolean greatest(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
    String setID = getSetIDInSingleSetOperation(arguments, 1, 2); // Does argument checking
    Set<BuiltInArgument> set = getSet(setID);
    boolean result = false;

    checkIfInAntecedent();
    
    if (!set.isEmpty()) {
      SortedSet<BuiltInArgument> sortedSet = new TreeSet<BuiltInArgument>(set);
      BuiltInArgument greatest = sortedSet.last();
      result = processResultArgument(arguments, 0, greatest);
    } // if

    return result;
  } // greatest

  public boolean greatestN(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
    String setID = getSetIDInSingleSetOperation(arguments, 1, 3); // Does argument checking
    int n = getArgumentAsAPositiveInteger(2, arguments);
    Set<BuiltInArgument> set = getSet(setID);
    boolean result = false;

    checkIfInAntecedent();
    
    if (!set.isEmpty()) {
      SortedSet<BuiltInArgument> sortedSet = new TreeSet<BuiltInArgument>(set);
      BuiltInArgument array[] = (BuiltInArgument[])sortedSet.toArray(new BuiltInArgument[sortedSet.size()]);
      Set<BuiltInArgument> greatestNSet = new HashSet<BuiltInArgument>();

      for (int i = set.size() - 1; i >= set.size() - n && i >= 0; i--) greatestNSet.add(array[i]);

      result = processResultArgument(arguments, 0, greatestNSet);
    } // if

    return result;
  } // greatestN
 
  public boolean notGreatestN(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
    String setID = getSetIDInSingleSetOperation(arguments, 1, 3); // Does argument checking
    int n = getArgumentAsAPositiveInteger(2, arguments);
    Set<BuiltInArgument> set = getSet(setID);
    boolean result = false;

    checkIfInAntecedent();
    
    if (!set.isEmpty()) {
      SortedSet<BuiltInArgument> sortedSet = new TreeSet<BuiltInArgument>(set);
      BuiltInArgument array[] = (BuiltInArgument[])sortedSet.toArray(new BuiltInArgument[sortedSet.size()]);
      Set<BuiltInArgument> notGreatestNSet = new HashSet<BuiltInArgument>();

      for (int i = 0; i < set.size() - n; i++) notGreatestNSet.add(array[i]);

      result = processResultArgument(arguments, 0, notGreatestNSet);
    } // if

    return result;
  } // notGreatestN

  public boolean leastN(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
    String setID = getSetIDInSingleSetOperation(arguments, 1, 3); // Does argument checking
    int n = getArgumentAsAPositiveInteger(2, arguments);
    Set<BuiltInArgument> set = getSet(setID);
    boolean result = false;
    
    checkIfInAntecedent();

    if (!set.isEmpty()) {
      SortedSet<BuiltInArgument> sortedSet = new TreeSet<BuiltInArgument>(set);
      BuiltInArgument array[] = (BuiltInArgument[])sortedSet.toArray(new BuiltInArgument[sortedSet.size()]);
      Set<BuiltInArgument> leastNSet = new HashSet<BuiltInArgument>();

      for (int i = 0; i < n && i < set.size(); i++) leastNSet.add(array[i]);
      
      result = processResultArgument(arguments, 0, leastNSet);
    } // if

    return result;
  } // leastN

  public boolean notLeastN(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
    String setID = getSetIDInSingleSetOperation(arguments, 1, 3); // Does argument checking
    int n = getArgumentAsAPositiveInteger(2, arguments);
    Set<BuiltInArgument> set = getSet(setID);
    boolean result = false;

    checkIfInAntecedent();
      
    if (!set.isEmpty()) {
      SortedSet<BuiltInArgument> sortedSet = new TreeSet<BuiltInArgument>(set);
      BuiltInArgument array[] = (BuiltInArgument[])sortedSet.toArray(new BuiltInArgument[sortedSet.size()]);
      Set<BuiltInArgument> notLeastNSet = new HashSet<BuiltInArgument>();

      for (int i = n - 1; i < set.size(); i++) notLeastNSet.add(array[i]);
      
      result = processResultArgument(arguments, 0, notLeastNSet);
    } // if

    return result;
  } // leastN

  public boolean least(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
    String setID = getSetIDInSingleSetOperation(arguments, 1, 2); // Does argument checking
    Set<BuiltInArgument> set = getSet(setID);
    boolean result = false;

    checkIfInAntecedent();
    
    if (!set.isEmpty()) {
      SortedSet<BuiltInArgument> sortedSet = new TreeSet<BuiltInArgument>(set);
      BuiltInArgument least = sortedSet.first();

      result = processResultArgument(arguments, 0, least);
    } // if

    return result;
  } // least

  // Internal methods

  private boolean isSet(String setID) { return sets.containsKey(setID); }

  private String getSetName(List<BuiltInArgument> arguments, int setArgumentNumber) throws BuiltInException
  {
    String ruleName = getInvokingRuleName();
    String setName = getVariableName(setArgumentNumber, arguments); 
    return ruleName + ":" + setName;
  } // getSetName

  private int getSetNumberOfGroupElements(String setName) throws BuiltInException
  {
    if (!setGroupElementNumbersMap.containsKey(setName)) 
      throw new BuiltInException("internal error: invalid set name " + setName + "; no group element number found");

    return setGroupElementNumbersMap.get(setName);
  } // getSetNumberOfGroupElements

  private String getSetIDInMake(List<BuiltInArgument> arguments) throws BuiltInException
  {
	checkNumberOfArgumentsAtLeast(2, arguments.size());
	
    String ruleName = getInvokingRuleName();
    String setName = getSetName(arguments, 0); // Always the first argument for a set
    int numberOfGroupArguments = arguments.size() - 2;
    boolean hasGroupPattern  = numberOfGroupArguments != 0;
    String groupPattern = !hasGroupPattern ? "" : createInvocationPattern(getInvokingBridge(), ruleName, 0, false,
                                                                                          arguments.subList(2, arguments.size()));
    
    if (isBoundArgument(0, arguments) && !setGroupElementNumbersMap.containsKey(setName)) // Set variable already used in non set context  
    	throw new BuiltInException("set variable ?" + arguments.get(0).getVariableName() + " already used in non set context");
    
    if (hasGroupPattern) {
    	if (!setGroupElementNumbersMap.containsKey(setName)) setGroupElementNumbersMap.put(setName, numberOfGroupArguments);
    	else if (setGroupElementNumbersMap.get(setName) != numberOfGroupArguments) {
    		throw new BuiltInException("internal error: inconsistent number of group elements for set " + setName);
    	} //if
    } else {
    	if (setGroupElementNumbersMap.containsKey(setName)) {
    		if (setGroupElementNumbersMap.get(setName) != 0) {
        		throw new BuiltInException("internal error: inconsistent number of group elements for set " + setName);
    		}
    	} else setGroupElementNumbersMap.put(setName, 0);
    } // if
	                           
    return setName + ":" + groupPattern;
  } // getSetIDInMake

  private String getSetIDInSingleSetOperation(List<BuiltInArgument> arguments, int setArgumentIndex, int numberOfCoreArguments) 
    throws BuiltInException
  {
    String ruleName = getInvokingRuleName();
    String setName = ruleName + ":" + getVariableName(setArgumentIndex, arguments);
    boolean hasGroupPattern  = (arguments.size() > numberOfCoreArguments);
    String groupPattern = "";

    if (hasGroupPattern) {
    	groupPattern = createInvocationPattern(getInvokingBridge(), ruleName, 0, false, 
    			                                               arguments.subList(numberOfCoreArguments, arguments.size()));
    } // if

    return setName + ":" + groupPattern;
  } // getSetIDInSingleSetOperation

  private String getSetIDInMultiSetOperation(List<BuiltInArgument> arguments, int setArgumentNumber, 
                                             int coreArgumentNumber, int groupArgumentOffset, int numberOfRelevantGroupArguments) 
   throws BuiltInException
  {
    String ruleName = getInvokingRuleName();
    String setName = getSetName(arguments, setArgumentNumber);
    String groupPattern = "";
    
    if (setGroupElementNumbersMap.containsKey(setName) && getSetNumberOfGroupElements(setName) != 0)
      groupPattern = createInvocationPattern(getInvokingBridge(), ruleName, 0, false, 
                                             arguments.subList(coreArgumentNumber + groupArgumentOffset, 
                                             coreArgumentNumber + groupArgumentOffset + numberOfRelevantGroupArguments)); 

    return setName + ":" + groupPattern;
  } // getCollectionIDInOperation

  
  private ResultImpl getResult(String queryName) throws BuiltInException
  {
    return getInvokingBridge().getSWRLRule(queryName).getSQWRLResult();
  } // getResult
  
  private void checkThatElementIsComparable(BuiltInArgument element) throws BuiltInException
  {
    if (!(element instanceof DataValue) || !((DataValue)element).isComparable())
      throw new BuiltInException("may only be applied to sets with comparable elements");
  } // checkThatElementIsNumeric

  private Set<BuiltInArgument> getSet(String setID) throws BuiltInException
  {
    if (!isSet(setID)) throw new BuiltInException("internal error: no set found for ID " + setID);
    return sets.get(setID);
  } // getSet

} // SWRLBuiltInLibraryImpl
