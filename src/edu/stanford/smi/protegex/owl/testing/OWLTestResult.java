package edu.stanford.smi.protegex.owl.testing;

import edu.stanford.smi.protegex.owl.model.RDFResource;

import javax.swing.*;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface OWLTestResult {

    final static int TYPE_ERROR = 1;

    final static int TYPE_WARNING = 2;

    final static int TYPE_OWL_FULL = 3;


    RDFResource getHost();


    /**
     * Gets an Icon to represent this type of OWLTestResult.
     *
     * @return an Icon (not null)
     */
    Icon getIcon();


    String getMessage();


    OWLTest getOWLTest();


    /**
     * Gets the type of result.
     *
     * @return one of TYPE_xxx
     */
    int getType();


    /**
     * Gets the (optional) user object attached to this OWLTestResult.
     *
     * @return the user object (e.g. to provide more info on how to repair this)
     */
    Object getUserObject();
}
