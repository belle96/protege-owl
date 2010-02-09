
package edu.stanford.smi.protegex.owl.swrl.ddm.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.stanford.smi.protege.plugin.PluginUtilities;
import edu.stanford.smi.protegex.owl.swrl.ddm.Column;
import edu.stanford.smi.protegex.owl.swrl.ddm.exceptions.JDBCException;

public class JDBCConnection
{
  private Connection connection;
  private Statement queryStmt;
  private DatabaseMetaData dbmd;

  public JDBCConnection(String jdbcConnectionString, String id, String password) throws SQLException
  {
    try {
      connection = DriverManager.getConnection(jdbcConnectionString, id, password);
      
      queryStmt = connection.createStatement();
      
      dbmd = connection.getMetaData();
    } catch (SQLException e) {
      throw new JDBCException("error creating JDBC connection '" + jdbcConnectionString + "': " + e.getMessage());
    } // try
  } // JDBCConnection

  public static String getConnectionString(String jdbcDriverName, String serverName, String databaseName, int portNumber) throws SQLException
  {
    String url = "";

    if (jdbcDriverName.equals("SQLServerJDBCDriver2000")) {
      url = new String("jdbc:microsoft:sqlserver://" + serverName + ":" + portNumber + ";databaseName=" + databaseName + ";");
    } else if (jdbcDriverName.equals("SQLServerJDBCDriver2005")) {
      url = new String("jdbc:sqlserver://" + serverName + ":" + portNumber + ";instanceName=" + databaseName + ";");
    } else if (jdbcDriverName.equals("SunJDBCDriver")) { 
      url = new String("jdbc:odbc:" + databaseName);
    } else if (jdbcDriverName.equals("OracleThin")) { 
      url = new String("jdbc:oracle:thin:@" + serverName + ":" + portNumber + ":" + databaseName);
    } else if (jdbcDriverName.equals("com.mysql.jdbc.Driver")) {
      url = new String("jdbc:mysql://" + serverName + ":" + portNumber + "/" + databaseName);
    } else {
      if (jdbcDriverName.equals("")) throw new JDBCException("no JDBC driver specified");
      else throw new JDBCException("unknown JDBC driver '" + jdbcDriverName + "'");
    } // if

    loadDrivers(jdbcDriverName);

    return url;
  } // getConnectionString

  public static void loadDrivers(String jdbcDriverName) throws SQLException
  {
    if (jdbcDriverName.equals("SQLServerJDBCDriver2000")) {
      try { PluginUtilities.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver", true);
      } catch (Exception e) {
	throw new JDBCException("failed to load Microsoft SQL Server 2000 JDBC driver");
      } // try
    } else if (jdbcDriverName.equals("SQLServerJDBCDriver2005")) {
      try { PluginUtilities.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver", true);
      } catch (Exception e) {
	throw new JDBCException("failed to load Microsoft SQL Server 2005 JDBC driver");
      } // try
    } else if (jdbcDriverName.equals("SunJDBCDriver")) { 
      try { PluginUtilities.forName("sun.jdbc.odbc.JdbcOdbcDriver", true);
      } catch (Exception e) {
        throw new JDBCException("failed to load Sun JDBC driver");
      } // try
    } else if (jdbcDriverName.equals("OracleThin")) { 
      try { PluginUtilities.forName("oracle.jdbc.driver.OracleDriver", true);
      } catch (Exception e) {
      throw new JDBCException("failed to load Oracle JDBC driver");
      } // try
    } else if (jdbcDriverName.equals("com.mysql.jdbc.Driver")) {
      try {
        PluginUtilities.forName("com.mysql.jdbc.Driver", true).newInstance();
      } catch (Exception e) {
	throw new JDBCException("failed to load MySQL JDBC driver");
      } // try
    } else {
      if (jdbcDriverName.equals("")) throw new JDBCException("no JDBC driver specified");
      else throw new JDBCException("unknown JDBC driver '" + jdbcDriverName + "'");
    } // if
  } // getConnectionString
  
