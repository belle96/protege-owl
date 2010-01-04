package edu.stanford.smi.protegex.owl.model.impl;

import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protegex.owl.model.OWLMinCardinality;
import edu.stanford.smi.protegex.owl.model.OWLNames;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.visitor.OWLModelVisitor;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

/**
 * A Cls representing a minimumCardinality restriction.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultOWLMinCardinality extends AbstractOWLCardinalityBase
        implements OWLMinCardinality {

    public final static char OPERATOR = '\u2265';


    public DefaultOWLMinCardinality(KnowledgeBase kb, FrameID id) {
        super(kb, id, OPERATOR);
    }


    public DefaultOWLMinCardinality() {
        super(OPERATOR);
    }


    public void accept(OWLModelVisitor visitor) {
        visitor.visitOWLMinCardinality(this);
    }


    public RDFProperty getFillerProperty() {
        return getOWLModel().getRDFProperty(OWLNames.Slot.MIN_CARDINALITY);
    }


    public String getIconName() {
        return OWLIcons.OWL_MIN_CARDINALITY;
    }


    public char getOperator() {
        return OPERATOR;
    }
}
