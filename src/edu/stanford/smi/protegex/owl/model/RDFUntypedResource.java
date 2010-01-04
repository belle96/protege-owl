package edu.stanford.smi.protegex.owl.model;

/**
 * An rdf:Resource without any rdf:type.
 * Instances of this class are URIs that can show up as property values, for example
 * if a resource from an external, unimported ontology is used.
 * <p/>
 * In contrast to all other types of (typed) resources, the names of this type is the
 * full URI string.  This is because otherwise it would be necessary for the ontology to
 * define namespace prefixes for each unique external resource prefix.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface RDFUntypedResource extends RDFIndividual {

}
