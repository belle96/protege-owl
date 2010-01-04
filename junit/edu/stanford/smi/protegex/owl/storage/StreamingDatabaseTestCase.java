package edu.stanford.smi.protegex.owl.storage;

import java.net.URI;

import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protege.test.APITestCase;
import edu.stanford.smi.protege.util.URIUtilities;
import edu.stanford.smi.protegex.owl.database.CreateOWLDatabaseFromFileProjectPlugin;
import edu.stanford.smi.protegex.owl.database.OWLDatabaseKnowledgeBaseFactory;
import edu.stanford.smi.protegex.owl.model.OWLModel;

public class StreamingDatabaseTestCase extends APITestCase {
  private static final String fileProject = "junit/projects/pizza.owl";
  
  public void testStreamingDatabase() {
    URI fileLocation = URIUtilities.createURI(fileProject);
    boolean configured = false;
    for (APITestCase.DBType dbtype : DBType.values()) {
      setDBType(dbtype);
      if (dbConfigured()) {
        configured = true;
        CreateOWLDatabaseFromFileProjectPlugin plugin = new CreateOWLDatabaseFromFileProjectPlugin();
        plugin.setKnowledgeBaseFactory(new OWLDatabaseKnowledgeBaseFactory());
        plugin.setDriver(getDBProperty(JUNIT_DB_DRIVER_PROPERTY));
        plugin.setUsername(getDBProperty(JUNIT_DB_USER_PROPERTY));
        plugin.setPassword(getDBProperty(JUNIT_DB_PASSWORD_PROPERTY));
        plugin.setURL(getDBProperty(JUNIT_DB_URL_PROPERTY));
        plugin.setTable(getDBProperty(JUNIT_DB_TABLE_PROPERTY));
        plugin.setUseExistingSources(true);
        plugin.setOntologyInputSource(fileLocation);
        Project p = plugin.createProject();
        OWLModel model = (OWLModel) p.getKnowledgeBase();
        model.setExpandShortNameInMethods(true);
        assertNotNull(model.getRDFResource("Pizza"));
      }
    }
    if (!configured) {
      System.out.println("No test configuration found for database test");
    }
  }

}