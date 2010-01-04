package edu.stanford.smi.protegex.owl.inference.protegeowl.log;

import edu.stanford.smi.protegex.owl.testing.OWLTestResult;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 21, 2004<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class ReasonerLoggerUtil {

    public static OWLTestResult convertToOWLTestResult(WarningMessageLogRecord logRecord) {
        return new WarningMessageTestResult(logRecord);
    }
}

