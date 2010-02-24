
package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import edu.stanford.smi.protegex.owl.swrl.bridge.IndividualArgument;

public class IndividualArgumentImpl extends BuiltInArgumentImpl implements IndividualArgument
{
  private String individualURI;
  
  public IndividualArgumentImpl(String individualURI) { this.individualURI = individualURI; }
  
  public String getURI() { return individualURI; }
  
  public String toString() { return getURI(); }
  
  public int compareTo(IndividualArgument o)
  {
    return individualURI.compareTo(o.getURI());
  } // compareTo

  public boolean equals(Object obj)
  {
    if(this == obj) return true;
    if((obj == null) || (obj.getClass() != this.getClass())) return false;
    IndividualArgumentImpl impl = (IndividualArgumentImpl)obj;
    return (getURI() == impl.getURI() || (getURI() != null && getURI().equals(impl.getURI())));
    } // equals

  public int hashCode()
  {
    int hash = 12;
    hash = hash + (null == getURI() ? 0 : getURI().hashCode());   
    return hash;
  } // hashCode

} // IndividualArgumentImpl