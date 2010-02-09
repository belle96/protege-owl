
package edu.stanford.smi.protegex.owl.swrl.ddm.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import edu.stanford.smi.protegex.owl.swrl.ddm.Column;
import edu.stanford.smi.protegex.owl.swrl.ddm.DDMFactory;
import edu.stanford.smi.protegex.owl.swrl.ddm.Database;
import edu.stanford.smi.protegex.owl.swrl.ddm.DatabaseConnection;
import edu.stanford.smi.protegex.owl.swrl.ddm.ForeignKey;
import edu.stanford.smi.protegex.owl.swrl.ddm.ForeignKeyColumn;
import edu.stanford.smi.protegex.owl.swrl.ddm.PrimaryKey;
import edu.stanford.smi.protegex.owl.swrl.ddm.PrimaryKeyColumn;
import edu.stanford.smi.protegex.owl.swrl.ddm.Table;
import edu.stanford.smi.protegex.owl.swrl.ddm.exceptions.JDBCException;

public class DatabaseConnectionImpl implements DatabaseConnection
{
  private String jdbcConnectionString, userID, password;
  private Database database;
  private JDBCConnection jdbcConnection = null;

  private Map<String, Table> tableMap;

  public DatabaseConnectionImpl(Database database, String userID, String password) throws SQLException
  {
    this.database = database;
    this.userID = userID;
    this.password = password;
    jdbcConnectionString = JDBCConnection.getConnectionString(database.getJDBCDriverName(), database.getServerName(), 
                                                              database.getDatabaseName(), database.getPortNumber());
    tableMap = new HashMap<String, Table>();
  } // DatabaseConnectionImpl

  public void open() throws JDBCException, SQLException
  {
    if (isOpen()) jdbcConnection.closeConnection();

    jdbcConnection = new JDBCConnection(jdbcConnectionString, userID, password);
  } // open

  public boolean isOpen() { return jdbcConnection != null; }

  public Database getDatabase() { return database; }

  public ResultSet executeQuery(String query) throws SQLException
  {
    if (!isOpen()) open();

    return jdbcConnection.executeQuery(query);
  } // executeQuery

  public void close() throws SQLException
  {
    if (jdbcConnection != null) jdbcConnection.closeConnection();
  } // close

  public String getUserID() { return userID; }
  public String getPassword() { return password; }

  public Set<String> getSchemaNames() throws SQLException
  {
    Set<String> result = new HashSet<String>();

    if (isOpen()) result = jdbcConnection.getSchemaNames();

    return result;
  } // getSchemaNames

  public Set<Table> getTables(String schemaName) throws SQLException
  {
    Set<Table> result = new HashSet<Table>();
    Table table;

    if (isOpen()) {
      for (String tableName : jdbcConnection.getTableNames(schemaName)) {

        table = getTable(schemaName, tableName);
        processColumns(table);
        processPrimaryKey(table);
        processForeignKeys(table);
             
        result.add(table);
      } // for
    } // if
    
    return result;
  } // getTables
  
  private void processColumns(Table table) throws SQLException
  {
    Set<Column> columns = new HashSet<Column>();
    String schemaName = table.getSchemaName();
    String tableName = table.getTableName();

    for (String columnName : jdbcConnection.getColumnNames(schemaName, tableName)) {
      int columnType = jdbcConnection.getColumnType(schemaName, tableName, columnName);
      columns.add(DDMFactory.createColumn(columnName, columnType));
    } // for
  } // processColumns
        
  private void processPrimaryKey(Table table) throws SQLException
  {
    Set<PrimaryKeyColumn> primaryKeyColumns = new HashSet<PrimaryKeyColumn>();
    String schemaName = table.getSchemaName();
    String tableName = table.getTableName();

    for (String columnName : jdbcConnection.getPrimaryKeyColumnNames(schemaName, tableName)) {
      int columnType = jdbcConnection.getColumnType(schemaName, tableName, columnName);
      primaryKeyColumns.add(DDMFactory.createPrimaryKeyColumn(columnName, columnType));
    } // for
    
    if (!primaryKeyColumns.isEmpty()) {
      PrimaryKey primaryKey = DDMFactory.createPrimaryKey(table, primaryKeyColumns);
      table.setPrimaryKey(primaryKey);
    } // if
  } // processPrimaryKey
   
  private void processForeignKeys(Table baseTable) throws SQLException
  {
    String schemaName = baseTable.getSchemaName();
    String tableName = baseTable.getTableName();
    Map<String, Map<String, String>> foreignKeyMap = jdbcConnection.getForeignKeys(schemaName, tableName);
    Set<ForeignKeyColumn> foreignKeyColumns = new HashSet<ForeignKeyColumn>();

    for (String referencedSchemaTableName : foreignKeyMap.keySet()) {
      String referencedSchemaName = StringUtils.substringBefore(referencedSchemaTableName, ".");
      String referencedTableName = referencedSchemaTableName.equals("") ? 
                                   referencedSchemaTableName : StringUtils.substringAfter(referencedSchemaTableName, ".");
      Map<String, String> keyColumnsNameMap = foreignKeyMap.get(referencedTableName);
      Table referencedTable = getTable(referencedSchemaName, referencedTableName);
      ForeignKey foreignKey;

      for (String baseKeyColumnName : keyColumnsNameMap.keySet()) {
        String referencedKeyColumnName = keyColumnsNameMap.get(baseKeyColumnName);
        int columnType = jdbcConnection.getColumnType(referencedSchemaName, referencedTableName, referencedKeyColumnName);
        ForeignKeyColumn foreignKeyColumn = DDMFactory.createForeignKeyColumn(baseKeyColumnName, columnType, referencedKeyColumnName);
        foreignKeyColumns.add(foreignKeyColumn);
      } // for

      foreignKey = DDMFactory.createForeignKey(baseTable, foreignKeyColumns, referencedTable);
      baseTable.addForeignKey(foreignKey);
    } // for
        
  } // processForeignKeys

  public Set<PrimaryKey> getPrimaryKeys(String schemaName) throws SQLException
  {
    Set<PrimaryKey> result = new HashSet<PrimaryKey>();

    if (isOpen()) {
      for (Table table : getTables(schemaName)) if (table.hasPrimaryKey()) result.add(table.getPrimaryKey());
    } // if
    return result;
  } // getPrimaryKeys

  public Set<ForeignKey> getForeignKeys(String schemaName) throws SQLException
  {
    Set<ForeignKey> result = new HashSet<ForeignKey>();

    if (isOpen()) {
      for (Table table : getTables(schemaName)) if (table.hasForeignKeys()) result.addAll(table.getForeignKeys());
    } // if
    return result;
  } // getForeignKeys

  public Set<Table> getTables() throws SQLException { return getTables(null); }
  public Set<PrimaryKey> getPrimaryKeys() throws SQLException { return getPrimaryKeys(null); }
  public Set<ForeignKey> getForeignKeys() throws SQLException { return getForeignKeys(null); }

  private Table getTable(String schemaName, String tableName) throws SQLException
  {
    String key = schemaName + ":" + tableName;
    Table table = null;

    if (tableMap.containsKey(key)) table = tableMap.get(key);
    else {
      table = DDMFactory.createTable(database, schemaName, tableName);
      tableMap.put(key, table);
    } // if

    return table;
  } // getTable      

} // DatabaseConnection

