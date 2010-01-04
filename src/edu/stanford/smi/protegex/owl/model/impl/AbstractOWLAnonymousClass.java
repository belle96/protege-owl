package edu.stanford.smi.protegex.owl.model.impl;

import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Reference;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.model.OWLAnonymousClass;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFList;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import javax.swing.*;
import java.util.*;
import java.util.logging.Level;

/**
 * A base implementation of OWLAnonymousClass.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public abstract class AbstractOWLAnonymousClass extends AbstractRDFSClass
        implements OWLAnonymousClass {


    public AbstractOWLAnonymousClass(KnowledgeBase kb, FrameID id) {
        super(kb, id);
        // setVisible(false);
    }


    public AbstractOWLAnonymousClass() {
    }


    public String getBrowserText() {
        return getOWLModel().getOWLClassDisplay().getDisplayText(this);
    }


    public Collection getDependingClasses() {
        return Collections.EMPTY_SET;
    }


    public OWLAnonymousClass getExpressionRoot() {
        OWLAnonymousClass root = this;
        try {
            root = (OWLAnonymousClass) new GetExpressionRootForAnonymousClassJob(getOWLModel(), this).execute();
        } catch (Throwable t) { 
            Log.getLogger().log(Level.SEVERE, "Could not compute expression root for anonymous class " + this, t);
        }
        return root;
    }


    public Icon getIcon() {
        // Suffix after "Default" in the class name is the icon name
        String className = getClass().getName();
        int index = className.lastIndexOf('.');
        String iconName = className.substring(index + 8);
        if (isEditable()) {
            return OWLIcons.getImageIcon(iconName);
        }
        else {
            return OWLIcons.getReadOnlyClsIcon(OWLIcons.getImageIcon(iconName));
        }
    }


    public ImageIcon getImageIcon() {
        // Suffix after "Default" in the class name is the icon name
        String className = getClass().getName();
        int index = className.lastIndexOf('.');
        String iconName = className.substring(index + 8);
        return OWLIcons.getImageIcon(iconName);
    }


    public OWLNamedClass getOwner() {
        OWLNamedClass owner = null;
        try {
            owner = (OWLNamedClass) new GetOwnerForAnonymousClassJob(getOWLModel(), this).execute();
        } catch (Throwable t) {
            Log.getLogger().log(Level.SEVERE, "Could not get owner for anonymous class: " + this, t);
        }
        return owner;
    }


    public boolean isAnonymous() {
        return true;
    }


    public boolean isVisible() {
        return false;
    }
}
