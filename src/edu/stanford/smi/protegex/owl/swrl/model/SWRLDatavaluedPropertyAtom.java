package edu.stanford.smi.protegex.owl.swrl.model;

import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.RDFObject;
import edu.stanford.smi.protegex.owl.model.RDFResource;

/**
 * @author Martin O'Connor  <moconnor@smi.stanford.edu>
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface SWRLDatavaluedPropertyAtom extends SWRLAtom {

    RDFResource getArgument1();


    void setArgument1(RDFResource iObject);


    RDFObject getArgument2();


    void setArgument2(RDFObject dObject);


    OWLDatatypeProperty getPropertyPredicate();


    void setPropertyPredicate(OWLDatatypeProperty datatypeSlot);

} // SWRLDatavaluedPropertyAtom
