
package edu.stanford.smi.protegex.owl.swrl.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.stanford.smi.protegex.owl.model.OWLDataRange;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.RDFList;
import edu.stanford.smi.protegex.owl.model.RDFObject;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.swrl.SWRLSystemFrames;
import edu.stanford.smi.protegex.owl.swrl.exceptions.SWRLFactoryException;
import edu.stanford.smi.protegex.owl.swrl.parser.SWRLParseException;
import edu.stanford.smi.protegex.owl.swrl.parser.SWRLParser;

/**
 * A utility class that can (and should) be used to create and access SWRL related objects in an ontology.
 *
 * See <a href="http://protege.cim3.net/cgi-bin/wiki.pl?SWRLFactoryFAQ">here</a> for documentation on using this class.
 */
public class SWRLFactory 
{
  private OWLModel owlModel;
  private SWRLSystemFrames systemFrames;

  public SWRLFactory(OWLModel owlModel) 
  {
    this.owlModel = owlModel;
    systemFrames = owlModel.getSystemFrames();
  } // SWRLFactory
  

  public SWRLImp createImp() 
  {
    String name = getNewImpName();
    return (SWRLImp) systemFrames.getImpCls().createInstance(name);
  }

  public SWRLImp createImpWithGivenName(String name)
  {
    RDFSClass impCls = owlModel.getRDFSNamedClass(SWRLNames.Cls.IMP);
    return (SWRLImp)impCls.createInstance(name);
  }
  
  public SWRLImp createImp(String expression) throws SWRLParseException 
  {
    SWRLParser parser = new SWRLParser(owlModel);
    parser.setParseOnly(false);
    return parser.parse(expression);
  }

  public SWRLImp createImp(String name, String expression) throws SWRLParseException {
    SWRLParser parser = new SWRLParser(owlModel);
    SWRLImp imp = createImpWithGivenName(name);
    parser.setParseOnly(false);
    return parser.parse(expression, imp);
  } // createImp

  public SWRLImp createImp(SWRLAtom headAtom, List<SWRLAtom> bodyAtoms) {
    SWRLAtomList head = createAtomList(Collections.singleton(headAtom));
    SWRLAtomList body = createAtomList(bodyAtoms);
    return createImp(head, body);
  }
  
  public SWRLImp createImp(SWRLAtomList head, SWRLAtomList body) 
  {
    SWRLImp swrlImp = createImp();
    swrlImp.setHead(head);
    swrlImp.setBody(body);
    head.setInHead(true);
    head.setInHead(false);
    return swrlImp;
  } // SWRLImp
 
  public SWRLAtomList createAtomList() 
  {
    return (SWRLAtomList) systemFrames.getAtomListCls().createAnonymousInstance();
  } // createAtomList

  public SWRLAtomList createAtomList(Collection<SWRLAtom> atoms) 
  {
    SWRLAtomList list = createAtomList();
    for (Iterator<SWRLAtom> it = atoms.iterator(); it.hasNext();) {
      Object o = it.next();
      list.append(o);
    }
    return list;
  }

  public SWRLBuiltinAtom createBuiltinAtom(SWRLBuiltin swrlBuiltin, Iterator<RDFObject> arguments) 
  {
    RDFList li = owlModel.createRDFList(arguments);
    return createBuiltinAtom(swrlBuiltin, li);    
  } // createBuiltinAtom

  public SWRLBuiltinAtom createBuiltinAtom(SWRLBuiltin swrlBuiltin, RDFList arguments) 
  {
    SWRLBuiltinAtom swrlBuiltinAtom;
    
    swrlBuiltinAtom = (SWRLBuiltinAtom) systemFrames.getBuiltinAtomCls().createAnonymousInstance();
    
    swrlBuiltinAtom.setBuiltin(swrlBuiltin);
    swrlBuiltinAtom.setArguments(arguments);
    
    return swrlBuiltinAtom;
  } // createBuiltinAtom

  public SWRLClassAtom createClassAtom(RDFSNamedClass aClass, RDFResource iObject) 
  {
    SWRLClassAtom swrlClassAtom;
    
    swrlClassAtom = (SWRLClassAtom) systemFrames.getClassAtomCls().createAnonymousInstance();
    
    swrlClassAtom.setClassPredicate(aClass);
    swrlClassAtom.setArgument1(iObject);
    
    return swrlClassAtom;
    
  } // createClassAtom

