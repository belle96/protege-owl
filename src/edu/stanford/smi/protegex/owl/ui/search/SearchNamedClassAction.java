package edu.stanford.smi.protegex.owl.ui.search;

import edu.stanford.smi.protege.resource.Icons;
import edu.stanford.smi.protege.ui.SubclassPane;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.actions.ResourceAction;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class SearchNamedClassAction extends ResourceAction {

    public static final String GROUP = "Search and View/";


    public SearchNamedClassAction() {
        super("Search subclass by property value...", Icons.getFindIcon(), GROUP);
    }


    public boolean isSuitable(Component component, RDFResource resource) {
        return component instanceof SubclassPane && resource instanceof RDFSNamedClass;
    }


    public void actionPerformed(ActionEvent e) {
        RDFSNamedClass rootClass = (RDFSNamedClass) getResource();
        SubclassPane subclassPane = (SubclassPane) getComponent();
        List result = SearchNamedClassPanel.showDialog(getComponent(), rootClass);
        if (result != null) {
            if (result.size() == 0) {
                ProtegeUI.getModalDialogFactory().showMessageDialog(rootClass.getOWLModel(),
                        "There were no matching classes.");
            }
            else {
                OWLNamedClass selectedCls = (OWLNamedClass) ProtegeUI.getSelectionDialogFactory().
                        selectResourceFromCollection(getComponent(),
                                rootClass.getOWLModel(), result, "Select a matching class");
                if (selectedCls != null) {
                    subclassPane.setSelectedCls(selectedCls);
                }
            }
        }
    }
}
