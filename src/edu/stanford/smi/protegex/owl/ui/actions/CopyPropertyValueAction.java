package edu.stanford.smi.protegex.owl.ui.actions;

import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.dialogs.ModalDialogFactory;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CopyPropertyValueAction extends AbstractAction {

    private RDFProperty property;

    private Object value;

    private Collection targetFrames;

    private String type;


    public CopyPropertyValueAction(String type, RDFProperty property, Object value, Collection targetResources) {
        this(type, property, value, targetResources, "annotation");
    }


    public CopyPropertyValueAction(String type, RDFProperty property, Object value, Collection targetResources, String partialName) {
        this(type, property, value, targetResources, partialName, OWLIcons.getImageIcon("CopyAnnotation"));
    }


    public CopyPropertyValueAction(String type, RDFProperty property, Object value, Collection targetFrames, String partialLabel, Icon icon) {
        super("Copy " + partialLabel + " value into " + type + "...", icon);
        this.property = property;
        this.value = value;
        this.targetFrames = targetFrames;
        this.type = type;
    }


    public void actionPerformed(ActionEvent e) {
        int result = ProtegeUI.getModalDialogFactory().showConfirmCancelDialog(property.getOWLModel(),
                "Shall the selected value replace any existing values\n" +
                        "of the " + property.getName() + " property at all " + type + ",\n" +
                        "if there already is exactly one value?",
                (String) getValue(Action.NAME));
        if (result == ModalDialogFactory.OPTION_YES) {
            for (Iterator it = targetFrames.iterator(); it.hasNext();) {
                RDFResource resource = (RDFResource) it.next();
                if (resource.getPropertyValues(property).size() == 1) {
                    resource.setPropertyValue(property, value);
                }
                else {
                    resource.addPropertyValue(property, value);
                }
            }
        }
        else if (result == JOptionPane.NO_OPTION) {
            for (Iterator it = targetFrames.iterator(); it.hasNext();) {
                RDFResource resource = (RDFResource) it.next();
                if (resource.getPropertyValues(property).size() != 1) {
                    resource.addPropertyValue(property, value);
                }
            }
        }
    }
}
