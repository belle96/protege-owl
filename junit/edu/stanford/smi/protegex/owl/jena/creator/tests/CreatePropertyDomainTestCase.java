package edu.stanford.smi.protegex.owl.jena.creator.tests;

import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.UnionClass;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLProperty;

import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreatePropertyDomainTestCase extends AbstractJenaCreatorTestCase {

    public void testDomainSingleClass() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        OWLProperty slot = owlModel.createOWLObjectProperty("slot");
        slot.setDomainDefined(true);
        slot.addUnionDomainClass(cls);
        OntModel newModel = runJenaCreator();
        ObjectProperty property = newModel.getObjectProperty(slot.getURI());
        assertEquals(newModel.getOntClass(cls.getURI()), property.getDomain());
    }


    public void testDomainTwoClasses() {
        OWLNamedClass clsA = owlModel.createOWLNamedClass("ClsA");
        OWLNamedClass clsB = owlModel.createOWLNamedClass("ClsB");
        OWLProperty slot = owlModel.createOWLObjectProperty("slot");
        slot.setDomainDefined(true);
        slot.addUnionDomainClass(clsA);
        slot.addUnionDomainClass(clsB);
        OntModel newModel = runJenaCreator();
        ObjectProperty property = newModel.getObjectProperty(slot.getURI());
        assertSize(1, property.listDomain());
        assertTrue(property.getDomain().canAs(UnionClass.class));
        UnionClass unionClass = (UnionClass) property.getDomain().as(UnionClass.class);
        Iterator it = unionClass.listOperands();
        assertEquals(newModel.getOntClass(clsA.getURI()), it.next());
        assertEquals(newModel.getOntClass(clsB.getURI()), it.next());
        assertFalse(it.hasNext());
    }


    public void testDomainUnrestricted() {
        OWLProperty owlProperty = owlModel.createOWLObjectProperty("owlProperty");
        owlProperty.setDomainDefined(false);
        OntModel newModel = runJenaCreator();
        ObjectProperty ontProperty = newModel.getObjectProperty(owlProperty.getURI());
        assertEquals(null, ontProperty.getDomain());
    }


    public void testDomainAtRestriction() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        OWLProperty slot = owlModel.createOWLObjectProperty("slot");
        slot.setDomainDefined(true);
        cls.addSuperclass(owlModel.createOWLMinCardinality(slot, 1));
        slot.addUnionDomainClass(cls);
        OntModel newModel = runJenaCreator();
        ObjectProperty property = newModel.getObjectProperty(slot.getURI());
        Iterator it = property.listDomain();
        assertEquals(newModel.getOntClass(cls.getURI()), it.next());
        assertFalse(it.hasNext());
    }
}
