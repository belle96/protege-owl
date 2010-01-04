package edu.stanford.smi.protegex.owl.testing;

/**
 * A marker interface for OWLTests that check whether an ontology is OWL DL.
 * These tests fail if an OWL Full construct is used.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface OWLDLTest extends OWLTest {

    final static String GROUP = "OWL-DL Tests";
}
