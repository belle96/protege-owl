package edu.stanford.smi.protegex.owl.jena.creator.tests;

import com.hp.hpl.jena.ontology.HasValueRestriction;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.Restriction;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateHasValueRestrictionsTestCase extends AbstractJenaCreatorTestCase {

    public void testHasValueRestrictionWithIndividual() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        RDFResource instance = (RDFResource) cls.createInstance("instance");
        OWLProperty property = owlModel.createOWLObjectProperty("property");
        cls.addSuperclass(owlModel.createOWLHasValue(property, instance));
        OntModel ontModel = runJenaCreator();
        Restriction restriction = getRestriction(ontModel.getOntClass(cls.getURI()));
        assertTrue(restriction.canAs(HasValueRestriction.class));
        HasValueRestriction hr = restriction.asHasValueRestriction();
        assertEquals(ontModel.getOntProperty(property.getURI()), hr.getOnProperty());
        assertEquals(ontModel.getIndividual(instance.getURI()), hr.getHasValue());
    }


    public void testHasValueRestrictionWithLiteral() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        OWLProperty property = owlModel.createOWLObjectProperty("property");
        cls.addSuperclass(owlModel.createOWLHasValue(property, "A"));
        OntModel ontModel = runJenaCreator();
        Restriction restriction = getRestriction(ontModel.getOntClass(cls.getURI()));
        assertTrue(restriction.canAs(HasValueRestriction.class));
        HasValueRestriction hr = restriction.asHasValueRestriction();
        assertEquals(ontModel.getOntProperty(property.getURI()), hr.getOnProperty());
        assertEquals(ontModel.createTypedLiteral((Object) "A"), hr.getHasValue());
    }
}
