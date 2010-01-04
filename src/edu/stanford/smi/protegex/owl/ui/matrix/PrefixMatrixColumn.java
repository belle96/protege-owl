package edu.stanford.smi.protegex.owl.ui.matrix;

import edu.stanford.smi.protege.ui.FrameRenderer;
import edu.stanford.smi.protegex.owl.model.RDFResource;

import javax.swing.table.TableCellRenderer;
import java.util.Comparator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class PrefixMatrixColumn extends AbstractMatrixColumn implements SortableMatrixColumn {

    public PrefixMatrixColumn() {
        super("Prefix", 150);
    }


    public TableCellRenderer getCellRenderer() {
        return new FrameRenderer() {
            public void load(Object value) {
                if (value instanceof RDFResource) {
                    RDFResource instance = (RDFResource) value;
                    setGrayedText(false);
                    addText(instance.getNamespacePrefix());
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
                return a.getName().compareTo(b.getName());
            }
        };
    }
}
