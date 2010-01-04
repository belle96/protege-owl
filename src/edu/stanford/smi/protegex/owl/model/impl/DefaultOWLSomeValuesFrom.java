package edu.stanford.smi.protegex.owl.model.impl;

import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protegex.owl.model.OWLNames;
import edu.stanford.smi.protegex.owl.model.OWLSomeValuesFrom;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.visitor.OWLModelVisitor;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

/**
 * The default implementation of OWLSomeValuesFrom.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultOWLSomeValuesFrom extends AbstractOWLQuantifierRestriction
        implements OWLSomeValuesFrom {

    /**
     * The unicode operator symbol for this kind of restriction
     */
    public final static char OPERATOR = '\u2203';


    public DefaultOWLSomeValuesFrom(KnowledgeBase kb, FrameID id) {
        super(kb, id);
    }


    public DefaultOWLSomeValuesFrom() {
    }


    public void accept(OWLModelVisitor visitor) {
        visitor.visitOWLSomeValuesFrom(this);
    }


    public RDFProperty getFillerProperty() {
        return getOWLModel().getRDFProperty(OWLNames.Slot.SOME_VALUES_FROM);
    }


    public String getIconName() {
        return OWLIcons.OWL_SOME_VALUES_FROM;
    }


    public char getOperator() {
        return OPERATOR;
    }


    public RDFResource getSomeValuesFrom() {
        return getFiller();
    }


    public void setSomeValuesFrom(RDFResource someValuesFrom) {
        setFiller(someValuesFrom);
    }
}
