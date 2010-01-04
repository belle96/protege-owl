package edu.stanford.smi.protegex.owl.ui.widget;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Facet;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSDatatype;
import edu.stanford.smi.protegex.owl.model.RDFSLiteral;
import edu.stanford.smi.protegex.owl.model.impl.XMLSchemaDatatypes;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class OWLDateTimeWidget extends OWLDateWidget implements TimePanel.Listener {


    private TimePanel timePanel;


    public OWLDateTimeWidget() {
        setPreferredColumns(3);
        timePanel = new TimePanel(this);      
    }


    protected RDFSLiteral createPropertyValue(Date date) {
        String str = XMLSchemaDatatypes.getDateString(date);
        RDFSDatatype datatype = getOWLModel().getRDFSDatatypeByURI(XSDDatatype.XSDdateTime.getURI());
        str += "T" + timePanel.getTime();
        return getOWLModel().createRDFSLiteral(str, datatype);
    }


    protected void deleteValue() {
        super.deleteValue();
        timePanel.clear();
    }


    protected Component getCenterComponent() {
        Component dateComponent = super.getCenterComponent();
        JPanel panel = new JPanel(new BorderLayout(4, 0));
        panel.add(BorderLayout.CENTER, dateComponent);
        panel.add(BorderLayout.EAST, timePanel);
        return panel;
    }


    protected void setValue(String s) {
        super.setValue(s);
        if (s != null) {
            int index = s.indexOf("T");
            if (index >= 0) {
                s = s.substring(index + 1);
                timePanel.setTime(s);
            }
            else {
                timePanel.clear();
            }
        }
        else {
            timePanel.clear();
        }
    }


    public void timeChanged(TimePanel timePanel) {
        updateValues();
    }


    protected void updateComponents() {

        super.updateComponents();

        RDFResource resource = getEditedResource();
        RDFProperty property = getRDFProperty();
        timePanel.setEnabled(!isReadOnlyConfiguredWidget() && resource != null && property != null && resource.isEditable());
    }


    /**
     * @param cls
     * @param slot
     * @param facet
     * @deprecated
     */
    public static boolean isSuitable(Cls cls, Slot slot, Facet facet) {
        return OWLWidgetMapper.isSuitable(OWLDateTimeWidget.class, cls, slot);
    }
}
