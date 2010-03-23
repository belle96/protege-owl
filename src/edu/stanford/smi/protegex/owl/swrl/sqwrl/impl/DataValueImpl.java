
package edu.stanford.smi.protegex.owl.swrl.sqwrl.impl;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import edu.stanford.smi.protegex.owl.swrl.bridge.xsd.XSDAnyURI;
import edu.stanford.smi.protegex.owl.swrl.bridge.xsd.XSDDate;
import edu.stanford.smi.protegex.owl.swrl.bridge.xsd.XSDDateTime;
import edu.stanford.smi.protegex.owl.swrl.bridge.xsd.XSDDuration;
import edu.stanford.smi.protegex.owl.swrl.bridge.xsd.XSDTime;
import edu.stanford.smi.protegex.owl.swrl.bridge.xsd.XSDType;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.DataValue;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.SQWRLResultValue;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.DataValueConversionException;

/**
 * Implementation of a data value object that represents Java and XML Schema primitive data literals.
 */
public class DataValueImpl  implements DataValue {
	private Object value; // TODO: This value object should implement Comparable.

  public DataValueImpl() { value = null; } 
  public DataValueImpl(String s) { value = s; } 
  public DataValueImpl(boolean b) { value = Boolean.valueOf(b); }
  public DataValueImpl(Boolean b) { value = b; }
  public DataValueImpl(Byte b) { value = b; }
  public DataValueImpl(int i) { value = Integer.valueOf(i); }
  public DataValueImpl(long l) { value = Long.valueOf(l); }
  public DataValueImpl(float f) { value = Float.valueOf(f); }
  public DataValueImpl(double d) { value = Double.valueOf(d); }
  public DataValueImpl(short s) { value = Short.valueOf(s); }
  public DataValueImpl(XSDType value) { this.value = value; }
  
  public DataValueImpl(DataValue dataValue) { this.value = dataValue.getValue(); }

  public DataValueImpl(Object o) throws DataValueConversionException
  { 
    if (!((o instanceof Number) || (o instanceof String) || (o instanceof Boolean) || (o instanceof Byte) || (o instanceof XSDType)))
      throw new DataValueConversionException("cannot convert value of type " + o.getClass().getCanonicalName() + " to DataValue"); 

    value = o;
  } 

  public boolean isString() { return value instanceof String; }
  public boolean isBoolean() { return value instanceof Boolean; }
  public boolean isNumeric() { return value instanceof Number; }
  public boolean isInteger() { return value instanceof Integer; }
  public boolean isLong() { return value instanceof Long; }
  public boolean isFloat() { return value instanceof Float; }
  public boolean isDouble() { return value instanceof Double; }
  public boolean isShort() { return value instanceof Short; }
  public boolean isByte() { return value instanceof Byte; }
  
  // XSD types
  public boolean isXSDType() { return value instanceof XSDType; }
  public boolean isXSDTime() { return value instanceof XSDTime; }
  public boolean isXSDDate() { return value instanceof XSDDate; }
  public boolean isXSDDateTime() { return value instanceof XSDDateTime; }
  public boolean isXSDDuration() { return value instanceof XSDDuration;}
  public boolean isXSDAnyURI() { return value instanceof XSDAnyURI; }
  
  public boolean isComparable() { return isNumeric() || isString() || isXSDTime() || isXSDDateTime() || isXSDDuration(); }

  public String getString() throws DataValueConversionException 
  { 
    if (!isString()) 
      throw new DataValueConversionException("cannot convert value of type " + value.getClass().getCanonicalName() + " to String"); 
    return (String)value; 
  } 

  public Number getNumber() throws DataValueConversionException 
  { 
    if (!isNumeric()) 
      throw new DataValueConversionException("cannot convert value of type " + value.getClass().getCanonicalName() + " to Number"); 
    return (Number)value; 
  } 

  public XSDType getXSDType() throws DataValueConversionException 
  { 
    if (!isXSDType()) 
      throw new DataValueConversionException("cannot convert value of type " + value.getClass().getCanonicalName() + " to XSDType"); 
    return (XSDType)value; 
  }

