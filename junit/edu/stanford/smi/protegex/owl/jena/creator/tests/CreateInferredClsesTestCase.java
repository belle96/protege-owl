package edu.stanford.smi.protegex.owl.jena.creator.tests;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.vocabulary.OWL;
import edu.stanford.smi.protegex.owl.jena.Jena;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;

import java.util.Set;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateInferredClsesTestCase extends AbstractJenaCreatorTestCase {

    public void testCreateInstance() throws Exception {
        OWLNamedClass superCls = owlModel.createOWLNamedClass("SuperCls");
        OWLNamedClass subCls = owlModel.createOWLNamedClass("SubCls");
        subCls.addSuperclass(owlModel.createOWLComplementClass(superCls));
        subCls.addInferredSuperclass(superCls);
        OntModel newModel = runJenaCreator(false, true);
        OntClass superClass = newModel.getOntClass(superCls.getURI());
        OntClass subClass = newModel.getOntClass(subCls.getURI());
        Set superClasses = Jena.set(subClass.listSuperClasses());
        assertSize(2, superClasses);
        assertFalse(superClasses.contains(OWL.Thing));
        superClasses.remove(superClass);
        assertSize(1, superClasses);
    }
}
