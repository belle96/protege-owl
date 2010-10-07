
package edu.stanford.smi.protegex.owl.swrl.bridge.exceptions;

public class BuiltInException extends SWRLRuleEngineBridgeException 
{
  private static final long serialVersionUID = -1637824554034882606L;
  
  public BuiltInException() { super(); } 
  public BuiltInException(String message) { super(message); } 
  public BuiltInException(String message, Throwable cause) { super(message, cause); }
}
