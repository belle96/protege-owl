package edu.stanford.smi.protegex.owl.jena.parser.tests;

import java.io.File;
import java.util.Collection;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Model;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLOntology;
import edu.stanford.smi.protegex.owl.model.ProtegeNames;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.model.triplestore.TripleStore;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class LoadProtegeFeaturesTestCase extends AbstractJenaTestCase {
	
	static {
		if (!ProtegeOWL.getPluginFolder().exists()) {
			ProtegeOWL.setPluginFolder(new File("etc"));
		}
	}

    public void testLoadAbstractFlag() throws Exception {
        loadRemoteOntology("abstractClass.owl");
        RDFProperty abstractProperty = owlModel.getRDFProperty("protege:abstract");
        assertNotNull(abstractProperty);
        TripleStore homeTripleStore = owlModel.getTripleStoreModel().getHomeTripleStore(abstractProperty);
        Object protegeTripleStore = owlModel.getTripleStoreModel().getTripleStores().get(2);
        assertEquals(protegeTripleStore, homeTripleStore);
        OWLNamedClass cls = owlModel.getOWLNamedClass("Cls");
        Object propertyValue = cls.getPropertyValue(abstractProperty);
        assertEquals(Boolean.TRUE, propertyValue);
        assertTrue(((Cls) cls).isAbstract());
        OWLOntology ontology = owlModel.getDefaultOWLOntology();
        Collection imps = ontology.getImports();
        assertSize(1, imps);
        Collection irs = ontology.getImportResources();
        assertSize(1, irs);
    }


    public void testLoadFromTo() throws Exception {
        loadRemoteOntology("ProtegeFromTo.owl");
        RDFProperty fromProperty = owlModel.getSystemFrames().getFromSlot();
        assertNotNull(fromProperty);
        RDFProperty toProperty = owlModel.getSystemFrames().getToSlot();
        assertNotNull(toProperty);
        RDFResource relation = owlModel.getRDFResource("relation");
        assertNotNull(relation);
        assertSize(1, relation.getPropertyValues(fromProperty));
        assertSize(1, relation.getPropertyValues(toProperty));
    }


    public void testDomainOfFromAndTo() throws Exception {
    	loadRemoteOntology("ProtegeFromTo.owl");
    	
        final Slot directDomainSlot = owlModel.getSlot(Model.Slot.DIRECT_DOMAIN);
        RDFSNamedClass dbrClass = owlModel.getRDFSNamedClass(ProtegeNames.Cls.DIRECTED_BINARY_RELATION);
        RDFProperty fromProperty = owlModel.getRDFProperty(ProtegeNames.Slot.FROM);
        RDFProperty toProperty = owlModel.getRDFProperty(ProtegeNames.Slot.TO);
        Collection directDomains = ((Slot) fromProperty).getDirectOwnSlotValues(directDomainSlot);
        assertSize(1, directDomains);
        assertContains(dbrClass, directDomains);
        
        assertNotNull(dbrClass);
        assertNotNull(fromProperty);
        assertNotNull(toProperty);
        assertSize(1, fromProperty.getDomains(false));
        assertEquals(dbrClass, fromProperty.getDomain(false));
        directDomains = ((Slot) fromProperty).getDirectOwnSlotValues(directDomainSlot);
        assertSize(1, directDomains);
        assertContains(dbrClass, directDomains);
    }
}
