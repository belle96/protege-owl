
package edu.stanford.smi.protegex.owl.swrl.sqwrl;

import edu.stanford.smi.protegex.owl.swrl.bridge.xsd.XSDType;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.DataValueConversionException;

public interface DataValue extends SQWRLResultValue
{
  String getString() throws DataValueConversionException; 
  Number getNumber() throws DataValueConversionException;
  XSDType getXSDType() throws DataValueConversionException; 
  boolean getBoolean() throws DataValueConversionException; 
  int getInt() throws DataValueConversionException; 
  long getLong() throws DataValueConversionException; 
  float getFloat() throws DataValueConversionException; 
  double getDouble() throws DataValueConversionException; 
  short getShort() throws DataValueConversionException; 
  byte getByte() throws DataValueConversionException;
  boolean isComparable();
  boolean isNumeric();
  boolean isBoolean();
  boolean isShort();
  boolean isInteger();
  boolean isLong();
  boolean isFloat();
  boolean isDouble();
  boolean isString();
  
  Object getValue();
  
  String toString();
  String toQuotedString();
}
