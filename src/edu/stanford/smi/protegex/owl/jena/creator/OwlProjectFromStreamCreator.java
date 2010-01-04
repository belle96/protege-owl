package edu.stanford.smi.protegex.owl.jena.creator;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protegex.owl.jena.JenaKnowledgeBaseFactory;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.jena.parser.ProtegeOWLParser;
import edu.stanford.smi.protegex.owl.model.factory.AbstractOwlProjectCreator;
import edu.stanford.smi.protegex.owl.repository.Repository;

public class OwlProjectFromStreamCreator extends AbstractOwlProjectCreator {
    private InputStream stream;
    private String xmlBase;
    private List<Repository> repositories = new ArrayList<Repository>();
    
    private Project project;
    private JenaOWLModel owlModel;


    public OwlProjectFromStreamCreator() {
        this(new JenaKnowledgeBaseFactory());
    }
    
    public OwlProjectFromStreamCreator(JenaKnowledgeBaseFactory factory) {
        super(factory);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void create(Collection errors) throws OntologyLoadException {
        project = Project.createNewProject(factory, errors);
        owlModel = (JenaOWLModel) project.getKnowledgeBase();
        insertRepositoriesIntoOwlModel(owlModel);
        
        ProtegeOWLParser parser = new ProtegeOWLParser(owlModel);
        try {
            parser.run(stream, xmlBase);
        }
        catch  (OntologyLoadException e) {
            errors.add(e);
        }
    }
    
    @Override
    public JenaOWLModel getOwlModel() {
        return owlModel;
    }
    
    @Override
    public Project getProject() {
        return project;
    }
    
    public void setStream(InputStream stream) {
        this.stream = stream;
    }

    public void setXmlBase(String xmlBase) {
        this.xmlBase = xmlBase;
    }
    
    public void addRepository(Repository repository) {
    	repositories.add(repository);
    }
    
    public void clearRepositories() {
    	repositories.clear();
    }
    
    public List<Repository> getRepositories() {
    	return Collections.unmodifiableList(repositories);
    }

}
