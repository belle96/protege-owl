
package edu.stanford.smi.protegex.owl.swrl.sqwrl;

import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.SQWRLException;

import java.util.List;

/**
 ** Interface that defines methods to process results from a SQWRL query. See <a
 ** href="http://protege.cim3.net/cgi-bin/wiki.pl?SQWRLQueryAPI">here</a> for documentation.
 */
public interface SQWRLResult
{
  List<String> getColumnNames() throws SQWRLException;
  int getNumberOfColumns() throws SQWRLException;
  String getColumnName(int columnIndex) throws SQWRLException;

  boolean isEmpty() throws SQWRLException;
  int getNumberOfRows() throws SQWRLException;
  void reset() throws SQWRLException;
  void next() throws SQWRLException;
  boolean hasNext() throws SQWRLException;
  
  boolean hasObjectValue(String columnName) throws SQWRLException;
  boolean hasObjectValue(int columnIndex) throws SQWRLException;
  boolean hasDataValue(String columnName) throws SQWRLException;
  boolean hasDataValue(int columnIndex) throws SQWRLException;
  boolean hasClassValue(String columnName) throws SQWRLException;
  boolean hasClassValue(int columnIndex) throws SQWRLException;
  boolean hasPropertyValue(String columnName) throws SQWRLException;
  boolean hasPropertyValue(int columnIndex) throws SQWRLException;

  List<SQWRLResultValue> getRow() throws SQWRLException;
  SQWRLResultValue getValue(String columnName) throws SQWRLException;
  SQWRLResultValue getValue(int columnIndex) throws SQWRLException;
  SQWRLResultValue getValue(int columnIndex, int rowIndex) throws SQWRLException;
  IndividualValue getObjectValue(String columnName) throws SQWRLException;
  IndividualValue getObjectValue(int columnIndex) throws SQWRLException;
  DataValue getDataValue(String columnName) throws SQWRLException;
  DataValue getDataValue(int columnIndex) throws SQWRLException;
  ClassValue getClassValue(String columnName) throws SQWRLException;
  ClassValue getClassValue(int columnIndex) throws SQWRLException;
  PropertyValue getPropertyValue(String columnName) throws SQWRLException;
  PropertyValue getPropertyValue(int columnIndex) throws SQWRLException;

  List<SQWRLResultValue> getColumn(String columnName) throws SQWRLException;
  List<SQWRLResultValue> getColumn(int columnIndex) throws SQWRLException;
} // SQWRLResult
