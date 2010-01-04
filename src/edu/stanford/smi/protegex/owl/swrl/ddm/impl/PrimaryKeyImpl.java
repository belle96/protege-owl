
package edu.stanford.smi.protegex.owl.swrl.ddm.impl;

import edu.stanford.smi.protegex.owl.swrl.ddm.*;

import java.util.*;

public class PrimaryKeyImpl extends KeyImpl implements PrimaryKey
{
  private Set<PrimaryKeyColumn> primaryKeyColumns;

  public PrimaryKeyImpl(Table baseTable, Set<PrimaryKeyColumn> primaryKeyColumns)
  {
    super(baseTable);
    this.primaryKeyColumns = primaryKeyColumns;
  } // PrimaryKeyImpl

  public boolean isComposite() { return primaryKeyColumns.size() > 1; }
  public Set<PrimaryKeyColumn> getPrimaryKeyColumns() { return primaryKeyColumns; }
} // PrimaryKeyImpl
