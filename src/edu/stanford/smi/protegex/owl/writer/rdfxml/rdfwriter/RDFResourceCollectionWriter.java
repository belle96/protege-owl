package edu.stanford.smi.protegex.owl.writer.rdfxml.rdfwriter;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Collections;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.triplestore.TripleStore;
import edu.stanford.smi.protegex.owl.writer.rdfxml.util.Util;
import edu.stanford.smi.protegex.owl.writer.xml.XMLWriter;
import edu.stanford.smi.protegex.owl.writer.xml.XMLWriterFactory;
import edu.stanford.smi.protegex.owl.writer.xml.XMLWriterNamespaceManager;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Apr 7, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class RDFResourceCollectionWriter {

    private RDFResourceCollectionContentWriter collectionContentWriter;

    private boolean enclose;

    private XMLWriter xmlWriter;


    public RDFResourceCollectionWriter(OWLModel model,
                                       TripleStore tripleStore,
                                       Collection resources,
                                       Writer writer,
                                       boolean encloseInRDFElement) {
        String ontologyName = Util.getOntologyName(model, tripleStore);
        String defaultNamespace = model.getNamespaceManager().getDefaultNamespace();
        if (defaultNamespace == null) {
        	defaultNamespace = tripleStore.getName() + "#";
        }
        XMLWriterNamespaceManager nsm = Util.getNamespacePrefixes(model.getNamespaceManager(), defaultNamespace);                
        this.xmlWriter = XMLWriterFactory.getInstance().createXMLWriter(writer, nsm, ontologyName);
        this.enclose = encloseInRDFElement;
        collectionContentWriter = new RDFResourceCollectionContentWriter(resources, tripleStore);
    }


    public RDFResourceCollectionWriter(OWLModel model,
                                       TripleStore tripleStore,
                                       Collection resources,
                                       XMLWriter xmlWriter,
                                       boolean encloseInRDFElement) {
        this.xmlWriter = xmlWriter;
        this.enclose = encloseInRDFElement;
        collectionContentWriter = new RDFResourceCollectionContentWriter(resources, tripleStore);
    }


    public void write()
            throws IOException {
        if (enclose) {
            RDFXMLDocumentWriter docWriter = new RDFXMLDocumentWriter(xmlWriter, Collections.singleton(collectionContentWriter));
            docWriter.writeDocument();
        }
        else {
            collectionContentWriter.writeContent(xmlWriter);
        }
    }
}

