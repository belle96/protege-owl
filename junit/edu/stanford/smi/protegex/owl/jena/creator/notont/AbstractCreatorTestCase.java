package edu.stanford.smi.protegex.owl.jena.creator.notont;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protege.model.framestore.NarrowFrameStore;
import edu.stanford.smi.protege.storage.database.DatabaseFrameDb;
import edu.stanford.smi.protege.test.APITestCase;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.database.creator.AbstractOwlDatabaseCreator;
import edu.stanford.smi.protegex.owl.database.creator.OwlDatabaseFromFileCreator;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.triplestore.TripleStore;
import edu.stanford.smi.protegex.owl.repository.Repository;
import edu.stanford.smi.protegex.owl.repository.impl.DatabaseRepository;

public class AbstractCreatorTestCase extends APITestCase {
    Logger log = Log.getLogger(AbstractCreatorTestCase.class);
    
    private EnumSet<DBType> initializedDb = EnumSet.noneOf(DBType.class);
    
    public static String DIRECTORY = "junit/projects/creator";
    
    public static String IMPORTING_LOCATION = "junit/projects/creator/Importing.owl";
    public static String BAD_LOCATION = "junit/projects/creator/NoSuchOntology.owl";
    public static String IMPORTED_LOCATION = "junit/projects/creator/Imported.owl";
    
    public static String IMPORTING_BASE = "http://www.tigraworld.com/protege/Importing.owl";
    public static String IMPORTED_BASE  = "http://www.tigraworld.com/protege/Imported.owl";
    
    
    protected Collection errors = new ArrayList();
    
    /* ------------------------------------------------------------
     * Utilities
     */
    
    protected void createImportedDbProject() throws OntologyLoadException {
        if (initializedDb.contains(APITestCase.getDBType())) {
            return;
        }
        OwlDatabaseFromFileCreator creator = new OwlDatabaseFromFileCreator();
        configureDbCreator(creator);
        creator.setTable("JunitImported");
        creator.setOntologySource(new File(IMPORTED_LOCATION).toURI().toString());
        creator.create(errors);
        handleErrors();
    }
    
    protected void configureDbCreator(AbstractOwlDatabaseCreator creator) {
        creator.setDriver(APITestCase.getDBProperty(APITestCase.JUNIT_DB_DRIVER_PROPERTY));
        creator.setUsername(APITestCase.getDBProperty(APITestCase.JUNIT_DB_USER_PROPERTY));
        creator.setPassword(APITestCase.getDBProperty(APITestCase.JUNIT_DB_PASSWORD_PROPERTY));
        creator.setURL(APITestCase.getDBProperty(APITestCase.JUNIT_DB_URL_PROPERTY));
    }
    
    protected Repository getDbRepository() throws SQLException, ClassNotFoundException {
        return new DatabaseRepository(APITestCase.getDBProperty(APITestCase.JUNIT_DB_DRIVER_PROPERTY),
                                      APITestCase.getDBProperty(APITestCase.JUNIT_DB_URL_PROPERTY),
                                      APITestCase.getDBProperty(APITestCase.JUNIT_DB_USER_PROPERTY),
                                      APITestCase.getDBProperty(APITestCase.JUNIT_DB_PASSWORD_PROPERTY));
    }
    
    protected void checkIsDatabaseModel(OWLModel owlModel, String name) {
        TripleStore importedStore = owlModel.getTripleStoreModel().getTripleStore(name);
        NarrowFrameStore nfs = importedStore.getNarrowFrameStore();
        while (nfs.getDelegate() != null) {
            nfs = nfs.getDelegate();
        }
        assertTrue(nfs instanceof DatabaseFrameDb);
    }
    
    protected void checkSuccessfulImport(OWLModel owlModel) {
        assertNotNull(owlModel.getOWLNamedClass("A"));
        assertNotNull(owlModel.getOWLNamedClass("imported:X"));
        
        assertEquals(1, owlModel.getDefaultOWLOntology().getImports().size());
        assertEquals(IMPORTED_BASE, owlModel.getDefaultOWLOntology().getImports().iterator().next());
        assertNotNull(owlModel.getTripleStoreModel().getTripleStore(IMPORTING_BASE));
        assertNotNull(owlModel.getTripleStoreModel().getTripleStore(IMPORTED_BASE));
    }
    
    protected void checkUnsucessfulImport(OWLModel owlModel) {
        assertNotNull(owlModel.getOWLNamedClass("A"));
        assertNull(owlModel.getOWLNamedClass("imported:X"));
        
        assertEquals(1, owlModel.getDefaultOWLOntology().getImports().size());
        assertEquals(IMPORTED_BASE, owlModel.getDefaultOWLOntology().getImports().iterator().next());
        assertNotNull(owlModel.getTripleStoreModel().getTripleStore(IMPORTING_BASE));
        assertNull(owlModel.getTripleStoreModel().getTripleStore(IMPORTED_BASE));
    }
    
    protected void checkBase(OWLModel owlModel, String expectedNamespace, String xmlBase) {
        TripleStore topTripleStore = owlModel.getTripleStoreModel().getTopTripleStore();
        assertEquals(expectedNamespace + "#", owlModel.getNamespaceManager().getDefaultNamespace()); 
        assertEquals(expectedNamespace + "#", topTripleStore.getNamespaceManager().getDefaultNamespace());
        assertEquals(xmlBase, topTripleStore.getOriginalXMLBase());
        assertEquals(expectedNamespace, owlModel.getDefaultOWLOntology().getName());
    }
    
    protected void handleErrors() {
        if (errors.isEmpty()) {
            return;
        }
        for (Object o : errors) {
            if (o instanceof Throwable) {
                log.log(Level.SEVERE, "exception caught", ((Throwable) o));
            }
            else {
                log.severe("Error = " + o);
            }
        }
        errors.clear();
        fail();
    }
}
