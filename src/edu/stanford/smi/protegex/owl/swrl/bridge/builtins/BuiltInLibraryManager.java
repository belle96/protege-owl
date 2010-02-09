
package edu.stanford.smi.protegex.owl.swrl.bridge.builtins;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.MultiArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.SWRLBuiltInBridge;
import edu.stanford.smi.protegex.owl.swrl.bridge.TargetSWRLRuleEngine;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.BuiltInException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.BuiltInLibraryException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.IncompatibleBuiltInClassException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.IncompatibleBuiltInMethodException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.SWRLRuleEngineBridgeException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.TargetSWRLRuleEngineException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.UnresolvedBuiltInClassException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.UnresolvedBuiltInMethodException;

/**
 ** This class manages the dynamic loading of SWRL built-in libraries and the invocation of built-ins in those libraries. A library is
 ** identified by a prefix and this prefix is used to find and dynamically load a Java class implementing the built-ins in this library. For
 ** example, the <a href="http://protege.cim3.net/cgi-bin/wiki.pl?CoreSWRLBuiltIns">core SWRL built-in library</a> is identified by the
 ** prefix swrlb; built-ins in this library can then be referred to in SWRL rules using this profix followed by the built-in name, e.g.,
 ** swrlb:lessThanOrEqual.<p>
 **
 ** See <a href="http://protege.cim3.net/cgi-bin/wiki.pl?SWRLBuiltInBridge">here</a> for documentation on defining these built-in libraries.
 */
public abstract class BuiltInLibraryManager
{
  private static String BuiltInLibraryPackageBaseName = "edu.stanford.smi.protegex.owl.swrl.bridge.builtins.";

  // Holds instances of implementation classed defining built-in libraries
  private static HashMap<String, SWRLBuiltInLibrary> builtInLibraries;

  static {
    builtInLibraries = new HashMap<String, SWRLBuiltInLibrary>();
  } // static

  /**
   * Invoke a SWRL built-in. This method is called from the invokeSWRLBuiltIn method in the bridge and should not be called directly from a
   * rule engine. The built-in name should be the fully qualified name of the built-in (e.g.,
   * http://www.w3.org/2003/11/swrlb#lessThanOrEqual).
   */
  public static boolean invokeSWRLBuiltIn(TargetSWRLRuleEngine targetRuleEngine, SWRLBuiltInBridge bridge, String ruleName, String builtInName, 
		  								  int builtInIndex, boolean isInConsequent, List<BuiltInArgument> arguments) 
    throws BuiltInException
  {
    String prefix = getPrefix(bridge, builtInName);
    String implementationClassName = getBuiltInLibraryImplementationClassName(prefix);
    String builtInMethodName = getBuiltInMethodName(builtInName);
    SWRLBuiltInLibrary library = loadBuiltInLibrary(bridge, ruleName, prefix, implementationClassName);
    Method method = resolveBuiltInMethod(ruleName, library, prefix, builtInMethodName); // TODO: cache the method
    
    checkBuiltInMethodSignature(ruleName, prefix, builtInMethodName, method); // Check signature of method.

    boolean result = library.invokeBuiltInMethod(method, bridge, ruleName, prefix, builtInMethodName, builtInIndex, isInConsequent, arguments);

    if (result) {
    	if(hasUnboundArguments(arguments)) // Make sure the built-in has bound all its arguments.
         throw new BuiltInException("built-in " + builtInName + " in rule " + ruleName + " returned with unbound arguments");
      
    	for (List<BuiltInArgument> binding : generateBuiltInArgumentBindings(ruleName, builtInName, builtInIndex, arguments)) {
    	  try {
    	    targetRuleEngine.defineBuiltInArgumentBinding(ruleName, builtInName, builtInIndex, binding);
    	  } catch (TargetSWRLRuleEngineException e) {
    	    throw new BuiltInException("error defining argument binding for built-in " + builtInName + " in rule " + ruleName + ": " + e.getMessage());
    	  } // try
    	} // for
    } // if

    return result;
  } // invokeSWRLBuiltIn

  private static SWRLBuiltInLibrary loadBuiltInLibrary(SWRLBuiltInBridge bridge, String ruleName, String prefix, String implementationClassName)
    throws BuiltInLibraryException
  {
    SWRLBuiltInLibrary library;

    if (builtInLibraries.containsKey(prefix)) { // Find the implementation
      library = builtInLibraries.get(prefix);
    } else { // Implementation class not loaded - load it, call its reset method, and cache it.
      library = loadBuiltInLibraryImpl(ruleName, prefix, implementationClassName);
      builtInLibraries.put(prefix, library);
      invokeBuiltInLibraryResetMethod(bridge, library);
    } // if
    return library;
  } // loadBuiltInLibrary