  public SWRLDataRangeAtom createDataRangeAtom(OWLDataRange dataRange, RDFObject dObject) 
  {
    SWRLDataRangeAtom swrlDataRangeAtom = (SWRLDataRangeAtom)systemFrames.getDataRangeAtomCls().createAnonymousInstance();
    
    swrlDataRangeAtom.setArgument1(dObject);
    swrlDataRangeAtom.setDataRange(dataRange);
    
    return swrlDataRangeAtom;
  } // createDataRangeAtom
  
  public SWRLDatavaluedPropertyAtom createDatavaluedPropertyAtom(OWLDatatypeProperty datatypeSlot, RDFResource iObject, RDFObject dObject) 
  {
    SWRLDatavaluedPropertyAtom swrlDatavaluedPropertyAtom 
            = (SWRLDatavaluedPropertyAtom) systemFrames.getDataValuedPropertyAtomCls().createAnonymousInstance();
    
    swrlDatavaluedPropertyAtom.setPropertyPredicate(datatypeSlot);
    swrlDatavaluedPropertyAtom.setArgument1(iObject);
    swrlDatavaluedPropertyAtom.setArgument2(dObject);

    return swrlDatavaluedPropertyAtom;
    
  } // createDatavaluedPropertyAtom
  
  public SWRLIndividualPropertyAtom createIndividualPropertyAtom(OWLObjectProperty objectSlot, RDFResource iObject1, RDFResource iObject2) 
  {
    SWRLIndividualPropertyAtom swrlIndividualPropertyAtom;
    
    swrlIndividualPropertyAtom = (SWRLIndividualPropertyAtom) systemFrames.getIndividualPropertyAtomCls().createAnonymousInstance();
    
    swrlIndividualPropertyAtom.setPropertyPredicate(objectSlot);
    swrlIndividualPropertyAtom.setArgument1(iObject1);
    swrlIndividualPropertyAtom.setArgument2(iObject2);
    
    return swrlIndividualPropertyAtom;
  } // createIndividualPropertyAtom
  
  public SWRLDifferentIndividualsAtom createDifferentIndividualsAtom(RDFResource argument1, RDFResource argument2) 
  {
    SWRLDifferentIndividualsAtom swrlDifferentIndividualsAtom;
    
    swrlDifferentIndividualsAtom = (SWRLDifferentIndividualsAtom) systemFrames.getDifferentIndividualsAtomCls().createAnonymousInstance();
    swrlDifferentIndividualsAtom.setArgument1(argument1);
    swrlDifferentIndividualsAtom.setArgument2(argument2);
    
    return swrlDifferentIndividualsAtom;
  } // createDifferentIndividualsAtom

  public SWRLSameIndividualAtom createSameIndividualAtom(RDFResource argument1, RDFResource argument2) 
  {
    SWRLSameIndividualAtom swrlSameIndividualAtom;
    
    swrlSameIndividualAtom = (SWRLSameIndividualAtom) systemFrames.getSameIndividualAtomCls().createAnonymousInstance();
    swrlSameIndividualAtom.setArgument1(argument1);
    swrlSameIndividualAtom.setArgument2(argument2);
    
    return swrlSameIndividualAtom;
  } // createSameIndividualAtom
  
  public SWRLVariable createVariable(String name) 
  {
    return (SWRLVariable) owlModel.getRDFSNamedClass(SWRLNames.Cls.VARIABLE).createInstance(name);
  } // createVariable

  public SWRLBuiltin createBuiltin(String name) 
  {
    return (SWRLBuiltin)owlModel.getRDFSNamedClass(SWRLNames.Cls.BUILTIN).createInstance(name);
  } // createBuiltin

  public SWRLBuiltin getBuiltin(String name) 
  {
    RDFResource resource = owlModel.getRDFResource(name);
    if (resource instanceof SWRLBuiltin) {
      return (SWRLBuiltin) resource;
    }
    else {
      System.err.println("[SWRLFactory]  Invalid attempt to cast " + name +
                         " into SWRLBuiltin (real type is " + resource.getProtegeType() + ")");
      return null;
    }
  } // getBuiltin

  public Collection<SWRLBuiltin> getBuiltins() 
  {
    RDFSNamedClass builtinCls = owlModel.getRDFSNamedClass(SWRLNames.Cls.BUILTIN);
    Set<SWRLBuiltin> result = new HashSet<SWRLBuiltin>();
    
    for (Object o : builtinCls.getInstances(true))
      if (o instanceof SWRLBuiltin) result.add((SWRLBuiltin)o);
    
    return result;
  }

  public Collection<SWRLImp> getImps() 
  {
    Collection<SWRLImp> imps = new HashSet<SWRLImp>();
    
    for (Object o : systemFrames.getImpCls().getInstances(true))
    	if (o instanceof SWRLImp) imps.add((SWRLImp)o);
    
    return imps;
  } // getImps

