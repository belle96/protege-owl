
package edu.stanford.smi.protegex.owl.swrl.sqwrl.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.stanford.smi.protegex.owl.swrl.bridge.sqwrl.impl.DataValueImpl;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.ClassValue;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.DataValue;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.IndividualValue;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.PropertyValue;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.SQWRLNames;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.SQWRLResult;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.SQWRLResultGenerator;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.SQWRLResultValue;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.InvalidAggregateFunctionNameException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.InvalidColumnIndexException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.InvalidColumnNameException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.InvalidColumnTypeException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.InvalidQueryException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.InvalidRowIndexException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.ResultStateException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.SQWRLException;

/**
 * This class implements the interfaces SQWRLResult and SQWRLResultGenerator. It can be used to generate a result structure and populate it with
 * data; it can also be used to retrieve those data from the result.<p>
 *
 * This class operates in three phases:<p>
 *
 * (1) Configuration Phase: In this phase the structure of the result is defined. This phase opened by a call to the configure() method (which
 * will also clear any existing data). In this phase the columns are defined; aggregation or ordering is also specified in this phase. This
 * phase is closed by a call to the configured() method.<p>
 *
 * (2) Preparation Phase: In this phase data are added to the result. This phase is implicitly opened by the call to the configured() method. It
 * is closed by a call to the prepared() method.<p>
 *
 * The interface ResultGenerator defines the calls used in these two phases.<p>
 *
 * (3) Processing Phase: In this phase data may be retrieved from the result. This phase is implicitly opened by the call to the closed()
 * method.<p>
 *
 * The interface Result defines the calls used in the processing phase.<p>
 *
 * An example configuration and data generation is:<p>
 *
 * SQWRLResultImpl result = new SQWRLResultImpl("TestResult");<p>
 *
 * result.addColumn("name");<p>
 * result.addAggregateColumn("average", SQWRLNames.AvgAggregateFunction);<p><p>
 *
 * result.configured();<>p<p>
 *
 * result.openRow();<p>
 * result.addRowData(new IndividualValue("Fred"));<p>
 * result.addRowData(new DataValue(27));<p>
 * result.closeRow();<p><p>
 *
 * result.openRow();<p>
 * result.adRowdData(new IndividualValue("Joe"));<p>
 * result.addRowData(new DataValue(34));<p>
 * result.closeRow();<p><p>
 *
 * result.openRow();<p>
 * result.addRowData(new IndividualValue("Joe"));<p>
 * result.addRowData(new DataValue(21));<p>
 * result.closeRow();<p><p>
 * 
 * result.prepared();<p><p>
 *
 * The result is now available for reading. The interface SQWRLResult defines the assessor methods. A row consists of a list of objects
 * defined by the interface SQWRLResultValue. There are four possible types of values (1) DataValue, representing literals; (2) IndividualValue,
 * representing OWL individuals; (3) ClassValue, representing OWL classes; and (4) PropertyValue, representing OWL properties.<p><p>
 *
 * while (result.hasNext()) {<p>
 *  IndividualValue nameValue = result.getIndividualValue("name");<p>
 *  DataValue averageValue = result.getDataValue("average");<p>
 *  System.out.println("Name: " + nameValue.getURI());<p>
 *  System.out.println("Average: " + averageValue.getInt());<p>
 * } // while<p><p>
 *
 * A convenience method addColumns that takes a list of column names is also supplied.<p><p>
 *
 * There is also a convenience method addRow, which takes a list of SQWRLResultValues. This method automatically does a row open and close. It
 * is expecting the exact same number of list elements as there are columns in the result.<p>
 */ 
public class SQWRLResultImpl implements SQWRLResult, SQWRLResultGenerator, Serializable
{
  private List<String> allColumnNames, columnDisplayNames;
  private List<Integer> selectedColumnIndexes, orderByColumnIndexes;
  private Map<Integer, String> aggregateColumnIndexes; // Map of (index, function) pairs
  private List<List<SQWRLResultValue>> rows; // List of List of ResultValue objects.
  private List<SQWRLResultValue> rowData; // List of ResultValue objects used when assembling a row.
  private Map<String, List<SQWRLResultValue>> columnVectorMap; // Maps column names to a vector of ResultValue objects for that column
  private int numberOfColumns, rowIndex, rowDataColumnIndex;
  private boolean isConfigured, isPrepared, isRowOpen, isOrdered, isAscending, isDistinct, hasAggregates;
  private int limit = -1, nth = -1, firstN = -1, lastN = -1;
  private boolean notNthSelection = false, firstSelection = false, lastSelection = false, notFirstSelection = false, notLastSelection = false;

