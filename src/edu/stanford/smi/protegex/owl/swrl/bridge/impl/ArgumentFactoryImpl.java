
package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import java.util.List;

import edu.stanford.smi.protegex.owl.swrl.bridge.ArgumentFactory;
import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.ClassArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.DataPropertyArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.DataValueArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.IndividualArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.MultiArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.ObjectPropertyArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.VariableArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.DataValueConversionException;
import edu.stanford.smi.protegex.owl.swrl.bridge.xsd.XSDType;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.DataValue;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.impl.DataValueImpl;

public class ArgumentFactoryImpl extends ArgumentFactory
{
  public VariableArgument createVariableArgument(String variableName) { return new VariableArgumentImpl(variableName); }

  public ClassArgument createClassArgument(String classURI) { return new ClassArgumentImpl(classURI); }
  public ObjectPropertyArgument createObjectPropertyArgument(String propertyURI) { return new ObjectPropertyArgumentImpl(propertyURI); }
  public DataPropertyArgument createDataPropertyArgument(String propertyURI) { return new DataPropertyArgumentImpl(propertyURI); }
  public IndividualArgument createIndividualArgument(String individualURI) { return new IndividualArgumentImpl(individualURI); }

  public DataValueArgument createDataValueArgument(DataValue dataValue) { return new DataValueArgumentImpl(dataValue); }
  
  public DataValueArgument createDataValueArgument(String s) { return new DataValueArgumentImpl(new DataValueImpl(s)); }
  public DataValueArgument createDataValueArgument(boolean b) { return new DataValueArgumentImpl(new DataValueImpl(b)); }
  public DataValueArgument createDataValueArgument(Boolean b) { return new DataValueArgumentImpl(new DataValueImpl(b)); }
  public DataValueArgument createDataValueArgument(int i) { return new DataValueArgumentImpl(new DataValueImpl(i)); }
  public DataValueArgument createDataValueArgument(long l) { return new DataValueArgumentImpl(new DataValueImpl(l)); }
  public DataValueArgument createDataValueArgument(float f) { return new DataValueArgumentImpl(new DataValueImpl(f)); }
  public DataValueArgument createDataValueArgument(double d){ return new DataValueArgumentImpl(new DataValueImpl(d)); }
  public DataValueArgument createDataValueArgument(short s) { return new DataValueArgumentImpl(new DataValueImpl(s)); }
  public DataValueArgument createDataValueArgument(Byte b) { return new DataValueArgumentImpl(new DataValueImpl(b)); }
  public DataValueArgument createDataValueArgument(XSDType xsd) { return new DataValueArgumentImpl(new DataValueImpl(xsd)); }
  
  public DataValueArgument createDataValueArgument(Object o) throws DataValueConversionException { return new DataValueArgumentImpl(new DataValueImpl(o)); }

  public MultiArgument createMultiArgument() { return new MultiArgumentImpl(); }
  public MultiArgument createMultiArgument(List<BuiltInArgument> arguments) { return new MultiArgumentImpl(arguments); }
}
