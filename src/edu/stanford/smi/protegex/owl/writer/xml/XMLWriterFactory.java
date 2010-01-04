package edu.stanford.smi.protegex.owl.writer.xml;

import java.io.Writer;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Apr 7, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class XMLWriterFactory {

    private static XMLWriterFactory instance;


    private XMLWriterFactory() {

    }


    /**
     * Gets the one and only instance of the <code>XMLWriterFactory</code>
     */
    public static synchronized XMLWriterFactory getInstance() {
        if (instance == null) {
            instance = new XMLWriterFactory();
        }
        return instance;
    }


    /**
     * Creates an XMLWriter.
     *
     * @param writer The <code>Writer</code> that the XMLWriter will actually write to
     */
    public XMLWriter createXMLWriter(Writer writer, XMLWriterNamespaceManager xmlWriterNamespaceManager, String xmlBase) {
        return new DefaultXMLWriter(writer, xmlWriterNamespaceManager, xmlBase);
    }
}

