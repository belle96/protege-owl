
package edu.stanford.smi.protegex.owl.swrl.ddm;

import edu.stanford.smi.protegex.owl.swrl.ddm.impl.*;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLClass;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLProperty;


import java.util.*;
import java.sql.*;

public class DDMFactory
{
  public static DatabaseConnection createDatabaseConnection(Database database, String userID, String password) throws SQLException
  {
    return new DatabaseConnectionImpl(database, userID, password);
  } // createDatabaseConnection

  public static Database createDatabase(String jdbcDriverName, String serverName, String databaseName, int portNumber) 
  {
    return new DatabaseImpl(jdbcDriverName, serverName, databaseName, portNumber);
  } // createDatabase

  public static Table createTable(Database database, String schemaName, String tableName)
  {
    return new TableImpl(database, schemaName, tableName);
  } // createTable

  public static Table createTable(Database database, String schemaName, String tableName, Set<Column> columns)
  {
    return new TableImpl(database, schemaName, tableName, columns);
  } // createTable

  public static Table createTable(Database database, String schemaName, String tableName, Set<Column> columns, 
                                  PrimaryKey primaryKey, Set<ForeignKey> foreignKeys)
  {
    return new TableImpl(database, schemaName, tableName, columns, primaryKey, foreignKeys);
  } // createTable

  public static Column createColumn(String columnName, int columnType) { return new ColumnImpl(columnName, columnType); }

  public static KeyColumn createKeyColumn(String columnName, int columnType) { return new KeyColumnImpl(columnName, columnType); }

  public static PrimaryKeyColumn createPrimaryKeyColumn(String columnName, int columnType) { return new PrimaryKeyColumnImpl(columnName, columnType); }

  public static ForeignKeyColumn createForeignKeyColumn(String columnName, int columnType, String referencedColumnName) 
  { 
    return new ForeignKeyColumnImpl(columnName, columnType, referencedColumnName); 
  } // createForeignKeyColumn

  public static PrimaryKey createPrimaryKey(Table table, Set<PrimaryKeyColumn> keyColumns) { return new PrimaryKeyImpl(table, keyColumns); }

  public static ForeignKey createForeignKey(Table baseTable, Set<ForeignKeyColumn> keyColumns, Table referencedTable) 
  { 
    return new ForeignKeyImpl(baseTable, keyColumns, referencedTable); 
  } // createForeignKey

  public static OWLClassMap createOWLClassMap(OWLClass owlClass, PrimaryKey primaryKey) { return new OWLClassMapImpl(owlClass, primaryKey); }

  public static OWLObjectPropertyMap createOWLObjectPropertyMap(OWLProperty owlProperty, PrimaryKey primaryKey, ForeignKey foreignKey)
  {
    return new OWLObjectPropertyMapImpl(owlProperty, primaryKey, foreignKey);
  } // OWLObjectPropertyMap
      
  public static OWLDatatypePropertyMap createOWLDatatypePropertyMap(OWLProperty owlProperty, PrimaryKey primaryKey, Column valueColumn)
  {
    return new OWLDatatypePropertyMapImpl(owlProperty, primaryKey, valueColumn);
  } // createOWLDatatypePropertyMap

} // DDMFactory
