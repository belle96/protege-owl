
package edu.stanford.smi.protegex.owl.swrl.exceptions;

public class InvalidRuleNameException extends SWRLRuleEngineException
{
  public InvalidRuleNameException(String ruleName) 
  { 
    super("invalid rule name '" + ruleName + "'"); 
  } // InvalidRuleNameException
} // InvalidRuleNameException
