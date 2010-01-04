package edu.stanford.smi.protegex.owl.ui.matrix.cls;

import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.ui.actions.ResourceAction;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.matrix.MatrixFilter;
import edu.stanford.smi.protegex.owl.ui.results.ResultsPanelManager;
import edu.stanford.smi.protegex.owl.ui.search.SearchNamedClassAction;

import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class SubclassesMatrixAction extends ResourceAction {

    public SubclassesMatrixAction() {
        super("Show list of subclasses",
                OWLIcons.getImageIcon(OWLIcons.CLASS_MATRIX),
                SearchNamedClassAction.GROUP);
    }


    public void actionPerformed(ActionEvent e) {
        RDFSNamedClass parentClass = (RDFSNamedClass) getResource();
        MatrixFilter filter = new SubclassesMatrixFilter(parentClass);
        ClassMatrixPanel panel = new ClassMatrixPanel(getOWLModel(), filter);
        ResultsPanelManager.addResultsPanel(getOWLModel(), panel, true);
    }


    public boolean isSuitable(Component component, RDFResource resource) {
        return resource instanceof RDFSNamedClass;
    }
}
