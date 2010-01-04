package edu.stanford.smi.protegex.owl.tests;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.TestCase;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protege.model.ValueType;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protege.util.URIUtilities;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.Jena;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.jena.parser.ProtegeOWLParser;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLOntology;
import edu.stanford.smi.protegex.owl.model.ProtegeNames;
import edu.stanford.smi.protegex.owl.model.XSPNames;
import edu.stanford.smi.protegex.owl.model.impl.XMLSchemaDatatypes;

/**
 * The base class of various JUnit tests for OWL.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public abstract class AbstractOWLTestCase extends TestCase {
    private static transient Logger log = Log.getLogger(AbstractOWLTestCase.class);
    
    public final static String TEST_ONTOLOGY_LOCATION_PROPERTY = "junit.testontologies";

    protected JenaOWLModel owlModel;

    protected Project project;

    protected OWLNamedClass owlThing;
    
    private static Properties junitProperties;


    public static void assertContains(Object value, Collection collection) {
        assertTrue(collection.contains(value));
    }


    public static void assertContains(Object value, Iterator it) {
        assertTrue(Jena.set(it).contains(value));
    }


    public static void assertContainsNot(Object value, Iterator it) {
        assertFalse(Jena.set(it).contains(value));
    }


    public static void assertHasValue(OntResource individual, OntProperty property,
                                      ValueType valueType, Object expectedValue) {
        XSDDatatype type = XMLSchemaDatatypes.getDefaultXSDDatatype(valueType);
        for (NodeIterator it = individual.listPropertyValues(property); it.hasNext();) {
            RDFNode node = it.nextNode();
            if (node instanceof Literal) {
                Literal literal = (Literal) node;
                if (type.equals(literal.getDatatype())) {
                    if (literal.getValue().equals(expectedValue)) {
                        return;
                    }
                }
            }
        }
        assertFalse("Value " + expectedValue + " not found for " +
                property + " at " + individual, true);
    }


    public static void assertSize(int size, Collection c) {
        assertEquals(size, c.size());
    }


    public static void assertSize(int size, Iterator it) {
        assertEquals(size, list(it).size());
    }


    protected void dumpRDF() {
        Jena.dumpRDF(owlModel.getOntModel(), log, Level.FINE);
    }
    
    public static String getRemoteOntologyRoot() {
      return getJunitProperties().getProperty(TEST_ONTOLOGY_LOCATION_PROPERTY, 
                                              "http://protege.stanford.edu/junitOntologies/testset/");
    }


    public static URI getRemoteOntologyURI(String localFileName) {
        try {
            String ontologyLoc = getRemoteOntologyRoot();
            return new URI(ontologyLoc + localFileName);
        }
        catch (Exception ex) {
            Log.getLogger().log(Level.SEVERE, "Exception caught", ex);
            return null;
        }
    }


    public static URI getTestOntologyURI(String localFileName) {
        try {
            final String fileName =
                    "/projects/protege-owl/src/edu/stanford/smi/protegex/owl/jena/tests/" +
                            localFileName;
            return new File(fileName).toURI();
        }
        catch (Exception ex) {
            assertFalse("Could not find file: " + localFileName, true);
            return null;
        }
    }


    public boolean hasTypedLiteral(Resource resource, DatatypeProperty property, Object value) {
        OntResource type = property.getRange();
        for (StmtIterator it = resource.listProperties(property); it.hasNext();) {
            Statement s = it.nextStatement();
            RDFNode node = s.getObject();
            if (node.canAs(Literal.class)) {
                Literal literal = (Literal) node.as(Literal.class);
                if (type.getURI().equals(literal.getDatatype().getURI()) &&
                        value.equals(literal.getValue())) {
                    return true;
                }
            }
        }
        return false;
    }


    protected static List list(Iterator it) {
        List result = new ArrayList();
        while (it.hasNext()) {
            result.add(it.next());
        }
        return result;
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        OntDocumentManager.getInstance().reset(true);
                
        owlModel = ProtegeOWL.createJenaOWLModel();
        owlModel.setExpandShortNameInMethods(true);
        project = owlModel.getProject();
        owlThing = owlModel.getOWLThingClass();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        project.dispose();
        project = null;
        owlModel = null;
        owlThing = null;
    }
    
    public static Properties getJunitProperties() {
      if (junitProperties != null) {
        return junitProperties;
      }
      try {
        Properties dbp = new Properties();
        String dbPropertyFile = "junit.properties";
        InputStream is = new FileInputStream(dbPropertyFile);
        dbp.load(is);
        junitProperties = dbp;
        return junitProperties;
      } catch (Exception e) {
        log.severe("Exception reading junit.properties - probably needs to be configured by the user");
        return null;
      }
    }
    
    /**
     * Makes sure that the Protege meta ontology is imported in an ontology tag
     * that has rdf:about="".
     */
    public boolean ensureProtegeMetaOntologyImported() {
        OWLOntology owlOntology = owlModel.getDefaultOWLOntology();
        for (Iterator imports = owlOntology.getImports().iterator(); imports.hasNext();) {
            String im = (String) imports.next();
            if (im.equals(ProtegeNames.PROTEGE_OWL_ONTOLOGY)) {
                return false;  // Already there
            }
        }
        owlOntology.addImports(ProtegeNames.PROTEGE_OWL_ONTOLOGY);
        try {
            owlModel.loadImportedAssertions(URIUtilities.createURI(ProtegeNames.PROTEGE_OWL_ONTOLOGY));
            ProtegeOWLParser.doFinalPostProcessing(owlModel);
        }
        catch (OntologyLoadException e) {
            Log.getLogger().log(Level.WARNING, "error importing protege ontology", e);
        }
        ensureProtegePrefixExists();
        return true;
    }


    private void ensureProtegePrefixExists() {
        if (owlModel.getNamespaceManager().getPrefix(ProtegeNames.PROTEGE_OWL_NAMESPACE) == null) {
            String prefix = ProtegeNames.PROTEGE_PREFIX;
            owlModel.getNamespaceManager().setPrefix(ProtegeNames.PROTEGE_OWL_NAMESPACE, prefix);
            owlModel.getNamespaceManager().setPrefix(XSPNames.NS, XSPNames.DEFAULT_PREFIX);
        }
    }


}
