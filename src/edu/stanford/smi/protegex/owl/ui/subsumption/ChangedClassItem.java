package edu.stanford.smi.protegex.owl.ui.subsumption;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLNames;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.impl.OWLUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * An object representing the subsumption changes for a single class.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
class ChangedClassItem implements Comparable<ChangedClassItem> {

    private OWLNamedClass cls;

    private Collection<RDFSClass> addedSuperClses = new ArrayList<RDFSClass>();

    private Collection<RDFSClass> removedSuperClses = new ArrayList<RDFSClass>();


    ChangedClassItem(OWLNamedClass cls) {
        this.cls = cls;
    }


    void addAddedSuperCls(Cls superCls) {
        addedSuperClses.add((RDFSClass) superCls);
    }


    void addRemovedSuperCls(Cls superCls) {
        removedSuperClses.add((RDFSClass) superCls);
    }


    void assertChange() {
        for (Iterator<RDFSClass> it = addedSuperClses.iterator(); it.hasNext();) {
            RDFSClass superCls = it.next();
            cls.addSuperclass(superCls);
        }
        for (Iterator<RDFSClass> it = removedSuperClses.iterator(); it.hasNext();) {
            RDFSClass superCls = (RDFSClass) it.next();
            cls.removeSuperclass(superCls);
        }
        OWLUtil.setConsistentClassificationStatus(cls);
    }


    public int compareTo(ChangedClassItem other) {
        return cls.compareTo(other.cls);
    }


    OWLNamedClass getCls() {
        return cls;
    }


    void removeAddedSuperCls(Cls superCls) {
        removedSuperClses.add((RDFSClass) superCls);
    }


    public String toString() {
        if (cls.getClassificationStatus() == OWLNames.CLASSIFICATION_STATUS_INCONSISTENT) {
            return "Inconsistent";
        }
        else {
            if (addedSuperClses.size() == 1 && removedSuperClses.size() == 1) {
                Cls removedSuperCls = (Cls) removedSuperClses.iterator().next();
                Cls addedSuperCls = (Cls) addedSuperClses.iterator().next();
                return "Moved from " + removedSuperCls.getBrowserText() +
                        " to " + addedSuperCls.getBrowserText();
            }
            else if (addedSuperClses.size() > 0 && removedSuperClses.size() > 0) {
                return toString("Moved from", removedSuperClses.iterator()) +
                        toString(" to", addedSuperClses.iterator());
            }
            else if (addedSuperClses.size() > 0) {
                Iterator<RDFSClass> added = addedSuperClses.iterator();
                return toString("Added", added);
            }
            else {
                Iterator<RDFSClass> removed = removedSuperClses.iterator();
                return toString("Removed", removed);
            }
        }
    }


    private String toString(String base, Iterator added) {
        int count = 0;
        String str = "";
        while (added.hasNext()) {
            Cls c = (Cls) added.next();
            str += c.getBrowserText();
            if (added.hasNext()) {
                str += ", ";
            }
            count++;
        }
        /*base += " superclass";
        if (count > 1) {
            base += "es";
        } */
        return base + " " + str;
    }
}
