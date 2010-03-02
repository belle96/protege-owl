
package edu.stanford.smi.protegex.owl.swrl.sqwrl;

import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.InvalidAggregateFunctionNameException;

import java.util.Set;
import java.util.HashSet;

public class SQWRLNames
{
  public static String SQWRLBuiltInLibraryName = "SQWRLBuiltIns";

  public static final String SQWRLNamespace = "http://sqwrl.stanford.edu/ontologies/built-ins/3.4/sqwrl.owl#";

  public static final String Select = SQWRLNamespace + "select";
  public static final String SelectDistinct = SQWRLNamespace + "selectDistinct";
  public static final String Count = SQWRLNamespace + "count";
  public static final String CountDistinct = SQWRLNamespace + "countDistinct";
  public static final String Avg = SQWRLNamespace + "avg";
  public static final String Min = SQWRLNamespace + "min";
  public static final String Max = SQWRLNamespace + "max";
  public static final String Sum = SQWRLNamespace + "sum";
  public static final String Median = SQWRLNamespace + "median";
  public static final String OrderBy = SQWRLNamespace + "orderBy";
  public static final String OrderByDescending = SQWRLNamespace + "orderByDescending";
  public static final String ColumnNames = SQWRLNamespace + "columnNames";
  public static final String CountAggregateFunction = "count"; 
  public static final String CountDistinctAggregateFunction = "countDistinct"; 
  public static final String Limit = SQWRLNamespace + "limit";

  // Aggregation
  public static final String MinAggregateFunction = "min"; 
  public static final String MaxAggregateFunction = "max"; 
  public static final String SumAggregateFunction = "sum"; 
  public static final String AvgAggregateFunction = "avg"; 
  public static final String MedianAggregateFunction = "median"; 

  // Collection construction operations
  public static final String MakeSet = SQWRLNamespace + "makeSet";
  public static final String MakeBag = SQWRLNamespace + "makeBag";
  public static final String GroupBy = SQWRLNamespace + "groupBy";
  
  // Single collection operations
  public static final String Size = SQWRLNamespace + "size";
  public static final String IsEmpty = SQWRLNamespace + "isEmpty";
  public static final String NotIsEmpty = SQWRLNamespace + "notIsEmpty";
  public static final String Contains = SQWRLNamespace + "contains";
  public static final String NotContains = SQWRLNamespace + "notContains";
      
  // First and last
  public static final String Last = SQWRLNamespace + "last";
  public static final String NotLast = SQWRLNamespace + "notLast";
  public static final String LastN = SQWRLNamespace + "lastN";
  public static final String NotLastN = SQWRLNamespace + "notLastN";
  public static final String First = SQWRLNamespace + "first";
  public static final String NotFirst = SQWRLNamespace + "notFirst";
  public static final String FirstN = SQWRLNamespace + "firstN";
  public static final String NotFirstN = SQWRLNamespace + "notFirstN";
  
  // nth
  public static final String Nth = SQWRLNamespace + "nth";
  public static final String NotNth = SQWRLNamespace + "notNth";
  public static final String NthLast = SQWRLNamespace + "nthLast";
  public static final String NotNthLast = SQWRLNamespace + "notNthLast";

  // Slicing
  public static final String NthSlice = SQWRLNamespace + "nthSlice";
  public static final String NotNthSlice = SQWRLNamespace + "notNthSlice";
  public static final String NthLastSlice = SQWRLNamespace + "nthLastSlice";
  public static final String NotNthLastSlice = SQWRLNamespace + "notNthLastSlice";

  // Aliases for first and last operators
  public static final String Greatest = SQWRLNamespace + "greatest";
  public static final String NotGreatest = SQWRLNamespace + "notGreatest";
  public static final String GreatestN = SQWRLNamespace + "greatestN";
  public static final String NotGreatestN = SQWRLNamespace + "notGreatestN";
  public static final String Least = SQWRLNamespace + "least";
  public static final String NotLeast = SQWRLNamespace + "notLeast";
  public static final String LeastN = SQWRLNamespace + "leastN";
  public static final String NotLeastN = SQWRLNamespace + "notLeastN";
  public static final String NthGreatest = SQWRLNamespace + "nthGreatest";
  public static final String NotNthGreatest = SQWRLNamespace + "notNthGreatest";
  public static final String NthGreatestSlice = SQWRLNamespace + "nthGreatestSlice";
  public static final String NotNthGreatestSlice = SQWRLNamespace + "notNthGreatestSlice";
  
  // Multi-collection operations
  public static final String Intersection = SQWRLNamespace + "intersection";
  public static final String Union = SQWRLNamespace + "union";
  public static final String Difference = SQWRLNamespace + "difference";
  public static final String Append = SQWRLNamespace + "append";
  public static final String Intersects = SQWRLNamespace + "intersects";
  public static final String NotIntersects = SQWRLNamespace + "notIntersects";
  
