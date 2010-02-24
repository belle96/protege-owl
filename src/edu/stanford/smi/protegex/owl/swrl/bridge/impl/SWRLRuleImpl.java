
package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.stanford.smi.protegex.owl.swrl.bridge.Atom;
import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInAtom;
import edu.stanford.smi.protegex.owl.swrl.bridge.ClassAtom;
import edu.stanford.smi.protegex.owl.swrl.bridge.DataValueArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.SWRLRule;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.BuiltInException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.DataValueConversionException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.SQWRLNames;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.SQWRLException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.impl.ResultImpl;

/**
 * Class implementing a SWRL rule or SQWRL query. TODO: separate out SQWRL functionality.
 */
public class SWRLRuleImpl implements SWRLRule
{
  private String ruleURI;
  private String ruleGroupName;
  private List<Atom> bodyAtoms, headAtoms;
  private Set<String> referencedVariableNames;
  private ResultImpl sqwrlResult = null;
  private boolean hasSQWRLBuiltIns, hasSQWRLCollectionBuiltIns;
  private boolean enabled = true;
  
  public SWRLRuleImpl(String ruleURI, List<Atom> bodyAtoms, List<Atom> headAtoms) throws SQWRLException, BuiltInException
  {
    this.ruleURI = ruleURI;
    this.bodyAtoms = bodyAtoms;
    this.headAtoms = headAtoms;
    hasSQWRLBuiltIns = false;
    hasSQWRLCollectionBuiltIns = false;
    ruleGroupName = "";
    buildReferencedVariableNames();
    processUnboundArguments();
    processSQWRLBuiltIns();
  } // SWRLRuleImpl
  
  public String getURI() { return ruleURI; }
  public void setURI(String ruleURI) { this.ruleURI = ruleURI; }
  public void setRuleText(String text) {}
  public List<Atom> getHeadAtoms() { return headAtoms; }
  public List<Atom> getBodyAtoms() { return bodyAtoms; }
  public boolean isSQWRL() { return hasSQWRLBuiltIns || hasSQWRLCollectionBuiltIns; }
  public boolean usesSQWRLCollections() { return hasSQWRLCollectionBuiltIns; }
  public String getRuleGroupName() { return ruleGroupName; }
  public void setRuleGroupName(String ruleGroupName) { this.ruleGroupName = ruleGroupName; }
  public boolean isEnabled() { return enabled; }
  public void setEnabled(Boolean enable) { this.enabled = enable; }
  public String getRuleText() { return toString(); }
  
  public List<BuiltInAtom> getBuiltInAtomsFromHead() { return getBuiltInAtoms(headAtoms); }
  public List<BuiltInAtom> getBuiltInAtomsFromHead(Set<String> builtInNames) { return getBuiltInAtoms(headAtoms, builtInNames); }

  public List<BuiltInAtom> getBuiltInAtomsFromBody() { return getBuiltInAtoms(bodyAtoms); }
  public List<BuiltInAtom> getBuiltInAtomsFromBody(Set<String> builtInNames) { return getBuiltInAtoms(bodyAtoms, builtInNames); }
  
  public void appendAtomsToBody(List<Atom> atoms)
  {
    bodyAtoms.addAll(atoms);
  } // appendAtomToBody

  public List<Atom> getSQWRLPhase1BodyAtoms()
  {
    List<Atom> result = new ArrayList<Atom>();

    for (Atom atom : bodyAtoms) {
      if (atom instanceof BuiltInAtom) {
    	BuiltInAtom builtInAtom = (BuiltInAtom)atom;	
    	if (builtInAtom.usesSQWRLCollectionResults() || builtInAtom.isSQWRLGroupCollection()) continue;
      } // if
      result.add(atom);
    } // for

    return result;
  } // getSQWRLPhase1BodyAtoms

  public List<Atom> getSQWRLPhase2BodyAtoms()
  {
    List<Atom> result = new ArrayList<Atom>();

    for (Atom atom : bodyAtoms) {
    	if (atom instanceof BuiltInAtom) {
    	  BuiltInAtom builtInAtom = (BuiltInAtom)atom;
    	  if (builtInAtom.isSQWRLMakeCollection() || builtInAtom.isSQWRLGroupCollection()) continue;
      } // if
      result.add(atom);
    } // for

    //System.err.println("SWRLRuleImpl.getSQWRLPhase2BodyAtoms: " + result);

    return result;
  } // getSQWRLPhase2BodyAtoms

