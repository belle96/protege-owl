 
package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.BuiltInException;

/**
 ** Class representing argument to built-ins
 */
public class BuiltInArgumentImpl implements BuiltInArgument
{
  // There is an equals methods defined for this class.
  private String variableName;
  private boolean isAVariable;
  private BuiltInArgument builtInResult; // Used to store result of binding for unbound arguments
  private boolean isArgumentUnbound;

  public BuiltInArgumentImpl()
  {
    variableName = "";
    isAVariable = false;
    builtInResult = null; 
    isArgumentUnbound = false;
  } // ArgumentImpl

  public BuiltInArgumentImpl(String variableName) 
  {
    this.variableName = variableName;
    isAVariable = true;
    builtInResult = null; 
    isArgumentUnbound = false;
  } // BuiltInArgumentImpl

  public boolean isVariable() { return isAVariable; }

  public void setVariableName(String variableName)
  {
    this.variableName = variableName;
    isAVariable = true;
  } // setVariableName

  public String getVariableName() 
  {
    //if (!isVariable()) throw new BuiltInException("attempt to get variable name of non variable argument '" + this.toString() + "'");
    
    return variableName;
  } // getVariableName

  public void setBuiltInResult(BuiltInArgument builtInResult) throws BuiltInException
  { 
    if (!isUnbound()) throw new BuiltInException("attempt to bind value to bound argument '" + this.toString() + "'");
    
    isArgumentUnbound = false;

    this.builtInResult = builtInResult; 
  } // setBuiltInResult

  public BuiltInArgument getBuiltInResult()
  { 
    //if (!isUnbound()) throw new BuiltInException("attempt to retrieve binding from a non bound argument '" + this.toString() + "'");

    return builtInResult; 
  } // getBuiltInResult

  public void setUnbound() { isArgumentUnbound = true; }
  public boolean isUnbound() { return isArgumentUnbound; }
  public boolean isBound() { return !isArgumentUnbound; }
  public boolean hasBuiltInResult() { return builtInResult != null; }

  public String toString()
  {
    if (builtInResult != null) return builtInResult.toString();
    else return "?" + variableName;
  } // toString

  public boolean equals(Object obj)
  {
    if(this == obj) return true;
    if((obj == null) || (obj.getClass() != this.getClass())) return false;
    BuiltInArgumentImpl impl = (BuiltInArgumentImpl)obj;
    return (((variableName == impl.variableName || variableName != null && variableName.equals(impl.variableName))) &&
            (isAVariable == impl.isAVariable) &&
            (isArgumentUnbound == impl.isArgumentUnbound) &&
            ((builtInResult == impl.builtInResult) || (builtInResult != null && builtInResult.equals(impl.builtInResult))));
  } // equals

  public int hashCode()
  {
    int hash = 78;
    hash = hash + (null == variableName ? 0 : variableName.hashCode());
    hash = hash + (isAVariable ? 0 : 1);
    hash = hash + (null == builtInResult ? 0 : builtInResult.hashCode());
    hash = hash + (isArgumentUnbound ? 0 : 1);
    return hash;
  } // hashCode

} // BuiltInArgumentImpl
