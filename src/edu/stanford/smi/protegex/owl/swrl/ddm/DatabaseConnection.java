
package edu.stanford.smi.protegex.owl.swrl.ddm;

import java.util.*;
import java.sql.*;

public interface DatabaseConnection
{
  String getUserID();
  String getPassword();
  Database getDatabase();

  Set<Table> getTables() throws SQLException;
  Set<PrimaryKey> getPrimaryKeys() throws SQLException;
  Set<ForeignKey> getForeignKeys() throws SQLException;

  Set<String> getSchemaNames() throws SQLException;
  Set<Table> getTables(String schemaName) throws SQLException;
  Set<PrimaryKey> getPrimaryKeys(String schemaName) throws SQLException;
  Set<ForeignKey> getForeignKeys(String schemaName) throws SQLException;

  void open() throws SQLException;
  ResultSet executeQuery(String query) throws SQLException;
  boolean isOpen();
  void close() throws SQLException;
} // DatabaseConnection

