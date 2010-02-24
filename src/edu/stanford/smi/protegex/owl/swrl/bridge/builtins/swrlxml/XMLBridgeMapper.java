
package edu.stanford.smi.protegex.owl.swrl.bridge.builtins.swrlxml;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.Text;
import org.jdom.filter.ContentFilter;
import org.jdom.filter.Filter;

import edu.stanford.smi.protegex.owl.swrl.bridge.OWLDataFactory;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLDataValueFactory;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLIndividual;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLProperty;
import edu.stanford.smi.protegex.owl.swrl.bridge.SWRLBuiltInBridge;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.OWLFactoryException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.SWRLBuiltInBridgeException;
import edu.stanford.smi.protegex.owl.swrl.bridge.impl.OWLDataFactoryImpl;
import edu.stanford.smi.protegex.owl.swrl.exceptions.SWRLOWLUtilException;
import edu.stanford.smi.protegex.owl.swrl.util.SWRLOWLUtil;

public class XMLBridgeMapper
{
  public static final String SWRLXMLAlias = "swrlxml:";
  public static final String SWRLXMLNamespace = "http://swrl.stanford.edu/ontologies/built-ins/3.4/swrlxml.owl#";

  public static final String XMLDocumentMappingOWLClassName = SWRLXMLNamespace + "XMLDocument";
  public static final String XMLDocumentMappingHasRootElementPropertyName = SWRLXMLNamespace + "hasRootElement";
  public static final String XMLDocumentMappingHasElementsPropertyName = SWRLXMLNamespace + "hasElements";

  public static final String XMLElementMappingOWLClassName = SWRLXMLNamespace + "XMLElement";
  public static final String XMLMappingHasNamePropertyName = SWRLXMLNamespace + "hasName";
  public static final String XMLMappingHasNamespacePrefixPropertyName = SWRLXMLNamespace + "hasNamespacePrefix";
  public static final String XMLMappingHasNamespaceURIPropertyName = SWRLXMLNamespace + "hasNamespaceURI";
  public static final String XMLElementMappingHasMappedIndividualsPropertyName = SWRLXMLNamespace + "hasMappedIndividuals";
  public static final String XMLElementMappingHasSubElementsPropertyName = SWRLXMLNamespace + "hasSubElements";
  public static final String XMLElementMappingHasAttributesPropertyName = SWRLXMLNamespace + "hasAttributes";
  public static final String XMLElementMappingHasContentPropertyName = SWRLXMLNamespace + "hasContent";

  public static final String XMLAttributeMappingOWLClassName = SWRLXMLNamespace + "XMLAttribute";
  public static final String XMLAttributeMappingHasValuePropertyName = SWRLXMLNamespace + "hasValue";

  private OWLProperty rootElementProperty, elementsProperty, subElementsProperty, nameProperty, namespacePrefixProperty, namespaceURIProperty, 
    contentProperty, valueProperty, attributesProperty;

  private OWLDataFactory owlDataFactory;
  private OWLDataValueFactory owlDataValueFactory;

  public XMLBridgeMapper() 
  {
    owlDataFactory = new OWLDataFactoryImpl();
    owlDataValueFactory = OWLDataValueFactory.create();
    
    rootElementProperty = owlDataFactory.getOWLObjectProperty(XMLDocumentMappingHasRootElementPropertyName);
    elementsProperty = owlDataFactory.getOWLObjectProperty(XMLDocumentMappingHasElementsPropertyName);
    nameProperty = owlDataFactory.getOWLDataProperty(XMLMappingHasNamePropertyName);
    namespacePrefixProperty = owlDataFactory.getOWLDataProperty(XMLMappingHasNamespacePrefixPropertyName);
    namespaceURIProperty = owlDataFactory.getOWLDataProperty(XMLMappingHasNamespaceURIPropertyName);
    contentProperty = owlDataFactory.getOWLDataProperty(XMLElementMappingHasContentPropertyName);
    subElementsProperty = owlDataFactory.getOWLObjectProperty(XMLElementMappingHasSubElementsPropertyName);
    valueProperty = owlDataFactory.getOWLDataProperty(XMLAttributeMappingHasValuePropertyName);
    attributesProperty = owlDataFactory.getOWLObjectProperty(XMLElementMappingHasAttributesPropertyName);
  } // XMLBridgeMapper