  public boolean getBoolean() throws DataValueConversionException 
  { 
    if (!isBoolean()) 
      throw new DataValueConversionException("cannot convert value of type " + value.getClass().getCanonicalName() + " to boolean"); 

    return ((Boolean)value).booleanValue(); 
  }

  public int getInt() throws DataValueConversionException 
  {
    int result = 0;

    if (isInteger()) result = ((Integer)value).intValue(); 
    else if (isShort()) result = (int)((Short)value).shortValue();
    else throw new DataValueConversionException("cannot convert value of type " + value.getClass().getCanonicalName() + " to int"); 

    return result;
  }

  public long getLong() throws DataValueConversionException 
  { 
    long result = 0;

    if (isLong()) result = ((Long)value).longValue(); 
    else if (isInteger()) result = (long)((Integer)value).intValue();
    else if (isShort()) result = (long)((Short)value).shortValue();
    else throw new DataValueConversionException("cannot convert value of type " + value.getClass().getCanonicalName() + " to long"); 

    return result;
  }

  // Some precision loss possible going from integer and long to
  // float. cf. http://www.particle.kth.se/~lindsey/JavaCourse/Book/Part1/Java/Chapter02/castsMixing.html
  public float getFloat() throws DataValueConversionException 
  { 
    float result = 0;

    if (isFloat()) result = ((Float)value).floatValue(); 
    else if (isInteger()) result = (float)((Integer)value).intValue();
    else if (isLong()) result = (float)((Long)value).longValue();
    else if (isShort()) result = (float)((Short)value).shortValue();
    else throw new DataValueConversionException("cannot convert value of type " + value.getClass().getCanonicalName() + " to float"); 

    return result;
  } 

  // Some precision loss possible going from long to
  // double. cf. http://www.particle.kth.se/~lindsey/JavaCourse/Book/Part1/Java/Chapter02/castsMixing.html
  public double getDouble() throws DataValueConversionException 
  { 
    double result = 0.0;

    if (isDouble()) result = ((Double)value).doubleValue(); 
    else if (isFloat()) result = (double)((Float)value).floatValue();
    else if (isInteger()) result = (double)((Integer)value).intValue();
    else if (isLong()) result = (double)((Long)value).longValue();
    else if (isShort()) result = (double)((Short)value).shortValue();
    else throw new DataValueConversionException("cannot convert value of type " + value.getClass().getCanonicalName() + " to double"); 

    return result;
  }

  public short getShort() throws DataValueConversionException 
  { 
    if (!isShort()) 
      throw new DataValueConversionException("cannot convert value of type " + value.getClass().getCanonicalName() + " to short"); 
    return ((Short)value).shortValue(); 
  }

  public byte getByte() throws DataValueConversionException 
  {
    if (!isByte()) 
      throw new DataValueConversionException("cannot convert value of type " + value.getClass().getCanonicalName() + " to byte"); 
    return ((java.lang.Byte)value).byteValue();
  }

  public String toString() 
  { 
    return "" + value;
  }
  
  public String toQuotedString() 
  { 
  	// Escape non-escaped double quote characters; for humans: [^\\]" -> \\"
  	return "\"" + value.toString().replaceAll("[~\\\\]\"", "\\\\\"") + "\""; 
  } 

  public Object getValue() { return value; }
  private void setValue(Object value) { this.value = value; }

  public boolean equals(Object obj)
  {
    if(this == obj) return true;
    if((obj == null) || (obj.getClass() != this.getClass())) return false;
    DataValueImpl info = (DataValueImpl)obj;
    return (value != null && info.value != null && value.equals(info.value));
  } 

  public int hashCode()
  {
    int hash = 66;
    hash = hash + (null == value ? 0 : value.toString().hashCode());
    return hash;
  }

  // TODO: this is a mess. Clean up.
  public int compareTo(SQWRLResultValue dataValue)
  {
    return  ((Comparable)value).compareTo(((DataValueImpl)dataValue).getValue());
  } 

  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
  {
    setValue(in.readObject());
  } // readExternal

  public void writeExternal(ObjectOutput out) throws IOException
  {
    out.writeObject(getValue());
  }
}
