package edu.stanford.smi.protegex.owl.ui.matrix;

import edu.stanford.smi.protege.ui.FrameRenderer;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.Comparator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class NameMatrixColumn extends AbstractMatrixColumn implements EditableMatrixColumn, SortableMatrixColumn {

    public NameMatrixColumn() {
        super("Name", 250);
    }


    public TableCellRenderer getCellRenderer() {
        return new FrameRenderer() {
            public void load(Object value) {
                if (value instanceof RDFResource) {
                    RDFResource instance = (RDFResource) value;
                    setGrayedText(false);
                    addIcon(ProtegeUI.getIcon(instance));
                    addText(instance.getLocalName());
                }
                else {
                    super.load(value);
                }
            }
        };
    }


    public Comparator getSortComparator() {
        return new Comparator() {
            public int compare(Object o1, Object o2) {
                final RDFResource a = (RDFResource) o1;
                final RDFResource b = (RDFResource) o2;
                final int c = a.getLocalName().compareTo(b.getLocalName());
                if (c == 0) {
                    return a.getName().compareTo(b.getName());
                }
                else {
                    return c;
                }
            }
        };
    }


    public TableCellEditor getTableCellEditor() {
        JTextField textField = new JTextField();
        return new DefaultCellEditor(textField) {
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                RDFResource instance = (RDFResource) value;
                return super.getTableCellEditorComponent(table, instance.getLocalName(), isSelected, row, column);
            }
        };
    }


    public boolean isCellEditable(RDFResource instance) {
        return true;
    }


    public void setValueAt(RDFResource instance, Object value) {
        String oldName = instance.getLocalName();
        if (!oldName.equals(value)) {
            String newName = instance.getNamespacePrefix();
            if (newName == null) {
                newName = (String) value;
            }
            else {
                newName += ":" + value;
            }
            RDFResource other = instance.getOWLModel().getRDFResource(newName);
            if (other != null && !other.equals(instance)) {
                ProtegeUI.getModalDialogFactory().showErrorMessageDialog(instance.getOWLModel(),
                        "The name \"" + newName + "\" is already used", "Rename failed");
            }
            else if (instance.getOWLModel().isValidResourceName(newName, instance)) {
                instance = (RDFResource) instance.rename(newName);
            }
            else {
                ProtegeUI.getModalDialogFactory().showErrorMessageDialog(instance.getOWLModel(),
                        "\"" + newName + "\" is not a valid name\nfor " +
                                instance.getBrowserText(), "Rename failed");
            }
        }
    }
}