  public ResultImpl getSQWRLResult() { return sqwrlResult; }

  /**
   * Find all built-in atoms with unbound arguments and tell them which of their arguments are unbound.
   * See <a href="http://protege.cim3.net/cgi-bin/wiki.pl?SWRLBuiltInBridge#nid88T">here</a> for a discussion of the role of this method.
   */
  private void processUnboundArguments() throws SQWRLException, BuiltInException
  {
    List<BuiltInAtom> bodyBuiltInAtoms = new ArrayList<BuiltInAtom>();
    List<Atom> bodyNonBuiltInAtoms = new ArrayList<Atom>();
    Set<String> variableNamesUsedByNonBuiltInBodyAtoms = new HashSet<String>(); // By definition, these will always be bound.
    Set<String> boundBuiltInVariableNames = new HashSet<String>(); // Names of variables bound by built-ins in this rule.
 
    // Process the body atoms and build up list of (1) built-in body atoms, and (2) the variables used by non-built body in atoms.
    for (Atom atom : getBodyAtoms()) {
      if (atom instanceof BuiltInAtom) bodyBuiltInAtoms.add((BuiltInAtom)atom);
      else {
        bodyNonBuiltInAtoms.add(atom); variableNamesUsedByNonBuiltInBodyAtoms.addAll(atom.getReferencedVariableURIs());
      } // if
    } // for

    // Process the body built-in atoms and determine if they bind any of their arguments.
    for (BuiltInAtom builtInAtom : bodyBuiltInAtoms) { // Read through built-in arguments and determine which are unbound.
    	Set<String> dependsOnVariableNames = new HashSet<String>(variableNamesUsedByNonBuiltInBodyAtoms); // TODO: Need to work this out properly.
    	dependsOnVariableNames.removeAll(builtInAtom.getArgumentsVariableNames());
      
    	builtInAtom.setDependsOnVariableNames(dependsOnVariableNames);
    	
    	for (BuiltInArgument argument : builtInAtom.getArguments()) {
        if (argument.isVariable()) {
          String argumentVariableName = argument.getVariableName();

          // If a variable argument is not used by any non built-in body atom or is not bound by another body built-in atom it will therefore be
          // unbound when this built-in is called. We thus set this built-in argument to unbound. If a built-in binds an argument, all later
          // built-ins (proceeding from left to right) will be passed the bound value of this variable during rule execution.
          if (!variableNamesUsedByNonBuiltInBodyAtoms.contains(argumentVariableName) &&
              !boundBuiltInVariableNames.contains(argumentVariableName)) {
            argument.setUnbound(); // Tell the built-in that it is expected to bind this argument.
            boundBuiltInVariableNames.add(argumentVariableName); // Flag this as a bound variable for later built-ins.
          } // if
        } // if
      } // for
    } // for

    // If we have built-in atoms, construct a new body with built-in atoms moved to the end of the list. Some rule engines (e.g., Jess)
    // expect variables used as parameters to functions to have been defined before their use in a left to right fashion.
    bodyAtoms = processBodyNonBuiltInAtoms(bodyNonBuiltInAtoms);
    bodyAtoms.addAll(bodyBuiltInAtoms);
  } // processUnboundArguments

  /**
   * Build up a list of body class atoms and non class, non built-in atoms. 
   */
  private List<Atom> processBodyNonBuiltInAtoms(List<Atom> bodyNonBuiltInAtoms)
  {
    List<Atom> bodyClassAtoms = new ArrayList<Atom>(); 
    List<Atom> bodyNonClassNonBuiltInAtoms = new ArrayList<Atom>();
    List<Atom> result = new ArrayList<Atom>();

    for (Atom atom : bodyNonBuiltInAtoms) {
      if (atom instanceof ClassAtom) bodyClassAtoms.add(atom);
      else bodyNonClassNonBuiltInAtoms.add(atom);
    } // for
    
    result.addAll(bodyNonClassNonBuiltInAtoms);
    result.addAll(bodyClassAtoms);

    return result;
  } // processBodyNonBuiltInAtoms

