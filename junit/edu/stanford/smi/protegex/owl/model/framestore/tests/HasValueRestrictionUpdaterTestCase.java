package edu.stanford.smi.protegex.owl.model.framestore.tests;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Facet;
import edu.stanford.smi.protege.model.Model;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLHasValue;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.impl.DefaultOWLNamedClass;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class HasValueRestrictionUpdaterTestCase extends AbstractJenaTestCase {

    public void testAddHasRestrictionOnStringProperty() {
        OWLNamedClass cls = owlModel.createOWLNamedSubclass("Cls", owlModel.getOWLThingClass());
        OWLDatatypeProperty slot = owlModel.createOWLDatatypeProperty("slot", owlModel.getXSDstring());
        slot.addUnionDomainClass(cls);
        OWLHasValue hasRestriction = owlModel.createOWLHasValue(slot, "value");
        cls.addSuperclass(hasRestriction);
    }


    public void testValuesFacetOverload1() {
        DefaultOWLNamedClass cls = (DefaultOWLNamedClass) owlModel.createOWLNamedClass("Cls");
        OWLDatatypeProperty slot = owlModel.createOWLDatatypeProperty("slot", owlModel.getXSDboolean());
        slot.addUnionDomainClass(cls);
        final Facet facet = owlModel.getFacet(Model.Facet.VALUES);
        ((Cls) cls).setTemplateFacetValue(slot, facet, Boolean.TRUE);
        assertTrue(((Cls) cls).hasDirectlyOverriddenTemplateSlot(slot));
        assertTrue(((Cls) cls).hasDirectlyOverriddenTemplateFacet(slot, facet));
        assertEquals(Boolean.TRUE, cls.getDirectTemplateFacetValue(slot, facet));
        assertSize(2, cls.getDirectSuperclasses());
    }


    public void testValuesFacetOverload2() {
        DefaultOWLNamedClass cls = (DefaultOWLNamedClass) owlModel.createOWLNamedClass("Cls");
        OWLDatatypeProperty slot = owlModel.createOWLDatatypeProperty("slot", owlModel.getXSDboolean());
        slot.addUnionDomainClass(cls);
        final Facet facet = owlModel.getFacet(Model.Facet.VALUES);
        ((Cls) cls).setTemplateSlotValue(slot, Boolean.TRUE);
        assertTrue(((Cls) cls).hasDirectlyOverriddenTemplateSlot(slot));
        assertTrue(((Cls) cls).hasDirectlyOverriddenTemplateFacet(slot, facet));
        assertEquals(Boolean.TRUE, cls.getDirectTemplateFacetValue(slot, facet));
        assertSize(2, cls.getDirectSuperclasses());
    }
}
