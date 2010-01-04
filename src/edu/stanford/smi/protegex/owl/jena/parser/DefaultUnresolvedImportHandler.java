package edu.stanford.smi.protegex.owl.jena.parser;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.triplestore.TripleStore;
import edu.stanford.smi.protegex.owl.repository.Repository;

import java.net.URI;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 21, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class DefaultUnresolvedImportHandler implements UnresolvedImportHandler {

    public Repository handleUnresolvableImport(OWLModel model,
                                               TripleStore tripleStore,
                                               URI ontologyName) {
        // Don't deal with the import
        return null;
    }
}

