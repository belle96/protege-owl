package edu.stanford.smi.protegex.owl.jena.creator.tests;

import com.hp.hpl.jena.ontology.ComplementClass;
import com.hp.hpl.jena.ontology.IntersectionClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.UnionClass;
import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;

import java.util.Arrays;
import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateLogicalClassesTestCase extends AbstractJenaCreatorTestCase {

    public void testCreateComplementClass() {
        OWLNamedClass a = owlModel.createOWLNamedClass("A");
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        cls.addSuperclass(owlModel.createOWLComplementClass(a));
        OntModel ontModel = runJenaCreator();
        ComplementClass complementClass =
                (ComplementClass) ontModel.listComplementClasses().next();
        assertNotNull(complementClass);
        assertEquals(ontModel.getOntClass(a.getURI()), complementClass.getOperand());
    }


    public void testCreateIntersectionClass() {
        OWLNamedClass a = owlModel.createOWLNamedClass("A");
        OWLNamedClass b = owlModel.createOWLNamedClass("B");
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        cls.addSuperclass(owlModel.createOWLIntersectionClass(Arrays.asList(new Cls[]{a, b})));
        OntModel ontModel = runJenaCreator();
        IntersectionClass intersectionClass =
                (IntersectionClass) ontModel.listIntersectionClasses().next();
        assertNotNull(intersectionClass);
        Iterator it = intersectionClass.listOperands();
        assertEquals(ontModel.getOntClass(a.getURI()), it.next());
        assertEquals(ontModel.getOntClass(b.getURI()), it.next());
        assertFalse(it.hasNext());
    }


    public void testCreateUnionClass() {
        OWLNamedClass a = owlModel.createOWLNamedClass("A");
        OWLNamedClass b = owlModel.createOWLNamedClass("B");
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        cls.addSuperclass(owlModel.createOWLUnionClass(Arrays.asList(new Cls[]{a, b})));
        OntModel ontModel = runJenaCreator();
        UnionClass unionClass =
                (UnionClass) ontModel.listUnionClasses().next();
        assertNotNull(unionClass);
        Iterator it = unionClass.listOperands();
        assertEquals(ontModel.getOntClass(a.getURI()), it.next());
        assertEquals(ontModel.getOntClass(b.getURI()), it.next());
        assertFalse(it.hasNext());
    }
}
