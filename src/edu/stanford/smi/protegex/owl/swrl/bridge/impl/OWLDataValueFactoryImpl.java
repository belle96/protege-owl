package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import edu.stanford.smi.protegex.owl.swrl.bridge.OWLDataValueFactory;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLDataValue;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.DataValueConversionException;
import edu.stanford.smi.protegex.owl.swrl.bridge.xsd.XSDType;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLLiteral;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.DataValue;

public class OWLDataValueFactoryImpl extends OWLDataValueFactory 
{
	public OWLDataValue getOWLDataValue(OWLLiteral literal) { return (OWLDataValue)literal; } // TODO: reimplement for P4 
	public OWLDataValue getOWLDataValue(DataValue dataValue) { return new OWLDataValueImpl(dataValue); }
	public OWLDataValue getOWLDataValue(String s) { return new OWLDataValueImpl(s); }
  public OWLDataValue getOWLDataValue(boolean b){ return new OWLDataValueImpl(b); }
  public OWLDataValue getOWLDataValue(Boolean b){ return new OWLDataValueImpl(b); }
  public OWLDataValue getOWLDataValue(int i) { return new OWLDataValueImpl(i); }
  public OWLDataValue getOWLDataValue(long l) { return new OWLDataValueImpl(l); }
  public OWLDataValue getOWLDataValue(float f) { return new OWLDataValueImpl(f); }
  public OWLDataValue getOWLDataValue(double d){ return new OWLDataValueImpl(d); }
  public OWLDataValue getOWLDataValue(short s) { return new OWLDataValueImpl(s); }
  public OWLDataValue getOWLDataValue(Byte b) { return new OWLDataValueImpl(b); }
  public OWLDataValue getOWLDataValue(XSDType xsd) { return new OWLDataValueImpl(xsd); }
  public OWLDataValue getOWLDataValue(Object o) throws DataValueConversionException { return new OWLDataValueImpl(o); } 
}
