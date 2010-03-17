
package edu.stanford.smi.protegex.owl.swrl.bridge;

import java.util.List;
import java.util.Set;

import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.BuiltInException;

/**
 * Interface representing a SWRL built-in atom
 */
public interface BuiltInAtom  extends Atom
{
  String getBuiltInURI();
  String getBuiltInPrefixedName();
  List<BuiltInArgument> getArguments();
  int getNumberOfArguments();
  int getBuiltInIndex();
  void setBuiltInIndex(int builtInIndex);

  boolean usesAtLeastOneVariableOf(Set<String> variableNames) throws BuiltInException;
  boolean isArgumentAVariable(int argumentNumber) throws BuiltInException;
  boolean isArgumentUnbound(int argumentNumber) throws BuiltInException;
  boolean hasUnboundArguments();
  Set<String> getUnboundArgumentVariableNames() throws BuiltInException;
  String getArgumentVariableName(int argumentNumber) throws BuiltInException;
  List<String> getArgumentsVariableNames() throws BuiltInException;
  List<String> getArgumentsVariableNamesExceptFirst() throws BuiltInException;
  Set<String> getDependsOnVariableNames(); // Indicates variables that this built-in depends on (directly  or indirectly)
  void addArguments(List<BuiltInArgument> additionalArguments);

  boolean usesSQWRLCollectionResults();
  boolean isSQWRLBuiltIn();
  boolean isSQWRLMakeCollection();
  boolean isSQWRLGroupCollection();
  boolean isSQWRLCollectionOperation();
  boolean isSQWRLCollectionCreateOperation();
  
  void setUsesSQWRLCollectionResults();
  void setDependsOnVariableNames(Set<String> variableNames);
}

