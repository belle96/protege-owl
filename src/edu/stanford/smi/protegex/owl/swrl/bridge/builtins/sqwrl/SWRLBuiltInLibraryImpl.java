
// TODO: a lot of cut-and-paste repetition here needs to be fixed.

package edu.stanford.smi.protegex.owl.swrl.bridge.builtins.sqwrl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import edu.stanford.smi.protegex.owl.swrl.sqwrl.impl.SQWRLResultImpl;

/*
 * Implementation library for SQWRL built-ins. See <a href="http://protege.cim3.net/cgi-bin/wiki.pl?SQWRL">here</a> for documentation.
 * 
 * Unlike other built-in libraries, this library needs to be preprocessed. cf. SWRLRuleImpl.java.
 */
public class SWRLBuiltInLibraryImpl extends AbstractSWRLBuiltInLibrary
{
  private Map<String, Collection<BuiltInArgument>> collections;
  private Map<String, Integer> collectionGroupElementNumbersMap; // Collection name to number of elements in group (which will be 0 for ungrouped collections)
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
  	checkThatInConsequent();
    checkForUnboundArguments(arguments);
    checkNumberOfArgumentsAtLeast(1, arguments.size());
    SQWRLResultImpl result = getResult(getInvokingRuleName());

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

  // Preprocessed to signal that duplicates should be removed from result
  public boolean selectDistinct(List<BuiltInArgument> arguments) throws BuiltInException
  {
  	checkThatInConsequent();
  	
    return select(arguments);
  } // selectDistinct
  
