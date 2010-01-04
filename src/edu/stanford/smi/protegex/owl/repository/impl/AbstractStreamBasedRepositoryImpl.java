package edu.stanford.smi.protegex.owl.repository.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protege.model.framestore.InMemoryFrameDb;
import edu.stanford.smi.protege.model.framestore.NarrowFrameStore;
import edu.stanford.smi.protegex.owl.jena.parser.ProtegeOWLParser;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.triplestore.TripleStore;
import edu.stanford.smi.protegex.owl.repository.Repository;
import edu.stanford.smi.protegex.owl.repository.util.XMLBaseExtractor;

public abstract class AbstractStreamBasedRepositoryImpl implements Repository {
    /**
     * Gets an inputstream to read the specified ontology
     * from
     *
     * @param ontologyName The name of the ontology.
     * @return an <code>InputStream</code> to read the ontology
     *         from, or <code>null</code> if the repository does not
     *         contain the ontology or the ontology cannot be retrieved.
     */
    public abstract InputStream getInputStream(URI ontologyName) throws OntologyLoadException;
    
    public TripleStore loadImportedAssertions(OWLModel owlModel, URI ontologyName) throws OntologyLoadException {
        ProtegeOWLParser parser = new ProtegeOWLParser(owlModel);
        parser.setImporting(true);
        InputStream is = getInputStream(ontologyName);
        URI xmlBase = XMLBaseExtractor.getXMLBase(getInputStream(ontologyName));
        if (xmlBase == null) {
            xmlBase = ontologyName;
        }
        TripleStore importedTripleStore = null;
        TripleStore importingTripleStore = owlModel.getTripleStoreModel().getActiveTripleStore();
        try {
            NarrowFrameStore frameStore = new InMemoryFrameDb(ontologyName.toString());
            importedTripleStore = owlModel.getTripleStoreModel().createActiveImportedTripleStore(frameStore);
            parser.loadTriples(ontologyName.toString(), xmlBase, is);
        } finally {
            owlModel.getTripleStoreModel().setActiveTripleStore(importingTripleStore);
        }
        
        /*
         * This call should not be needed anymore.
         * This should be called only once at the end of all the imports. 
         */         
        //((AbstractOWLModel) owlModel).copyFacetValuesIntoNamedClses();
        
        return importedTripleStore;
    }
    
    public boolean hasOutputStream(URI ontologyName) {
        return isWritable(ontologyName);
    }

}
