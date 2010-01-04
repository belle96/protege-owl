package edu.stanford.smi.protegex.owl.ui.matrix;

import edu.stanford.smi.protege.ui.FrameRenderer;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.Comparator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class AnnotationPropertyMatrixColumn implements EditableMatrixColumn, SortableMatrixColumn {

    private RDFProperty property;


    public AnnotationPropertyMatrixColumn(RDFProperty property) {
        this.property = property;
    }


    public RDFProperty getAnnotationProperty() {
        return property;
    }


    public TableCellRenderer getCellRenderer() {
        return new FrameRenderer() {
            public void load(Object value) {
                if (value instanceof RDFResource) {
                    RDFResource instance = (RDFResource) value;
                    value = instance.getPropertyValue(property);
                }
                if (value == null) {
                    clear();
                }
                else {
                    super.load(value);
                }
            }
        };
    }


    public String getName() {
        return property.getName();
    }


    public int getWidth() {
        return 250;
    }


    public Comparator getSortComparator() {
        return new Comparator() {
            public int compare(Object o1, Object o2) {
                final RDFResource a = (RDFResource) o1;
                final RDFResource b = (RDFResource) o2;
                final Object valueA = a.getPropertyValue(property);
                final Object valueB = b.getPropertyValue(property);
                if (valueA instanceof Comparable) {
                    if (valueB == null) {
                        return -1;
                    }
                    else {
                        int c = ((Comparable) valueA).compareTo(valueB);
                        if (c != 0) {
                            return c;
                        }
                    }
                }
                else if (valueB != null) {
                    return 1;
                }
                return a.getName().compareTo(b.getName());
            }
        };
    }


    public TableCellEditor getTableCellEditor() {
        JTextField textField = new JTextField();
        return new DefaultCellEditor(textField) {
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                RDFResource instance = (RDFResource) value;
                value = instance.getPropertyValue(property);
                if (value == null) {
                    value = "";
                }
                return super.getTableCellEditorComponent(table, instance.getLocalName(), isSelected, row, column);
            }
        };
    }


    public boolean isCellEditable(RDFResource instance) {
        return instance.getPropertyValues(property).size() < 2;
    }


    public void setValueAt(RDFResource instance, Object value) {
        if (((String) value).length() == 0) {
            value = null;
        }
        instance.setPropertyValue(property, value);
    }
}
