package edu.stanford.smi.protegex.owl.swrl.model;

import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;

/**
 * @author Martin O'Connor  <moconnor@smi.stanford.edu>
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface SWRLClassAtom extends SWRLAtom {

    RDFResource getArgument1();


    void setArgument1(RDFResource iObject);


    RDFSClass getClassPredicate();


    void setClassPredicate(RDFSClass aClass);

} // SWRLBuiltinAtom
