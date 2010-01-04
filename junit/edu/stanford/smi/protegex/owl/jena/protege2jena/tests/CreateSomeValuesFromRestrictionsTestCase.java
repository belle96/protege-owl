package edu.stanford.smi.protegex.owl.jena.protege2jena.tests;

import com.hp.hpl.jena.ontology.DataRange;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.Restriction;
import com.hp.hpl.jena.ontology.SomeValuesFromRestriction;
import com.hp.hpl.jena.rdf.model.Resource;
import edu.stanford.smi.protege.model.ValueType;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.model.impl.XMLSchemaDatatypes;

import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateSomeValuesFromRestrictionsTestCase extends AbstractProtege2JenaTestCase {

    public void testCreateSomeRestrictionWithClass() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        OWLObjectProperty slot = owlModel.createOWLObjectProperty("slot");
        cls.addSuperclass(owlModel.createOWLSomeValuesFrom(slot, cls));
        OntModel ontModel = createOntModel();
        Restriction r = getRestriction(ontModel.getOntClass(cls.getURI()));
        assertTrue(r.canAs(SomeValuesFromRestriction.class));
        SomeValuesFromRestriction ar = r.asSomeValuesFromRestriction();
        assertEquals(ontModel.getOntProperty(slot.getURI()), ar.getOnProperty());
        assertEquals(ontModel.getOntClass(cls.getURI()), ar.getSomeValuesFrom());
    }


    public void testCreateSomeRestrictionWithDataRange() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        OWLDatatypeProperty slot = owlModel.createOWLDatatypeProperty("slot", owlModel.getXSDstring());
        OWLSomeValuesFrom restriction = owlModel.createOWLSomeValuesFrom(slot, new RDFSLiteral[]{
                owlModel.createRDFSLiteral("A"),
                owlModel.createRDFSLiteral("B")
        });
        cls.addSuperclass(restriction);
        OntModel ontModel = createOntModel();
        Restriction r = getRestriction(ontModel.getOntClass(cls.getURI()));
        assertTrue(r.canAs(SomeValuesFromRestriction.class));
        SomeValuesFromRestriction ar = r.asSomeValuesFromRestriction();
        assertEquals(ontModel.getOntProperty(slot.getURI()), ar.getOnProperty());
        Resource resource = ar.getSomeValuesFrom();
        assertTrue(resource.canAs(DataRange.class));
        DataRange dataRange = (DataRange) resource.as(DataRange.class);
        Iterator it = dataRange.listOneOf();
        assertEquals(ontModel.createTypedLiteral((Object) "A"), it.next());
        assertEquals(ontModel.createTypedLiteral((Object) "B"), it.next());
        assertFalse(it.hasNext());
    }


    public void testCreateSomeRestrictionWithDatatype() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        OWLDatatypeProperty slot = owlModel.createOWLDatatypeProperty("slot", owlModel.getXSDstring());
        cls.addSuperclass(owlModel.createOWLSomeValuesFrom(slot, owlModel.getXSDstring()));
        OntModel newModel = createOntModel();
        Restriction r = getRestriction(newModel.getOntClass(cls.getURI()));
        assertTrue(r.canAs(SomeValuesFromRestriction.class));
        SomeValuesFromRestriction ar = r.asSomeValuesFromRestriction();
        assertEquals(newModel.getOntProperty(slot.getURI()), ar.getOnProperty());
        Resource resource = ar.getSomeValuesFrom();
        assertEquals(XMLSchemaDatatypes.getValueTypeURI(ValueType.STRING), resource.getURI());
    }
}
