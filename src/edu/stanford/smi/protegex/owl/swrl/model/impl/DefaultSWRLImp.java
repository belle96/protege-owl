
package edu.stanford.smi.protegex.owl.swrl.model.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Icon;

import edu.stanford.smi.protege.model.Frame;
import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Model;
import edu.stanford.smi.protege.model.Reference;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.RDFList;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLAtomList;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLBuiltin;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLImp;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLIndividual;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLNames;
import edu.stanford.smi.protegex.owl.swrl.parser.SWRLParseException;
import edu.stanford.smi.protegex.owl.swrl.parser.SWRLParser;
import edu.stanford.smi.protegex.owl.swrl.ui.icons.SWRLIcons;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

public class DefaultSWRLImp extends AbstractSWRLIndividual implements SWRLImp 
{
  private transient Logger log = Log.getLogger(DefaultSWRLImp.class);
    
  public static final String EMPTY_RULE_TEXT = "<EMPTY_RULE>";
  
  private boolean isCacheInitialized = false;
  private boolean isRuleEnabled = true;
  private Map<String, Boolean> ruleGroups; // Contains rule groups and their enabled status.
 
  public DefaultSWRLImp(KnowledgeBase kb, FrameID id) 
  {
    super(kb, id);
  } // DefaultSWRLImp

  public DefaultSWRLImp() 
  {
  }
  
  private void checkCacheInitialized() {
      if (!isCacheInitialized) {
          isRuleEnabled = getIsRuleEnabledAnnotation();
          ruleGroups = getRuleGroupAnnotations();
          isCacheInitialized = true;
      }
  }

  public SWRLImp createClone() 
  {
    String text = getBrowserText();
    OWLModel owlModel = (OWLModel)getKnowledgeBase();
    SWRLParser parser = new SWRLParser(owlModel);
    parser.setParseOnly(false);
    try {
      return parser.parse(text);
    } catch (SWRLParseException ex) {
      log.log(Level.SEVERE, "Shouldn't Happen ", ex);
      return null;  // Should not happen
    } // try
  } // createClone

  private boolean getIsRuleEnabledAnnotation()
  {
      OWLDatatypeProperty ruleEnabledProperty = getRuleEnabledProperty();
      if (ruleEnabledProperty == null) return true;
      Object value = getPropertyValue(ruleEnabledProperty);
      if (value == null) return true;
      if (!(value instanceof Boolean)) return true; // Should not happen

      return ((Boolean)value).booleanValue();
  } // getIsRuleEnabledAnnotation

  @SuppressWarnings("unchecked")
  private Map<String, Boolean> getRuleGroupAnnotations()
  {
      OWLObjectProperty ruleGroupProperty = getRuleGroupProperty();
      OWLDatatypeProperty ruleGroupEnabledProperty = getRuleGroupEnabledProperty();
      Map<String, Boolean> result = new HashMap<String, Boolean>();

      if (ruleGroupProperty != null && ruleGroupEnabledProperty != null) {
          Collection values = getPropertyValues(ruleGroupProperty);
          if (values != null) {
              Iterator iterator = values.iterator();
              while (iterator.hasNext()) {
                  Boolean enabled = Boolean.TRUE;
                  OWLIndividual ruleGroupIndividual = (OWLIndividual)iterator.next();
                  Object isEnabled = ruleGroupIndividual.getPropertyValue(ruleGroupEnabledProperty);
                  if (isEnabled != null) enabled = (Boolean)isEnabled;
                  result.put(ruleGroupIndividual.getName(), enabled);
              } // while
          } // if
      } // if
    
    return result;
  } // getRuleGroupNamesAnnotation

  private void deleteHeadAndBody() {
    Slot directInstancesSlot = getKnowledgeBase().getSlot(Model.Slot.DIRECT_INSTANCES);
    Collection<RDFResource> instances = getReferencedInstances();
    for (RDFResource resource : instances) {
      if (resource instanceof SWRLIndividual && !(resource instanceof SWRLBuiltin)) {
        SWRLIndividual swrlIndividual = (SWRLIndividual)resource;
        if (!swrlIndividual.isDeleted()) {
          Collection<Reference> references = getKnowledgeBase().getReferences(swrlIndividual, -1);
          boolean hasExternalRef = false;
          for (Reference reference : references) {
            if (!directInstancesSlot.equals(reference.getSlot())) {
              Frame frame = reference.getFrame();
              if (!instances.contains(frame) && !equals(frame)) {
                hasExternalRef = true;
              }
            }
          }
                    if (!hasExternalRef) {
                      swrlIndividual.delete();
                    }
        }
      }
    }
  }

  public void deleteImp() {
    deleteHeadAndBody();
    delete();
  }

