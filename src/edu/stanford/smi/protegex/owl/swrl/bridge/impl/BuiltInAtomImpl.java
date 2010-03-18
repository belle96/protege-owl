
package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInAtom;
import edu.stanford.smi.protegex.owl.swrl.bridge.DataValueArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.BuiltInException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.DataValue;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.SQWRLNames;

/**
 * Class representing a SWRL built-in atom
 */
public class BuiltInAtomImpl extends AtomImpl implements BuiltInAtom
{
  private String builtInURI, builtInPrefixedName;
  private List<BuiltInArgument> arguments; 
  private int builtInIndex = -1; // Index of this built-in atom in rule body; left-to-right, first built-in index is 0, second in 1, and so on
  private boolean sqwrlCollectionResultsUsed = false;
  private Set<String> dependsOnVariableNames = new HashSet<String>();
  
  public BuiltInAtomImpl(String builtInURI, String builtInPrefixedName, List<BuiltInArgument> arguments)
  {
    this.builtInURI = builtInURI;
    this.builtInPrefixedName = builtInPrefixedName;
    this.arguments = arguments;
  }

  public BuiltInAtomImpl(String builtInURI, String builtInPrefixedName)
  {
    this.builtInURI = builtInURI;
    this.builtInPrefixedName = builtInPrefixedName;
    this.arguments = new ArrayList<BuiltInArgument>();
  }

  public void setBuiltInArguments(List<BuiltInArgument> arguments) { this.arguments = arguments; }

  public String getBuiltInURI() { return builtInURI; }  
  public String getBuiltInPrefixedName() { return builtInPrefixedName; }  

  public List<BuiltInArgument> getArguments() { return arguments; }
  public int getNumberOfArguments() { return arguments.size(); }
  public int getBuiltInIndex() { return builtInIndex; }
  public void setBuiltInIndex(int builtInIndex) { this.builtInIndex = builtInIndex; }
  public Set<String> getDependsOnVariableNames() { return dependsOnVariableNames; }

  public boolean usesSQWRLCollectionResults() { return sqwrlCollectionResultsUsed; } 
  public boolean isSQWRLBuiltIn() { return SQWRLNames.isSQWRLBuiltIn(builtInURI); }
  public boolean isSQWRLMakeCollection() { return SQWRLNames.isSQWRLCollectionMakeBuiltIn(builtInURI); }
  public boolean isSQWRLGroupCollection() { return SQWRLNames.isSQWRLCollectionGroupByBuiltIn(builtInURI); }
  public boolean isSQWRLCollectionOperation() { return SQWRLNames.isSQWRLCollectionOperationBuiltIn(builtInURI); }
  public boolean isSQWRLCollectionCreateOperation() { return SQWRLNames.isSQWRLCollectionCreateOperationBuiltIn(builtInURI); }

  public void setUsesSQWRLCollectionResults() { sqwrlCollectionResultsUsed = true; }
  
  public boolean usesAtLeastOneVariableOf(Set<String> variableNames) throws BuiltInException
  { 
    Set<String> s = new HashSet<String>(variableNames);

    s.retainAll(getArgumentsVariableNames());

    return !s.isEmpty();
  }

  public boolean isArgumentAVariable(int argumentNumber) throws BuiltInException
  {
    checkArgumentNumber(argumentNumber);

    return arguments.get(argumentNumber).isVariable();
  }

  public boolean isArgumentUnbound(int argumentNumber) throws BuiltInException
  {
    checkArgumentNumber(argumentNumber);

    return arguments.get(argumentNumber).isUnbound();
  }

  public boolean hasUnboundArguments() 
  {
    for (BuiltInArgument argument: arguments) if (argument.isUnbound()) return true;
    return false;
  }

  public Set<String> getUnboundArgumentVariableNames() throws BuiltInException
  {  
    Set<String> result = new HashSet<String>();

    for (BuiltInArgument argument : arguments) if (argument.isUnbound()) result.add(argument.getVariableName());

    return result;
  }

  public String getArgumentVariableName(int argumentNumber) throws BuiltInException
  {
    checkArgumentNumber(argumentNumber);

    if (!arguments.get(argumentNumber).isVariable())
      throw new BuiltInException("expecting a variable for (0-offset) argument #" + argumentNumber);
    
    return arguments.get(argumentNumber).getVariableName();
  } 

  public List<String> getArgumentsVariableNames() throws BuiltInException
  {
    List<String> result = new ArrayList<String>();

    for (BuiltInArgument argument : arguments) if (argument.isVariable()) result.add(argument.getVariableName());

    return result;
  } 

  public List<String> getArgumentsVariableNamesExceptFirst() throws BuiltInException
  {
    List<String> result = new ArrayList<String>();
    int argumentCount = 0;

    for (BuiltInArgument argument : arguments) if (argument.isVariable() && argumentCount++ != 0) result.add(argument.getVariableName());

    return result;
  }

  public void addArguments(List<BuiltInArgument> additionalArguments) 
  { 
    arguments.addAll(additionalArguments); 
  }

  public void setDependsOnVariableNames(Set<String> variableNames)
  {
	  dependsOnVariableNames = variableNames;
  }
  
  private void checkArgumentNumber(int argumentNumber) throws BuiltInException
  {
    if (argumentNumber < 0 || argumentNumber > arguments.size()) throw new BuiltInException("invalid (0-offset) argument #" + argumentNumber);
  }  

  public String toString() 
  {
    String result = builtInPrefixedName + "(";
    boolean isFirst = true;

    for (BuiltInArgument argument : getArguments()) {
      if (!isFirst) result += ", ";
      if (argument instanceof DataValueArgument) {
      	DataValueArgument dataValueArgument = (DataValueArgument)argument;
      	DataValue dataValue = dataValueArgument.getDataValue();
        if (dataValue.isString()) result += "\"" + dataValue + "\"";
        else result += "" + dataValue;
      } else result += "" + argument;
      isFirst = false;
    } // for

    result += ")";

    return result;
  }
}
