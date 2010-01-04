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
public interface UnresolvedImportHandler {

    /**
     * Used by the <code>ProtegeOWLParser</code> to obtain an ontology repository
     * that contains the specified ontology.
     *
     * @param model        The <code>OWLModel</code>
     * @param tripleStore  The triple store that the triples from the ontology will be
     *                     inserted into.
     * @param ontologyName The name of the ontology that the system is attempting to
     *                     import.
     * @return A <code>Repository</code> containing the ontology, or, <code>null</code>
     *         if the missing ontology cannot be found and the loading process should be aborted.
     */
    Repository handleUnresolvableImport(OWLModel model, TripleStore tripleStore, URI ontologyName);
}
