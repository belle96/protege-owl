package edu.stanford.smi.protegex.owl.jena.creator.tests;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import edu.stanford.smi.protegex.owl.model.RDFIndividual;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateSubpropertyValuesTestCase extends AbstractJenaCreatorTestCase {

    public void testValueOfSubpropertyWithInheritedDomain() {

        RDFProperty superproperty = owlModel.createRDFProperty("superproperty");
        RDFProperty subproperty = owlModel.createRDFProperty("subproperty");
        subproperty.addSuperproperty(superproperty);

        RDFSNamedClass cls = owlModel.createRDFSNamedClass("Class");
        superproperty.setDomain(cls);

        Object value = "Value";
        RDFIndividual rdfIndividual = cls.createRDFIndividual("Individual");
        rdfIndividual.addPropertyValue(subproperty, value);

        OntModel ontModel = runJenaCreator();
        Individual individual = ontModel.getIndividual(rdfIndividual.getURI());
        OntProperty subOntProperty = ontModel.getOntProperty(subproperty.getURI());
        assertNotNull(individual.getPropertyValue(subOntProperty));
    }

    /*public void testIndirectOwnSlots() {
       ClipsKnowledgeBaseFactory factory = new ClipsKnowledgeBaseFactory();
       Project project = Project.createNewProject(factory, new ArrayList());
       KnowledgeBase kb = project.getKnowledgeBase();
       Cls cls = kb.createCls("Cls", kb.getRootClses());
       Slot superSlot = kb.createSlot("superSlot");
       Slot subSlot = kb.createSlot("subSlot");
       subSlot.addDirectSuperslot(superSlot);
       cls.addDirectTemplateSlot(superSlot);
       assertTrue(cls.hasDirectTemplateSlot(superSlot));
       assertTrue(cls.hasTemplateSlot(subSlot));

       Instance instance = cls.createDirectInstance("Instance");
       Collection templateSlots = instance.getDirectType().getTemplateSlots();
       assertTrue(templateSlots.contains(superSlot));
       assertTrue(templateSlots.contains(subSlot));
       assertEquals(2, templateSlots.size());

       Collection ownSlots = instance.getOwnSlots();
       assertTrue(ownSlots.contains(superSlot));
       assertTrue(ownSlots.contains(subSlot));
       assertEquals(2, ownSlots.size());
   } */
}
