package edu.stanford.smi.protegex.owl.swrl.model.impl;

import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLDifferentIndividualsAtom;

public class DefaultSWRLDifferentIndividualsAtom extends AbstractSWRLIndividualsAtom implements SWRLDifferentIndividualsAtom {

    public DefaultSWRLDifferentIndividualsAtom(KnowledgeBase kb, FrameID id) {
        super(kb, id);
    } // DefaultSWRLDifferentIndividualsAtom


    public DefaultSWRLDifferentIndividualsAtom() {
    }


    protected String getOperatorName() {
        return "differentFrom";
    }
} // DefaultSWRLDifferentIndiivdualsAtom


