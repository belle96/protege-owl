package edu.stanford.smi.protegex.owl.tests;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;
import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protegex.owl.jena.JenaKnowledgeBaseFactory;
import edu.stanford.smi.protegex.owl.model.OWLModel;

/**
 * The base class of various JUnit tests on OWL ontologies.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public abstract class AbstractOWLModelTestCase extends TestCase {

    protected OWLModel owlModel;

    protected Project project;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Collection errors = new ArrayList();
        project = new Project(null, errors);
        final JenaKnowledgeBaseFactory factory = new JenaKnowledgeBaseFactory();
        project.setKnowledgeBaseFactory(factory);
        project.createDomainKnowledgeBase(factory, errors, false);
        owlModel = (OWLModel) project.getKnowledgeBase();
        owlModel.setExpandShortNameInMethods(true);
    }
}
