package edu.stanford.smi.protegex.owl.swrl.model;

import java.util.Set;

import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.swrl.parser.SWRLParseException;

/**
 * @author Martin O'Connor  <moconnor@smi.stanford.edu>
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface SWRLImp extends SWRLIndividual 
{
  SWRLImp createClone();
  
  /**
   * Deletes this and all depending objects of the rule.
   */
  void deleteImp();
  Set<RDFResource> getReferencedInstances();
  SWRLAtomList getBody();
  void setBody(SWRLAtomList swrlAtomList);
  SWRLAtomList getHead();
  void setHead(SWRLAtomList swrlAtomList);
  
  /**
   * Tries to parse the given text to create head and body
   * of this Imp.  This will replace the old content.
   * This method can be used to implement editing of existing
   * rules without deleting them.
   *
   * @param parsableText a SWRL expression
     */
  void setExpression(String parsableText) throws SWRLParseException;
  
  // Annotation-driven methods
  boolean isEnabled();
  void enable();
  void disable();

  Set<String> getRuleGroupNames();
  boolean addRuleGroup(String name); // Return true on successful addition
  boolean removeRuleGroup(String name); // Return true on successful deletion
  boolean isInRuleGroups(Set<String> names);
  boolean isInRuleGroup(String name);
  void enable(String ruleGroupName);
  void enable(Set<String> ruleGroupNames);
  void disable(String ruleGroupName);
  void disable(Set<String> ruleGroupNames);
} 

