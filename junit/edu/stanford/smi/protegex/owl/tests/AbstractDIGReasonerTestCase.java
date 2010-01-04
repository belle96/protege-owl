/**
 * 
 */
package edu.stanford.smi.protegex.owl.tests;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import edu.stanford.smi.protege.util.ApplicationProperties;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.inference.dig.exception.DIGReasonerException;
import edu.stanford.smi.protegex.owl.inference.dig.reasoner.DIGReasonerIdentity;
import edu.stanford.smi.protegex.owl.inference.dig.reasoner.DefaultDIGReasoner;
import edu.stanford.smi.protegex.owl.inference.protegeowl.ProtegeOWLReasoner;
import edu.stanford.smi.protegex.owl.inference.protegeowl.ReasonerManager;
import edu.stanford.smi.protegex.owl.inference.protegeowl.log.MessageLogRecord;
import edu.stanford.smi.protegex.owl.inference.protegeowl.log.ReasonerLogRecord;
import edu.stanford.smi.protegex.owl.inference.protegeowl.log.ReasonerLogger;
import edu.stanford.smi.protegex.owl.inference.protegeowl.log.ReasonerLoggerListener;
import edu.stanford.smi.protegex.owl.inference.protegeowl.task.ReasonerTaskEvent;
import edu.stanford.smi.protegex.owl.inference.protegeowl.task.ReasonerTaskListener;
import edu.stanford.smi.protegex.owl.inference.util.ReasonerUtil;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLNames;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSLiteral;

/**
 * @author Nicolas Rouquette
 */
public class AbstractDIGReasonerTestCase extends AbstractJenaTestCase {
    public static final String REASONER_URL_PROPERTY = "junit.dig.url";

    public static String REASONER_URL = null;
    static {
      try {
        Properties jup = getJunitProperties();
        if (jup != null) {
          REASONER_URL = jup.getProperty(REASONER_URL_PROPERTY);
          ApplicationProperties.setString(DefaultDIGReasoner.DEFAULT_URL_PROPERTY, 
                                          AbstractDIGReasonerTestCase.REASONER_URL);
        }
      } catch (Exception e) {
        Log.getLogger().info("Reasoner not configured - tests ignored.");
      }
    }

    private static String EXPECTED_CLASSIFICATION_PROPNAME = "expectedClassificationStatus";

    private String reasoner_url = null;

    private ReasonerTaskListener tlnsr = null;

    private RDFProperty expectedClassificationProperty = null;

    private boolean haveReasoningTestAnnotations = false;

    // Allows access to the reasoner.
    protected ProtegeOWLReasoner reasoner = null;


    /**
     * The reasoner is connected during the call to super.setUp();
     * Therefore, to use an alternate reasoner in a derived test class, do:
     * <p/>
     * String myReasoner = ....;
     * setURL(myReasoner);
     * super.setUp();
     */
    protected void setURL(String url) {
        try {
            URI uri = new URI(url);
            reasoner_url = uri.toString();
            Log.getLogger().info("# Reasoner URL = " + reasoner_url);
        }
        catch (URISyntaxException e) {
            String message = "Reasoner URL is invalid: " + e.getMessage();
            fail(message);
        }
    }
    
    protected boolean reasonerInitialized() {
      return reasoner_url != null;
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        initializeReasoner();
    }
    
