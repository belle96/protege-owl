
package edu.stanford.smi.protegex.owl.swrl.bridge.tmp;

import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.ClassArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.sqwrl.impl.BuiltInArgumentImpl;

public class ClassArgumentImpl extends BuiltInArgumentImpl implements ClassArgument
{
  private String classURI;
  
  public ClassArgumentImpl(String classURI) { this.classURI = classURI; }
  
  public String getURI() { return classURI; }
  
  public boolean isNamedClass() { return true; }
  
  public int compareTo(BuiltInArgument o)
  {
    return classURI.compareTo(((ClassArgumentImpl)o).getURI());
  } // compareTo

} // ClassArgumentImpl
