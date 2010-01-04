package edu.stanford.smi.protegex.owl.jena.export;

import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protege.plugin.ExportPlugin;
import edu.stanford.smi.protege.ui.ProjectView;
import edu.stanford.smi.protegex.owl.jena.JenaKnowledgeBaseFactory;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.widget.OWLUI;

import javax.swing.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public abstract class AbstractJenaFileExportPlugin implements ExportPlugin {

    private String lang;


    protected AbstractJenaFileExportPlugin(String lang) {
        this.lang = lang;
    }


    public void dispose() {
    }


    public String getName() {
        return lang;
    }


    public void handleExportRequest(Project project) {
        KnowledgeBase kb = project.getKnowledgeBase();
        if (kb instanceof JenaOWLModel) {
            JenaOWLModel owlModel = (JenaOWLModel) kb;
            Collection errors = new ArrayList();
            JFileChooser fileChooser = OWLUI.createJFileChooser(lang, JenaKnowledgeBaseFactory.getExtension(lang));
            if (fileChooser.showSaveDialog(ProtegeUI.getTopLevelContainer(kb.getProject())) == JFileChooser.APPROVE_OPTION) {
                URI fileURI = fileChooser.getSelectedFile().toURI();
                owlModel.save(fileURI, lang, errors);
                if (errors.isEmpty()) {
                    ProtegeUI.getModalDialogFactory().showMessageDialog(owlModel, "Successfully exported to " + fileURI);
                }
                else {
                    ProtegeUI.getModalDialogFactory().showErrorMessageDialog(owlModel, "Could not export:\n" + errors.iterator().next());
                }
            }
        }
        else {
            ProjectView projectView = ProtegeUI.getProjectView(project);
            ProtegeUI.getModalDialogFactory().showMessageDialog(projectView,
                    "This function can only export OWL File projects.");
        }
    }
}
