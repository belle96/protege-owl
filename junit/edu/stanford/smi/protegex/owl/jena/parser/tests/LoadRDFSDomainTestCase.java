package edu.stanford.smi.protegex.owl.jena.parser.tests;

import edu.stanford.smi.protege.model.Model;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.jena.Jena;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.OWLUnionClass;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class LoadRDFSDomainTestCase extends AbstractJenaTestCase {
    private static final Logger log = Log.getLogger(LoadRDFSDomainTestCase.class);
    
    public static void enableDebug() {
        log.setLevel(Level.FINE);
    }

    @SuppressWarnings("unchecked")
    public void testLoadUnionDomain() throws Exception {

        OWLNamedClass oldPersonClass = owlModel.createOWLNamedClass("Person");
        OWLNamedClass oldCorporationClass = owlModel.createOWLNamedClass("Corporation");
        OWLObjectProperty oldProperty = owlModel.createOWLObjectProperty("hasBankAccount");
        oldProperty.setDomain(oldPersonClass);
        oldProperty.addUnionDomainClass(oldCorporationClass);
        Collection oldDomain = ((Slot) oldProperty).getDirectDomain();
        assertSize(2, oldDomain);
        assertContains(oldPersonClass, oldDomain);
        assertContains(oldCorporationClass, oldDomain);

        Jena.dumpRDF(owlModel.getOntModel(), log, Level.FINE);

        OWLModel newModel = reload(owlModel);
        OWLNamedClass newPersonClass = newModel.getOWLNamedClass(oldPersonClass.getName());
        OWLNamedClass newCorporationClass = newModel.getOWLNamedClass(oldCorporationClass.getName());
        OWLObjectProperty newProperty = newModel.getOWLObjectProperty(oldProperty.getName());
        OWLUnionClass unionDomain = (OWLUnionClass) newProperty.getDomain(false);
        assertSize(2, unionDomain.getOperands());
        assertContains(newPersonClass, unionDomain.getOperands());
        assertContains(newCorporationClass, unionDomain.getOperands());
        Collection newDomain = ((Slot) newProperty).getDirectDomain();
        assertSize(2, newDomain);
        assertContains(newPersonClass, newDomain);
        assertContains(newCorporationClass, newDomain);
    }


    public void testLoadInheritedDomain() throws Exception {
        loadRemoteOntology("inheritedDomain.owl");
        Slot subProperty = owlModel.getOWLProperty("subSlot");
        assertSize(1, subProperty.getDirectOwnSlotValues(owlModel.getSlot(Model.Slot.DIRECT_DOMAIN)));
        assertContains(owlModel.getOWLThingClass(), subProperty.getDirectOwnSlotValues(owlModel.getSlot(Model.Slot.DIRECT_DOMAIN)));
    }
}
