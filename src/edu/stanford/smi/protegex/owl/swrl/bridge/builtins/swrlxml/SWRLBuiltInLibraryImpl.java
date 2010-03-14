
package edu.stanford.smi.protegex.owl.swrl.bridge.builtins.swrlxml;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;

import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.MultiArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.builtins.AbstractSWRLBuiltInLibrary;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.BuiltInException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.BuiltInLibraryException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.InvalidBuiltInArgumentException;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLClass;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLIndividual;

public class SWRLBuiltInLibraryImpl extends AbstractSWRLBuiltInLibrary 
{
  private static String SWRLXMLLibraryName = "SWRLXMLBuiltIns";

  private OWLClass xmlElementOWLClass = null;

  public SWRLBuiltInLibraryImpl() throws BuiltInLibraryException
  { 
    super(SWRLXMLLibraryName); 

    reset();
  } // SWRLBuiltInLibraryImpl

  private XMLProcessor xmlProcessor;
  private XMLBridgeMapper xmlMapper;

  private Map<String, OWLIndividual> documentMappings; // File name to OWL document individuals
  private Map<String, Document> documents; // Individual name to Document

  private Map<String, Set<OWLIndividual>> elementMappings; // XML path to element individuals
  private Map<String, Element> elements; // Individual name to Element

  public void reset() throws BuiltInLibraryException
  {
    xmlProcessor = new XMLProcessor();

    xmlMapper = new XMLBridgeMapper();

    documentMappings = new HashMap<String, OWLIndividual>();
    documents = new HashMap<String, Document>();

    elementMappings = new HashMap<String, Set<OWLIndividual>>();
    elements = new HashMap<String, Element>();
  } // reset

  public boolean makeXMLDocument(List<BuiltInArgument> arguments) throws BuiltInException 
  { 
    boolean result = false;

    checkNumberOfArgumentsEqualTo(2, arguments.size());
    checkForUnboundNonFirstArguments(arguments);

    if (isUnboundArgument(0, arguments)) {
      String inputXMLStreamName = getArgumentAsAString(1, arguments);
      OWLIndividual xmlDocument = null;
      Document doc;

      if (!documentMappings.containsKey(inputXMLStreamName)) {

        try {
          doc = xmlProcessor.processXMLStream(inputXMLStreamName);
          xmlDocument = xmlMapper.document2XMLDocumentMapping(doc, getInvokingBridge());
          documentMappings.put(inputXMLStreamName, xmlDocument);
          documents.put(xmlDocument.getURI(), doc);
        } catch (XMLProcessorException e) {
          throw new BuiltInException("error processing XML stream " + inputXMLStreamName + ": " + e.getMessage());
        } catch (XMLBridgeMapperException e) {
          throw new BuiltInException("error mapping XML stream " + inputXMLStreamName + ": " + e.getMessage());
        } // try
      } else xmlDocument = documentMappings.get(inputXMLStreamName);

      arguments.get(0).setBuiltInResult(createIndividualArgument(xmlDocument.getURI())); // Bind the result to the first parameter
      result = true;
    } // if
    return result;
  } // makeXMLDocument

  public boolean element(List<BuiltInArgument> arguments) throws BuiltInException 
  {
    boolean result = false;

    checkNumberOfArgumentsEqualTo(3, arguments.size());
    checkForUnboundNonFirstArguments(arguments);

    if (isUnboundArgument(0, arguments)) {
      Document doc = getArgumentAsADocument(1, arguments);
      String path = getArgumentAsAString(2, arguments);
      Set<OWLIndividual> owlElements;

      if (!elementMappings.containsKey(path)) {

        try {
          XPath xPath = XPath.newInstance(path);
          Iterator elementIterator = xPath.selectNodes(doc).iterator();
          owlElements = new HashSet<OWLIndividual>();
          
          while (elementIterator.hasNext()) {
            Object o = elementIterator.next();
            OWLIndividual xmlElement = getInvokingBridge().injectOWLIndividualOfClass(getXMLElementOWLClass());
            Element element;
            
            if (!(o instanceof Element)) 
              throw new BuiltInException("path " + path + " must only refer to XML elements, found " + o.getClass());

            element = (Element)o;
            elements.put(xmlElement.getURI(), element);

            owlElements.add(xmlElement);
          } // while
          if (!owlElements.isEmpty()) elementMappings.put(path, owlElements);
        } catch (JDOMException e) {
          throw new BuiltInException("JDOM error processing XML path " + path + ": " + e.getMessage());
        } // try
      } else owlElements = elementMappings.get(path);

      if (owlElements.isEmpty()) result = false;
      else if (owlElements.size() == 1) {
      	OWLIndividual individual = (OWLIndividual)owlElements.toArray()[0]; // Bind the single individual to the first parameter
      	arguments.get(0).setBuiltInResult(createIndividualArgument(individual.getURI()));
        result = true;
      } else {
        MultiArgument multiArgument = createMultiArgument();
        for (OWLIndividual xmlElement : owlElements) 
          multiArgument.addArgument(createIndividualArgument(xmlElement.getURI()));
        result = !multiArgument.hasNoArguments();
        arguments.get(0).setBuiltInResult(multiArgument);
      } // if
    } // if
    return result;
  }

