
package edu.stanford.smi.protegex.owl.swrl.bridge.sqwrl.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInAtom;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLDataValue;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.BuiltInException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.SQWRLNames;

/**
 ** Class representing a SWRL built-in atom
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
  } // BuiltInArgument

  public BuiltInAtomImpl(String builtInURI, String builtInPrefixedName)
  {
    this.builtInURI = builtInURI;
    this.builtInPrefixedName = builtInPrefixedName;
    this.arguments = new ArrayList<BuiltInArgument>();
  } // BuiltInArgument

  public void setBuiltInArguments(List<BuiltInArgument> arguments) { this.arguments = arguments; }

  public String getBuiltInURI() { return builtInURI; }  
  public String getBuiltInPrefixedName() { return builtInPrefixedName; }  

  public List<BuiltInArgument> getArguments() { return arguments; }
  public int getNumberOfArguments() { return arguments.size(); }
  public int getBuiltInIndex() { return builtInIndex; }
  public void setBuiltInIndex(int builtInIndex) { this.builtInIndex = builtInIndex; }
  public Set<String> getDependsOnVariableNames() { return dependsOnVariableNames; }

  public boolean usesSQWRLCollectionResults() { return sqwrlCollectionResultsUsed; } 
  public boolean isSQWRLMakeCollection() { return SQWRLNames.isSQWRLCollectionMakeBuiltIn(builtInURI); }
  public boolean isSQWRLGroupCollection() { return SQWRLNames.isSQWRLCollectionGroupBuiltIn(builtInURI); }
  public boolean isSQWRLCollectionOperation() { return SQWRLNames.isSQWRLCollectionOperationBuiltIn(builtInURI); } 

  public void setUsesSQWRLCollectionResults() { sqwrlCollectionResultsUsed = true; }
  
  public boolean usesAtLeastOneVariableOf(Set<String> variableNames) throws BuiltInException
  { 
    Set<String> s = new HashSet<String>(variableNames);

    s.retainAll(getArgumentsVariableNames());

    return !s.isEmpty();
  } // usesAtLeastOneVariableOf

  public boolean isArgumentAVariable(int argumentNumber) throws BuiltInException
  {
    checkArgumentNumber(argumentNumber);

    return arguments.get(argumentNumber).isVariable();
  } // isArgumentAVariable

  public boolean isArgumentUnbound(int argumentNumber) throws BuiltInException
  {
    checkArgumentNumber(argumentNumber);

    return arguments.get(argumentNumber).isUnbound();
  } // isArgumentUnbound

  public boolean hasUnboundArguments() 
  {
    for (BuiltInArgument argument: arguments) if (argument.isUnbound()) return true;
    return false;
  } // hasUnboundArguments

  public Set<String> getUnboundArgumentVariableNames() throws BuiltInException
  {  
    Set<String> result = new HashSet<String>();

    for (BuiltInArgument argument : arguments) if (argument.isUnbound()) result.add(argument.getVariableName());

    return result;
  } // getUnboundArgumentVariableNames

  public String getArgumentVariableName(int argumentNumber) throws BuiltInException
  {
    checkArgumentNumber(argumentNumber);

    if (!arguments.get(argumentNumber).isVariable())
      throw new BuiltInException("expecting a variable for (0-offset) argument #" + argumentNumber);
    
    return arguments.get(argumentNumber).getVariableName();
  } // getArgumentVariableName

  public Set<String> getArgumentsVariableNames() throws BuiltInException
  {
    Set<String> result = new HashSet<String>();

    for (BuiltInArgument argument : arguments) if (argument.isVariable()) result.add(argument.getVariableName());

    return result;
  } // getArgumentsVariableNames

  public Set<String> getArgumentsVariableNamesExceptFirst() throws BuiltInException
  {
    Set<String> result = new HashSet<String>();
    int argumentCount = 0;

    for (BuiltInArgument argument : arguments) if (argument.isVariable() && argumentCount++ != 0) result.add(argument.getVariableName());

    return result;
  } // getArgumentsVariableNamesExceptFirst

  public void addArguments(List<BuiltInArgument> additionalArguments) 
  { 
    arguments.addAll(additionalArguments); 
  } // addArguments

  public void setDependsOnVariableNames(Set<String> variableNames)
  {
	dependsOnVariableNames = variableNames;
  } // setDependsOnVariableNames
  
  private void checkArgumentNumber(int argumentNumber) throws BuiltInException
  {
    if (argumentNumber < 0 || argumentNumber > arguments.size()) throw new BuiltInException("invalid (0-offset) argument #" + argumentNumber);
  } // checkArgumentNumber  

  public String toString() 
  {
    String result = builtInPrefixedName + "(";
    boolean isFirst = true;

    for (BuiltInArgument argument : getArguments()) {
      if (!isFirst) result += ", ";
      if (argument instanceof OWLDataValue && ((OWLDataValue)argument).isString())
        result += "\"" + argument + "\"";
      else result += "" + argument;
      isFirst = false;
    } // for

    result += ")";

    return result;
  } // toString

} // BuiltInAtomImpl
