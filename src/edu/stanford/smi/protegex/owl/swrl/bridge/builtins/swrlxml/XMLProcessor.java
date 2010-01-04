
package edu.stanford.smi.protegex.owl.swrl.bridge.builtins.swrlxml;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class XMLProcessor
{
  private XMLOutputter serializer;

  public XMLProcessor()
  {
    serializer = new XMLOutputter(Format.getPrettyFormat());
  } // XMLProcessor

  /**
   ** Method that writes an XML file from an instance of a Document.
   */
  public void generateXMLFile(Document doc, String outputXMLFileName) throws XMLProcessorException
  {
    OutputStream xmlStream = createOutputXMLStream(outputXMLFileName);

    if ((doc == null) || !doc.hasRootElement()) throw new XMLProcessorException("document is empty");

    try {
      serializer.output(doc, xmlStream);
      xmlStream.close();
    } catch (IOException e) {
      throw new XMLProcessorException("error writing XML file '" + outputXMLFileName + "': " + e.getMessage());
    } // try
  } // generateXMLFile

  /**
   ** Method that writes an XML string from an instance of a Document.
   */
  public String generateXMLString(Document doc) throws XMLProcessorException
  {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    if ((doc == null) || !doc.hasRootElement()) throw new XMLProcessorException("document is empty");

    try {
      serializer.output(doc, outputStream);
    } catch (IOException e) {
      throw new XMLProcessorException("error writing XML string: " + e.getMessage());
    } // try

    return outputStream.toString();
  } // generateXMLString

  /**
   ** Method that reads a simple XML file and generates an instance of a Document from it.
   */
  public Document processXMLStream(String inputXMLStreamName) throws XMLProcessorException
  {
    Document doc = null;
    try {
      SAXBuilder builder = new SAXBuilder();
      InputStream xmlStream = createInputXMLStream(inputXMLStreamName);
      doc = builder.build(xmlStream);
      xmlStream.close();
    } catch (Exception e) {
      throw new XMLProcessorException("error opening XML file '" + inputXMLStreamName + "': " + e.getMessage());
    } // try

    return doc;
  } // processXMLStream

  /**
   ** Method that reads an XML string and generates an instance of a Document from it.
   */
  public Document processXMLString(String inputXMLString) throws XMLProcessorException
  {
    Document doc = null;
    try {
      SAXBuilder builder = new SAXBuilder();
      doc = builder.build(new StringReader(inputXMLString));
    } catch (Exception e) {
      throw new XMLProcessorException("error processing XML string: " + e.getMessage());
    } // try
    
    return doc;
  } // processXMLString 

  private OutputStream createOutputXMLStream(String outputXMLStreamName) throws XMLProcessorException
  {
    OutputStream xmlStream = null;

    try {
      xmlStream = new FileOutputStream(outputXMLStreamName);
    } catch (IOException e) {
      throw new XMLProcessorException("error creating XML serializer for XML stream '" + outputXMLStreamName + "': " + e.getMessage());
    } // try

    return xmlStream;
  } // createOutputXMLStream

  private InputStream createInputXMLStream(String inputXMLStreamName) throws XMLProcessorException
  {
    InputStream xmlStream = null;

    try {
      URL url = new URL(inputXMLStreamName);
      String protocol = url.getProtocol();
      if (protocol.equals("file")) {
        String path = url.getPath();
        xmlStream = new FileInputStream(path);
      } else xmlStream = url.openStream();
    } catch (MalformedURLException e) {
      throw new XMLProcessorException("invalid URL for XML stream '" + inputXMLStreamName + "': " + e.getMessage());
    } catch (IOException e) {
      throw new XMLProcessorException("IO error opening XML stream '" + inputXMLStreamName + "': " + e.getMessage());
    } // try

    return xmlStream;
  } // createOutputXMLStream

} // XMLProcessor
