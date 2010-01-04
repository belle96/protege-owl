package edu.stanford.smi.protegex.owl.jena.protege2jena.tests;

import java.io.File;
import java.util.logging.Level;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;

import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.parser.FrameCreatorUtility;
import edu.stanford.smi.protegex.owl.model.ProtegeNames;
import edu.stanford.smi.protegex.owl.model.RDFIndividual;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateProtegeFramesTestCase extends AbstractProtege2JenaTestCase {
    
    static {
        if (!ProtegeOWL.getPluginFolder().exists()) {
            ProtegeOWL.setPluginFolder(new File("etc"));
        }
    }
    
    
    public static void enableDebug() {
        Log.setLoggingLevel(AbstractJenaTestCase.class, Level.FINE);
        //Log.setLoggingLevel(ProtegeOWLParser.class, Level.FINEST);
        //Log.setLoggingLevel(TripleProcessor.class, Level.FINEST);
        Log.setLoggingLevel(FrameCreatorUtility.class, Level.FINEST);
    }

    public void testCreateUnlinkedPALConstraint() throws Exception {    
        ensureProtegeMetaOntologyImported();

        RDFSNamedClass constraintClass = owlModel.getSystemFrames().getPalConstraintCls();
        RDFIndividual constraint = constraintClass.createRDFIndividual("MyIndividual");

        RDFProperty palNameProperty = owlModel.getSystemFrames().getPalNameSlot();
        constraint.setPropertyValue(palNameProperty, "MyName");

        RDFProperty palStatementProperty = owlModel.getSystemFrames().getPalStatementSlot();
        constraint.setPropertyValue(palStatementProperty, "MyStatement");

        owlModel = reloadWithJenaLoader(owlModel);
        OntModel newModel = createOntModel();

        OntClass constraintOntClass = newModel.getOntClass(ProtegeNames.Cls.PAL_CONSTRAINT);
        assertNotNull(constraintOntClass);
        OntProperty palNameOntProperty = newModel.getOntProperty(ProtegeNames.Slot.PAL_NAME);
        assertNotNull(palNameOntProperty);
        OntProperty palStatementOntProperty = newModel.getOntProperty(ProtegeNames.Slot.PAL_STATEMENT);
        assertNotNull(palStatementOntProperty);
        Individual constraintIndividual = newModel.getIndividual(constraint.getURI());
        assertNotNull(constraintIndividual);
        assertSize(1, constraintIndividual.listPropertyValues(palNameOntProperty));
        assertSize(1, constraintIndividual.listPropertyValues(palStatementOntProperty));
    }
    
    public void testCreateUnlinkedPALConstraintEasy() throws Exception {    
        ensureProtegeMetaOntologyImported();

        RDFSNamedClass constraintClass = owlModel.getRDFSNamedClass(ProtegeNames.Cls.PAL_CONSTRAINT);
        RDFIndividual constraint = constraintClass.createRDFIndividual("MyIndividual");

        RDFProperty palNameProperty = owlModel.getRDFProperty(ProtegeNames.Slot.PAL_NAME);
        constraint.setPropertyValue(palNameProperty, "MyName");

        RDFProperty palStatementProperty = owlModel.getRDFProperty(ProtegeNames.Slot.PAL_STATEMENT);
        constraint.setPropertyValue(palStatementProperty, "MyStatement");

        owlModel = reloadWithJenaLoader(owlModel);
        OntModel newModel = createOntModel();

        OntClass constraintOntClass = newModel.getOntClass(ProtegeNames.Cls.PAL_CONSTRAINT);
        assertNotNull(constraintOntClass);
        OntProperty palNameOntProperty = newModel.getOntProperty(ProtegeNames.Slot.PAL_NAME);
        assertNotNull(palNameOntProperty);
        OntProperty palStatementOntProperty = newModel.getOntProperty(ProtegeNames.Slot.PAL_STATEMENT);
        assertNotNull(palStatementOntProperty);
        Individual constraintIndividual = newModel.getIndividual(constraint.getURI());
        assertNotNull(constraintIndividual);
        assertSize(1, constraintIndividual.listPropertyValues(palNameOntProperty));
        assertSize(1, constraintIndividual.listPropertyValues(palStatementOntProperty));
    }


    public void testCreateLinkedPALConstraint() throws Exception {
        Log.setLoggingLevel(AbstractJenaTestCase.class, Level.FINE);

        ensureProtegeMetaOntologyImported();
        owlModel = reloadWithJenaLoader(owlModel);

        RDFSNamedClass constraintClass = owlModel.getSystemFrames().getPalConstraintCls();
        RDFIndividual constraint = constraintClass.createRDFIndividual("MyIndividual");

        RDFSNamedClass rdfsClass = owlModel.createRDFSNamedClass("MyClass");
        RDFProperty constraintsProperty = owlModel.getSystemFrames().getSlotConstraintsSlot();
        rdfsClass.setPropertyValue(constraintsProperty, constraint);

        OntModel newModel = createOntModel();
        OntProperty constraintsOntProperty = newModel.getOntProperty(ProtegeNames.Slot.CONSTRAINTS);
        assertNotNull(constraintsOntProperty);

        Individual constraintIndividual = newModel.getIndividual(constraint.getURI());
        OntClass ontClass = newModel.getOntClass(rdfsClass.getURI());
        assertSize(1, ontClass.listPropertyValues(constraintsOntProperty));
        assertEquals(constraintIndividual, ontClass.getPropertyValue(constraintsOntProperty));
    }
}