  public SQWRLResultImpl() 
  {
    initialize();
  } // SQWRLResultImpl
  
  // Configuration phase methods

  public boolean isConfigured() { return isConfigured; }
  public boolean isRowOpen() { return isRowOpen; }
  public boolean isDistinct() { return isDistinct; }

  public boolean isPrepared() { return isPrepared; }
  public boolean isOrdered() { return isOrdered; }
  public boolean isAscending() { return isAscending; }

  public void addColumns(List<String> columnNames) throws SQWRLException
  {
    for (String columnName : columnNames) addColumn(columnName);
  } // addColumns

  public void addColumn(String columnName) throws SQWRLException
  {
    throwExceptionIfAlreadyConfigured();

    selectedColumnIndexes.add(Integer.valueOf(numberOfColumns));
    allColumnNames.add(columnName);
    numberOfColumns++;
  } // addColumn

  public void addAggregateColumn(String columnName, String aggregateFunctionName) throws SQWRLException
  {
    throwExceptionIfAlreadyConfigured();

    SQWRLNames.checkAggregateFunctionName(aggregateFunctionName);

    aggregateColumnIndexes.put(Integer.valueOf(numberOfColumns), aggregateFunctionName);
    allColumnNames.add(columnName);
    numberOfColumns++;
  } // addAggregateColumn
  
  public void addOrderByColumn(int orderedColumnIndex, boolean ascending) throws SQWRLException
  {
    throwExceptionIfAlreadyConfigured();

    if (orderedColumnIndex < 0 || orderedColumnIndex >= allColumnNames.size()) 
      throw new SQWRLException("ordered column index " + orderedColumnIndex + " out of range");

    if (isOrdered && (isAscending != ascending)) {
      if (isAscending) 
        throw new SQWRLException("attempt to order column " + allColumnNames.get(orderedColumnIndex) + " ascending when descending was previously selected");
      else throw new SQWRLException("attempt to order column " + allColumnNames.get(orderedColumnIndex) + " descending when ascending was previously selected");
    } // if

    isOrdered = true;
    isAscending = ascending;

    orderByColumnIndexes.add(Integer.valueOf(orderedColumnIndex));
  } // addOrderByColumn
    
  public void addColumnDisplayName(String columnName) throws SQWRLException
  {
    if (columnName.length() == 0 || columnName.indexOf(',') != -1) 
      throw new SQWRLException("invalid column name " + columnName + " - no commas or empty names allowed");

    columnDisplayNames.add(columnName);
  } // addColumnDisplayName

  public void configured() throws SQWRLException
  {
    throwExceptionIfAlreadyConfigured();

    // We will already have checked that all ordered columns are selected or aggregated

    if (containsOneOf(selectedColumnIndexes, aggregateColumnIndexes.keySet()))
      throw new InvalidQueryException("aggregate columns cannot also be selected columns");

    hasAggregates = !aggregateColumnIndexes.isEmpty();

    isConfigured = true;
  } // configured

  // Methods used to retrieve the result structure after the result has been configured

  public void setIsDistinct() { isDistinct = true; }

  public int getNumberOfColumns() throws SQWRLException
  {
    throwExceptionIfNotConfigured();

    return numberOfColumns; 
  } // getNumberOfColumns
  
  public List<String> getColumnNames() throws SQWRLException
  {
    List<String> result = new ArrayList<String>();

    throwExceptionIfNotConfigured();
    
    if (columnDisplayNames.size() < getNumberOfColumns()) {
      result.addAll(columnDisplayNames);
      result.addAll(allColumnNames.subList(columnDisplayNames.size(), allColumnNames.size()));
    } else result.addAll(columnDisplayNames);

    return result;
  } // getColumnNames
  
  public String getColumnName(int columnIndex) throws SQWRLException
  {
    throwExceptionIfNotConfigured(); checkColumnIndex(columnIndex);

    if (columnIndex < columnDisplayNames.size()) return columnDisplayNames.get(columnIndex);
    else return allColumnNames.get(columnIndex);
  } // getColumnName

  // Methods used to add data after result has been configured

