package edu.stanford.smi.protegex.owl.jena;


/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface OWLCreateProjectPlugin {


    void setDefaultClassView(Class typeClass);


    void setOntologyName(String namespace);


    void setProfile(String profileURI);
}
