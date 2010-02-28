package edu.stanford.smi.protegex.owl.writer.rdfxml.rdfwriter;

import java.io.Writer;
import java.util.Comparator;

import edu.stanford.smi.protege.ui.FrameComparator;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.triplestore.TripleStore;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Oct 8, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class OWLModelOrderedWriter extends OWLModelWriter {

    public OWLModelOrderedWriter(OWLModel model, TripleStore tripleStore, Writer writer) {
        super(model, tripleStore, writer);
    }


    protected RDFXMLContentWriter getContentWriter(OWLModel model,
                                                   TripleStore tripleStore) {
        return new OWLModelOrderedContentWriter(model, tripleStore, new FrameComparator<RDFResource>());
    }
}

