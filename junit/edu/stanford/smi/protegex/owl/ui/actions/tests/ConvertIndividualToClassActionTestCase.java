package edu.stanford.smi.protegex.owl.ui.actions.tests;

import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLHasValue;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.RDFIndividual;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;
import edu.stanford.smi.protegex.owl.ui.actions.ConvertIndividualToClassAction;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ConvertIndividualToClassActionTestCase extends AbstractJenaTestCase {

    public void testDirectTypeBecomesSuperclassAndNameEquals() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Person");
        final String name = "Instance";
        RDFIndividual instance = cls.createOWLIndividual(name);
        String newName = ConvertIndividualToClassAction.getClsName(instance);
        OWLNamedClass result = ConvertIndividualToClassAction.performAction(instance);
        assertEquals(newName, result.getName());
        assertSize(1, result.getSuperclasses(false));
        assertContains(cls, result.getSuperclasses(false));
        assertSize(1, result.getInstances(false));
        assertContains(instance, result.getInstances(false));
    }


    public void testConvertDatatypePropertyValues() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Person");
        OWLDatatypeProperty property = owlModel.createOWLDatatypeProperty("age", owlModel.getXSDint());
        property.setDomain(cls);
        final String name = "Instance";
        RDFIndividual instance = cls.createOWLIndividual(name);
        final Integer value = new Integer(42);
        instance.setPropertyValue(property, value);
        OWLNamedClass result = ConvertIndividualToClassAction.performAction(instance);
        assertSize(1, result.getRestrictions(false));
        OWLHasValue hasRestriction = (OWLHasValue) result.getRestrictions(false).iterator().next();
        assertEquals(value, hasRestriction.getHasValue());
        assertEquals(property, hasRestriction.getOnProperty());
    }


    public void testConvertObjectPropertyValues() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Person");
        OWLObjectProperty property = owlModel.createOWLObjectProperty("children");
        property.setDomain(cls);
        final String name = "Instance";
        RDFIndividual instance = cls.createOWLIndividual(name);
        Instance otherInstance = cls.createOWLIndividual(null);
        instance.setPropertyValue(property, otherInstance);
        OWLNamedClass result = ConvertIndividualToClassAction.performAction(instance);
        assertSize(1, result.getRestrictions(false));
        OWLHasValue hasRestriction = (OWLHasValue) result.getRestrictions(false).iterator().next();
        assertEquals(otherInstance, hasRestriction.getHasValue());
        assertEquals(property, hasRestriction.getOnProperty());
    }
}
