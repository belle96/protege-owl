 
package edu.stanford.smi.protegex.owl.swrl.bridge.sqwrl.impl;

import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.MultiArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.BuiltInException;
import edu.stanford.smi.protegex.owl.swrl.bridge.tmp.ArgumentImpl;

/**
 * Class representing argument to built-ins
 */
public abstract class BuiltInArgumentImpl extends ArgumentImpl implements BuiltInArgument
{
  // There is an equals methods defined for this class.
  private BuiltInArgument builtInResult; // Used to store result of binding for unbound arguments

  public BuiltInArgumentImpl()
  {
  	super();
    builtInResult = null; 
  } // ArgumentImpl

  public BuiltInArgumentImpl(String variableName) 
  {
  	super(variableName);
    builtInResult = null; 
  } // BuiltInArgumentImpl

  public void setBuiltInResult(BuiltInArgument builtInResult) throws BuiltInException
  { 
    if (!isUnbound()) throw new BuiltInException("attempt to bind value to bound argument '" + this.toString() + "'");
    
    setBound();

    this.builtInResult = builtInResult; 
  } // setBuiltInResult

  public BuiltInArgument getBuiltInResult()
  { 
    //if (!isUnbound()) throw new BuiltInException("attempt to retrieve binding from a non bound argument '" + this.toString() + "'");

    return builtInResult; 
  } // getBuiltInResult

  public boolean hasBuiltInResult() { return builtInResult != null; }
  public boolean hasBuiltInMultiArgumentResult() { return hasBuiltInResult() && builtInResult instanceof MultiArgument; }
  
  public MultiArgument getBuiltInMultiArgumentResult() throws BuiltInException
  {
	  if (!hasBuiltInMultiArgumentResult()) throw new BuiltInException("argument is not a multi-argument");
	  
	  return (MultiArgument)builtInResult;
  } // getBuiltInMultiArgumentResult  

  public String toString()
  {
    if (builtInResult != null) return builtInResult.toString();
    else return "?" + getVariableName();
  } // toString

  public boolean equals(Object obj)
  {
    if(this == obj) return true;
    if((obj == null) || (obj.getClass() != this.getClass())) return false;
    BuiltInArgumentImpl impl = (BuiltInArgumentImpl)obj;
    return this.equals(impl) && 
           ((builtInResult == impl.builtInResult) || (builtInResult != null && builtInResult.equals(impl.builtInResult)));
  } // equals

  public int hashCode()
  {
    int hash = 78;
    hash = hash + super.hashCode();
    hash = hash + (null == builtInResult ? 0 : builtInResult.hashCode());
    return hash;
  } // hashCode

} // BuiltInArgumentImpl
