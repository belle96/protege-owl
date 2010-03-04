package edu.stanford.smi.protegex.owl.swrl.sqwrl;

import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.DataValueConversionException;
import edu.stanford.smi.protegex.owl.swrl.bridge.xsd.XSDType;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.impl.ClassValueImpl;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.impl.DataPropertyValueImpl;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.impl.DataValueImpl;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.impl.IndividualValueImpl;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.impl.ObjectPropertyValueImpl;

public class SQWRLResultValueFactory 
{
	public ClassValue createClassValue(String classURI) { return new ClassValueImpl(classURI); }
  public IndividualValue createIndividualValue(String individualURI) { return new IndividualValueImpl(individualURI); }
  public ObjectPropertyValue createObjectPropertyValue(String propertyURI) { return new ObjectPropertyValueImpl(propertyURI); }
  public DataPropertyValue createDataPropertyValue(String propertyURI) { return new DataPropertyValueImpl(propertyURI); }
  
  public DataValue createDataValue(String s) { return new DataValueImpl(s); }
  public DataValue createDataValue(boolean b) { return new DataValueImpl(b); }
  public DataValue createDataValue(Boolean b) { return new DataValueImpl(b); }
  public DataValue createDataValue(int i) { return new DataValueImpl(i); }
  public DataValue createDataValue(long l) { return new DataValueImpl(l); }
  public DataValue createDataValue(float f) { return new DataValueImpl(f); }
  public DataValue createDataValue(double d) { return new DataValueImpl(d); }
  public DataValue createDataValue(short s) { return new DataValueImpl(s); }
  public DataValue createDataValue(XSDType xsd) { return new DataValueImpl(xsd); }
  public DataValue createDataValue(Object o) throws DataValueConversionException { return new DataValueImpl(o); }
}
