package edu.stanford.smi.protegex.owl.inference.protegeowl.log;

import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.testing.OWLTest;
import edu.stanford.smi.protegex.owl.testing.OWLTestResult;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import javax.swing.*;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 21, 2004<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class WarningMessageTestResult implements OWLTestResult {

    private WarningMessageLogRecord logRecord;


    public WarningMessageTestResult(WarningMessageLogRecord logRecord) {
        this.logRecord = logRecord;
    }


    public RDFResource getHost() {
        return logRecord.getCause();
    }


    /**
     * Gets an Icon to represent this type of OWLTestResult.
     *
     * @return an Icon (not null)
     */
    public Icon getIcon() {
        return OWLIcons.getOWLTestWarningIcon();
    }


    public String getMessage() {
        return logRecord.getMessage();
    }


    public OWLTest getOWLTest() {
        return null;
    }


    /**
     * Gets the type of result.
     *
     * @return one of TYPE_xxx
     */
    public int getType() {
        return TYPE_WARNING;
    }


    /**
     * Gets the (optional) user object attached to this OWLTestResult.
     *
     * @return the user object (e.g. to provide more info on how to repair this)
     */
    public Object getUserObject() {
        return null;
    }
}

