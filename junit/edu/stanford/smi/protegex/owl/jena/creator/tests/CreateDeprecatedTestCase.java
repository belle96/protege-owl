package edu.stanford.smi.protegex.owl.jena.creator.tests;

import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.vocabulary.OWL;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateDeprecatedTestCase extends AbstractJenaCreatorTestCase {

    public void testCreateDeprecatedClass() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("deprecatedCls");
        cls.setDeprecated(true);
        OntModel newModel = runJenaCreator();
        OntClass ontClass = newModel.getOntClass(cls.getURI());
        assertNotNull(ontClass);
        assertTrue(ontClass.hasRDFType(OWL.DeprecatedClass));
    }


    public void testCreateDeprecatedProperty() {
        OWLObjectProperty slot = owlModel.createOWLObjectProperty("deprecatedSlot");
        slot.setDeprecated(true);
        OntModel newModel = runJenaCreator();
        ObjectProperty objectProperty = newModel.getObjectProperty(slot.getURI());
        assertNotNull(objectProperty);
        assertTrue(objectProperty.hasRDFType(OWL.DeprecatedProperty));
    }
}
