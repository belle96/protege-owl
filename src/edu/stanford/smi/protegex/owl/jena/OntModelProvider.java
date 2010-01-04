package edu.stanford.smi.protegex.owl.jena;

import com.hp.hpl.jena.ontology.OntModel;

/**
 * An interface that can be implemented by all OWLModel implementations
 * which provide a Jena OntModel.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface OntModelProvider {

    final static int OWL_LITE = 0;

    final static int OWL_DL = 1;

    final static int OWL_FULL = 2;


    /**
     * Gets an OntModel which represents the current state.
     *
     * @return an OntModel
     */
    OntModel getOntModel();


    /**
     * Gets or prepares an OntModel which is guaranteed to be in OWL DL, for
     * classification and other reasoning tasks.
     *
     * @return null  if the OntModel cannot be reduced to OWL DL
     */
    OntModel getOWLDLOntModel();


    /**
     * Gets the OWL Species of the current main OntModel.
     *
     * @return one of the three OWL_xxx constants
     */
    int getOWLSpecies();


    /**
     * Gets an OntModel that is connected to a (DIG) reasoner.
     *
     * @param classifierURL the URL of the classifier (usually defined in the preferences)
     * @return An OntModel that contains the classification result
     */
    OntModel getReasonerOntModel(String classifierURL);
}
