package edu.stanford.smi.protegex.owl.jena;


/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface OWLFilesPlugin extends OWLCreateProjectPlugin {

    /**
     * @deprecated
     */
    void addImport(String uri, String prefix);

    void setFile(String fileURI);

    void setLanguage(String lang);
}
