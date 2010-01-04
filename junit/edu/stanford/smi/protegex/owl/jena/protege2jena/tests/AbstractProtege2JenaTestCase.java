package edu.stanford.smi.protegex.owl.jena.protege2jena.tests;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.Restriction;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.jena.protege2jena.Protege2Jena;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public abstract class AbstractProtege2JenaTestCase extends AbstractJenaTestCase {

    protected OntModel createOntModel() {
        return Protege2Jena.createOntModel(owlModel);
    }


    protected OntModel createOntModelR() {
        return Protege2Jena.createOntModel(owlModel, OntModelSpec.OWL_MEM_RDFS_INF, owlModel.getTripleStoreModel().getTripleStores());
    }


    protected Restriction getRestriction(OntClass ontClass) {
        for (Iterator it = ontClass.listSuperClasses(); it.hasNext();) {
            OntClass superClass = (OntClass) it.next();
            if (superClass.canAs(Restriction.class)) {
                return (Restriction) superClass.as(Restriction.class);
            }
        }
        return null;
    }


    public JenaOWLModel reloadWithJenaLoader(JenaOWLModel owlModel) throws Exception {
        return reload(owlModel);
    }
}
