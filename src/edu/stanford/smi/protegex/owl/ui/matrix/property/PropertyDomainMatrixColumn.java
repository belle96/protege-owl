package edu.stanford.smi.protegex.owl.ui.matrix.property;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.ui.FrameRenderer;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.impl.DefaultOWLUnionClass;
import edu.stanford.smi.protegex.owl.ui.matrix.SortableMatrixColumn;

import javax.swing.table.TableCellRenderer;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class PropertyDomainMatrixColumn implements SortableMatrixColumn {

    public TableCellRenderer getCellRenderer() {
        return new FrameRenderer() {
            protected void loadSlot(Slot slot) {
                RDFProperty rdfProperty = (RDFProperty) slot;
                addText(getText(rdfProperty));
            }
        };
    }


    public String getName() {
        return "Domain";
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
        String str = "";
        Collection domain = rdfProperty.getUnionDomain();
        for (Iterator it = domain.iterator(); it.hasNext();) {
            Cls cls = (Cls) it.next();
            str += cls.getBrowserText();
            if (it.hasNext()) {
                str += " " + DefaultOWLUnionClass.OPERATOR + " ";
            }
        }
        return str;
    }


    public int getWidth() {
        return 200;
    }
}
