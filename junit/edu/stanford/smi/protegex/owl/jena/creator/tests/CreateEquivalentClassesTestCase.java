package edu.stanford.smi.protegex.owl.jena.creator.tests;

import com.hp.hpl.jena.ontology.IntersectionClass;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;

import java.util.Arrays;
import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateEquivalentClassesTestCase extends AbstractJenaCreatorTestCase {

    public void testCreateDefinition() {
        OWLNamedClass person = owlModel.createOWLNamedClass("Person");
        OWLObjectProperty slot = owlModel.createOWLObjectProperty("children");
        OWLNamedClass cls = owlModel.createOWLNamedClass("Parent");
        cls.removeSuperclass(owlModel.getOWLThingClass());
        cls.addSuperclass(person);
        cls.addEquivalentClass(owlModel.createOWLIntersectionClass(Arrays.asList(new Cls[]{
                person,
                owlModel.createOWLMinCardinality(slot, 1)
        })));
        OntModel ontModel = runJenaCreator();
        OntClass ontClass = ontModel.getOntClass(cls.getURI());
        assertFalse(ontClass.listSuperClasses().hasNext());
        Iterator it = ontClass.listEquivalentClasses();
        OntClass equi = (OntClass) it.next();
        assertTrue(equi.canAs(IntersectionClass.class));
        assertFalse(it.hasNext());
    }


    public void testCreateInferredEquivalentClass() {
        OWLNamedClass a = owlModel.createOWLNamedClass("A");
        OWLNamedClass b = (OWLNamedClass) owlModel.createOWLNamedSubclass("B", a);
        a.addInferredSuperclass(b);
        b.addInferredSuperclass(a);
        OntModel ontModel = runJenaCreator(false, true);
        OntClass aClass = ontModel.getOntClass(a.getURI());
        OntClass bClass = ontModel.getOntClass(b.getURI());
        assertTrue(aClass.hasEquivalentClass(bClass));
        assertFalse(bClass.hasSuperClass(aClass));
    }
}