  public void addRow(List<SQWRLResultValue> resultValues) throws SQWRLException
  {
    if (resultValues.size() != getNumberOfColumns()) 
      throw new SQWRLException("addRow expecting " + getNumberOfColumns() + ", got " + resultValues.size() + " values");

    openRow();
    for (SQWRLResultValue value: resultValues) addRowData(value);
    closeRow();
  } // addRow

  public void openRow() throws SQWRLException
  {
    throwExceptionIfNotConfigured(); throwExceptionIfAlreadyPrepared(); throwExceptionIfRowOpen();

    rowDataColumnIndex = 0;
    rowData = new ArrayList<SQWRLResultValue>();

    isRowOpen = true;
  } // openRow

  public void addRowData(SQWRLResultValue value) throws SQWRLException
  {
    throwExceptionIfNotConfigured(); throwExceptionIfAlreadyPrepared(); throwExceptionIfRowNotOpen();

    if (rowDataColumnIndex == getNumberOfColumns()) throw new ResultStateException("attempt to add data beyond the end of a row");

    if (aggregateColumnIndexes.containsKey(Integer.valueOf(rowDataColumnIndex)) && 
        (!aggregateColumnIndexes.get(Integer.valueOf(rowDataColumnIndex)).equals(SQWRLNames.CountAggregateFunction)) && 
        (!aggregateColumnIndexes.get(Integer.valueOf(rowDataColumnIndex)).equals(SQWRLNames.CountDistinctAggregateFunction)) && 
        (!isNumericValue(value)))
        throw new SQWRLException("attempt to add non numeric value " + value + " to min, max, sum, or avg aggregate column " + 
                                  allColumnNames.get(rowDataColumnIndex));
    rowData.add(value);
    rowDataColumnIndex++;

    if (rowDataColumnIndex == getNumberOfColumns()) closeRow(); 
  } // addRowData    

  // Will ignore if row is already closed
  public void closeRow() throws SQWRLException
  {
    throwExceptionIfNotConfigured(); throwExceptionIfAlreadyPrepared(); 

    if (isRowOpen) rows.add(rowData);

    isRowOpen = false;
  } // closeRow

  public void prepared() throws SQWRLException
  {
    throwExceptionIfNotConfigured(); throwExceptionIfAlreadyPrepared();

    if (rowDataColumnIndex != 0) throwExceptionIfRowOpen(); // We allow prepared() with an open row if no data have been added.

    isPrepared = true;
    isRowOpen = false;
    rowDataColumnIndex = 0;
    if (getNumberOfRows() > 0) rowIndex = 0;
    else rowIndex = -1;

    if (hasAggregates) rows = aggregate(rows, allColumnNames, aggregateColumnIndexes); // Aggregation implies killing duplicate rows
    else if (isDistinct) rows = distinct(rows);

    if (isOrdered) rows = orderBy(rows, allColumnNames, orderByColumnIndexes, isAscending);
    
    if (hasLimit()) rows = rows.subList(0, limit);
    else if (hasNth()) {
    	if (nth <= 0 || nth > rows.size()) rows.clear();
    	else rows = rows.subList(nth - 1, nth);
    } else if (hasNotNth()) {
    	if (nth > 0 || nth <= rows.size()) rows.remove(nth - 1);
    } else if (hasFirstSelection()) {
    	if (firstN <= 0) rows.clear();
    	else rows = rows.subList(0, firstN);
    } else if (hasNotFirstSelection()) {
    	if (firstN > 0 && firstN <= rows.size()) rows = rows.subList(firstN, rows.size());
    } else if (hasLastSelection()) {
    	if (lastN <= 0) rows.clear();
    	else if (lastN <= rows.size()) rows = rows.subList(rows.size() - lastN, rows.size());
    } else if (hasNotLastSelection()) {
    	if (lastN > 0 && lastN <= rows.size()) rows = rows.subList(0, rows.size() - lastN);
    }
    
    prepareColumnVectors();
  } // prepared
  
  // nth, firstN, etc. are 1-indexed
  public void setLimit(int limit) { this.limit = limit; }
  public void setNth(int nth) { this.nth = nth; }
  public void setNotNth(int nth) { notNthSelection = true; this.nth = nth; }
  public void setFirst() { firstSelection = true; firstN = 1;}
  public void setFirst(int n) { firstSelection = true; firstN = n; }
  public void setLast() { lastSelection = true; lastN = 1;}
  public void setLast(int n) { lastSelection = true; lastN = n; }
  public void setNotFirst() { notFirstSelection = true; firstN = 1;}
  public void setNotFirst(int n) { notFirstSelection = true; firstN = n; }
  public void setNotLast() { notLastSelection = true; lastN = 1;}
  public void setNotLast(int n) { notLastSelection = true; lastN = n; }
  
