
package edu.stanford.smi.protegex.owl.swrl.bridge.builtins.swrlx;

import java.util.HashMap;
import java.util.List;

import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.builtins.AbstractSWRLBuiltInLibrary;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.BuiltInException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.SWRLRuleEngineBridgeException;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLClass;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLIndividual;

/**
 * Implementations library for SWRL Extensions built-in methods. See <a
 * href="http://protege.cim3.net/cgi-bin/wiki.pl?SWRLExtensionsBuiltIns">here</a> for documentation on this library.
 *
 * See <a href="http://protege.cim3.net/cgi-bin/wiki.pl?SWRLBuiltInBridge">here</a> for documentation on defining SWRL built-in libraries.
 */
public class SWRLBuiltInLibraryImpl extends AbstractSWRLBuiltInLibrary
{
  private static String SWRLXLibraryName = "SWRLExtensionsBuiltIns";
  private HashMap<String, OWLClass> classInvocationMap;
  private HashMap<String, OWLIndividual> individualInvocationMap;

  public SWRLBuiltInLibraryImpl() 
  { 
    super(SWRLXLibraryName); 
  } // SWRLBuiltInLibraryImpl

  public void reset() 
  {
    classInvocationMap = new HashMap<String, OWLClass>();
    individualInvocationMap = new HashMap<String, OWLIndividual>();
  } // reset

  /**
   ** For every pattern of second and subsequent arguments, create an OWL anonymous class and bind it to the first argument. If
   ** the first argument is already bound when the built-in is called, this method returns true.
   */
  public boolean makeOWLClass(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsAtLeast(2, arguments.size());

    if (isUnboundArgument(0, arguments)) {
      OWLClass owlClass = null;
      String createInvocationPattern 
        = createInvocationPattern(getInvokingBridge(), getInvokingRuleName(), getInvokingBuiltInIndex(), getIsInConsequent(),
                                  arguments.subList(1, arguments.size()));

      if (classInvocationMap.containsKey(createInvocationPattern)) owlClass = classInvocationMap.get(createInvocationPattern);
      else {
        owlClass = getInvokingBridge().injectOWLClass();
        classInvocationMap.put(createInvocationPattern, owlClass);
      } // if
      arguments.get(0).setBuiltInResult(createClassArgument(owlClass.getURI())); // Bind the result to the first parameter      
    } // if
    
    return true;
  } // makeOWLClass

  /**
   ** For every pattern of second and subsequent arguments, create an OWL individual of type OWL:Thing and bind it to the first argument. If
   ** the first argument is already bound when the built-in is called, this method returns true.
   */
  public boolean makeOWLIndividual(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsAtLeast(2, arguments.size());

    if (isUnboundArgument(0, arguments)) {
      OWLIndividual owlIndividual = null;
      String createInvocationPattern 
        = createInvocationPattern(getInvokingBridge(), getInvokingRuleName(), getInvokingBuiltInIndex(), getIsInConsequent(),
                                  arguments.subList(1, arguments.size()));

      if (individualInvocationMap.containsKey(createInvocationPattern)) owlIndividual = individualInvocationMap.get(createInvocationPattern);
      else {
        owlIndividual = getInvokingBridge().injectOWLIndividual();
        individualInvocationMap.put(createInvocationPattern, owlIndividual);
      } // if
      arguments.get(0).setBuiltInResult(createIndividualArgument(owlIndividual.getURI())); // Bind the result to the first parameter      
    } // if
    
    return true;
  } // makeOWLIndividual

  // For backwards compatability
  public boolean makeOWLThing(List<BuiltInArgument> arguments) throws BuiltInException
  {
    return makeOWLIndividual(arguments);
  } // makeOWLThing

  // For backwards compatability
  public boolean createOWLThing(List<BuiltInArgument> arguments) throws BuiltInException
  {
    return makeOWLIndividual(arguments);
  } // createOWLThing

  // TODO: check for invocations to swrlx built-ins, which will cause blocking
  public boolean invokeSWRLBuiltIn(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsAtLeast(2, arguments.size());
    
    String builtInName = getArgumentAsAnIndividualURI(0, arguments);
    boolean result = false;

    try {
      result = getInvokingBridge().invokeSWRLBuiltIn(getInvokingRuleName(), builtInName, getInvokingBuiltInIndex(), getIsInConsequent(),
                                                     arguments.subList(1, arguments.size()));
    } catch (SWRLRuleEngineBridgeException e) {
      throw new BuiltInException("error invoking built-in '" + builtInName + "' from built-in: " + e.getMessage());
    } // try

    return result;
  } // invokeSWRLBuiltIn


} // SWRLBuiltInLibraryImpl
