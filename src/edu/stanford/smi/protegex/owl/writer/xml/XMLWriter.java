package edu.stanford.smi.protegex.owl.writer.xml;

import java.io.IOException;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Mar 22, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public interface XMLWriter {

    /**
     * Sets the encoding for the document that the rdfwriter produces.
     * The default encoding is "UTF-8".
     *
     * @param encoding The encoding.
     */
    public void setEncoding(String encoding);


    /**
     * Gets the default namespace that the rdfwriter uses.
     */
    public String getDefaultNamespace();


    /**
     * Gets the rdfwriter's namespace manager.
     */
    public XMLWriterNamespaceManager getNamespacePrefixes();


    public String getXMLBase();


    /**
     * Causes the current element's attributes to be wrapped in the
     * output.
     */
    public void setWrapAttributes(boolean b);


    /**
     * Starts writing the document.  The root element will contain
     * the namespace declarations and xml:base attribute.
     *
     * @param rootElementName The name of the root element.
     */
    public void startDocument(String rootElementName) throws IOException;


    /**
     * Causes all open elements, including the document root
     * element, to be closed.
     */
    public void endDocument() throws IOException;


    /**
     * Writes the start of an element
     *
     * @param name The tag name of the element to be written.
     */
    public void writeStartElement(String name) throws IOException;

    /**
     * Writes the start of an element with namespace and a name
     * 
     * @param namespace The namespace of the element to be written (e.g. http://smi-protege.stanford.edu/ontologes/test.owl#)
     */
    public void writeStartElement(String namespace, String name) throws IOException;

    

    /**
     * Writes the closing tag of the last element to be started.
     */
    public void writeEndElement() throws IOException;


    /**
     * Writes an attribute of the last element to be started (that
     * has not been closed).
     *
     * @param attr The name of the attribute
     * @param val  The value of the attribute
     */
    public void writeAttribute(String attr, String val) throws IOException;


    /**
     * Writes a text element
     * @param text The text to be written
     */
    public void writeTextContent(String text) throws IOException;
}

