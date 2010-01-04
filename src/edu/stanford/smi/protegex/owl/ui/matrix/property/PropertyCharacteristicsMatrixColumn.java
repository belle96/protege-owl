package edu.stanford.smi.protegex.owl.ui.matrix.property;

import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.ui.FrameRenderer;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.OWLProperty;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.ui.matrix.MatrixColumn;

import javax.swing.table.TableCellRenderer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class PropertyCharacteristicsMatrixColumn implements MatrixColumn {

    public TableCellRenderer getCellRenderer() {
        return new FrameRenderer() {
            protected void loadSlot(Slot slot) {
                RDFProperty rdfProperty = (RDFProperty) slot;
                setGrayedSecondaryText(false);
                String text = getText(rdfProperty);
                if (text.length() > 0) {
                    addText(text);
                }
            }
        };
    }


    private String getText(RDFProperty rdfProperty) {
        Collection cs = new ArrayList();
        if (rdfProperty.isFunctional()) {
            cs.add("Functional");
        }
        if (rdfProperty instanceof OWLProperty) {
            if (((OWLProperty) rdfProperty).isInverseFunctional()) {
                cs.add("InverseFunctional");
            }
            if (rdfProperty instanceof OWLObjectProperty) {
                OWLObjectProperty objectSlot = (OWLObjectProperty) rdfProperty;
                if (objectSlot.isSymmetric()) {
                    cs.add("Symmetric");
                }
                if (objectSlot.isTransitive()) {
                    cs.add("Transitive");
                }
            }
        }
        Collection equis = rdfProperty.getEquivalentProperties();
        if (!equis.isEmpty()) {
            String str = "Equivalents: {";
            for (Iterator it = equis.iterator(); it.hasNext();) {
                Slot equi = (Slot) it.next();
                str += equi.getBrowserText();
                if (it.hasNext()) {
                    str += ", ";
                }
            }
            str += "}";
            cs.add(str);
        }
        Collection supers = rdfProperty.getSuperproperties(false);
        if (!supers.isEmpty()) {
            String str = "Super properties: {";
            for (Iterator it = supers.iterator(); it.hasNext();) {
                Slot equi = (Slot) it.next();
                str += equi.getBrowserText();
                if (it.hasNext()) {
                    str += ", ";
                }
            }
            str += "}";
            cs.add(str);
        }
        String str = "";
        if (!cs.isEmpty()) {
            for (Iterator it = cs.iterator(); it.hasNext();) {
                String s = (String) it.next();
                str += s;
                if (it.hasNext()) {
                    str += ", ";
                }
            }
        }
        return str;
    }


    public String getName() {
        return "Other Characteristics";
    }


    public int getWidth() {
        return 400;
    }
}
