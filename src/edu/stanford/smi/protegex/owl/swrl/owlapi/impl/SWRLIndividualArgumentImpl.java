
package edu.stanford.smi.protegex.owl.swrl.owlapi.impl;

import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.impl.BuiltInArgumentImpl;
import edu.stanford.smi.protegex.owl.swrl.owlapi.SWRLIndividualArgument;

public class SWRLIndividualArgumentImpl extends BuiltInArgumentImpl implements SWRLIndividualArgument
{
  private String individualURI;
  
  public SWRLIndividualArgumentImpl(String individualURI) { this.individualURI = individualURI; }
  
  public String getURI() { return individualURI; }
  
  public String toString() { return getURI(); }
  
  public int compareTo(BuiltInArgument o)
  {
  	return individualURI.compareTo(((SWRLIndividualArgument)o).getURI());
  } 

  public boolean equals(Object obj)
  {
    if(this == obj) return true;
    if((obj == null) || (obj.getClass() != this.getClass())) return false;
    SWRLIndividualArgumentImpl impl = (SWRLIndividualArgumentImpl)obj;
    return (getURI() == impl.getURI() || (getURI() != null && getURI().equals(impl.getURI())));
    } // equals

  public int hashCode()
  {
    int hash = 12;
    hash = hash + (null == getURI() ? 0 : getURI().hashCode());   
    return hash;
  } // hashCode

}