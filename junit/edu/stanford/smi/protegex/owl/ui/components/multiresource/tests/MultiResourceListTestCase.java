package edu.stanford.smi.protegex.owl.ui.components.multiresource.tests;

import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;
import edu.stanford.smi.protegex.owl.ui.components.multiresource.MultiResourceList;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class MultiResourceListTestCase extends AbstractJenaTestCase {

    public void testIsRemoveEnabled() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Class");
        RDFResource subject = cls.createOWLIndividual("Individual");
        RDFProperty predicate = owlModel.createOWLObjectProperty("property");
        RDFResource a = owlThing.createOWLIndividual("A");
        RDFResource b = owlThing.createOWLIndividual("B");
        cls.addSuperclass(owlModel.createOWLHasValue(predicate, a));
        subject.addPropertyValue(predicate, b);

        MultiResourceList list = new MultiResourceList(predicate, false);
        list.getListModel().setSubject(subject);
        assertFalse(list.isRemoveEnabled());
        list.setSelectedIndex(0);
        assertFalse(list.isRemoveEnabled());
        list.setSelectedIndices(new int[]{0, 1});
        assertFalse(list.isRemoveEnabled());
        list.setSelectedIndex(1);
        assertTrue(list.isRemoveEnabled());
    }
}
