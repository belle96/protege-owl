package edu.stanford.smi.protegex.owl.jena.creator.notont;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.TestCase;
import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protege.model.framestore.NarrowFrameStore;
import edu.stanford.smi.protege.storage.database.DatabaseFrameDb;
import edu.stanford.smi.protege.test.APITestCase;
import edu.stanford.smi.protege.test.APITestCase.DBType;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.database.creator.OwlDatabaseCreator;
import edu.stanford.smi.protegex.owl.database.creator.OwlDatabaseFromFileCreator;
import edu.stanford.smi.protegex.owl.jena.creator.NewOwlProjectCreator;
import edu.stanford.smi.protegex.owl.jena.creator.OwlProjectFromReaderCreator;
import edu.stanford.smi.protegex.owl.jena.creator.OwlProjectFromStreamCreator;
import edu.stanford.smi.protegex.owl.jena.creator.OwlProjectFromUriCreator;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.triplestore.TripleStore;
import edu.stanford.smi.protegex.owl.repository.impl.DatabaseRepository;
import edu.stanford.smi.protegex.owl.repository.impl.LocalFolderRepository;


public class CreatorTestCase extends AbstractCreatorTestCase {
    private Logger log = Log.getLogger(CreatorTestCase.class);
    private EnumSet<DBType> initializedDb = EnumSet.noneOf(DBType.class);
    
    public static String DIRECTORY = "junit/projects/creator";
    
    public static String IMPORTING_LOCATION = "junit/projects/creator/Importing.owl";
    public static String BAD_LOCATION = "junit/projects/creator/NoSuchOntology.owl";
    public static String IMPORTED_LOCATION = "junit/projects/creator/Imported.owl";
    
    public static String IMPORTING_BASE = "http://www.tigraworld.com/protege/Importing.owl";
    public static String IMPORTED_BASE  = "http://www.tigraworld.com/protege/Imported.owl";
    
    
    private Collection errors = new ArrayList();
    
    public void testNewOwlProjectCreator01() throws OntologyLoadException {
        NewOwlProjectCreator creator = new NewOwlProjectCreator();
        creator.create(errors);
        handleErrors();
        OWLModel owlModel = creator.getOwlModel();

        String name = owlModel.getDefaultOWLOntology().getName();
        checkBase(owlModel, name, name);
    }
    
    public void testNewOwlProjectCreator02() throws OntologyLoadException {
        NewOwlProjectCreator creator = new NewOwlProjectCreator();
        creator.setOntologyName(IMPORTED_BASE);
        creator.create(errors);
        handleErrors();
        OWLModel owlModel = creator.getOwlModel();

        checkBase(owlModel, IMPORTED_BASE, IMPORTED_BASE);
    }
    
    public void testUriCreatorRepositoryBaseline() throws OntologyLoadException {
        File ontology = new File(IMPORTING_LOCATION);
        OwlProjectFromUriCreator creator = new OwlProjectFromUriCreator();
        creator.setOntologyUri(ontology.toURI().toString());
        creator.create(errors);
        handleErrors();
        OWLModel owlModel = creator.getOwlModel();
        
        checkUnsucessfulImport(owlModel);
        checkBase(owlModel, IMPORTING_BASE, IMPORTING_BASE);
    }

    public void testUriCreatorRepository() throws OntologyLoadException {
        File ontology = new File(IMPORTING_LOCATION);
        OwlProjectFromUriCreator creator = new OwlProjectFromUriCreator();
        creator.setOntologyUri(ontology.toURI().toString());
        creator.addRepository(new LocalFolderRepository(new File(DIRECTORY)));
        creator.create(errors);
        handleErrors();
        OWLModel owlModel = creator.getOwlModel();
        
        checkSuccessfulImport(owlModel);
        checkBase(owlModel, IMPORTING_BASE, IMPORTING_BASE);
    }
    
    public void testUriCreatorRepositoryImportsDb() 
    throws OntologyLoadException, SQLException, ClassNotFoundException {
        for (DBType dbt : DBType.values()) {
            APITestCase.setDBType(dbt);
            if (!APITestCase.dbConfigured()) {
                continue;
            }
            createImportedDbProject();
            
            File ontology = new File(IMPORTING_LOCATION);
            OwlProjectFromUriCreator creator = new OwlProjectFromUriCreator();
            creator.setOntologyUri(ontology.toURI().toString());

            creator.addRepository(getDbRepository());
            creator.create(errors);
            handleErrors();
            OWLModel owlModel = creator.getOwlModel();

            checkSuccessfulImport(owlModel);
            checkBase(owlModel, IMPORTING_BASE, IMPORTING_BASE);
            
            checkIsDatabaseModel(owlModel, IMPORTED_BASE);
        }
    }

