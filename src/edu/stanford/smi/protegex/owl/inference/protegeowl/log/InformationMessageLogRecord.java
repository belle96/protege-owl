package edu.stanford.smi.protegex.owl.inference.protegeowl.log;

import edu.stanford.smi.protegex.owl.inference.ui.icons.InferenceIcons;
import edu.stanford.smi.protegex.owl.model.RDFResource;

import javax.swing.*;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 13, 2004<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class InformationMessageLogRecord extends MessageLogRecord {

    public InformationMessageLogRecord(RDFResource cause, String message, ReasonerLogRecord parent) {
        super(cause, message, parent);

        // Set the colour and icon to reflect an information message
        JLabel label = getJLabel();

        label.setIcon(InferenceIcons.getInformationMessageIcon());

        String colour = "6E6E6E";

        label.setText("<html><body color=\"" + colour + "\">" +
                getHTMLMessage() + "</body></html>");
    }
}

