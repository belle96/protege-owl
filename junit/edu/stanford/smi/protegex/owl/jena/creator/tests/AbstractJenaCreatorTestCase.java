package edu.stanford.smi.protegex.owl.jena.creator.tests;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.Restriction;
import edu.stanford.smi.protegex.owl.jena.creator.JenaCreator;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
abstract class AbstractJenaCreatorTestCase extends AbstractJenaTestCase {

    protected Restriction getRestriction(OntClass ontClass) {
        for (Iterator it = ontClass.listSuperClasses(); it.hasNext();) {
            OntClass superClass = (OntClass) it.next();
            if (superClass.canAs(Restriction.class)) {
                return (Restriction) superClass.as(Restriction.class);
            }
        }
        return null;
    }


    protected OntModel runJenaCreator() {
        return runJenaCreator(false);
    }


    protected OntModel runJenaCreator(boolean forReasoning) {
        return runJenaCreator(forReasoning, false);
    }


    protected OntModel runJenaCreator(boolean forReasoning, boolean inferred) {
        return new JenaCreator(owlModel, forReasoning, inferred, null, null).createOntModel();
    }
}
