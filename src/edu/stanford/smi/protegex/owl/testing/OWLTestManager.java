package edu.stanford.smi.protegex.owl.testing;

/**
 * A container of the currently active OWLTests.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface OWLTestManager {

    void addOWLTest(OWLTest test);


    OWLTest[] getOWLTests();


    boolean isAutoRepairEnabled();


    boolean isOWLTestGroupEnabled(String groupName);


    void removeOWLTest(OWLTest test);


    void setAutoRepairEnabled(boolean value);


    void setOWLTestGroupEnabled(String groupName, boolean value);
}