  public Document xmlDocumentMapping2Document(SWRLBuiltInBridge bridge) throws XMLBridgeMapperException
  {

    edu.stanford.smi.protegex.owl.model.OWLNamedClass xmlDocumentMappingClass;
    edu.stanford.smi.protegex.owl.model.OWLIndividual xmlDocumentMapping, xmlRootElementMapping = null;
    edu.stanford.smi.protegex.owl.model.RDFResource propertyValue;
    Document doc = new Document();

    try {
      xmlDocumentMappingClass = SWRLOWLUtil.getNamedClass(bridge.getOWLModel(), XMLDocumentMappingOWLClassName);
      xmlDocumentMapping = SWRLOWLUtil.getIndividual(bridge.getOWLModel(), xmlDocumentMappingClass, true, 1);
      propertyValue = SWRLOWLUtil.getObjectPropertyValue(bridge.getOWLModel(), xmlDocumentMapping, 
                                                         XMLDocumentMappingHasRootElementPropertyName, false);

      if (propertyValue == null) throw new XMLBridgeMapperException("no document root element specified");
      else if (propertyValue instanceof edu.stanford.smi.protegex.owl.model.OWLIndividual)
        xmlRootElementMapping = (edu.stanford.smi.protegex.owl.model.OWLIndividual)propertyValue;
      else throw new XMLBridgeMapperException("invalid document root element '" + propertyValue + "'");

      xmlElementMapping2Element(doc, bridge, xmlRootElementMapping, null);
    } catch (SWRLOWLUtilException e) {
      throw new XMLBridgeMapperException("error mapping OWL XML ontology to Document: " + e.getMessage());
    } // try

    return doc;
  } // xmlDocumentMapping2Document

  private void xmlElementMapping2Element(Document doc, SWRLBuiltInBridge bridge, edu.stanford.smi.protegex.owl.model.OWLIndividual xmlElementMapping, Element parentElement) 
    throws XMLBridgeMapperException, SWRLOWLUtilException
  {
    String elementName = SWRLOWLUtil.getDatavaluedPropertyValueAsString(bridge.getOWLModel(), xmlElementMapping, XMLMappingHasNamePropertyName, true);
    String elementNamespacePrefix = SWRLOWLUtil.getDatavaluedPropertyValueAsString(bridge.getOWLModel(), xmlElementMapping, XMLMappingHasNamespacePrefixPropertyName, false, "");
    String elementNamespaceURI = SWRLOWLUtil.getDatavaluedPropertyValueAsString(bridge.getOWLModel(), xmlElementMapping, XMLMappingHasNamespaceURIPropertyName, false, "");
    Set<edu.stanford.smi.protegex.owl.model.RDFResource> attributes = SWRLOWLUtil.getObjectPropertyValues(bridge.getOWLModel(), xmlElementMapping, XMLElementMappingHasAttributesPropertyName, false);
    Set<edu.stanford.smi.protegex.owl.model.RDFResource> subElements = SWRLOWLUtil.getObjectPropertyValues(bridge.getOWLModel(), xmlElementMapping, XMLElementMappingHasSubElementsPropertyName, false);
    String content = SWRLOWLUtil.getDatavaluedPropertyValueAsString(bridge.getOWLModel(), xmlElementMapping, XMLElementMappingHasContentPropertyName, false, "");
    Element element = createElement(doc, parentElement, elementName);

    element.setNamespace(Namespace.getNamespace(elementNamespacePrefix, elementNamespaceURI));

    if (content != null) element.addContent(content);

    for (edu.stanford.smi.protegex.owl.model.RDFResource value : subElements) {
      if (value instanceof edu.stanford.smi.protegex.owl.model.OWLIndividual) {
        edu.stanford.smi.protegex.owl.model.OWLIndividual xmlSubElementMapping = (edu.stanford.smi.protegex.owl.model.OWLIndividual)value;
        xmlElementMapping2Element(doc, bridge, xmlSubElementMapping, element);
      } //if
    } // for

    for (edu.stanford.smi.protegex.owl.model.RDFResource value : attributes) {
      if (value instanceof edu.stanford.smi.protegex.owl.model.OWLIndividual) {
        edu.stanford.smi.protegex.owl.model.OWLIndividual xmlAttributeMapping = (edu.stanford.smi.protegex.owl.model.OWLIndividual)value;
        xmlAttributeMapping2Attribute(doc, bridge, xmlAttributeMapping, element);
      } // if
  } // for
  } // xmlElementMapping2Element

