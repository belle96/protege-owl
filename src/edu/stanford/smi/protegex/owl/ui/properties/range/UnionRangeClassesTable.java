package edu.stanford.smi.protegex.owl.ui.properties.range;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.ui.ResourceRenderer;
import edu.stanford.smi.protegex.owl.ui.clsdesc.ClassDescriptionEditorComponent;
import edu.stanford.smi.protegex.owl.ui.code.SymbolEditorComponent;
import edu.stanford.smi.protegex.owl.ui.code.SymbolErrorDisplay;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.owltable.OWLTable;

import javax.swing.*;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
class UnionRangeClassesTable extends OWLTable {

    private UnionRangeClassesTableModel tableModel;


    public UnionRangeClassesTable(UnionRangeClassesTableModel tableModel, OWLModel owlModel) {
        super(tableModel, owlModel, true);
        this.tableModel = tableModel;
        getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }


    void createAndEditRow() {
        int row = getOWLTableModel().addEmptyRow(-1);
        editExpression(row);
    }


    protected ResourceRenderer createOWLFrameRenderer() {
        return new ResourceRenderer() {
            protected Icon getClsIcon(Cls cls) {
                if (tableModel.isInherited((RDFSClass) cls)) {
                    ImageIcon icon = ((RDFSClass) cls).getImageIcon();
                    return OWLIcons.getInheritedIcon(icon, OWLIcons.CLASS_FRAME);
                }
                else {
                    return super.getClsIcon(cls);
                }
            }
        };
    }


    protected SymbolEditorComponent createSymbolEditorComponent(OWLModel model,
                                                                SymbolErrorDisplay errorDisplay) {
        return new ClassDescriptionEditorComponent(model, errorDisplay, false);
    }


    public void hideSymbolPanel() {
        super.hideSymbolPanel();
        getOWLTableModel().removeEmptyRow();
    }

    /*public Component prepareEditor(TableCellEditor editor, int row, int column) {
       String expression = (String) getModel().getValueAt(row, column);
       showSymbolPanel(expression.length() == 0);
       return super.prepareEditor(editor, row, column);
   } */
}