  private List<BuiltInAtom> getBuiltInAtoms(List<Atom> atoms, Set<String> builtInNames) 
  {
    List<BuiltInAtom> result = new ArrayList<BuiltInAtom>();
    
    for (Atom atom : atoms) {
      if (atom instanceof BuiltInAtom) {
        BuiltInAtom builtInAtom = (BuiltInAtom)atom;
        if (builtInNames.contains(builtInAtom.getBuiltInURI())) result.add(builtInAtom);
        } // if
    } // for
    return result;
  } // getBuiltInAtoms

  private List<BuiltInAtom> getBuiltInAtoms(List<Atom> atoms) 
  {
    List<BuiltInAtom> result = new ArrayList<BuiltInAtom>();
    
    for (Atom atom : atoms) if (atom instanceof BuiltInAtom) result.add((BuiltInAtom)atom);

    return result;
  } // getBuiltInAtoms

  private void buildReferencedVariableNames()
  {
    referencedVariableNames = new HashSet<String>();
    
    for (Atom atom : getBodyAtoms()) referencedVariableNames.addAll(atom.getReferencedVariableURIs());
  } // buildReferencedVariableNames

  private void processSQWRLBuiltIns() throws DataValueConversionException, SQWRLException, BuiltInException
  {
    Map<String, List<BuiltInArgument>> setGroupArgumentsMap = new HashMap<String, List<BuiltInArgument>>(); 
    Set<String> setNames = new HashSet<String>();

    sqwrlResult = new ResultImpl();
    
    preprocessSQWRLHeadBuiltIns();
    preprocessSQWRLCollectionMakeBuiltIns(setNames, setGroupArgumentsMap);
    preprocessSQWRLCollectionOperationBuiltIns(setNames, setGroupArgumentsMap);
    
    sqwrlResult.configured();
    sqwrlResult.openRow();
    
    if (hasSQWRLCollectionBuiltIns) sqwrlResult.setIsDistinct(); 
  } // configureResult

  /**
   * Give each built-in a unique index proceeding from left to right.
   */
  private void preprocessBuiltInIndexes()
  {
    int builtInIndex = 0;

    for (BuiltInAtom builtInAtom : getBuiltInAtomsFromBody()) builtInAtom.setBuiltInIndex(builtInIndex++);
    for (BuiltInAtom builtInAtom : getBuiltInAtomsFromHead()) builtInAtom.setBuiltInIndex(builtInIndex++);
  } // preprocessBuiltInIndexes

