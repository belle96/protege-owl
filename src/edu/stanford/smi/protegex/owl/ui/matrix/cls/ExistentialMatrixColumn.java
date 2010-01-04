package edu.stanford.smi.protegex.owl.ui.matrix.cls;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.ui.FrameRenderer;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLSomeValuesFrom;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.impl.DefaultOWLSomeValuesFrom;
import edu.stanford.smi.protegex.owl.ui.matrix.DependentMatrixColumn;
import edu.stanford.smi.protegex.owl.ui.matrix.MatrixColumn;

import javax.swing.table.TableCellRenderer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ExistentialMatrixColumn implements DependentMatrixColumn, MatrixColumn {

    private RDFProperty property;


    public ExistentialMatrixColumn(RDFProperty property) {
        this.property = property;
    }


    public TableCellRenderer getCellRenderer() {
        return new FrameRenderer() {
            protected void loadCls(Cls cls) {
                if (cls instanceof OWLNamedClass) {
                    Collection cs = getMatchingRestrictions((OWLNamedClass) cls);
                    setGrayedSecondaryText(false);
                    for (Iterator it = cs.iterator(); it.hasNext();) {
                        OWLSomeValuesFrom someRestriction = (OWLSomeValuesFrom) it.next();
                        addText(someRestriction.getFillerText());
                        if (it.hasNext()) {
                            addText(", ");
                        }
                    }
                }
            }
        };
    }


    private Collection getMatchingRestrictions(OWLNamedClass cls) {
        List result = new ArrayList();
        if (cls.getDefinition() == null) {
            for (Iterator it = cls.getSuperclasses(false).iterator(); it.hasNext();) {
                Cls superCls = (Cls) it.next();
                if (superCls instanceof OWLSomeValuesFrom) {
                    if (property.equals(((OWLSomeValuesFrom) superCls).getOnProperty())) {
                        result.add(superCls);
                    }
                }
            }
        }
        return result;
    }


    public String getName() {
        return DefaultOWLSomeValuesFrom.OPERATOR + " " + property.getBrowserText();
    }


    public RDFProperty getProperty() {
        return property;
    }


    public int getWidth() {
        return 200;
    }


    public boolean isDependentOn(RDFResource instance) {
        return property.equals(instance);
    }
}
