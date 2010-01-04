package edu.stanford.smi.protegex.owl.jena.creator.tests;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.vocabulary.OWL;
import edu.stanford.smi.protegex.owl.jena.Jena;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;

import java.util.Collection;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateSuperClassesTestCase extends AbstractJenaCreatorTestCase {

    public void testCreateSimpleNamedOntClass() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        OntModel ontModel = runJenaCreator();
        OntClass ontClass = ontModel.getOntClass(cls.getURI());
        assertNotNull(ontClass);
    }


    public void testCreateOneSuperClass() {
        OWLNamedClass superCls = owlModel.createOWLNamedClass("superCls");
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        cls.addSuperclass(superCls);
        cls.removeSuperclass(owlThing);
        OntModel ontModel = runJenaCreator();
        OntClass ontClass = ontModel.getOntClass(cls.getURI());
        assertSize(1, ontClass.listSuperClasses());
        assertEquals(ontModel.getOntClass(superCls.getURI()), ontClass.getSuperClass());
    }


    public void testCreateOnlyThingAsSuperClass() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        OntModel ontModel = runJenaCreator();
        OntClass ontClass = ontModel.getOntClass(cls.getURI());
        assertNull(ontClass.getSuperClass());
    }


    public void testCreateTwoSuperClasses() {
        OWLNamedClass superCls1 = owlModel.createOWLNamedClass("superCls1");
        OWLNamedClass superCls2 = owlModel.createOWLNamedClass("superCls2");
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        cls.addSuperclass(superCls1);
        cls.addSuperclass(superCls2);
        cls.removeSuperclass(owlThing);
        OntModel ontModel = runJenaCreator();
        OntClass ontClass = ontModel.getOntClass(cls.getURI());
        assertSize(2, ontClass.listSuperClasses());
        Collection superClasses = Jena.set(ontClass.listSuperClasses());
        assertContains(ontModel.getOntClass(superCls1.getURI()), superClasses);
        assertContains(ontModel.getOntClass(superCls2.getURI()), superClasses);
    }


    public void testCreateTwoSuperClassesIncludingThing() {
        OWLNamedClass superCls1 = owlModel.createOWLNamedClass("superCls1");
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        cls.addSuperclass(superCls1);
        OntModel ontModel = runJenaCreator();
        OntClass ontClass = ontModel.getOntClass(cls.getURI());
        assertSize(2, ontClass.listSuperClasses());
        Collection superClasses = Jena.set(ontClass.listSuperClasses());
        assertContains(ontModel.getOntClass(superCls1.getURI()), superClasses);
        assertContains(OWL.Thing, superClasses);
    }
}
