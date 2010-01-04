package edu.stanford.smi.protegex.owl.model.impl;

import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protegex.owl.model.OWLMaxCardinality;
import edu.stanford.smi.protegex.owl.model.OWLNames;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.visitor.OWLModelVisitor;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

/**
 * A Cls representing a maximumCardinality restriction.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultOWLMaxCardinality extends AbstractOWLCardinalityBase
        implements OWLMaxCardinality {

    /**
     * The unicode operator symbol for this kind of restriction
     */
    public final static char OPERATOR = '\u2264';


    public DefaultOWLMaxCardinality(KnowledgeBase kb, FrameID id) {
        super(kb, id, OPERATOR);
    }


    public DefaultOWLMaxCardinality() {
        super(OPERATOR);
    }


    public void accept(OWLModelVisitor visitor) {
        visitor.visitOWLMaxCardinality(this);
    }


    public RDFProperty getFillerProperty() {
        return getOWLModel().getRDFProperty(OWLNames.Slot.MAX_CARDINALITY);
    }


    public String getIconName() {
        return OWLIcons.OWL_MAX_CARDINALITY;
    }


    public char getOperator() {
        return OPERATOR;
    }
}
