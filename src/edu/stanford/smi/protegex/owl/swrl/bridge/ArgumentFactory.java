
package edu.stanford.smi.protegex.owl.swrl.bridge;

import java.util.List;

import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.DataValueConversionException;
import edu.stanford.smi.protegex.owl.swrl.bridge.impl.ArgumentFactoryImpl;
import edu.stanford.smi.protegex.owl.swrl.bridge.xsd.XSDType;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.DataValue;

public abstract class ArgumentFactory
{
  public static ArgumentFactory getFactory() { return new ArgumentFactoryImpl(); }

  public abstract ClassArgument createClassArgument(String classURI);
  public abstract IndividualArgument createIndividualArgument(String individualURI);
  public abstract ObjectPropertyArgument createObjectPropertyArgument(String propertyURI);
  public abstract DataPropertyArgument createDataPropertyArgument(String propertyURI);

  public abstract DataValueArgument createDataValueArgument(DataValue dataValue);
  
  public abstract DataValueArgument createDataValueArgument(String s);
  public abstract DataValueArgument createDataValueArgument(boolean b);
  public abstract DataValueArgument createDataValueArgument(Boolean b);
  public abstract DataValueArgument createDataValueArgument(int i);
  public abstract DataValueArgument createDataValueArgument(long l);
  public abstract DataValueArgument createDataValueArgument(float f);
  public abstract DataValueArgument createDataValueArgument(double d);
  public abstract DataValueArgument createDataValueArgument(short s); 
  public abstract DataValueArgument createDataValueArgument(Byte b);
  public abstract DataValueArgument createDataValueArgument(XSDType xsd);
  public abstract DataValueArgument createDataValueArgument(Object o) throws DataValueConversionException;

  public abstract VariableBuiltInArgument createVariableBuiltInArgument(String variableName);
  public abstract MultiArgument createMultiArgument();
  public abstract MultiArgument createMultiArgument(List<BuiltInArgument> arguments);
}
