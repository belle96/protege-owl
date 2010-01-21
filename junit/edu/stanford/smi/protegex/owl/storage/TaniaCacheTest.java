package edu.stanford.smi.protegex.owl.storage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protege.test.APITestCase;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.database.creator.OwlDatabaseCreator;
import edu.stanford.smi.protegex.owl.model.OWLModel;

public class TaniaCacheTest extends APITestCase {
	private static Logger log = Log.getLogger(TaniaCacheTest.class);
	
    private String importedTable = "junitSuppliedTypes";
    private String importedOntology = "http://smi-protege.stanford.edu/ProvidedTypes.owl";
    private String importingTable = "junitUseTypes";
    private String importingOntology = "http://smi-protege.stanford.edu/UseTypes.owl";


	private void testDelete() {

	    boolean configured = false;
	    for (APITestCase.DBType dbtype : DBType.values()) {
	        setDBType(dbtype);
	        if (dbConfigured()) {
	            configured = true;
	        }

	    }
	    if (!configured) {
	        log.info("No test configuration found for database test");
	    }
	}
	
	private OWLModel createOntology() throws OntologyLoadException {
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

	    OwlDatabaseCreator importingCreator = new OwlDatabaseCreator(true);
	    importingCreator.setDriver(getDBProperty(JUNIT_DB_DRIVER_PROPERTY));
	    importingCreator.setUsername(getDBProperty(JUNIT_DB_USER_PROPERTY));
	    importingCreator.setPassword(getDBProperty(JUNIT_DB_PASSWORD_PROPERTY));
	    importingCreator.setURL(getDBProperty(JUNIT_DB_URL_PROPERTY));
	    importingCreator.setTable(importingTable);
	    importingCreator.setOntologyName(importingOntology);
	    importingCreator.create(errors);
	    assertTrue(errors.isEmpty());

	    return importingCreator.getOwlModel();
	}
}
