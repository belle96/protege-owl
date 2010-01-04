package edu.stanford.smi.protegex.owl.model.triplestore;

import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;

/**
 * A DelegatingTripleWriter that only writes those triples that are also in a given TripleStore.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class TripleStoreFilterTripleWriter extends DelegatingTripleWriter {

    private TripleStore tripleStore;


    public TripleStoreFilterTripleWriter(TripleWriter delegate, TripleStore tripleStore) {
        super(delegate);
        this.tripleStore = tripleStore;
    }


    public void write(RDFResource resource, RDFProperty property, Object object) throws Exception {
        if (tripleStore.contains(resource, property, object)) {
            super.write(resource, property, object);
        }
    }
}
