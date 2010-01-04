package edu.stanford.smi.protegex.owl.swrl.model.impl.tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLAtom;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLClassAtom;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLImp;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DeleteImpTestCase extends AbstractSWRLTestCase {

    public void testDeleteVariable() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        int oldFrameCount = owlModel.getFrameCount();
        SWRLVariable x = factory.createVariable("x");
        SWRLClassAtom headAtom = factory.createClassAtom(cls, x);
        SWRLClassAtom bodyAtom = factory.createClassAtom(cls, x);
        List<SWRLAtom> list = new ArrayList<SWRLAtom>();
        list.add(bodyAtom);
        //TODO: Martin, please take a look. Maybe interface should be changed
        SWRLImp imp = factory.createImp(headAtom, list);
        //SWRLImp imp = factory.createImp(headAtom, Collections.singleton(bodyAtom));
        imp.deleteImp();
        assertEquals(oldFrameCount, owlModel.getFrameCount());
        assertTrue(x.isDeleted());
    }


    public void testDontDeleteVariable() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        SWRLVariable x = factory.createVariable("x");
        SWRLImp otherImp = factory.createImp(factory.createClassAtom(cls, x), Collections.EMPTY_LIST);
        String otherImpText = otherImp.getBrowserText();
        SWRLClassAtom headAtom = factory.createClassAtom(cls, x);
        SWRLClassAtom bodyAtom = factory.createClassAtom(cls, x);
        List<SWRLAtom> list = new ArrayList<SWRLAtom>();
        list.add(bodyAtom);
        //TODO: Martin, please take a look. Maybe interface should be changed
        SWRLImp imp = factory.createImp(headAtom, list);
        //SWRLImp imp = factory.createImp(headAtom, Collections.singleton(bodyAtom));
        imp.deleteImp();
        assertFalse(x.isDeleted());
        assertEquals(otherImpText, otherImp.getBrowserText());
    }
}
