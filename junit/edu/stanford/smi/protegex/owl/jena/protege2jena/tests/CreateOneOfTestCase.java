package edu.stanford.smi.protegex.owl.jena.protege2jena.tests;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.RDFNode;
import edu.stanford.smi.protege.model.ValueType;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSLiteral;
import edu.stanford.smi.protegex.owl.model.impl.XMLSchemaDatatypes;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateOneOfTestCase extends AbstractProtege2JenaTestCase {

    public void testCreateFloatValue() {

        OWLNamedClass cls = owlModel.createOWLNamedClass("Table");
        OWLDatatypeProperty slot = owlModel.createOWLDatatypeProperty("length",
                new RDFSLiteral[]{
                        owlModel.createRDFSLiteral(new Float(2)),
                        owlModel.createRDFSLiteral(new Float(3))
                });
        slot.addUnionDomainClass(cls);
        RDFResource instance = (RDFResource) cls.createInstance("instance");
        instance.setPropertyValue(slot, new Float(2));

        OntModel newModel = createOntModel();

        Individual individual = newModel.getIndividual(instance.getURI());
        OntProperty ontProperty = newModel.getOntProperty(slot.getURI());
        assertSize(1, individual.listPropertyValues(ontProperty));
        RDFNode node = individual.getPropertyValue(ontProperty);
        Literal literal = (Literal) node.as(Literal.class);
        assertEquals(XMLSchemaDatatypes.getDefaultXSDDatatype(ValueType.FLOAT),
                literal.getDatatype());
    }
}
