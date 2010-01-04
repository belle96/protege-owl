package edu.stanford.smi.protegex.owl.inference.protegeowl.tests;

import edu.stanford.smi.protege.util.ApplicationProperties;
import edu.stanford.smi.protegex.owl.inference.dig.reasoner.DefaultDIGReasoner;
import edu.stanford.smi.protegex.owl.tests.AbstractDIGReasonerTestCase;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

public class AbstractProtegeOwlTestCase extends AbstractJenaTestCase {
	
  public void setUp() throws Exception {
    super.setUp();
    if (AbstractDIGReasonerTestCase.REASONER_URL != null) {
      ApplicationProperties.setString(DefaultDIGReasoner.DEFAULT_URL_PROPERTY, 
    		                          AbstractDIGReasonerTestCase.REASONER_URL);
    }
  }
}
