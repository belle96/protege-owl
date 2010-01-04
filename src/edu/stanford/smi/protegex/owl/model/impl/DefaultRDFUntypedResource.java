package edu.stanford.smi.protegex.owl.model.impl;

import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protegex.owl.model.RDFUntypedResource;
import edu.stanford.smi.protegex.owl.model.visitor.OWLModelVisitor;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import javax.swing.*;

/**
 * The default implementation of RDFUntypedResource.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultRDFUntypedResource extends DefaultRDFIndividual implements RDFUntypedResource {

    public DefaultRDFUntypedResource(KnowledgeBase kb, FrameID id) {
        super(kb, id);
    }


    public DefaultRDFUntypedResource() {
    }


    public void accept(OWLModelVisitor visitor) {
        visitor.visitRDFUntypedResource(this);
    }


    public Icon getIcon() {
        return isEditable() ? OWLIcons.getExternalResourceIcon() :
                OWLIcons.getReadOnlyClsIcon(OWLIcons.getExternalResourceIcon());
    }


    public String getLocalName() {
        return getName();
    }


    public String getNamespace() {
        return null;
    }


    public String getNamespacePrefix() {
        return null;
    }


    public String getURI() {
        return getName();
    }
}
