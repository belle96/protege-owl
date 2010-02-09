
package edu.stanford.smi.protegex.owl.swrl.model.impl;

import java.util.Set;

import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.impl.DefaultOWLIndividual;
import edu.stanford.smi.protegex.owl.model.visitor.OWLModelVisitor;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLIndividual;

public abstract class AbstractSWRLIndividual extends DefaultOWLIndividual implements SWRLIndividual
{
  public AbstractSWRLIndividual(KnowledgeBase kb, FrameID id) {
    super(kb, id);
  } // DefaultSWRLSameIndividualAtom

  public AbstractSWRLIndividual() {}

  public abstract void getReferencedInstances(Set<RDFResource> set);
  
  public void accept(OWLModelVisitor visitor) {
      visitor.visitSWRLIndividual(this);
  }
}
