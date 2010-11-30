
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
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.SWRLBuiltInLibraryException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.IncompatibleBuiltInClassException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.IncompatibleBuiltInMethodException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.TargetSWRLRuleEngineException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.UnresolvedBuiltInClassException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.UnresolvedBuiltInMethodException;

/**
 * This class manages the dynamic loading of SWRL built-in libraries and the invocation of built-ins in those libraries. A library is
 * identified by a prefix and this prefix is used to find and dynamically load a Java class implementing the built-ins in this library. For
 * example, the <a href="http://protege.cim3.net/cgi-bin/wiki.pl?CoreSWRLBuiltIns">core SWRL built-in library</a> is identified by the
 * prefix swrlb; built-ins in this library can then be referred to in SWRL rules using this prefix followed by the built-in name, e.g.,
 * swrlb:lessThanOrEqual.<p>
 *
 * See <a href="http://protege.cim3.net/cgi-bin/wiki.pl?SWRLBuiltInBridge">here</a> for documentation on defining these built-in libraries.
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
  public static boolean invokeSWRLBuiltIn(TargetSWRLRuleEngine targetRuleEngine, SWRLBuiltInBridge bridge, String ruleName, String builtInURI, 
		  								                    int builtInIndex, boolean isInConsequent, List<BuiltInArgument> arguments) 
    throws BuiltInException
  {
    String prefix = getPrefix(bridge, builtInURI);
    String implementationClassName = getBuiltInLibraryImplementationClassName(prefix);
    String builtInMethodName = getBuiltInMethodName(builtInURI);
    SWRLBuiltInLibrary library = loadBuiltInLibrary(bridge, ruleName, prefix, implementationClassName);
    Method method = resolveBuiltInMethod(ruleName, library, prefix, builtInMethodName); // TODO: cache the method
    
    checkBuiltInMethodSignature(ruleName, prefix, builtInMethodName, method); // Check signature of method.

    boolean result = library.invokeBuiltInMethod(method, bridge, ruleName, prefix, builtInMethodName, builtInIndex, isInConsequent, arguments);

    if (result) {
    	if(hasUnboundArguments(arguments)) // Make sure the built-in has bound all its arguments.
         throw new BuiltInException("built-in " + builtInURI + "(index " + builtInIndex + ") in rule " + ruleName + " returned with unbound arguments");
      
    	for (List<BuiltInArgument> binding : generateBuiltInArgumentBindings(ruleName, builtInURI, builtInIndex, arguments)) {
    	  try {
    	    targetRuleEngine.defineBuiltInArgumentBinding(ruleName, builtInURI, builtInIndex, binding);
    	  } catch (TargetSWRLRuleEngineException e) {
    	    throw new BuiltInException("error defining argument binding for built-in " + builtInURI + "(index " + builtInIndex + ") in rule " + ruleName + ": " + e.getMessage());
    	  } // try
    	} // for
    } // if

    return result;
  }

  private static SWRLBuiltInLibrary loadBuiltInLibrary(SWRLBuiltInBridge bridge, String ruleName, String prefix, String implementationClassName)
    throws SWRLBuiltInLibraryException
  {
    SWRLBuiltInLibrary library;

    if (builtInLibraries.containsKey(prefix)) { // Find the implementation.
      library = builtInLibraries.get(prefix);
    } else { // Implementation class not loaded - load it, call its reset method, and cache it.
      library = loadBuiltInLibraryImpl(ruleName, prefix, implementationClassName);
      builtInLibraries.put(prefix, library);
      invokeBuiltInLibraryResetMethod(bridge, library);
    } // if
    return library;
  }

  private static String getPrefix(SWRLBuiltInBridge bridge, String builtInURI) 
  {
    int hashIndex = builtInURI.indexOf('#');

    if (hashIndex != -1) {
      return bridge.getActiveOntology().getOWLModel().getPrefixForResourceName(builtInURI); // TODO: we need an OWIAPI compliant way of doing this.
    } else return ""; // No prefix - try the base built-ins package. Ordinarily, built-ins should not be located here.
  }

  private static String getBuiltInLibraryImplementationClassName(String prefix)
  {
    if (prefix.equals("")) return  BuiltInLibraryPackageBaseName + "SWRLBuiltInLibraryImpl";
    return BuiltInLibraryPackageBaseName + prefix + ".SWRLBuiltInLibraryImpl";
  }

  private static String getBuiltInMethodName(String builtInURI)
  {
    String builtInMethodName;

    if (builtInURI.indexOf("#") == -1) builtInMethodName = builtInURI;
    else builtInMethodName = builtInURI.substring(builtInURI.indexOf("#") + 1, builtInURI.length());

    return builtInMethodName;
  }

  /**
   * Invoke the reset() method for each registered built-in library.
   */
  private static void invokeBuiltInLibraryResetMethod(SWRLBuiltInBridge bridge, SWRLBuiltInLibrary library) throws SWRLBuiltInLibraryException
  {
    try {
      library.invokeResetMethod(bridge);
    } catch (Exception e) {
      throw new SWRLBuiltInLibraryException("error calling reset method in built-in library " + library.getClass());
    } // try
  } 
  
  public static void invokeAllBuiltInLibrariesResetMethod(SWRLBuiltInBridge bridge) throws SWRLBuiltInLibraryException
  {
    for (SWRLBuiltInLibrary library : builtInLibraries.values()) invokeBuiltInLibraryResetMethod(bridge, library);
  }

  /**
   * This method is called with a list of built-in arguments. Some argument positions may contain multi-arguments, indicating that there is more
   * than one pattern. If the result has more than one multi-argument, each multi-argument must have the same number of elements.
   */
  public static Set<List<BuiltInArgument>> generateBuiltInArgumentBindings(String ruleName, String builtInURI, int builtInIndex, List<BuiltInArgument> arguments) 
    throws BuiltInException
  {
    List<Integer> multiArgumentIndexes = getMultiArgumentIndexes(arguments);
    Set<List<BuiltInArgument>> bindings = new HashSet<List<BuiltInArgument>>();
    
    if (multiArgumentIndexes.isEmpty()) // No multi-arguments - do a simple bind.
      bindings.add(arguments);
    else { // Generate all possible bindings.
      int firstMultiArgumentIndex = multiArgumentIndexes.get(0); // Pick the first multi-argument.
      MultiArgument multiArgument = arguments.get(firstMultiArgumentIndex).getBuiltInMultiArgumentResult();
      int numberOfArgumentsInMultiArgument = multiArgument.getNumberOfArguments(); 

      if (numberOfArgumentsInMultiArgument < 1) throw new BuiltInException("empty multi-argument for built-in " + builtInURI + "(index " + builtInIndex + ") in rule " + ruleName);
      
      for (int i = 1; i < multiArgumentIndexes.size(); i++) {
      	int multiArgumentIndex = multiArgumentIndexes.get(i);
      	multiArgument = arguments.get(multiArgumentIndex).getBuiltInMultiArgumentResult();
      	if (numberOfArgumentsInMultiArgument != multiArgument.getNumberOfArguments())
      		 throw new BuiltInException("all multi-arguments must have the same number of elements for built-in " + builtInURI + "(index " + builtInIndex + ") in rule " + ruleName);
      } // for
      
      for (int multiArgumentArgumentIndex = 0; multiArgumentArgumentIndex < numberOfArgumentsInMultiArgument; multiArgumentArgumentIndex++) {
      	List<BuiltInArgument> argumentsPattern = generateArgumentsPattern(arguments, multiArgumentArgumentIndex);
        bindings.add(argumentsPattern); 
      } // for
    } // if
    
    return bindings;
  } 

  // Find indices of multi-arguments (if any) in a list of arguments.
  private static List<Integer> getMultiArgumentIndexes(List<BuiltInArgument> arguments)
  {
    List<Integer> result = new ArrayList<Integer>();

    for (int i = 0; i < arguments.size(); i++) 
      if (arguments.get(i).hasBuiltInMultiArgumentResult()) result.add(Integer.valueOf(i));

    return result;
  }
  
  private static List<BuiltInArgument> generateArgumentsPattern(List<BuiltInArgument> arguments, int multiArgumentArgumentIndex)
    throws BuiltInException
  {
    List<BuiltInArgument> result = new ArrayList<BuiltInArgument>();

    for (BuiltInArgument argument : arguments) {
      if (argument.hasBuiltInMultiArgumentResult()) {
        MultiArgument multiArgument = argument.getBuiltInMultiArgumentResult();
        result.add(multiArgument.getArguments().get(multiArgumentArgumentIndex));
      } else result.add(argument);
    } // for

    return result;
  }   

  private static Method resolveBuiltInMethod(String ruleName, SWRLBuiltInLibrary library, String prefix, String builtInURI)
    throws UnresolvedBuiltInMethodException
  {
    Method method;

    try { 
      method = library.getClass().getMethod(builtInURI, new Class[] { List.class });
    } catch (Exception e) {
      throw new UnresolvedBuiltInMethodException(ruleName, prefix, builtInURI, e.getMessage());
    } // try

    return method;
  } 

  // TODO: need to get constructor of library to catch exceptions it may throw.
  private static SWRLBuiltInLibrary loadBuiltInLibraryImpl(String ruleName, String prefix, String className) 
    throws UnresolvedBuiltInClassException, IncompatibleBuiltInClassException, SWRLBuiltInLibraryException
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
  }

  private static void checkBuiltInMethodSignature(String ruleName, String prefix, String builtInURI, Method method) 
      throws IncompatibleBuiltInMethodException
  {
    Class<?> exceptionTypes[];
    Type parameterTypes[];

    if (method.getReturnType() != Boolean.TYPE) 
      throw new IncompatibleBuiltInMethodException(ruleName, prefix, builtInURI, "Java method must return a boolean");

    exceptionTypes = method.getExceptionTypes();

    if ((exceptionTypes.length != 1) || (exceptionTypes[0] != BuiltInException.class))
      throw new IncompatibleBuiltInMethodException(ruleName, prefix, builtInURI, 
                                                   "Java method must throw a single exception of type BuiltInException");

    parameterTypes = method.getGenericParameterTypes();

    if ((parameterTypes.length != 1) || (!(parameterTypes[0] instanceof ParameterizedType)) || 
        (((ParameterizedType)parameterTypes[0]).getRawType() != List.class) ||
        (((ParameterizedType)parameterTypes[0]).getActualTypeArguments().length != 1) ||
        (((ParameterizedType)parameterTypes[0]).getActualTypeArguments()[0] != BuiltInArgument.class))
      throw new IncompatibleBuiltInMethodException(ruleName, prefix, builtInURI, 
                                                   "Java built-in method implementation must accept a single List of BuiltInArgument objects");
  }

  private static boolean hasUnboundArguments(List<BuiltInArgument> arguments)
  {
    for (BuiltInArgument argument : arguments) if (argument.isUnbound()) return true;

    return false;
  }

  private static void checkBuiltInMethodsClassCompatibility(String ruleName, String prefix, Class<?> cls) 
    throws IncompatibleBuiltInClassException
  {
    if (!SWRLBuiltInLibrary.class.isAssignableFrom(cls)) 
      throw new IncompatibleBuiltInClassException(ruleName, prefix, cls.getName(), "Java class does not extend SWRLBuiltInLibrary");
  } 
}

