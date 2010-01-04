package edu.stanford.smi.protegex.owl.testing;

/**
 * An OWLTest that can be repaired (semi-) automatically.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface RepairableOWLTest extends OWLTest {

    /**
     * Attempts to repair the cause of a given OWLTestResult.
     *
     * @param testResult the OWLTestResult encapsulating the error
     * @return true if the repair was successful
     */
    boolean repair(OWLTestResult testResult);
}
