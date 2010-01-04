package edu.stanford.smi.protegex.owl.database;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protege.model.KnowledgeBaseFactory;
import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protege.plugin.CreateProjectWizard;
import edu.stanford.smi.protege.util.ApplicationProperties;
import edu.stanford.smi.protege.util.URIUtilities;
import edu.stanford.smi.protege.util.WizardPage;
import edu.stanford.smi.protegex.owl.database.creator.OwlDatabaseFromFileCreator;
import edu.stanford.smi.protegex.owl.jena.JenaKnowledgeBaseFactory;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateOWLDatabaseFromFileProjectPlugin extends CreateOWLDatabaseProjectPlugin {

	private static String MERGE_MODE_PROP = "protege.owl.parser.convert.db.merge.mode";

    protected URI ontologyInputSource;
    private boolean isMergeImportMode = ApplicationProperties.getBooleanProperty(MERGE_MODE_PROP, false);

	public CreateOWLDatabaseFromFileProjectPlugin() {
        super("OWL File (.owl or .rdf)");
    }

    @Override
	@SuppressWarnings("unchecked")
    protected Project buildNewProject(KnowledgeBaseFactory factory) {
        JenaKnowledgeBaseFactory.useStandalone = false;
        Collection errors = new ArrayList();
        OwlDatabaseFromFileCreator creator = new OwlDatabaseFromFileCreator((OWLDatabaseKnowledgeBaseFactory) factory);
        creator.setDriver(getDriver());
        creator.setURL(getUrl());
        creator.setTable(getTable());
        creator.setUsername(getUsername());
        creator.setPassword(getPassword());
        creator.setOntologySource(getOntologyInputSource().toString());
        creator.setMergeImportMode(isMergeImportMode);

        try {
            creator.create(errors);
        }
        catch (OntologyLoadException ioe) {
            errors.add(ioe);
        }
        handleErrors(errors);
        return creator.getProject();
    }


    @Override
	public boolean canCreateProject(KnowledgeBaseFactory factory, boolean useExistingSources) {
        return super.canCreateProject(factory, useExistingSources) && useExistingSources;
    }


    @Override
	public WizardPage createCreateProjectWizardPage(CreateProjectWizard wizard, boolean useExistingSources) {
        return new InitOWLDatabaseFromFileWizardPage(wizard, this);
    }



    public void setOntologyInputSource(URI uri) {
        this.ontologyInputSource = uri;
    }


    /**
     * @deprecated Use {@link #setOntologyInputSource(URI)}
     */
    @Deprecated
    public void setOntologyFileURI(String uri) {
    	URI uritemp = URIUtilities.createURI(uri);
    	if (uritemp == null) {
    		throw new RuntimeException("Invalid uri " + uri);
    	}
    	setOntologyInputSource(uritemp);
    }

    public URI getOntologyInputSource() {
        return ontologyInputSource;
    }

    public boolean isMergeImportMode() {
		return isMergeImportMode;
	}

	public void setMergeImportMode(boolean isMergeImportMode) {
		this.isMergeImportMode = isMergeImportMode;
	}

}
