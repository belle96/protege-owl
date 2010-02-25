
// TODO: a lot of cut-and-paste repetition here needs to be fixed.
// TODO: replace operators min, max with least, greatest?
// TODO: need to optimize sorted sets so that they are not resorted unnecessarily

package edu.stanford.smi.protegex.owl.swrl.bridge.builtins.sqwrl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import edu.stanford.smi.protegex.owl.swrl.bridge.Argument;
import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.ClassArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.DataPropertyArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.DataValueArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.IndividualArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.ObjectPropertyArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.builtins.AbstractSWRLBuiltInLibrary;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.BuiltInException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.InvalidBuiltInArgumentException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.ClassValue;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.DataPropertyValue;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.DataValue;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.IndividualValue;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.ObjectPropertyValue;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.SQWRLNames;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.SQWRLResultValueFactory;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.impl.ResultImpl;

/*
 * Implementation library for SQWRL built-ins. See <a href="http://protege.cim3.net/cgi-bin/wiki.pl?SQWRL">here</a> for documentation
 * on this built-in library.
 */
public class SWRLBuiltInLibraryImpl extends AbstractSWRLBuiltInLibrary
{
  private Map<String, Collection<BuiltInArgument>> collections;
  private Map<String, Integer> collectionGroupElementNumbersMap; // Set name to number of elements in group (which may be 0)
  private SQWRLResultValueFactory resultValueFactory;
  
  public SWRLBuiltInLibraryImpl() 
  { 
  	super(SQWRLNames.SQWRLBuiltInLibraryName);
  	
  	resultValueFactory = new SQWRLResultValueFactory();
  }
  
  public void reset()
  {
    collections = new HashMap<String, Collection<BuiltInArgument>>();
    collectionGroupElementNumbersMap = new HashMap<String, Integer>(); 
  } // reset
  
  public boolean select(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkForUnboundArguments(arguments);
    checkNumberOfArgumentsAtLeast(1, arguments.size());
    checkThatInConsequent();
    ResultImpl result = getResult(getInvokingRuleName());

    if (!result.isRowOpen()) result.openRow();

    int argumentIndex = 0;
    for (BuiltInArgument argument : arguments) {
      if (argument instanceof DataValueArgument) {
      	DataValue dataValue = ((DataValueArgument)argument).getDataValue();
      	result.addRowData(dataValue);
      } else if (argument instanceof IndividualArgument) {
      	IndividualArgument individualArgument = (IndividualArgument)argument;
      	IndividualValue individualValue =  resultValueFactory.createIndividualValue(individualArgument.getURI());
      	result.addRowData(individualValue);
      } else if (argument instanceof ClassArgument) {
      	ClassArgument classArgument = (ClassArgument)argument;
      	ClassValue classValue =  resultValueFactory.createClassValue(classArgument.getURI());
      	result.addRowData(classValue);
      } else if (argument instanceof ObjectPropertyArgument) { 
      	ObjectPropertyArgument objectPropertyArgument = (ObjectPropertyArgument)argument;
      	ObjectPropertyValue objectPropertyValue =  resultValueFactory.createObjectPropertyValue(objectPropertyArgument.getURI());
      	result.addRowData(objectPropertyValue); 
      } else if (argument instanceof DataPropertyArgument) { 
       	DataPropertyArgument dataPropertyArgument = (DataPropertyArgument)argument;
       	DataPropertyValue dataPropertyValue =  resultValueFactory.createDataPropertyValue(dataPropertyArgument.getURI());
       	result.addRowData(dataPropertyValue);
      } else throw new InvalidBuiltInArgumentException(argumentIndex, "unknown type " + argument.getClass());
      argumentIndex++;
    } // for
    
    return false;
  } // select

  public boolean selectDistinct(List<BuiltInArgument> arguments) throws BuiltInException
  {
	  checkThatInConsequent();
    return select(arguments);
  } // selectDistinct
  
