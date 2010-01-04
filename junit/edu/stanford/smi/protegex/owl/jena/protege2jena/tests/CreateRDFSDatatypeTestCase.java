package edu.stanford.smi.protegex.owl.jena.protege2jena.tests;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.RDFSDatatype;
import edu.stanford.smi.protegex.owl.model.RDFSNames;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateRDFSDatatypeTestCase extends AbstractProtege2JenaTestCase {

    public void testUserDefinedRDFSDatatype() {
        RDFSClass datatypeClass = owlModel.getRDFSNamedClass(RDFSNames.Cls.DATATYPE);
        RDFSDatatype type = (RDFSDatatype) datatypeClass.createInstance("MyDatatype");
        RDFProperty property = owlModel.createRDFProperty("property");
        property.setRange(type);
        OntModel ontModel = createOntModel();
        OntProperty ontProperty = ontModel.getOntProperty(property.getURI());
        Resource range = ontProperty.getRange();
        assertNotNull(range.getProperty(RDF.type));
        assertEquals(RDFS.Datatype, range.getProperty(RDF.type).getObject());
    }
}
