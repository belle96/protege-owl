package edu.stanford.smi.protegex.owl.junit_config;

import java.util.Properties;
import java.util.logging.Level;

import edu.stanford.smi.protege.server.Server_Test;
import edu.stanford.smi.protege.test.APITestCase;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.tests.AbstractDIGReasonerTestCase;

public class CheckConfiguration {
  
  public static void main(String args[]) {
    Log.getLogger().setLevel(Level.SEVERE);
    System.out.println("-------------------------------------------");
    checkProperty(AbstractDIGReasonerTestCase.REASONER_URL_PROPERTY, "No reasoner specified");
    checkProperty(Server_Test.JAR_PROPERTY, "Server needs to know the location of the protege.jar file");
    checkDbConfiguration();
    System.out.println("-------------------------------------------");
  }
  
  private static void checkDbConfiguration() {
    boolean isDbConfigured = false;
    for (APITestCase.DBType dbType : APITestCase.DBType.values()) {
      APITestCase.setDBType(dbType);
      if (APITestCase.dbConfigured(false)) {
        System.out.println("" + dbType + " database tests configured");
        isDbConfigured = true;
      }
      else {
        System.out.println("" + dbType + " database tests not configured");
      }
    }
    if (!isDbConfigured) {
      System.out.println("No database tests configured");
    } 
  }

  
  private static void checkProperty(String property, String onFailure) {
    Properties props = APITestCase.getJunitProperties();
    if (props.getProperty(property) == null) {
      System.out.println(onFailure);
      System.out.println("Set the property " + property + " in junit.properties");
    }
  }
}