  public boolean count(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkForUnboundArguments(arguments);
    checkNumberOfArgumentsEqualTo(1, arguments.size());

    ResultImpl result = getResult(getInvokingRuleName());
    BuiltInArgument argument = arguments.get(0);

    if (!result.isRowOpen()) result.openRow();
    
    if (argument instanceof DataValueArgument) {
    	DataValue dataValue = ((DataValueArgument)argument).getDataValue();
    	result.addRowData(dataValue);
    } else if (argument instanceof IndividualArgument) {
    	IndividualArgument individualArgument = (IndividualArgument)argument;
    	IndividualValue individualValue =  resultValueFactory.createIndividualValue(individualArgument.getURI());
    	result.addRowData(individualValue);
    } else if (argument instanceof ClassArgument) {
    	ClassArgument classArgument = (ClassArgument)argument;
    	ClassValue classValue =  resultValueFactory.createClassValue(classArgument.getURI());
    	result.addRowData(classValue);
    } else if (argument instanceof ObjectPropertyArgument) { 
    	ObjectPropertyArgument objectPropertyArgument = (ObjectPropertyArgument)argument;
    	ObjectPropertyValue objectPropertyValue =  resultValueFactory.createObjectPropertyValue(objectPropertyArgument.getURI());
    	result.addRowData(objectPropertyValue); 
    } else if (argument instanceof DataPropertyArgument) { 
     	DataPropertyArgument dataPropertyArgument = (DataPropertyArgument)argument;
     	DataPropertyValue dataPropertyValue =  resultValueFactory.createDataPropertyValue(dataPropertyArgument.getURI());
     	result.addRowData(dataPropertyValue);
    } else throw new InvalidBuiltInArgumentException(0, "unknown type " + argument.getClass());
    
    return false;
  } // count

  public boolean countDistinct(List<BuiltInArgument> arguments) throws BuiltInException
  {
	checkThatInConsequent();
    return count(arguments);
  } // countDistinct

  // The use of columnNames, orderBy, orderByDescending is handled at initial processing in the SWRLRule object.  
  public boolean columnNames(List<BuiltInArgument> arguments) throws BuiltInException { return false; } 
  public boolean orderBy(List<BuiltInArgument> arguments) throws BuiltInException { return false; } 
  public boolean orderByDescending(List<BuiltInArgument> arguments) throws BuiltInException { return false; }

  public boolean makeSet(List<BuiltInArgument> arguments) throws BuiltInException
  {
	  String collectionID = getCollectionIDInMake(arguments); // Get unique ID for set; does argument checking
	  BuiltInArgument element = arguments.get(1); // The second argument is always the value
	  Collection<BuiltInArgument> set;
	
	  checkThatInAntecedent();
	    
    if (collections.containsKey(collectionID)) set = collections.get(collectionID);
    else {  
      set = new HashSet<BuiltInArgument>(); collections.put(collectionID, set); 
      //System.err.println("creating set with id: " + collectionID);
    } // if

    set.add(element);
    //System.err.println("adding element " + element + " to set " + collectionID);

    if (isUnboundArgument(0, arguments)) arguments.get(0).setBuiltInResult(createDataValueArgument(collectionID));

    return true;
  } // makeSet

  public boolean makeBag(List<BuiltInArgument> arguments) throws BuiltInException
  {
	String collectionID = getCollectionIDInMake(arguments); // Get unique ID for bag; does argument checking
	BuiltInArgument element = arguments.get(1); // The second argument is always the value
	Collection<BuiltInArgument> bag;
	
	checkThatInAntecedent();
	    
    if (collections.containsKey(collectionID)) bag = collections.get(collectionID);
    else {  
      bag = new ArrayList<BuiltInArgument>(); collections.put(collectionID, bag); 
    } // if

    bag.add(element);
    // System.err.println("adding element " + element + " to bag " + collectionID);

    if (isUnboundArgument(0, arguments)) 
    	arguments.get(0).setBuiltInResult(createDataValueArgument(collectionID));

    return true;
  } // makeBag

  public boolean groupBy(List<BuiltInArgument> arguments) throws BuiltInException
  {
	checkThatInAntecedent();
	
	return true; // Should never be invoked - is a directive only and is processed by SQWRL processor
  } // groupBy

