package edu.stanford.smi.protegex.owl.model.impl;

import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.visitor.OWLModelVisitor;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import javax.swing.*;

/**
 * The default implementation of the OWLDatatypeProperty interface.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultOWLDatatypeProperty extends AbstractOWLProperty implements OWLDatatypeProperty {

    public final static String ICON_NAME = "OWLDatatypeProperty";


    public DefaultOWLDatatypeProperty(KnowledgeBase kb, FrameID id) {
        super(kb, id);
    }


    public DefaultOWLDatatypeProperty() {
    }


    public String getIconName() {
        if (isAnnotationProperty()) {
            return OWLIcons.OWL_DATATYPE_ANNOTATION_PROPERTY;
        }
        else {
            return OWLIcons.OWL_DATATYPE_PROPERTY;
        }
    }


    public Icon getInheritedIcon() {
        return OWLIcons.getImageIcon(OWLIcons.OWL_DATATYPE_PROPERTY_INHERITED);
    }


    public boolean hasDatatypeRange() {
        return true;
    }


    public void accept(OWLModelVisitor visitor) {
        visitor.visitOWLDatatypeProperty(this);
    }
}
