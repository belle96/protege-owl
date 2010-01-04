package edu.stanford.smi.protegex.owl.ui.properties.types;

import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.widget.AbstractPropertyWidget;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Iterator;

/**
 * A SlotWidget to edit additional RDF types of an RDFProperty
 * (e.g., FunctionalProperty, TransitiveProperty).
 * The widget basically consists of a stack of checkboxes where
 * the single types can be switched on or off.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public abstract class AbstractPropertyTypesWidget extends AbstractPropertyWidget {

    private TypeCheckBox[] checkBoxes;

    private class TypeCheckBox extends JCheckBox {

        private RDFSNamedClass type;


        TypeCheckBox(RDFSNamedClass type) {
            String name = type.getLocalName();
            setText(name.substring(0, name.length() - "Property".length()));
            this.type = type;
            addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    addOrRemoveType();
                }
            });
        }


        private void addOrRemoveType() {
            RDFProperty property = getEditedProperty();
            if (isSelected() && !property.hasProtegeType(type)) {
                if (isValidChange(type, true)) {
                    property.addProtegeType(type);
                    postProcessChange(type);
                }
                else {
                    setSelected(false);
                }
            }
            else if (!isSelected() && property.hasProtegeType(type)) {
                if (isValidChange(type, false)) {
                    property.removeProtegeType(type);
                    postProcessChange(type);
                }
                else {
                    setSelected(true);
                }
            }
        }


        void updateSelection() {
            boolean selected = getEditedProperty().hasProtegeType(type);
            if (!selected && isPropagatedType(type)) {
                selected = getSuperpropertyWithType(type) != null;
            }
            setSelected(selected);
        }
    }


    private RDFProperty getEditedProperty() {
        return (RDFProperty) getEditedResource();
    }


    private RDFProperty getSuperpropertyWithType(RDFSNamedClass type) {
        for (Iterator it = getEditedProperty().getSuperproperties(true).iterator(); it.hasNext();) {
            RDFProperty superproperty = (RDFProperty) it.next();
            if (superproperty.hasProtegeType(type)) {
                return superproperty;
            }
        }
        return null;
    }


    private String getTypeLabel(RDFSNamedClass type) {
        return type.getName();
    }


    protected void initialize(RDFSNamedClass[] types) {
        checkBoxes = new TypeCheckBox[types.length];
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        for (int i = 0; i < types.length; i++) {
            RDFSNamedClass type = types[i];
            checkBoxes[i] = new TypeCheckBox(type);
            add(checkBoxes[i]);
            add(Box.createVerticalStrut(4));
        }
    }


    private boolean isPropagatedType(RDFSClass type) {
        return type.equals(type.getOWLModel().getOWLFunctionalPropertyClass()) ||
                type.equals(type.getOWLModel().getOWLInverseFunctionalPropertyClass());
    }


    protected boolean isValidChange(RDFSNamedClass type, boolean value) {
        if (!value && isPropagatedType(type)) {
            RDFProperty superproperty = getSuperpropertyWithType(type);
            if (superproperty != null) {
                ProtegeUI.getModalDialogFactory().showMessageDialog(
                        type.getOWLModel(),
                        "This property already has the " + getTypeLabel(type) + "\n" +
                                "super-property " + superproperty.getBrowserText() + " and therefore\n" +
                                "must also remain " + getTypeLabel(type) + ".", "Warning");
                return false;
            }
        }
        return true;
    }


    /**
     * Can be overloaded to perform operations after the type has changed.
     *
     * @param type the type that was added/removed
     */
    protected void postProcessChange(RDFSNamedClass type) {

        RDFProperty property = getEditedProperty();
        RDFProperty inverseProperty = property.getInverseProperty();
        if (inverseProperty != null) {
            RDFSNamedClass functional = type.getOWLModel().getOWLFunctionalPropertyClass();
            RDFSNamedClass inverseFunctional = type.getOWLModel().getOWLInverseFunctionalPropertyClass();
            if (type.equals(functional)) {
                if (property.hasRDFType(functional)) {
                    if (!inverseProperty.hasRDFType(inverseFunctional)) {
                        inverseProperty.addRDFType(inverseFunctional);
                    }
                }
                else {  // !functional
                    if (inverseProperty.hasRDFType(inverseFunctional)) {
                        inverseProperty.removeRDFType(inverseFunctional);
                    }
                }
            }
            else {
                if (property.hasRDFType(inverseFunctional)) {
                    if (!inverseProperty.hasRDFType(functional)) {
                        inverseProperty.addRDFType(functional);
                    }
                }
                else {  // !functional
                    if (inverseProperty.hasRDFType(functional)) {
                        inverseProperty.removeRDFType(functional);
                    }
                }
            }
        }

        //Collection subproperties = getEditedProperty().getSubproperties(true);
        //if (isPropagatedType(type) && getEditedProperty().hasRDFType(type)) {
        //    for (Iterator it = subproperties.iterator(); it.hasNext();) {
        //        RDFProperty subproperty = (RDFProperty) it.next();
        //        if (!subproperty.hasRDFType(type)) {
        //            subproperty.addRDFType(type);
        //        }
        //    }
        //}
    }


    public void setEditable(boolean b) {
        for (int i = 0; i < checkBoxes.length; i++) {
            boolean enabled = b;
            TypeCheckBox checkBox = checkBoxes[i];
            if (isPropagatedType(checkBox.type)) {
                Collection superproperties = getEditedProperty().getSuperproperties(true);
                for (Iterator it = superproperties.iterator(); it.hasNext();) {
                    RDFProperty superproperty = (RDFProperty) it.next();
                    if (superproperty.hasRDFType(checkBox.type)) {
                        enabled = false;
                        break;
                    }
                }
            }
            checkBox.setEnabled(enabled);
        }
    }


    public void setValues(Collection values) {
        updateCheckBoxes();
    }


    private void updateCheckBoxes() {
        for (int i = 0; i < checkBoxes.length; i++) {
            TypeCheckBox checkBox = checkBoxes[i];
            checkBox.updateSelection();
        }
    }
    
    public void setEnabled(boolean enabled) {
    	setEditable(enabled);
    	super.setEnabled(enabled);
    };
}
