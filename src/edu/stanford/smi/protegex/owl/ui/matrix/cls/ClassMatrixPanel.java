package edu.stanford.smi.protegex.owl.ui.matrix.cls;

import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.matrix.MatrixColumn;
import edu.stanford.smi.protegex.owl.ui.matrix.MatrixFilter;
import edu.stanford.smi.protegex.owl.ui.matrix.MatrixPanel;
import edu.stanford.smi.protegex.owl.ui.matrix.MatrixTableModel;
import edu.stanford.smi.protegex.owl.ui.resourceselection.ResourceSelectionAction;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ClassMatrixPanel extends MatrixPanel {

    private ResourceSelectionAction addExistentialAction = new ResourceSelectionAction("Add someValuesFrom column...",
            OWLIcons.getAddIcon(OWLIcons.OWL_SOME_VALUES_FROM)) {

        public void resourceSelected(RDFResource resource) {
            RDFProperty property = (RDFProperty) resource;
            ExistentialMatrixColumn col = new ExistentialMatrixColumn(property);
            getTable().addColumn(col);
        }


        public RDFResource pickResource() {
            return ProtegeUI.getSelectionDialogFactory().selectResourceFromCollection(ClassMatrixPanel.this, getOWLModel(),
                    getSelectableResources(), "Select a property");
        }


        public Collection getSelectableResources() {
            RDFSNamedClass aClass = (RDFSNamedClass) getTable().getSelectedInstance();
            Collection results = getPotentialProperties(aClass);
            int count = getTableModel().getColumnCount();
            for (int i = 0; i < count; i++) {
                MatrixColumn col = getTableModel().getMatrixColumn(i);
                if (col instanceof ExistentialMatrixColumn) {
                    results.remove(((ExistentialMatrixColumn) col).getProperty());
                }
            }
            return results;
        }
    };


    public ClassMatrixPanel(OWLModel owlModel, MatrixFilter filter) {
        this(owlModel, filter, new ClassMatrixTableModel(owlModel, filter));
    }


    public ClassMatrixPanel(OWLModel owlModel, MatrixFilter filter, MatrixTableModel tableModel) {
        super(owlModel, filter, tableModel);
        getTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                updateActions();
            }
        });
        addButton(addExistentialAction);
        updateActions();
    }


    static Collection getPotentialProperties(RDFSNamedClass aClass) {
        Collection results = new ArrayList();
        for (Iterator it = aClass.getUnionDomainProperties(true).iterator(); it.hasNext();) {
            RDFProperty property = (RDFProperty) it.next();
            if (property instanceof OWLObjectProperty && !((OWLObjectProperty) property).isAnnotationProperty()) {
                results.add(property);
            }
        }
        return results;
    }


    private void updateActions() {
        RDFSNamedClass aClass = (RDFSNamedClass) getTable().getSelectedInstance();
        boolean enabled = aClass instanceof OWLNamedClass;
        addExistentialAction.setEnabled(enabled);
    }
}