  private boolean hasLimit() { return limit != -1; }
  private boolean hasNth() { return !hasNotNth() && nth != -1; }
  private boolean hasNotNth() { return notNthSelection; }
  private boolean hasFirstSelection() { return firstSelection; }
  private boolean hasLastSelection() { return lastSelection; }
  private boolean hasNotFirstSelection() { return notFirstSelection; }
  private boolean hasNotLastSelection() { return notLastSelection; }

  private void prepareColumnVectors() throws SQWRLException
  {
    columnVectorMap = new HashMap<String, List<SQWRLResultValue>>();

    if (getNumberOfColumns() > 0) {
      List<List<SQWRLResultValue>> columns = new ArrayList<List<SQWRLResultValue>>(getNumberOfColumns());
      
      for (int c = 0; c < getNumberOfColumns(); c++) columns.add(new ArrayList<SQWRLResultValue>(getNumberOfRows()));
      
      for (int r = 0; r < getNumberOfRows(); r++) 
        for (int c = 0; c < getNumberOfColumns(); c++) 
          columns.get(c).add(rows.get(r).get(c));
      
      for (int c = 0; c < getNumberOfColumns(); c++) columnVectorMap.put(getColumnName(c), columns.get(c));
    } // if
  } // prepareColumnVectors

  // Methods used to retrieve data after result has been prepared

  public int getNumberOfRows() throws SQWRLException
  { 
    throwExceptionIfNotConfigured(); throwExceptionIfNotPrepared();

    return rows.size(); 
  } // getNumberOfRows

  public boolean isEmpty() throws SQWRLException { return getNumberOfRows() == 0; }

  public void reset() throws SQWRLException
  {
    throwExceptionIfNotConfigured(); throwExceptionIfNotPrepared();

    if (getNumberOfRows() > 0) rowIndex = 0;
  } // reset
  
  public void next() throws SQWRLException
  {
    throwExceptionIfNotConfigured(); throwExceptionIfNotPrepared(); throwExceptionIfAtEndOfResult();

    if (rowIndex != -1 && rowIndex < getNumberOfRows()) rowIndex++;
  } // next
  
  public boolean hasNext() throws SQWRLException
  { 
    throwExceptionIfNotConfigured(); throwExceptionIfNotPrepared();

    return (rowIndex != -1 && rowIndex < getNumberOfRows());
  } // hasNext
    
  public List<SQWRLResultValue> getRow() throws SQWRLException
  {
    throwExceptionIfNotConfigured(); throwExceptionIfNotPrepared(); throwExceptionIfAtEndOfResult();

    return (List<SQWRLResultValue>)rows.get(rowIndex);
  } // getRow

  public SQWRLResultValue getValue(String columnName) throws SQWRLException
  {
	List<SQWRLResultValue> row;
    int columnIndex;
    
    throwExceptionIfNotConfigured(); throwExceptionIfNotPrepared(); throwExceptionIfAtEndOfResult();

    checkColumnName(columnName);
    
    columnIndex = allColumnNames.indexOf(columnName);
    
    row = rows.get(rowIndex);
    return (SQWRLResultValue)row.get(columnIndex);
  } // getColumnValue
  
  public SQWRLResultValue getValue(int columnIndex) throws SQWRLException
  {
	List<SQWRLResultValue> row;

    throwExceptionIfNotConfigured(); throwExceptionIfNotPrepared(); throwExceptionIfAtEndOfResult();

    checkColumnIndex(columnIndex);
    
    row = rows.get(rowIndex);
    return (SQWRLResultValue)row.get(columnIndex);
  } // getColumnValue

  public SQWRLResultValue getValue(int columnIndex, int rowIndex)throws SQWRLException
  {
    
    throwExceptionIfNotConfigured(); throwExceptionIfNotPrepared(); 

    checkColumnIndex(columnIndex); checkRowIndex(rowIndex); 
    
    return rows.get(rowIndex).get(columnIndex);
  } // getValue
  
  public IndividualValue getObjectValue(String columnName) throws SQWRLException
  {
    if (!hasObjectValue(columnName)) 
      throw new InvalidColumnTypeException("expecting ObjectValue type for column " + columnName);
    return (IndividualValue)getValue(columnName);
  } // getObjectValue
  