  public synchronized ResultSet executeQuery(String query) throws SQLException
  {
    ResultSet rs = null;

    if (queryStmt == null) queryStmt = connection.createStatement();
    
    rs = queryStmt.executeQuery(query);

    return rs;
  } // executeQuery

  public synchronized int executeUpdate(String command) throws SQLException
  {
    int result;

    if (queryStmt == null) queryStmt = connection.createStatement();
    
    result = queryStmt.executeUpdate(command);

    return result;
  } // executeUpdate

  public synchronized int getMaxColumnNameLength() throws SQLException
  {
    return dbmd.getMaxColumnNameLength();
  } // getMaxColumnNameLenght

  public synchronized int getMaxCatalogNameLength() throws SQLException
  {
    return dbmd.getMaxCatalogNameLength();
  } // getMaxCatalogNameLength

  public synchronized int getMaxCharLiteralLength() throws SQLException
  {
    return dbmd.getMaxCharLiteralLength();
  } // getMaxCharLiteralLength

  public synchronized String getDatabaseProductName() throws SQLException
  {
    return dbmd.getDatabaseProductName();
  } // getMaxCharLiteralLength

  public synchronized int getMaxRowSize() throws SQLException
  {
    return dbmd.getMaxRowSize();
  } // getMaxRowSize

  public synchronized int getMaxTableNameLength() throws SQLException
  {
    return dbmd.getMaxTableNameLength();
  } // getMaxRowSize

  public synchronized int getMaxSchemaNameLength() throws SQLException
  {
    return dbmd.getMaxSchemaNameLength();
  } // getMaxSchemaNameLength

  public synchronized String getUserName() throws SQLException
  {
    return dbmd.getUserName();
  } // getUserName

  public synchronized Set<String> getCatalogs() throws SQLException
  {
    Set<String> catalogs = new HashSet<String>();
    ResultSet rs = dbmd.getCatalogs();

    while (rs.next()) {
      String s = rs.getString(1);
      catalogs.add(s);
    } // while

    rs.close();
      
    return catalogs;
  } // getCatalogs

  public synchronized Set<String> getSchemaNames() throws SQLException
  {
    Set<String> schemaNames = new HashSet<String>();
    ResultSet rs = dbmd.getSchemas();

    while (rs.next()) {
      String schemaName = rs.getString(1);
      schemaNames.add(schemaName);
    } // while

    rs.close();

    return schemaNames;
  } // getSchemaNames

  public synchronized Set<String> getTableNames(String schemaName) throws SQLException
  {
    Set<String> tableNames = new HashSet<String>();
    ResultSet rs = dbmd.getTables(null, schemaName, null, null);
    
    while (rs.next()) {
      String s = rs.getString("TABLE_NAME");
      tableNames.add(s);
    } // while

    rs.close();

    return tableNames;
  } // getTableNames

  public synchronized Set<String> getPrimaryKeyColumnNames(String schemaName, String tableName) throws SQLException
  {
    Set<String> keyColumnNames = new HashSet<String>();
    ResultSet rs = dbmd.getPrimaryKeys(null, schemaName, tableName);
 
    while (rs.next()) {
      String s = rs.getString("COLUMN_NAME");
      keyColumnNames.add(s);
    } // while

    rs.close();

    return keyColumnNames;
  } // getPrimaryKeyColumnNames

