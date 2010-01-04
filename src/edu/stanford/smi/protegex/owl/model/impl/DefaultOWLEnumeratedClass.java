package edu.stanford.smi.protegex.owl.model.impl;

import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.model.visitor.OWLModelVisitor;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;


/**
 * The default implementation of OWLEnumeratedClass.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultOWLEnumeratedClass extends AbstractOWLAnonymousClass
        implements OWLEnumeratedClass {


    public DefaultOWLEnumeratedClass(KnowledgeBase kb, FrameID id) {
        super(kb, id);
    }


    public DefaultOWLEnumeratedClass() {
    }


    public void accept(OWLModelVisitor visitor) {
        visitor.visitOWLEnumeratedClass(this);
    }


    public void addOneOf(RDFResource resource) {
        final RDFProperty property = getOWLModel().getOWLOneOfProperty();
        RDFList list = (RDFList) getPropertyValue(property);
        if (list == null || getOWLModel().getRDFNil().equals(list)) {
            list = getOWLModel().createRDFList();
            setPropertyValue(property, list);
        }
        list.append(resource);
    }


    public boolean equalsStructurally(RDFObject object) {
        if (object instanceof OWLEnumeratedClass) {
            OWLEnumeratedClass compCls = (OWLEnumeratedClass) object;
            return OWLUtil.equalsStructurally(getOneOf(), compCls.getOneOf());
        }
        else {
            return false;
        }
    }

    /*public String getBrowserText() {
       Collection values = getOneOf();
       String str = "{";
       for (Iterator it = values.iterator(); it.hasNext();) {
           Instance instance = (Instance) it.next();
           str += instance.getBrowserText();
           if (it.hasNext()) {
               str += " ";
           }
       }
       return str + "}";
   } */


    public String getIconName() {
        return OWLIcons.OWL_ENUMERATED_CLASS;
    }


    public Collection getOneOf() {
        RDFList list = (RDFList) getPropertyValue(getOWLModel().getOWLOneOfProperty());
        if (list == null) {
            return Collections.EMPTY_LIST;
        }
        else {
            return list.getValues();
        }
    }


    public Collection getOneOfValues() {
        return getOneOf();
    }


    public Iterator listOneOf() {
        return getOneOf().iterator();
    }


    public String getNestedBrowserText() {
        return getBrowserText();
    }


    public void getNestedNamedClasses(Set set) {
        for (Iterator it = getOneOf().iterator(); it.hasNext();) {
            Object o = it.next();
            if (o instanceof RDFSClass) {
                ((RDFSClass) o).getNestedNamedClasses(set);
            }
        }
    }


    public void removeOneOf(RDFResource resource) {
        DefaultRDFList.removeListValue(this, getOWLModel().getOWLOneOfProperty(), resource);
    }


    public void setOneOf(Collection resources) {
        // Stupid implementation -> too tired to think right now
        Collection oldValues = getOneOf();
        for (Iterator it = oldValues.iterator(); it.hasNext();) {
            RDFResource resource = (RDFResource) it.next();
            removeOneOf(resource);
        }
        for (Iterator it = resources.iterator(); it.hasNext();) {
            RDFResource resource = (RDFResource) it.next();
            addOneOf(resource);
        }
    }


    public void setOneOfValues(Collection values) {
        setOneOf(values);
    }
}
