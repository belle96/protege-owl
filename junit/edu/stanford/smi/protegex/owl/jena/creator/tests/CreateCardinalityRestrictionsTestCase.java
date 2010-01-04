package edu.stanford.smi.protegex.owl.jena.creator.tests;

import com.hp.hpl.jena.ontology.*;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLProperty;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateCardinalityRestrictionsTestCase extends AbstractJenaCreatorTestCase {

    public void testCreateCardinalityRestriction() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        OWLProperty property = owlModel.createOWLObjectProperty("property");
        cls.addSuperclass(owlModel.createOWLCardinality(property, 4));
        OntModel ontModel = runJenaCreator();
        Restriction r = getRestriction(ontModel.getOntClass(cls.getURI()));
        assertTrue(r.canAs(CardinalityRestriction.class));
        CardinalityRestriction cr = r.asCardinalityRestriction();
        assertEquals(ontModel.getOntProperty(property.getURI()), cr.getOnProperty());
        assertEquals(4, cr.getCardinality());
    }


    public void testCreateMaxCardinalityRestriction() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        OWLProperty property = owlModel.createOWLObjectProperty("property");
        cls.addSuperclass(owlModel.createOWLMaxCardinality(property, 4));
        OntModel ontModel = runJenaCreator();
        Restriction r = getRestriction(ontModel.getOntClass(cls.getURI()));
        assertTrue(r.canAs(MaxCardinalityRestriction.class));
        MaxCardinalityRestriction cr = r.asMaxCardinalityRestriction();
        assertEquals(ontModel.getOntProperty(property.getURI()), cr.getOnProperty());
        assertEquals(4, cr.getMaxCardinality());
    }


    public void testCreateMinCardinalityRestriction() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        OWLProperty property = owlModel.createOWLObjectProperty("property");
        cls.addSuperclass(owlModel.createOWLMinCardinality(property, 4));
        OntModel ontModel = runJenaCreator();
        Restriction r = getRestriction(ontModel.getOntClass(cls.getURI()));
        assertTrue(r.canAs(MinCardinalityRestriction.class));
        MinCardinalityRestriction cr = r.asMinCardinalityRestriction();
        assertEquals(ontModel.getOntProperty(property.getURI()), cr.getOnProperty());
        assertEquals(4, cr.getMinCardinality());
    }
}
