package edu.stanford.smi.protegex.owl.ui.widget;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Facet;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.util.LabeledComponent;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSLiteral;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.Date;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class OWLTimeWidget extends AbstractPropertyWidget implements TimePanel.Listener {


    private Action deleteAction = new AbstractAction("Delete value", OWLIcons.getDeleteIcon()) {
        public void actionPerformed(ActionEvent e) {
            timePanel.clear();
            timeChanged(timePanel);
        }
    };

    private LabeledComponent lc;

    private Action nowAction = new AbstractAction("Set to now", OWLIcons.getAddIcon()) {
        public void actionPerformed(ActionEvent e) {
            timePanel.setTime(new Date());
            timeChanged(timePanel);
        }
    };

    private TimePanel timePanel;


    public void initialize() {
        timePanel = new TimePanel(this);
        setLayout(new BorderLayout());
        lc = new LabeledComponent(getRDFProperty().getBrowserText(), timePanel);
        lc.addHeaderButton(nowAction);
        lc.addHeaderButton(deleteAction);
        add(BorderLayout.CENTER, lc);
    }


    public void setEditable(boolean b) {
        super.setEditable(b);
        nowAction.setEnabled(b);
        deleteAction.setEnabled(b);
    }


    public void setInstance(Instance newInstance) {
        super.setInstance(newInstance);
        if (newInstance != null) {
            setTimePanelValue();
        }
    }


    private void setTimePanelValue() {
        RDFResource resource = getEditedResource();
        RDFProperty property = getRDFProperty();
        Object value = resource.getPropertyValue(property);
        if (value == null) {
            timePanel.clear();
        }
        else {
            timePanel.setTime(value.toString());
        }
    }


    public void timeChanged(TimePanel timePanel) {
        RDFResource resource = getEditedResource();
        RDFProperty property = getRDFProperty();
        if (!timePanel.isNull()) {
            String newDate = timePanel.getTime();
            RDFSLiteral literal = getOWLModel().createRDFSLiteral(newDate,
                    getOWLModel().getRDFSDatatypeByURI(XSDDatatype.XSDtime.getURI()));
            resource.setPropertyValue(property, literal);
        }
        else {
            resource.setPropertyValues(property, Collections.EMPTY_LIST);
        }
    }


    /**
     * @param cls
     * @param slot
     * @param facet
     * @deprecated
     */
    public static boolean isSuitable(Cls cls, Slot slot, Facet facet) {
        return OWLWidgetMapper.isSuitable(OWLTimeWidget.class, cls, slot);
    }
}
