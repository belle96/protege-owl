package edu.stanford.smi.protegex.owl.inference.ui.icons;

import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import javax.swing.*;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Aug 22, 2004<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class InferenceIcons {

    public static final String REASONER_INSPECTOR = "ReasonerInspector.png";


    public static ImageIcon getIcon(String name) {
        return OWLIcons.getImageIcon(name, InferenceIcons.class);
    }


    public static Icon getErrorMessageIcon() {
        return getIcon("LogRecordErrorIcon.png");
    }


    public static Icon getWarningMessageIcon() {
        return getIcon("LogRecordWarningIcon.png");
    }


    public static Icon getInformationMessageIcon() {
        return getIcon("LogRecordInformationIcon.gif");
    }


    public static Icon getReasonerInspectorIcon() {
        return getIcon(REASONER_INSPECTOR);
    }


    public static Icon getReasonerInspectorTreeIcon() {
        return getIcon("ReasonerInspectorTreeIcon.png");
    }
}

