  
package edu.stanford.smi.protegex.owl.swrl.ddm.exceptions;

import java.sql.*;

public class JDBCException extends SQLException
{
  public JDBCException(String message) { super(message); }
} // JDBCException

