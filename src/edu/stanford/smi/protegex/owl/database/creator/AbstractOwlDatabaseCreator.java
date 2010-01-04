package edu.stanford.smi.protegex.owl.database.creator;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protege.storage.database.DatabaseKnowledgeBaseFactory;
import edu.stanford.smi.protege.util.PropertyList;
import edu.stanford.smi.protegex.owl.database.OWLDatabaseKnowledgeBaseFactory;
import edu.stanford.smi.protegex.owl.database.OWLDatabaseModel;
import edu.stanford.smi.protegex.owl.jena.JenaKnowledgeBaseFactory;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.factory.AbstractOwlProjectCreator;

/**
 * This is the common part  of all the owl database project creators.  It is abstract because
 * it does not ensure that the ontology is not null.
 *
 * @author tredmond
 *
 */

public abstract class AbstractOwlDatabaseCreator extends AbstractOwlProjectCreator {

    private String driver;

    private String table;

    private String username;

    private String password;

    private String url;

    protected Project project;

    protected AbstractOwlDatabaseCreator(OWLDatabaseKnowledgeBaseFactory factory) {
        super(factory);
    }

    @SuppressWarnings("unchecked")
	@Override
    public void create(Collection errors) throws OntologyLoadException {
        project = Project.createBuildProject(factory, errors);
        initializeSources(project.getSources());
        project.createDomainKnowledgeBase(factory, errors, true);
        insertRepositoriesIntoOwlModel((OWLModel) project.getKnowledgeBase());
    }

    @Override
    public OWLDatabaseModel getOwlModel() {
        if (project != null) {
            return (OWLDatabaseModel) project.getKnowledgeBase();
        }
        return null;
    }

    @Override
    public Project getProject() {
        return project;
    }

    protected void initializeSources(PropertyList sources) {
        DatabaseKnowledgeBaseFactory.setSources(sources, driver, url, table, username, password);
    }

    @SuppressWarnings("unchecked")
    protected void initializeTable(Collection errors) throws IOException {
        JenaKnowledgeBaseFactory.useStandalone = false;

        Project project = Project.createNewProject(factory, errors);
        initializeSources(project.getSources());
        File tempProjectFile = File.createTempFile("protege", "temp");
        project.setProjectFilePath(tempProjectFile.getPath());
        project.save(errors);
    }

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

    public String getUrl() {
        return url;
    }


}
