
package edu.stanford.smi.protegex.owl.swrl.ddm;

import java.util.*;

public interface Table
{
  Database getDatabase();
  PrimaryKey getPrimaryKey();
  String getTableName();
  String getSchemaName();
  Set<Column> getColumns(); 
  Set<ForeignKey> getForeignKeys();

  boolean hasPrimaryKey();
  boolean hasForeignKeys();

  void setPrimaryKey(PrimaryKey primaryKey);
  void setForeignKeys(Set<ForeignKey> foreignKeys);
  void addForeignKey(ForeignKey foreignKey);
  void addColumns(Set<Column> columns);
} // Table
