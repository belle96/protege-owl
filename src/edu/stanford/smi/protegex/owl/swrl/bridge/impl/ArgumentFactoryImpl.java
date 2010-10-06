
package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import java.util.List;

import edu.stanford.smi.protegex.owl.swrl.bridge.ArgumentFactory;
import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.ClassArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.CollectionArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.DataPropertyArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.MultiArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.ObjectPropertyArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.xsd.XSDType;
import edu.stanford.smi.protegex.owl.swrl.owlapi.SWRLLiteralArgument;
import edu.stanford.smi.protegex.owl.swrl.owlapi.SWRLIndividualArgument;
import edu.stanford.smi.protegex.owl.swrl.owlapi.SWRLVariable;
import edu.stanford.smi.protegex.owl.swrl.owlapi.impl.SWRLLiteralArgumentImpl;
import edu.stanford.smi.protegex.owl.swrl.owlapi.impl.SWRLIndividualArgumentImpl;
import edu.stanford.smi.protegex.owl.swrl.owlapi.impl.SWRLVariableImpl;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.DataValue;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.DataValueConversionException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.impl.DataValueImpl;

public class ArgumentFactoryImpl extends ArgumentFactory
{
  public SWRLVariable createVariableArgument(String variableName) { return new SWRLVariableImpl(variableName); }

  public ClassArgument createClassArgument(String classURI) { return new ClassArgumentImpl(classURI); }
  public ObjectPropertyArgument createObjectPropertyArgument(String propertyURI) { return new ObjectPropertyArgumentImpl(propertyURI); }
  public DataPropertyArgument createDataPropertyArgument(String propertyURI) { return new DataPropertyArgumentImpl(propertyURI); }
  public SWRLIndividualArgument createIndividualArgument(String individualURI) { return new SWRLIndividualArgumentImpl(individualURI); }

  public SWRLLiteralArgument createDataValueArgument(DataValue dataValue) { return new SWRLLiteralArgumentImpl(dataValue); }
  
  public SWRLLiteralArgument createDataValueArgument(String s) { return new SWRLLiteralArgumentImpl(new DataValueImpl(s)); }
  public SWRLLiteralArgument createDataValueArgument(boolean b) { return new SWRLLiteralArgumentImpl(new DataValueImpl(b)); }
  public SWRLLiteralArgument createDataValueArgument(Boolean b) { return new SWRLLiteralArgumentImpl(new DataValueImpl(b)); }
  public SWRLLiteralArgument createDataValueArgument(int i) { return new SWRLLiteralArgumentImpl(new DataValueImpl(i)); }
  public SWRLLiteralArgument createDataValueArgument(long l) { return new SWRLLiteralArgumentImpl(new DataValueImpl(l)); }
  public SWRLLiteralArgument createDataValueArgument(float f) { return new SWRLLiteralArgumentImpl(new DataValueImpl(f)); }
  public SWRLLiteralArgument createDataValueArgument(double d){ return new SWRLLiteralArgumentImpl(new DataValueImpl(d)); }
  public SWRLLiteralArgument createDataValueArgument(short s) { return new SWRLLiteralArgumentImpl(new DataValueImpl(s)); }
  public SWRLLiteralArgument createDataValueArgument(Byte b) { return new SWRLLiteralArgumentImpl(new DataValueImpl(b)); }
  public SWRLLiteralArgument createDataValueArgument(XSDType xsd) { return new SWRLLiteralArgumentImpl(new DataValueImpl(xsd)); }
  
  public SWRLLiteralArgument createDataValueArgument(Object o) throws DataValueConversionException { return new SWRLLiteralArgumentImpl(new DataValueImpl(o)); }

  public MultiArgument createMultiArgument() { return new MultiArgumentImpl(); }
  public MultiArgument createMultiArgument(List<BuiltInArgument> arguments) { return new MultiArgumentImpl(arguments); }
  
  public CollectionArgument createCollectionArgument(String collectionID) { return new CollectionArgumentImpl(collectionID); }
}
