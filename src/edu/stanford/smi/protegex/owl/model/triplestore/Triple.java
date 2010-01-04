package edu.stanford.smi.protegex.owl.model.triplestore;



/**
 * A triple in an OWLModel or RDF graph.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface Triple extends Tuple, TripleDescriptor {

    Object getObject();
}
