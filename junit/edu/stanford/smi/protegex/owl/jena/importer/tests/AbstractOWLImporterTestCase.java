package edu.stanford.smi.protegex.owl.jena.importer.tests;

import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protegex.owl.jena.importer.OWLImporter;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

import java.util.ArrayList;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
abstract class AbstractOWLImporterTestCase extends AbstractJenaTestCase {

    public KnowledgeBase runOWLImporter() {
        Project project = Project.createNewProject(null, new ArrayList());
        KnowledgeBase kb = project.getKnowledgeBase();
        new OWLImporter(owlModel, kb);
        return kb;
    }
}
