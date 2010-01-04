package edu.stanford.smi.protegex.owl.ui.profiles.tests;

import com.hp.hpl.jena.ontology.OntModel;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;
import edu.stanford.smi.protegex.owl.ui.profiles.ProfilesManager;

import java.util.Set;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ProfileCacheTestCase extends AbstractJenaTestCase {

    public void testCacheDefaultOntModel() {
        OntModel a = ProfilesManager.getDefaultProfileOntModel();
        OntModel b = ProfilesManager.getDefaultProfileOntModel();
        assertTrue(a == b);
    }


    public void testCacheCustomOntModel() throws Exception {
        String uri = "http://protege.stanford.edu/plugins/owl/testdata/TestProfile.owl";
        ProfilesManager.setProfile(owlModel, uri);
        OntModel ontModelA = ProfilesManager.getProfileOntModel(owlModel);
        Set setA = ProfilesManager.getFeaturesSet(uri);
        OntModel ontModelB = ProfilesManager.getProfileOntModel(owlModel);
        Set setB = ProfilesManager.getFeaturesSet(uri);
        assertTrue(ontModelA == ontModelB);
        assertTrue(setA == setB);

        ProfilesManager.clearCache(uri);

        OntModel ontModelC = ProfilesManager.getProfileOntModel(owlModel);
        Set setC = ProfilesManager.getFeaturesSet(uri);
        assertFalse(ontModelA == ontModelC);
        assertFalse(setA == setC);
    }
}
