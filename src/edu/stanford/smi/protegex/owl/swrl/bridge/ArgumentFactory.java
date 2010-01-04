
package edu.stanford.smi.protegex.owl.swrl.bridge;

import java.util.List;

import edu.stanford.smi.protegex.owl.swrl.bridge.impl.ArgumentFactoryImpl;
import edu.stanford.smi.protegex.owl.swrl.bridge.xsd.XSDType;

public abstract class ArgumentFactory
{
  public static ArgumentFactory getFactory() { return new ArgumentFactoryImpl(); }

  public abstract ClassArgument createClassArgument(String className);
  public abstract IndividualArgument createIndividualArgument(String individualName);
  public abstract ObjectPropertyArgument createObjectPropertyArgument(String propertyName);
  public abstract DataPropertyArgument createDataPropertyArgument(String propertyName);

  public abstract DataValueArgument createDataValueArgument(String s);
  public abstract DataValueArgument createDataValueArgument(Number n);
  public abstract DataValueArgument createDataValueArgument(boolean b);
  public abstract DataValueArgument createDataValueArgument(int i);
  public abstract DataValueArgument createDataValueArgument(long l);
  public abstract DataValueArgument createDataValueArgument(float f);
  public abstract DataValueArgument createDataValueArgument(double d);
  public abstract DataValueArgument createDataValueArgument(short s); 
  public abstract DataValueArgument createDataValueArgument(Byte b);
  public abstract DataValueArgument createDataValueArgument(XSDType xsd);

  public abstract DataValueArgument createDataValueArgument(OWLDataValue dataValue);

  public abstract MultiArgument createMultiArgument(String variableName);
  public abstract MultiArgument createMultiArgument(String variableName, List<BuiltInArgument> arguments);
} // ArgumentFactory
