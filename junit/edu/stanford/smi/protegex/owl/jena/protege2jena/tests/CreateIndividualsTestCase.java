package edu.stanford.smi.protegex.owl.jena.protege2jena.tests;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import edu.stanford.smi.protege.model.ValueType;
import edu.stanford.smi.protegex.owl.jena.Jena;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateIndividualsTestCase extends AbstractProtege2JenaTestCase {

    public void testCreateInstance() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        RDFResource instance = (RDFResource) cls.createInstance("individual");
        OntModel newModel = createOntModel();
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

        OntModel newModel = createOntModel();
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

    // TODO
    /*
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
    } */
}
