package edu.stanford.smi.protegex.owl.model.impl;

import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.model.classparser.OWLClassParseException;
import edu.stanford.smi.protegex.owl.model.classparser.OWLClassParser;
import edu.stanford.smi.protegex.owl.model.visitor.OWLModelVisitor;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import java.util.Set;

/**
 * A Cls representing a hasValue restriction.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultOWLHasValue extends AbstractOWLRestriction
        implements OWLHasValue {

    /**
     * The unicode operator symbol for this kind of restriction
     */
    public final static char OPERATOR = '\u220B';


    public DefaultOWLHasValue(KnowledgeBase kb, FrameID id) {
        super(kb, id);
    }


    public DefaultOWLHasValue() {
    }


    public void accept(OWLModelVisitor visitor) {
        visitor.visitOWLHasValue(this);
    }


    public void checkFillerText(String text) throws Exception {
        checkFillerText(text, getOnProperty());
    }


    public static void checkFillerText(String text, RDFProperty onProperty) throws OWLClassParseException {
        if (onProperty != null) {
            if (!(onProperty instanceof OWLDatatypeProperty)) {
                OWLModel owlModel = onProperty.getOWLModel();
                OWLClassParser parser = owlModel.getOWLClassDisplay().getParser();
                parser.checkHasValueFiller(owlModel, text);
            }
        }
    }


    public boolean equalsStructurally(RDFObject object) {
        if (object instanceof OWLHasValue) {
            OWLHasValue compCls = (OWLHasValue) object;
            RDFObject compVal;
            if (compCls.getHasValue() instanceof RDFResource) {
                compVal = (RDFResource) compCls.getHasValue();
            }
            else {
                compVal = (RDFObject) compCls.getOWLModel().asRDFSLiteral(compCls.getHasValue());
            }
            RDFObject val;
            if (getHasValue() instanceof RDFResource) {
                val = (RDFResource) getHasValue();
            }
            else {
                val = (RDFObject) compCls.getOWLModel().asRDFSLiteral(getHasValue());
            }
            return getOnProperty().equalsStructurally(compCls.getOnProperty()) &&
                    val.equalsStructurally(compVal);

        }
        return false;
    }

    /*public String getBrowserText() {
       return getBrowserTextPropertyName() + " " + OPERATOR + " " + getBrowserTextFiller();
   } */


    public RDFProperty getFillerProperty() {
        return getOWLModel().getRDFProperty(OWLNames.Slot.HAS_VALUE);
    }


    public String getFillerText() {
        Object value = getHasValue();
        if (value == null) {
            return "";
        }
        else {
            if (value instanceof RDFResource) {
                return ((RDFResource) value).getBrowserText();
            }
            else {
                if (value instanceof String) {
                    return "\"" + value + "\"";
                }
                else {
                    return value.toString();
                }
            }
        }
    }


    public Object getHasValue() {
        return getPropertyValue(getFillerProperty());
    }


    public String getIconName() {
        return OWLIcons.OWL_HAS_VALUE;
    }


    public void getNestedNamedClasses(Set set) {
        if (getHasValue() instanceof RDFSClass) {
            ((RDFSClass) getHasValue()).getNestedNamedClasses(set);
        }
    }


    public char getOperator() {
        return OPERATOR;
    }


    public void setFillerText(String text) throws Exception {
        OWLModel owlModel = getOWLModel();
        OWLClassParser parser = owlModel.getOWLClassDisplay().getParser();
        if (getOnProperty() instanceof OWLDatatypeProperty) {
            Object value = text;
            try {
                value = parser.parseHasValueFiller(owlModel, text);
            }
            catch (Exception ex) {
                // Ignore -> use string as it is
            }
            setHasValue(value);
        }
        else {
            Object value = parser.parseHasValueFiller(owlModel, text);
            setHasValue(value);
        }
    }


    public void setHasValue(Object value) {
        if (value instanceof Double) {
            value = new Float(((Double) value).doubleValue());
        }
        if (value instanceof Long) {
            value = new Integer(((Long) value).intValue());
        }
        setDirectOwnSlotValue(getFillerProperty(), value);
    }
}
