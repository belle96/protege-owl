package edu.stanford.smi.protegex.owl.model.classdisplay.tests;

import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.model.classdisplay.OWLClassDisplay;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * A generic test driver for various different class renderers - making it easier
 * to compare their results.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public abstract class AbstractOWLClassRendererTestCase extends AbstractJenaTestCase {

    private OWLClassDisplay renderer;


    public AbstractOWLClassRendererTestCase(OWLClassDisplay renderer) {
        this.renderer = renderer;
    }


    // property ? (Class | !Class)
    protected OWLSomeValuesFrom getSomeValuesFromWithNestedUnion() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Class");
        OWLUnionClass unionClass = owlModel.createOWLUnionClass();
        unionClass.addOperand(cls);
        unionClass.addOperand(owlModel.createOWLComplementClass(cls));
        RDFProperty property = owlModel.createRDFProperty("property");
        return owlModel.createOWLSomeValuesFrom(property, unionClass);
    }


    // !(Class | (property = 1))
    protected OWLComplementClass getComplexComplement() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Class");
        RDFProperty property = owlModel.createRDFProperty("property");
        OWLRestriction restriction = owlModel.createOWLCardinality(property, 1);
        OWLUnionClass unionClass = owlModel.createOWLUnionClass();
        unionClass.addOperand(cls);
        unionClass.addOperand(restriction);
        return owlModel.createOWLComplementClass(unionClass);
    }


    protected String getDisplayText(RDFSClass cls) {
        return renderer.getDisplayText(cls);
    }
}
