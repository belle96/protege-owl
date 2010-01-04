
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

  public static final String MinAggregateFunction = "min"; 
  public static final String MaxAggregateFunction = "max"; 
  public static final String SumAggregateFunction = "sum"; 
  public static final String AvgAggregateFunction = "avg"; 
  public static final String MedianAggregateFunction = "median"; 
  public static final String CountAggregateFunction = "count"; 
  public static final String CountDistinctAggregateFunction = "countDistinct"; 

  // Set operations
  public static final String MakeSet = SQWRLNamespace + "makeSet";
  public static final String GroupBy = SQWRLNamespace + "groupBy";
  
  // Single set operations
  public static final String Size = SQWRLNamespace + "size";
  public static final String IsEmpty = SQWRLNamespace + "isEmpty";
  public static final String NotIsEmpty = SQWRLNamespace + "notIsEmpty";
  public static final String Contains = SQWRLNamespace + "contains";
  public static final String Greatest = SQWRLNamespace + "greatest";
  public static final String GreatestN = SQWRLNamespace + "greatestN";
  public static final String NotGreatestN = SQWRLNamespace + "notGreatestN";
  public static final String Least = SQWRLNamespace + "least";
  public static final String LeastN = SQWRLNamespace + "leastN";
  public static final String NotContains = SQWRLNamespace + "notContains";

  // Multi-set operations
  public static final String Intersection = SQWRLNamespace + "intersection";
  public static final String Union = SQWRLNamespace + "union";
  public static final String Difference = SQWRLNamespace + "difference";
  public static final String Intersects = SQWRLNamespace + "intersects";
  public static final String NotIntersects = SQWRLNamespace + "notIntersects";
  
  private static final String headBuiltInNamesArray[] 
     = { Select, SelectDistinct, Count, CountDistinct, Avg, Min, Max, Sum, OrderBy, OrderByDescending, ColumnNames };

  private static final String setBuildBuiltInNamesArray[] = { MakeSet, GroupBy };

  private static final String singleSetOperationBuiltInNamesArray[] = { Size, IsEmpty, Contains, 
      Greatest, GreatestN, NotGreatestN, Least, LeastN,
      NotIsEmpty, NotContains,
      Min, Max, Sum, Avg, Median};

  private static final String multiSetOperationBuiltInNamesArray[] = { Intersection, Union, Difference, Intersects, NotIntersects };

  private static final String createSetOperationBuiltInNamesArray[] = { Intersection, Union, Difference };

  public static  final String aggregateFunctionNames[] = { MinAggregateFunction, MaxAggregateFunction, SumAggregateFunction,
                                                           AvgAggregateFunction, MedianAggregateFunction,
                                                           CountAggregateFunction, CountDistinctAggregateFunction };

  private static Set<String> headBuiltInNames, setBuildBuiltInNames, setOperationBuiltInNames, singleSetOperationBuiltInNames,
    multiSetOperationBuiltInNames, createSetOperationBuiltInNames, sqwrlBuiltInNames; 

  static {
    headBuiltInNames = new HashSet<String>();
    setBuildBuiltInNames = new HashSet<String>();
    singleSetOperationBuiltInNames = new HashSet<String>();
    multiSetOperationBuiltInNames = new HashSet<String>();
    setOperationBuiltInNames = new HashSet<String>();
    createSetOperationBuiltInNames = new HashSet<String>();
    sqwrlBuiltInNames =  new HashSet<String>();

    for (String builtInName : headBuiltInNamesArray) headBuiltInNames.add(builtInName);
    for (String builtInName : setBuildBuiltInNamesArray) setBuildBuiltInNames.add(builtInName);

    for (String builtInName : singleSetOperationBuiltInNamesArray) {
        singleSetOperationBuiltInNames.add(builtInName);
        setOperationBuiltInNames.add(builtInName);
      } // for 

    for (String builtInName : multiSetOperationBuiltInNamesArray) {
        multiSetOperationBuiltInNames.add(builtInName);
        setOperationBuiltInNames.add(builtInName);
    } // for 

    for (String builtInName : createSetOperationBuiltInNamesArray) {
      createSetOperationBuiltInNames.add(builtInName);
      setOperationBuiltInNames.add(builtInName);
    } // for

    sqwrlBuiltInNames.addAll(setBuildBuiltInNames); 
    sqwrlBuiltInNames.addAll(setOperationBuiltInNames);
  } // static

  public static Set<String> getSQWRLBuiltInNames() { return sqwrlBuiltInNames; }
  public static Set<String> getHeadBuiltInNames() { return headBuiltInNames; }
  public static Set<String> getSetBuildBuiltInNames() { return setBuildBuiltInNames; }
  public static Set<String> getSetOperationBuiltInNames() { return setOperationBuiltInNames; }

  public static boolean isSetOperationBuiltIn(String builtInName) { return setOperationBuiltInNames.contains(builtInName); }
  public static boolean isCreateSetOperationBuiltIn(String builtInName) { return createSetOperationBuiltInNames.contains(builtInName); }
  public static boolean isSQWRLBuiltIn(String builtInName) { return sqwrlBuiltInNames.contains(builtInName); }

  public static void checkAggregateFunctionName(String aggregateFunctionName) throws InvalidAggregateFunctionNameException
  {
    boolean found = false;
    
    for (int i = 0; i < aggregateFunctionNames.length; i++) 
      if (aggregateFunctionNames[i].equalsIgnoreCase(aggregateFunctionName)) found = true;

    if (!found) throw new InvalidAggregateFunctionNameException("invalid aggregate function '" + aggregateFunctionName + "'");
  } // checkAggregateFunctionName

} // SQWRLNames
