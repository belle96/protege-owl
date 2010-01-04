package edu.stanford.smi.protegex.owl.database;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protege.model.KnowledgeBaseFactory;
import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protege.plugin.AbstractCreateProjectPlugin;
import edu.stanford.smi.protege.plugin.CreateProjectWizard;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protege.util.WizardPage;
import edu.stanford.smi.protegex.owl.database.creator.OwlDatabaseCreator;

/**
 * @author Ray Fergerson  <fergerson@smi.stanford.edu>
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateOWLDatabaseProjectPlugin extends
        AbstractCreateProjectPlugin implements OWLDatabasePlugin {
    private static transient Logger log = Log.getLogger(CreateOWLDatabaseProjectPlugin.class);

    private String driver;

    private String table;

    private String username;

    private String password;

    private String url;
    
    private String ontologyName;


    public CreateOWLDatabaseProjectPlugin() {
        this("OWL Database");
    }


    public CreateOWLDatabaseProjectPlugin(String name) {
        super(name);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    protected Project createNewProject(KnowledgeBaseFactory factory) {
        return makeProject(factory, true);
    }
    
    @Override
    protected Project buildNewProject(KnowledgeBaseFactory factory) {
        return makeProject(factory, false);
    }
    
    private Project makeProject(KnowledgeBaseFactory factory, boolean wipe) {
        OwlDatabaseCreator creator = new OwlDatabaseCreator((OWLDatabaseKnowledgeBaseFactory) factory, wipe);
        creator.setDriver(driver);
        creator.setURL(url);
        creator.setTable(table);
        creator.setUsername(username);
        creator.setPassword(password);
        creator.setOntologyName(ontologyName);
        Collection errors = new ArrayList();
        try {
            creator.create(errors);
        }
        catch (OntologyLoadException ioe) {
            errors.add(ioe);
        }
        handleErrors(errors);
        return creator.getProject();
    }


    public boolean canCreateProject(KnowledgeBaseFactory factory, boolean useExistingSources) {
        return factory instanceof OWLDatabaseKnowledgeBaseFactory;
    }
    
    


    public WizardPage createCreateProjectWizardPage(CreateProjectWizard wizard,
                                                    boolean useExistingSources) {
        return useExistingSources ?
                new OWLDatabaseWizardPageExistingSources(wizard, this) :
                new OWLDatabaseWizardPage(wizard, this);
    }


    /*
     * getters and setters
     */
    
    public void setDriver(String driver) {
        this.driver = driver;
    }




    public void setTable(String table) {
        this.table = table;
    }


    public void setURL(String url) {
        this.url = url;
    }


    public void setUsername(String username) {
        this.username = username;
    }


    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setOntologyName(String name) {
        this.ontologyName = name;
    }


    public String getUrl() {
        return url;
    }


    public void setUrl(String url) {
        this.url = url;
    }


    public String getDriver() {
        return driver;
    }


    public String getTable() {
        return table;
    }


    public String getUsername() {
        return username;
    }


    public String getPassword() {
        return password;
    }

    public String getOntologyName() {
        return ontologyName;
    }
}