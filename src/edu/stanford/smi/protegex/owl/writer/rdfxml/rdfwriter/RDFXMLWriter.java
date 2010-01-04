package edu.stanford.smi.protegex.owl.writer.rdfxml.rdfwriter;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.triplestore.TripleStore;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Collections;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Oct 10, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class RDFXMLWriter {

    public static void writeAxioms(RDFResource resource, Writer writer) throws IOException {
        OWLModel model = resource.getOWLModel();
        Collection c = Collections.singleton(resource);
        writeAxioms(model, c, writer);
    }


    public static void writeAxioms(OWLModel model, Collection resources, Writer writer) throws IOException {
        TripleStore ts = model.getTripleStoreModel().getActiveTripleStore();
        RDFResourceCollectionWriter w = new RDFResourceCollectionWriter(model, ts, resources, writer, true);
        w.write();
    }
}

