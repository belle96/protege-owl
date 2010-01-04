package edu.stanford.smi.protegex.owl.testing;

import edu.stanford.smi.protegex.owl.model.OWLModel;

import java.util.List;

/**
 * An interface for objects that can perform sanity checks on an ontology.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface OWLModelTest extends OWLTest {

    /**
     * @param owlModel
     * @return a List of OWLTestResult objects
     */
    List test(OWLModel owlModel);
}
