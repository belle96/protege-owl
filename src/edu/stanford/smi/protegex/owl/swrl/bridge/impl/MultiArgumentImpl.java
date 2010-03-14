
package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.MultiArgument;

/**
 * A class used to bind multiple arguments to a built-in argument
 */
public class MultiArgumentImpl extends BuiltInArgumentImpl implements MultiArgument
{
  private List<BuiltInArgument> arguments;

  public MultiArgumentImpl()
  {
    super();
    arguments = new ArrayList<BuiltInArgument>();
  }

  public MultiArgumentImpl(List<BuiltInArgument> arguments)
  {
    super();
    this.arguments = arguments;
  }

  public void addArgument(BuiltInArgument argument) { arguments.add(argument); }
  public void setArguments(List<BuiltInArgument> arguments) { this.arguments = arguments; }
  public List<BuiltInArgument> getArguments() { return arguments; }
  public int getNumberOfArguments() { return arguments.size(); }
  public boolean hasNoArguments() { return arguments.size() == 0; }
  
  public void setVariableName(String variableName)
  {
  	for (BuiltInArgument argument : arguments) argument.setVariableName(variableName); 
  }
  
  public int compareTo(BuiltInArgument argument)
  {
  	return getVariableName().compareTo(argument.getVariableName());
  }     
}
