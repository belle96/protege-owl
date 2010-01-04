package edu.stanford.smi.protegex.owl.jena.creator;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protege.test.APITestCase;
import edu.stanford.smi.protege.test.APITestCase.DBType;
import edu.stanford.smi.protegex.owl.database.creator.OwlDatabaseFromFileCreator;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.repository.impl.DatabaseRepository;

public class FileImportsDatabase extends TestCase {
    public final static String TRAVEL_LOCATION = "junit/projects/creator/travel.owl";
    public final static String PIZZA_LOCATION = "junit/projects/creator/pizza.owl";
    public final static String TABLE = "JunitTravel";
    
    public final static String TRAVEL_CLASS_URI = "http://www.owl-ontologies.com/travel.owl#AccommodationRating";
    public final static String PIZZA_CLASS_URI  = "http://www.co-ode.org/ontologies/pizza/2005/10/18/pizza.owl#Country";
    
    
    public void testFileImportsDatabase() throws OntologyLoadException, SQLException, ClassNotFoundException {
        for (DBType dbt : DBType.values()) {
            APITestCase.setDBType(dbt);
            if (!APITestCase.dbConfigured()) {
                continue;
            }
            createDbProject();

            OwlProjectFromUriCreator creator = new OwlProjectFromUriCreator();
            creator.setOntologyUri(new File(PIZZA_LOCATION).toURI().toString());
            DatabaseRepository  repository = new DatabaseRepository(APITestCase.getDBProperty(APITestCase.JUNIT_DB_DRIVER_PROPERTY),
                                                                    APITestCase.getDBProperty(APITestCase.JUNIT_DB_URL_PROPERTY),
                                                                    APITestCase.getDBProperty(APITestCase.JUNIT_DB_USER_PROPERTY),
                                                                    APITestCase.getDBProperty(APITestCase.JUNIT_DB_PASSWORD_PROPERTY),
                                                                    TABLE);
            creator.addRepository(repository);
            List  errors = new ArrayList();
            creator.create(errors);
            assertTrue(errors.isEmpty());
            OWLModel owlModel = creator.getOwlModel();
            // check the import
            OWLNamedClass rating = owlModel.getOWLNamedClass(TRAVEL_CLASS_URI);
            assertNotNull(rating);
            assertTrue(rating.getInstances(false).size() == 3);
            
            // check the importing
            OWLNamedClass country = owlModel.getOWLNamedClass(PIZZA_CLASS_URI);
            assertNotNull(country);
            assertTrue(country.getInstances(false).size() == 5);
        }
    }
    
    private void createDbProject() throws OntologyLoadException {
        OwlDatabaseFromFileCreator creator = new OwlDatabaseFromFileCreator();
        creator.setDriver(APITestCase.getDBProperty(APITestCase.JUNIT_DB_DRIVER_PROPERTY));
        creator.setUsername(APITestCase.getDBProperty(APITestCase.JUNIT_DB_USER_PROPERTY));
        creator.setPassword(APITestCase.getDBProperty(APITestCase.JUNIT_DB_PASSWORD_PROPERTY));
        creator.setURL(APITestCase.getDBProperty(APITestCase.JUNIT_DB_URL_PROPERTY));
        creator.setTable(TABLE);
        creator.setOntologySource(TRAVEL_LOCATION);

        List errors = new ArrayList();
        creator.create(errors);
        assertTrue(errors.isEmpty());
    }

}
