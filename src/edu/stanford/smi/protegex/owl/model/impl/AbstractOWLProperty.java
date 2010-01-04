package edu.stanford.smi.protegex.owl.model.impl;

import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import javax.swing.*;
import java.util.Collection;
import java.util.Iterator;

/**
 * The default implementation of the OWLProperty interface.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public abstract class AbstractOWLProperty extends DefaultRDFProperty implements OWLProperty {

    public AbstractOWLProperty(KnowledgeBase kb, FrameID id) {
        super(kb, id);
    }


    AbstractOWLProperty() {
    }


    public ImageIcon getBaseImageIcon() {
        return OWLIcons.getImageIcon(getIconName());
    }


    public Icon getIcon() {
        String iconName = getIconName();
        if (isEditable()) {
            return OWLIcons.getImageIcon(iconName);
        }
        else {
            if (isAnnotationProperty()) {
                return OWLIcons.getReadOnlyAnnotationPropertyIcon(OWLIcons.getImageIcon(iconName));
            }
            else {
                return OWLIcons.getReadOnlyPropertyIcon(OWLIcons.getImageIcon(iconName));
            }
        }
    }


    public String getIconName() {
        String iconName = isObjectProperty() ?
                OWLIcons.OWL_OBJECT_PROPERTY : OWLIcons.OWL_DATATYPE_PROPERTY;
        if (isAnnotationProperty()) {
            iconName = "Annotation" + iconName;
        }
        return iconName;
    }


    public boolean isInverseFunctional() {
        RDFSClass metaclass = getOWLModel().getRDFSNamedClass(OWLNames.Cls.INVERSE_FUNCTIONAL_PROPERTY);
        if (hasProtegeType(metaclass)) {
            return true;
        }
        for (Iterator it = getSuperproperties(false).iterator(); it.hasNext();) {
            RDFProperty property = (RDFProperty) it.next();
            if (property instanceof OWLProperty) {
                if (((OWLProperty) property).isInverseFunctional()) {
                    return true;
                }
            }
        }
        return false;
    }


    public boolean isObjectProperty() {
        return getKnowledgeBase().getSlot(getName()) instanceof OWLObjectProperty;
        //getValueType() == ValueType.INSTANCE || getValueType() == ValueType.CLS;
    }


    public void setAnnotationProperty(boolean value) {
        updateRDFType(value, (RDFSClass) getKnowledgeBase().getCls(OWLNames.Cls.ANNOTATION_PROPERTY));
    }


    public void setEquivalentProperties(Collection slots) {
        Slot equivalentClassesSlot = getOWLModel().getOWLEquivalentPropertyProperty();
        setOwnSlotValues(equivalentClassesSlot, slots);
    }


    public void setInverseFunctional(boolean value) {
        RDFSClass metaclass = getOWLModel().getRDFSNamedClass(OWLNames.Cls.INVERSE_FUNCTIONAL_PROPERTY);
        updateRDFType(value, metaclass);
    }
}
