
package edu.stanford.smi.protegex.owl.swrl.bridge.builtins.swrlxml;

import java.net.URI;
import java.net.URISyntaxException;
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

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.util.ImportHelper;
import edu.stanford.smi.protegex.owl.swrl.exceptions.SWRLOWLUtilException;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLNames;
import edu.stanford.smi.protegex.owl.swrl.util.SWRLOWLUtil;

public class XMLMapper
{
  public static final String XMLAlias = "swrlxml:";

  public static final String XMLDocumentOWLClassName = XMLAlias + "XMLDocument";
  public static final String XMLDocumentHasRootElementPropertyName = XMLAlias + "hasRootElement";
  public static final String XMLDocumentHasElementsPropertyName = XMLAlias + "hasElements";

  public static final String XMLElementOWLClassName = XMLAlias + "XMLElement";
  public static final String XMLHasNamePropertyName = XMLAlias + "hasName";
  public static final String XMLHasNamespacePrefixPropertyName = XMLAlias + "hasNamespacePrefix";
  public static final String XMLHasNamespaceURIPropertyName = XMLAlias + "hasNamespaceURI";
  public static final String XMLElementHasMappedIndividualsPropertyName = XMLAlias + "hasMappedIndividuals";
  public static final String XMLElementHasSubElementsPropertyName = XMLAlias + "hasSubElements";
  public static final String XMLElementHasAttributesPropertyName = XMLAlias + "hasAttributes";
  public static final String XMLElementHasContentPropertyName = XMLAlias + "hasContent";

  public static final String XMLAttributeOWLClassName = XMLAlias + "XMLAttribute";
  public static final String XMLAttributeHasValuePropertyName = XMLAlias + "hasValue";

  private OWLModel owlModel;

  private OWLProperty rootElementProperty, elementsProperty, subElementsProperty, nameProperty, namespacePrefixProperty, 
    namespaceURIProperty, contentProperty, valueProperty, attributesProperty;

  public XMLMapper(OWLModel owlModel)
  {
    this.owlModel = owlModel;

    rootElementProperty = SWRLOWLUtil.createOWLObjectProperty(owlModel, XMLDocumentHasRootElementPropertyName);
    elementsProperty = SWRLOWLUtil.createOWLObjectProperty(owlModel, XMLDocumentHasElementsPropertyName);
    nameProperty = SWRLOWLUtil.createOWLDatatypeProperty(owlModel, XMLHasNamePropertyName);
    namespacePrefixProperty = SWRLOWLUtil.createOWLDatatypeProperty(owlModel, XMLHasNamespacePrefixPropertyName);
    namespaceURIProperty = SWRLOWLUtil.createOWLDatatypeProperty(owlModel, XMLHasNamespaceURIPropertyName);
    contentProperty = SWRLOWLUtil.createOWLDatatypeProperty(owlModel, XMLElementHasContentPropertyName);
    subElementsProperty = SWRLOWLUtil.createOWLObjectProperty(owlModel, XMLElementHasSubElementsPropertyName);
    valueProperty = SWRLOWLUtil.createOWLDatatypeProperty(owlModel, XMLAttributeHasValuePropertyName);
    attributesProperty = SWRLOWLUtil.createOWLObjectProperty(owlModel, XMLElementHasAttributesPropertyName);
  } // XMLMapper

  public Document xmlDocument2Document() throws XMLMapperException
  {
    OWLNamedClass xmlDocumentClass;
    OWLIndividual xmlDocument, xmlRootElement = null;
    RDFResource propertyValue;
    Document doc = new Document();

    try {
      xmlDocumentClass = SWRLOWLUtil.getNamedClass(owlModel, XMLDocumentOWLClassName);
      xmlDocument = SWRLOWLUtil.getIndividual(owlModel, xmlDocumentClass, true, 1);
      propertyValue = SWRLOWLUtil.getObjectPropertyValue(owlModel, xmlDocument, XMLDocumentHasRootElementPropertyName, false);

      if (propertyValue == null) throw new XMLMapperException("no document root element specified");
      else if (propertyValue instanceof OWLIndividual) xmlRootElement = (OWLIndividual)propertyValue;
      else throw new XMLMapperException("invalid document root element " + propertyValue);

      xmlElement2Element(doc, xmlRootElement, null);
    } catch (SWRLOWLUtilException e) {
      throw new XMLMapperException("error mapping OWL XML ontology to Document: " + e.getMessage());
    } // try

    return doc;
  } // xmlDocument2Document