  /**
   ** Returns a (possibly empty) map of maps of the form <referencedSchemaTableName, <baseKeyColumnName, referencedKeyColumnName>>. The
   ** referencedSchemaTableName string will be of the form 
   */
  public synchronized Map<String, Map<String, String>> getForeignKeys(String schemaName, String tableName) throws SQLException
  {
    ResultSet rs = dbmd.getImportedKeys(null, schemaName, tableName);
    Map<String, Map<String, String>> result = new HashMap<String, Map<String, String>>();
    Map<String, String> keyColumnNamesMap = new HashMap<String, String>();
    int oldKeySequenceNumber = -1;
    String oldReferencedTableName = null, oldReferencedSchemaName = null;

    while (rs.next()) {
      String baseTableName = rs.getString("PKTABLE_NAME");
      String baseKeyColumnName = rs.getString("PKCOLUMN_NAME");
      String referencedSchemaName = rs.getString("FKTABLE_SCHEM");
      String referencedTableName = rs.getString("FKTABLE_NAME");
      String referencedKeyColumnName = rs.getString("FKCOLUMN_NAME");
      int keySequenceNumber = rs.getInt("KEY_SEQ");

      if (!baseTableName.equalsIgnoreCase(tableName)) 
        throw new JDBCException("expecting table '" + tableName + "', got '" + baseTableName + "' in call to getImportedKeys");

      if (oldKeySequenceNumber == -1 || keySequenceNumber == oldKeySequenceNumber) { // Still processing the first or same key
        keyColumnNamesMap.put(baseKeyColumnName, referencedKeyColumnName);
      } else { // New key - first save old one
        result.put(buildSchemaTableName(referencedSchemaName, referencedTableName), keyColumnNamesMap);
        keyColumnNamesMap = new HashMap<String, String>();
      } // if

      oldKeySequenceNumber = keySequenceNumber;
      oldReferencedSchemaName = referencedSchemaName;
      oldReferencedTableName = referencedTableName;
    } // while

    if (oldKeySequenceNumber != -1) result.put(buildSchemaTableName(oldReferencedSchemaName, oldReferencedTableName), keyColumnNamesMap); // Save the final key

    rs.close();

    return result;
  } // getForeignKeys

  private String buildSchemaTableName(String schemaName, String tableName)
  {
    if (schemaName == null) return tableName;
    else return schemaName + "." + tableName;
  } // buildSchemaTableName

  public synchronized void closeConnection() throws SQLException
  {
    connection.close();
    connection = null;
    dbmd = null;
  } // closeConnection

  public synchronized void closeStatements() throws SQLException
  {
    if (queryStmt != null) queryStmt.close();

    queryStmt = null;
  } // closeStatements

  public synchronized String getColumnTypeName(String schemaName, String tableName, String columnName) throws SQLException
  {
    ResultSet rs;
    String columnType = "";

    rs = dbmd.getColumns(null, schemaName, tableName, columnName);
    rs.next();

    if (rs.getString("COLUMN_NAME").equalsIgnoreCase(columnName)) columnType = rs.getString("TYPE_NAME");

    return columnType;
  } // getColumnTypeName

  public synchronized int getColumnType(String schemaName, String tableName, String columnName) throws SQLException
  {
    ResultSet rs = dbmd.getColumns(null, schemaName, tableName, columnName);
    int columnType = -1;

    rs.next();
    if (rs.getString("COLUMN_NAME").equalsIgnoreCase(columnName)) columnType = rs.getInt("DATA_TYPE");

    return columnType;
  } // getColumnType

  public synchronized Set<String> getColumnTypeNames(String schemaName, String tableName) throws SQLException
  {
    ResultSetMetaData rsmd;
    Set<String> columnTypeNames = new HashSet<String>();
    ResultSet rs = dbmd.getColumns(null, schemaName, tableName, null);
    int i = 0;

    rsmd = rs.getMetaData();

    while (rs.next()) columnTypeNames.add(rsmd.getColumnTypeName(i++));

    rs.close();

    return columnTypeNames;
  } // getColumnTypesNames

  public synchronized Set<Column> getColumns(String schemaName, String tableName) throws SQLException
  {
    Set<Column> result = new HashSet<Column>();
    
    for (String columnName : getColumnNames(schemaName, tableName)) {
      Column column = new ColumnImpl(columnName, getColumnType(schemaName, tableName, columnName));
      result.add(column);
    } // for

    return result;
  } // getColumns

  public synchronized Set<String> getColumnNames(String schemaName, String tableName) throws SQLException
  {
    ResultSet rs;    
    Set<String> columnNames = new HashSet<String>();

    rs = dbmd.getColumns(null, schemaName, tableName, null);

    while (rs.next()) columnNames.add(rs.getString("COLUMN_NAME"));

    rs.close();
      
    return columnNames;
  } // getColumnNames

} // JDBCConnection