  private static String getPrefix(SWRLBuiltInBridge bridge, String builtInName) 
  {
    int hashIndex = builtInName.indexOf('#');

    if (hashIndex != -1) {
      return bridge.getOWLModel().getPrefixForResourceName(builtInName);
    } else return ""; // No prefix - try the base built-ins package. Ordinarily, built-ins should not be located here.
  } // getPrefix

  private static String getBuiltInLibraryImplementationClassName(String prefix)
  {
    if (prefix.equals("")) return  BuiltInLibraryPackageBaseName + "SWRLBuiltInLibraryImpl";
    return BuiltInLibraryPackageBaseName + prefix + ".SWRLBuiltInLibraryImpl";
  } // getBuiltInLibraryImplementationClassName

  private static String getBuiltInMethodName(String builtInName)
  {
    String builtInMethodName;

    if (builtInName.indexOf("#") == -1) builtInMethodName = builtInName;
    else builtInMethodName = builtInName.substring(builtInName.indexOf("#") + 1, builtInName.length());

    return builtInMethodName;
  } // getBuiltInMethodName

  /**
   ** Invoke the reset() method for each registered built-in library.
   */
  private static void invokeBuiltInLibraryResetMethod(SWRLBuiltInBridge bridge, SWRLBuiltInLibrary library) throws BuiltInLibraryException
  {
    try {
      library.invokeResetMethod(bridge);
    } catch (Exception e) {
      throw new BuiltInLibraryException("error calling 'reset' method in built-in library '" + library.getClass().getName() + "'");
    } // try
  } // invokeBuiltInLibraryResetMethod
  
  public static void invokeAllBuiltInLibrariesResetMethod(SWRLBuiltInBridge bridge) throws SWRLRuleEngineBridgeException
  {
    for (SWRLBuiltInLibrary library : builtInLibraries.values()) invokeBuiltInLibraryResetMethod(bridge, library);
  } // invokeAllBuiltInLibrariesResetMethod

  /**
   * This method is called with a list of built-in arguments. Some argument positions may contain multi-arguments, indicating that there is more
   * than one pattern. If the result has more than one multi-argument, each multi-argument must have the same number of elements.
   */
  public static Set<List<BuiltInArgument>> generateBuiltInArgumentBindings(String ruleName, String builtInName, int builtInIndex, List<BuiltInArgument> arguments) throws BuiltInException
  {
    List<Integer> multiArgumentIndexes = getMultiArgumentIndexes(arguments);
    Set<List<BuiltInArgument>> bindings = new HashSet<List<BuiltInArgument>>();
    MultiArgument multiArgument;
    
    if (multiArgumentIndexes.isEmpty()) // No multi-arguments - do a simple bind.
      bindings.add(arguments);
    else { // Generate all possible bindings.
      int firstMultiArgumentIndex = multiArgumentIndexes.get(0); // Pick the first multi-argument.
      multiArgument = (MultiArgument)arguments.get(firstMultiArgumentIndex);
      int numberOfArgumentsInMultiArgument = multiArgument.getNumberOfArguments(); 

      if (numberOfArgumentsInMultiArgument < 1) throw new BuiltInException("empty multi-argument for built-in " + builtInName + " in rule " + ruleName);
      
      for (int i = 1; i < multiArgumentIndexes.size(); i++) {
      	int multiArgumentIndex = multiArgumentIndexes.get(i);
      	multiArgument = (MultiArgument)arguments.get(multiArgumentIndex);
      	if (numberOfArgumentsInMultiArgument != multiArgument.getNumberOfArguments())
      		 throw new BuiltInException("all multi-arguments must have the same number of elements for built-in " + builtInName + " in rule " + ruleName);
      } // for
      
      for (int multiArgumentArgumentIndex = 0; multiArgumentArgumentIndex < numberOfArgumentsInMultiArgument; multiArgumentArgumentIndex++) {
      	List<BuiltInArgument> argumentsPattern = generateArgumentsPattern(arguments, multiArgumentArgumentIndex);
        bindings.add(argumentsPattern); 
      } // for
    } // if
    
    return bindings;
  } // generateBuiltInBindings

  // Find indices of multi-arguments (if any) in a list of arguments.
  private static List<Integer> getMultiArgumentIndexes(List<BuiltInArgument> arguments)
  {
    List<Integer> result = new ArrayList<Integer>();

    for (int i = 0; i < arguments.size(); i++) 
      if (arguments.get(i) instanceof MultiArgument) result.add(Integer.valueOf(i));

    return result;
  } // getMultiArgumentIndexes

