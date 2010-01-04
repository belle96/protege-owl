package edu.stanford.smi.protegex.owl.model.impl;

import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protegex.owl.model.OWLIntersectionClass;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.visitor.OWLModelVisitor;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

/**
 * The default implementation of the OWLIntersectionClass interface.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultOWLIntersectionClass extends AbstractOWLNAryLogicalClass
        implements OWLIntersectionClass {

    /**
     * The unicode operator symbol for this kind of class
     */
    public final static char OPERATOR = '\u2293';


    public DefaultOWLIntersectionClass(KnowledgeBase kb, FrameID id) {
        super(kb, id);
    }


    public DefaultOWLIntersectionClass() {
    }


    public void accept(OWLModelVisitor visitor) {
        visitor.visitOWLIntersectionClass(this);
    }


    public String getIconName() {
        return OWLIcons.OWL_INTERSECTION_CLASS;
    }


    public RDFProperty getOperandsProperty() {
        return getOWLModel().getOWLIntersectionOfProperty();
    }


    public char getOperatorSymbol() {
        return OPERATOR;
    }
}
