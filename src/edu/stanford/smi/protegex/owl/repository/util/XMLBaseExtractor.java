package edu.stanford.smi.protegex.owl.repository.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;

import org.apache.xerces.parsers.SAXParser;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protege.util.URIUtilities;
import edu.stanford.smi.protegex.owl.jena.parser.ProtegeOWLParser;
import edu.stanford.smi.protegex.owl.model.factory.FactoryUtils;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 21, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class XMLBaseExtractor {

    private InputStream is;

    private URI xmlBase;

    private String rootElementName;

    private String defaultNamespace;


    public XMLBaseExtractor(InputStream is) {
        this.is = is;
        this.xmlBase = null;
    }
    
    public static URI getXMLBase(InputStream is) {
        XMLBaseExtractor xmlBaseExtractor = new XMLBaseExtractor(is);
        return xmlBaseExtractor.getXMLBase();
    }
    
    public static URI getXMLBase(String ontologyName) throws MalformedURLException, OntologyLoadException {
        URI ontologyURI = URIUtilities.createURI(ontologyName);             
        InputStream is = ProtegeOWLParser.getInputStream(ontologyURI.toURL());
        return getXMLBase(is);
    }


    public URI getXMLBase() {
        SAXParser parser = new SAXParser();
        parser.setContentHandler(new MyHandler());
        try {
            parser.parse(new InputSource(is));
        }
        catch (Throwable t) {
          Log.emptyCatchBlock(t);
        }
        return xmlBase;
    }


    public String getRootElementName() {
        return rootElementName;
    }


    public String getDefaultNamespace() {
        return defaultNamespace;
    }


    private class MyHandler implements ContentHandler {

        private boolean startElement;


        public void setDocumentLocator(Locator locator) {
        }


        public void startDocument()
                throws SAXException {
        }


        public void endDocument()
                throws SAXException {
        }


        public void startPrefixMapping(String prefix,
                                       String uri)
                throws SAXException {
        	
        	if (prefix == null || prefix.equals("")) {
        		defaultNamespace = uri;
        	}
        	
        }


        public void endPrefixMapping(String prefix)
                throws SAXException {
        }


        public void startElement(String namespaceURI,
                                 String localName,
                                 String qName,
                                 Attributes atts)
                throws SAXException {
            if (startElement == false) {
                rootElementName = qName;
                for (int i = 0; i < atts.getLength(); i++) {                	
                    if (atts.getQName(i).equals("xml:base")) {
                        URI attURL = null;
                        try {
                            attURL = new URI(FactoryUtils.adjustOntologyName(atts.getValue(i)));
                        }
                        catch (URISyntaxException e) {
                            Log.getLogger().log(Level.SEVERE, "Exception caught", e);
                        }
                        xmlBase = attURL;
                    }           
                }
                startElement = true;
           }   
            else {
                throw new SAXException("No xml:base");
            }
            
        }


        public void endElement(String namespaceURI,
                               String localName,
                               String qName)
                throws SAXException {
        }


        public void characters(char ch[],
                               int start,
                               int length)
                throws SAXException {
        }


        public void ignorableWhitespace(char ch[],
                                        int start,
                                        int length)
                throws SAXException {
        }


        public void processingInstruction(String target,
                                          String data)
                throws SAXException {
        }


        public void skippedEntity(String name)
                throws SAXException {
        }
    }
}

