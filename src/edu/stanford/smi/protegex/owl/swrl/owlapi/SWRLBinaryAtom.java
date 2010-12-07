
package edu.stanford.smi.protegex.owl.swrl.owlapi;

public interface SWRLBinaryAtom extends SWRLAtom {
  SWRLArgument getFirstArgument();
  SWRLArgument getSecondArgument();
}
