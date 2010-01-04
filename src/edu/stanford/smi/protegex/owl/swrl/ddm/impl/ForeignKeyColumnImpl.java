
package edu.stanford.smi.protegex.owl.swrl.ddm.impl;

import edu.stanford.smi.protegex.owl.swrl.ddm.*;

public class ForeignKeyColumnImpl extends KeyColumnImpl implements ForeignKeyColumn
{
  private String referencedColumnName;

  public ForeignKeyColumnImpl(String columnName, int columnType, String referencedColumnName)
  {
    super(columnName, columnType);
    this.referencedColumnName = referencedColumnName;
  } // ForeignKeyColumnImpl

  public String getReferencedColumnName() { return referencedColumnName; }
} // ForeignKeyColumn
