
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
  public static final String Limit = SQWRLNamespace + "limit";
  public static final String MinN = "minN"; 
  public static final String MaxN = "maxN";

  public static final String MinAggregateFunction = "min"; 
  public static final String MaxAggregateFunction = "max"; 
  public static final String SumAggregateFunction = "sum"; 
  public static final String AvgAggregateFunction = "avg"; 
  public static final String MedianAggregateFunction = "median"; 
  public static final String CountAggregateFunction = "count"; 
  public static final String CountDistinctAggregateFunction = "countDistinct"; 

  // Collection operations
  public static final String MakeSet = SQWRLNamespace + "makeSet";
  public static final String MakeBag = SQWRLNamespace + "makeBag";
  public static final String GroupBy = SQWRLNamespace + "groupBy";
  
  // Single collection operations
  public static final String Size = SQWRLNamespace + "size";
  public static final String IsEmpty = SQWRLNamespace + "isEmpty";
  public static final String NotIsEmpty = SQWRLNamespace + "notIsEmpty";
  public static final String Contains = SQWRLNamespace + "contains";
  public static final String NotContains = SQWRLNamespace + "notContains";
  public static final String Nth = SQWRLNamespace + "nth";
  public static final String NotNth = SQWRLNamespace + "notNth";
  
  public static final String Last = SQWRLNamespace + "last";
  public static final String NotLast = SQWRLNamespace + "notLast";
  public static final String LastN = SQWRLNamespace + "lastN";
  public static final String NotLastN = SQWRLNamespace + "notLastN";
  public static final String First = SQWRLNamespace + "first";
  public static final String NotFirst = SQWRLNamespace + "notFirst";
  public static final String FirstN = SQWRLNamespace + "firstN";
  public static final String NotFirstN = SQWRLNamespace + "notFirstN";
  
  public static final String Greatest = SQWRLNamespace + "greatest";
  public static final String NotGreatest = SQWRLNamespace + "notGreatest";
  public static final String GreatestN = SQWRLNamespace + "greatestN";
  public static final String NotGreatestN = SQWRLNamespace + "notGreatestN";
  public static final String Least = SQWRLNamespace + "least";
  public static final String NotLeast = SQWRLNamespace + "notLeast";
  public static final String LeastN = SQWRLNamespace + "leastN";
  public static final String NotLeastN = SQWRLNamespace + "notLeastN";
  
  // Multi-collection operations
  public static final String Intersection = SQWRLNamespace + "intersection";
  public static final String Union = SQWRLNamespace + "union";
  public static final String Difference = SQWRLNamespace + "difference";
  public static final String Intersects = SQWRLNamespace + "intersects";
  public static final String NotIntersects = SQWRLNamespace + "notIntersects";
  
  private static final String headSelectionBuiltInNamesArray[] = { Select, SelectDistinct, OrderBy, OrderByDescending, ColumnNames };
  private static final String headAggregationBuiltInNamesArray[] = { Count, CountDistinct, Avg, Min, Max, Sum };
  private static final String headSlicingBuiltInNamesArray[] = { Limit, 
                                                 	               Nth, NotNth,
                                                 	 	             First, Last, NotFirst, NotLast, FirstN, NotFirstN, LastN, NotLastN,
                                                 	 	             Least, Greatest, LeastN, NotLeastN, GreatestN, NotGreatestN};
  
  private static final String collectionMakeBuiltInNamesArray[] = { MakeSet, MakeBag };
  private static final String collectionGroupBuiltInNamesArray[] = { GroupBy };
 
  private static final String singleCollectionOperationBuiltInNamesArray[] = 
  { Size, IsEmpty, NotIsEmpty, Contains, NotContains, 
  	Nth, NotNth,
	  First, NotFirst, FirstN, NotFirstN, Last, NotLast, LastN, NotLastN,
	  Least, NotLeast, LeastN, NotLeastN, Greatest, NotGreatest, GreatestN, NotGreatestN,
    Min, Max, Sum, Avg, Median};

  private static final String multiCollectionOperationBuiltInNamesArray[] = { Intersection, Union, Difference, Intersects, NotIntersects };
  private static final String createCollectionOperationBuiltInNamesArray[] = { Intersection, Union, Difference };
  public static  final String aggregateFunctionNames[] = { MinAggregateFunction, MaxAggregateFunction, SumAggregateFunction,
                                                           AvgAggregateFunction, MedianAggregateFunction,
                                                           CountAggregateFunction, CountDistinctAggregateFunction };

  private static Set<String> sqwrlBuiltInNames, headBuiltInNames, headSelectionBuiltInNames, headAggregationBuiltInNames, 
                             headSlicingBuiltInNames;
  
  private static Set<String> collectionMakeBuiltInNames, collectionGroupBuiltInNames, collectionMakeAndGroupBuiltInNames;
  
  private static Set<String> collectionOperationBuiltInNames, singleCollectionOperationBuiltInNames,
                             multiCollectionOperationBuiltInNames, createCollectionOperationBuiltInNames; 

  static {
  	sqwrlBuiltInNames =  new HashSet<String>();
  	headBuiltInNames = new HashSet<String>();
  	headSelectionBuiltInNames = new HashSet<String>();
  	headAggregationBuiltInNames = new HashSet<String>();
  	headSlicingBuiltInNames = new HashSet<String>();
  	
    collectionMakeBuiltInNames = new HashSet<String>();
    collectionGroupBuiltInNames = new HashSet<String>();
    collectionMakeAndGroupBuiltInNames = new HashSet<String>();
    
    collectionOperationBuiltInNames = new HashSet<String>();
    singleCollectionOperationBuiltInNames = new HashSet<String>();
    multiCollectionOperationBuiltInNames = new HashSet<String>();
    createCollectionOperationBuiltInNames = new HashSet<String>();
    
    for (String builtInName : headSelectionBuiltInNamesArray) headSelectionBuiltInNames.add(builtInName);
    for (String builtInName : headAggregationBuiltInNamesArray) headAggregationBuiltInNames.add(builtInName);
    for (String builtInName : headSlicingBuiltInNamesArray) headSlicingBuiltInNames.add(builtInName);
    headBuiltInNames.addAll(headSelectionBuiltInNames);
    headBuiltInNames.addAll(headAggregationBuiltInNames);
    headBuiltInNames.addAll(headSlicingBuiltInNames);
    
    for (String builtInName : collectionMakeBuiltInNamesArray) collectionMakeBuiltInNames.add(builtInName);
    for (String builtInName : collectionGroupBuiltInNamesArray) collectionGroupBuiltInNames.add(builtInName);
    collectionMakeAndGroupBuiltInNames.addAll(collectionMakeBuiltInNames);
    collectionMakeAndGroupBuiltInNames.addAll(collectionGroupBuiltInNames);

    for (String builtInName : createCollectionOperationBuiltInNamesArray) createCollectionOperationBuiltInNames.add(builtInName);
    for (String builtInName : singleCollectionOperationBuiltInNamesArray) singleCollectionOperationBuiltInNames.add(builtInName);
    for (String builtInName : multiCollectionOperationBuiltInNamesArray) multiCollectionOperationBuiltInNames.add(builtInName);
    collectionOperationBuiltInNames.addAll(createCollectionOperationBuiltInNames);
    collectionOperationBuiltInNames.addAll(singleCollectionOperationBuiltInNames);
    collectionOperationBuiltInNames.addAll(multiCollectionOperationBuiltInNames);

    sqwrlBuiltInNames.addAll(headBuiltInNames);
    sqwrlBuiltInNames.addAll(collectionMakeAndGroupBuiltInNames);
    sqwrlBuiltInNames.addAll(collectionOperationBuiltInNames);
  } // static

  public static Set<String> getSQWRLBuiltInNames() { return sqwrlBuiltInNames; }
  public static Set<String> getHeadBuiltInNames() { return headBuiltInNames; }
  public static Set<String> getHeadSelectionBuiltInNames() { return headSelectionBuiltInNames; }
  public static Set<String> getHeadSlicingBuiltInNames() { return headSlicingBuiltInNames; }
  public static Set<String> getHeadAggregationBuiltInNames() { return headAggregationBuiltInNames; }
  public static Set<String> getCollectionMakeBuiltInNames() { return collectionMakeBuiltInNames; }
  public static Set<String> getCollectionGroupBuiltInNames() { return collectionGroupBuiltInNames; }
  public static Set<String> getCollectionMakeAndGroupBuiltInNames() { return collectionMakeAndGroupBuiltInNames; }
  public static Set<String> getCollectionOperationBuiltInNames() { return collectionOperationBuiltInNames; }

  public static boolean isSQWRLBuiltIn(String builtInName) { return sqwrlBuiltInNames.contains(builtInName); }
  public static boolean isSQWRLHeadBuiltIn(String builtInName) { return headBuiltInNames.contains(builtInName); }
  public static boolean isSQWRLHeadSelectionBuiltIn(String builtInName) { return headSelectionBuiltInNames.contains(builtInName); }
  public static boolean isSQWRLHeadAggregationBuiltIn(String builtInName) { return headAggregationBuiltInNames.contains(builtInName); }
  public static boolean isSQWRLHeadSlicingBuiltIn(String builtInName) { return headSlicingBuiltInNames.contains(builtInName); }
  public static boolean isSQWRLCollectionMakeBuiltIn(String builtInName) { return collectionMakeBuiltInNames.contains(builtInName); }
  public static boolean isSQWRLCollectionGroupBuiltIn(String builtInName) { return collectionGroupBuiltInNames.contains(builtInName); }
  public static boolean isSQWRLCollectionOperationBuiltIn(String builtInName) { return collectionOperationBuiltInNames.contains(builtInName); }

  public static void checkAggregateFunctionName(String aggregateFunctionName) throws InvalidAggregateFunctionNameException
  {
    boolean found = false;
    
    for (int i = 0; i < aggregateFunctionNames.length; i++) 
      if (aggregateFunctionNames[i].equalsIgnoreCase(aggregateFunctionName)) found = true;

    if (!found) throw new InvalidAggregateFunctionNameException("invalid aggregate function " + aggregateFunctionName);
  } // checkAggregateFunctionName

} // SQWRLNames