  public boolean count(List<BuiltInArgument> arguments) throws BuiltInException
  {
  	checkThatInConsequent();
    checkForUnboundArguments(arguments);
    checkNumberOfArgumentsEqualTo(1, arguments.size());

    SQWRLResultImpl result = getResult(getInvokingRuleName());
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
  
  // Preprocessed to signal that duplicates should be removed from count
  public boolean countDistinct(List<BuiltInArgument> arguments) throws BuiltInException
  {
	  checkThatInConsequent();
    return count(arguments);
  } // countDistinct

  // These built-ins handled at initial processing.  
  public boolean columnNames(List<BuiltInArgument> arguments) throws BuiltInException { return true; } 
  public boolean orderBy(List<BuiltInArgument> arguments) throws BuiltInException { return true; } 
  public boolean orderByDescending(List<BuiltInArgument> arguments) throws BuiltInException { return true; }
  public boolean limit(List<BuiltInArgument> arguments) throws BuiltInException { return true; }

  public boolean makeSet(List<BuiltInArgument> arguments) throws BuiltInException
  {
	  String collectionID = getCollectionIDInMake(arguments); // Get unique ID for set; does argument checking
	  BuiltInArgument element = arguments.get(1); // The second argument is always the value
	  Collection<BuiltInArgument> set;
	
	  checkThatInAntecedent();
	    
    if (collections.containsKey(collectionID)) set = collections.get(collectionID);
    else {  
      set = new HashSet<BuiltInArgument>(); collections.put(collectionID, set); 
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

    if (isUnboundArgument(0, arguments)) arguments.get(0).setBuiltInResult(createDataValueArgument(collectionID));

    return true;
  } // makeBag

  // Preprocesed
  public boolean groupBy(List<BuiltInArgument> arguments) throws BuiltInException
  {
	  checkThatInAntecedent();
	
	  return true;
  } // groupBy

  public boolean isEmpty(List<BuiltInArgument> arguments) throws BuiltInException
  {
    String collectionID = getCollectionIDInSingleCollectionOperation(arguments, 0, 1); // Does argument checking
    
    checkThatInAntecedent();

    return getCollection(collectionID).size() == 0;    
   } // isEmpty

  public boolean notEmpty(List<BuiltInArgument> arguments) throws BuiltInException
  {
    return !isEmpty(arguments);
  } // notEmpty

  public boolean size(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkThatInAntecedent();

    String collectionID = getCollectionIDInSingleCollectionOperation(arguments, 1, 2); // Does argument checking
    int size = getCollection(collectionID).size(); // Checks collection ID validity
    
    return processResultArgument(arguments, 0, size);
  } // size

  public boolean min(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (getIsInConsequent()) { // Simple SQWRL aggregation operator
      checkForUnboundArguments(arguments);
      checkNumberOfArgumentsEqualTo(1, arguments.size());
      
      SQWRLResultImpl resultImpl = getResult(getInvokingRuleName());
      BuiltInArgument argument = arguments.get(0);
      
      if (!resultImpl.isRowOpen()) resultImpl.openRow();
      
      if (argument instanceof DataValueArgument && ((DataValueArgument)argument).getDataValue().isNumeric()) {
      	DataValue dataValue = ((DataValueArgument)argument).getDataValue();
      	resultImpl.addRowData(dataValue);
      } else throw new InvalidBuiltInArgumentException(0, "expecting numeric literal, got " + argument);
      
      result = true;
    } else result = least(arguments); // Redirect to SQWRL collection operator
    
    return result;
  } // min

  public boolean max(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (getIsInConsequent()) { // Simple SQWRL aggregation operator
      checkForUnboundArguments(arguments);
      checkNumberOfArgumentsEqualTo(1, arguments.size());
      
      SQWRLResultImpl resultImpl = getResult(getInvokingRuleName());
      BuiltInArgument argument = arguments.get(0);
      
      if (!resultImpl.isRowOpen()) resultImpl.openRow();
      
      if (argument instanceof DataValueArgument && ((DataValueArgument)argument).getDataValue().isNumeric()) {
      	DataValue dataValue = ((DataValueArgument)argument).getDataValue();
      	resultImpl.addRowData(dataValue);
      } else throw new InvalidBuiltInArgumentException(0, "expecting numeric literal, got: " + argument);

      result = true;
    } else result = greatest(arguments); // Redirect to SQWRL collection operator
     
    return result;
  } // max

  public boolean sum(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (getIsInConsequent()) { // Simple SQWRL aggregation operator
      checkForUnboundArguments(arguments);
      checkNumberOfArgumentsEqualTo(1, arguments.size());
      
      SQWRLResultImpl resultImpl = getResult(getInvokingRuleName());
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
      
      SQWRLResultImpl resultImpl = getResult(getInvokingRuleName());
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
  	String collectionName1 = getCollectionName(arguments, 0); 
    String collectionName2 = getCollectionName(arguments, 1);
    int collection1NumberOfGroupElements = getNumberOfGroupElements(collectionName1);
    int collection2NumberOfGroupElements = getNumberOfGroupElements(collectionName2);
    final int numberOfArguments = 2;
    String collectionID1 = getCollectionIDInMultiCollectionOperation(arguments, 0, numberOfArguments, 0, collection1NumberOfGroupElements); // Does argument checking
    String collectionID2 = getCollectionIDInMultiCollectionOperation(arguments, 1, numberOfArguments, collection1NumberOfGroupElements, collection2NumberOfGroupElements); // Does argument checking
    Collection<BuiltInArgument> collection1 = getCollection(collectionID1);
    Collection<BuiltInArgument> collection2 = getCollection(collectionID2);

    for (BuiltInArgument element : collection1) if (collection2.contains(element)) return true;

    return false;
  } // intersects
  
  public boolean notIntersects(List<BuiltInArgument> arguments) throws BuiltInException
  { 
	  return !intersects(arguments);
  } // notIntersects
  
  public boolean intersection(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
	  String collectionName1 = getCollectionName(arguments, 1); 
	  String collectionName2 = getCollectionName(arguments, 2);
	  int collection1NumberOfGroupElements = getNumberOfGroupElements(collectionName1);
	  int collection2NumberOfGroupElements = getNumberOfGroupElements(collectionName2);
	  int collectionResultNumberOfGroupElements = collection1NumberOfGroupElements + collection2NumberOfGroupElements;
	  final int numberOfArguments = 3;
	  String resultCollectionID = getCollectionIDInMultiCollectionOperation(arguments, 0, numberOfArguments, 0, collectionResultNumberOfGroupElements); // Does argument checking
	  String collectionID1 = getCollectionIDInMultiCollectionOperation(arguments, 1, numberOfArguments, 0, collection1NumberOfGroupElements); // Does argument checking
	  String collectionID2 = getCollectionIDInMultiCollectionOperation(arguments, 2, numberOfArguments, 0 + collection1NumberOfGroupElements, collection2NumberOfGroupElements); // Does argument checking
	  Collection<BuiltInArgument> collection1 = getCollection(collectionID1);
	  Collection<BuiltInArgument> collection2 = getCollection(collectionID2);
	  Collection<BuiltInArgument> intersection = new HashSet<BuiltInArgument>(collection1);
	    
	  intersection.retainAll(collection2);

	  if (!collections.containsKey(resultCollectionID)) collections.put(resultCollectionID, intersection);

	  if (isUnboundArgument(0, arguments)) arguments.get(0).setBuiltInResult(createDataValueArgument(resultCollectionID));

	  return true;
   } // intersection

  public boolean union(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
		String collectionName1 = getCollectionName(arguments, 1); 
		String collectionName2 = getCollectionName(arguments, 2);
		int collection1NumberOfGroupElements = getNumberOfGroupElements(collectionName1);
		int collection2NumberOfGroupElements = getNumberOfGroupElements(collectionName2);
		int resultCollectionNumberOfGroupElements = collection1NumberOfGroupElements + collection2NumberOfGroupElements;
		final int numberOfArguments = 3;
		String collectionID1 = getCollectionIDInMultiCollectionOperation(arguments, 1, numberOfArguments, 0, collection1NumberOfGroupElements); // Does argument checking
		String collectionID2 = getCollectionIDInMultiCollectionOperation(arguments, 2, numberOfArguments, 0 + collection1NumberOfGroupElements, collection2NumberOfGroupElements); // Does argument checking
		String resultCollectionID = getCollectionIDInMultiCollectionOperation(arguments, 0, numberOfArguments, 0, resultCollectionNumberOfGroupElements); // Does argument checking
		Collection<BuiltInArgument> collection1 = getCollection(collectionID1);
		Collection<BuiltInArgument> collection2 = getCollection(collectionID2);
		Set<BuiltInArgument> union = new HashSet<BuiltInArgument>(collection1);
		
		union.addAll(collection2);
		
		if (!collections.containsKey(resultCollectionID)) collections.put(resultCollectionID, union);
		
		if (isUnboundArgument(0, arguments)) arguments.get(0).setBuiltInResult(createDataValueArgument(resultCollectionID));
		
		return true;
	} 

  public boolean difference(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
		String collectionName1 = getCollectionName(arguments, 1); 
		String collectionName2 = getCollectionName(arguments, 2);
		int collection1NumberOfGroupElements = getNumberOfGroupElements(collectionName1);
		int collection2NumberOfGroupElements = getNumberOfGroupElements(collectionName2);
		int collectionResultNumberOfGroupElements = collection1NumberOfGroupElements + collection2NumberOfGroupElements;
		final int numberOfArguments = 3;
		String resultCollectionID = getCollectionIDInMultiCollectionOperation(arguments, 0, numberOfArguments, 0, collectionResultNumberOfGroupElements); // Does argument checking
		String collectionID1 = getCollectionIDInMultiCollectionOperation(arguments, 1, numberOfArguments, 0, collection1NumberOfGroupElements); // Does argument checking
		String collectionID2 = getCollectionIDInMultiCollectionOperation(arguments, 2, numberOfArguments, 0 + collection1NumberOfGroupElements, collection2NumberOfGroupElements); // Does argument checking
		Collection<BuiltInArgument> collection1 = getCollection(collectionID1);
		Collection<BuiltInArgument> collection2 = getCollection(collectionID2);
		Collection<BuiltInArgument> difference = new HashSet<BuiltInArgument>(collection1);
		
		difference.removeAll(collection2);
	
		if (!collections.containsKey(resultCollectionID)) collections.put(resultCollectionID, difference);
		
		if (isUnboundArgument(0, arguments)) arguments.get(0).setBuiltInResult(createDataValueArgument(resultCollectionID));
		
		return true;
  }

  public boolean contains(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
  	final int numberOfArguments = 2;
    String collectionID = getCollectionIDInSingleCollectionOperation(arguments, 0, numberOfArguments); // Does argument checking
    
    return processResultArgument(arguments, 1, getCollection(collectionID));
  }

  public boolean notContains(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
    return !contains(arguments);
  } 

  // Alias definitions
  public boolean nthLast(List<BuiltInArgument> arguments) throws BuiltInException { return nthGreatest(arguments); }
  public boolean notNthLast(List<BuiltInArgument> arguments) throws BuiltInException { return notNthGreatest(arguments); }
  public boolean nthLastSlice(List<BuiltInArgument> arguments) throws BuiltInException { return nthGreatestSlice(arguments); }
  public boolean notNthLastSlice(List<BuiltInArgument> arguments) throws BuiltInException { return notNthGreatestSlice(arguments); }
  public boolean last(List<BuiltInArgument> arguments) throws BuiltInException { return greatest(arguments); }
  public boolean notLast(List<BuiltInArgument> arguments) throws BuiltInException { return notGreatest(arguments); }
  public boolean lastN(List<BuiltInArgument> arguments) throws BuiltInException { return greatestN(arguments); }
  public boolean notLastN(List<BuiltInArgument> arguments) throws BuiltInException { return notGreatestN(arguments); }
  public boolean first(List<BuiltInArgument> arguments) throws BuiltInException { return least(arguments); }
  public boolean notFirst(List<BuiltInArgument> arguments) throws BuiltInException { return notLeast(arguments); }
  public boolean firstN(List<BuiltInArgument> arguments) throws BuiltInException { return leastN(arguments); }
  public boolean notFirstN(List<BuiltInArgument> arguments) throws BuiltInException { return notLeastN(arguments); }

  public boolean nth(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
  	final int numberOfArguments = 3;
    boolean result = false;
  	
  	if (getIsInConsequent()) result = true; // Post processed - ignore
  	else {
  		String collectionID = getCollectionIDInSingleCollectionOperation(arguments, 1, numberOfArguments); // Does argument checking
      int n = getArgumentAsAPositiveInteger(2, arguments) - 1; // 1-offset for user, 0 for processing
      List<BuiltInArgument> sortedCollection = getSortedCollection(collectionID);

      if (!sortedCollection.isEmpty()) {

      	if (n >= 0 && n < sortedCollection.size()) {
      		BuiltInArgument nth = sortedCollection.get(n);
      		result = processResultArgument(arguments, 0, nth);
      	} else result = false;
      } // if
  	} // if

    return result;
  } 
    
  public boolean greatest(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
  	final int resultArgumentNumber = 0, sourceArgumentNumber = 1, numberOfArguments = 2;
    boolean result = false;
  	
  	if (getIsInConsequent()) result = true; // Post processed - ignore
  	else {
      String collectionID = getCollectionIDInSingleCollectionOperation(arguments, sourceArgumentNumber, numberOfArguments); // Does argument checking
      List<BuiltInArgument> sortedCollection = getSortedCollection(collectionID);
    
      if (!sortedCollection.isEmpty()) {
        BuiltInArgument greatest = sortedCollection.get(sortedCollection.size() - 1);
        result = processResultArgument(arguments, resultArgumentNumber, greatest);
      } // if
  	} // if

    return result;
  } 

  public boolean nthGreatest(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
  	final int resultArgumentNumber = 0, sourceArgumentNumber = 1, numberOfArguments = 3;
    boolean result = false;
  	
  	if (getIsInConsequent()) result = true; // Post processed - ignore
  	else {
  		String collectionID = getCollectionIDInSingleCollectionOperation(arguments, sourceArgumentNumber, numberOfArguments); // Does argument checking
      List<BuiltInArgument> sortedCollection = getSortedCollection(collectionID);
      int n = getArgumentAsAPositiveInteger(2, arguments);

      if (!sortedCollection.isEmpty() && n > 0 && n <= sortedCollection.size()) {
      	BuiltInArgument nthGreatest = sortedCollection.get(sortedCollection.size() - n);
      	result = processResultArgument(arguments, resultArgumentNumber, nthGreatest);
      } else result = false;
  	} // if

    return result;
  } 

  public boolean least(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
  	final int resultArgumentNumber = 0, sourceArgumentNumber = 1, numberOfArguments = 2;
    boolean result = false;
  	
  	if (getIsInConsequent()) result = true; // Post processed - ignore
  	else {
  	  String collectionID = getCollectionIDInSingleCollectionOperation(arguments, sourceArgumentNumber, numberOfArguments); // Does argument checking
  	  List<BuiltInArgument> sortedCollection = getSortedCollection(collectionID);
    
      if (!sortedCollection.isEmpty()) {
        BuiltInArgument least = sortedCollection.get(0);
        result = processResultArgument(arguments, resultArgumentNumber, least);
      } // if
  	} // if

    return result;
  }

  // Slicing operations that create collections
  
  public boolean notNthGreatest(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
  	final int resultArgumentNumber = 0, sourceArgumentNumber = 1, numberOfArguments = 3;
    boolean result = false;
  	
  	if (getIsInConsequent()) result = true; // Post processed - ignore
  	else {
  		String collectionID = getCollectionIDInSingleCollectionOperation(arguments, sourceArgumentNumber, numberOfArguments); // Does argument checking
      int n = getArgumentAsAPositiveInteger(2, arguments);
      List<BuiltInArgument> sortedCollection = getSortedCollection(collectionID);

      if (!sortedCollection.isEmpty() && n > 0 && n <= sortedCollection.size())	sortedCollection.remove(sortedCollection.size() - n);
      	
      result = processCollectionResultArgument(arguments, resultArgumentNumber, sourceArgumentNumber, numberOfArguments, sortedCollection); 
  	} // if

    return result;
  } // notNthGreatest

  public boolean nthSlice(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
  	final int resultArgumentNumber = 0, sourceArgumentNumber = 1, numberOfArguments = 4;
    boolean result = false;
  	
  	if (getIsInConsequent()) result = true; // Post processed - ignore
  	else {
  		String collectionID = getCollectionIDInSingleCollectionOperation(arguments, sourceArgumentNumber, numberOfArguments); // Does argument checking
      int n = getArgumentAsAPositiveInteger(2, arguments)  - 1; // 1-offset for user, 0 for processing
      int sliceSize = getArgumentAsAPositiveInteger(3, arguments);
      List<BuiltInArgument> sortedCollection = getSortedCollection(collectionID);
      List<BuiltInArgument> slice = new ArrayList<BuiltInArgument>();

      if (!sortedCollection.isEmpty() && n >= 0) {      
    		int startIndex = n;
    		int finishIndex = n + sliceSize - 1;
      	for (int index = startIndex; index <= finishIndex && index < sortedCollection.size(); index++) slice.add(sortedCollection.get(index));
      } // if
      	 
      result = processCollectionResultArgument(arguments, resultArgumentNumber, sourceArgumentNumber, numberOfArguments, slice);
  	} // if

    return result;
  } // nthSlice
  
  public boolean notNthSlice(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
  	final int resultArgumentNumber = 0, sourceArgumentNumber = 1, numberOfArguments = 4;
    boolean result = false;
  	
  	if (getIsInConsequent()) result = true; // Post processed - ignore
  	else {
  		String collectionID = getCollectionIDInSingleCollectionOperation(arguments, sourceArgumentNumber, numberOfArguments); // Does argument checking
  		List<BuiltInArgument> sortedCollection = getSortedCollection(collectionID);
      int n = getArgumentAsAPositiveInteger(2, arguments) - 1; // 1-offset for user, 0 for processing
      int sliceSize = getArgumentAsAPositiveInteger(3, arguments);
      List<BuiltInArgument> notSlice = new ArrayList<BuiltInArgument>();

      if (!sortedCollection.isEmpty() && n >= 0 && n < sortedCollection.size()) {
    		int startIndex = n;
    		int finishIndex = n + sliceSize - 1;
      	for (int index = 0; index < sortedCollection.size(); index++) 
      	  if (index < startIndex || index > finishIndex) notSlice.add(sortedCollection.get(index));
      } // if
      	     	
      result = processCollectionResultArgument(arguments, resultArgumentNumber, sourceArgumentNumber, numberOfArguments, notSlice);
  	} // if

    return result;
  } // notNthSlice

  public boolean nthGreatestSlice(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
  	final int resultArgumentNumber = 0, sourceArgumentNumber = 1, numberOfArguments = 4;
    boolean result = false;
  	
  	if (getIsInConsequent()) result = true; // Post processed - ignore
  	else {
  		String collectionID = getCollectionIDInSingleCollectionOperation(arguments, sourceArgumentNumber, numberOfArguments); // Does argument checking
  		List<BuiltInArgument> sortedCollection = getSortedCollection(collectionID);
      int n = getArgumentAsAPositiveInteger(2, arguments);
      int sliceSize = getArgumentAsAPositiveInteger(3, arguments);
      List<BuiltInArgument> slice = new ArrayList<BuiltInArgument>();
      
      if (!sortedCollection.isEmpty() && n > 0) {
      	int startIndex = sortedCollection.size() - n;
    		int finishIndex = startIndex + sliceSize - 1;
    		if (startIndex < 0) startIndex = 0;
    		for (int index = startIndex; index <= finishIndex && index < sortedCollection.size(); index++) slice.add(sortedCollection.get(index));
      } // if
      	
      result = processCollectionResultArgument(arguments, resultArgumentNumber, sourceArgumentNumber, numberOfArguments, slice);
  	} // if

    return result;
  } // nthGreatestSlice

  public boolean notNthGreatestSlice(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
  	final int resultArgumentNumber = 0, sourceArgumentNumber = 1, numberOfArguments = 4;
    boolean result = false;
  	
  	if (getIsInConsequent()) result = true; // Post processed - ignore
  	else {
  		String collectionID = getCollectionIDInSingleCollectionOperation(arguments, sourceArgumentNumber, numberOfArguments); // Does argument checking
      List<BuiltInArgument> sortedCollection = getSortedCollection(collectionID);
      int n = getArgumentAsAPositiveInteger(2, arguments);
      int sliceSize = getArgumentAsAPositiveInteger(3, arguments);
      List<BuiltInArgument> slice = new ArrayList<BuiltInArgument>();

      if (!sortedCollection.isEmpty() && n > 0 && n <= sortedCollection.size()) {
    		int startIndex = sortedCollection.size() - n;
    		int finishIndex = startIndex + sliceSize - 1;
    		for (int index = 0; index < sortedCollection.size(); index++) 
    			if (index < startIndex || index > finishIndex) slice.add(sortedCollection.get(index));
      } // if
    	   
      result = processCollectionResultArgument(arguments, resultArgumentNumber, sourceArgumentNumber, numberOfArguments, slice);
  	} // if
  	
    return result;
  } 

  public boolean notNth(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
  	final int resultArgumentNumber = 0, sourceArgumentNumber = 1, numberOfArguments = 3;
    boolean result = false;
  	
  	if (getIsInConsequent()) result = true; // Post processed - ignore
  	else {
      String collectionID = getCollectionIDInSingleCollectionOperation(arguments, sourceArgumentNumber, numberOfArguments); // Does argument checking
      List<BuiltInArgument> sortedCollection = getSortedCollection(collectionID);
      int n = getArgumentAsAPositiveInteger(2, arguments) - 1;  // 1-offset for user, 0 for processing
    
      if (!sortedCollection.isEmpty() && n >= 0 && n < sortedCollection.size()) sortedCollection.remove(n);

      result = processCollectionResultArgument(arguments, resultArgumentNumber, sourceArgumentNumber, numberOfArguments, sortedCollection);
  	} // if

    return result;
  } 
  
  public boolean notGreatest(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
  	final int resultArgumentNumber = 0, sourceArgumentNumber = 1, numberOfArguments = 2;
    boolean result = false;
  	
  	if (getIsInConsequent()) result = true; // Post processed - ignore
  	else {
  		String collectionID = getCollectionIDInSingleCollectionOperation(arguments, sourceArgumentNumber, numberOfArguments); // Does argument checking
      List<BuiltInArgument> sortedCollection = getSortedCollection(collectionID);
    
      if (!sortedCollection.isEmpty()) sortedCollection.remove(sortedCollection.size() - 1);

      result = processCollectionResultArgument(arguments, resultArgumentNumber, sourceArgumentNumber, numberOfArguments, sortedCollection);
  	} // if

    return result;
  } 

  public boolean greatestN(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
  	final int resultArgumentNumber = 0, sourceArgumentNumber = 1, numberOfArguments = 3;
    boolean result = false;
  	
  	if (getIsInConsequent()) result = true; // Post processed - ignore
  	else {
  		String collectionID = getCollectionIDInSingleCollectionOperation(arguments, sourceArgumentNumber, numberOfArguments); // Does argument checking
  		List<BuiltInArgument> sortedCollection = getSortedCollection(collectionID);
  	  int n = getArgumentAsAPositiveInteger(2, arguments);
  	  List<BuiltInArgument> greatestN = new ArrayList<BuiltInArgument>();
    
  	  if (!sortedCollection.isEmpty() && n > 0) 
        for (int i = sortedCollection.size() - n; i < sortedCollection.size(); i++) greatestN.add(sortedCollection.get(i));
	
  	  result = processCollectionResultArgument(arguments, resultArgumentNumber, sourceArgumentNumber, numberOfArguments, greatestN);
  	} // if

    return result;
  }
 
  public boolean notGreatestN(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
  	final int resultArgumentNumber = 0, sourceArgumentNumber = 1, numberOfArguments = 3;
    boolean result = false;
  	
  	if (getIsInConsequent()) result = true; // Post processed - ignore
  	else {
      String collectionID = getCollectionIDInSingleCollectionOperation(arguments, sourceArgumentNumber, numberOfArguments); // Does argument checking
      List<BuiltInArgument> sortedCollection = getSortedCollection(collectionID);
      int n = getArgumentAsAPositiveInteger(2, arguments);
      List<BuiltInArgument> notGreatestN = new ArrayList<BuiltInArgument>();
      
      if (!sortedCollection.isEmpty() && n > 0)
        for (int i = 0; i < sortedCollection.size() - n; i++) notGreatestN.add(sortedCollection.get(i));
	
      result = processCollectionResultArgument(arguments, resultArgumentNumber, sourceArgumentNumber, numberOfArguments, notGreatestN);
  	} // if

    return result;
  }

  public boolean notLeast(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
  	final int resultArgumentNumber = 0, sourceArgumentNumber = 1, numberOfArguments = 2;
    boolean result = false;
  	
  	if (getIsInConsequent()) result = true; // Post processed - ignore
  	else {
      String collectionID = getCollectionIDInSingleCollectionOperation(arguments, sourceArgumentNumber, numberOfArguments); // Does argument checking
      List<BuiltInArgument> sortedCollection = getSortedCollection(collectionID);
    
      if (!sortedCollection.isEmpty()) sortedCollection.remove(0);
	
      result = processCollectionResultArgument(arguments, resultArgumentNumber, sourceArgumentNumber, numberOfArguments, sortedCollection);
  	} // if

    return result;
  } 

  public boolean leastN(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
  	final int resultArgumentNumber = 0, sourceArgumentNumber = 1, numberOfArguments = 3;
    boolean result = false;
  	
  	if (getIsInConsequent()) result = true; // Post processed - ignore
  	else {
      String collectionID = getCollectionIDInSingleCollectionOperation(arguments, sourceArgumentNumber, numberOfArguments); // Does argument checking
      List<BuiltInArgument> sortedCollection = getSortedCollection(collectionID);
      int n = getArgumentAsAPositiveInteger(2, arguments);
      List<BuiltInArgument> leastN = new ArrayList<BuiltInArgument>();
 
      for (int i = 0; i < n && i < sortedCollection.size(); i++) leastN.add(sortedCollection.get(i));
      	
      result = processCollectionResultArgument(arguments, resultArgumentNumber, sourceArgumentNumber, numberOfArguments, leastN);
  	} // if

    return result;
  } 

  public boolean notLeastN(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
  	final int resultArgumentNumber = 0, sourceArgumentNumber = 1, numberOfArguments = 3;
    boolean result = false;
  	
  	if (getIsInConsequent()) result = true; // Post processed - ignore
  	else {
      String collectionID = getCollectionIDInSingleCollectionOperation(arguments, sourceArgumentNumber, numberOfArguments); // Does argument checking
      List<BuiltInArgument> sortedCollection = getSortedCollection(collectionID);
      int n = getArgumentAsAPositiveInteger(2, arguments);
      List<BuiltInArgument> notLeastN = new ArrayList<BuiltInArgument>();

      for (int i = n; i < sortedCollection.size(); i++) notLeastN.add(sortedCollection.get(i));
      	
      result = processCollectionResultArgument(arguments, resultArgumentNumber, sourceArgumentNumber, numberOfArguments, notLeastN);
  	} // if

    return result;
  } 

  // Internal methods

  private boolean isCollection(String collectionID) { return collections.containsKey(collectionID); }

  private String getCollectionName(List<BuiltInArgument> arguments, int collectionArgumentNumber) throws BuiltInException
  {
    String queryName = getInvokingRuleName();
    String collectionName = getVariableName(collectionArgumentNumber, arguments); 
    return queryName + ":" + collectionName;
  }

  private int getNumberOfGroupElements(String collectionName) throws BuiltInException
  {
    if (!collectionGroupElementNumbersMap.containsKey(collectionName)) 
      throw new BuiltInException("internal error: invalid collection name " + collectionName + "; no group element number found");

    return collectionGroupElementNumbersMap.get(collectionName);
  }

  private String getCollectionIDInMake(List<BuiltInArgument> arguments) throws BuiltInException
  {
	  checkNumberOfArgumentsAtLeast(2, arguments.size());
	
    String queryName = getInvokingRuleName();
    String collectionName = getCollectionName(arguments, 0); // The collection is always the first argument.
    int numberOfGroupArguments = arguments.size() - 2;
    boolean hasGroupPattern  = numberOfGroupArguments != 0;
    String groupPattern = !hasGroupPattern ? "" : createInvocationPattern(getInvokingBridge(), queryName, 0, false,
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
  }

  private String getCollectionIDInSingleCollectionOperation(List<BuiltInArgument> arguments, int collectionArgumentIndex, int coreArgumentNumber) 
    throws BuiltInException
  {
    String queryName = getInvokingRuleName();
    String collectionName = queryName + ":" + getVariableName(collectionArgumentIndex, arguments);
    boolean hasGroupPattern  = (arguments.size() > coreArgumentNumber);
    String groupPattern = "";

    checkThatInAntecedent();
    
    if (hasGroupPattern)
    	groupPattern = createInvocationPattern(getInvokingBridge(), queryName, 0, false, arguments.subList(coreArgumentNumber, arguments.size()));

    return collectionName + ":" + groupPattern;
  }

  private String getCollectionIDInMultiCollectionOperation(List<BuiltInArgument> arguments, int collectionArgumentIndex, 
                                             	             int coreArgumentNumber, int groupArgumentOffset, int numberOfRelevantGroupArguments) 
   throws BuiltInException
  {
    String queryName = getInvokingRuleName();
    String collectionName = getCollectionName(arguments, collectionArgumentIndex);
    String groupPattern = "";
    
    checkThatInAntecedent();
    
    if (collectionGroupElementNumbersMap.containsKey(collectionName) && getNumberOfGroupElements(collectionName) != 0)
      groupPattern = createInvocationPattern(getInvokingBridge(), queryName, 0, false, 
                                             arguments.subList(coreArgumentNumber + groupArgumentOffset, 
                                             coreArgumentNumber + groupArgumentOffset + numberOfRelevantGroupArguments)); 

    return collectionName + ":" + groupPattern;
  }
  
  private boolean processCollectionResultArgument(List<BuiltInArgument> arguments, int resultArgumentNumber, int sourceArgumentNumber, int numberOfArguments,
  			                                          List<BuiltInArgument> resultCollection)
    throws BuiltInException
  {
  	String sourceCollectionName = getCollectionName(arguments, sourceArgumentNumber);
  	String resultCollectionName = getCollectionName(arguments, resultArgumentNumber);
  	String resultCollectionID = getCollectionIDInSingleCollectionOperation(arguments, resultArgumentNumber, numberOfArguments);
  	
	  if (!collections.containsKey(resultCollectionID)) collections.put(resultCollectionID, resultCollection);
	  
	  if (!collectionGroupElementNumbersMap.containsKey(resultCollectionName))
	  		collectionGroupElementNumbersMap.put(resultCollectionName, getNumberOfGroupElements(sourceCollectionName));
	
	  return processResultArgument(arguments, resultArgumentNumber, createDataValueArgument(resultCollectionID));
  }

  private SQWRLResultImpl getResult(String queryName) throws BuiltInException
  {
    return getInvokingBridge().getSWRLRule(queryName).getSQWRLResult();
  }
  
  private void checkThatElementIsComparable(BuiltInArgument element) throws BuiltInException
  {
    if (!(element instanceof DataValueArgument) || !((DataValueArgument)element).getDataValue().isComparable())
      throw new BuiltInException("may only be applied to collections with comparable elements");
  }

  private Collection<BuiltInArgument> getCollection(String collectionID) throws BuiltInException
  {
    if (!isCollection(collectionID)) throw new BuiltInException("argument " + collectionID + " does not refer to a collection");
    return collections.get(collectionID);
  } 
  
  private List<BuiltInArgument> getSortedCollectionInSingleCollectionOperation(List<BuiltInArgument> arguments, int sourceArgumentNumber, int numberOfArguments)
    throws BuiltInException
  {
  	String collectionID = getCollectionIDInSingleCollectionOperation(arguments, sourceArgumentNumber, numberOfArguments);
  	
  	return getSortedCollection(collectionID);
  }

  // We do not cache because only one built-in will typically perform an operation on a particular collection per query. 
  // Note: currently implementations may modify the returned collection.
  private List<BuiltInArgument> getSortedCollection(String collectionID) throws BuiltInException
  {
    Collection<BuiltInArgument> collection = getCollection(collectionID);
    List<BuiltInArgument> result = new ArrayList<BuiltInArgument>(collection);
    Collections.sort(result);
  	
  	return result;
  }    

}