  public IndividualValue getObjectValue(int columnIndex) throws SQWRLException
  {
    return getObjectValue(getColumnName(columnIndex));
  } // getObjectValue
  
  public DataValue getDataValue(String columnName) throws SQWRLException
  {
    if (!hasDataValue(columnName)) 
      throw new InvalidColumnTypeException("expecting DataValue type for column " + columnName);
    return (DataValue)getValue(columnName);
  } // getDataValue

  public ClassValue getClassValue(String columnName) throws SQWRLException
  {
    if (!hasClassValue(columnName)) 
      throw new InvalidColumnTypeException("expecting ClassValue type for column " + columnName);
    return (ClassValue)getValue(columnName);
  } // getClassValue

  public ClassValue getClassValue(int columnIndex) throws SQWRLException
  {
    return getClassValue(getColumnName(columnIndex));
  } // getClassValue

  public PropertyValue getPropertyValue(int columnIndex) throws SQWRLException
  {
    return getPropertyValue(getColumnName(columnIndex));
  } // getPropertyValue

  public PropertyValue getPropertyValue(String columnName) throws SQWRLException
  {
    if (!hasPropertyValue(columnName)) 
      throw new InvalidColumnTypeException("expecting PropertyValue type for column " + columnName);
    return (PropertyValue)getValue(columnName);
  } // getPropertyValue
  
  public DataValue getDataValue(int columnIndex) throws SQWRLException
  {
    return getDataValue(getColumnName(columnIndex));
  } // getDataValue
  
  public List<SQWRLResultValue> getColumn(String columnName) throws SQWRLException
  {
    throwExceptionIfNotConfigured(); throwExceptionIfNotPrepared();

    checkColumnName(columnName);
    
    return columnVectorMap.get(columnName);
  } // getColumnValue

  public List<SQWRLResultValue> getColumn(int columnIndex) throws SQWRLException
  {
    return getColumn(getColumnName(columnIndex));
  } // getColumn

  public boolean hasObjectValue(String columnName) throws SQWRLException
  {
    return getValue(columnName) instanceof IndividualValue;
  } // hasObjectValue
  
  public boolean hasObjectValue(int columnIndex) throws SQWRLException
  {
    return getValue(columnIndex) instanceof IndividualValue;
  } // hasObjectValue
  
  public boolean hasDataValue(String columnName) throws SQWRLException
  {
    return getValue(columnName) instanceof DataValue;
  } // hasDataValue
  
  public boolean hasDataValue(int columnIndex) throws SQWRLException
  {
    return getValue(columnIndex) instanceof DataValue;
  } // hasDataValue

  public boolean hasClassValue(String columnName) throws SQWRLException
  {
    return getValue(columnName) instanceof ClassValue;
  } // hasClassValue
  
  public boolean hasClassValue(int columnIndex) throws SQWRLException
  {
    return getValue(columnIndex) instanceof ClassValue;
  } // hasClassValue

  public boolean hasPropertyValue(String columnName) throws SQWRLException
  {
    return getValue(columnName) instanceof PropertyValue;
  } // hasPropertyValue
  
  public boolean hasPropertyValue(int columnIndex) throws SQWRLException
  {
    return getValue(columnIndex) instanceof PropertyValue;
  } // hasPropertyValue

  public String toString()
  {
    String result = "[numberOfColumns: " + numberOfColumns + ", isConfigured: " + isConfigured + ", isPrepared: " + isPrepared + ", isRowOpen: " + isRowOpen +
              ", isOrdered: " + isOrdered + ", isAscending " + isAscending + ", isDistinct: " + isDistinct + 
              ", hasAggregates: " + hasAggregates + "]\n";

    result += "[columnDisplayNames: ";
    for (String columnDisplayName : columnDisplayNames) result += "" + columnDisplayName + "";
    result += "]\n";

    for (List<SQWRLResultValue> row : rows) {
      for (SQWRLResultValue value : row) {
        result += "" + value + " ";
      } // for
      result += "\n";
    } // for

    result += "--------------------------------------------------------------------------------\n";
      
    return result;
  } // toString      

