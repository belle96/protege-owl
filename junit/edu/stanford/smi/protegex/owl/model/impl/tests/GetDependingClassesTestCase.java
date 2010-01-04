package edu.stanford.smi.protegex.owl.model.impl.tests;

import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class GetDependingClassesTestCase extends AbstractJenaTestCase {

    /**
     * * children !A      -> !A
     */
    public void testAllRestriction() {
        OWLNamedClass namedCls = owlModel.createOWLNamedClass("A");
        OWLComplementClass complementCls = owlModel.createOWLComplementClass(namedCls);
        OWLObjectProperty slot = owlModel.createOWLObjectProperty("slot");
        OWLRestriction restriction = owlModel.createOWLAllValuesFrom(slot, complementCls);
        Collection deps = restriction.getDependingClasses();
        assertSize(1, deps);
        assertContains(complementCls, deps);
    }


    /**
     * * children A      -> {}
     */
    public void testAllRestrictionWithNamedCls() {
        OWLNamedClass namedCls = owlModel.createOWLNamedClass("A");
        OWLObjectProperty slot = owlModel.createOWLObjectProperty("slot");
        OWLRestriction restriction = owlModel.createOWLAllValuesFrom(slot, namedCls);
        assertSize(0, restriction.getDependingClasses());
    }


    /**
     * !A
     */
    public void testComplementCls() {
        OWLNamedClass aCls = owlModel.createOWLNamedClass("A");
        OWLComplementClass complementCls = owlModel.createOWLComplementClass(aCls);
        assertSize(0, complementCls.getDependingClasses());
    }


    /**
     * !A & B & !C    -> !A , !C
     */
    public void testIntersectionCls() {
        OWLNamedClass aCls = owlModel.createOWLNamedClass("A");
        OWLNamedClass bCls = owlModel.createOWLNamedClass("B");
        OWLNamedClass cCls = owlModel.createOWLNamedClass("C");
        OWLComplementClass complementA = owlModel.createOWLComplementClass(aCls);
        OWLComplementClass complementC = owlModel.createOWLComplementClass(cCls);
        OWLLogicalClass logicalCls = owlModel.createOWLIntersectionClass(Arrays.asList(new Object[]{
                complementA,
                bCls,
                complementC
        }));
        Collection deps = logicalCls.getDependingClasses();
        assertSize(2, deps);
        assertContains(complementA, deps);
        assertContains(complementC, deps);
    }


    /**
     * ? children !(A | B)     -> !(A | B)
     */
    public void testSomeRestriction() {
        OWLNamedClass aCls = owlModel.createOWLNamedClass("A");
        OWLNamedClass bCls = owlModel.createOWLNamedClass("B");
        OWLUnionClass unionCls = owlModel.createOWLUnionClass();
        unionCls.addOperand(aCls);
        unionCls.addOperand(bCls);
        OWLComplementClass complementCls = owlModel.createOWLComplementClass(unionCls);
        OWLObjectProperty slot = owlModel.createOWLObjectProperty("slot");
        OWLRestriction restriction = owlModel.createOWLSomeValuesFrom(slot, complementCls);
        Collection deps = restriction.getDependingClasses();
        assertSize(1, deps);
        assertContains(complementCls, deps);
    }


    /**
     * !A | B | (* children !C)    -> !A , (* children !C)
     */
    public void testUnionCls() {
        OWLObjectProperty slot = owlModel.createOWLObjectProperty("slot");
        OWLNamedClass aCls = owlModel.createOWLNamedClass("A");
        OWLNamedClass bCls = owlModel.createOWLNamedClass("B");
        OWLNamedClass cCls = owlModel.createOWLNamedClass("C");
        OWLComplementClass complementA = owlModel.createOWLComplementClass(aCls);
        OWLComplementClass complementC = owlModel.createOWLComplementClass(cCls);
        OWLRestriction restriction = owlModel.createOWLAllValuesFrom(slot, complementC);
        OWLLogicalClass logicalCls = owlModel.createOWLUnionClass(Arrays.asList(new Object[]{
                complementA,
                bCls,
                restriction
        }));
        Collection deps = logicalCls.getDependingClasses();
        assertSize(2, deps);
        assertContains(complementA, deps);
        assertContains(restriction, deps);
    }
}
