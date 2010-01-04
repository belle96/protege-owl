package edu.stanford.smi.protegex.owl.writer.rdfxml.rdfwriter;

import edu.stanford.smi.protegex.owl.writer.xml.XMLWriter;

import java.io.IOException;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Apr 7, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public interface RDFXMLContentWriter {

    public void writeContent(XMLWriter writer) throws IOException;
}
