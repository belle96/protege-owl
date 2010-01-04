package edu.stanford.smi.protegex.owl.swrl.model.impl.tests;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLAtom;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLClassAtom;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLImp;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultSWRLImpTestCase extends AbstractSWRLTestCase {

    public void testCreateClone() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        SWRLVariable x = factory.createVariable("x");
        SWRLClassAtom headAtom = factory.createClassAtom(cls, x);
        SWRLClassAtom bodyAtom = factory.createClassAtom(cls, x);
        //TODO: Martin, please take a look. Maybe interface should be changed
        List<SWRLAtom> list = new ArrayList<SWRLAtom>();
        list.add(bodyAtom);
        SWRLImp imp = factory.createImp(headAtom, list);
      //SWRLImp imp = factory.createImp(headAtom, Collections.singleton(bodyAtom));
        SWRLImp c = imp.createClone();
        assertFalse(imp.equals(c));
        assertEquals(c.getBrowserText(), imp.getBrowserText());
    }
}
