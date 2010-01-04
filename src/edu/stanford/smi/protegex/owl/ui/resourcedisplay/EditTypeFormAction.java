package edu.stanford.smi.protegex.owl.ui.resourcedisplay;

import edu.stanford.smi.protege.ui.ProjectView;
import edu.stanford.smi.protege.widget.FormsTab;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.widget.OWLFormsTab;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * An Action that shows the form editor for the direct type of a given Instance.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class EditTypeFormAction extends AbstractAction {

    private RDFResource resource;


    public EditTypeFormAction(RDFResource resource) {
        super("Edit form of type...", OWLIcons.getImageIcon("EditTypeForm"));
        this.resource = resource;
    }


    public void actionPerformed(ActionEvent e) {

        if (ensureMetaClsVisible(resource)) {

            RDFSClass directType = resource.getProtegeType();

            ProjectView currentProjectView = ProtegeUI.getProjectView(directType.getProject());

            JTabbedPane tabbedPane = new JTabbedPane();
            Component[] comps = currentProjectView.getComponents();
            for (int i = 0; i < comps.length; i++) {
                Component c = comps[i];
                if (c instanceof JTabbedPane) {
                    tabbedPane = (JTabbedPane) c;
                }
            }

            FormsTab formsTab = (FormsTab) currentProjectView.getTabByClassName(OWLFormsTab.class.getName());
            if (formsTab == null){
                formsTab = (FormsTab) currentProjectView.getTabByClassName(FormsTab.class.getName());
            }

            if (formsTab != null) {
                tabbedPane.setSelectedComponent(formsTab);
                formsTab.setSelectedCls(directType);
            }
            else {
                ProtegeUI.getModalDialogFactory().showErrorMessageDialog(resource.getOWLModel(),
                        "The Forms tab is not visible.\nPlease use Project/Configure...");
            }
        }
    }


    static boolean ensureMetaClsVisible(RDFResource instance) {
        RDFSClass directType = instance.getProtegeType();
        RDFSClass propertyMetaclass = instance.getOWLModel().getRDFPropertyClass();
        OWLModel owlModel = instance.getOWLModel();
        if (!directType.isVisible() || directType.isSubclassOf(propertyMetaclass) && !propertyMetaclass.isVisible()) {
            final String message = "The class " + directType.getBrowserText() +
                    " is not visible.\nShould it be made visible now?";
            if (ProtegeUI.getModalDialogFactory().showConfirmDialog(owlModel, message, "Confirm")) {
                if (directType.getName().equals(OWLNames.Cls.NAMED_CLASS)) {
                    owlModel.getRDFSNamedClassClass().setVisible(true);
                }
                if (directType.isSubclassOf(owlModel.getRDFSNamedClass(RDFNames.Cls.PROPERTY))) {
                    owlModel.getRDFPropertyClass().setVisible(true);
                    owlModel.getOWLObjectPropertyClass().setVisible(true);
                    owlModel.getOWLDatatypePropertyClass().setVisible(true);
                }
                directType.setVisible(true);
                ProtegeUI.reloadUI(owlModel.getProject());
                return true;
            }
            return false;
        }
        return true;
    }
}
