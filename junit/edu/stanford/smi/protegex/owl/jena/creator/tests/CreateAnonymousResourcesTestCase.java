package edu.stanford.smi.protegex.owl.jena.creator.tests;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.vocabulary.RDF;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateAnonymousResourcesTestCase extends AbstractJenaCreatorTestCase {

    public void testCreatePseudoList() {
        OWLNamedClass listCls = owlModel.createOWLNamedClass("List");
        OWLObjectProperty restSlot = owlModel.createOWLObjectProperty("rest");
        restSlot.addUnionDomainClass(listCls);
        RDFResource rootNode = (RDFResource) listCls.createInstance("Root");
        RDFResource middleNode = listCls.createAnonymousInstance();
        rootNode.setPropertyValue(restSlot, middleNode);
        RDFResource leafNode = listCls.createAnonymousInstance();
        middleNode.setPropertyValue(restSlot, leafNode);

        OntModel newModel = runJenaCreator();
        Individual rootIndividual = newModel.getIndividual(rootNode.getURI());
        assertFalse(rootIndividual.isAnon());
        Property restProperty = newModel.getOntProperty(restSlot.getURI());
        Individual middleIndividual = (Individual) rootIndividual.getPropertyValue(restProperty).as(Individual.class);
        assertTrue(middleIndividual.isAnon());
        OntClass listClass = newModel.getOntClass(listCls.getURI());
        assertSize(3, newModel.listStatements(null, RDF.type, listClass));
    }
}
