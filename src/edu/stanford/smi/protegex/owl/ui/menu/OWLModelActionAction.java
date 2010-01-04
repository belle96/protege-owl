package edu.stanford.smi.protegex.owl.ui.menu;

import edu.stanford.smi.protege.resource.Icons;
import edu.stanford.smi.protege.util.Disposable;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.ui.actions.OWLModelAction;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * A Swing Action wrapping an OWLModelAction.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class OWLModelActionAction extends AbstractAction implements Disposable {

    private OWLModel owlModel;

    private OWLModelAction owlModelAction;

    private PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent evt) {
            setEnabled(owlModelAction.isSuitable(owlModel));
        }
    };


    public OWLModelActionAction(OWLModelAction owlModelAction, OWLModel owlModel) {
        super(owlModelAction.getName(), getIcon(owlModelAction));
        this.owlModelAction = owlModelAction;
        this.owlModel = owlModel;
        setEnabled(owlModelAction.isSuitable(owlModel));
        owlModelAction.addPropertyChangeListener(propertyChangeListener);
    }


    public void actionPerformed(ActionEvent e) {
        owlModelAction.run(owlModel);
    }


    public void dispose() {
        owlModelAction.removePropertyChangeListener(propertyChangeListener);
    }


    public static Icon getIcon(OWLModelAction owlModelAction) {
        Icon icon = Icons.getBlankIcon();
        String fileName = owlModelAction.getIconFileName();
        if (fileName != null) {
            Class c = owlModelAction.getIconResourceClass();
            if (c == null) {
                c = OWLIcons.class;
            }
            if (!fileName.endsWith(".gif") && !fileName.endsWith(".png")) {
                fileName += ".gif";
            }
            icon = OWLIcons.getImageIcon(fileName, c);
        }
        return icon;
    }
}
