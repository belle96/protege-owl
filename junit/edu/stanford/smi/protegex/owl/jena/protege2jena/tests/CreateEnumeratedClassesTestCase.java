package edu.stanford.smi.protegex.owl.jena.protege2jena.tests;

import com.hp.hpl.jena.ontology.EnumeratedClass;
import com.hp.hpl.jena.ontology.OntModel;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protegex.owl.model.OWLEnumeratedClass;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFResource;

import java.util.Arrays;
import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateEnumeratedClassesTestCase extends AbstractProtege2JenaTestCase {

    public void testCreateEnumeratedClass() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Color");
        RDFResource a = (RDFResource) cls.createInstance("a");
        RDFResource b = (RDFResource) cls.createInstance("b");
        cls.addSuperclass(owlModel.createOWLEnumeratedClass(Arrays.asList(new Instance[]{a, b})));
        OntModel ontModel = createOntModel();
        EnumeratedClass enumeratedClass = (EnumeratedClass) ontModel.listEnumeratedClasses().next();
        Iterator it = enumeratedClass.listOneOf();
        assertEquals(ontModel.getIndividual(a.getURI()), it.next());
        assertEquals(ontModel.getIndividual(b.getURI()), it.next());
        assertFalse(it.hasNext());
    }


    public void testCreateEnumeratedEquivalentClass() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Color");
        RDFResource a = (RDFResource) cls.createInstance("a");
        RDFResource b = (RDFResource) cls.createInstance("b");
        OWLEnumeratedClass enumerationCls = owlModel.createOWLEnumeratedClass(Arrays.asList(new Instance[]{a, b}));
        cls.addEquivalentClass(enumerationCls);
        OntModel newModel = createOntModel();
        EnumeratedClass enumeratedClass = (EnumeratedClass) newModel.listEnumeratedClasses().next();
        Iterator it = enumeratedClass.listOneOf();
        assertEquals(newModel.getIndividual(a.getURI()), it.next());
        assertEquals(newModel.getIndividual(b.getURI()), it.next());
        assertFalse(it.hasNext());
    }


    public void testCreateEnumeratedClassWithClsAndSlot() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Color");
        RDFResource a = owlModel.createOWLNamedClass("Cls");
        RDFResource b = owlModel.createOWLObjectProperty("slot");
        cls.addSuperclass(owlModel.createOWLEnumeratedClass(Arrays.asList(new Instance[]{a, b})));
        OntModel newModel = createOntModel();
        EnumeratedClass enumeratedClass = (EnumeratedClass) newModel.listEnumeratedClasses().next();
        Iterator it = enumeratedClass.listOneOf();
        assertEquals(newModel.getIndividual(a.getURI()), it.next());
        assertEquals(newModel.getIndividual(b.getURI()), it.next());
        assertFalse(it.hasNext());
    }
}
