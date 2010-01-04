package edu.stanford.smi.protegex.owl.swrl.model;

import edu.stanford.smi.protegex.owl.model.RDFResource;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface SWRLIndividualsAtom extends SWRLAtom {

    RDFResource getArgument1();


    void setArgument1(RDFResource iObject);


    RDFResource getArgument2();


    void setArgument2(RDFResource iObject);
}
