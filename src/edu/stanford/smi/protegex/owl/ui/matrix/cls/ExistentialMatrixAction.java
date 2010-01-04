package edu.stanford.smi.protegex.owl.ui.matrix.cls;

import edu.stanford.smi.protege.resource.Icons;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.actions.ResourceAction;
import edu.stanford.smi.protegex.owl.ui.matrix.MatrixFilter;
import edu.stanford.smi.protegex.owl.ui.matrix.MatrixTableModel;
import edu.stanford.smi.protegex.owl.ui.results.ResultsPanelManager;
import edu.stanford.smi.protegex.owl.ui.search.SearchNamedClassAction;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ExistentialMatrixAction extends ResourceAction {

    public ExistentialMatrixAction() {
        super("Show existential matrix of subclasses...",
                Icons.getBlankIcon(),
                SearchNamedClassAction.GROUP);
    }


    public void actionPerformed(ActionEvent e) {
        OWLModel owlModel = getOWLModel();
        RDFSNamedClass parentClass = (RDFSNamedClass) getResource();
        final Collection slots = selectProperties(parentClass);
        if (!slots.isEmpty()) {
            MatrixFilter filter = new SubclassesMatrixFilter(parentClass);
            MatrixTableModel tableModel = new MatrixTableModel(owlModel, filter) {
                protected void addDefaultColumns() {
                    super.addDefaultColumns();
                    for (Iterator it = slots.iterator(); it.hasNext();) {
                        RDFProperty property = (RDFProperty) it.next();
                        addColumn(new ExistentialMatrixColumn(property));
                    }
                }
            };
            ClassMatrixPanel panel = new ClassMatrixPanel(owlModel, filter, tableModel);
            ResultsPanelManager.addResultsPanel(owlModel, panel, true);
        }
    }


    public boolean isSuitable(Component component, RDFResource resource) {
        return resource instanceof RDFSNamedClass;
    }


    private Collection selectProperties(RDFSNamedClass parentClass) {
        Collection properties = ClassMatrixPanel.getPotentialProperties(parentClass);
        OWLModel owlModel = parentClass.getOWLModel();
        return ProtegeUI.getSelectionDialogFactory().selectResourcesFromCollection(getComponent(), owlModel,
                properties, "Select properties to show");
    }
}