  private void initialize()
  {
    isConfigured = false;
    isPrepared = false;
    isRowOpen = false;
    
    // The following variables will not be externally valid until configured() is called. 
    allColumnNames = new ArrayList<String>();
    aggregateColumnIndexes = new HashMap<Integer, String>();
    selectedColumnIndexes = new ArrayList<Integer>();
    orderByColumnIndexes = new ArrayList<Integer>();
    columnDisplayNames = new ArrayList<String>();

    numberOfColumns = 0; isOrdered = isAscending = isDistinct = false;

    // The following variables will not be externally valid until prepared() is called.
    rowIndex = -1; // If there are no rows in the final result, it will remain at -1.
    rows = new ArrayList<List<SQWRLResultValue>>();
  } // prepare

  // Phase verification exception throwing methods

  private void throwExceptionIfNotConfigured() throws SQWRLException
  {
    if (!isConfigured()) throw new ResultStateException("attempt to add data to unconfigured result");
  } // throwExceptionIfNotConfigured

  private void throwExceptionIfAtEndOfResult() throws SQWRLException
  {
    if (!hasNext()) throw new ResultStateException("attempt to get data after end of result reached");
  } // throwExceptionIfAtEndOfResult

  private void throwExceptionIfNotPrepared() throws SQWRLException
  {
    if (!isPrepared()) throw new ResultStateException("attempt to process unprepared result");
  } // throwExceptionIfNotConfigured

  private void throwExceptionIfAlreadyConfigured() throws SQWRLException
  {
    if (isConfigured()) throw new ResultStateException("attempt to configure already configured result");
  } // throwExceptionIfAlreadyConfigured

  private void throwExceptionIfAlreadyPrepared() throws SQWRLException
  {
    if (isPrepared()) throw new ResultStateException("attempt to modify prepared result");
  } // throwExceptionIfAlreadyConfigured

  private void checkColumnName(String columnName) throws InvalidColumnNameException
  {
    if (!allColumnNames.contains(columnName)) throw new InvalidColumnNameException("invalid column name " + columnName);
  } // checkColumnName
  
  private void throwExceptionIfRowNotOpen() throws SQWRLException
  {
    if (!isRowOpen) throw new ResultStateException("attempt to add data to an unopened row");
  } // throwExceptionIfRowNotOpen

  private void throwExceptionIfRowOpen() throws SQWRLException
  {
    if (isRowOpen) throw new ResultStateException("attempt to process result with a partially prepared row");
  } // throwExceptionIfRowOpen

  private void checkColumnIndex(int columnIndex) throws SQWRLException
  {
    if (columnIndex < 0 || columnIndex >= getNumberOfColumns())
      throw new InvalidColumnIndexException("column index " + columnIndex + " out of bounds");
  } // checkColumnIndex

  private void checkRowIndex(int rowIndex) throws SQWRLException
  {
    if (rowIndex < 0 || rowIndex >= getNumberOfRows())
      throw new InvalidRowIndexException("row index " + rowIndex + " out of bounds");
  } // checkRowIndex
  
  private boolean containsOneOf(List<Integer> collection1, Set<Integer> collection2)
  {
    for (Integer i : collection2)
     if (collection1.contains(i)) return true;
    
    return false;
  } // containsOneOf

  private boolean isNumericValue(SQWRLResultValue value)
  {
    return ((value instanceof DataValue) && (((DataValue)value).isNumeric()));
  } // isNumericValue

  // TODO: fix - very inefficient
  private List<List<SQWRLResultValue>> distinct(List<List<SQWRLResultValue>> rows)
  {
    List<List<SQWRLResultValue>> localRows = new ArrayList<List<SQWRLResultValue>>(rows);
    List<List<SQWRLResultValue>> result = new ArrayList<List<SQWRLResultValue>>();
    RowComparator rowComparator = new RowComparator(allColumnNames, true); // Look at the entire row.

    Collections.sort(localRows, rowComparator); // binary search is expecting a sorted list
    for (List<SQWRLResultValue> row : localRows) if (Collections.binarySearch(result, row, rowComparator) < 0) result.add(row);

    return result;
  } // distinct

