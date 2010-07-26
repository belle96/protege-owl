package edu.stanford.smi.protegex.owl.jena.creator;

import java.util.Collection;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protegex.owl.jena.JenaKnowledgeBaseFactory;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.factory.AbstractOwlProjectCreator;
import edu.stanford.smi.protegex.owl.model.factory.AlreadyImportedException;
import edu.stanford.smi.protegex.owl.model.factory.FactoryUtils;

public class NewOwlProjectCreator extends AbstractOwlProjectCreator {
    private String ontologyName;
    
    private Project project;
    private JenaOWLModel owlModel;
    
    public NewOwlProjectCreator() {
        this(new JenaKnowledgeBaseFactory());
    }
    
    public NewOwlProjectCreator(JenaKnowledgeBaseFactory factory) {
        super(factory);
    }

    @Override
    public void create(Collection errors) throws OntologyLoadException {
        project = Project.createNewProject(factory, errors);
        owlModel = (JenaOWLModel) project.getKnowledgeBase();
        
        if (ontologyName == null) {
            ontologyName = FactoryUtils.generateOntologyURIBase();
        }
        try {
            FactoryUtils.addOntologyToTripleStore(owlModel, owlModel.getTripleStoreModel().getActiveTripleStore(), ontologyName);
        }
        catch (AlreadyImportedException e) {
            throw new RuntimeException("This shouldn't happen", e);
        }
        addViewSettings(project.getSources());
    }

    @Override
    public JenaOWLModel getOwlModel() {
        return owlModel;
    }
    
    @Override
    public Project getProject() {
        return project;
    }
    
    /*
     * ---------------------------------------------------------------------
     * setters and getters
     */
    
    public void setOntologyName(String ontologyName) {
        this.ontologyName = ontologyName;
    }
    
    

}