    public void testStreamCreatorRepositoryBaseline() throws OntologyLoadException, FileNotFoundException {
        FileInputStream in = new FileInputStream(new File(IMPORTING_LOCATION));
        OwlProjectFromStreamCreator creator = new OwlProjectFromStreamCreator();
        creator.setStream(in);
        creator.setXmlBase(IMPORTING_BASE);
        creator.create(errors);
        handleErrors();
        OWLModel owlModel = creator.getOwlModel();
        
        checkUnsucessfulImport(owlModel);
        checkBase(owlModel, IMPORTING_BASE, IMPORTING_BASE);
    }
    
    public void testStreamCreatorRepository01() throws OntologyLoadException, FileNotFoundException {
        FileInputStream in = new FileInputStream(new File(IMPORTING_LOCATION));
        OwlProjectFromStreamCreator creator = new OwlProjectFromStreamCreator();
        creator.setStream(in);
        creator.setXmlBase(new File(BAD_LOCATION).toURI().toString());
        creator.addRepository(new LocalFolderRepository(new File(DIRECTORY)));
        creator.create(errors);
        handleErrors();
        OWLModel owlModel = creator.getOwlModel();
        
        checkSuccessfulImport(owlModel);
        checkBase(owlModel, IMPORTING_BASE, new File(BAD_LOCATION).toURI().toString());
    }
    
    public void testStreamCreatorRepository02() throws OntologyLoadException, FileNotFoundException {
        FileInputStream in = new FileInputStream(new File(IMPORTING_LOCATION));
        OwlProjectFromStreamCreator creator = new OwlProjectFromStreamCreator();
        creator.setStream(in);
        creator.setXmlBase(IMPORTING_BASE);
        creator.addRepository(new LocalFolderRepository(new File(DIRECTORY)));
        creator.create(errors);
        handleErrors();
        OWLModel owlModel = creator.getOwlModel();
        
        checkSuccessfulImport(owlModel);
        checkBase(owlModel, IMPORTING_BASE, IMPORTING_BASE);
    }
    
    public void testReaderCreatorRepository01() throws OntologyLoadException, FileNotFoundException {
        FileReader in = new FileReader(new File(IMPORTING_LOCATION));
        OwlProjectFromReaderCreator creator = new OwlProjectFromReaderCreator();
        creator.setReader(in);
        creator.setXmlBase(new File(BAD_LOCATION).toURI().toString());
        creator.addRepository(new LocalFolderRepository(new File(DIRECTORY)));
        creator.create(errors);
        handleErrors();
        OWLModel owlModel = creator.getOwlModel();
        
        checkSuccessfulImport(owlModel);
        checkBase(owlModel, IMPORTING_BASE, new File(BAD_LOCATION).toURI().toString());
    }
    
    public void testReaderCreatorRepository02() throws OntologyLoadException, FileNotFoundException {
        FileReader in = new FileReader(new File(IMPORTING_LOCATION));
        OwlProjectFromReaderCreator creator = new OwlProjectFromReaderCreator();
        creator.setReader(in);
        creator.setXmlBase(IMPORTING_BASE);
        creator.addRepository(new LocalFolderRepository(new File(DIRECTORY)));
        creator.create(errors);
        handleErrors();
        OWLModel owlModel = creator.getOwlModel();
        
        checkSuccessfulImport(owlModel);
        checkBase(owlModel, IMPORTING_BASE, IMPORTING_BASE);
    }
    
    public void testReaderCreatorRepositoryBaseline() throws OntologyLoadException, FileNotFoundException {
        FileReader in = new FileReader(new File(IMPORTING_LOCATION));
        OwlProjectFromReaderCreator creator = new OwlProjectFromReaderCreator();
        creator.setReader(in);
        creator.setXmlBase(IMPORTING_BASE);
        creator.create(errors);
        handleErrors();
        OWLModel owlModel = creator.getOwlModel();
        
        checkUnsucessfulImport(owlModel);
        checkBase(owlModel, IMPORTING_BASE, IMPORTING_BASE);
    }
    
 
    
}
