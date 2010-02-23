
package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.IndividualArgument;

public class IndividualArgumentImpl extends BuiltInArgumentImpl implements IndividualArgument
{
  private String individualURI;
  
  public IndividualArgumentImpl(String individualURI) { this.individualURI = individualURI; }
  
  public String getURI() { return individualURI; }
  
  public int compareTo(BuiltInArgument o)
  {
    return individualURI.compareTo(((IndividualArgumentImpl)o).getURI());
  } // compareTo

} // IndividualArgumentImpl