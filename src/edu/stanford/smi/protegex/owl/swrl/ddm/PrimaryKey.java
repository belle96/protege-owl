
package edu.stanford.smi.protegex.owl.swrl.ddm;

import java.util.Set;

public interface PrimaryKey extends Key
{
  Set<PrimaryKeyColumn> getPrimaryKeyColumns();
} // PrimaryKey
