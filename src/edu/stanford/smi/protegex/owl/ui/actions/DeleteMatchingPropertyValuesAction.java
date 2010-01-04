package edu.stanford.smi.protegex.owl.ui.actions;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.widget.OWLUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DeleteMatchingPropertyValuesAction extends AbstractAction {

    private RDFProperty property;

    private Object value;

    private Collection targetFrames;

    private String type;


    public DeleteMatchingPropertyValuesAction(String type, RDFProperty property, Object value, Collection targetResources) {
        this(type, property, value, targetResources, "annotation");
    }


    public DeleteMatchingPropertyValuesAction(String type, RDFProperty property, Object value, Collection targetResources,
                                              String partialName) {
        super("Delete selected " + partialName + " value in " + type + "...", OWLIcons.getDeleteIcon(OWLIcons.SUB_CLASS));
        this.property = property;
        this.value = value;
        this.targetFrames = targetResources;
        this.type = type;
    }


    public void actionPerformed(ActionEvent e) {
        OWLModel owlModel = property.getOWLModel();
        try {
            owlModel.beginTransaction("Delete annotation " + value +
                    " from property " + property.getBrowserText() + " at multiple " + type, (property == null ? null : property.getName()));
            for (Iterator it = targetFrames.iterator(); it.hasNext();) {
                RDFResource resource = (RDFResource) it.next();
                if (resource.getPropertyValues(property).contains(value)) {
                    resource.removePropertyValue(property, value);
                }
            }
            owlModel.commitTransaction();
        }
        catch (Exception ex) {
        	owlModel.rollbackTransaction();
            OWLUI.handleError(owlModel, ex);
        }
    }
}