  private List<List<SQWRLResultValue>> aggregate(List<List<SQWRLResultValue>> rows, List<String> allColumnNames, 
                                                 Map<Integer, String> aggregateColumnIndexes)
    throws SQWRLException
  {
    List<List<SQWRLResultValue>> result = new ArrayList<List<SQWRLResultValue>>();
    RowComparator rowComparator = new RowComparator(allColumnNames, selectedColumnIndexes, true); 
    // Key is index of aggregated row in result, value is hash map of aggregate column index to list of original values.
    HashMap<Integer, HashMap<Integer, List<SQWRLResultValue>>> aggregatesMap = new HashMap<Integer, HashMap<Integer, List<SQWRLResultValue>>>();
    HashMap<Integer, List<SQWRLResultValue>> aggregateRowMap; // Map of column indexes to value lists; used to accumulate values for aggregation.
    List<SQWRLResultValue> values;
    SQWRLResultValue value;
    int rowIndex;

    for (List<SQWRLResultValue> row : rows) {
      rowIndex = findRowIndex(result, row, rowComparator); // Find a row with the same values for non aggregated columns.

      if (rowIndex < 0) { // Row with same values for non aggregated columns not yet present in result.
        aggregateRowMap = new HashMap<Integer, List<SQWRLResultValue>>();
        // Find value for each aggregated column in row and add each to map indexed by result row
        for (Integer aggregateColumnIndex : aggregateColumnIndexes.keySet()) { 
          values = new ArrayList<SQWRLResultValue>();
          value = row.get(aggregateColumnIndex.intValue());
          values.add(value);
          aggregateRowMap.put(aggregateColumnIndex, values);
        } // for
        aggregatesMap.put(Integer.valueOf(result.size()), aggregateRowMap); // 
        result.add(row);
      } else { // We found a row that has the same values for the non aggregated columns.
        aggregateRowMap = aggregatesMap.get(Integer.valueOf(rowIndex)); // Find the aggregate map
        for (Integer aggregateColumnIndex : aggregateColumnIndexes.keySet()) {
          value = row.get(aggregateColumnIndex.intValue());  // Find value
          values = aggregateRowMap.get(aggregateColumnIndex); // Find row map
          values.add(value); // Add value
        } // for
      } // if
    } // for

    rowIndex = 0;
    for (List<SQWRLResultValue> row : result) {
      aggregateRowMap = aggregatesMap.get(Integer.valueOf(rowIndex));

      for (Integer aggregateColumnIndex : aggregateColumnIndexes.keySet()) {
        String aggregateFunctionName = aggregateColumnIndexes.get(aggregateColumnIndex);
        values = aggregateRowMap.get(aggregateColumnIndex);

        // We have checked in addRowData that only numeric data are added for sum, max, min, and avg
        if (aggregateFunctionName.equalsIgnoreCase(SQWRLNames.MinAggregateFunction)) value = min(values);
        else if (aggregateFunctionName.equalsIgnoreCase(SQWRLNames.MaxAggregateFunction)) value = max(values);
        else if (aggregateFunctionName.equalsIgnoreCase(SQWRLNames.SumAggregateFunction)) value = sum(values);
        else if (aggregateFunctionName.equalsIgnoreCase(SQWRLNames.AvgAggregateFunction)) value = avg(values);
        else if (aggregateFunctionName.equalsIgnoreCase(SQWRLNames.CountAggregateFunction)) value = count(values);
        else if (aggregateFunctionName.equalsIgnoreCase(SQWRLNames.CountDistinctAggregateFunction)) value = countDistinct(values);
        else throw new InvalidAggregateFunctionNameException("invalid aggregate function " + aggregateFunctionName);

        row.set(aggregateColumnIndex.intValue(), value);
      } // for
      rowIndex++;
    } // for

    return result;
  } // aggregate

  private List<List<SQWRLResultValue>> orderBy(List<List<SQWRLResultValue>> rows, List<String> allColumnNames, 
                                          List<Integer> orderByColumnIndexes, boolean ascending)
    throws SQWRLException
  {
    List<List<SQWRLResultValue>> result = new ArrayList<List<SQWRLResultValue>>(rows);
    RowComparator rowComparator = new RowComparator(allColumnNames, orderByColumnIndexes, ascending); 

    Collections.sort(result, rowComparator);

    return result;
  } // orderBy

  private DataValue min(List<SQWRLResultValue> values) throws SQWRLException
  {
    DataValue result = null, value;

    if (values.isEmpty()) throw new SQWRLException("empty aggregate list for " + SQWRLNames.MinAggregateFunction);

    for (SQWRLResultValue resultValue : values) {

      if (!(resultValue instanceof DataValue))
        throw new SQWRLException("attempt to use " + SQWRLNames.MinAggregateFunction + " aggregate on non datatype " + resultValue);

      value = (DataValue)resultValue;

      if (!value.isNumeric()) 
        throw new SQWRLException("attempt to use " + SQWRLNames.MinAggregateFunction + " aggregate on non numeric datatype " + value);

      if (result == null) result = value;
      else if (value.compareTo(result) < 0) result = value;
    } // for

    return result;
  } // min

