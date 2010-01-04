package edu.stanford.smi.protegex.owl.model.impl;

import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protegex.owl.model.OWLUnionClass;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.visitor.OWLModelVisitor;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

/**
 * The default implementation of OWLUnionClass.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultOWLUnionClass extends AbstractOWLNAryLogicalClass
        implements OWLUnionClass {

    /**
     * The unicode operator symbol for this kind of class
     */
    public final static char OPERATOR = '\u2294';


    public DefaultOWLUnionClass(KnowledgeBase kb, FrameID id) {
        super(kb, id);
    }


    public DefaultOWLUnionClass() {
    }


    public void accept(OWLModelVisitor visitor) {
        visitor.visitOWLUnionClass(this);
    }


    public String getIconName() {
        return OWLIcons.OWL_UNION_CLASS;
    }


    public RDFProperty getOperandsProperty() {
        return getOWLModel().getOWLUnionOfProperty();
    }


    public char getOperatorSymbol() {
        return OPERATOR;
    }
}
