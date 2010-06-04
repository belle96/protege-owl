package edu.stanford.smi.protegex.owl.model.framestore.tests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import junit.framework.TestCase;
import edu.stanford.smi.protegex.owl.jena.creator.NewOwlProjectCreator;
import edu.stanford.smi.protegex.owl.model.OWLIntersectionClass;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;

public class CheeseyInferenceTest extends TestCase {
    public static final String NS="http://smi-protege.stanford.edu/ontologies/TestCheeseyInference";

    private OWLModel model;
    private OWLNamedClass a;
    private OWLNamedClass b;
    private OWLObjectProperty p;

    @SuppressWarnings("unchecked")
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        NewOwlProjectCreator creator = new NewOwlProjectCreator();
        creator.setOntologyName(NS);
        Collection errors = new ArrayList();
        creator.create(errors);
        assertTrue(errors.isEmpty());
        model = creator.getOwlModel();
        a = model.createOWLNamedClass(NS + "#A");
        b = model.createOWLNamedClass(NS + "#B");
        p = model.createOWLObjectProperty(NS + "#p");
    }
    
    @SuppressWarnings({ "unchecked", "deprecation" })
    public void testBadCheeseyInference() {
        Collection conjuncts = new HashSet();
        conjuncts.add(b);
        conjuncts.add(model.createOWLSomeValuesFrom(p, b));
        OWLIntersectionClass conjunct = model.createOWLIntersectionClass(conjuncts);
        conjunct.addDirectSuperclass(a);
        assertTrue(b.getSubclasses(true).isEmpty());
    }

}
