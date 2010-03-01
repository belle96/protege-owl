
package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.PropertyArgument;

public class PropertyArgumentImpl extends BuiltInArgumentImpl implements PropertyArgument
{
  private String propertyURI;
  
  public PropertyArgumentImpl(String propertyURI) { this.propertyURI = propertyURI; }
  
  public String getURI() { return propertyURI; }
  
  public String toString() { return getURI(); }
  
  public int compareTo(BuiltInArgument o)
  {
  	return propertyURI.compareTo(((PropertyArgument)o).getURI());
  } // compareTo

  public boolean equals(Object obj)
  {
    if(this == obj) return true;
    if((obj == null) || (obj.getClass() != this.getClass())) return false;
    PropertyArgumentImpl impl = (PropertyArgumentImpl)obj;
    return (getURI() == impl.getURI() || (getURI() != null && getURI().equals(impl.getURI())));
    } // equals

  public int hashCode()
  {
    int hash = 12;
    hash = hash + (null == getURI() ? 0 : getURI().hashCode());   
    return hash;
  } // hashCode

} // PropertyArgumentImpl