  private void xmlAttributeMapping2Attribute(Document doc, SWRLBuiltInBridge bridge, edu.stanford.smi.protegex.owl.model.OWLIndividual xmlAttributeMapping, Element element) 
    throws XMLBridgeMapperException, SWRLOWLUtilException
  {
    String attributeName = SWRLOWLUtil.getDatavaluedPropertyValueAsString(bridge.getOWLModel(), xmlAttributeMapping, XMLMappingHasNamePropertyName, true);
    String attributeValue = SWRLOWLUtil.getDatavaluedPropertyValueAsString(bridge.getOWLModel(), xmlAttributeMapping, XMLAttributeMappingHasValuePropertyName, true);
    String attributeNamespacePrefix = SWRLOWLUtil.getDatavaluedPropertyValueAsString(bridge.getOWLModel(), xmlAttributeMapping, XMLMappingHasNamespacePrefixPropertyName, false, "");
    String attributeNamespaceURI = SWRLOWLUtil.getDatavaluedPropertyValueAsString(bridge.getOWLModel(), xmlAttributeMapping, XMLMappingHasNamespaceURIPropertyName, false, "");

    setAttribute(element, attributeName, attributeValue, attributeNamespacePrefix, attributeNamespaceURI);
  } // xmlAttributeMapping2Attribute

  public OWLIndividual document2XMLDocumentMapping(Document doc, SWRLBuiltInBridge bridge) throws XMLBridgeMapperException
  {
    Element rootElement = doc.getRootElement();
    OWLIndividual xmlDocumentMapping;

    if (isSchema(rootElement)) throw new XMLBridgeMapperException("not expecting 'schema' root element");

    try {
      xmlDocumentMapping = bridge.injectOWLIndividualOfClass(owlDataFactory.getOWLClass(XMLDocumentMappingOWLClassName));
      element2XMLElementMapping(doc, bridge, xmlDocumentMapping, null, rootElement);
    } catch (SWRLBuiltInBridgeException e) {
      throw new XMLBridgeMapperException("bridge error mapping Document to OWL XML ontology: " + e.getMessage());
    } catch (OWLFactoryException e) {
      throw new XMLBridgeMapperException("OWL factory error mapping Document to OWL XML ontology: " + e.getMessage());
    } // try

    return xmlDocumentMapping;
  } // document2XMLDocumentMapping

  private void element2XMLElementMapping(Document doc, SWRLBuiltInBridge bridge, OWLIndividual xmlDocumentMapping, 
                                         OWLIndividual parentElementMapping, Element element) 
    throws XMLBridgeMapperException, SWRLBuiltInBridgeException, OWLFactoryException
  {
    OWLIndividual elementMapping = bridge.injectOWLIndividualOfClass(owlDataFactory.getOWLClass(XMLElementMappingOWLClassName));
    String elementName = element.getName();
    String elementNamespacePrefix = element.getNamespace().getPrefix();
    String elementNamespaceURI = element.getNamespace().getURI();
    Filter textFilter = new ContentFilter(ContentFilter.TEXT);
    String content = "";

    if (parentElementMapping == null)
      bridge.injectOWLAxiom(owlDataFactory.getOWLObjectPropertyAssertionAxiom(xmlDocumentMapping, rootElementProperty, elementMapping));
    else 
      bridge.injectOWLAxiom(owlDataFactory.getOWLObjectPropertyAssertionAxiom(parentElementMapping, subElementsProperty, elementMapping));

    bridge.injectOWLAxiom(owlDataFactory.getOWLObjectPropertyAssertionAxiom(xmlDocumentMapping, elementsProperty, elementMapping));

    bridge.injectOWLAxiom(owlDataFactory.getOWLDataPropertyAssertionAxiom(elementMapping, nameProperty, 
                                                                          owlDataValueFactory.getOWLDataValue(elementName)));

    bridge.injectOWLAxiom(owlDataFactory.getOWLDataPropertyAssertionAxiom(elementMapping, namespacePrefixProperty, 
                                                                          owlDataValueFactory.getOWLDataValue(elementNamespacePrefix)));

    bridge.injectOWLAxiom(owlDataFactory.getOWLDataPropertyAssertionAxiom(elementMapping, namespaceURIProperty, 
                                                                          owlDataValueFactory.getOWLDataValue(elementNamespaceURI)));
 
    for (Object o : element.getContent(textFilter)) {
      Text text = (Text)o;
      content += text.getValue();
    } // for
    
    bridge.injectOWLAxiom(owlDataFactory.getOWLDataPropertyAssertionAxiom(elementMapping, contentProperty, 
                                                                          owlDataValueFactory.getOWLDataValue(content)));

    for (Attribute attribute : getAttributes(element)) attribute2XMLAttributeMapping(doc, bridge, elementMapping, attribute);

    for (Element subElement : getSubElements(element)) element2XMLElementMapping(doc, bridge, xmlDocumentMapping, elementMapping, subElement);
  } // element2XMLElementMapping

