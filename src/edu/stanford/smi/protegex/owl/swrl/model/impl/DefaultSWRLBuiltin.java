package edu.stanford.smi.protegex.owl.swrl.model.impl;

import java.util.Set;

import javax.swing.Icon;

import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLBuiltin;
import edu.stanford.smi.protegex.owl.swrl.ui.icons.SWRLIcons;

public class DefaultSWRLBuiltin extends AbstractSWRLIndividual implements SWRLBuiltin {

    public DefaultSWRLBuiltin(KnowledgeBase kb, FrameID id) {
        super(kb, id);
    }


    public DefaultSWRLBuiltin() {
    }


    public Icon getIcon() {
        return SWRLIcons.getBuiltinIcon();
    }


    public void getReferencedInstances(Set<RDFResource> set) {
    }
}
