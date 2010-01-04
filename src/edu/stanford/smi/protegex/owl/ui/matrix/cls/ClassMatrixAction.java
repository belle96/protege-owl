package edu.stanford.smi.protegex.owl.ui.matrix.cls;

import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.matrix.MatrixFilter;
import edu.stanford.smi.protegex.owl.ui.results.ResultsPanelManager;
import edu.stanford.smi.protegex.owl.ui.widget.OWLUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ClassMatrixAction extends AbstractAction {

    private OWLModel owlModel;


    public ClassMatrixAction(OWLModel owlModel) {
        super("Show matrix of all classes", OWLIcons.getImageIcon(OWLIcons.CLASS_MATRIX));
        this.owlModel = owlModel;
    }


    public void actionPerformed(ActionEvent e) {
        if (!OWLUI.isConfirmationNeeded(owlModel) ||
                OWLUI.isConfirmed(owlModel, ((KnowledgeBase) owlModel).getClsCount() > OWLUI.getConfirmationThreshold(owlModel))) {
            performAction();
        }
    }


    private void performAction() {
        MatrixFilter filter = new MatrixFilter() {

            public Collection getInitialValues() {
                Collection clses = owlModel.getUserDefinedOWLNamedClasses();
                for (Iterator it = clses.iterator(); it.hasNext();) {
                    RDFSNamedClass aClass = (RDFSNamedClass) it.next();
                    if (!isSuitable(aClass)) {
                        it.remove();
                    }
                }
                return clses;
            }


            public String getName() {
                return "All Classes";
            }


            public boolean isSuitable(RDFResource instance) {
                return instance instanceof RDFSNamedClass &&
                        instance.isVisible() &&
                        (instance.isEditable() || instance.isIncluded());
            }
        };
        ClassMatrixPanel panel = new ClassMatrixPanel(owlModel, filter);
        ResultsPanelManager.addResultsPanel(owlModel, panel, true);
    }
}
