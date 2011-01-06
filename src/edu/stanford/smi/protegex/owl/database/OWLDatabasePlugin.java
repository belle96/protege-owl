package edu.stanford.smi.protegex.owl.database;

import java.net.URI;

import edu.stanford.smi.protege.storage.database.DatabasePlugin;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface OWLDatabasePlugin extends DatabasePlugin {
    
    String getOntologyName();
    
    void setOntologyName(String name);
    
    String getUrl();
    
    String getDriver();

    String getTable();

    String getUsername();

    String getPassword();

}
