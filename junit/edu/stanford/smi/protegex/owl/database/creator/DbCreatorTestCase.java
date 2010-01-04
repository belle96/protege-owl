package edu.stanford.smi.protegex.owl.database.creator;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protege.test.APITestCase;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.jena.creator.notont.AbstractCreatorTestCase;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.repository.Repository;
import edu.stanford.smi.protegex.owl.repository.impl.LocalFolderRepository;


public class DbCreatorTestCase extends AbstractCreatorTestCase {
    private Logger log = Log.getLogger(DbCreatorTestCase.class);

    public void testOwlDatabaseCreator() throws OntologyLoadException {
        for (DBType dbt : DBType.values()) {
            APITestCase.setDBType(dbt);
            if (!APITestCase.dbConfigured()) {
                continue;
            }
            String tableName = "JunitImported";
            OwlDatabaseCreator creator = new OwlDatabaseCreator(true);
            configureDbCreator(creator);
            creator.setTable("JunitImported");
            creator.create(errors);
            handleErrors();
            OWLModel owlModel = creator.getOwlModel();
            
            String name = owlModel.getDefaultOWLOntology().getName();
            checkBase(owlModel, name, name);
            
            String className = "AnInsertedClass";
            assertNotNull(owlModel.createOWLNamedClass(className));
            owlModel.getProject().dispose();
            
            creator  = new OwlDatabaseCreator(false);
            configureDbCreator(creator);
            creator.setTable(tableName);
            creator.create(errors);
            handleErrors();
            owlModel = creator.getOwlModel();
            assertNotNull(owlModel.getOWLNamedClass(className));
            assertTrue(owlModel.getOWLOntologyClass().getInstanceCount(true) == 1);
            owlModel.getProject().dispose();
            
            creator  = new OwlDatabaseCreator(true);
            configureDbCreator(creator);
            creator.setTable(tableName);
            creator.create(errors);
            handleErrors();
            owlModel = creator.getOwlModel();
            assertNull(owlModel.getOWLNamedClass(className));
            owlModel.getProject().dispose();
        }
    }
    
    public void testOwlDatabaseFromFileCreatorBaseline() throws OntologyLoadException {
        for (DBType dbt : DBType.values()) {
            APITestCase.setDBType(dbt);
            if (!APITestCase.dbConfigured()) {
                continue; 
            }
            OwlDatabaseFromFileCreator creator = new OwlDatabaseFromFileCreator();
            configureDbCreator(creator);
            creator.setTable("JunitImporting");
            creator.setOntologySource(new File(IMPORTING_LOCATION).toURI().toString());
            creator.create(errors);
            handleErrors();
            OWLModel owlModel = creator.getOwlModel();
            checkUnsucessfulImport(owlModel);
            checkBase(owlModel, IMPORTING_BASE, IMPORTING_BASE);

            creator.getProject().dispose();
        }
    }
    
    public void testOwlDatabaseFromFileCreator01() throws OntologyLoadException {
        for (DBType dbt : DBType.values()) {
            APITestCase.setDBType(dbt);
            if (!APITestCase.dbConfigured()) {
                continue;
            }
            OwlDatabaseFromFileCreator creator = new OwlDatabaseFromFileCreator();
            configureDbCreator(creator);
            creator.setTable("JunitImporting");
            creator.setOntologySource(new File(IMPORTING_LOCATION).toURI().toString());
            creator.addRepository(new LocalFolderRepository(new File(DIRECTORY)));
            creator.create(errors);
            handleErrors();
            OWLModel owlModel = creator.getOwlModel();
            
            checkSuccessfulImport(owlModel);
            checkBase(owlModel, IMPORTING_BASE, IMPORTING_BASE);
            
            creator.getProject().dispose();
        }
    }
    
    public void testOwlDatabaseFromFileCreator02() throws OntologyLoadException, SQLException, ClassNotFoundException {
        for (DBType dbt : DBType.values()) {
            APITestCase.setDBType(dbt);
            if (!APITestCase.dbConfigured()) {
                continue;
            }
            createImportedDbProject();
            OwlDatabaseFromFileCreator creator = new OwlDatabaseFromFileCreator();
            configureDbCreator(creator);
            creator.setTable("JunitImporting");
            creator.setOntologySource(new File(IMPORTING_LOCATION).toURI().toString());
            creator.addRepository(getDbRepository());
            creator.create(errors);
            handleErrors();
            OWLModel owlModel = creator.getOwlModel();
            
            checkSuccessfulImport(owlModel);
            checkBase(owlModel, IMPORTING_BASE, IMPORTING_BASE);
            checkIsDatabaseModel(owlModel, IMPORTING_BASE);
            checkIsDatabaseModel(owlModel, IMPORTED_BASE);
            
            creator.getProject().dispose();
        }
    }
    
    @SuppressWarnings("unchecked")
    public void testCreatorWithRepository() throws OntologyLoadException {
        for (DBType dbt : DBType.values()) {
            APITestCase.setDBType(dbt);
            if (!APITestCase.dbConfigured()) {
                continue;
            }
            String dbImportsFileTable = "JunitDbImportsFiles";
            String distinguishingClass = "http://protege.stanford.edu/ontologies/ChAO/changes.rdfs#ClassFromFileVersion";
            List errors = new ArrayList();
            Repository repository = new LocalFolderRepository(new File("junit/projects/creator"));
            
            OwlDatabaseFromFileCreator initialCreator = new OwlDatabaseFromFileCreator();
            configureDbCreator(initialCreator);
            initialCreator.setTable(dbImportsFileTable);
            initialCreator.setOntologySource(new File("junit/projects/creator/DbImportsFile.owl").toURI().toString());
            initialCreator.addRepository(repository);
            initialCreator.create(errors);
            assertTrue(errors.isEmpty());
            assertTrue(initialCreator.getOwlModel().getOWLNamedClass(distinguishingClass) != null);
            initialCreator.getProject().dispose();

            OwlDatabaseCreator creator = new OwlDatabaseCreator(false);
            configureDbCreator(creator);
            creator.setTable(dbImportsFileTable);
            creator.addRepository(repository);
            creator.create(errors);
            assertTrue(errors.isEmpty());
            assertTrue(creator.getOwlModel().getOWLNamedClass(distinguishingClass) != null);
            creator.getProject().dispose();
            
        }
    }
}
