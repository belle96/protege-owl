package edu.stanford.smi.protegex.owl.model.impl;

import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protegex.owl.model.OWLCardinality;
import edu.stanford.smi.protegex.owl.model.OWLNames;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.visitor.OWLModelVisitor;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

/**
 * A class representing a cardinality restriction.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultOWLCardinality extends AbstractOWLCardinalityBase
        implements OWLCardinality {

    /**
     * The unicode operator symbol for this kind of restriction
     */
    public final static char OPERATOR = '=';


    public DefaultOWLCardinality(KnowledgeBase kb, FrameID id) {
        super(kb, id, OPERATOR);
    }


    public DefaultOWLCardinality() {
        super(OPERATOR);
    }


    public void accept(OWLModelVisitor visitor) {
        visitor.visitOWLCardinality(this);
    }


    public String getIconName() {
        return OWLIcons.OWL_CARDINALITY;
    }


    public RDFProperty getFillerProperty() {
        return getOWLModel().getRDFProperty(OWLNames.Slot.CARDINALITY);
    }


    public char getOperator() {
        return OPERATOR;
    }
}
