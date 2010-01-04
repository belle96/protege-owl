package edu.stanford.smi.protegex.owl.jena.parser;

import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface ProtegeOWLParserLogger {

    void logImport(String uri, String physicalURL);


    void logTripleAdded(RDFResource subject, RDFProperty predicate, Object object);


    void logWarning(String message);
}
