package edu.stanford.smi.protegex.owl.inference.protegeowl.log;

import edu.stanford.smi.protegex.owl.inference.util.ReasonerUtil;
import edu.stanford.smi.protegex.owl.model.OWLAnonymousClass;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

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
public class ErrorMessageLogRecord extends MessageLogRecord {

    public ErrorMessageLogRecord(RDFResource cause, String message, ReasonerLogRecord parent) {
        super(cause, message, parent);


        String causeText;

        if (cause != null) {
            causeText = cause.getBrowserText();

            if (cause instanceof OWLAnonymousClass) {
                OWLNamedClass namedCls = ReasonerUtil.getInstance().getNamedReferent((OWLAnonymousClass) cause);

                if (namedCls != null) {
                    causeText += " (on <font color=\"000000\">" + namedCls.getBrowserText() + "</font>)";
                }
            }
        }
        else {
            causeText = "";
        }

        JLabel label = getJLabel();

        label.setIcon(OWLIcons.getOWLTestErrorIcon());

        label.setText("<html><body color=\"6E6E6E\">" +
                "<font color=\"ff6600\">ERROR<br></font>" +
                (causeText.equals("") ? "" : "<font color=\"ff6600\">CAUSE: </font><font color=\"000000\">" + causeText + "</font><br>" ) +
                getHTMLMessage() + "</body></html>");

        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }
}

