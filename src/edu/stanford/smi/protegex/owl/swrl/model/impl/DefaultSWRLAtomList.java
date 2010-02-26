package edu.stanford.smi.protegex.owl.swrl.model.impl;

import java.util.Iterator;
import java.util.Set;

import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFList;
import edu.stanford.smi.protegex.owl.model.RDFObject;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.impl.DefaultRDFList;
import edu.stanford.smi.protegex.owl.model.visitor.OWLModelVisitor;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLAtomList;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLBuiltin;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLBuiltinAtom;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLIndividual;
import edu.stanford.smi.protegex.owl.swrl.parser.SWRLParser;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.SQWRLNames;

public class DefaultSWRLAtomList extends DefaultRDFList implements SWRLAtomList 
{
	private boolean isInHead = false;
	
  public DefaultSWRLAtomList(KnowledgeBase kb, FrameID id) {
    super(kb, id);
  } // DefaultSWRLAtomList

  public DefaultSWRLAtomList() {}

  public void setInHead(boolean isInHead) { this.isInHead = isInHead; }
  
  public String getBrowserText() 
  {
    String s = "";
    boolean atomProcessed = false;
    boolean setBuildEncountered = false;
    boolean setOperationEncountered = false;

    if (getValues() != null) {
      Iterator iterator = getValues().iterator();
      while (iterator.hasNext()) {
    	  Instance instance = (Instance)iterator.next();
        if (instance instanceof SWRLBuiltinAtom) {
        	SWRLBuiltin builtIn = ((SWRLBuiltinAtom)instance).getBuiltin();
        	if (builtIn == null) {
        		if (atomProcessed) s += "  " + SWRLParser.AND_CHAR + "  ";
            s += SWRLUtil.getSWRLBrowserText((RDFObject)instance, "BUILTIN ATOM");
        	} else {
        		String builtInName = builtIn.getName();
        		if (!isInHead && (SQWRLNames.isSQWRLCollectionMakeBuiltIn(builtInName) || SQWRLNames.isSQWRLCollectionGroupBuiltIn(builtInName)) && !setBuildEncountered) {
        			setBuildEncountered = true;
        			s += "  " + SWRLParser.RING_CHAR + "  " + SWRLUtil.getSWRLBrowserText((RDFObject)instance, "ATOM");
        		} else if (!isInHead && SQWRLNames.getCollectionOperationBuiltInNames().contains(builtInName) && !setOperationEncountered && atomProcessed) {
        			setOperationEncountered = true;
        			s += "  " + SWRLParser.RING_CHAR + "  " + SWRLUtil.getSWRLBrowserText((RDFObject)instance, "ATOM");
        		} else {
        			if(atomProcessed) s += "  " + SWRLParser.AND_CHAR + "  ";
        			s += SWRLUtil.getSWRLBrowserText((RDFObject)instance, "BUILTIN ATOM");
        		} // if
        	} // if
        } else {
        	if (atomProcessed) s += "  " + SWRLParser.AND_CHAR + "  ";
            s += SWRLUtil.getSWRLBrowserText((RDFObject)instance, "ATOM");
        } // if
        atomProcessed = true;
      } // while
  } else s += "<DELETED_ATOM_LIST>";
    
    return s;
  } // getBrowserText

  public void getReferencedInstances(Set<RDFResource> set) {
    final OWLModel owlModel = getOWLModel();
    RDFList li = this;
    while (li != null && !li.equals(owlModel.getRDFNil())) {
      set.add(li);
      Object value = li.getFirst();
      if (value instanceof SWRLIndividual) {
        SWRLIndividual individual = (SWRLIndividual) value;
        set.add(individual);
        individual.getReferencedInstances(set);
      }
      li = li.getRest();
    }
  }
  
  @Override
  public void accept(OWLModelVisitor visitor) {
	 visitor.visitSWRLAtomListIndividual(this);
  }
  
} // DefaultSWRLAtomList