  private void xmlElement2Element(Document doc, OWLIndividual xmlElement, Element parentElement) 
    throws XMLMapperException, SWRLOWLUtilException
  {
    String elementName = SWRLOWLUtil.getDatavaluedPropertyValueAsString(owlModel, xmlElement, XMLHasNamePropertyName, true);
    String elementNamespacePrefix = SWRLOWLUtil.getDatavaluedPropertyValueAsString(owlModel, xmlElement, XMLHasNamespacePrefixPropertyName, false, "");
    String elementNamespaceURI = SWRLOWLUtil.getDatavaluedPropertyValueAsString(owlModel, xmlElement, XMLHasNamespaceURIPropertyName, false, "");
    Set<RDFResource> attributes = SWRLOWLUtil.getObjectPropertyValues(owlModel, xmlElement, XMLElementHasAttributesPropertyName, false);
    Set<RDFResource> subElements = SWRLOWLUtil.getObjectPropertyValues(owlModel, xmlElement, XMLElementHasSubElementsPropertyName, false);
    String content = SWRLOWLUtil.getDatavaluedPropertyValueAsString(owlModel, xmlElement, XMLElementHasContentPropertyName, false, "");
    Element element = createElement(doc, parentElement, elementName);

    element.setNamespace(Namespace.getNamespace(elementNamespacePrefix, elementNamespaceURI));

    if (content != null) element.addContent(content);

    for (RDFResource value : subElements) {
      if (value instanceof OWLIndividual) {
        OWLIndividual xmlSubElement = (OWLIndividual)value;
        xmlElement2Element(doc, xmlSubElement, element);
      } //if
    } // for

    for (RDFResource value : attributes) {
      if (value instanceof OWLIndividual) {
        OWLIndividual xmlAttribute = (OWLIndividual)value;
        xmlAttribute2Attribute(doc, xmlAttribute, element);
      } // if
  } // for
  } // xmlElement2Element

  private void xmlAttribute2Attribute(Document doc, OWLIndividual xmlAttribute, Element element) 
    throws XMLMapperException, SWRLOWLUtilException
  {
    String attributeName = SWRLOWLUtil.getDatavaluedPropertyValueAsString(owlModel, xmlAttribute, XMLHasNamePropertyName, true);
    String attributeValue = SWRLOWLUtil.getDatavaluedPropertyValueAsString(owlModel, xmlAttribute, XMLAttributeHasValuePropertyName, true);
    String attributeNamespacePrefix = SWRLOWLUtil.getDatavaluedPropertyValueAsString(owlModel, xmlAttribute, XMLHasNamespacePrefixPropertyName, false, "");
    String attributeNamespaceURI = SWRLOWLUtil.getDatavaluedPropertyValueAsString(owlModel, xmlAttribute, XMLHasNamespaceURIPropertyName, false, "");

    setAttribute(element, attributeName, attributeValue, attributeNamespacePrefix, attributeNamespaceURI);
  } // xmlAttribute2Attribute

  public OWLIndividual document2XMLDocument(Document doc) throws XMLMapperException
  {
    Element rootElement = doc.getRootElement();
    OWLIndividual xmlDocument;

    if (isSchema(rootElement)) throw new XMLMapperException("not expecting schema root element");

    addSWRLXMLImport();

    try {
      xmlDocument = SWRLOWLUtil.createIndividualOfClass(owlModel, XMLDocumentOWLClassName);
      element2XMLElement(doc, xmlDocument, null, rootElement);
    } catch (SWRLOWLUtilException e) {
      throw new XMLMapperException("error mapping Document to OWL XML ontology: " + e.getMessage());
    } // try

    return xmlDocument;
  } // document2XMLDocument

