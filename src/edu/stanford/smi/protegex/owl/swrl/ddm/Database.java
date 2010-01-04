
package edu.stanford.smi.protegex.owl.swrl.ddm;

import java.util.*;

public interface Database
{
  String getJDBCDriverName();
  String getServerName();
  String getDatabaseName();
  int getPortNumber();
} // Database