  public SWRLAtomList getHead() 
  { 
    Object propertyValue = getPropertyValue(getOWLModel().getRDFProperty(SWRLNames.Slot.HEAD));
    SWRLAtomList atomList = null;

    if (propertyValue instanceof SWRLAtomList) atomList = (SWRLAtomList)propertyValue;
    else if (propertyValue instanceof RDFList) { // To deal with: <swrl:head rdf:parseType="Collection">
      ((RDFList)propertyValue).setRDFType(getOWLModel().getSystemFrames().getAtomListCls());
      atomList = (SWRLAtomList)getOWLModel().getFrame(((RDFList)propertyValue).getName());
    } 

if (atomList != null) atomList.setInHead(true);
    
    return atomList;
  } // getHead

  public SWRLAtomList getBody() 
  { 
    Object propertyValue = getPropertyValue(getOWLModel().getRDFProperty(SWRLNames.Slot.BODY));
    SWRLAtomList atomList = null; 

    if (propertyValue instanceof SWRLAtomList) atomList = (SWRLAtomList)propertyValue;
    else if (propertyValue instanceof RDFList) { // To deal with: <swrl:body rdf:parseType="Collection">
      ((RDFList)propertyValue).setRDFType(getOWLModel().getSystemFrames().getAtomListCls());
      return atomList = (SWRLAtomList)getOWLModel().getFrame(((RDFList)propertyValue).getName());
    }
    
    if (atomList != null) atomList.setInHead(false);
    
    return atomList;
  } // getBody

  public void setBody(SWRLAtomList swrlAtomList) { setPropertyValue(getOWLModel().getRDFProperty(SWRLNames.Slot.BODY), swrlAtomList); } 
  public void setHead(SWRLAtomList swrlAtomList) { setPropertyValue(getOWLModel().getRDFProperty(SWRLNames.Slot.HEAD), swrlAtomList); }

  @Override
public Icon getIcon() {
    return isEditable() ? SWRLIcons.getImpIcon() :
                OWLIcons.getReadOnlyIcon(SWRLIcons.getImpIcon(), "RoundedBoxFrame");
  }

  public Set<RDFResource> getReferencedInstances() 
  {
    Set<RDFResource> set = new HashSet<RDFResource>();
    getReferencedInstances(set);
    return set;
  }

  @Override
  public void getReferencedInstances(Set<RDFResource> set) 
  {
    SWRLAtomList head = getHead();
    if (head != null) {
      set.add(head);
      head.getReferencedInstances(set);
    }
    SWRLAtomList body = getBody();
    if (body != null) {
      set.add(body);
      body.getReferencedInstances(set);
    }
  }
  
  @Override
public String getBrowserText() 
  {
    SWRLAtomList body = getBody();
    SWRLAtomList head = getHead();
    String s = "";

    if (head == null && body == null) {
      s += EMPTY_RULE_TEXT;
    } else {
      s += SWRLUtil.getSWRLBrowserText(body, "BODY");
      s += " " + SWRLParser.IMP_CHAR + " ";
      s += SWRLUtil.getSWRLBrowserText(head, "HEAD");
    } // if

    return s;
  } // toString

  public void setExpression(String parsableText) throws SWRLParseException 
  {
    SWRLParser parser = new SWRLParser(getOWLModel());
    parser.parse(parsableText);
    deleteHeadAndBody();
    parser.setParseOnly(false);
    parser.parse(parsableText, this);
  }

  public boolean isEnabled() 
  { 
      checkCacheInitialized();
      return isRuleEnabled;
  } // isEnabled

  public void enable() { enable(new HashSet<String>()); }

  public void enable(String ruleGroupName)
  {
    Set<String> ruleGroupNames = new HashSet<String>();
    ruleGroupNames.add(ruleGroupName);
    enable(ruleGroupNames);
  } // enable

  public void enable(Set<String> ruleGroupNames) 
  {
      checkCacheInitialized();
      if (ruleGroupNames.isEmpty()) {
          OWLDatatypeProperty ruleEnabledProperty = getRuleEnabledProperty();
          if (ruleEnabledProperty != null) setPropertyValue(ruleEnabledProperty, Boolean.TRUE);
          isRuleEnabled = true;
      } else {
          OWLObjectProperty ruleGroupProperty = getRuleGroupProperty();
          OWLDatatypeProperty ruleGroupEnabledProperty = getRuleGroupEnabledProperty();
          for (String ruleGroupName : ruleGroupNames) {
              OWLIndividual ruleGroupIndividual = getOWLModel().getOWLIndividual(ruleGroupName);
              if (ruleGroupIndividual != null && ruleGroupProperty != null) {
                  addPropertyValue(ruleGroupProperty, ruleGroupIndividual);
                  ruleGroupIndividual.addPropertyValue(ruleGroupEnabledProperty, Boolean.TRUE);
              }  // if
              ruleGroups.put(ruleGroupName, Boolean.TRUE);
          } // for
      } // if  
  } // enable

