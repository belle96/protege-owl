package edu.stanford.smi.protegex.owl.model.impl;

import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.RDFObject;
import edu.stanford.smi.protegex.owl.model.visitor.OWLModelVisitor;

public class DefaultOWLIndividual extends DefaultRDFIndividual implements OWLIndividual {

    public DefaultOWLIndividual(KnowledgeBase kb, FrameID id) {
        super(kb, id);
    }


    public DefaultOWLIndividual() {
    }


    public void accept(OWLModelVisitor visitor) {
        visitor.visitOWLIndividual(this);
    }


    public boolean equalsStructurally(RDFObject object) {
        if (object instanceof OWLIndividual) {
            return getName().equals(((OWLIndividual) object).getName());
        }
        return false;
    }
}
