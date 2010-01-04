package edu.stanford.smi.protegex.owl.ui.profiles.tests;

import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;
import edu.stanford.smi.protegex.owl.ui.profiles.OWLProfiles;
import edu.stanford.smi.protegex.owl.ui.profiles.ProfilesManager;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ProfilesManagerTestCase extends AbstractJenaTestCase {

    public void testIsFeatureSupported() {

        assertTrue(ProfilesManager.isFeatureSupported(owlModel, OWLProfiles.OWL_DL));
        assertTrue(ProfilesManager.isFeatureSupported(owlModel, OWLProfiles.DataRanges));

        String uri = "http://protege.stanford.edu/plugins/owl/testdata/TestProfile.owl";
        ProfilesManager.setProfile(owlModel, uri);

        assertFalse(ProfilesManager.isFeatureSupported(owlModel, OWLProfiles.OWL_Full));
        assertTrue(ProfilesManager.isFeatureSupported(owlModel, OWLProfiles.OWL_Lite));
        assertFalse(ProfilesManager.isFeatureSupported(owlModel, OWLProfiles.DataRanges));
    }
}
