
package edu.stanford.smi.protegex.owl.swrl.sqwrl;

import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.ResultException;

import java.util.List;

/**
 * Interface to configure a result and add data to it. See the Result class for detailed comments.
 */
public interface SQWRLResultGenerator
{   
  void addColumns(List<String> columnNames) throws ResultException;
  void addColumn(String columnName) throws ResultException;

  void addAggregateColumn(String columnName, String aggregateFunctionName) throws ResultException;
  void addOrderByColumn(int orderedColumnIndex, boolean ascending) throws ResultException;
  
  boolean isOrdered();
  boolean isAscending();
  
  void setIsDistinct();
  void addColumnDisplayName(String columnName) throws ResultException;
  
  boolean isConfigured();
  void configured() throws ResultException;
  
  void addRow(List<SQWRLResultValue> resultValues) throws ResultException;
  void openRow() throws ResultException;
  void addRowData(SQWRLResultValue value) throws ResultException;
  void closeRow() throws ResultException;
  
  boolean isRowOpen();
  boolean isPrepared();
  void prepared() throws ResultException;
} // SQWRLResultGenerator