  private DataValue max(List<SQWRLResultValue> values) throws SQWRLException
  {
    DataValue result = null, value;

    if (values.isEmpty()) throw new SQWRLException("empty aggregate list for " + SQWRLNames.MaxAggregateFunction);

    for (SQWRLResultValue resultValue : values) {

      if (!(resultValue instanceof DataValue))
        throw new SQWRLException("attempt to use " + SQWRLNames.MaxAggregateFunction + " aggregate on non datatype " + resultValue);

      value = (DataValue)resultValue;

      if (!value.isNumeric()) 
        throw new SQWRLException("attempt to use " + SQWRLNames.MaxAggregateFunction + " aggregate on non numeric datatype " + value);

      if (result == null) result = value;
      else if (value.compareTo(result) > 0) result = value;
    } // for

    return result;
  } // max

  private DataValue sum(List<SQWRLResultValue> values) throws SQWRLException
  {
    double sum = 0, value;

    if (values.isEmpty()) throw new SQWRLException("empty aggregate list for " + SQWRLNames.SumAggregateFunction);

    for (SQWRLResultValue resultValue : values) {

      if (!(resultValue instanceof DataValue))
        throw new SQWRLException("attempt to use " + SQWRLNames.SumAggregateFunction + " aggregate on non data value: " + resultValue);

        value = ((DataValue)resultValue).getDouble();

      sum = sum + value;
    } // for

    return new DataValueImpl(sum);
  } // sum

  private DataValue avg(List<SQWRLResultValue> values) throws SQWRLException
  {
    double sum = 0, value;
    int count = 0;

    if (values.isEmpty()) throw new SQWRLException("empty aggregate list for function " + SQWRLNames.AvgAggregateFunction);

    for (SQWRLResultValue resultValue : values) {

      if (!(resultValue instanceof DataValue))
        throw new SQWRLException("attempt to use " + SQWRLNames.AvgAggregateFunction + " aggregate on non data value " + resultValue);

        value = ((DataValue)resultValue).getDouble();

      count++;
      sum = sum + value;
    } // for

    return new DataValueImpl(sum / count);
  } // sum

  private DataValue count(List<SQWRLResultValue> values) throws SQWRLException
  {
    return new DataValueImpl(values.size());
  } // count

  private DataValue countDistinct(List<SQWRLResultValue> values) throws SQWRLException
  {
    Set<SQWRLResultValue> distinctValues = new HashSet<SQWRLResultValue>(values);

    return new DataValueImpl(distinctValues.size());
  } // countDistinct

  // TODO: linear search is not very efficient. 
  private int findRowIndex(List<List<SQWRLResultValue>> result, List<SQWRLResultValue> rowToFind, Comparator<List<SQWRLResultValue>> rowComparator)
  {
    int rowIndex = 0;

    for (List<SQWRLResultValue> row : result) {
      if (rowComparator.compare(rowToFind, row) == 0) return rowIndex;
      rowIndex++;
    } // for

    return -1;
  } // findRowIndex

  // Quick and dirty: all checking left to the Java runtime.
  private static class RowComparator implements Comparator<List<SQWRLResultValue>>
  {
    private List<Integer> orderByColumnIndexes;
    private boolean ascending;
    
    public RowComparator(List<String> allColumnNames, List<Integer> orderByColumnIndexes, boolean ascending)
    {
      this.ascending = ascending;
      this.orderByColumnIndexes = orderByColumnIndexes;
    } // RowComparator
    
    public RowComparator(List<String> allColumnNames, boolean ascending)
    {
      this.ascending = ascending;
      orderByColumnIndexes = new ArrayList<Integer>();

      for (String columnName : allColumnNames) orderByColumnIndexes.add(allColumnNames.indexOf(columnName));
    } // RowComparator

    public int compare(List<SQWRLResultValue> row1, List<SQWRLResultValue> row2) 
    {
      for (Integer columnIndex : orderByColumnIndexes) {
        int result = row1.get(columnIndex).compareTo(row2.get(columnIndex));
        if (result != 0) if (ascending) return result; else return -result;
      } // for

      return 0;
    } // compare
  } // RowComparator

} // SQWRLResultImpl
