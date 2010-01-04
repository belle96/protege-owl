package edu.stanford.smi.protegex.owl.ui.metrics;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.actions.AbstractOWLModelAction;
import edu.stanford.smi.protegex.owl.ui.dialogs.ModalDialogFactory;
import edu.stanford.smi.protegex.owl.ui.metrics.lang.DLExpressivityPanel;

import javax.swing.*;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Aug 31, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class ShowMetricsAction extends AbstractOWLModelAction {

    public String getMenubarPath() {
        return PROJECT_MENU;
    }


    public String getName() {
        return "Metrics...";
    }


    public void run(OWLModel owlModel) {
	    MetricsPanel metricsPanel = new MetricsPanel(owlModel);
	    DLExpressivityPanel expressivityPanel = new DLExpressivityPanel(owlModel);
	    JTabbedPane tabbedPane = new JTabbedPane();
	    tabbedPane.add("Metrics", metricsPanel);
	    tabbedPane.add("DL Expressivity", expressivityPanel);
	    ProtegeUI.getModalDialogFactory().showDialog(ProtegeUI.getTopLevelContainer(owlModel.getProject()),
                tabbedPane,
                "OWL Model Metrics",
                ModalDialogFactory.MODE_CLOSE);
    }
}

