package edu.stanford.smi.protegex.owl.storage;

import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protege.test.APITestCase;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.database.creator.OwlDatabaseCreator;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.util.ImportHelper;
import edu.stanford.smi.protegex.owl.repository.impl.DatabaseRepository;

public class TaniaCacheTest extends APITestCase {
	private static Logger log = Log.getLogger(TaniaCacheTest.class);
	
    private String importedTable = "junitSuppliedTypes";
    private String importedOntology = "http://smi-protege.stanford.edu/ProvidedTypes.owl";
    private String importingTable = "junitUseTypes";
    private String importingOntology = "http://smi-protege.stanford.edu/UseTypes.owl";
    
    private String typeAClassName = importedOntology + "#" + "TypeA";
    private String typeBClassName = importedOntology + "#" + "TypeB";

    private String aClassName = "A";
    private String bClassName = "B";
    private String cClassName = "C";


	public void testDelete() throws OntologyLoadException, SQLException, ClassNotFoundException {

	    boolean configured = false;
	    for (APITestCase.DBType dbtype : DBType.values()) {
	        setDBType(dbtype);
	        if (dbConfigured()) {
	            configured = true;
	            createImportedOntology();
	            OWLModel model = createOntology();
	            OWLNamedClass typeAClass = model.getOWLNamedClass(typeAClassName);
	            OWLNamedClass typeBClass = model.getOWLNamedClass(typeBClassName);
	            
	            typeAClass.createInstance(aClassName);
	            OWLNamedClass aClass = model.getOWLNamedClass(aClassName);
	            assertTrue(aClass != null);
	            typeAClass.createInstance(bClassName);
	            OWLNamedClass bClass = model.getOWLNamedClass(bClassName);
	            assertTrue(bClass != null);
	            bClass.addSuperclass(aClass);
	            bClass.delete();
	            
	            
	            model.getProject().dispose();
	        }
	    }
	    if (!configured) {
	        log.info("No test configuration found for database test");
	    }
	}
	
	private void createImportedOntology() throws OntologyLoadException {
	       Collection errors = new ArrayList();
	        OwlDatabaseCreator importedCreator = new OwlDatabaseCreator(true);
	        importedCreator.setDriver(getDBProperty(JUNIT_DB_DRIVER_PROPERTY));
	        importedCreator.setUsername(getDBProperty(JUNIT_DB_USER_PROPERTY));
	        importedCreator.setPassword(getDBProperty(JUNIT_DB_PASSWORD_PROPERTY));
	        importedCreator.setURL(getDBProperty(JUNIT_DB_URL_PROPERTY));
	        importedCreator.setTable(importedTable);
	        importedCreator.setOntologyName(importedOntology);
	        importedCreator.create(errors);
	        assertTrue(errors.isEmpty());
	        OWLModel importedModel = importedCreator.getOwlModel();
	        OWLNamedClass typeAClass = importedModel.createOWLNamedClass(typeAClassName);
	        typeAClass.addSuperclass(importedModel.getOWLNamedClassClass());
	        OWLNamedClass typeBClass = importedModel.createOWLNamedClass(typeBClassName);
	        typeBClass.addSuperclass(typeAClass);
	        
	        importedModel.getProject().dispose();
	}
	
	private OWLModel createOntology() throws OntologyLoadException, SQLException, ClassNotFoundException {
        Collection errors = new ArrayList();

	    OwlDatabaseCreator importingCreator = new OwlDatabaseCreator(true);
	    importingCreator.setDriver(getDBProperty(JUNIT_DB_DRIVER_PROPERTY));
	    importingCreator.setUsername(getDBProperty(JUNIT_DB_USER_PROPERTY));
	    importingCreator.setPassword(getDBProperty(JUNIT_DB_PASSWORD_PROPERTY));
	    importingCreator.setURL(getDBProperty(JUNIT_DB_URL_PROPERTY));
	    importingCreator.setTable(importingTable);
	    importingCreator.setOntologyName(importingOntology);
	    importingCreator.create(errors);
	    assertTrue(errors.isEmpty());
	    OWLModel importingModel = importingCreator.getOwlModel();
	    
	    importingModel.getRepositoryManager()
            .addProjectRepository(
                new DatabaseRepository(
                      getDBProperty(JUNIT_DB_DRIVER_PROPERTY),
                      getDBProperty(JUNIT_DB_URL_PROPERTY),
                      getDBProperty(JUNIT_DB_USER_PROPERTY),
                      getDBProperty(JUNIT_DB_PASSWORD_PROPERTY),
                      importedTable));
	    ImportHelper helper = new ImportHelper(importingModel);
	    helper.addImport(URI.create(importedOntology));
	    helper.importOntologies();
	    return importingModel;
	}
}
