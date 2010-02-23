
package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import edu.stanford.smi.protegex.owl.swrl.bridge.*;

/*
** Class representing a SWRL class atom
*/
public class ClassAtomImpl extends AtomImpl implements ClassAtom
{
  private AtomArgument argument1;
  private String className, prefixedClassName;

  public ClassAtomImpl(String className, String prefixedClassName, AtomArgument argument1)
  {
    this.className = className;
    this.prefixedClassName = prefixedClassName;
    this.argument1 = argument1;
  } // ClassAtomImpl

  public ClassAtomImpl(String className, String prefixedClassName)
  {
    this.className = className;
    this.prefixedClassName = prefixedClassName;
    this.argument1 = null;
  } // ClassAtomImpl

  public void setArgument1(AtomArgument argument1) { this.argument1 = argument1; }
    
  public String getClassName() { return className; }
  public String getPrefixedClassName() { return prefixedClassName; }
  public AtomArgument getArgument1() { return argument1; }

  public String toString() { return getPrefixedClassName() + "(" + getArgument1() + ")"; }
} // ClassAtomImpl

