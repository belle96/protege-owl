package edu.stanford.smi.protegex.owl.server;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protege.server.RemoteProjectManager;
import edu.stanford.smi.protege.server.Server;
import edu.stanford.smi.protege.server.Server_Test;
import edu.stanford.smi.protege.util.LockStepper;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLOntology;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;
import edu.stanford.smi.protegex.owl.ui.importstree.server.SetActiveOntologyJob;

public class SetActiveOntologyJobTestCase extends AbstractJenaTestCase {
    private static final String USER1 = "Timothy Redmond";
    private static final String PASSWORD1 = "timothy";

    private static final String USER2 = "Tania Tudorache";
    private static final String PASSWORD2 = "tania";

    private static final String PROJECT_NAME = "Importing";

    public static final String PROTEGE_JAR_LOC_PROP = "junit.server.protege.jar";

    public static final String IMPORTING_CLASS = "http://protege.stanford.edu/test/Importing.owl#ImportingClass";
    public static final String IMPORTED_CLASS  = "http://protege.stanford.edu/test/Imported.owl#ImportedClass";

    public static final String IMPORTING_ONTOLOGY_LOCATION = "junit/projects/Importing.pprj";
    public static final String IMPORTED_ONTOLOGY_LOCATION  = "junit/projects/Imported.pprj";

    public static final int SAVE_INTERVAL_MSEC = 1000;

    private void startServer() throws Exception {
        Server_Test.setProtegeJarLocation(System.getProperty(PROTEGE_JAR_LOC_PROP));
        Server_Test.setMetaProject("junit/projects/metaproject.pprj");
        Server_Test.startServer();
    }

    public enum TestStages { 
        testStarted, wroteImported, sawImportedWriteAndWroteImporting;
    };

    @SuppressWarnings("unchecked")
    public void testSetActiveOntologyJob() throws Exception {
        startServer();
        final LockStepper<TestStages> lockStepper = new LockStepper<TestStages>(TestStages.testStarted);
        Project p1 = RemoteProjectManager.getInstance().getProject(Server_Test.HOST, USER1, PASSWORD1, PROJECT_NAME, true);
        OWLModel owlModel = (OWLModel) p1.getKnowledgeBase();
        int importingClassCount = owlModel.getOWLNamedClass(IMPORTING_CLASS).getInstanceCount(false);
        final int importedClassCount  = owlModel.getOWLNamedClass(IMPORTED_CLASS).getInstanceCount(false);

        new Thread() {
            @Override
            public void run() {
                try {
                    Project p2 = RemoteProjectManager.getInstance().getProject(Server_Test.HOST, USER2, PASSWORD2, PROJECT_NAME, true);
                    OWLModel owlModel = (OWLModel) p2.getKnowledgeBase();
                    lockStepper.waitForStage(TestStages.wroteImported);
                    assertTrue(owlModel.getOWLNamedClass(IMPORTED_CLASS).getInstanceCount(false) == importedClassCount + 1);
                    String createdIndividualName = owlModel.getOWLNamedClass(IMPORTING_CLASS).createOWLIndividual(null).getName();
                    owlModel.dispose();
                    lockStepper.stageAchieved(TestStages.sawImportedWriteAndWroteImporting, createdIndividualName);
                }
                catch (Throwable t) {
                    lockStepper.exceptionOffMainThread(TestStages.sawImportedWriteAndWroteImporting, t);
                }
            }
        }.start();

        OWLOntology topLevelOntology = owlModel.getDefaultOWLOntology();
        OWLOntology importedOntology = (OWLOntology) topLevelOntology.getImportResources().iterator().next();
        new SetActiveOntologyJob(owlModel, importedOntology).execute();
        String importedIndividualName = owlModel.getOWLNamedClass(IMPORTED_CLASS).createOWLIndividual(null).getName();
        lockStepper.stageAchieved(TestStages.wroteImported, null);
        String importingIndividualName = (String) lockStepper.waitForStage(TestStages.sawImportedWriteAndWroteImporting);
        owlModel.getOWLNamedClass(IMPORTED_CLASS).createOWLIndividual(null).getName();
        assertTrue(owlModel.getOWLNamedClass(IMPORTING_CLASS).getInstanceCount(false) == importingClassCount + 1);
        owlModel.dispose();

        Server.getInstance().saveAllProjects();

        List errors = new ArrayList();
        OWLModel imported = (OWLModel) new Project(IMPORTED_ONTOLOGY_LOCATION, errors).getKnowledgeBase();
        assertTrue(errors.isEmpty());
        assertTrue(imported.getOWLIndividual(importedIndividualName) != null);
        assertTrue(imported.getOWLIndividual(importingIndividualName) == null);
        imported.dispose();
        OWLModel importing = (OWLModel) new Project(IMPORTING_ONTOLOGY_LOCATION, errors).getKnowledgeBase();
        assertTrue(errors.isEmpty());
        assertTrue(importing.getOWLIndividual(importedIndividualName) != null);
        assertTrue(importing.getOWLIndividual(importingIndividualName) != null);
        imported.dispose();
    }
}
