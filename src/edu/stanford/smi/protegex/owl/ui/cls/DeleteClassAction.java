package edu.stanford.smi.protegex.owl.ui.cls;

import edu.stanford.smi.protege.resource.LocalizedText;
import edu.stanford.smi.protege.resource.ResourceKey;
import edu.stanford.smi.protege.ui.ProjectManager;
import edu.stanford.smi.protege.util.ComponentUtilities;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.actions.ResourceAction;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.widget.OWLUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * A ResourceAction for deleted the selected named classes inside a ClassTreePanel.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DeleteClassAction extends ResourceAction {

    public final static String TEXT = "Delete class";


    public DeleteClassAction() {
        super(TEXT, OWLIcons.getDeleteClsIcon());
    }


    public void actionPerformed(ActionEvent e) {
        ClassTreePanel classTreePanel = (ClassTreePanel) getComponent();
        Collection<RDFSNamedClass> clses = ComponentUtilities.getSelection(classTreePanel.getTree());
        performAction(clses, classTreePanel);
    }


    private static boolean confirmDelete(Component parent) {
        boolean delete = true;
        if (ProjectManager.getProjectManager().getCurrentProject().getDisplayConfirmationOnRemove()) {
            String text = LocalizedText.getText(ResourceKey.DIALOG_CONFIRM_DELETE_TEXT);
            delete = ProtegeUI.getModalDialogFactory().showConfirmDialog(parent, text, "Confirm Delete");
        }
        return delete;
    }

    public boolean isSuitable(Component component, RDFResource resource) {
        return component instanceof ClassTreePanel &&
               resource instanceof RDFSNamedClass &&
               resource.isEditable();
    }


    public static void performAction(RDFSNamedClass cls, ClassTreePanel classTreePanel) {
        performAction(Collections.singleton(cls), classTreePanel);
    }


    public static void performAction(Collection<RDFSNamedClass> clses, ClassTreePanel classTreePanel) {

        if (confirmDelete((Component) classTreePanel)) {
            for (Iterator<RDFSNamedClass> i = clses.iterator(); i.hasNext();) {
                RDFSNamedClass cls = i.next();

                if (cls.getInstanceCount(true) > 0) {
                    String text = LocalizedText.getText(ResourceKey.DELETE_CLASS_FAILED_DIALOG_TEXT);
                    text += "\n " + cls.getBrowserText();
                    ProtegeUI.getModalDialogFactory().showMessageDialog(cls.getOWLModel(), text);
                }
                else {
                    JTree tree = classTreePanel.getTree();
                    ComponentUtilities.removeSelection(tree);
                    //TODO: should this be done in a transaction?
                    try {
                    	cls.delete();
					} catch (Exception e) {						
						OWLUI.handleError(null, e);
					}                    
                }
            }
        }
    }
}
