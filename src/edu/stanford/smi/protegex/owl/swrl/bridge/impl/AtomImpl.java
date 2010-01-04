
package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import edu.stanford.smi.protegex.owl.swrl.bridge.*;

import java.util.Set;
import java.util.HashSet;

/*
** Base class representing information about atoms in a SWRL rule
*/
public class AtomImpl implements Atom
{
  private Set<String> referencedClassNames;
  private Set<String> referencedPropertyNames;
  private Set<String> referencedIndividualNames;
  private Set<String> referencedVariableNames;

  public AtomImpl() 
  { 
    referencedClassNames = new HashSet<String>();
    referencedPropertyNames = new HashSet<String>();
    referencedIndividualNames = new HashSet<String>();
    referencedVariableNames = new HashSet<String>();
  } // AtomImpl

  public boolean hasReferencedClasses() { return referencedClassNames.size() != 0; }
  public Set<String> getReferencedClassNames() { return referencedClassNames; }
  public boolean hasReferencedProperties() { return referencedPropertyNames.size() != 0; }
  public Set<String> getReferencedPropertyNames() { return referencedPropertyNames; }
  public boolean hasReferencedIndividuals() { return referencedIndividualNames.size() != 0; }
  public Set<String> getReferencedIndividualNames() { return referencedIndividualNames; }
  public boolean hasReferencedVariables() { return referencedVariableNames.size() != 0; }
  public Set<String> getReferencedVariableNames() { return referencedVariableNames; }
  
  public void addReferencedClassName(String className) 
  { 
    if (!referencedClassNames.contains(className)) referencedClassNames.add(className); 
  } // addReferencedIndividualName

  public void addReferencedPropertyName(String propertyName) 
  { 
    if (!referencedPropertyNames.contains(propertyName)) referencedPropertyNames.add(propertyName); 
  } // addReferencedIndividualName

  public void addReferencedIndividualName(String individualName) 
  { 
    if (!referencedIndividualNames.contains(individualName)) referencedIndividualNames.add(individualName); 
  } // addReferencedIndividualName
  
  public void addReferencedVariableName(String variableName) 
  { 
    if (!referencedVariableNames.contains(variableName)) referencedVariableNames.add(variableName); 
  } // addReferencedVariableName
} // AtomImpl