  private static final String headSelectionBuiltInNamesArray[] = { Select, SelectDistinct, OrderBy, OrderByDescending, ColumnNames };
  private static final String headAggregationBuiltInNamesArray[] = { Count, CountDistinct, Avg, Min, Max, Sum };
  
  private static final String headSlicingBuiltInNamesArray[] = 
  { Limit, 
  	Nth, NthGreatest, NthLast,
  	NthSlice, NthLastSlice, NthGreatestSlice, NotNthGreatestSlice, NotNthLastSlice, NotNthSlice,
  	NotNth,  NotNthLast, NotNthGreatest, NotFirst, NotFirstN, NotLast, NotLastN, NotGreatestN, 
  	NotGreatest, NotLeastN, NotLeast, LastN, FirstN, LeastN, GreatestN 
  };
  
  private static final String collectionMakeBuiltInNamesArray[] = { MakeSet, MakeBag };
  private static final String collectionGroupByBuiltInNamesArray[] = { GroupBy };
 
  private static final String singleCollectionOperationWithoutCollectionCreateBuiltInNamesArray[] = 
  { Size, IsEmpty, NotIsEmpty, Contains, NotContains,
  	First,  Last, Least, Greatest, 
  	Min, Max, Sum, Avg, Median,
  	Nth, NthGreatest, NthLast };

  private static final String singleCollectionOperationWithCollectionCreateBuiltInNamesArray[] = 
  { NthSlice, NthLastSlice, NthGreatestSlice, NotNthGreatestSlice, NotNthLastSlice, NotNthSlice,
  	NotNth,  NotNthLast, NotNthGreatest, NotFirst, NotFirstN, NotLast, NotLastN, NotGreatestN, 
  	NotGreatest, NotLeastN, NotLeast, LastN, FirstN, LeastN, GreatestN };

  private static final String multiCollectionOperationWithoutCollectionCreateBuiltInNamesArray[] = 
  { Intersects, NotIntersects };
  
  private static final String multiCollectionOperationWithCollectionCreateBuiltInNamesArray[] = 
  { Intersection, Union, Difference, Append };
  
  public static  final String aggregateFunctionNames[] = { MinAggregateFunction, MaxAggregateFunction, SumAggregateFunction,
                                                           AvgAggregateFunction, MedianAggregateFunction,
                                                           CountAggregateFunction, CountDistinctAggregateFunction };
  private static Set<String> sqwrlBuiltInNames;
  private static Set<String> headBuiltInNames, headSelectionBuiltInNames, headAggregationBuiltInNames, headSlicingBuiltInNames;
  private static Set<String> collectionMakeBuiltInNames, collectionGroupByBuiltInNames;
  private static Set<String> collectionCreateOperationBuiltInNames, collectionOperationBuiltInNames, 
  	singleCollectionOperationWithCollectionCreateBuiltInNames, singleCollectionOperationWithoutCollectionCreateBuiltInNames,
  	multiCollectionOperationWithCollectionCreateBuiltInNames, multiCollectionOperationWithoutCollectionCreateBuiltInNames;
  	                          
