package edu.stanford.smi.protegex.owl.jena.creator.tests;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.vocabulary.OWL;
import edu.stanford.smi.protege.model.ValueType;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLNames;

import java.util.Collections;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateMetaClassesTestCase extends AbstractJenaCreatorTestCase {

    public void testCreateMetaClass() {
        OWLNamedClass metaCls = (OWLNamedClass) owlModel.createCls("Meta",
                                                                   Collections.singleton(owlModel.getOWLNamedClassClass()));
        OntModel newModel = runJenaCreator();
        OntClass metaClass = newModel.getOntClass(metaCls.getURI());
        assertEquals(OWL.Class, metaClass.getSuperClass());
    }


    public void testCreateMetaClassInstance() {
        OWLNamedClass metaCls = (OWLNamedClass) owlModel.createCls("Meta",
                                                                   Collections.singleton(owlModel.getOWLNamedClassClass()));
        OWLDatatypeProperty slot = owlModel.createOWLDatatypeProperty("author", owlModel.getXSDstring());
        slot.addUnionDomainClass(metaCls);
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls", metaCls);
        cls.setPropertyValue(slot, "A");

        OntModel newModel = runJenaCreator();
        OntClass metaClass = newModel.getOntClass(metaCls.getURI());
        OntClass ontClass = newModel.getOntClass(cls.getURI());
        assertContains(metaClass, ontClass.listRDFTypes(false));
        OntProperty ontProperty = newModel.getOntProperty(slot.getURI());
        assertHasValue(ontClass, ontProperty, ValueType.STRING, "A");
    }


    public void testCreateMetaSlot() {
        OWLNamedClass datatypeSlotMetaCls = (OWLNamedClass) owlModel.createCls("DatatypeMetaSlot",
                                                                               Collections.singleton(owlModel.getCls(OWLNames.Cls.DATATYPE_PROPERTY)));
        OWLNamedClass objectSlotMetaCls = (OWLNamedClass) owlModel.createCls("ObjectMetaSlot",
                                                                             Collections.singleton(owlModel.getCls(OWLNames.Cls.OBJECT_PROPERTY)));
        OntModel newModel = runJenaCreator();
        OntClass datatypeMetaClass = newModel.getOntClass(datatypeSlotMetaCls.getURI());
        OntClass objectMetaClass = newModel.getOntClass(objectSlotMetaCls.getURI());
        assertEquals(OWL.DatatypeProperty, datatypeMetaClass.getSuperClass());
        assertEquals(OWL.ObjectProperty, objectMetaClass.getSuperClass());
    }


    public void testCreateMetaSlotInstance() {
        OWLNamedClass metaCls = (OWLNamedClass) owlModel.createCls("DatatypeMetaSlot",
                                                                   Collections.singleton(owlModel.getCls(OWLNames.Cls.DATATYPE_PROPERTY)));
        OWLDatatypeProperty slot = owlModel.createOWLDatatypeProperty("author", owlModel.getXSDstring());
        slot.addUnionDomainClass(metaCls);
        OWLDatatypeProperty instance = (OWLDatatypeProperty) owlModel.createSlot("Cls", metaCls);
        instance.setPropertyValue(slot, "A");

        OntModel newModel = runJenaCreator();
        OntClass metaClass = newModel.getOntClass(metaCls.getURI());
        OntProperty property = newModel.getOntProperty(instance.getURI());
        assertEquals(metaClass, property.getRDFType());
        OntProperty ontProperty = newModel.getOntProperty(slot.getURI());
        assertHasValue(property, ontProperty, ValueType.STRING, "A");
    }
}
