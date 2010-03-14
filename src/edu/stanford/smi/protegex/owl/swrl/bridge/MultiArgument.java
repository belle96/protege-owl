
package edu.stanford.smi.protegex.owl.swrl.bridge;

import java.util.List;

/**
 * A class used to bind multiple arguments to a built-in argument. See <a
 * href="http://protege.cim3.net/cgi-bin/wiki.pl?SWRLBuiltInBridge#nid8LH">here</a> for details.
 */
public interface MultiArgument extends BuiltInArgument
{
  void addArgument(BuiltInArgument argument);
  void setArguments(List<BuiltInArgument> arguments);
  List<BuiltInArgument> getArguments();
  int getNumberOfArguments(); 
  boolean hasNoArguments();
}
