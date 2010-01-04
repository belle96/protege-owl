package edu.stanford.smi.protegex.owl.ui.code;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.TreeCellEditor;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.EventObject;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 6, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class SymbolCellEditor extends AbstractCellEditor implements TableCellEditor, TreeCellEditor, SymbolEditorHandler {

    private SymbolEditorComponent editorComponent;


    public SymbolCellEditor(SymbolEditorComponent editorComponent) {
        this.editorComponent = editorComponent;
    }


    public Object getCellEditorValue() {
        return editorComponent.getTextComponent().getText();
    }


    public boolean isCellEditable(EventObject e) {
        if (e instanceof MouseEvent) {
            return ((MouseEvent) e).getClickCount() >= 2 &&
                    SwingUtilities.isRightMouseButton((MouseEvent) e) == false;
        }
        return true;
    }


    public Component getTableCellEditorComponent(JTable table,
                                                 Object value,
                                                 boolean isSelected,
                                                 int row,
                                                 int column) {
        editorComponent.getTextComponent().setText(value != null ? value.toString() : "");
        editorComponent.getTextComponent().requestFocus();
        return editorComponent;
    }


    public Component getTreeCellEditorComponent(JTree tree,
                                                Object value,
                                                boolean isSelected,
                                                boolean expanded,
                                                boolean leaf,
                                                int row) {
        editorComponent.getTextComponent().setText(value != null ? value.toString() : "");
        editorComponent.requestFocus();
        return editorComponent;
    }


    public void stopEditing() {
        stopCellEditing();
    }


    public void cancelEditing() {
        cancelCellEditing();
    }
}

