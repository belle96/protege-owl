package edu.stanford.smi.protegex.owl.storage.tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Level;

import com.hp.hpl.jena.ontology.OntProperty;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.jena.creator.NewOwlProjectCreator;
import edu.stanford.smi.protegex.owl.model.OWLEnumeratedClass;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.OWLRestriction;
import edu.stanford.smi.protegex.owl.model.OWLSomeValuesFrom;
import edu.stanford.smi.protegex.owl.storage.KnowledgeBaseCopier;
import edu.stanford.smi.protegex.owl.storage.OWL2OWLCopier;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class OWL2OWLCopierTestCase extends AbstractJenaTestCase {
    
    public static void debug() {
        Log.setLoggingLevel(OWL2OWLCopierTestCase.class, Level.FINE);
        Log.setLoggingLevel(KnowledgeBaseCopier.class, Level.FINE);
    }


    private JenaOWLModel runCopier() {
        Collection errors = new ArrayList();
        try {
            NewOwlProjectCreator creator = new NewOwlProjectCreator();
            creator.create(errors);
            project = creator.getProject();
        }
        catch (OntologyLoadException e) {
            Log.getLogger().log(Level.SEVERE, "Exception caught", e);
            fail();
        }
        if (!errors.isEmpty()) {
            Log.getLogger().severe("Errors  found trying to create empty owl ontology");
            fail();
        }
        JenaOWLModel target = (JenaOWLModel) project.getKnowledgeBase();
        new OWL2OWLCopier(owlModel, target).run();
        return target;
    }

    /*
    public void testAnnotation() {
       OWLDatatypeProperty annotationSlot = owlModel.createAnnotationOWLDatatypeProperty("anno");
       annotationSlot.setDomainDefined(false);
       annotationSlot.addPropertyValue(annotationSlot, "Value");

       JenaOWLModel target = runCopier();
       target.setExpandShortNameInMethods(true);
       Slot newSlot = target.getOWLDatatypeProperty("anno");
       assertEquals(1, newSlot.getDirectOwnSlotValues(newSlot).size());
       assertTrue(newSlot.getDirectOwnSlotValues(newSlot).contains("Value"));
   } */


    public void testEnumeration() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Degree");
        Instance ms = cls.createInstance("MS");
        Instance ma = cls.createInstance("MA");
        OWLEnumeratedClass enumerationCls =
                owlModel.createOWLEnumeratedClass(Arrays.asList(new Instance[]{ms, ma}));
        OWLObjectProperty slot = owlModel.createOWLObjectProperty("slot");
        OWLSomeValuesFrom restriction = owlModel.createOWLSomeValuesFrom(slot, enumerationCls);
        OWLNamedClass studentCls = owlModel.createOWLNamedClass("Student");
        OWLNamedClass graduateStudentCls = owlModel.createOWLNamedSubclass("GraduateStudent", studentCls);
        graduateStudentCls.addSuperclass(restriction);

        JenaOWLModel target = runCopier();

        Instance newMS = target.getInstance(ms.getName());
        assertEquals(1, newMS.getDirectTypes().size());
        assertSize(1, target.getOntModel().listEnumeratedClasses());
    }


    public void testEquivalentClass() {
        OWLNamedClass genderCls = owlModel.createOWLNamedClass("Gender");
        Instance male = genderCls.createInstance("male");
        OWLObjectProperty genderSlot = owlModel.createOWLObjectProperty("gender");
        OWLNamedClass maleCls = owlModel.createOWLNamedClass("Male");
        OWLRestriction hasRestriction = owlModel.createOWLHasValue(genderSlot, male);
        maleCls.setDefinition(hasRestriction);

        JenaOWLModel target = runCopier();

        assertSize(1, target.getOntModel().listRestrictions());
    }


    public void testInverseSlots() {
        OWLObjectProperty aSlot = owlModel.createOWLObjectProperty("A");
        OWLObjectProperty bSlot = owlModel.createOWLObjectProperty("B");
        aSlot.setInverseProperty(bSlot);

        JenaOWLModel target = runCopier();

        OWLObjectProperty newASlot = (OWLObjectProperty) target.getSlot(aSlot.getName());
        OWLObjectProperty newBSlot = (OWLObjectProperty) target.getSlot(bSlot.getName());
        assertNotNull(newASlot.getInverseProperty());
        assertEquals(newASlot.getInverseProperty(), newBSlot);
        OntProperty aProperty = target.getOntModel().getOntProperty(newASlot.getURI());
        OntProperty bProperty = target.getOntModel().getOntProperty(newBSlot.getURI());
        assertSize(1, aProperty.listInverseOf());
        assertEquals(aProperty, bProperty.getInverseOf());
        assertSize(1, bProperty.listInverseOf());
        assertEquals(bProperty, aProperty.getInverseOf());
    }


    public void testNamespaces() {
        final String DEFAULT = "http://aldi.de/ont#";
        owlModel.getNamespaceManager().setDefaultNamespace(DEFAULT);
        final String TEST = "http://test.de/ont#";
        owlModel.getNamespaceManager().setPrefix(TEST, "test");

        JenaOWLModel target = runCopier();

        assertEquals(TEST, target.getNamespaceManager().getNamespaceForPrefix("test"));
        assertEquals(DEFAULT, target.getNamespaceManager().getDefaultNamespace());
    }
}
