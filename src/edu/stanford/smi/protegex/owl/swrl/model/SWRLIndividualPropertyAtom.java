package edu.stanford.smi.protegex.owl.swrl.model;

import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;

/**
 * @author Martin O'Connor  <moconnor@smi.stanford.edu>
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface SWRLIndividualPropertyAtom extends SWRLAtom {

    RDFResource getArgument1();


    void setArgument1(RDFResource iObject);


    RDFResource getArgument2();


    void setArgument2(RDFResource iObject);


    OWLObjectProperty getPropertyPredicate();


    void setPropertyPredicate(OWLObjectProperty objectSlot);

} // SWRLIndividualPropertyAtom