  public boolean isEmpty(List<BuiltInArgument> arguments) throws BuiltInException
  {
    String collectionID = getCollectionIDInSingleCollectionOperation(arguments, 0, 1); // Does argument checking
    
    checkThatInAntecedent();
    
    // System.err.println("isEmpty: arguments: " + arguments);
    // System.err.println("isEmpty: collectionID: " + collectionID);

    return getCollection(collectionID).size() == 0;    
   } // isEmpty

  public boolean notEmpty(List<BuiltInArgument> arguments) throws BuiltInException
  {
    return !isEmpty(arguments);
  } // notEmpty

  public boolean size(List<BuiltInArgument> arguments) throws BuiltInException
  {
    String collectionID = getCollectionIDInSingleCollectionOperation(arguments, 1, 2); // Does argument checking
    int size = getCollection(collectionID).size(); // Checks collection ID validity
    
    checkThatInAntecedent();

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
      
      if (argument instanceof DataValueArgument && ((DataValueArgument)argument).getDataValue().isNumeric()) {
      	DataValue dataValue = ((DataValueArgument)argument).getDataValue();
      	resultImpl.addRowData(dataValue);
      } else throw new InvalidBuiltInArgumentException(0, "expecting numeric literal, got " + argument);
      
      result = true;
    } else result = least(arguments); // SQWRL collection operator
    
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
      
      if (argument instanceof DataValueArgument && ((DataValueArgument)argument).getDataValue().isNumeric()) {
      	DataValue dataValue = ((DataValueArgument)argument).getDataValue();
      	resultImpl.addRowData(dataValue);
      } else throw new InvalidBuiltInArgumentException(0, "expecting numeric literal, got: " + argument);

