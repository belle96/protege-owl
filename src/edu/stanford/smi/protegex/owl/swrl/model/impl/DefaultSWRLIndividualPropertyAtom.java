package edu.stanford.smi.protegex.owl.swrl.model.impl;

import java.util.Set;

import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLIndividualPropertyAtom;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLNames;

public class DefaultSWRLIndividualPropertyAtom extends DefaultSWRLAtom implements SWRLIndividualPropertyAtom {

    public DefaultSWRLIndividualPropertyAtom(KnowledgeBase kb, FrameID id) {
        super(kb, id);
    } // DefaultSWRLIndividualPropertyAtom


    public DefaultSWRLIndividualPropertyAtom() {
    }


    public void getReferencedInstances(Set<RDFResource> set) {
        RDFResource argument1 = getArgument1();
        if (argument1 != null) {
            set.add(argument1);
        }
        RDFResource argument2 = getArgument2();
        if (argument2 != null) {
            set.add(argument2);
        }
        RDFResource slot = getPropertyPredicate();
        if (slot != null) {
            set.add(slot);
        }
    }


  public RDFResource getArgument1() 
  {
    return (RDFResource) getPropertyValue(getOWLModel().getRDFProperty(SWRLNames.Slot.ARGUMENT1));
  } // getArgument1


  public void setArgument1(RDFResource iObject) 
  {
    setPropertyValue(getOWLModel().getRDFProperty(SWRLNames.Slot.ARGUMENT1), iObject);
  } // setArgument1


  public RDFResource getArgument2() 
  {
    return (RDFResource) getPropertyValue(getOWLModel().getRDFProperty(SWRLNames.Slot.ARGUMENT2));
  } // getArgument2


  public void setArgument2(RDFResource iObject) 
  {
    setPropertyValue(getOWLModel().getRDFProperty(SWRLNames.Slot.ARGUMENT2), iObject);
  } // setArgument2

  public OWLObjectProperty getPropertyPredicate() 
  {
    Object propertyValue = getPropertyValue(getOWLModel().getRDFProperty(SWRLNames.Slot.PROPERTY_PREDICATE));
  
    if (propertyValue instanceof OWLObjectProperty) return (OWLObjectProperty)propertyValue;
    else return null;
  } // getPropertyPredicate

  public void setPropertyPredicate(OWLObjectProperty objectSlot) 
  {
    setPropertyValue(getOWLModel().getRDFProperty(SWRLNames.Slot.PROPERTY_PREDICATE), objectSlot);
  } // setPropertyPredicate

  public String getBrowserText() 
  {
    Object propertyPredicate = getPropertyValue(getOWLModel().getRDFProperty(SWRLNames.Slot.PROPERTY_PREDICATE));
    RDFResource argument1 = getArgument1();
    RDFResource argument2 = getArgument2();
    String s = "";

    s += SWRLUtil.getSWRLBrowserText(propertyPredicate, "PROPERTY");
    s += "(";
    s += SWRLUtil.getSWRLBrowserText(argument1, "ARGUMENT1");
    s += ", ";
    s += SWRLUtil.getSWRLBrowserText(argument2, "ARGUMENT2");
    s += ")";
    
    return s;
  } // getBrowserText

} // DefaultSWRLIndividualPropertyAtom


