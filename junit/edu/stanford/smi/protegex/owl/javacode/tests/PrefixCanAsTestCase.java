package edu.stanford.smi.protegex.owl.javacode.tests;



import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;
import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protegex.owl.code.generation.test01.CODE1_B;
import edu.stanford.smi.protegex.owl.code.generation.test01.CODE1_C;
import edu.stanford.smi.protegex.owl.code.generation.test01.CODE1_D;
import edu.stanford.smi.protegex.owl.code.generation.test01.CODE2_X;
import edu.stanford.smi.protegex.owl.code.generation.test01.Test01Factory;
import edu.stanford.smi.protegex.owl.code.generation.test01.Top;
import edu.stanford.smi.protegex.owl.model.OWLModel;

public class PrefixCanAsTestCase extends TestCase {
    private Project p;
    private OWLModel owlModel;
    private Test01Factory factory;

    @Override
    protected void setUp() throws Exception {
        Collection errors = new ArrayList();
        p = new Project(GenerateJunitCode.SOURCE_ONTOLOGY_01, errors);
        GenerateJunitCode.handleErrors(errors);
        owlModel = (OWLModel)  p.getKnowledgeBase();
        factory = new Test01Factory(owlModel);
    }

    @Override
    protected void tearDown() throws Exception {
        p.dispose();
        p = null;
        owlModel = null;
    }

    public void testSimpleAs() {
        CODE1_C fC = factory.getCODE1_C("fC");
        CODE1_D fD = factory.getCODE1_D("fD");
        assertFalse(fC.canAs(CODE1_D.class));
        assertTrue(fD.canAs(CODE1_D.class));
        assertNotNull(fD.as(CODE1_D.class));
        assertTrue(fD.as(CODE1_D.class) instanceof CODE1_D);
        assertTrue(fD.as(CODE1_D.class) instanceof CODE1_D);
        CODE1_D fDAlt = (CODE1_D) fD.as(CODE1_D.class);
        assertTrue(fDAlt != null);
        assertTrue(fDAlt.equals(fD));
    }

    public void testGetP() {
        Top t = factory.getTop("fTop");
        for (Object o : t.getCODE1_p()) {
            if (!(o instanceof CODE1_C)) {
                fail("Wrong type for p values found");
            }
        }
    }

    public void testIncompatibleTypes() {
        CODE2_X x = factory.getCODE2_X("fX");
        assertTrue(x.canAs(CODE1_B.class));
        assertNotNull(x.as(CODE1_B.class));
        assertTrue(x.as(CODE1_B.class) instanceof CODE1_B);
        CODE1_B xb = (CODE1_B) x.as(CODE1_B.class);
        assertNotNull(xb);
        assertTrue(xb.canAs(CODE2_X.class));
        assertNotNull(xb.as(CODE2_X.class));
        assertTrue(xb.as(CODE2_X.class) instanceof CODE2_X);
    }
}
