
package edu.stanford.smi.protegex.owl.swrl.ddm.impl;

import edu.stanford.smi.protegex.owl.swrl.ddm.*;

import java.util.*;

public abstract class KeyImpl implements Key
{
  private Table baseTable;

  public KeyImpl(Table baseTable)
  {
    this.baseTable = baseTable;
  } // KeyImpl

  public Table getBaseTable() { return baseTable; }
} // KeyImpl
