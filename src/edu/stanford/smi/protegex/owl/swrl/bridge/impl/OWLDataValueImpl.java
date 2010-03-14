package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import edu.stanford.smi.protegex.owl.swrl.bridge.OWLDataValue;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.DataValueConversionException;
import edu.stanford.smi.protegex.owl.swrl.bridge.xsd.XSDType;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.DataValue;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.impl.DataValueImpl;

public class OWLDataValueImpl extends DataValueImpl implements OWLDataValue
{  
	public OWLDataValueImpl(String s) { super(s); }
  public OWLDataValueImpl(boolean b) { super(b); }
  public OWLDataValueImpl(Boolean b) { super(b); }
  public OWLDataValueImpl(int i) { super(i); }
  public OWLDataValueImpl(long l) { super(l); }
  public OWLDataValueImpl(float f) { super(f); }
  public OWLDataValueImpl(double d) { super(d); }
  public OWLDataValueImpl(short s) { super(s); }
  public OWLDataValueImpl(Byte b) { super(b); }
  public OWLDataValueImpl(XSDType xsd) { super(xsd); }

  public OWLDataValueImpl(DataValue dataValue) { super(dataValue); }
  public OWLDataValueImpl(Object o) throws DataValueConversionException { super(o); }
  
  public boolean isOWLStringLiteral() { return isString(); }
}
