package edu.stanford.smi.protegex.owl.model.impl;

import edu.stanford.smi.protege.model.DefaultSimpleInstance;
import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protegex.owl.model.OWLNames;
import edu.stanford.smi.protegex.owl.model.RDFExternalResource;
import edu.stanford.smi.protegex.owl.model.visitor.OWLModelVisitor;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import javax.swing.*;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultRDFExternalResource extends DefaultSimpleInstance implements RDFExternalResource {

    public DefaultRDFExternalResource(KnowledgeBase kb, FrameID id) {
        super(kb, id);
    }


    public DefaultRDFExternalResource() {
    }


    public String getBrowserText() {
        String uri = getResourceURI();
        return uri == null ? "" : uri;
    }


    public Icon getIcon() {
        return isEditable() ? OWLIcons.getExternalResourceIcon() :
                OWLIcons.getReadOnlyClsIcon(OWLIcons.getExternalResourceIcon());
    }


    public String getResourceURI() {
        return (String) getDirectOwnSlotValue(getResourceURISlot());
    }


    private Slot getResourceURISlot() {
        return getKnowledgeBase().getSlot(OWLNames.Slot.RESOURCE_URI);
    }

    //public boolean isSystem() {
    //    return OWLUtil.isSystem(this);
    //}


    public void setResourceURI(String value) {
        setDirectOwnSlotValue(getResourceURISlot(), value);
    }


    public void accept(OWLModelVisitor visitor) {
        // visitor.visitRDFExternalResource(this);
    }
}
