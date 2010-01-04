package edu.stanford.smi.protegex.owl.model.validator;

import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;

/**
 * An interface for objects that can validate whether a certain value would be a valid
 * object for a given subject/predicate pair.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface PropertyValueValidator {

    boolean isValidPropertyValue(RDFResource subject, RDFProperty predicate, Object object);
}
