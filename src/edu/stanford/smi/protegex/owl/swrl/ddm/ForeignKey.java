
package edu.stanford.smi.protegex.owl.swrl.ddm;

import java.util.Set;

public interface ForeignKey extends Key
{
  Table getReferencedTable();
  Set<ForeignKeyColumn> getForeignKeyColumns();
} // ForeignKey