  public void disable() { disable(new HashSet<String>()); }

  public void disable(String ruleGroupName)
  {
    Set<String> ruleGroupNames = new HashSet<String>();
    ruleGroupNames.add(ruleGroupName);
    disable(ruleGroupNames);
  } // disable

  public void disable(Set<String> ruleGroupNames) 
  { 
      checkCacheInitialized();
      if (ruleGroupNames.isEmpty()) {
          OWLDatatypeProperty ruleEnabledProperty = getRuleEnabledProperty();
          if (ruleEnabledProperty != null) setPropertyValue(ruleEnabledProperty, Boolean.FALSE);
          isRuleEnabled = false;
      } else {
          OWLObjectProperty ruleGroupProperty = getRuleGroupProperty();
          OWLDatatypeProperty ruleGroupEnabledProperty = getRuleGroupEnabledProperty();
          for (String ruleGroupName : ruleGroupNames) {
              OWLIndividual ruleGroupIndividual = getOWLModel().getOWLIndividual(ruleGroupName);
              if (ruleGroupIndividual != null && ruleGroupProperty != null) {
                  addPropertyValue(ruleGroupProperty, ruleGroupIndividual);
                  ruleGroupIndividual.addPropertyValue(ruleGroupEnabledProperty, Boolean.FALSE);
              }  // if
              ruleGroups.put(ruleGroupName, Boolean.FALSE);
          } // for
      } // if  
  } // disable

  public Set<String> getRuleGroupNames() { 
      checkCacheInitialized();
      return ruleGroups.keySet(); 
  }
  
  public boolean isInRuleGroup(String name) { 
      checkCacheInitialized();
      return ruleGroups.containsKey(name); 
  }

  public boolean isInRuleGroups(Set<String> names) 
  { 
      checkCacheInitialized();
      for (String name : names) if (ruleGroups.containsKey(name)) return true;
      return false;
  }  // isInRuleGroups

  /**
   ** Add a rule group name to a rule. Returns true on success. The name must the name of an OWL individual of class RuleGroup defined in
   ** the ontology http://swrl.stanford.edu/ontologies/3.3/swrla.owl. Make the group enabled by default.
   */
  public boolean addRuleGroup(String name) 
  { 
      OWLNamedClass ruleGroupClass = getRuleGroupClass();
      OWLObjectProperty ruleGroupProperty = getRuleGroupProperty();
      OWLDatatypeProperty ruleGroupEnabledProperty = getRuleGroupEnabledProperty();
      
      checkCacheInitialized();
      boolean result = false;

      if (isInRuleGroup(name)) return true;

      if (ruleGroupClass != null) {
          OWLIndividual ruleGroupIndividual = getOWLModel().getOWLIndividual(name);
          if (ruleGroupIndividual != null) {
              if (ruleGroupProperty != null) {
                  addPropertyValue(ruleGroupProperty, ruleGroupIndividual);
                  ruleGroupIndividual.addPropertyValue(ruleGroupEnabledProperty, Boolean.TRUE);
                  ruleGroups.put(name, Boolean.TRUE);
                  result = true;
              } // if
          } // if
      } // if

    return result;
  } // addRuleGroupName

  /**
   ** Remove a rule group name from a rule. Returns true on success. 
   */
  public boolean removeRuleGroup(String name) 
  { 
      OWLNamedClass ruleGroupClass = getRuleGroupClass();
      OWLObjectProperty ruleGroupProperty = getRuleGroupProperty();
      checkCacheInitialized();
      boolean result = false;

      if (!isInRuleGroup(name)) return true;

      if (ruleGroupClass != null) {
          OWLIndividual individual = getOWLModel().getOWLIndividual(name);
          if (individual != null) {
              if (ruleGroupProperty != null) {
                  removePropertyValue(ruleGroupProperty, individual);
                  ruleGroups.remove(name);
                  result = true;
              } // if
          } // if
      } // if

      return result;
  } // removeRuleGroupName
  
  
  private OWLDatatypeProperty getRuleEnabledProperty() {
      return getOWLModel().getOWLDatatypeProperty(SWRLNames.Annotations.IS_RULE_ENABLED);
  }
  
  private OWLDatatypeProperty getRuleGroupEnabledProperty() {
      return getOWLModel().getOWLDatatypeProperty(SWRLNames.Annotations.IS_RULE_GROUP_ENABLED);
  }
  
  private OWLObjectProperty getRuleGroupProperty() {
      return getOWLModel().getOWLObjectProperty(SWRLNames.Annotations.HAS_RULE_GROUP);
  }
  
  private OWLNamedClass getRuleGroupClass() {
      return getOWLModel().getOWLNamedClass(SWRLNames.Annotations.RULE_GROUP);
  }

} // DefaultSWRLImp
