package edu.stanford.smi.protegex.owl.database.creator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protege.storage.database.DatabaseKnowledgeBaseFactory;
import edu.stanford.smi.protege.test.APITestCase;
import edu.stanford.smi.protege.util.PropertyList;
import edu.stanford.smi.protegex.owl.database.OWLDatabaseKnowledgeBaseFactory;
import edu.stanford.smi.protegex.owl.jena.creator.OwlProjectFromStreamCreator;
import edu.stanford.smi.protegex.owl.jena.creator.notont.AbstractCreatorTestCase;
import edu.stanford.smi.protegex.owl.model.OWLModel;

public class DbMemoryLeakTestCase extends AbstractCreatorTestCase {
    public static final int REPETITIONS = 1;
    public static final String PIZZA_FILE = "examples/pizza.owl";
    
    public void testStreamingParseMemoryLeak() throws MalformedURLException, OntologyLoadException {
        for (DBType dbt : DBType.values()) {
            APITestCase.setDBType(dbt);
            if (!APITestCase.dbConfigured()) {
                continue;
            }
            List<WeakReference<OWLModel>>  disposedModels = new ArrayList<WeakReference<OWLModel>>();
            for (int counter  = 0; counter < REPETITIONS; counter++) {
                OwlDatabaseFromFileCreator creator = new OwlDatabaseFromFileCreator();
                configureDbCreator(creator);
                creator.setTable("JunitPizza");
                creator.setOntologySource(new File(PIZZA_FILE).toURL().toString());
                creator.create(errors);
                handleErrors();
                OWLModel owlModel = creator.getOwlModel();
                disposedModels.add(new WeakReference<OWLModel>(owlModel));
                creator.getProject().dispose();
            }
            reallyCollectGarbagePleaseOk();
            for (WeakReference<OWLModel> garbage : disposedModels) {
                assertNull(garbage.get());
            }
        }
    }
    
    public void testConvertToDbMemoryLeak() throws OntologyLoadException, FileNotFoundException, InterruptedException {
        String tableName = "JunitPizza";
        for (DBType dbt : DBType.values()) {
            APITestCase.setDBType(dbt);
            if (!APITestCase.dbConfigured()) {
                continue;
            }
            List<WeakReference<OWLModel>>  disposedModels = new ArrayList<WeakReference<OWLModel>>();
            for (int counter = 0; counter < REPETITIONS; counter++) {
                OwlProjectFromStreamCreator fcreator = new OwlProjectFromStreamCreator();
                fcreator.setStream(new FileInputStream(new File(PIZZA_FILE)));
                fcreator.create(errors);
                handleErrors();
                assertNotNull(fcreator.getOwlModel().getOWLNamedClass("CheeseyPizza"));
                
                Project fileProject = fcreator.getProject(); 
                OWLModel fileModel = fcreator.getOwlModel();

                OWLDatabaseKnowledgeBaseFactory factory = new OWLDatabaseKnowledgeBaseFactory();
                PropertyList sources = PropertyList.create(fileProject.getInternalProjectKnowledgeBase());
                DatabaseKnowledgeBaseFactory.setSources(sources, 
                                                        APITestCase.getDBProperty(APITestCase.JUNIT_DB_DRIVER_PROPERTY), 
                                                        APITestCase.getDBProperty(APITestCase.JUNIT_DB_URL_PROPERTY),
                                                        tableName, 
                                                        APITestCase.getDBProperty(APITestCase.JUNIT_DB_USER_PROPERTY), 
                                                        APITestCase.getDBProperty(APITestCase.JUNIT_DB_PASSWORD_PROPERTY));
                factory.saveKnowledgeBase(fileModel, sources, errors);
                handleErrors();
                fileProject.dispose();

                OwlDatabaseCreator  dbcreator = new OwlDatabaseCreator(false);
                configureDbCreator(dbcreator);
                dbcreator.setTable("JunitPizza");
                dbcreator.create(errors);
                handleErrors();
                assertNotNull(dbcreator.getOwlModel().getOWLNamedClass("CheeseyPizza"));
                disposedModels.add(new WeakReference<OWLModel>(dbcreator.getOwlModel()));
                dbcreator.getProject().dispose();
                
                disposedModels.add(new  WeakReference<OWLModel>(fileModel));
                fileProject.dispose();
            }
            
            reallyCollectGarbagePleaseOk();
            for (WeakReference<OWLModel> garbage : disposedModels) {
                assertNull(garbage.get());
            }
        }
    }
    
    public void reallyCollectGarbagePleaseOk() {
        // ok this is very funky!!  Just because the junit fails doesn't mean there is a problem...
        for (int counter = 0; counter < 20; counter++) {
            System.gc();
        }
    }

}
