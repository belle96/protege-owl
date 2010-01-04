package edu.stanford.smi.protegex.owl.jena.protege2jena.tests;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.vocabulary.RDFS;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateRDFSClassesTestCase extends AbstractProtege2JenaTestCase {

    public void testCreateRDFSClass() {
        RDFSNamedClass aClass = owlModel.createRDFSNamedClass("Cls");
        RDFSNamedClass subCls = owlModel.createRDFSNamedSubclass("SubCls", aClass);
        OntModel newModel = createOntModel();
        OntClass ontClass = newModel.getOntClass(aClass.getURI());
        assertNotNull(ontClass);
        assertEquals(RDFS.Class, ontClass.getRDFType());
        assertSize(1, ontClass.listSubClasses());
        assertEquals(newModel.getOntClass(subCls.getURI()), ontClass.getSubClass());
    }
}
