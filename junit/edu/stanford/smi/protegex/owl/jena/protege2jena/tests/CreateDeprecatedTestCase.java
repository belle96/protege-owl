package edu.stanford.smi.protegex.owl.jena.protege2jena.tests;

import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.vocabulary.OWL;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateDeprecatedTestCase extends AbstractProtege2JenaTestCase {

    public void testCreateDeprecatedClass() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("deprecatedCls");
        cls.setDeprecated(true);
        OntModel newModel = createOntModel();
        OntClass ontClass = newModel.getOntClass(cls.getURI());
        assertNotNull(ontClass);
        assertTrue(ontClass.hasRDFType(OWL.DeprecatedClass));
    }


    public void testCreateDeprecatedProperty() {
        OWLObjectProperty slot = owlModel.createOWLObjectProperty("deprecatedSlot");
        slot.setDeprecated(true);
        OntModel newModel = createOntModel();
        ObjectProperty objectProperty = newModel.getObjectProperty(slot.getURI());
        assertNotNull(objectProperty);
        assertTrue(objectProperty.hasRDFType(OWL.DeprecatedProperty));
    }
}
