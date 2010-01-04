package edu.stanford.smi.protegex.owl.inference.ui;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.results.ResultsPanel;
import edu.stanford.smi.protegex.owl.ui.testing.OWLTestResultsPanel;

import javax.swing.*;
import java.util.Collection;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 21, 2004<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class ReasonerWarningPanel extends OWLTestResultsPanel {

    /**
     * Constructs a ReasonerWarningPanel
     *
     * @param kb    The knowledge base associated with the warnings
     * @param items A collection of OWLTestResults
     */
    public ReasonerWarningPanel(OWLModel kb, Collection items) {
        super(kb, items, null, false);
    }


    public Icon getIcon() {
        return OWLIcons.getClassifyIcon();
    }


    public String getTabName() {
        return "Reasoner warnings";
    }


    public boolean isReplaceableBy(ResultsPanel resultsPanel) {
        // Make the panel replaceable if it is to be replaced
        // with another instace of ReasonerWarningPanel
        if (resultsPanel instanceof ReasonerWarningPanel) {
            return true;
        }
        else {
            return false;
        }
    }

}