  public Collection<SWRLImp> getEnabledImps() { return getImps(new HashSet<String>(), true); }
  public Collection<SWRLImp> getEnabledImps(Set<String> ruleGroupNames) { return getImps(ruleGroupNames, true); }

  public Collection<SWRLImp> getEnabledImps(String ruleGroupName) 
  { 
    Set<String> ruleGroupNames = new HashSet<String>();
    ruleGroupNames.add(ruleGroupName);
    return getImps(ruleGroupNames, true); 
  } // getEnabledImps

  // If the ruleGroupNames is empty, return all imps.
  private Collection<SWRLImp> getImps(Set<String> ruleGroupNames, boolean isEnabled) 
  {
    Collection<SWRLImp> result = new ArrayList<SWRLImp>();
    Collection<SWRLImp> imps = getImps();

    if (imps != null) {
      for (SWRLImp imp : imps) {
        if (ruleGroupNames.isEmpty() || imp.isInRuleGroups(ruleGroupNames)) {
          if (imp.isEnabled() == isEnabled) result.add(imp);
        } // if
      } // while
    } // if
    return result;
  } // getImps

  public void deleteImps()
  {
    for (SWRLImp imp : getImps()) {
      imp.deleteImp();
    } // for
  } // deleteImps

  public void replaceImps(OWLModel sourceOWLModel) throws SWRLFactoryException
  {
    deleteImps();
    copyImps(sourceOWLModel);
  } // replaceImps

  public void copyImps(OWLModel sourceOWLModel) throws SWRLFactoryException
  {
    SWRLFactory sourceSWRLFactory = new SWRLFactory(sourceOWLModel);

    for (Object o : sourceSWRLFactory.getImps()) {
      SWRLImp imp = (SWRLImp)o;
      String ruleName = imp.getLocalName();
      String expression = imp.getBrowserText();

      if (hasImp(ruleName)) throw new SWRLFactoryException("attempt to copy rule '" + ruleName + "' that has same name as an existing rule");

      try { createImp(ruleName, expression); }
      catch (SWRLParseException e) { throw new SWRLFactoryException("error copying rule '" + ruleName + "': " + e.getMessage()); }
    } // for
  } // copyImps

  public SWRLImp getImp(String name) throws SWRLFactoryException
  {
    RDFResource resource = owlModel.getRDFResource(name);
    SWRLImp result = null;

    if (resource instanceof SWRLImp || resource == null) result = (SWRLImp) resource;
    else throw new SWRLFactoryException("invalid attempt to cast " + name + " into SWRLImp (real type is " + resource.getProtegeType() + ")");

    return result;
  } // getImp

  public boolean hasImp(String name)
  {
    RDFResource resource = owlModel.getRDFResource(name);

    return (resource != null) && (resource instanceof SWRLImp);
  } // hasImp

  public String getNewImpName() {
    String prefix = owlModel.getNamespaceManager().getDefaultNamespace();
    String base = prefix + "Rule-";
    int i = Math.max(1, systemFrames.getImpCls().getInstances(false).size());
    while (owlModel.getRDFResource(base + i) != null) {
      i++;
        }
    return base + i;
  }


    public SWRLVariable getVariable(String name) {
        return (SWRLVariable) owlModel.getRDFResource(name);
    }


    public Collection<SWRLVariable> getVariables() {
        RDFSClass variableCls = owlModel.getRDFSNamedClass(SWRLNames.Cls.VARIABLE);
        Set<SWRLVariable> result = new HashSet<SWRLVariable>();
        
        for (Object o : variableCls.getInstances(true))
          if (o instanceof SWRLVariable) result.add((SWRLVariable)o);
        
        return result;
    }

  public Collection<SWRLImp> getReferencedImps(RDFResource rdfResource)
  {
    Collection<SWRLImp> result = new ArrayList<SWRLImp>();

    if (rdfResource != null) {
      for (SWRLImp imp : getImps()) {
        Set<RDFResource> resources = imp.getReferencedInstances();
        if (resources.contains(rdfResource) && !result.contains(imp)) result.add(imp);
      } // while
    } // if
    return result;
  } // getReferencedImps

  public void enableAll() { enableStatusUpdate(new HashSet<String>(), true); }
  public void disableAll() { enableStatusUpdate(new HashSet<String>(), false); }

  public void enableAll(Set<String> ruleGroupNames) { enableStatusUpdate(ruleGroupNames, true); }
  public void disableAll(Set<String> ruleGroupNames) { enableStatusUpdate(ruleGroupNames, false); }

