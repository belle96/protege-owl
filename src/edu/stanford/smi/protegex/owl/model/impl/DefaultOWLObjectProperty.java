package edu.stanford.smi.protegex.owl.model.impl;

import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protegex.owl.model.OWLNames;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.model.visitor.OWLModelVisitor;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * The default implementation of the OWLObjectProperty interface.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultOWLObjectProperty extends AbstractOWLProperty implements OWLObjectProperty {

    public DefaultOWLObjectProperty(KnowledgeBase kb, FrameID id) {
        super(kb, id);
    }


    public DefaultOWLObjectProperty() {
    }


    public void addUnionRangeClass(RDFSClass rangeClass) {
        Collection unionRange = new ArrayList(getUnionRangeClasses());
        if (!unionRange.contains(rangeClass)) {
            unionRange.add(rangeClass);
            setUnionRangeClasses(unionRange);
        }
    }


    public String getIconName() {
        if(isAnnotationProperty()) {
            return OWLIcons.OWL_OBJECT_ANNOTATION_PROPERTY;
        }
        else {
            return OWLIcons.OWL_OBJECT_PROPERTY;
        }
    }


    public Icon getInheritedIcon() {
        return OWLIcons.getImageIcon(OWLIcons.OWL_OBJECT_PROPERTY_INHERITED);
    }


    public Collection getUnionRange() {
        return getAllowedClses();
    }


    public boolean hasObjectRange() {
        return true;
    }


    public boolean isSymmetric() {
        RDFSNamedClass metaclass = getOWLModel().getRDFSNamedClass(OWLNames.Cls.SYMMETRIC_PROPERTY);
        return hasProtegeType(metaclass);
    }


    public boolean isTransitive() {
        RDFSNamedClass metaclass = getOWLModel().getRDFSNamedClass(OWLNames.Cls.TRANSITIVE_PROPERTY);
        return hasProtegeType(metaclass);
    }


    public void removeUnionRangeClass(RDFSClass rangeClass) {
        Collection unionRange = new ArrayList(getUnionRange());
        if (unionRange.contains(rangeClass)) {
            unionRange.remove(rangeClass);
            setUnionRangeClasses(unionRange);
        }
    }


    public void setSymmetric(boolean value) {
        RDFSNamedClass metaclass = getOWLModel().getRDFSNamedClass(OWLNames.Cls.SYMMETRIC_PROPERTY);
        updateRDFType(value, metaclass);
    }


    public void setTransitive(boolean value) {
        RDFSNamedClass metaclass = getOWLModel().getRDFSNamedClass(OWLNames.Cls.TRANSITIVE_PROPERTY);
        updateRDFType(value, metaclass);
    }


    public void accept(OWLModelVisitor visitor) {
        visitor.visitOWLObjectProperty(this);
    }

}