  private void attribute2XMLAttributeMapping(Document doc, SWRLBuiltInBridge bridge, OWLIndividual elementMapping, Attribute attribute) 
    throws XMLBridgeMapperException, SWRLBuiltInBridgeException, OWLFactoryException
  {
    OWLIndividual attributeMapping = bridge.injectOWLIndividualOfClass(owlDataFactory.getOWLClass(XMLAttributeMappingOWLClassName));
    String attributeName = attribute.getName();
    String attributeValue = attribute.getValue();
    String attributeNamespacePrefix = attribute.getNamespace().getPrefix();
    String attributeNamespaceURI = attribute.getNamespace().getURI();
    
    bridge.injectOWLAxiom(owlDataFactory.getOWLDataPropertyAssertionAxiom(attributeMapping, nameProperty, owlDataValueFactory.getOWLDataValue(attributeName)));
    
    bridge.injectOWLAxiom(owlDataFactory.getOWLDataPropertyAssertionAxiom(attributeMapping, valueProperty, owlDataValueFactory.getOWLDataValue(attributeValue)));

    bridge.injectOWLAxiom(owlDataFactory.getOWLDataPropertyAssertionAxiom(attributeMapping, namespacePrefixProperty,
                                                                          owlDataValueFactory.getOWLDataValue(attributeNamespacePrefix)));

    bridge.injectOWLAxiom(owlDataFactory.getOWLDataPropertyAssertionAxiom(attributeMapping, namespaceURIProperty,
                                                                          owlDataValueFactory.getOWLDataValue(attributeNamespaceURI)));

    bridge.injectOWLAxiom(owlDataFactory.getOWLObjectPropertyAssertionAxiom(elementMapping, attributesProperty, attributeMapping));
  } // attribute2XMLAttributeMapping

  private Element createElement(Document doc, Element parentElement, String elementName)
  {
    Element element = new Element(elementName);

    if (parentElement == null) doc.setRootElement(element);
    else parentElement.addContent(element);

    return element;
  } // createElement

  private void setAttribute(Element element, String attributeName, String attributeValue, String namespacePrefix, String namespaceURI)
  {
    Attribute attribute = new Attribute(attributeName, attributeValue, Namespace.getNamespace(namespacePrefix, namespaceURI));

    element.setAttribute(attribute);
  } // setAttribute

  private List<Attribute> getAttributes(Element element) throws XMLBridgeMapperException 
  {
    return new ArrayList<Attribute>(element.getAttributes()); 
  } // getAttributes

  private List<Element> getSubElements(Element element) throws XMLBridgeMapperException 
  { 
    List<Element> result = new ArrayList<Element>();

    for (Object o : element.getChildren()) if (o instanceof Element) result.add((Element)o);

    return result;
  } // getSubElements

  // Methods below this point are not used yet - they are for future support of more complex XML and XML Schema documents.

  private boolean isSchema(Element element) { return hasName(element, "schema"); }
  private boolean isElement(Element element) { return hasName(element, "element"); }
  public boolean isAll(Element element) { return hasName(element, "all"); }
  public boolean isComplexType(Element element) { return hasName(element, "complexType"); }
  public boolean isSequence(Element element) { return hasName(element, "sequence"); }
  public boolean isGroup(Element element) { return hasName(element, "group"); }
  public boolean isAttributeGroup(Element element) { return hasName(element, "attributeGroup"); }
  public boolean isChoice(Element element) { return hasName(element, "choice"); }
  public boolean isAny(Element element) { return hasName(element, "any"); }
  public boolean isAnyAttribute(Element element) { return hasName(element, "anyAttribute"); }
  public boolean isAttribute(Element element) { return hasName(element, "attribute"); }
  public boolean isComplexContent(Element element) { return hasName(element, "complexContent"); }
  public boolean isSimpleContent(Element element) { return hasName(element, "simpleContent"); }
  public boolean isSimpleContext(Element element) { return hasName(element, "simpleContext"); }
  public boolean isSimpleType(Element element) { return hasName(element, "simpleType"); }
  public boolean isRefElement(Element element) { return isElement(element) && hasRefAttribute(element); }

  public String getNameAttribute(Element element) throws XMLBridgeMapperException { return getNameAttributeValue(element); }
  public String getReafAttribute(Element element) throws XMLBridgeMapperException { return getRefAttributeValue(element); }
  public String getTypeAttribute(Element element) throws XMLBridgeMapperException { return getTypeAttributeValue(element); }
  public String getUseAttribute(Element element) throws XMLBridgeMapperException { return getUseAttributeValue(element); }
  public String getMinOccursAttribute(Element element) throws XMLBridgeMapperException { return getMinOccursAttributeValue(element); }
  public String getMaxOccursAttribute(Element element) throws XMLBridgeMapperException { return getMaxOccursAttributeValue(element); }

