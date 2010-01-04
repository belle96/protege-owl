
package edu.stanford.smi.protegex.owl.swrl.model.impl;

import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLSameIndividualAtom;

public class DefaultSWRLSameIndividualAtom extends AbstractSWRLIndividualsAtom implements SWRLSameIndividualAtom 
{
  public DefaultSWRLSameIndividualAtom(KnowledgeBase kb, FrameID id) { super(kb, id); }
  public DefaultSWRLSameIndividualAtom() {}
  
  protected String getOperatorName() { return "sameAs"; }
} // DefaultSWRLSameIndivdualAtom


