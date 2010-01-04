package edu.stanford.smi.protegex.owl.model.impl;

import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.model.visitor.OWLModelVisitor;

import java.util.*;

public class DefaultOWLAllDifferent extends DefaultOWLIndividual
        implements OWLAllDifferent {

    public DefaultOWLAllDifferent(KnowledgeBase kb, FrameID id) {
        super(kb, id);
    }


    public DefaultOWLAllDifferent() {
    }


    public void accept(OWLModelVisitor visitor) {
        visitor.visitOWLAllDifferent(this);
    }


    public void addDistinctMember(RDFResource resource) {
        RDFProperty property = getOWLModel().getRDFProperty(OWLNames.Slot.DISTINCT_MEMBERS);
        RDFList value = (RDFList) getPropertyValue(property);
        if (value == null) {
            RDFList list = getOWLModel().createRDFList();
            list.setFirst(resource);
            list.setRest(getOWLModel().getRDFNil());
            setPropertyValue(property, list);
        }
        else {
            value.append(resource);
        }
    }


    public boolean equalsStructurally(RDFObject object) {
        if (object instanceof OWLAllDifferent) {
            OWLAllDifferent comp = (OWLAllDifferent) object;
            return OWLUtil.equalsStructurally(getDistinctMembers(), comp.getDistinctMembers());
        }
        return false;
    }


    public String getBrowserText() {
        String str = "AllDifferent {";
        Iterator it = listDistinctMembers();
        while (it.hasNext() && str.length() < 80) {
            RDFResource instance = (RDFResource) it.next();
            str += instance.getBrowserText();
            if (it.hasNext()) {
                str += ", ";
            }
        }
        if (it.hasNext()) {
            str += "...";
        }
        str += "}";
        return str;
    }


    public Collection getDistinctMembers() {
        RDFProperty property = getOWLModel().getRDFProperty(OWLNames.Slot.DISTINCT_MEMBERS);
        RDFList list = (RDFList) getPropertyValue(property);
        if (list == null) {
            return Collections.EMPTY_LIST;
        }
        else {
            return list.getValues();
        }
    }


    public Iterator listDistinctMembers() {
        return getDistinctMembers().iterator();
    }


    public Set getReferringAnonymousClasses() {
        return Collections.EMPTY_SET;
    }


    public void removeDistinctMember(RDFResource instance) {
        RDFProperty property = getOWLModel().getRDFProperty(OWLNames.Slot.DISTINCT_MEMBERS);
        DefaultRDFList.removeListValue(this, property, instance);
    }


    public void setDistinctMembers(List values) {
        for (Iterator it = getDistinctMembers().iterator(); it.hasNext();) {
            RDFResource resource = (RDFResource) it.next();
            removeDistinctMember(resource);
        }
        for (Iterator it = values.iterator(); it.hasNext();) {
            RDFResource resource = (RDFResource) it.next();
            addDistinctMember(resource);
        }
    }
}
