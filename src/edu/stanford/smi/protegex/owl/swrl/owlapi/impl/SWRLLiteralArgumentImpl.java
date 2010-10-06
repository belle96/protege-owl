
package edu.stanford.smi.protegex.owl.swrl.owlapi.impl;

import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.impl.BuiltInArgumentImpl;
import edu.stanford.smi.protegex.owl.swrl.owlapi.SWRLLiteralArgument;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.DataValue;

/**
 * Implementation of an SWRL data value atom argument
 */
public class SWRLLiteralArgumentImpl extends BuiltInArgumentImpl implements SWRLLiteralArgument 
{
	private DataValue dataValue;
	
	public SWRLLiteralArgumentImpl(DataValue dataValue) { this.dataValue = dataValue; }
	
	public DataValue getLiteral() { return dataValue; }
	
	public String toString() { return dataValue.toString(); }
	
	public int compareTo(BuiltInArgument argument) { return dataValue.compareTo(((SWRLLiteralArgument)argument).getLiteral()); }
	
	public boolean equals(Object obj)
  {
    if(this == obj) return true;
    if((obj == null) || (obj.getClass() != this.getClass())) return false;
    SWRLLiteralArgumentImpl impl = (SWRLLiteralArgumentImpl)obj;
    return (getLiteral() == impl.getLiteral() || (getLiteral() != null && getLiteral().equals(impl.getLiteral())));
    } // equals

  public int hashCode()
  {
    int hash = 12;
    hash = hash + (null == getLiteral() ? 0 : getLiteral().hashCode());   
    return hash;
  } // hashCode

} 
