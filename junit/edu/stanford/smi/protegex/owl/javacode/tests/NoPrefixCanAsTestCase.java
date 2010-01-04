package edu.stanford.smi.protegex.owl.javacode.tests;



import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;
import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protegex.owl.code.generation.test02.B;
import edu.stanford.smi.protegex.owl.code.generation.test02.C;
import edu.stanford.smi.protegex.owl.code.generation.test02.D;
import edu.stanford.smi.protegex.owl.code.generation.test02.Test02Factory;
import edu.stanford.smi.protegex.owl.code.generation.test02.Top;
import edu.stanford.smi.protegex.owl.code.generation.test02.X;
import edu.stanford.smi.protegex.owl.model.OWLModel;

public class NoPrefixCanAsTestCase extends TestCase {
    private Project p;
    private OWLModel owlModel;
    private Test02Factory factory;

    @Override
    protected void setUp() throws Exception {
        Collection errors = new ArrayList();
        p = new Project(GenerateJunitCode.SOURCE_ONTOLOGY_01, errors);
        GenerateJunitCode.handleErrors(errors);
        owlModel = (OWLModel)  p.getKnowledgeBase();
        factory = new Test02Factory(owlModel);
    }

    @Override
    protected void tearDown() throws Exception {
        p.dispose();
        p = null;
        owlModel = null;
    }

    public void testSimpleAs() {
        C fC = factory.getC("fC");
        D fD = factory.getD("fD");
        assertFalse(fC.canAs(D.class));
        assertTrue(fD.canAs(D.class));
        assertNotNull(fD.as(D.class));
        assertTrue(fD.as(D.class) instanceof D);
        assertTrue(fD.as(D.class) instanceof D);
        D fDAlt = (D) fD.as(D.class);
        assertTrue(fDAlt != null);
        assertTrue(fDAlt.equals(fD));
    }

    public void testGetP() {
        Top t = factory.getTop("fTop");
        for (Object o : t.getP()) {
            if (!(o instanceof C)) {
                fail("Wrong type for p values found");
            }
        }
    }

    public void testIncompatibleTypes() {
        X x = factory.getX("fX");
        assertTrue(x.canAs(B.class));
        assertNotNull(x.as(B.class));
        assertTrue(x.as(B.class) instanceof B);
        B xb = (B) x.as(B.class);
        assertNotNull(xb);
        assertTrue(xb.canAs(X.class));
        assertNotNull(xb.as(X.class));
        assertTrue(xb.as(X.class) instanceof X);
    }
}
