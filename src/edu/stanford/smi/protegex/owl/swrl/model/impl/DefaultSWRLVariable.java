package edu.stanford.smi.protegex.owl.swrl.model.impl;

import java.util.Set;

import javax.swing.Icon;

import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protegex.owl.model.NamespaceUtil;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable;
import edu.stanford.smi.protegex.owl.swrl.ui.icons.SWRLIcons;

public class DefaultSWRLVariable extends AbstractSWRLIndividual implements SWRLVariable {
	
    public DefaultSWRLVariable(KnowledgeBase kb, FrameID id) {
        super(kb, id);
    }


    public DefaultSWRLVariable() {
    }

    @Override
    public String getBrowserText() {
        //return "?" + NamespaceUtil.getPrefixedName(getOWLModel(), getName());
    	return "?" + NamespaceUtil.getLocalName(getName());
    }


    @Override
    public Icon getIcon() {
        return SWRLIcons.getVariableIcon();
    }


    @Override
    public void getReferencedInstances(Set<RDFResource> set) {
    }
}
