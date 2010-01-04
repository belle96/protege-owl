package edu.stanford.smi.protegex.owl.model.validator;

import edu.stanford.smi.protegex.owl.model.*;

import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultPropertyValueValidator implements PropertyValueValidator {

    public boolean isValidPropertyValue(RDFResource subject, RDFProperty predicate, Object object) {
        Iterator types = subject.listRDFTypes();
        while (types.hasNext()) {
            RDFSClass type = (RDFSClass) types.next();
            RDFResource range = null;
            if (type instanceof OWLNamedClass) {
                range = ((OWLNamedClass) type).getAllValuesFrom(predicate);
            }
            else {
                range = predicate.getRange();
            }
            if (range instanceof RDFSDatatype) {
                if (object instanceof RDFResource) {
                    return false;
                }
                RDFSLiteral literal = (RDFSLiteral) subject.getOWLModel().asRDFObject(object);
                RDFSDatatype datatype = (RDFSDatatype) range;
                if (!datatype.isValidValue(literal)) {
                    return false;
                }
            }
        }
        return true;
    }
}
