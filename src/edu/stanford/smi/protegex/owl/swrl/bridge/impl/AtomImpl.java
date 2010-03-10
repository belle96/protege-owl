
package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import java.util.HashSet;
import java.util.Set;

import edu.stanford.smi.protegex.owl.swrl.bridge.Atom;

/**
 * Base class representing information about atoms in a SWRL rule
 */
public class AtomImpl implements Atom
{
  private Set<String> referencedClassURIs;
  private Set<String> referencedPropertyURIs;
  private Set<String> referencedIndividualURIs;
  private Set<String> referencedVariableNames;

  public AtomImpl() 
  { 
    referencedClassURIs = new HashSet<String>();
    referencedPropertyURIs = new HashSet<String>();
    referencedIndividualURIs = new HashSet<String>();
    referencedVariableNames = new HashSet<String>();
  }

  public boolean hasReferencedClasses() { return referencedClassURIs.size() != 0; }
  public Set<String> getReferencedClassURIs() { return referencedClassURIs; }
  public boolean hasReferencedProperties() { return referencedPropertyURIs.size() != 0; }
  public Set<String> getReferencedPropertyURIs() { return referencedPropertyURIs; }
  public boolean hasReferencedIndividuals() { return referencedIndividualURIs.size() != 0; }
  public Set<String> getReferencedIndividualURIs() { return referencedIndividualURIs; }
  public boolean hasReferencedVariables() { return referencedVariableNames.size() != 0; }
  public Set<String> getReferencedVariableNames() { return referencedVariableNames; }
  
  public void addReferencedClassURI(String classURI) 
  { 
    if (!referencedClassURIs.contains(classURI)) referencedClassURIs.add(classURI); 
  }

  public void addReferencedPropertyURI(String propertyURI) 
  { 
    if (!referencedPropertyURIs.contains(propertyURI)) referencedPropertyURIs.add(propertyURI); 
  }

  public void addReferencedIndividualURI(String individualURI) 
  { 
    if (!referencedIndividualURIs.contains(individualURI)) referencedIndividualURIs.add(individualURI); 
  }
  
  public void addReferencedVariableName(String variableName) 
  { 
    if (!referencedVariableNames.contains(variableName)) referencedVariableNames.add(variableName); 
  }

}