      result = true;
    } else result = greatest(arguments); // SQWRL collection operator
     
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
      
      if (argument instanceof DataValueArgument && ((DataValueArgument)argument).getDataValue().isNumeric()) {
      	DataValue dataValue = ((DataValueArgument)argument).getDataValue();
      	resultImpl.addRowData(dataValue);
      } else throw new InvalidBuiltInArgumentException(0, "expecting numeric literal, got: " + argument);
      
      result = true;
    } else { // SQWRL collection operator
      String collectionID = getCollectionIDInSingleCollectionOperation(arguments, 1, 2); // Does argument checking
      Collection<BuiltInArgument> collection = getCollection(collectionID); // Checks collectionID validity
      
      if (collection.isEmpty()) result = false;
      else {
        double sumValue = 0, value;
        for (BuiltInArgument element : collection) {
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
      
      if (argument instanceof DataValueArgument && ((DataValueArgument)argument).getDataValue().isNumeric()) {
      	DataValue dataValue = ((DataValueArgument)argument).getDataValue();
      	resultImpl.addRowData(dataValue);
      } else throw new InvalidBuiltInArgumentException(0, "expecting numeric literal, got: " + argument);
    } else { // SQWRL collection operator
      String collectionID = getCollectionIDInSingleCollectionOperation(arguments, 1, 2); // Does argument checking
      Collection<BuiltInArgument> collection = getCollection(collectionID); // Checks collectionID validity
      
      if (collection.isEmpty()) result = false;
      else {
        double avgValue, sumValue = 0, value;
        for (BuiltInArgument element : collection) {
          checkThatElementIsComparable(element);
          value = getArgumentAsADouble(element);
          sumValue += value;
        } // for
        avgValue = sumValue / collection.size();
        
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
    } else { // SQWRL collection operator
      String collectionID = getCollectionIDInSingleCollectionOperation(arguments, 1, 2); // Does argument checking
      Collection<BuiltInArgument> collection = getCollection(collectionID); // Checks collectionID validity
      
      if (collection.isEmpty()) result = false;
      else {
        double[] valueArray = new double[collection.size()];
        int count = 0, middle = collection.size() / 2;
        double medianValue, value;

        for (BuiltInArgument element : collection) {
          checkThatElementIsComparable(element);
          value = getArgumentAsADouble(element);
          valueArray[count++] = value;
        } // for
        
        Arrays.sort(valueArray);

        if (collection.size() % 2 == 1) medianValue = valueArray[middle];
        else medianValue = (valueArray[middle - 1] + valueArray[middle]) / 2;
        
        result = processResultArgument(arguments, 0, medianValue);
      } // if
    } // if

    return result;
  } // median

  public boolean intersects(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
    String setName1 = getCollectionName(arguments, 0); 
    String setName2 = getCollectionName(arguments, 1);
    int set1NumberOfGroupElements = getCollectionNumberOfGroupElements(setName1);
    int set2NumberOfGroupElements = getCollectionNumberOfGroupElements(setName2);
    String collectionID1 = getCollectionIDInMultiCollectionOperation(arguments, 0, 2, 0, set1NumberOfGroupElements); // Does argument checking
    String collectionID2 = getCollectionIDInMultiCollectionOperation(arguments, 1, 2, set1NumberOfGroupElements, set2NumberOfGroupElements); // Does argument checking
    Collection<BuiltInArgument> collection1 = getCollection(collectionID1);
    Collection<BuiltInArgument> collection2 = getCollection(collectionID2);
    
    checkThatInAntecedent();

    for (BuiltInArgument element : collection1) if (collection2.contains(element)) return true;

    return false;
  } // intersects
  
  public boolean notIntersects(List<BuiltInArgument> arguments) throws BuiltInException
  { 
	  return !intersects(arguments);
  } // notIntersects
  
  public boolean intersection(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
	  String setName1 = getCollectionName(arguments, 1); 
	  String setName2 = getCollectionName(arguments, 2);
	    int set1NumberOfGroupElements = getCollectionNumberOfGroupElements(setName1);
	    int set2NumberOfGroupElements = getCollectionNumberOfGroupElements(setName2);
	    int setResultNumberOfGroupElements = set1NumberOfGroupElements + set2NumberOfGroupElements;
	    String collectionIDResult = getCollectionIDInMultiCollectionOperation(arguments, 0, 3, 0, setResultNumberOfGroupElements); // Does argument checking
	    String collectionID1 = getCollectionIDInMultiCollectionOperation(arguments, 1, 3, 0, set1NumberOfGroupElements); // Does argument checking
	    String collectionID2 = getCollectionIDInMultiCollectionOperation(arguments, 2, 3, 0 + set1NumberOfGroupElements, set2NumberOfGroupElements); // Does argument checking
	    Collection<BuiltInArgument> collection1 = getCollection(collectionID1);
	    Collection<BuiltInArgument> collection2 = getCollection(collectionID2);
	    Collection<BuiltInArgument> intersection = new HashSet<BuiltInArgument>(collection1);
	    
	    checkThatInAntecedent();
	    
	    intersection.retainAll(collection2);

	    if (!collections.containsKey(collectionIDResult)) collections.put(collectionIDResult, intersection);

	    if (isUnboundArgument(0, arguments)) arguments.get(0).setBuiltInResult(createDataValueArgument(collectionIDResult));

	    return true;
   } // intersection

  public boolean union(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
		String setName1 = getCollectionName(arguments, 1); 
		String setName2 = getCollectionName(arguments, 2);
		int set1NumberOfGroupElements = getCollectionNumberOfGroupElements(setName1);
		int set2NumberOfGroupElements = getCollectionNumberOfGroupElements(setName2);
		int setResultNumberOfGroupElements = set1NumberOfGroupElements + set2NumberOfGroupElements;
		String collectionIDResult = getCollectionIDInMultiCollectionOperation(arguments, 0, 3, 0, setResultNumberOfGroupElements); // Does argument checking
		String collectionID1 = getCollectionIDInMultiCollectionOperation(arguments, 1, 3, 0, set1NumberOfGroupElements); // Does argument checking
		String collectionID2 = getCollectionIDInMultiCollectionOperation(arguments, 2, 3, 0 + set1NumberOfGroupElements, set2NumberOfGroupElements); // Does argument checking
		Collection<BuiltInArgument> collection1 = getCollection(collectionID1);
		Collection<BuiltInArgument> collection2 = getCollection(collectionID2);
		Set<BuiltInArgument> union = new HashSet<BuiltInArgument>(collection1);
		
		checkThatInAntecedent();
		
		union.addAll(collection2);
		
		if (!collections.containsKey(collectionIDResult)) collections.put(collectionIDResult, union);
		
		if (isUnboundArgument(0, arguments)) arguments.get(0).setBuiltInResult(createDataValueArgument(collectionIDResult));
		
		return true;
	} // union

  public boolean difference(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
		String setName1 = getCollectionName(arguments, 1); 
		String setName2 = getCollectionName(arguments, 2);
		int set1NumberOfGroupElements = getCollectionNumberOfGroupElements(setName1);
		int set2NumberOfGroupElements = getCollectionNumberOfGroupElements(setName2);
		int setResultNumberOfGroupElements = set1NumberOfGroupElements + set2NumberOfGroupElements;
		String collectionIDResult = getCollectionIDInMultiCollectionOperation(arguments, 0, 3, 0, setResultNumberOfGroupElements); // Does argument checking
		String collectionID1 = getCollectionIDInMultiCollectionOperation(arguments, 1, 3, 0, set1NumberOfGroupElements); // Does argument checking
		String collectionID2 = getCollectionIDInMultiCollectionOperation(arguments, 2, 3, 0 + set1NumberOfGroupElements, set2NumberOfGroupElements); // Does argument checking
		Collection<BuiltInArgument> collection1 = getCollection(collectionID1);
		Collection<BuiltInArgument> collection2 = getCollection(collectionID2);
		Collection<BuiltInArgument> difference = new HashSet<BuiltInArgument>(collection1);
		
		checkThatInAntecedent();
		
		difference.removeAll(collection2);
	
		if (!collections.containsKey(collectionIDResult)) collections.put(collectionIDResult, difference);
		
		if (isUnboundArgument(0, arguments)) arguments.get(0).setBuiltInResult(createDataValueArgument(collectionIDResult));
		
		return true;
  } // difference

  public boolean contains(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
    String collectionID = getCollectionIDInSingleCollectionOperation(arguments, 0, 2); // Does argument checking
    
    checkThatInAntecedent();
    
    return processResultArgument(arguments, 1, getCollection(collectionID));
  } // contains

  public boolean notContains(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
	  checkThatInAntecedent();
	
    return !contains(arguments);
  } // notContains

  public boolean nth(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
    String collectionID = getCollectionIDInSingleCollectionOperation(arguments, 1, 3); // Does argument checking
    int n = getArgumentAsAPositiveInteger(2, arguments) - 1; // 1-offset for user, 0 for processing
    Collection<BuiltInArgument> collection = getCollection(collectionID);
    boolean result = false;

    checkThatInAntecedent();
    
    if (!collection.isEmpty()) {
      SortedSet<BuiltInArgument> sortedSet = new TreeSet<BuiltInArgument>(collection);
      BuiltInArgument array[] = (BuiltInArgument[])sortedSet.toArray(new BuiltInArgument[sortedSet.size()]);
      
      if (n >= 0 && n < array.length) {
        BuiltInArgument nth = array[n];
        result = processResultArgument(arguments, 0, nth);
      } else result = false;
    } // if

    return result;
  } // nth

  public boolean notNth(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
    String collectionID = getCollectionIDInSingleCollectionOperation(arguments, 1, 3); // Does argument checking
    int n = getArgumentAsAPositiveInteger(2, arguments) - 1;  // 1-offset for user, 0 for processing
    Collection<BuiltInArgument> collection = getCollection(collectionID);
    boolean result = false;

    checkThatInAntecedent();
    
    if (!collection.isEmpty()) {
      SortedSet<BuiltInArgument> sortedSet = new TreeSet<BuiltInArgument>(collection);
      BuiltInArgument array[] = (BuiltInArgument[])sortedSet.toArray(new BuiltInArgument[sortedSet.size()]);

      if (n >= 0 && n < array.length) {
        BuiltInArgument nth = array[n];
        sortedSet.remove(nth);
        result = processResultArgument(arguments, 0, sortedSet);
      } else result = false;
    } // if

    return result;
  } // notNth

  public boolean last(List<BuiltInArgument> arguments) throws BuiltInException { return greatest(arguments); }
  public boolean notLast(List<BuiltInArgument> arguments) throws BuiltInException { return notGreatest(arguments); }
  public boolean lastN(List<BuiltInArgument> arguments) throws BuiltInException { return greatestN(arguments); }
  public boolean notLastN(List<BuiltInArgument> arguments) throws BuiltInException { return notGreatestN(arguments); }
  public boolean first(List<BuiltInArgument> arguments) throws BuiltInException { return least(arguments); }
  public boolean notFirst(List<BuiltInArgument> arguments) throws BuiltInException { return notLeast(arguments); }
  public boolean firstN(List<BuiltInArgument> arguments) throws BuiltInException { return leastN(arguments); }
  public boolean notFirstN(List<BuiltInArgument> arguments) throws BuiltInException { return notLeastN(arguments); }
  
  public boolean greatest(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
    String collectionID = getCollectionIDInSingleCollectionOperation(arguments, 1, 2); // Does argument checking
    Collection<BuiltInArgument> collection = getCollection(collectionID);
    boolean result = false;
    
    if (!collection.isEmpty()) {
      SortedSet<BuiltInArgument> sortedSet = new TreeSet<BuiltInArgument>(collection);
      BuiltInArgument greatest = sortedSet.last();
      result = processResultArgument(arguments, 0, greatest);
    } // if

    return result;
  } // greatest

  public boolean notGreatest(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
    String collectionID = getCollectionIDInSingleCollectionOperation(arguments, 1, 2); // Does argument checking
    Collection<BuiltInArgument> collection = getCollection(collectionID);
    boolean result = false;
    
    if (!collection.isEmpty()) {
      SortedSet<BuiltInArgument> sortedSet = new TreeSet<BuiltInArgument>(collection);
      BuiltInArgument greatest = sortedSet.last();
      sortedSet.remove(greatest);
      result = processResultArgument(arguments, 0, sortedSet);
    } // if

    return result;
  } // notGreatest

  public boolean greatestN(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
    String collectionID = getCollectionIDInSingleCollectionOperation(arguments, 1, 3); // Does argument checking
    int n = getArgumentAsAPositiveInteger(2, arguments);
    Collection<BuiltInArgument> collection = getCollection(collectionID);
    boolean result = false;

    checkThatInAntecedent();
    
    if (!collection.isEmpty()) {
      SortedSet<BuiltInArgument> sortedSet = new TreeSet<BuiltInArgument>(collection);
      BuiltInArgument array[] = (BuiltInArgument[])sortedSet.toArray(new BuiltInArgument[sortedSet.size()]);
      Set<BuiltInArgument> greatestNSet = new HashSet<BuiltInArgument>();

      for (int i = collection.size() - 1; i >= collection.size() - n && i >= 0; i--) greatestNSet.add(array[i]);

      result = processResultArgument(arguments, 0, greatestNSet);
    } // if

    return result;
  } // greatestN
 
  public boolean notGreatestN(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
    String collectionID = getCollectionIDInSingleCollectionOperation(arguments, 1, 3); // Does argument checking
    int n = getArgumentAsAPositiveInteger(2, arguments);
    Collection<BuiltInArgument> collection = getCollection(collectionID);
    boolean result = false;

    checkThatInAntecedent();
    
    if (!collection.isEmpty()) {
      SortedSet<BuiltInArgument> sortedSet = new TreeSet<BuiltInArgument>(collection);
      BuiltInArgument array[] = (BuiltInArgument[])sortedSet.toArray(new BuiltInArgument[sortedSet.size()]);
      Set<BuiltInArgument> notGreatestNSet = new HashSet<BuiltInArgument>();

      for (int i = 0; i < collection.size() - n; i++) notGreatestNSet.add(array[i]);

      result = processResultArgument(arguments, 0, notGreatestNSet);
    } // if

    return result;
  } // notGreatestN

  public boolean least(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
    String collectionID = getCollectionIDInSingleCollectionOperation(arguments, 1, 2); // Does argument checking
    Collection<BuiltInArgument> collection = getCollection(collectionID);
    boolean result = false;

    checkThatInAntecedent();
    
    if (!collection.isEmpty()) {
      SortedSet<BuiltInArgument> sortedSet = new TreeSet<BuiltInArgument>(collection);
      BuiltInArgument least = sortedSet.first();

      result = processResultArgument(arguments, 0, least);
    } // if

    return result;
  } // least

  public boolean notLeast(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
    String collectionID = getCollectionIDInSingleCollectionOperation(arguments, 1, 2); // Does argument checking
    Collection<BuiltInArgument> collection = getCollection(collectionID);
    boolean result = false;

    checkThatInAntecedent();
    
    if (!collection.isEmpty()) {
      SortedSet<BuiltInArgument> sortedSet = new TreeSet<BuiltInArgument>(collection);
      BuiltInArgument least = sortedSet.first();
      sortedSet.remove(least);

      result = processResultArgument(arguments, 0, sortedSet);
    } // if

    return result;
  } // least

  public boolean leastN(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
    String collectionID = getCollectionIDInSingleCollectionOperation(arguments, 1, 3); // Does argument checking
    int n = getArgumentAsAPositiveInteger(2, arguments);
    Collection<BuiltInArgument> collection = getCollection(collectionID);
    boolean result = false;
    
    checkThatInAntecedent();

    if (!collection.isEmpty()) {
      SortedSet<BuiltInArgument> sortedSet = new TreeSet<BuiltInArgument>(collection);
      BuiltInArgument array[] = (BuiltInArgument[])sortedSet.toArray(new BuiltInArgument[sortedSet.size()]);
      Set<BuiltInArgument> leastNSet = new HashSet<BuiltInArgument>();

      for (int i = 0; i < n && i < collection.size(); i++) leastNSet.add(array[i]);
      
      result = processResultArgument(arguments, 0, leastNSet);
    } // if

    return result;
  } // leastN

  public boolean notLeastN(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
    String collectionID = getCollectionIDInSingleCollectionOperation(arguments, 1, 3); // Does argument checking
    int n = getArgumentAsAPositiveInteger(2, arguments);
    Collection<BuiltInArgument> collection = getCollection(collectionID);
    boolean result = false;

    checkThatInAntecedent();
      
    if (!collection.isEmpty()) {
      SortedSet<BuiltInArgument> sortedSet = new TreeSet<BuiltInArgument>(collection);
      BuiltInArgument array[] = (BuiltInArgument[])sortedSet.toArray(new BuiltInArgument[sortedSet.size()]);
      Set<BuiltInArgument> notLeastNSet = new HashSet<BuiltInArgument>();

      for (int i = n - 1; i < collection.size(); i++) notLeastNSet.add(array[i]);
      
      result = processResultArgument(arguments, 0, notLeastNSet);
    } // if

    return result;
  } // leastN

  // Internal methods

  private boolean isCollection(String collectionID) { return collections.containsKey(collectionID); }

  private String getCollectionName(List<BuiltInArgument> arguments, int setArgumentNumber) throws BuiltInException
  {
    String ruleName = getInvokingRuleName();
    String setName = getVariableName(setArgumentNumber, arguments); 
    return ruleName + ":" + setName;
  } // getSetName

  private int getCollectionNumberOfGroupElements(String collectionName) throws BuiltInException
  {
    if (!collectionGroupElementNumbersMap.containsKey(collectionName)) 
      throw new BuiltInException("internal error: invalid collection name " + collectionName + "; no group element number found");

    return collectionGroupElementNumbersMap.get(collectionName);
  } // getSetNumberOfGroupElements

  private String getCollectionIDInMake(List<BuiltInArgument> arguments) throws BuiltInException
  {
	  checkNumberOfArgumentsAtLeast(2, arguments.size());
	
    String ruleName = getInvokingRuleName();
    String collectionName = getCollectionName(arguments, 0); // Always the first argument for a collection
    int numberOfGroupArguments = arguments.size() - 2;
    boolean hasGroupPattern  = numberOfGroupArguments != 0;
    String groupPattern = !hasGroupPattern ? "" : createInvocationPattern(getInvokingBridge(), ruleName, 0, false,
                                                                          arguments.subList(2, arguments.size()));
    
    if (isBoundArgument(0, arguments) && !collectionGroupElementNumbersMap.containsKey(collectionName)) // Collection variable already used in non collection context  
    	throw new BuiltInException("collection variable ?" + arguments.get(0).getVariableName() + " already used in non collection context");
    
    if (hasGroupPattern) {
    	if (!collectionGroupElementNumbersMap.containsKey(collectionName)) collectionGroupElementNumbersMap.put(collectionName, numberOfGroupArguments);
    	else if (collectionGroupElementNumbersMap.get(collectionName) != numberOfGroupArguments) {
    		throw new BuiltInException("internal error: inconsistent number of group elements for collection " + collectionName);
    	} //if
    } else {
    	if (collectionGroupElementNumbersMap.containsKey(collectionName)) {
    		if (collectionGroupElementNumbersMap.get(collectionName) != 0) {
        		throw new BuiltInException("internal error: inconsistent number of group elements for collection " + collectionName);
    		}
    	} else collectionGroupElementNumbersMap.put(collectionName, 0);
    } // if
	                           
    return collectionName + ":" + groupPattern;
  } // getCollectionIDInMake

  private String getCollectionIDInSingleCollectionOperation(List<BuiltInArgument> arguments, int setArgumentIndex, int numberOfCoreArguments) 
    throws BuiltInException
  {
    String ruleName = getInvokingRuleName();
    String setName = ruleName + ":" + getVariableName(setArgumentIndex, arguments);
    boolean hasGroupPattern  = (arguments.size() > numberOfCoreArguments);
    String groupPattern = "";

    if (hasGroupPattern) {
    	groupPattern = createInvocationPattern(getInvokingBridge(), ruleName, 0, false, arguments.subList(numberOfCoreArguments, arguments.size()));
    } // if

    return setName + ":" + groupPattern;
  } // getCollectionIDInSingleCollectionOperation

  private String getCollectionIDInMultiCollectionOperation(List<BuiltInArgument> arguments, int setArgumentNumber, 
                                             	             int coreArgumentNumber, int groupArgumentOffset, int numberOfRelevantGroupArguments) 
   throws BuiltInException
  {
    String ruleName = getInvokingRuleName();
    String setName = getCollectionName(arguments, setArgumentNumber);
    String groupPattern = "";
    
    if (collectionGroupElementNumbersMap.containsKey(setName) && getCollectionNumberOfGroupElements(setName) != 0)
      groupPattern = createInvocationPattern(getInvokingBridge(), ruleName, 0, false, 
                                             arguments.subList(coreArgumentNumber + groupArgumentOffset, 
                                             coreArgumentNumber + groupArgumentOffset + numberOfRelevantGroupArguments)); 

    return setName + ":" + groupPattern;
  } // getCollectionIDInMultiCollectionOperation
  
  private ResultImpl getResult(String queryName) throws BuiltInException
  {
    return getInvokingBridge().getSWRLRule(queryName).getSQWRLResult();
  } // getResult
  
  private void checkThatElementIsComparable(BuiltInArgument element) throws BuiltInException
  {
    if (!(element instanceof DataValueArgument) || !((DataValueArgument)element).getDataValue().isComparable())
      throw new BuiltInException("may only be applied to sets with comparable elements");
  } // checkThatElementIsNumeric

  private Collection<BuiltInArgument> getCollection(String collectionID) throws BuiltInException
  {
    if (!isCollection(collectionID)) throw new BuiltInException("argument " + collectionID + " does not refer to a collection");
    return collections.get(collectionID);
  } // getCollection 
  
} // SWRLBuiltInLibraryImpl
