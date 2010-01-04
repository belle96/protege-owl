package edu.stanford.smi.protegex.owl.ui.matrix;

import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.resourceselection.ResourceSelectionAction;
import edu.stanford.smi.protegex.owl.ui.results.ResultsPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class MatrixPanel extends ResultsPanel {

    private ResourceSelectionAction addAnnotationPropertyAction = new ResourceSelectionAction("Add column for annotation property...",
            OWLIcons.getAddIcon(OWLIcons.OWL_DATATYPE_ANNOTATION_PROPERTY)) {

        public void resourceSelected(RDFResource resource) {
            addAnnotationProperty((RDFProperty) resource);
        }


        public RDFResource pickResource() {
            return ProtegeUI.getSelectionDialogFactory().selectResourceFromCollection(MatrixPanel.this, getOWLModel(),
                    getSelectableResources(), "Select annotation property");
        }


        public Collection getSelectableResources() {
            Collection results = new ArrayList(owlModel.getOWLAnnotationProperties());
            results.removeAll(tableModel.getVisibleAnnotationProperties());
            for (Iterator it = results.iterator(); it.hasNext();) {
                RDFProperty property = (RDFProperty) it.next();
                if (!(property instanceof OWLDatatypeProperty)) {
                    it.remove();
                }
            }
            return results;
        }
    };

    private MatrixFilter filter;

    private OWLModel owlModel;

    private MatrixTable table;

    private MatrixTableModel tableModel;


    public MatrixPanel(OWLModel owlModel, MatrixFilter filter, MatrixTableModel tableModel) {

        super(owlModel);

        this.filter = filter;
        this.owlModel = owlModel;
        this.tableModel = tableModel;

        table = createMatrixTable(tableModel);

        addRefreshButton();

        addAnnotationButtons();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(table.getBackground());
        setCenterComponent(scrollPane);
    }


    private void addRefreshButton() {
        Action refreshAction = new AbstractAction("Refresh", OWLIcons.getImageIcon(OWLIcons.REFRESH)) {
            public void actionPerformed(ActionEvent e) {
                refresh();
            }
        };
        addButton(refreshAction);
    }


    private void addAnnotationButtons() {
        JButton addButton = addButton(addAnnotationPropertyAction);
        addAnnotationPropertyAction.activateComboBox(addButton);
    }


    private void addAnnotationProperty(RDFProperty property) {
        AnnotationPropertyMatrixColumn col = new AnnotationPropertyMatrixColumn(property);
        table.addColumn(col);
    }


    protected MatrixTable createMatrixTable(MatrixTableModel tableModel) {
        return new MatrixTable(tableModel);
    }


    public void dispose() {
        super.dispose();
        tableModel.dispose();
    }


    public MatrixTable getTable() {
        return table;
    }


    public MatrixTableModel getTableModel() {
        return tableModel;
    }


    public String getTabName() {
        return filter.getName();
    }


    private void refresh() {
        tableModel.refill();
    }
}
