package edu.stanford.smi.protegex.owl.jena.importer;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protege.plugin.ImportPlugin;
import edu.stanford.smi.protege.ui.ProjectManager;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protege.util.WaitCursor;
import edu.stanford.smi.protegex.owl.jena.JenaFilePanel;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.jena.creator.OwlProjectFromUriCreator;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.dialogs.ModalDialogFactory;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class JenaImportPlugin implements ImportPlugin {
    private static transient Logger log = Log.getLogger(JenaImportPlugin.class);

    public void dispose() {
    }


    public String getName() {
        return "OWL File";
    }


    public Project handleImportRequest() {
        Project result = null;
        JenaFilePanel panel = new JenaFilePanel();
        int rval = ProtegeUI.getModalDialogFactory().showDialog(null, panel, "OWL File to Import", ModalDialogFactory.MODE_OK_CANCEL);
        if (rval == ModalDialogFactory.OPTION_OK) {
            String filePath = panel.getOWLFilePath();
            WaitCursor cursor = new WaitCursor(ProjectManager.getProjectManager().getMainPanel());
            try {
                result = importProject(new File(filePath).toURI());
            }
            finally {
                cursor.hide();
            }
        }
        return result;
    }


    @SuppressWarnings("unchecked")
    private Project importProject(URI uri) {
        java.util.Collection errors = new ArrayList();
        OwlProjectFromUriCreator creator  = new OwlProjectFromUriCreator();
        creator.setOntologyUri(uri.toString());
        try {
            creator.create(errors);
        }
        catch (OntologyLoadException ioe) {
            errors.add(ioe);
        }
        JenaOWLModel owlModel = creator.getOwlModel();
        if (errors.isEmpty()) {
            Project project = Project.createNewProject(null, new ArrayList());
            KnowledgeBase kb = project.getKnowledgeBase();
            new OWLImporter(owlModel, kb);
            return project;
        }
        else {
            for (Object error : errors) {
                if (error instanceof Throwable) {
                    log.log(Level.WARNING, "Exception caught", (Throwable) error);
                }
                else {
                    log.warning("Error found " + error);
                }
            }
            return null;
        }
    }
}