  private static List<BuiltInArgument> generateArgumentsPattern(List<BuiltInArgument> arguments, int multiArgumentArgumentIndex)
  {
    List<BuiltInArgument> result = new ArrayList<BuiltInArgument>();

    for (BuiltInArgument argument: arguments) {
      if (argument instanceof MultiArgument) {
        MultiArgument multiArgument = (MultiArgument)argument;
        result.add(multiArgument.getArguments().get(multiArgumentArgumentIndex));
      } else result.add(argument);
    } // for

    return result;
  } // generateArgumentsPattern  

  private static Method resolveBuiltInMethod(String ruleName, SWRLBuiltInLibrary library, String prefix, String builtInName)
    throws UnresolvedBuiltInMethodException
  {
    Method method;

    try { 
      method = library.getClass().getMethod(builtInName, new Class[] { List.class });
    } catch (Exception e) {
      throw new UnresolvedBuiltInMethodException(ruleName, prefix, builtInName, e.getMessage());
    } // try

    return method;
  } // resolveBuiltInMethod

  // TODO: need to get constructor of library to catch exceptions it may throw.
  private static SWRLBuiltInLibrary loadBuiltInLibraryImpl(String ruleName, String prefix, String className) 
    throws UnresolvedBuiltInClassException, IncompatibleBuiltInClassException, BuiltInLibraryException
  {
    Class<?> swrlBuiltInLibraryClass;
    SWRLBuiltInLibrary swrlBuiltInLibrary;

    try {
      swrlBuiltInLibraryClass = Class.forName(className);
    } catch (Exception e) {
      throw new UnresolvedBuiltInClassException(ruleName, prefix, e.getMessage());
    } // try

    checkBuiltInMethodsClassCompatibility(ruleName, prefix, swrlBuiltInLibraryClass); // Check implementation class for compatibility.

    try {
      swrlBuiltInLibrary = (SWRLBuiltInLibrary)swrlBuiltInLibraryClass.newInstance();
    } catch (InstantiationException e) {
      throw new IncompatibleBuiltInClassException(ruleName, prefix, className, e.getMessage());
    } catch (ExceptionInInitializerError e) {
      throw new IncompatibleBuiltInClassException(ruleName, prefix, className, e.getMessage());
    } catch (IllegalAccessException e) {
      throw new IncompatibleBuiltInClassException(ruleName, prefix, className, e.getMessage());
    } catch (SecurityException e) {
      throw new IncompatibleBuiltInClassException(ruleName, prefix, className, e.getMessage());
    } // try

    return swrlBuiltInLibrary;
  } // loadBuiltInLibraryImpl

  private static void checkBuiltInMethodSignature(String ruleName, String prefix, String builtInName, Method method) 
      throws IncompatibleBuiltInMethodException
  {
    Class<?> exceptionTypes[];
    Type parameterTypes[];

    if (method.getReturnType() != Boolean.TYPE) 
      throw new IncompatibleBuiltInMethodException(ruleName, prefix, builtInName, "Java method must return a boolean");

    exceptionTypes = method.getExceptionTypes();

    if ((exceptionTypes.length != 1) || (exceptionTypes[0] != BuiltInException.class))
      throw new IncompatibleBuiltInMethodException(ruleName, prefix, builtInName, 
                                                   "Java method must throw a single exception of type BuiltInException");

    parameterTypes = method.getGenericParameterTypes();

    if ((parameterTypes.length != 1) || (!(parameterTypes[0] instanceof ParameterizedType)) || 
        (((ParameterizedType)parameterTypes[0]).getRawType() != List.class) ||
        (((ParameterizedType)parameterTypes[0]).getActualTypeArguments().length != 1) ||
        (((ParameterizedType)parameterTypes[0]).getActualTypeArguments()[0] != BuiltInArgument.class))
      throw new IncompatibleBuiltInMethodException(ruleName, prefix, builtInName, 
                                                   "Java method must accept a single List of BuiltInArgument objects");
  } // checkBuiltInMethodSignature

  private static boolean hasUnboundArguments(List<BuiltInArgument> arguments)
  {
    for (BuiltInArgument argument : arguments) if (argument.isUnbound()) return true;

    return false;
  } // hasUnboundArguments

  private static void checkBuiltInMethodsClassCompatibility(String ruleName, String prefix, Class<?> cls) 
    throws IncompatibleBuiltInClassException
  {
    if (!SWRLBuiltInLibrary.class.isAssignableFrom(cls)) 
      throw new IncompatibleBuiltInClassException(ruleName, prefix, cls.getName(), "Java class does not implement SWRLBuiltInLibrary");
  } // checkBuiltInMethodsClassCompatibility

} // BuiltInLibraryManager

