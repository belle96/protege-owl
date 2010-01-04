package edu.stanford.smi.protegex.owl.ui.editors;

import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.widget.HTMLEditorPanel;
import edu.stanford.smi.protegex.owl.ui.widget.OWLWidgetUtil;

import java.awt.*;
import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class StringValueEditor implements PropertyValueEditor {

    public boolean canEdit(RDFResource instance, RDFProperty property, Object value) {
        final OWLModel owlModel = instance.getOWLModel();
        if (value != null) {
            if (value instanceof RDFSLiteral) {
                RDFSLiteral literal = (RDFSLiteral) value;
                return instance.getOWLModel().getXSDstring().equals(literal.getDatatype());
            }
            else {
                return value instanceof String;
            }
        }
        else {
            for (Iterator it = instance.getProtegeTypes().iterator(); it.hasNext();) {
                RDFSNamedClass type = (RDFSNamedClass) it.next();
                if (OWLWidgetUtil.isDatatypeProperty(owlModel.getXSDstring(), type, property)) {
                    return true;
                }
            }
        }
        return false;
    }


    public Object createDefaultValue(RDFResource instance, RDFProperty property) {
        final OWLModel owlModel = instance.getOWLModel();
        String lang = owlModel.getDefaultLanguage();
        if (lang != null) {
            return owlModel.createRDFSLiteral("", lang);
        }
        else {
            return "";
        }
    }


    public Object editValue(Component parent, RDFResource instance, RDFProperty property, Object value) {
        OWLModel owlModel = instance.getOWLModel();
        RDFSLiteral oldLiteral = null;
        if (value instanceof String) {
            oldLiteral = owlModel.createRDFSLiteral((String) value, owlModel.getXSDstring());
        }
        else if (value instanceof RDFSLiteral) {
            oldLiteral = (RDFSLiteral) value;
        }
        else {
            oldLiteral = owlModel.createRDFSLiteral("", owlModel.getXSDstring());
        }
        if (parent == null) {
            parent = ProtegeUI.getTopLevelContainer(property.getProject());
        }
        RDFSLiteral newLiteral = HTMLEditorPanel.show(parent, oldLiteral,
                "Edit " + property.getBrowserText() + " at " + instance.getBrowserText(), instance.getOWLModel());
        if (newLiteral != null && !oldLiteral.equals(newLiteral)) {
            if(newLiteral.getLanguage() == null || newLiteral.getLanguage().length() == 0) {
                return newLiteral.getString();
            }
            else {
                return newLiteral;
            }
        }
        else {
            return null;
        }
    }


    public boolean mustEdit(RDFResource subject, RDFProperty predicate, Object value) {
        return false;
    }
}