  static {
  	sqwrlBuiltInNames =  new HashSet<String>();
  	
  	headBuiltInNames = new HashSet<String>();
  	headSelectionBuiltInNames = new HashSet<String>();
  	headAggregationBuiltInNames = new HashSet<String>();
  	headSlicingBuiltInNames = new HashSet<String>();
  	
    collectionMakeBuiltInNames = new HashSet<String>();
    collectionGroupByBuiltInNames = new HashSet<String>();
    
    collectionCreateOperationBuiltInNames = new HashSet<String>();
    collectionOperationBuiltInNames = new HashSet<String>();
    singleCollectionOperationWithCollectionCreateBuiltInNames = new HashSet<String>();
    singleCollectionOperationWithoutCollectionCreateBuiltInNames = new HashSet<String>();
    multiCollectionOperationWithCollectionCreateBuiltInNames = new HashSet<String>();
    multiCollectionOperationWithoutCollectionCreateBuiltInNames = new HashSet<String>();
    
    for (String builtInName : headSelectionBuiltInNamesArray) headSelectionBuiltInNames.add(builtInName);
    for (String builtInName : headAggregationBuiltInNamesArray) headAggregationBuiltInNames.add(builtInName);
    for (String builtInName : headSlicingBuiltInNamesArray) headSlicingBuiltInNames.add(builtInName);
    headBuiltInNames.addAll(headSelectionBuiltInNames);
    headBuiltInNames.addAll(headAggregationBuiltInNames);
    headBuiltInNames.addAll(headSlicingBuiltInNames);
    sqwrlBuiltInNames.addAll(headBuiltInNames);
    
    for (String builtInName : collectionMakeBuiltInNamesArray) collectionMakeBuiltInNames.add(builtInName);
    collectionCreateOperationBuiltInNames.addAll(collectionMakeBuiltInNames);
    sqwrlBuiltInNames.addAll(collectionMakeBuiltInNames);
    
    for (String builtInName : collectionGroupByBuiltInNamesArray) collectionGroupByBuiltInNames.add(builtInName);
    sqwrlBuiltInNames.addAll(collectionGroupByBuiltInNames);
    
    for (String builtInName : singleCollectionOperationWithCollectionCreateBuiltInNamesArray) 
    	singleCollectionOperationWithCollectionCreateBuiltInNames.add(builtInName);
    collectionCreateOperationBuiltInNames.addAll(singleCollectionOperationWithCollectionCreateBuiltInNames);
    collectionOperationBuiltInNames.addAll(singleCollectionOperationWithCollectionCreateBuiltInNames);
    sqwrlBuiltInNames.addAll(singleCollectionOperationWithCollectionCreateBuiltInNames);

    for (String builtInName : singleCollectionOperationWithoutCollectionCreateBuiltInNamesArray) 
    	singleCollectionOperationWithoutCollectionCreateBuiltInNames.add(builtInName);
    collectionOperationBuiltInNames.addAll(singleCollectionOperationWithoutCollectionCreateBuiltInNames);
    sqwrlBuiltInNames.addAll(singleCollectionOperationWithoutCollectionCreateBuiltInNames);

    for (String builtInName : multiCollectionOperationWithCollectionCreateBuiltInNamesArray) 
    	multiCollectionOperationWithCollectionCreateBuiltInNames.add(builtInName);
    collectionCreateOperationBuiltInNames.addAll(multiCollectionOperationWithCollectionCreateBuiltInNames);
    collectionOperationBuiltInNames.addAll(multiCollectionOperationWithCollectionCreateBuiltInNames);
    sqwrlBuiltInNames.addAll(multiCollectionOperationWithCollectionCreateBuiltInNames);

    for (String builtInName : multiCollectionOperationWithoutCollectionCreateBuiltInNamesArray) 
    	multiCollectionOperationWithoutCollectionCreateBuiltInNames.add(builtInName);
    collectionOperationBuiltInNames.addAll(multiCollectionOperationWithoutCollectionCreateBuiltInNames);
    sqwrlBuiltInNames.addAll(multiCollectionOperationWithoutCollectionCreateBuiltInNames);
  } // static

  public static Set<String> getSQWRLBuiltInNames() { return sqwrlBuiltInNames; }
  public static Set<String> getHeadBuiltInNames() { return headBuiltInNames; }
  public static Set<String> getHeadSlicingBuiltInNames() { return headSlicingBuiltInNames; }
  public static Set<String> getHeadSelectionBuiltInNames() { return headSelectionBuiltInNames; }
  public static Set<String> getCollectionMakeBuiltInNames() { return collectionMakeBuiltInNames; }
  public static Set<String> getCollectionGroupByBuiltInNames() { return collectionGroupByBuiltInNames; }
  public static Set<String> getCollectionCreateBuiltInNames() { return collectionCreateOperationBuiltInNames; }
  public static Set<String> getCollectionOperationBuiltInNames() { return collectionOperationBuiltInNames; }

  public static boolean isSQWRLBuiltIn(String builtInName) { return sqwrlBuiltInNames.contains(builtInName); }
  public static boolean isSQWRLHeadBuiltIn(String builtInName) { return headBuiltInNames.contains(builtInName); }
  public static boolean isSQWRLHeadSelectionBuiltIn(String builtInName) { return headSelectionBuiltInNames.contains(builtInName); }
  public static boolean isSQWRLHeadAggregationBuiltIn(String builtInName) { return headAggregationBuiltInNames.contains(builtInName); }
  public static boolean isSQWRLHeadSlicingBuiltIn(String builtInName) { return headSlicingBuiltInNames.contains(builtInName); }
  public static boolean isSQWRLCollectionMakeBuiltIn(String builtInName) { return collectionMakeBuiltInNames.contains(builtInName); }
  public static boolean isSQWRLCollectionGroupByBuiltIn(String builtInName) { return collectionGroupByBuiltInNames.contains(builtInName); }
  public static boolean isSQWRLCollectionCreateOperationBuiltIn(String builtInName) { return collectionCreateOperationBuiltInNames.contains(builtInName); }
  public static boolean isSQWRLCollectionOperationBuiltIn(String builtInName) { return collectionOperationBuiltInNames.contains(builtInName); }

  public static void checkAggregateFunctionName(String aggregateFunctionName) throws InvalidAggregateFunctionNameException
  {
    boolean found = false;
    
    for (int i = 0; i < aggregateFunctionNames.length; i++) 
      if (aggregateFunctionNames[i].equalsIgnoreCase(aggregateFunctionName)) found = true;

    if (!found) throw new InvalidAggregateFunctionNameException("invalid aggregate function " + aggregateFunctionName);
  } // checkAggregateFunctionName

} // SQWRLNames
