
package edu.stanford.smi.protegex.owl.swrl.owlapi;

import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;

/**
 * Interface representing a variable argument to a SWRL atom or built-in
 */
public interface SWRLVariable extends SWRLArgument, BuiltInArgument
{
  String getVariableName();
}
