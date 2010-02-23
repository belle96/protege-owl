
package edu.stanford.smi.protegex.owl.swrl.bridge.tmp;

import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.PropertyArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.sqwrl.impl.BuiltInArgumentImpl;

public class PropertyArgumentImpl extends BuiltInArgumentImpl implements PropertyArgument
{
  private String propertyURI;
  
  public PropertyArgumentImpl(String propertyURI) { this.propertyURI = propertyURI; }
  
  public String getURI() { return propertyURI; }
  
  public int compareTo(BuiltInArgument o)
  {
    return propertyURI.compareTo(((PropertyArgumentImpl)o).getURI());
  } // compareTo

} // PropertyArgumentImpl