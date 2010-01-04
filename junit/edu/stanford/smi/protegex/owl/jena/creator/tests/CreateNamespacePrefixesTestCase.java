package edu.stanford.smi.protegex.owl.jena.creator.tests;

import com.hp.hpl.jena.ontology.OntModel;

import java.util.Iterator;
import java.util.Map;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateNamespacePrefixesTestCase extends AbstractJenaCreatorTestCase {

    public void testDefaultNamespaces() {
        owlModel.getNamespaceManager().setPrefix("http://anything.de/Onto#", "aldi");
        Map oldMap = owlModel.getOntModel().getNsPrefixMap();
        OntModel newModel = runJenaCreator();
        Map newMap = newModel.getNsPrefixMap();
        assertEquals(oldMap.size(), newMap.size());
        for (Iterator it = oldMap.keySet().iterator(); it.hasNext();) {
            String prefix = (String) it.next();
            String oldNS = (String) oldMap.get(prefix);
            String newNS = (String) newMap.get(prefix);
            assertEquals(oldNS, newNS);
        }
    }
}
