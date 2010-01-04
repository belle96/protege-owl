package edu.stanford.smi.protegex.owl.writer.rdfxml.rdfwriter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import edu.stanford.smi.protegex.owl.model.NamespaceUtil;
import edu.stanford.smi.protegex.owl.model.RDFNames;
import edu.stanford.smi.protegex.owl.model.impl.OWLNamespaceManager;
import edu.stanford.smi.protegex.owl.writer.xml.XMLWriter;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Apr 7, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class RDFXMLDocumentWriter {

    private XMLWriter xmlWriter;

    private Collection contentWriters;


    public RDFXMLDocumentWriter(XMLWriter xmlWriter,
                                Collection contentWriters) {
        this.xmlWriter = xmlWriter;
        this.contentWriters = contentWriters;
    }


    public void writeDocument() throws IOException {
        writeDocStart();
        for (Iterator it = contentWriters.iterator(); it.hasNext();) {
            RDFXMLContentWriter contentWriter = (RDFXMLContentWriter) it.next();
            contentWriter.writeContent(xmlWriter);
        }
        writeDocEnd();

    }


    private void writeDocStart() throws IOException {
        xmlWriter.startDocument(RDFNames.RDF);
    }


    private void writeDocEnd() throws IOException {
        xmlWriter.endDocument(); // Close off, finishing with our rdf:RDF element
    }
}

