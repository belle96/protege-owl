 
package edu.stanford.smi.protegex.owl.swrl.bridge.tmp;

import edu.stanford.smi.protegex.owl.swrl.bridge.Argument;

/**
 * Class representing argument to built-ins and atoms
 */
public abstract class ArgumentImpl implements Argument
{
  // There is an equals methods defined for this class.
  private String variableName;
  private boolean isAVariable;
  private boolean isArgumentUnbound;

  public ArgumentImpl()
  {
    variableName = "";
    isAVariable = false;
    isArgumentUnbound = false;
  } // ArgumentImpl

  public ArgumentImpl(String variableName) 
  {
    this.variableName = variableName;
    isAVariable = true; 
    isArgumentUnbound = false;
  } // ArgumentImpl

  public boolean isVariable() { return isAVariable; }

  public void setVariableName(String variableName)
  {
    this.variableName = variableName;
    isAVariable = true;
  } // setVariableName

  public String getVariableName() 
  { 
    return variableName;
  } // getVariableName

  public void setUnbound() { isArgumentUnbound = true; }
  public void setBound() { isArgumentUnbound = false; }
  public boolean isUnbound() { return isArgumentUnbound; }
  public boolean isBound() { return !isArgumentUnbound; }
  
  public boolean equals(Object obj)
  {
    if(this == obj) return true;
    if((obj == null) || (obj.getClass() != this.getClass())) return false;
    ArgumentImpl impl = (ArgumentImpl)obj;
    return (((variableName == impl.variableName || variableName != null && variableName.equals(impl.variableName))) &&
            (isAVariable == impl.isAVariable) &&
            (isArgumentUnbound == impl.isArgumentUnbound));
  } // equals

  public int hashCode()
  {
    int hash = 78;
    hash = hash + (null == variableName ? 0 : variableName.hashCode());
    hash = hash + (isAVariable ? 0 : 1);
    hash = hash + (isArgumentUnbound ? 0 : 1);
    return hash;
  } // hashCode

} // ArgumentImpl
