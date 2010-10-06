
package edu.stanford.smi.protegex.owl.swrl.bridge;

import java.util.List;

import edu.stanford.smi.protegex.owl.swrl.bridge.impl.ArgumentFactoryImpl;
import edu.stanford.smi.protegex.owl.swrl.bridge.xsd.XSDType;
import edu.stanford.smi.protegex.owl.swrl.owlapi.SWRLLiteralArgument;
import edu.stanford.smi.protegex.owl.swrl.owlapi.SWRLIndividualArgument;
import edu.stanford.smi.protegex.owl.swrl.owlapi.SWRLVariable;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.DataValue;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.DataValueConversionException;

public abstract class ArgumentFactory
{
  public static ArgumentFactory getFactory() { return new ArgumentFactoryImpl(); }

  public abstract SWRLVariable createVariableArgument(String variableName);
  
  public abstract ClassArgument createClassArgument(String classURI);
  public abstract SWRLIndividualArgument createIndividualArgument(String individualURI);
  public abstract ObjectPropertyArgument createObjectPropertyArgument(String propertyURI);
  public abstract DataPropertyArgument createDataPropertyArgument(String propertyURI);

  public abstract SWRLLiteralArgument createDataValueArgument(DataValue dataValue);
  
  public abstract SWRLLiteralArgument createDataValueArgument(String s);
  public abstract SWRLLiteralArgument createDataValueArgument(boolean b);
  public abstract SWRLLiteralArgument createDataValueArgument(Boolean b);
  public abstract SWRLLiteralArgument createDataValueArgument(int i);
  public abstract SWRLLiteralArgument createDataValueArgument(long l);
  public abstract SWRLLiteralArgument createDataValueArgument(float f);
  public abstract SWRLLiteralArgument createDataValueArgument(double d);
  public abstract SWRLLiteralArgument createDataValueArgument(short s); 
  public abstract SWRLLiteralArgument createDataValueArgument(Byte b);
  public abstract SWRLLiteralArgument createDataValueArgument(XSDType xsd);
  public abstract SWRLLiteralArgument createDataValueArgument(Object o) throws DataValueConversionException;

  public abstract MultiArgument createMultiArgument();
  public abstract MultiArgument createMultiArgument(List<BuiltInArgument> arguments);
  
  public abstract CollectionArgument createCollectionArgument(String collectionID);
}
