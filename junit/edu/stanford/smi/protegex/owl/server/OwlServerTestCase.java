package edu.stanford.smi.protegex.owl.server;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protege.server.RemoteProjectManager;
import edu.stanford.smi.protege.server.Server_Test;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

public class OwlServerTestCase extends AbstractJenaTestCase {
  private static final String USER = "Timothy Redmond";
  private static final String PASSWORD = "timothy";
  private static final String PROJECT_NAME = "Pizza";
  
  public static final String PROTEGE_JAR_LOC_PROP = "junit.server.protege.jar";
  
  
  public void setUp() throws Exception {
    super.setUp();
    Server_Test.setProtegeJarLocation(System.getProperty(PROTEGE_JAR_LOC_PROP));
    Server_Test.setMetaProject("junit/projects/metaproject.pprj");
    if (!Server_Test.startServer()) {
      return;
    }
  }
  
  public void testGetProject() {
    if (!Server_Test.isServerRunning()) {
      return;
    }
    Project p = RemoteProjectManager.getInstance().getProject(Server_Test.HOST, USER, PASSWORD, PROJECT_NAME, true);
    assertNotNull(p);
    OWLModel kb = (OWLModel) p.getKnowledgeBase();
    Cls cls = kb.getOWLNamedClass(kb.getNamespaceManager().getDefaultNamespace() + "Pizza");
    Log.getLogger().info("found " + cls + " with frame id = " + cls.getFrameID());
    assertNotNull(cls);
    p.dispose();
  }

}