  public boolean hasNameAttribute(Element element) { return hasAttribute(element, "name"); }
  public boolean hasValueAttribute(Element element) { return hasAttribute(element, "value"); }
  public boolean hasBaseAttribute(Element element) { return hasAttribute(element, "base"); }
  public boolean hasMixedAttribute(Element element) { return hasAttribute(element, "mixed"); }
  public boolean hasTypeAttribute(Element element) { return hasAttribute(element, "type"); }
  public boolean hasDefaultAttribute(Element element) { return hasAttribute(element, "default"); }
  public boolean hasFixedAttribute(Element element) { return hasAttribute(element, "fixed"); }
  public boolean hasRefAttribute(Element element) { return hasAttribute(element, "ref"); }
  public boolean hasUseAttribute(Element element) { return hasAttribute(element, "use"); }
  public boolean hasMaxOccursAttribute(Element element) { return hasAttribute(element, "maxOccurs"); }
  public boolean hasMinOccursAttribute(Element element) { return hasAttribute(element, "minOccurs"); }

  public String getNameAttributeValue(Element element) throws XMLBridgeMapperException { return getAttributeValue(element, "name"); }
  public String getValueAttributeValue(Element element) throws XMLBridgeMapperException  { return getAttributeValue(element, "value"); }
  public String getMixedAttributeValue(Element element) throws XMLBridgeMapperException  { return getAttributeValue(element, "mixed"); }
  public String getBaseAttributeValue(Element element) throws XMLBridgeMapperException  { return getAttributeValue(element, "base"); }
  public String getTypeAttributeValue(Element element) throws XMLBridgeMapperException  { return getAttributeValue(element, "type"); }
  public String getDefaultAttributeValue(Element element) throws XMLBridgeMapperException  { return getAttributeValue(element, "default"); }
  public String getFixedAttributeValue(Element element) throws XMLBridgeMapperException  { return getAttributeValue(element, "fixed"); }
  private String getRefAttributeValue(Element element) throws XMLBridgeMapperException { return getAttributeValue(element, "ref"); }
  private String getUseAttributeValue(Element element) throws XMLBridgeMapperException { return getAttributeValue(element, "use"); }
  private String getMaxOccursAttributeValue(Element element) throws XMLBridgeMapperException { return getAttributeValue(element, "maxOccurs"); }
  private String getMinOccursAttributeValue(Element element) throws XMLBridgeMapperException { return getAttributeValue(element, "minOccurs"); }

  private Element getComplexTypeChild(Element element) throws XMLBridgeMapperException
  {
    if (!hasComplexTypeChild(element))
      throw new XMLBridgeMapperException("expecting complexType child for element '" + getNameAttributeValue(element) + "'");
    return element.getChild("complexType", element.getNamespace());
  } // getComplexTypeChild

  private int count(List l, Object o) 
  {
    int result = 0, i = l.indexOf(o);
    
    if (i != -1)
      for (; i < l.size(); i++) 
        if (l.get(i) == o) result++;

    return result;
  } // count

  private boolean hasAttribute(Element element, String attributeName) 
  { 
    return element.getAttributeValue(attributeName) != null;
  } // hasAttribute
  
  private String getAttributeValue(Element element, String attributeName) throws XMLBridgeMapperException
  {
    if (!hasAttribute(element, attributeName))
      throw new XMLBridgeMapperException("no " + attributeName + " attribute found in element " + element.getName());

    return element.getAttributeValue(attributeName);
  } // getAttributeValue

  private Element getFirstChild(Element element) throws XMLBridgeMapperException
  {
    if (element.getChildren() == null) 
      throw new XMLBridgeMapperException("getFirstChild called on non-parent element '" + getNameAttributeValue(element) + "'");

    return (Element)element.getChildren().get(0);
  } // getFirstChild
  private boolean hasName(Element element, String name) { return element.getName() != null && element.getName().equals(name); }

  private boolean hasChildren(Element element) { return element.getChildren() != null; }

  private boolean hasComplexTypeChild(Element element) 
  { 
    return isElement(element) && element.getChild("complexType", element.getNamespace()) != null; 
  } // hasComplexTypeChild

  private boolean hasSimpleTypeChild(Element element) 
  { 
    return isElement(element) && element.getChild("simpleTypeChild", element.getNamespace()) != null; 
  } // hasSimpleTypeChild

} // XMLBridgeMapper
