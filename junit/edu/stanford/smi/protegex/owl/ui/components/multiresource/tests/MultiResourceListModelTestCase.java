package edu.stanford.smi.protegex.owl.ui.components.multiresource.tests;

import edu.stanford.smi.protege.util.FrameWithBrowserText;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;
import edu.stanford.smi.protegex.owl.ui.components.multiresource.MultiResourceListModel;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class MultiResourceListModelTestCase extends AbstractJenaTestCase {

    public void testGetAndSetValues() {
        RDFResource subject = owlThing.createOWLIndividual("Instance");
        RDFProperty predicate = owlModel.createRDFProperty("property");
        MultiResourceListModel listModel = new MultiResourceListModel(predicate);
        listModel.setSubject(subject);
        assertEquals(0, listModel.getSize());

        subject.addPropertyValue(predicate, owlThing);
        listModel.updateValues();
        assertEquals(1, listModel.getSize());
        assertEquals(owlThing, ((FrameWithBrowserText) listModel.getElementAt(0)).getFrame());
        assertTrue(listModel.isRDFResource(0));
        assertEquals(owlThing, listModel.getResourceAt(0));
        assertTrue(listModel.isEditable(0));
    }


    public void testHasValueRestrictions() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Class");
        RDFResource subject = cls.createOWLIndividual("Individual");
        RDFProperty predicate = owlModel.createOWLObjectProperty("property");
        RDFResource a = owlThing.createOWLIndividual("A");
        RDFResource b = owlThing.createOWLIndividual("B");
        cls.addSuperclass(owlModel.createOWLHasValue(predicate, a));

        MultiResourceListModel listModel = new MultiResourceListModel(predicate);
        listModel.setSubject(subject);
        assertEquals(1, listModel.getSize());
        assertEquals(a, listModel.getResourceAt(0));
        assertFalse(listModel.isEditable(0));

        subject.addPropertyValue(predicate, b);
        listModel.updateValues();
        assertEquals(2, listModel.getSize());
        assertTrue(a.equals(listModel.getResourceAt(0)) || a.equals(listModel.getResourceAt(1)));
        assertTrue(b.equals(listModel.getResourceAt(0)) || b.equals(listModel.getResourceAt(1)));
    }


    public void testSubpropertyValue() {
        OWLNamedClass type = owlModel.createOWLNamedClass("Class");
        RDFProperty predicate = owlModel.createOWLObjectProperty("superproperty");
        RDFProperty subproperty = owlModel.createOWLObjectProperty("subproperty");
        subproperty.addSuperproperty(predicate);
        OWLIndividual subject = type.createOWLIndividual("Individual");
        subject.addPropertyValue(subproperty, owlThing);
        MultiResourceListModel listModel = new MultiResourceListModel(predicate);
        listModel.setSubject(subject);
        assertEquals(1, listModel.getSize());
        assertEquals(owlThing, listModel.getResourceAt(0));
        assertFalse(listModel.isEditable(0));
    }
}
