
// TODO: need to rewrite this using JavaCC to make it more extensible.

package edu.stanford.smi.protegex.owl.swrl.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.StringTokenizer;

import edu.stanford.smi.protegex.owl.model.NamespaceUtil;
import edu.stanford.smi.protegex.owl.model.OWLDataRange;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.OWLProperty;
import edu.stanford.smi.protegex.owl.model.RDFObject;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSDatatype;
import edu.stanford.smi.protegex.owl.model.classparser.ParserUtils;
import edu.stanford.smi.protegex.owl.model.impl.XMLSchemaDatatypes;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLAtom;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLAtomList;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLBuiltin;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLFactory;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLImp;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLNames;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable;

/**
 * @author Martin O'Connor  <moconnor@smi.stanford.edu>
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class SWRLParser 
{
  public final static char AND_CHAR = '\u2227';   // ^
  public final static char IMP_CHAR = '\u2192';   // >
  public final static char RING_CHAR = '\u02da';   // .
  public final static String delimiters = " ?\n\t()[],\"'" + AND_CHAR + IMP_CHAR + RING_CHAR; // Note space.

  private OWLModel owlModel;
  private SWRLFactory swrlFactory;
  private boolean parseOnly;
  private Tokenizer tokenizer;
  private Set<String> xmlSchemaSymbols = XMLSchemaDatatypes.getSlotSymbols();
  private Set<String> variables;
  private boolean inHead = false;

  private Map<String, RDFResource> cachedRDFResources;
  
  public SWRLParser(OWLModel owlModel) 
  {
    this.owlModel = owlModel;
    swrlFactory = new SWRLFactory(owlModel);
    parseOnly = true;
    variables = new HashSet<String>();
    cachedRDFResources = new HashMap<String, RDFResource>();
  } // SWRLParser

  public void setParseOnly(boolean parseOnly) { this.parseOnly = parseOnly; } 

  /**
   ** If the rule is correct and incomplete return 'true'; if the rule has errors or is correct and complete, return 'false'.
   */
  public boolean isCorrectAndIncomplete(String rule)
  {
    boolean oldParseOnly = parseOnly;
    boolean result = false;

    setParseOnly(true);

    try {
      parse(rule);
    } catch (SWRLParseException e) {
      if (e instanceof SWRLIncompleteRuleException) result = true;
    } // catch

    setParseOnly(oldParseOnly);

    return result;
  } // isCorrectAndIncomplete

  // This parser will throw a SWRLParseException if it finds errors in the supplied rule. If the rule is correct but incomplete, a
  // SWRLIncompleteRuleException (which is a subclass of SWRLParseException) will be thrown.
  //
  // If parseOnly is true, only checking is performed - no OWL individuals are created; if it is false, individuals are created.

  public SWRLImp parse(String rule) throws SWRLParseException 
  {
    return parse(rule, null);
  } // parse

  public SWRLImp parse(String rule, SWRLImp imp) throws SWRLParseException {
    String token, message;
    SWRLAtomList head = null, body = null;
    SWRLAtom atom = null;
    boolean atLeastOneAtom = false, justProcessedAtom = true;
    
    inHead = false;
    
    variables.clear();
    tokenizer = new Tokenizer(rule.trim());
    
    if (!parseOnly) {
      head = swrlFactory.createAtomList();
      head.setInHead(true);
      body = swrlFactory.createAtomList();
      head.setInHead(false);
    } // if
    
    if (!parseOnly && !tokenizer.hasMoreTokens()) throw new SWRLParseException("Empty rule.");
    
    do {
      if (justProcessedAtom) {
        if (inHead) message = "Expecting " + AND_CHAR;
        else message = "Expecting " + IMP_CHAR + " or " + AND_CHAR + " or " + RING_CHAR + ".";
      } else {
        if (inHead) message = "Expecting atom.";
        else message = "Expecting atom or " + IMP_CHAR + ".";
      } // if
      
      token = getNextNonSpaceToken(message);
      
      if (token.equals("" + IMP_CHAR) || token.equals("->")) { // A rule can have an empty body.
        if (inHead) throw new SWRLParseException("Second occurence of " + IMP_CHAR + ".");
        inHead = true; 
        justProcessedAtom = false;
      } else if (token.equals("-")) {        
        continue; // Ignore "->" while we build up IMP_CHAR.
      } else if (token.equals("" + AND_CHAR) || token.equals("^")) {
          if (!justProcessedAtom) throw new SWRLParseException(AND_CHAR + " may occur only after an atom.");
          justProcessedAtom = false;
      } else if (token.equals("" + RING_CHAR) || token.equals(".")) {
          if (inHead || !justProcessedAtom) throw new SWRLParseException(RING_CHAR + " may occur only in query body.");
          justProcessedAtom = false;
      } else {
        atom = parseAtom(token);
        atLeastOneAtom = true;
        if (!parseOnly) {
          if (inHead) head.append(atom);
          else body.append(atom);
        } // if
        justProcessedAtom = true;
      } // if
    } while (tokenizer.hasMoreTokens());
    
    if (!parseOnly) {
      if (!atLeastOneAtom) throw new SWRLParseException("Incomplete rule - no antecedent or consequent.");
      if (imp == null) imp = swrlFactory.createImp(head, body);
      else {
        imp.setHead(head);
        imp.setBody(body);
      } // if
    } else imp = null;
    
    return imp;
  } // parse

  //public static String getParsableRuleString(String unicodeRule) { return unicodeRule.replace(AND_CHAR, '^').replace(IMP_CHAR, '>'); }

  private SWRLAtom parseAtom(String identifier) throws SWRLParseException 
  {
    SWRLAtom atom = null;
    List<RDFObject> enumeratedList = null;
    boolean isEnumeratedList = false;
    
    if (identifier.startsWith("[")) { // A data range with an enumerated literal list
      enumeratedList = parseDObjectList();
      isEnumeratedList = true;
    } // if

    if (isEnumeratedList) checkAndSkipToken("(", "Expecting parameters enclosed in parentheses for data range atom.");
    else checkAndSkipToken("(", "Expecting parameters enclosed in parentheses for atom " + identifier + ".");
    
    if (isEnumeratedList) atom = parseEnumeratedListParameters(enumeratedList);
    else if (isSameAs(identifier)) atom = parseSameAsAtomParameters();
    else if (isDifferentFrom(identifier)) atom = parseDifferentFromAtomParameters();
    else if (isOWLClassName(identifier)) atom = parseClassAtomParameters(identifier);
    else if (isOWLObjectPropertyName(identifier)) atom = parseIndividualPropertyAtomParameters(identifier);
    else if (isOWLDatatypePropertyName(identifier)) atom = parseDatavaluedPropertyAtomParameters(identifier);
    else if (isBuiltinName(identifier)) atom = parseBuiltinParameters(identifier);
    else throw new SWRLParseException("Invalid atom name " + identifier + ".");
    
    return atom;
  } // parseAtom

  private void checkAndSkipToken(String skipToken, String unexpectedTokenMessage) throws SWRLParseException 
  {
    String token = getNextNonSpaceToken(unexpectedTokenMessage);
    
    if (!token.equalsIgnoreCase(skipToken)) 
      throw new SWRLParseException("Expecting " + skipToken + ", got " + token + "; " + unexpectedTokenMessage);
  } // checkAndSkipToken

  // TODO: Does not deal with escaped quotation characters.
  private String getNextStringToken(String noTokenMessage) throws SWRLParseException 
  {
    String token = "";
    String errorMessage = "Incomplete rule. " + noTokenMessage;
    
    if (!tokenizer.hasMoreTokens()) {
      if (parseOnly) throw new SWRLIncompleteRuleException(errorMessage);
      else throw new SWRLParseException(errorMessage);
    } // if
    
    while (tokenizer.hasMoreTokens()) {
      token = tokenizer.nextToken("\"");
      if (token.equals("\"")) token = ""; // Empty string 
      else checkAndSkipToken("\"", "Expected \" to close string.");
      return token;
    } // while
    
    if (parseOnly) throw new SWRLIncompleteRuleException(errorMessage);
    else throw new SWRLParseException(errorMessage); // Should not get here  
  } // getNextStringToken

  private String getNextNonSpaceToken(String noTokenMessage) throws SWRLParseException 
  {
    String token = "";
    String errorMessage = "Incomplete rule. " + noTokenMessage;
    
    if (!tokenizer.hasMoreTokens()) {
      if (parseOnly) throw new SWRLIncompleteRuleException(errorMessage);
      else throw new SWRLParseException(errorMessage);
    } // if

    while (tokenizer.hasMoreTokens()) {
      token = tokenizer.nextToken();
      if (!(token.equals(" ") || token.equals("\n") || token.equals("\t"))) return token;
    } // while
    
    if (parseOnly) throw new SWRLIncompleteRuleException(errorMessage);
    else throw new SWRLParseException(errorMessage); // Should not get here
  } // getNextNonSpaceToken
  
  private SWRLAtom parseSameAsAtomParameters() throws SWRLParseException 
  {
    RDFResource iObject1, iObject2;
    SWRLAtom atom = null;
    
    iObject1 = parseIObject();
    checkAndSkipToken(",", "Expecting comma-separated second parameter for SameAsAtom.");
    iObject2 = parseIObject();
    
    if (!parseOnly) atom = swrlFactory.createSameIndividualAtom(iObject1, iObject2);
    
    checkAndSkipToken(")", "Expecting closing parenthesis after second parameters in SameAsAtom");
    
    return atom;
  } // parseSameAsAtomParameters

  private SWRLAtom parseDifferentFromAtomParameters() throws SWRLParseException 
  {
    RDFResource iObject1, iObject2;
    SWRLAtom atom = null;
    
    iObject1 = parseIObject();
    checkAndSkipToken(",", "Expecting comma-separated second parameters for DifferentFromAtom");
    iObject2 = parseIObject();
    
    if (!parseOnly) atom = swrlFactory.createDifferentIndividualsAtom(iObject1, iObject2);

    checkAndSkipToken(")", "Only two parameters allowed for DifferentFromAtom");
    
    return atom;
  } // parseDifferentFromAtomParameters

  private SWRLAtom parseClassAtomParameters(String identifier) throws SWRLParseException 
  {
    RDFResource iObject;
    SWRLAtom atom = null;
    
    iObject = parseIObject();
    
    if (!parseOnly) {
      OWLNamedClass aClass = ParserUtils.getOWLClassFromName(owlModel, identifier);
      atom = swrlFactory.createClassAtom(aClass, iObject);
    } // if
    
    checkAndSkipToken(")", "Expecting closing parenthesis for parameter for ClassAtom '" + identifier + "'.");
    
    return atom;
  } // parseClassAtomParameters

  private SWRLAtom parseIndividualPropertyAtomParameters(String identifier) throws SWRLParseException 
  {
    RDFResource iObject1, iObject2;
    SWRLAtom atom = null;
    
    iObject1 = parseIObject();
    checkAndSkipToken(",", "Expecting comma-separated second parameter for IndividualPropertyAtom '" + identifier + "'");
    iObject2 = parseIObject();
    
    if (!parseOnly) {
      OWLObjectProperty objectProperty = ParserUtils.getOWLObjectPropertyFromName(owlModel, identifier);
      if (objectProperty == null) throw new SWRLParseException("no datatype property found for IndividualPropertyAtom: " + identifier);
      atom = swrlFactory.createIndividualPropertyAtom(objectProperty, iObject1, iObject2);
    } // if
    
    checkAndSkipToken(")", "Expecting closing parenthesis after second parameter of IndividualPropertyAtom '" + identifier + "'.");
    
    return atom;
  } // parseClassAtomParameters

  private SWRLAtom parseDatavaluedPropertyAtomParameters(String identifier) throws SWRLParseException 
  {
    RDFResource iObject;
    RDFObject dObject;
    SWRLAtom atom = null;
    OWLDatatypeProperty datatypeProperty;
    String token, errorMessage = "Expecting literal qualification symbol '#' or closing parenthesis after second parameter of DatavaluedPropertyAtom' ";
    
    iObject = parseIObject();
    checkAndSkipToken(",", "Expecting comma-separated second parameter for DatavaluedPropertyAtom '" + identifier + "'.");
    dObject = parseDObject();
    
    token = getNextNonSpaceToken(errorMessage + identifier + "'.");
    
    if (token.equals("#")) { // Literal qualification.
      token = getNextNonSpaceToken("Expecting XML Schema datatype.");
      if (tokenizer.hasMoreTokens() && !isXSDDatatype(token)) 
        throw new SWRLParseException("Invalid XML Schema datatype name: '" + token + "'.");
      if (!parseOnly) dObject = owlModel.createRDFSLiteral(dObject.getBrowserText(), token);
      checkAndSkipToken(")", "Expecting closing parenthesis after second parameter of DatavaluedPropertyAtom");
    } else if (!token.equals(")")) throw new SWRLParseException(errorMessage + identifier + "'.");
    
    if (!parseOnly) {
      datatypeProperty = ParserUtils.getOWLDatatypePropertyFromName(owlModel, identifier);
      atom = swrlFactory.createDatavaluedPropertyAtom(datatypeProperty, iObject, dObject);
    } // if
    
    return atom;
  } // parseDatavaluedPropertyAtomParameters

  private SWRLAtom parseBuiltinParameters(String identifier) throws SWRLParseException 
  {
    SWRLBuiltin builtin;
    SWRLAtom atom = null;
    List<RDFObject> objects = parseObjectList(); // Swallows ')'

    if (!parseOnly) {
      builtin = swrlFactory.getBuiltin(NamespaceUtil.getFullName(owlModel, identifier));
      atom = swrlFactory.createBuiltinAtom(builtin, objects.iterator());
    } // if
    
    return atom;
  } // parseBuiltinParameters

  private SWRLAtom parseEnumeratedListParameters(List<RDFObject> enumeratedList)
    throws SWRLParseException {
    RDFObject dObject;
    SWRLAtom atom = null;
    Object literalValue;
    Iterator<RDFObject> iterator;
    
    dObject = parseDObject();
    
    if (!parseOnly) {
      OWLDataRange dataRange = owlModel.createOWLDataRange();
      RDFProperty oneOfProperty = owlModel.getOWLOneOfProperty();
      
      iterator = enumeratedList.iterator();
      while (iterator.hasNext()) {
        literalValue = (Object) iterator.next();
        dataRange.addPropertyValue(oneOfProperty, literalValue);
      } // while
      atom = swrlFactory.createDataRangeAtom(dataRange, dObject);
    } // if
    checkAndSkipToken(")", "Expecting closing parenthesis after parameter in DataRangeAtom.");
    
    return atom;
  } // parseEnumeratedListParameters

  // Parse a list of variables and literals.
  private List<RDFObject> parseDObjectList() throws SWRLParseException 
  {
    RDFObject dObject;
    List<RDFObject> dObjects = null;
    
    if (!parseOnly) dObjects = new ArrayList<RDFObject>();
    
    dObject = parseDObject();   
    if (!parseOnly) dObjects.add(dObject);
    
    String token = getNextNonSpaceToken("Expecting additional comma-separated variables or literals or closing parenthesis.");
    while (token.equals(",")) {
      dObject = parseDObject();
      if (!parseOnly) dObjects.add(dObject);      
      token = getNextNonSpaceToken("Expecting ',' or ')'.");      
      if (!(token.equals(",") || token.equals(")"))) throw new SWRLParseException("Expecting ',' or ')', got '" + token + "'.");
    } // if    
    return dObjects;
  } // parseDObjectList

  // Parse a list of variables, literals and individual names. 
  private List<RDFObject> parseObjectList() throws SWRLParseException 
  {
    RDFObject object;
    List<RDFObject> objects = null;
    
    if (!parseOnly) objects = new ArrayList<RDFObject>();
    
    object = parseObject();   
    if (!parseOnly) objects.add(object);
    
    String token = getNextNonSpaceToken("Expecting additional comma-separated variables, literals or individual names or closing parenthesis.");
    while (token.equals(",")) {
      object = parseObject();
      if (!parseOnly) objects.add(object);      
      token = getNextNonSpaceToken("Expecting ',' or ')'.");      
      if (!(token.equals(",") || token.equals(")"))) throw new SWRLParseException("Expecting ',' or ')', got '" + token + "'.");
    } // if    
    return objects;
  } // parseObjectList

  // Parse a variable, literal or an individual name.
  private RDFObject parseObject() throws SWRLParseException 
  {
    RDFObject parsedEntity = null;
    String parsedString = getNextNonSpaceToken("Expecting variable or individual name or literal.");
    
    if (parsedString.equals("?")) parsedEntity = parseVariable();
    else { // The entity is an individual name or literal
      if (isValidIndividualName(parsedString)) {
        if (!parseOnly) parsedEntity = getIndividual(parsedString);
      } else if (isValidClassName(parsedString)) {
        if (!parseOnly) parsedEntity = getClass(parsedString);
      } else if (isValidPropertyName(parsedString)) {
        if (!parseOnly) parsedEntity = getProperty(parsedString);
      } else parsedEntity = parseLiteral(parsedString);
    } // if
    
    return parsedEntity;
  } // parseObject

  // Parse a variable or an individual name. For SWRL Full, also allow class and property names.
  private RDFResource parseIObject() throws SWRLParseException 
  {
    RDFResource parsedEntity = null;
    String parsedString = getNextNonSpaceToken("Expecting variable or individual name.");
    
    if (parsedString.equals("?")) parsedEntity = parseVariable();
    else { // The entity is an 
      if (isValidIndividualName(parsedString)) { if (!parseOnly) parsedEntity = getIndividual(parsedString); }
      else if (isValidClassName(parsedString)) { if (!parseOnly) parsedEntity = getClass(parsedString); } // SWRL Full
      else if (isValidPropertyName(parsedString)) { if (!parseOnly) parsedEntity = getProperty(parsedString); } // SWRL Full
      else if (tokenizer.hasMoreTokens()) throw new SWRLParseException("Invalid entity name: '" + parsedString + "'.");
    } // if
    return parsedEntity;
  } // parseIObject

  // Parse a variable or a literal.
  private RDFObject parseDObject() throws SWRLParseException 
  {
    RDFObject parsedEntity = null;
    String parsedString = getNextNonSpaceToken("Expecting variable or literal.");

    if (parsedString.equals("?")) parsedEntity = parseVariable();
    else parsedEntity = parseLiteral(parsedString);

    return parsedEntity;
  } // parseDObject

  private RDFResource parseVariable() throws SWRLParseException
  {
    RDFResource parsedEntity = null;
    String variableName = getNextNonSpaceToken("Expected variable name");      
    checkThatVariableNameIsValid(variableName);
    
    if (tokenizer.hasMoreTokens()) {
      if (!inHead) variables.add(variableName);
      else if (!variables.contains(variableName))
        throw new SWRLParseException("Variable ?" + variableName + " referred to in consequent is not present in antecedent.");
    } // if      
    if (!parseOnly) parsedEntity = getSWRLVariable(variableName);
    return parsedEntity;
  } // parseVariable

  private RDFObject parseLiteral(String parsedString) throws SWRLParseException
  {
    RDFObject parsedEntity = null;

    if (parsedString.equals("\"")) { // The parsed entity is a string
      String stringValue = getNextStringToken("Expected a string.");
      if (!parseOnly) parsedEntity = owlModel.createRDFSLiteral(stringValue, owlModel.getXSDstring());
    } // if
    // According to the XSD spec, xsd:boolean's have the lexical space: {true, false, 1, 0}. We don't allow {1, 0} since these are parsed
    // as XSDints.
    else if (parsedString.startsWith("t") || parsedString.startsWith("T") || parsedString.startsWith("f") || parsedString.startsWith("F")) {
      if (tokenizer.hasMoreTokens()) {
        if (parsedString.equalsIgnoreCase("true") || parsedString.equalsIgnoreCase("false")) {
          if (!parseOnly) parsedEntity = owlModel.createRDFSLiteral(parsedString, owlModel.getXSDboolean());
        } else throw new SWRLParseException("Invalid literal " + parsedString + ".");
      } // if
    } else { // Is it an integer, float, long or double then?
      try {
    	if (parsedString.contains(".")) {
    	  Double.parseDouble(parsedString); // Check it
          if (!parseOnly) parsedEntity = owlModel.createRDFSLiteral(parsedString, owlModel.getXSDdouble());
    	} else {
          Long.parseLong(parsedString); // Check it
          if (!parseOnly) parsedEntity = owlModel.createRDFSLiteral(parsedString, owlModel.getXSDlong());
        } // if
      } catch (NumberFormatException e) {
        String errorMessage = "Invalid literal " + parsedString + ".";
        if (parseOnly) throw new SWRLIncompleteRuleException(errorMessage);
        else throw new SWRLParseException(errorMessage);
      } // try
    } // if
    
    return parsedEntity;
  } // parseLiteral

  private boolean isSameAs(String identifier) throws SWRLParseException 
  {
    return identifier.equalsIgnoreCase("sameAs");
  } // isSameAs

  private boolean isDifferentFrom(String identifier) throws SWRLParseException 
  {
    return identifier.equalsIgnoreCase("differentFrom");
  } // isDifferentFrom

  private boolean isXSDDatatype(String identifier) throws SWRLParseException 
  {
    return (identifier.startsWith("xsd:") && xmlSchemaSymbols.contains(identifier.substring(4)));
  } // isXSDDatatype
  
  private void checkThatIdentifierIsValid(String identifier) throws SWRLParseException
  {
    if (!isValidIdentifier(identifier)) throw new SWRLParseException("Invalid identifier " + identifier + ".");
  } // checkThatIdentifierIsValid

  // Possible valid identifiers include 'http://swrl.stanford.edu/ontolgies/built-ins/3.3/swrlx.owl#createIndividual'.
  private boolean isValidIdentifier(String s) 
  {
    if (s.length() == 0) return false;

    if (!Character.isJavaIdentifierStart(s.charAt(0)) && s.charAt(0) != ':') return false; // HACK to deal with ":TO" and ":FROM".

    for (int i = 1; i < s.length(); i++) {
      char c = s.charAt(i);
      if (!(Character.isJavaIdentifierPart(c) || c == ':' || c == '-' || c == '#' || c == '/' || c == '.')) {
        return false;
      } // if
    } // for
    return true;
  } // isValidIdentifier

  private void checkThatVariableNameIsValid(String variableName) throws SWRLParseException
  {
    RDFResource resource;
    checkThatIdentifierIsValid(variableName);
    
    resource = getRDFResource(variableName); 

    if ((resource != null) && !(resource instanceof SWRLVariable)) 
	throw new SWRLParseException("Invalid variable name " + variableName + 
                               ". Cannot use name of existing OWL class, property, or individual.");
  } // checkThatVariableNameIsValid
 
  private boolean isOWLClassName(String identifier) throws SWRLParseException 
  {
    return ParserUtils.getOWLClassFromName(owlModel, identifier) != null; 
  } // isOWLClassName

  private boolean isOWLObjectPropertyName(String identifier) throws SWRLParseException 
  {
    return ParserUtils.getOWLObjectPropertyFromName(owlModel, identifier) != null; 
  } // isOWLObjectPropertyName

  private boolean isOWLDatatypePropertyName(String identifier) throws SWRLParseException 
  {
      return ParserUtils.getOWLDatatypePropertyFromName(owlModel, identifier) != null; 
  } // isOWLDatatypePropertyName

  private boolean isBuiltinName(String identifier) throws SWRLParseException 
  {
    RDFResource resource = getRDFResource(identifier); 
    return resource != null && resource.getProtegeType().getName().equals(SWRLNames.Cls.BUILTIN);
  } // isBuiltinName

  private boolean isValidIndividualName(String name) throws SWRLParseException 
  {
      try {
          return ParserUtils.getOWLIndividualFromName(owlModel, name) != null;
      }
      catch (Throwable t) {
          return false;
      }
  } // isValidIndividualName

  private boolean isValidClassName(String name) throws SWRLParseException 
  {
      try {
          RDFResource resource = getRDFResource(name);

          if (resource == null) return false;

          if (resource instanceof OWLNamedClass) return true;
          else return false;
      }
      catch (Throwable t) {
          return false;
      }
  } // isValidClassName

  private boolean isValidPropertyName(String name) throws SWRLParseException 
  {
      try {
          RDFResource resource = getRDFResource(name);

          if (resource == null) return false;

          if (resource instanceof OWLProperty) return true;
          else return false;
      }
      catch (Throwable t) {
          return false;
      }
  } // isValidPropertyName

  private OWLIndividual getIndividual(String name) throws SWRLParseException 
  {
      OWLIndividual resource = ParserUtils.getOWLIndividualFromName(owlModel, name);

    if (resource == null) throw new SWRLParseException(name + " is not a valid individual name");

    return resource;
  } // getIndividual

  private OWLNamedClass getClass(String name) throws SWRLParseException 
  {
      OWLNamedClass resource = ParserUtils.getOWLClassFromName(owlModel, name);
      
      if (resource == null) throw new SWRLParseException(name + " is not a valid class name");

    return resource;
  } // getClass

  private OWLProperty getProperty(String name) throws SWRLParseException 
  {
      RDFProperty resource = ParserUtils.getRDFPropertyFromName(owlModel, name);

      if (resource == null || !(resource instanceof OWLProperty)) throw new SWRLParseException(name + " is not a valid property name");

      return (OWLProperty) resource;
  } // getProperty

  private SWRLVariable getSWRLVariable(String name) throws SWRLParseException 
  {
    RDFResource resource = owlModel.getRDFResource(NamespaceUtil.getFullName(owlModel, name));
    
    if (resource instanceof SWRLVariable) return (SWRLVariable) resource;
    else if (resource == null) return swrlFactory.createVariable(NamespaceUtil.getFullName(owlModel, name));
    else throw new SWRLParseException(name + " cannot be used as a variable name");
  } // getSWRLVariable

  private RDFResource getRDFResource(String resourceName)
  {
    RDFResource resource;

    if (parseOnly) {
      if (cachedRDFResources.containsKey(resourceName)) resource = cachedRDFResources.get(resourceName);
      else {
          resource = ParserUtils.getRDFResourceFromName(owlModel, resourceName);
          cachedRDFResources.put(resourceName, resource); // May be null
      } // if
    } else resource = ParserUtils.getRDFResourceFromName(owlModel, resourceName);

    return resource;
  } // getRDFResource
  
  private static class Tokenizer 
  {
    private StringTokenizer internalTokenizer;
    
    public Tokenizer(String input) { internalTokenizer = new StringTokenizer(input, delimiters, true); }

    public boolean hasMoreTokens() { return internalTokenizer.hasMoreTokens();  }
    public String nextToken(String myDelimiters) { return internalTokenizer.nextToken(myDelimiters); }
      
    public String nextToken() throws NoSuchElementException 
    {
      String token = internalTokenizer.nextToken(delimiters);
      if  (!token.equals("'")) return token;

      StringBuffer buffer = new StringBuffer();
      while (internalTokenizer.hasMoreTokens() &&
             !(token = internalTokenizer.nextToken()).equals("'")) {
        buffer.append(token);
      }
      return buffer.toString();
    } // nextToken
   
  } // Tokenizer

  
} // SWRLParser