  private void preprocessSQWRLHeadBuiltIns() throws DataValueConversionException, SQWRLException, BuiltInException
  {
     List<String> selectedVariableNames = new ArrayList<String>();

     preprocessBuiltInIndexes();

     for (BuiltInAtom builtInAtom : getBuiltInAtomsFromHead(SQWRLNames.getHeadBuiltInNames())) {
       String builtInName = builtInAtom.getBuiltInURI();
       hasSQWRLBuiltIns = true;
       
       for (BuiltInArgument argument : builtInAtom.getArguments()) {
         boolean isArgumentAVariable = argument.isVariable();
         String variableName = null, columnName;
         int argumentIndex = 0, columnIndex;

         if (isArgumentAVariable) {
           variableName = argument.getVariableName(); selectedVariableNames.add(variableName);
         } // if
         
         if (builtInName.equalsIgnoreCase(SQWRLNames.Select)) {
           if (isArgumentAVariable) columnName = "?" + variableName;
           else columnName = "[" + argument + "]";
           sqwrlResult.addColumn(columnName);
         } else if (builtInName.equalsIgnoreCase(SQWRLNames.SelectDistinct)) {
           if (isArgumentAVariable) columnName = "?" + variableName; else columnName = "[" + argument + "]";
           sqwrlResult.addColumn(columnName); sqwrlResult.setIsDistinct();
         } else if (builtInName.equalsIgnoreCase(SQWRLNames.Count)) {
           if (isArgumentAVariable) columnName = "count(?" + variableName + ")"; else columnName = "[" + argument + "]";
           sqwrlResult.addAggregateColumn(columnName, SQWRLNames.CountAggregateFunction);
         } else if (builtInName.equalsIgnoreCase(SQWRLNames.CountDistinct)) {
           if (isArgumentAVariable) columnName = "countDistinct(?" + variableName + ")"; else columnName = "[" + argument + "]";
           sqwrlResult.addAggregateColumn(columnName, SQWRLNames.CountDistinctAggregateFunction);
         } else if (builtInName.equalsIgnoreCase(SQWRLNames.Min)) {
           if (isArgumentAVariable) columnName = "min(?" + variableName + ")"; else columnName = "min[" + argument + "]";
           sqwrlResult.addAggregateColumn(columnName, SQWRLNames.MinAggregateFunction);
         } else if (builtInName.equalsIgnoreCase(SQWRLNames.Max)) {
           if (isArgumentAVariable) columnName = "max(?" + variableName + ")"; else columnName = "max[" + argument + "]";
           sqwrlResult.addAggregateColumn(columnName, SQWRLNames.MaxAggregateFunction);
         } else if (builtInName.equalsIgnoreCase(SQWRLNames.Sum)) {
           if (isArgumentAVariable) columnName = "sum(?" + variableName + ")"; else columnName = "sum[" + argument + "]";
           sqwrlResult.addAggregateColumn(columnName, SQWRLNames.SumAggregateFunction);
         } else if (builtInName.equalsIgnoreCase(SQWRLNames.Median)) {
           if (isArgumentAVariable) columnName = "median(?" + variableName + ")"; else columnName = "median[" + argument + "]";
           sqwrlResult.addAggregateColumn(columnName, SQWRLNames.MedianAggregateFunction);
         } else if (builtInName.equalsIgnoreCase(SQWRLNames.Avg)) {
           if (isArgumentAVariable) columnName = "avg(?" + variableName + ")"; else columnName = "avg[" + argument + "]";
           sqwrlResult.addAggregateColumn(columnName, SQWRLNames.AvgAggregateFunction);
         } else if (builtInName.equalsIgnoreCase(SQWRLNames.OrderBy)) {
           if (!isArgumentAVariable) throw new SQWRLException("only variables allowed for ordered columns - found '" + argument + "'");
           columnIndex = selectedVariableNames.indexOf(variableName);
           if (columnIndex != -1) sqwrlResult.addOrderByColumn(columnIndex, true);
           else throw new SQWRLException("variable ?" + variableName + " must be selected before it can be ordered");
         } else if (builtInName.equalsIgnoreCase(SQWRLNames.OrderByDescending)) {
           if (!isArgumentAVariable) throw new SQWRLException("only variables allowed for ordered columns - found '" + argument + "'");
           columnIndex = selectedVariableNames.indexOf(variableName);
           if (columnIndex != -1) sqwrlResult.addOrderByColumn(columnIndex, false);
           else throw new SQWRLException("variable ?" + variableName + " must be selected before it can be ordered");
         } else if (builtInName.equalsIgnoreCase(SQWRLNames.ColumnNames)) {
           if (argument instanceof DataValueArgument && ((DataValueArgument)argument).getDataValue().isString()) {
             DataValueArgument dataValueArgument = (DataValueArgument)argument;
             sqwrlResult.addColumnDisplayName(dataValueArgument.getDataValue().getString());
           } else throw new SQWRLException("only string literals allowed as column names - found: " + argument);
         } // if
         argumentIndex++;
       } // for
     } // for
  } // preprocessSQWRLHeadBuiltIns

  // We store the group arguments for each collection specified in the make operation; these arguments are later appended to the collection
  // operation built-ins
  private void preprocessSQWRLCollectionMakeBuiltIns(Set<String> collectionNames, Map<String, List<BuiltInArgument>> collectionGroupArgumentsMap) 
    throws SQWRLException, BuiltInException
  {
    for (BuiltInAtom builtInAtom : getBuiltInAtomsFromBody(SQWRLNames.getCollectionMakeAndGroupBuiltInNames())) {
      String collectionName = builtInAtom.getArgumentVariableName(0); // First argument is the collection name

      hasSQWRLCollectionBuiltIns = true;

      if (builtInAtom.isSQWRLMakeCollection()) {
    	if (builtInAtom.getNumberOfArguments() != 2) throw new SQWRLException("makeSet or makeBag must have two arguments");
        if (!collectionNames.contains(collectionName)) collectionNames.add(collectionName);
      } else if (builtInAtom.isSQWRLGroupCollection()) {
        List<BuiltInArgument> builtInArguments = builtInAtom.getArguments();
        List<BuiltInArgument> groupArguments = builtInArguments.subList(1, builtInArguments.size());

        if (builtInAtom.getNumberOfArguments() < 2) throw new SQWRLException("groupBy must have at least two arguments");
        if (!collectionNames.contains(collectionName)) throw new SQWRLException("groupBy applied to undefined collection ?" + collectionName);
        if (collectionGroupArgumentsMap.containsKey(collectionName)) throw new SQWRLException("groupBy specified more than once for same collection ?" + collectionName);
        
        collectionGroupArgumentsMap.put(collectionName, groupArguments); // Store group arguments

        //System.err.println("found group arguments " + groupArguments + " for collection " + collectionName);
      } // if          
    } // for
  } // preprocessSQWRLCollectionBuildBuiltIns

