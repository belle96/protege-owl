package edu.stanford.smi.protegex.owl.jena.protege2jena.tests;

import com.hp.hpl.jena.ontology.AllValuesFromRestriction;
import com.hp.hpl.jena.ontology.DataRange;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.Restriction;
import com.hp.hpl.jena.rdf.model.Resource;
import edu.stanford.smi.protege.model.ValueType;
import edu.stanford.smi.protegex.owl.jena.Jena;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.model.impl.XMLSchemaDatatypes;

import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateAllValuesFromRestrictionsTestCase extends AbstractProtege2JenaTestCase {

    public void testCreateAllRestrictionWithClass() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        OWLObjectProperty slot = owlModel.createOWLObjectProperty("slot");
        cls.addSuperclass(owlModel.createOWLAllValuesFrom(slot, cls));
        OntModel ontModel = createOntModel();
        Restriction r = getRestriction(ontModel.getOntClass(cls.getURI()));
        assertTrue(r.canAs(AllValuesFromRestriction.class));
        AllValuesFromRestriction ar = r.asAllValuesFromRestriction();
        assertEquals(ontModel.getOntProperty(slot.getURI()), ar.getOnProperty());
        assertEquals(ontModel.getOntClass(cls.getURI()), ar.getAllValuesFrom());
    }


    public void testCreateAllRestrictionWithDataRange() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        OWLDatatypeProperty slot = owlModel.createOWLDatatypeProperty("slot", owlModel.getXSDstring());
        OWLAllValuesFrom restriction = owlModel.createOWLAllValuesFrom(slot, new RDFSLiteral[]{
                owlModel.createRDFSLiteral("A"),
                owlModel.createRDFSLiteral("B"),
        });
        cls.addSuperclass(restriction);
        OntModel ontModel = createOntModel();
        Jena.dumpRDF(ontModel);
        Restriction r = getRestriction(ontModel.getOntClass(cls.getURI()));
        assertTrue(r.canAs(AllValuesFromRestriction.class));
        assertTrue(r.isAnon());
        AllValuesFromRestriction ar = r.asAllValuesFromRestriction();
        assertEquals(ontModel.getOntProperty(slot.getURI()), ar.getOnProperty());
        Resource resource = ar.getAllValuesFrom();
        assertTrue(resource.canAs(DataRange.class));
        DataRange dataRange = (DataRange) resource.as(DataRange.class);
        Iterator it = dataRange.listOneOf();
        assertEquals(ontModel.createTypedLiteral((Object) "A"), it.next());
        assertEquals(ontModel.createTypedLiteral((Object) "B"), it.next());
        assertFalse(it.hasNext());
    }


    public void testCreateAllRestrictionWithDatatype() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        OWLDatatypeProperty property = owlModel.createOWLDatatypeProperty("property", owlModel.getXSDstring());
        cls.addSuperclass(owlModel.createOWLAllValuesFrom(property, owlModel.getXSDstring()));
        OntModel ontModel = createOntModel();
        Restriction r = getRestriction(ontModel.getOntClass(cls.getURI()));
        assertTrue(r.canAs(AllValuesFromRestriction.class));
        AllValuesFromRestriction ar = r.asAllValuesFromRestriction();
        assertEquals(ontModel.getOntProperty(property.getURI()), ar.getOnProperty());
        Resource resource = ar.getAllValuesFrom();
        assertEquals(XMLSchemaDatatypes.getValueTypeURI(ValueType.STRING), resource.getURI());
    }
}
