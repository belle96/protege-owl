package edu.stanford.smi.protegex.owl.testing;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface OWLTest {


    /**
     * Gets a documentation text that describes the test.
     * This could be HTML text.
     *
     * @return the documentation
     */
    String getDocumentation();


    String getGroup();


    String getName();
}
