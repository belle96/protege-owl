package edu.stanford.smi.protegex.owl.jena.creator.tests;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.vocabulary.RDFS;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateRDFSClassesTestCase extends AbstractJenaCreatorTestCase {

    public void testCreateRDFSClass() {
        RDFSNamedClass aClass = owlModel.createRDFSNamedClass("Cls");
        RDFSNamedClass subCls = owlModel.createRDFSNamedSubclass("SubCls", aClass);
        OntModel ontModel = runJenaCreator();
        OntClass ontClass = ontModel.getOntClass(aClass.getURI());
        assertNotNull(ontClass);
        assertEquals(RDFS.Class, ontClass.getRDFType());
        assertSize(1, ontClass.listSubClasses());
        assertEquals(ontModel.getOntClass(subCls.getURI()), ontClass.getSubClass());
    }
}
