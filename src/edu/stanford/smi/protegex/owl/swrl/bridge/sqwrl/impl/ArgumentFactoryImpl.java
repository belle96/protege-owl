
package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import java.util.List;

import edu.stanford.smi.protegex.owl.swrl.bridge.ArgumentFactory;
import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.ClassArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.DataPropertyArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.DataValueArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.IndividualArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.MultiArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLDataValue;
import edu.stanford.smi.protegex.owl.swrl.bridge.ObjectPropertyArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.xsd.XSDType;

public class ArgumentFactoryImpl extends ArgumentFactory
{
  public ClassArgument createClassArgument(String className) { return new OWLClassImpl(className); }

  public IndividualArgument createIndividualArgument(String individualName) { return new OWLIndividualImpl(individualName); }

  public ObjectPropertyArgument createObjectPropertyArgument(String propertyName) { return new OWLObjectPropertyImpl(propertyName); }
  public DataPropertyArgument createDataPropertyArgument(String propertyName) { return new OWLDataPropertyImpl(propertyName); }

  public DataValueArgument createDataValueArgument(String s) { return new OWLDataValueImpl(s); }
  public DataValueArgument createDataValueArgument(Number n) { return new OWLDataValueImpl(n); }
  public DataValueArgument createDataValueArgument(boolean b){ return new OWLDataValueImpl(b); }
  public DataValueArgument createDataValueArgument(int i) { return new OWLDataValueImpl(i); }
  public DataValueArgument createDataValueArgument(long l) { return new OWLDataValueImpl(l); }
  public DataValueArgument createDataValueArgument(float f) { return new OWLDataValueImpl(f); }
  public DataValueArgument createDataValueArgument(double d){ return new OWLDataValueImpl(d); }
  public DataValueArgument createDataValueArgument(short s) { return new OWLDataValueImpl(s); }
  public DataValueArgument createDataValueArgument(Byte b) { return new OWLDataValueImpl(b); }
  public DataValueArgument createDataValueArgument(XSDType xsd) { return new OWLDataValueImpl(xsd); }

  public DataValueArgument createDataValueArgument(OWLDataValue dataValue) { return dataValue; }

  public MultiArgument createMultiArgument(String variableName) { return new MultiArgumentImpl(variableName); }
  public MultiArgument createMultiArgument(String variableName, List<BuiltInArgument> arguments) { return new MultiArgumentImpl(variableName, arguments); }
} // ArgumentFactory