  private void element2XMLElement(Document doc, OWLIndividual xmlDocument, OWLIndividual parentXMLElement, Element element) 
    throws XMLMapperException, SWRLOWLUtilException
  {
    OWLIndividual xmlElement = SWRLOWLUtil.createIndividualOfClass(owlModel, XMLElementOWLClassName);
    String elementName = element.getName();
    String elementNamespacePrefix = element.getNamespace().getPrefix();
    String elementNamespaceURI = element.getNamespace().getURI();
    Filter textFilter = new ContentFilter(ContentFilter.TEXT);
    String content = "";

    if (parentXMLElement == null) SWRLOWLUtil.addPropertyValue(owlModel, xmlDocument, rootElementProperty, xmlElement);
    else SWRLOWLUtil.addPropertyValue(owlModel, parentXMLElement, subElementsProperty, xmlElement);

    SWRLOWLUtil.addPropertyValue(owlModel, xmlDocument, elementsProperty, xmlElement);
    SWRLOWLUtil.addPropertyValue(owlModel, xmlElement, nameProperty, elementName);
    SWRLOWLUtil.addPropertyValue(owlModel, xmlElement, namespacePrefixProperty, elementNamespacePrefix);
    SWRLOWLUtil.addPropertyValue(owlModel, xmlElement, namespaceURIProperty, elementNamespaceURI);
 
    for (Object o : element.getContent(textFilter)) {
      Text text = (Text)o;
      content += text.getValue();
    } // for

    SWRLOWLUtil.addPropertyValue(owlModel, xmlElement, contentProperty, content);

    for (Attribute attribute : getAttributes(element)) attribute2XMLAttribute(doc, xmlElement, attribute);

    for (Element subElement : getSubElements(element)) element2XMLElement(doc, xmlDocument, xmlElement, subElement);
  } // element2XMLElement

  private void attribute2XMLAttribute(Document doc, OWLIndividual xmlElement, Attribute attribute) 
    throws XMLMapperException, SWRLOWLUtilException
  {
    OWLIndividual xmlAttribute = SWRLOWLUtil.createIndividualOfClass(owlModel, XMLAttributeOWLClassName);
    String attributeName = attribute.getName();
    String attributeValue = attribute.getValue();
    String attributeNamespacePrefix = attribute.getNamespace().getPrefix();
    String attributeNamespaceURI = attribute.getNamespace().getURI();
    
    SWRLOWLUtil.addPropertyValue(owlModel, xmlAttribute, nameProperty, attributeName);
    SWRLOWLUtil.addPropertyValue(owlModel, xmlAttribute, valueProperty, attributeValue);
    SWRLOWLUtil.addPropertyValue(owlModel, xmlAttribute, namespacePrefixProperty, attributeNamespacePrefix);
    SWRLOWLUtil.addPropertyValue(owlModel, xmlAttribute, namespaceURIProperty, attributeNamespaceURI);
    SWRLOWLUtil.addPropertyValue(owlModel, xmlElement, attributesProperty, xmlAttribute);

    System.err.print("(" + attributeName + ", value: " + attributeValue + ")");
  } // attribute2XMLAttribute

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

  private List<Attribute> getAttributes(Element element) throws XMLMapperException 
  {
    return new ArrayList<Attribute>(element.getAttributes()); 
  } // getAttributes

  private List<Element> getSubElements(Element element) throws XMLMapperException 
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

