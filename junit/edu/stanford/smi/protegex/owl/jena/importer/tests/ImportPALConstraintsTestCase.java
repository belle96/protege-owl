package edu.stanford.smi.protegex.owl.jena.importer.tests;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Model;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.ProtegeNames;

import java.util.Collection;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ImportPALConstraintsTestCase extends AbstractOWLImporterTestCase {

    public void testImportPALConstraint() {
        OWLNamedClass oldCls = owlModel.createOWLNamedClass("Cls");
        Cls oldConstraintCls = owlModel.getSystemFrames().getPalConstraintCls();
        Instance oldConstraint = oldConstraintCls.createDirectInstance("PAL");
        oldConstraint.setDirectOwnSlotValue(owlModel.getSystemFrames().getPalNameSlot(), "name");
        oldConstraint.setDirectOwnSlotValue(owlModel.getSystemFrames().getPalRangeSlot(), "range");
        ((Cls) oldCls).addOwnSlotValue(owlModel.getSystemFrames().getConstraintsSlot(), oldConstraint);
        KnowledgeBase kb = runOWLImporter();
        Cls newCls = kb.getCls(oldCls.getName());
        Collection newConstraints = newCls.getDirectOwnSlotValues(kb.getSystemFrames().getConstraintsSlot());
        assertSize(1, newConstraints);
        Instance newConstraint = (Instance) newConstraints.iterator().next();
        assertEquals(Model.Cls.PAL_CONSTRAINT, newConstraint.getDirectType().getName());
        assertEquals("name", newConstraint.getDirectOwnSlotValue(kb.getSystemFrames().getPalNameSlot()));
        assertEquals("range", newConstraint.getDirectOwnSlotValue(kb.getSystemFrames().getPalRangeSlot()));
    }
}
