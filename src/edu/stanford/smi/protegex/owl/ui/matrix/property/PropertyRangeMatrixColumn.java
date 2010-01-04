package edu.stanford.smi.protegex.owl.ui.matrix.property;

import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.ui.FrameRenderer;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.ui.matrix.SortableMatrixColumn;

import javax.swing.table.TableCellRenderer;
import java.util.Comparator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class PropertyRangeMatrixColumn implements SortableMatrixColumn {

    public TableCellRenderer getCellRenderer() {
        return new FrameRenderer() {
            protected void loadSlot(Slot slot) {
                RDFProperty rdfProperty = (RDFProperty) slot;
                addText(getText(rdfProperty));
            }
        };
    }


    public String getName() {
        return "Range";
    }


    public Comparator getSortComparator() {
        return new Comparator() {
            public int compare(Object o1, Object o2) {
                RDFProperty a = (RDFProperty) o1;
                RDFProperty b = (RDFProperty) o2;
                String ta = getText(a);
                String tb = getText(b);
                return ta.compareTo(tb);
            }
        };
    }


    private String getText(RDFProperty rdfProperty) {
        RDFResource range = rdfProperty.getRange();
        if (range == null) {
            return "";
        }
        else {
            return range.getBrowserText();
        }
    }


    public int getWidth() {
        return 200;
    }
}
