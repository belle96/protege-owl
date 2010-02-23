
package edu.stanford.smi.protegex.owl.swrl.bridge.sqwrl.impl;

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

  public MultiArgumentImpl(String variableName)
  {
    super(variableName);
    arguments = new ArrayList<BuiltInArgument>();
  } // MultiArgumentImpl

  public MultiArgumentImpl(String variableName, List<BuiltInArgument> arguments)
  {
    super(variableName);
    this.arguments = arguments;
  } // MultiArgumentImpl

  public void addArgument(BuiltInArgument argument) 
  { 
	  argument.setVariableName(getVariableName());
	  arguments.add(argument);
  } // addArguments
  
  public void setArguments(List<BuiltInArgument> arguments) 
  { 
	  for (BuiltInArgument argument : arguments) argument.setVariableName(getVariableName());
	  this.arguments = arguments; 
  } // setArguments
  
  public List<BuiltInArgument> getArguments() { return arguments; }
  public int getNumberOfArguments() { return arguments.size(); }
  public boolean hasNoArguments() { return arguments.size() == 0; }
} // MultiArgumentImpl