  public String getNameAttribute(Element element) throws XMLMapperException { return getNameAttributeValue(element); }
  public String getReafAttribute(Element element) throws XMLMapperException { return getRefAttributeValue(element); }
  public String getTypeAttribute(Element element) throws XMLMapperException { return getTypeAttributeValue(element); }
  public String getUseAttribute(Element element) throws XMLMapperException { return getUseAttributeValue(element); }
  public String getMinOccursAttribute(Element element) throws XMLMapperException { return getMinOccursAttributeValue(element); }
  public String getMaxOccursAttribute(Element element) throws XMLMapperException { return getMaxOccursAttributeValue(element); }

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

  public String getNameAttributeValue(Element element) throws XMLMapperException { return getAttributeValue(element, "name"); }
  public String getValueAttributeValue(Element element) throws XMLMapperException  { return getAttributeValue(element, "value"); }
  public String getMixedAttributeValue(Element element) throws XMLMapperException  { return getAttributeValue(element, "mixed"); }
  public String getBaseAttributeValue(Element element) throws XMLMapperException  { return getAttributeValue(element, "base"); }
  public String getTypeAttributeValue(Element element) throws XMLMapperException  { return getAttributeValue(element, "type"); }
  public String getDefaultAttributeValue(Element element) throws XMLMapperException  { return getAttributeValue(element, "default"); }
  public String getFixedAttributeValue(Element element) throws XMLMapperException  { return getAttributeValue(element, "fixed"); }
  public String getRefAttributeValue(Element element) throws XMLMapperException { return getAttributeValue(element, "ref"); }
  public String getUseAttributeValue(Element element) throws XMLMapperException { return getAttributeValue(element, "use"); }
  public String getMaxOccursAttributeValue(Element element) throws XMLMapperException { return getAttributeValue(element, "maxOccurs"); }
  public String getMinOccursAttributeValue(Element element) throws XMLMapperException { return getAttributeValue(element, "minOccurs"); }

  public Element getComplexTypeChild(Element element) throws XMLMapperException
  {
    if (!hasComplexTypeChild(element))
      throw new XMLMapperException("expecting complexType child for element " + getNameAttributeValue(element));
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
  
  private String getAttributeValue(Element element, String attributeName) throws XMLMapperException
  {
    if (!hasAttribute(element, attributeName))
      throw new XMLMapperException("no " + attributeName + " attribute found in element " + element.getName());

    return element.getAttributeValue(attributeName);
  } // getAttributeValue

  public Element getFirstChild(Element element) throws XMLMapperException
  {
    if (element.getChildren() == null) 
      throw new XMLMapperException("getFirstChild called on non-parent element " + getNameAttributeValue(element));

    return (Element)element.getChildren().get(0);
  } // getFirstChild
  private boolean hasName(Element element, String name) { return element.getName() != null && element.getName().equals(name); }

  public boolean hasChildren(Element element) { return element.getChildren() != null; }

  private boolean hasComplexTypeChild(Element element) 
  { 
    return isElement(element) && element.getChild("complexType", element.getNamespace()) != null; 
  } // hasComplexTypeChild

  public boolean hasSimpleTypeChild(Element element) 
  { 
    return isElement(element) && element.getChild("simpleTypeChild", element.getNamespace()) != null; 
  } // hasSimpleTypeChild

  private void addSWRLXMLImport() throws XMLMapperException
  {
    ImportHelper importHelper = new ImportHelper(owlModel);

    try {
      owlModel.getNamespaceManager().setPrefix(new URI(SWRLNames.SWRLXML_NAMESPACE), SWRLNames.SWRLXML_PREFIX);
      
      if  (owlModel.getTripleStoreModel().getTripleStore(SWRLNames.SWRLXML_IMPORT) == null) 
        importHelper.addImport(new URI(SWRLNames.SWRLXML_IMPORT));

      importHelper.importOntologies(false);
    } catch (URISyntaxException e) {
      throw new XMLMapperException("error importing SWRLXML ontology: " + e.getMessage());
    } catch (OntologyLoadException e) {
      throw new XMLMapperException("error loading SWRLXML ontology: " + e.getMessage());
    } // try

  } // addSWRLXMLImport

} // XMLMapper
