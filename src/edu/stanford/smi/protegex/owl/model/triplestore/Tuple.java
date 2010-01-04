package edu.stanford.smi.protegex.owl.model.triplestore;

import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;

/**
 * A subject/predicate pair.
 *
 * This interface can be compared to the core Protege class <CODE>Reference</CODE>.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface Tuple {


    RDFResource getSubject();


    RDFProperty getPredicate();
}
