package edu.stanford.smi.protegex.owl.swrl.model.impl;

import java.util.Set;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLClassAtom;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLNames;

public class DefaultSWRLClassAtom extends DefaultSWRLAtom implements SWRLClassAtom {

    public DefaultSWRLClassAtom(KnowledgeBase kb, FrameID id) {
        super(kb, id);
    } // DefaultSWRLClassAtom


    public DefaultSWRLClassAtom() {
    }


    public void getReferencedInstances(Set<RDFResource> set) {
        Cls cls = getClassPredicate();
        if (cls instanceof RDFResource) {
            set.add((RDFResource)cls);
        }
        RDFResource argument1 = getArgument1();
        if (argument1 != null) {
            set.add(argument1);
        }
    }


    public RDFResource getArgument1() {
        return (RDFResource) getPropertyValue(getOWLModel().getRDFProperty(SWRLNames.Slot.ARGUMENT1));
    } // getArgument1


    public void setArgument1(RDFResource iObject) {
        setPropertyValue(getOWLModel().getRDFProperty(SWRLNames.Slot.ARGUMENT1), iObject);
    } // setArgument1


  public RDFSClass getClassPredicate() 
  {
    Object propertyValue = getPropertyValue(getOWLModel().getRDFProperty(SWRLNames.Slot.CLASS_PREDICATE));
    if (propertyValue instanceof RDFSClass) return (RDFSClass)propertyValue;
    else return null;
  } // getClassPredicate


    public void setClassPredicate(RDFSClass aClass) {
        setPropertyValue(getOWLModel().getRDFProperty(SWRLNames.Slot.CLASS_PREDICATE), aClass);
    } // setClassPredicate


  public String getBrowserText() 
  {
    Object aClass = getPropertyValue(getOWLModel().getRDFProperty(SWRLNames.Slot.CLASS_PREDICATE));
    RDFResource argument = getArgument1();

    String clsStr = SWRLUtil.getSWRLBrowserText(aClass, "CLASS");
    String argStr = SWRLUtil.getSWRLBrowserText(argument, "ARGUMENT1");

    return clsStr + "(" + argStr + ")";
  } // getBrowserText

} // DefaultSWRLClassAtom