  public void enableAll(String ruleGroupName) 
  {
    Set<String> ruleGroupNames = new HashSet<String>();
    ruleGroupNames.add(ruleGroupName);
    enableStatusUpdate(ruleGroupNames, true); 
  } // enable

  public void disableAll(String ruleGroupName) 
  {
    Set<String> ruleGroupNames = new HashSet<String>();
    ruleGroupNames.add(ruleGroupName);
    enableStatusUpdate(ruleGroupNames, false); 
  } // enable

  public static boolean isSWRLResource(RDFResource resource)
  {
    return  (resource instanceof SWRLAtomList) || (resource instanceof SWRLBuiltinAtom) || (resource instanceof SWRLClassAtom) || 
            (resource instanceof SWRLDataRangeAtom) || (resource instanceof SWRLDatavaluedPropertyAtom) ||
            (resource instanceof SWRLDifferentIndividualsAtom) || (resource instanceof SWRLImp) || 
            (resource instanceof SWRLIndividualPropertyAtom) || (resource instanceof SWRLSameIndividualAtom) || 
            (resource instanceof SWRLBuiltin) || (resource instanceof SWRLAtom) || (resource instanceof SWRLVariable);
  } // isSWRLResource

  private void enableStatusUpdate(Set<String> ruleGroupNames, boolean enable)
  {
    for (SWRLImp imp : getImps()) {
      if (ruleGroupNames.isEmpty() || imp.isInRuleGroups(ruleGroupNames)) {
        if (enable) imp.enable(); else imp.disable();
      } // if
    } // while
  } // enableAll

  public Collection<RDFSNamedClass> getSWRLClasses() {
	  ArrayList<RDFSNamedClass> swrlClasses = new ArrayList<RDFSNamedClass>();
	  swrlClasses.add(systemFrames.getAtomListCls());		
	  swrlClasses.add(systemFrames.getBuiltinAtomCls());    
	  swrlClasses.add(systemFrames.getClassAtomCls());
	  swrlClasses.add(systemFrames.getDataRangeAtomCls());
	  swrlClasses.add(systemFrames.getDataValuedPropertyAtomCls());
	  swrlClasses.add(systemFrames.getDifferentIndividualsAtomCls());
	  swrlClasses.add(systemFrames.getImpCls());
	  swrlClasses.add(systemFrames.getIndividualPropertyAtomCls());
	  swrlClasses.add(systemFrames.getSameIndividualAtomCls());
	  swrlClasses.add(systemFrames.getBuiltInCls());
	  swrlClasses.add(systemFrames.getAtomCls());
	  swrlClasses.add(systemFrames.getVariableCls());
	  
	  return swrlClasses;
  }
  
  public Collection<RDFProperty> getSWRLProperties() {
	  ArrayList<RDFProperty> swrlProperties = new ArrayList<RDFProperty>();
	  swrlProperties.add(systemFrames.getBodyProperty());
	  swrlProperties.add(systemFrames.getHeadProperty());
	  swrlProperties.add(systemFrames.getArgumentsProperty());
	  swrlProperties.add(systemFrames.getBuiltInProperty());
	  swrlProperties.add(systemFrames.getArgument1Property());
	  swrlProperties.add(systemFrames.getArgument2Property());
	  swrlProperties.add(systemFrames.getClassPredicateProperty());
	  swrlProperties.add(systemFrames.getPropertyPredicateProperty());
	  swrlProperties.add(systemFrames.getDataRangeProperty());
	  
	  return swrlProperties;
  }
  
  public Collection<RDFProperty> getSWRLBProperties(){
	  ArrayList<RDFProperty> swrlbProperties = new ArrayList<RDFProperty>();
	  	  
	  RDFProperty swrlbArgs = owlModel.getRDFProperty(SWRLNames.Slot.ARGS);
	  if (swrlbArgs != null) {
		  swrlbProperties.add(swrlbArgs);
	  }
	  
	  RDFProperty swrlbMinArgs = owlModel.getRDFProperty(SWRLNames.Slot.MIN_ARGS);
	  if (swrlbMinArgs != null) {
		  swrlbProperties.add(swrlbMinArgs);
	  }

	  RDFProperty swrlbMaxArgs = owlModel.getRDFProperty(SWRLNames.Slot.MAX_ARGS);
	  if (swrlbMaxArgs != null) {
		  swrlbProperties.add(swrlbMaxArgs);
	  }

	  return swrlbProperties;
  }


} // SWRLFactory
