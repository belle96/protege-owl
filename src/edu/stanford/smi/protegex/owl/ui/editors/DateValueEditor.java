package edu.stanford.smi.protegex.owl.ui.editors;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.toedter.calendar.JCalendar;
import edu.stanford.smi.protege.util.LabeledComponent;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.model.impl.XMLSchemaDatatypes;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.dialogs.ModalDialogFactory;
import edu.stanford.smi.protegex.owl.ui.widget.OWLDateWidget;
import edu.stanford.smi.protegex.owl.ui.widget.OWLWidgetUtil;

import java.awt.*;
import java.util.Date;
import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DateValueEditor implements PropertyValueEditor {

    public boolean canEdit(RDFResource instance, RDFProperty property, Object value) {
        final RDFSDatatype date = instance.getOWLModel().getXSDdate();
        return canEdit(date, instance, property, value);
    }


    static boolean canEdit(final RDFSDatatype dataType, RDFResource instance, RDFProperty property, Object value) {
        if (value instanceof RDFSLiteral) {
            RDFSLiteral literal = (RDFSLiteral) value;
            if (dataType.equals(literal.getDatatype())) {
                return true;
            }
        }
        for (Iterator it = instance.getRDFTypes().iterator(); it.hasNext();) {
            RDFSClass type = (RDFSClass) it.next();
            if (type instanceof RDFSNamedClass) {
                if (OWLWidgetUtil.isDatatypeProperty(dataType, (RDFSNamedClass) type, property)) {
                    return true;
                }
            }
        }
        return false;
    }


    public Object createDefaultValue(RDFResource instance, RDFProperty property) {
        String str = XMLSchemaDatatypes.getDefaultDateValue();
        return createRDFSLiteral(property.getOWLModel(), str);
    }


    private RDFSLiteral createRDFSLiteral(OWLModel owlModel, String str) {
        return owlModel.createRDFSLiteral(str, owlModel.getRDFSDatatypeByURI(XSDDatatype.XSDdate.getURI()));
    }


    public Object editValue(Component parent, RDFResource instance, RDFProperty property, Object value) {
        if (canEdit(instance, property, value)) {
            JCalendar calendar = new JCalendar(OWLDateWidget.getDate(value.toString()));
            String name = property.getBrowserText();
            name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
            LabeledComponent lc = new LabeledComponent(name, calendar);
            int r = ProtegeUI.getModalDialogFactory().showDialog(parent, lc, "Edit " + property.getBrowserText(), ModalDialogFactory.MODE_OK_CANCEL);
            if (r == ModalDialogFactory.OPTION_OK) {
                Date date = calendar.getDate();
                String newValue = XMLSchemaDatatypes.getDateString(date);
                return createRDFSLiteral(property.getOWLModel(), newValue);
            }
        }
        return null;
    }


    public boolean mustEdit(RDFResource subject, RDFProperty predicate, Object value) {
        return canEdit(subject, predicate, value);
    }
}
