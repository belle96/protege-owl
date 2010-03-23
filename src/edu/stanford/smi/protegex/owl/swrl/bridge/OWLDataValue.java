
package edu.stanford.smi.protegex.owl.swrl.bridge;

import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLLiteral;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLPropertyValue;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLTypedLiteral;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.DataValueConversionException;

/**
 * Convenience wrapper around OWLAPI classes OWLLiteral and OWLDataType
 */
public interface OWLDataValue extends OWLPropertyValue, OWLLiteral, OWLTypedLiteral 
{
  // Java types
  boolean isString();
  boolean isBoolean();
  boolean isNumeric();
  boolean isInteger();
  boolean isLong();
  boolean isFloat();
  boolean isDouble();
  boolean isShort();
  boolean isByte();
  
  boolean isXSDType();
  boolean isXSDTime();
  boolean isXSDDate();
  boolean isXSDDateTime();
  boolean isXSDDuration();
  boolean isXSDAnyURI();
  
  boolean isComparable();

  String getString() throws DataValueConversionException;
  boolean getBoolean() throws DataValueConversionException;
  int getInt() throws DataValueConversionException;
  long getLong() throws DataValueConversionException;
  float getFloat() throws DataValueConversionException;
  double getDouble() throws DataValueConversionException;
  short getShort() throws DataValueConversionException;
  byte getByte() throws DataValueConversionException;
  Number getNumber() throws DataValueConversionException;

  String toString();
  String toQuotedString();
}
