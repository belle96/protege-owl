package edu.stanford.smi.protegex.owl.model.framestore.tests;

import edu.stanford.smi.protege.model.Facet;
import edu.stanford.smi.protege.model.Model;
import edu.stanford.smi.protegex.owl.model.OWLCardinality;
import edu.stanford.smi.protegex.owl.model.OWLProperty;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.impl.AbstractRDFSClass;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

public class CardinalityRestrictionUpdaterTestCase extends AbstractJenaTestCase {

    public void testCardiAdded() throws Exception {
        RDFSClass personClass = owlModel.createOWLNamedClass("Person");
        OWLProperty property = owlModel.createOWLObjectProperty("workplace");
        property.addUnionDomainClass(personClass);
        property.setRange(owlModel.createOWLNamedClass("Workplace"));
        property.setFunctional(true);
        AbstractRDFSClass happyPersonClass = (AbstractRDFSClass) owlModel.createOWLNamedClass("HappyPerson");
        happyPersonClass.addSuperclass(personClass);

        OWLCardinality cardiRestriction = owlModel.createOWLCardinality(property, 3);
        happyPersonClass.addSuperclass(cardiRestriction);
        Facet minFacet = owlModel.getFacet(Model.Facet.MINIMUM_CARDINALITY);
        Facet maxFacet = owlModel.getFacet(Model.Facet.MAXIMUM_CARDINALITY);
        assertEquals(new Integer(3), happyPersonClass.getDirectTemplateFacetValue(property, minFacet));
        assertEquals(new Integer(3), happyPersonClass.getDirectTemplateFacetValue(property, maxFacet));
    }
}
