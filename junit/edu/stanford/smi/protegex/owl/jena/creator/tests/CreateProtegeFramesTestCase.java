package edu.stanford.smi.protegex.owl.jena.creator.tests;

import java.io.File;
import java.util.logging.Logger;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;

import edu.stanford.smi.protege.model.Model;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.model.RDFIndividual;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateProtegeFramesTestCase extends AbstractJenaCreatorTestCase {
    private static transient final Logger log = Log.getLogger(CreateProtegeFramesTestCase.class);
    
    static {
        if (!ProtegeOWL.getPluginFolder().exists()) {
            ProtegeOWL.setPluginFolder(new File("etc"));
        }
    }

    public void testCreateUnlinkedPALConstraint() {

        ensureProtegeMetaOntologyImported();

        RDFSNamedClass constraintClass = owlModel.getSystemFrames().getPalConstraintCls();
        RDFIndividual constraint = constraintClass.createRDFIndividual("MyIndividual");

        RDFProperty palNameProperty = owlModel.getSystemFrames().getPalNameSlot();
        constraint.setPropertyValue(palNameProperty, "MyName");

        RDFProperty palStatementProperty = owlModel.getSystemFrames().getPalStatementSlot();
        constraint.setPropertyValue(palStatementProperty, "MyStatement");

        OntModel newModel = runJenaCreator();
        // Jena.dumpRDF(newModel, log, Level.FINE);

        OntClass constraintOntClass = newModel.getOntClass(owlModel.getSystemFrames().getPalConstraintCls().getName());
        assertNotNull(constraintOntClass);
        OntProperty palNameOntProperty = newModel.getOntProperty(owlModel.getSystemFrames().getPalNameSlot().getName());
        assertNotNull(palNameOntProperty);
        OntProperty palStatementOntProperty = newModel.getOntProperty(owlModel.getSystemFrames().getPalStatementSlot().getName());

        assertNotNull(palStatementOntProperty);
        Individual constraintIndividual = newModel.getIndividual(constraint.getURI());
        assertNotNull(constraintIndividual);
        assertSize(1, constraintIndividual.listPropertyValues(palNameOntProperty));
        assertSize(1, constraintIndividual.listPropertyValues(palStatementOntProperty));
    }


    public void testCreateLinkedPALConstraint() {

        ensureProtegeMetaOntologyImported();

        RDFSNamedClass constraintClass = owlModel.getSystemFrames().getPalConstraintCls();
        RDFIndividual constraint = constraintClass.createRDFIndividual("MyIndividual");

        RDFSNamedClass rdfsClass = owlModel.createRDFSNamedClass("MyClass");
        RDFProperty constraintsProperty = owlModel.getSystemFrames().getConstraintsSlot();
        rdfsClass.setPropertyValue(constraintsProperty, constraint);

        OntModel newModel = runJenaCreator();
        // Jena.dumpRDF(newModel, log, Level.FINE);
        OntProperty constraintsOntProperty = newModel.getOntProperty(owlModel.getSystemFrames().getConstraintsSlot().getName());
        assertNotNull(constraintsOntProperty);

        Individual constraintIndividual = newModel.getIndividual(constraint.getURI());
        OntClass ontClass = newModel.getOntClass(rdfsClass.getURI());
        assertSize(1, ontClass.listPropertyValues(constraintsOntProperty));
        assertEquals(constraintIndividual, ontClass.getPropertyValue(constraintsOntProperty));
    }
}
