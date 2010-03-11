package edu.stanford.smi.protegex.owl.swrl.model;

import edu.stanford.smi.protegex.owl.model.OWLDataRange;
import edu.stanford.smi.protegex.owl.model.RDFObject;

/**
 * @author Martin O'Connor  <moconnor@smi.stanford.edu>
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface SWRLDataRangeAtom extends SWRLAtom {

    /**
     * This argument is either an RDFList of RDFSLiterals, or
     * a SWRLVariable.
     */
    RDFObject getArgument1();


    /**
     * This argument is either an RDFList of RDFSLiterals, or
     * a SWRLVariable.
     */
    void setArgument1(RDFObject dObject);


    OWLDataRange getDataRange();


    void setDataRange(OWLDataRange dataRange);
}