  public boolean subElement(List<BuiltInArgument> arguments) throws BuiltInException 
  {
    boolean result = false;

    checkNumberOfArgumentsEqualTo(3, arguments.size());
    checkForUnboundNonFirstArguments(arguments);

    if (isUnboundArgument(0, arguments)) {
      Element parent = getArgumentAsAnElement(1, arguments);
      String path = getArgumentAsAString(2, arguments);
      Set<OWLIndividual> owlElements;

      try {
        XPath xPath = XPath.newInstance(path);
        Iterator elementIterator = xPath.selectNodes(parent).iterator();
        owlElements = new HashSet<OWLIndividual>();
        
        while (elementIterator.hasNext()) {
          Object o = elementIterator.next();
          OWLIndividual xmlElement = getInvokingBridge().injectOWLIndividualOfClass(getXMLElementOWLClass());
          Element element;
          
          if (!(o instanceof Element)) 
            throw new BuiltInException("path " + path + " must only refer to XML elements, found " + o.getClass());
          
          element = (Element)o;
          elements.put(xmlElement.getURI(), element);
          
          owlElements.add(xmlElement);
        } // while
      } catch (JDOMException e) {
        throw new BuiltInException("JDOM error processing XML path " + path + ": " + e.getMessage());
      } // try
      
      if (owlElements.isEmpty()) result = false;
      else if (owlElements.size() == 1) {
      	OWLIndividual individual = (OWLIndividual)owlElements.toArray()[0]; // Bind the single individual to the first parameter
      	arguments.get(0).setBuiltInResult(createIndividualArgument(individual.getURI()));
        result = true;
      } else {
        MultiArgument multiArgument = createMultiArgument();
        for (OWLIndividual xmlElement : owlElements) 
          multiArgument.addArgument(createIndividualArgument(xmlElement.getURI()));
        arguments.get(0).setBuiltInResult(multiArgument);
        result = !multiArgument.hasNoArguments();
      } // if
    } // if
    return result;
  } 

  public boolean attributeValue(List<BuiltInArgument> arguments) throws BuiltInException 
  {
    boolean result = false;

    checkNumberOfArgumentsEqualTo(3, arguments.size());
    checkForUnboundNonFirstArguments(arguments);

    if (isUnboundArgument(0, arguments)) {
      Element element = getArgumentAsAnElement(1, arguments);
      String attributeName = getArgumentAsAString(2, arguments);
      String attributeValue = element.getAttributeValue(attributeName);

      if (attributeValue != null) {
        arguments.get(0).setBuiltInResult(createDataValueArgument(attributeValue));
        result = true;
      } // if
    } // if
    return result;
  } 

  private Document getArgumentAsADocument(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
  {
    Document document = null;

    if (isArgumentAnIndividual(argumentNumber, arguments)) {
      String individualURI = getArgumentAsAnIndividualURI(argumentNumber, arguments);
      
      if (getInvokingBridge().isOWLIndividualOfClass(individualURI, XMLBridgeMapper.XMLDocumentMappingOWLClassName)) {
      } else throw new InvalidBuiltInArgumentException(argumentNumber, "individual " + individualURI + " is not a " +
                                                       XMLBridgeMapper.XMLDocumentMappingOWLClassName);

      if (documents.containsKey(individualURI)) document = documents.get(individualURI);
      else throw new InvalidBuiltInArgumentException(argumentNumber, "" + XMLBridgeMapper.XMLDocumentMappingOWLClassName + " individual " + 
                                                     individualURI + " does not refer to a valid document");
    } else throw new InvalidBuiltInArgumentException(argumentNumber, "expecting a " +  XMLBridgeMapper.XMLDocumentMappingOWLClassName + " individual" +
                                                     ", got " + arguments.get(argumentNumber));
    return document;
  } // getArgumentAsADocument

  private Element getArgumentAsAnElement(int argumentNumber, List<BuiltInArgument> arguments) throws BuiltInException
  {
    Element element = null;

    if (isArgumentAnIndividual(argumentNumber, arguments)) {
      String individualURI = getArgumentAsAnIndividualURI(argumentNumber, arguments);
      
      if (getInvokingBridge().isOWLIndividualOfClass(individualURI, XMLBridgeMapper.XMLElementMappingOWLClassName)) {
      } else throw new InvalidBuiltInArgumentException(argumentNumber, "individual " + individualURI + " is not a " +
                                                       XMLBridgeMapper.XMLElementMappingOWLClassName);

      if (elements.containsKey(individualURI)) element = elements.get(individualURI);
      else throw new InvalidBuiltInArgumentException(argumentNumber, "" + XMLBridgeMapper.XMLElementMappingOWLClassName + " individual " + 
                                                     individualURI + " does not refer to a valid element");
    } else throw new InvalidBuiltInArgumentException(argumentNumber, "expecting a " +  XMLBridgeMapper.XMLElementMappingOWLClassName + " individual" +
                                                     ", got " + arguments.get(argumentNumber) + "");
    return element;
  } // getArgumentAsAnElement

  private OWLClass getXMLElementOWLClass() throws BuiltInException
  {
    if (xmlElementOWLClass == null) {
      xmlElementOWLClass = getInvokingBridge().getOWLDataFactory().getOWLClass(XMLBridgeMapper.XMLElementMappingOWLClassName);
    } //  if

    return xmlElementOWLClass;
  } // getXMLElementOWLClass

} // SWRLBuiltInLibraryImpl
