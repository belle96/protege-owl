package edu.stanford.smi.protegex.owl.jena.creator.tests;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.Property;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.model.ValueType;
import edu.stanford.smi.protegex.owl.jena.Jena;
import edu.stanford.smi.protegex.owl.model.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateIndividualsTestCase extends AbstractJenaCreatorTestCase {

    public void testCreateInstance() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        RDFResource instance = (RDFResource) cls.createInstance("individual");
        OntModel newModel = runJenaCreator();
        Individual individual = newModel.getIndividual(instance.getURI());
        assertNotNull(individual);
    }


    public void testCreatePropertyValues() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        OWLObjectProperty objectSlot = owlModel.createOWLObjectProperty("object");
        objectSlot.setUnionRangeClasses(Collections.singleton(cls));
        objectSlot.addUnionDomainClass(cls);
        OWLDatatypeProperty datatypeSlot = owlModel.createOWLDatatypeProperty("datatype");
        datatypeSlot.addUnionDomainClass(cls);
        RDFResource instance = (RDFResource) cls.createInstance("individual");
        instance.setPropertyValue(objectSlot, instance);
        instance.setPropertyValues(datatypeSlot, Arrays.asList(new String[]{"A", "B"}));

        OntModel newModel = runJenaCreator();
        Individual individual = newModel.getIndividual(instance.getURI());
        assertNotNull(individual);
        OntProperty datatypeProperty = newModel.getOntProperty(datatypeSlot.getURI());
        OntProperty objectProperty = newModel.getOntProperty(objectSlot.getURI());
        assertEquals(individual, individual.getPropertyValue(objectProperty));
        Set values = Jena.set(individual.listPropertyValues(datatypeProperty));
        assertSize(2, values);
        assertHasValue(individual, datatypeProperty, ValueType.STRING, "A");
        assertHasValue(individual, datatypeProperty, ValueType.STRING, "B");
    }


    public void testCreateIndividualWithInferredTypes() {
        OWLNamedClass clsA = owlModel.createOWLNamedClass("A");
        OWLNamedClass clsB = owlModel.createOWLNamedClass("B");
        OWLNamedClass clsC = owlModel.createOWLNamedClass("C");
        RDFResource instance = (RDFResource) clsA.createInstance("instance");
        ((Instance) instance).addOwnSlotValue(owlModel.getSlot(ProtegeNames.Slot.INFERRED_TYPE), clsB);
        ((Instance) instance).addOwnSlotValue(owlModel.getSlot(ProtegeNames.Slot.INFERRED_TYPE), clsC);
        OntModel newModel = runJenaCreator(false, true);
        Individual individual = newModel.getIndividual(instance.getURI());
        OntClass classB = newModel.getOntClass(clsB.getURI());
        OntClass classC = newModel.getOntClass(clsC.getURI());
        assertSize(2, individual.listRDFTypes(true));
        assertContains(classB, individual.listRDFTypes(true));
        assertContains(classC, individual.listRDFTypes(true));
    }


    public void testCreateInstanceOfOWLThing() {
        OWLIndividual individual = owlThing.createOWLIndividual("Test");
        OntModel newModel = runJenaCreator();
        Individual indi = newModel.getIndividual(individual.getURI());
        assertNotNull(indi);
    }


    public void testCreateIndividualsChain() {
        RDFProperty property = owlModel.createOWLObjectProperty("property");
        OWLIndividual a = owlThing.createOWLIndividual("a");
        OWLIndividual b = owlThing.createOWLIndividual("b");
        OWLIndividual c = owlThing.createOWLIndividual("c");
        a.setPropertyValue(property, b);
        b.setPropertyValue(property, c);

        OntModel newModel = runJenaCreator(false);
        Individual ontA = newModel.getIndividual(a.getURI());
        Individual ontB = newModel.getIndividual(b.getURI());
        Individual ontC = newModel.getIndividual(c.getURI());
        Property ontProperty = newModel.getOntProperty(property.getURI());
        assertSize(1, ontA.listPropertyValues(ontProperty));
        assertEquals(ontB, ontA.getPropertyValue(ontProperty));
        assertSize(1, ontB.listPropertyValues(ontProperty));
        assertSize(0, ontC.listPropertyValues(ontProperty));
    }
}