    protected void initializeReasoner() {
        // Get a reasoner for the (empty) OWL model from the ReasonerManager.
        ReasonerManager reasonerManager = ReasonerManager.getInstance();
        reasoner = reasonerManager.getReasoner(owlModel);

        ReasonerLoggerListener lsnr = new ReasonerLoggerListener() {
            public void logRecordPosted(ReasonerLogRecord reasonerLogRecord) {
                if (reasonerLogRecord instanceof MessageLogRecord) {
                    MessageLogRecord msgLog = (MessageLogRecord) reasonerLogRecord;
                    Log.getLogger().info(msgLog.getMessage());
                }
            }
        };

        ReasonerTaskListener tlnsr = new ReasonerTaskListener() {
            public void addedToTask(ReasonerTaskEvent event) {
                Log.getLogger().info(event.getSource().getMessage());
            }


            public void progressChanged(ReasonerTaskEvent event) {
                Log.getLogger().info(event.getSource().getMessage());
            }


            public void progressIndeterminateChanged(ReasonerTaskEvent event) {
                Log.getLogger().info(event.getSource().getMessage());
            }


            public void descriptionChanged(ReasonerTaskEvent event) {
                Log.getLogger().info(event.getSource().getMessage());
            }


            public void messageChanged(ReasonerTaskEvent event) {
                Log.getLogger().info(event.getSource().getMessage());
            }


            public void taskFailed(ReasonerTaskEvent event) {
                Log.getLogger().info(event.getSource().getMessage());
            }


            public void taskCompleted(ReasonerTaskEvent event) {
                Log.getLogger().info(event.getSource().getMessage());
            }
        };

        ReasonerLogger.getInstance().addListener(lsnr);

        setURL(REASONER_URL);
        reasoner.setURL(reasoner_url);
        {
            String message = "Connect DIG reasoner at: '" + reasoner_url + "'";
            assertTrue(message, reasoner.isConnected());
        }

        // Get the reasoner identity - this contains information
        // about the reasoner, such as its name and version,
        // and the tell and ask operations that it supports.
        DIGReasonerIdentity reasonerIdentity = reasoner.getIdentity();
        Log.getLogger().info("# Connected to " + reasonerIdentity.getName());
    }


    private void getReasoningTestAnnotations() {
        if (!haveReasoningTestAnnotations) {
            Collection annProps = new ArrayList(owlModel.getOWLAnnotationProperties());
            for (Iterator it = annProps.iterator(); it.hasNext();) {
                RDFProperty property = (RDFProperty) it.next();
                if (property instanceof OWLDatatypeProperty) {
                    if (property.getLocalName().compareTo(EXPECTED_CLASSIFICATION_PROPNAME) == 0) {
                        expectedClassificationProperty = property;
                        Log.getLogger().info("# Found annotation property: " + expectedClassificationProperty.getLocalName());
                    }
                }
            }

            assertNotNull(
                    "Searching for the 'expectedClassificationStatus' annotation",
                    expectedClassificationProperty);
            haveReasoningTestAnnotations = true;
        }
    }


    /**
     * Use the reasoner to analyze the consistency of the OWL model.
     * The OWL model should include adequate annotation properties
     * to compare the actual consistency of each class that has an annotation property:
     * expectedConsistencyStatus = true | false
     */
    protected void computeAndCheckInconsistentConcepts() {
        getReasoningTestAnnotations();
        try {
            reasoner.computeInconsistentConcepts(tlnsr);
        }
        catch (DIGReasonerException e) {
            String message = "Exception while checking consisntency: " + e.getMessage();
            fail(message);
        }

        Collection namedClses =
                ReasonerUtil.getInstance().getNamedClses(owlModel);

        int numberExpectations = 0;
        int actualExpectations = 0;

        Iterator namedClsesIt = namedClses.iterator();
        while (namedClsesIt.hasNext()) {
            final OWLNamedClass curNamedCls = (OWLNamedClass) namedClsesIt.next();
            String clsName = curNamedCls.getLocalName();
            if (curNamedCls.hasPropertyValue(expectedClassificationProperty)) {
                RDFSLiteral status = curNamedCls.getPropertyValueLiteral(expectedClassificationProperty);
                boolean expectedConsistency = status.getBoolean();

                int clsStatus = curNamedCls.getClassificationStatus();
                boolean actualConsistency =
                        clsStatus == OWLNames.CLASSIFICATION_STATUS_CONSISTENT_AND_CHANGED ||
                                clsStatus == OWLNames.CLASSIFICATION_STATUS_CONSISTENT_AND_UNCHANGED;


                Log.getLogger().info("# Consistency of '" + clsName + "' is expected="
                        + expectedConsistency + " actual=" + actualConsistency);
                ++numberExpectations;
                actualExpectations += (expectedConsistency == actualConsistency) ? 1 : 0;
            }
        }

        assertEquals("Expected consistency mismatch:", numberExpectations, actualExpectations);
    }
}