  // Append the group arguments to all collection built-ins for each of it the collection arguments; also append group arguments
  // to collections created by operation built-ins.
  private void preprocessSQWRLCollectionOperationBuiltIns(Set<String> collectionNames, Map<String, List<BuiltInArgument>> collectionGroupArgumentsMap) 
    throws SQWRLException, BuiltInException
  {
    Set<String> cascadedUnboundVariableNames = new HashSet<String>();

    for (BuiltInAtom builtInAtom : getBuiltInAtomsFromBody()) {
      String builtInName = builtInAtom.getBuiltInURI();
      //String builtInPrefixedName = builtInAtom.getBuiltInPrefixedName();
      if (builtInAtom.isSQWRLMakeCollection() || builtInAtom.isSQWRLCollectionOperation()) {
        Set<String> argumentsVariableNames = builtInAtom.getArgumentsVariableNames();

        if (SQWRLNames.isSQWRLCollectionOperationBuiltIn(builtInName)) builtInAtom.setUsesSQWRLCollectionResults();
        
        for (String variableName : argumentsVariableNames) {
          if (collectionNames.contains(variableName) && collectionGroupArgumentsMap.containsKey(variableName)) { // Variable refers to a grouped collection
            List<BuiltInArgument> collectionGroupArguments = collectionGroupArgumentsMap.get(variableName); // Get the grouping arguments
            builtInAtom.addArguments(collectionGroupArguments); // Append each collections's group arguments to built-in
            
            // System.err.println("appending group arguments " + collectionGroupArguments + " in built-in " + builtInPrefixedName + " for collection ?" + variableName);
          } // if
        } // for
        
        if (builtInAtom.hasUnboundArguments()) { // Keep track of built-in's unbound arguments so that we can mark dependent built-ins
          Set<String> unboundVariableNames = builtInAtom.getUnboundArgumentVariableNames();
          // System.err.println("adding unbound variables " + unboundVariableNames + " for built-in " + builtInPrefixedName);
          cascadedUnboundVariableNames.addAll(unboundVariableNames);
        } // if
      } else {
        // Mark later built-ins that (directly or indirectly) use variables bound by collection operation built-ins
        if (builtInAtom.usesAtLeastOneVariableOf(cascadedUnboundVariableNames)) {
          builtInAtom.setUsesSQWRLCollectionResults(); // Mark this built-in as dependent on collection built-in bindings
          // System.err.println("marking built-in " + builtInPrefixedName + " as using SQWRL collection bindings");
          if (builtInAtom.hasUnboundArguments()) { // Cascade the dependency from this built-in to others using its arguments
            cascadedUnboundVariableNames.addAll(builtInAtom.getUnboundArgumentVariableNames()); // Record its unbound variables too.
          } // if
        } // if
      } // if
    } // for
  } // preprocessSQWRLCollectionOperationBuiltIns

  public String toString()
  {
    String result = "";
    boolean isFirst = true;

    for (Atom atom : getBodyAtoms()) {
      if (!isFirst) result += " ^ ";
      result += "" + atom;
      isFirst = false;
    } // for
    
    result += " -> ";

    isFirst = true;
    for (Atom atom : getHeadAtoms()) {
      if (!isFirst) result += " ^ ";
      result += "" + atom;
      isFirst = false;
    } // for

    return result;
  } // toString

} // SWRLRuleImpl
