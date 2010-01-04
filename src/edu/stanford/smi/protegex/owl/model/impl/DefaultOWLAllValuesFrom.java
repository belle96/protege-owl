package edu.stanford.smi.protegex.owl.model.impl;

import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protegex.owl.model.OWLAllValuesFrom;
import edu.stanford.smi.protegex.owl.model.OWLNames;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.visitor.OWLModelVisitor;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

/**
 * The default implementation of OWLAllValuesFrom.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultOWLAllValuesFrom extends AbstractOWLQuantifierRestriction
        implements OWLAllValuesFrom {

    public final static String ICON_NAME = "OWLAllValuesFrom";

    /**
     * The unicode operator symbol for this kind of restriction
     */
    public final static char OPERATOR = '\u2200';


    public DefaultOWLAllValuesFrom(KnowledgeBase kb, FrameID id) {
        super(kb, id);
    }


    public DefaultOWLAllValuesFrom() {
    }


    public void accept(OWLModelVisitor visitor) {
        visitor.visitOWLAllValuesFrom(this);
    }


    public RDFResource getAllValuesFrom() {
        return getFiller();
    }


    public RDFProperty getFillerProperty() {
        return getOWLModel().getRDFProperty(OWLNames.Slot.ALL_VALUES_FROM);
    }


    public String getIconName() {
        return OWLIcons.OWL_ALL_VALUES_FROM;
    }


    public char getOperator() {
        return OPERATOR;
    }


    public void setAllValuesFrom(RDFResource allValuesFrom) {
        setFiller(allValuesFrom);
    }
}
