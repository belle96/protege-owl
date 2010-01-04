package edu.stanford.smi.protegex.owl.ui.actions;

import edu.stanford.smi.protege.resource.Icons;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.ui.widget.OWLUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DeleteAllPropertyValuesAction extends AbstractAction {

    private RDFProperty property;

    private Collection targetFrames;

    private String type;


    public DeleteAllPropertyValuesAction(String type, RDFProperty property, Collection targetFrames, String partialName) {
        this(type, property, targetFrames, Icons.getBlankIcon());
    }


    public DeleteAllPropertyValuesAction(String type, RDFProperty property, Collection targetFrames, Icon icon) {
        super("Delete all values of selected property in " + type, icon);
        this.property = property;
        this.targetFrames = targetFrames;
        this.type = type;
    }


    public void actionPerformed(ActionEvent e) {
        OWLModel owlModel = property.getOWLModel();
        try {
            owlModel.beginTransaction((String) getValue(Action.NAME), (property == null ? null : property.getName()));
            for (Iterator it = targetFrames.iterator(); it.hasNext();) {
                RDFResource resource = (RDFResource) it.next();
                Collection values = resource.getPropertyValues(property);
                for (Iterator vit = values.iterator(); vit.hasNext();) {
                    Object val = (Object) vit.next();
                    resource.removePropertyValue(property, val);
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
