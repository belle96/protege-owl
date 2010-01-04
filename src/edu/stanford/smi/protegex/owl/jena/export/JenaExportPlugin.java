package edu.stanford.smi.protegex.owl.jena.export;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.util.FileUtils;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protege.plugin.ExportPlugin;
import edu.stanford.smi.protege.ui.ProjectManager;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protege.util.MessageError;
import edu.stanford.smi.protege.util.WaitCursor;
import edu.stanford.smi.protegex.owl.database.OWLDatabaseModel;
import edu.stanford.smi.protegex.owl.jena.JenaFilePanel;
import edu.stanford.smi.protegex.owl.jena.JenaKnowledgeBaseFactory;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.jena.creator.NewOwlProjectCreator;
import edu.stanford.smi.protegex.owl.jena.writersettings.JenaWriterSettings;
import edu.stanford.smi.protegex.owl.jena.writersettings.WriterSettings;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.storage.ProtegeSaver;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.dialogs.ModalDialogFactory;
import edu.stanford.smi.protegex.owl.writer.rdfxml.rdfwriter.OWLModelAllTripleStoresWriter;
import edu.stanford.smi.protegex.owl.writer.rdfxml.util.ProtegeWriterSettings;
import edu.stanford.smi.protegex.owl.writer.xml.XMLWriterPreferences;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class JenaExportPlugin implements ExportPlugin {

	public void handleExportRequest(Project project) {	
		JenaFilePanel panel = new JenaFilePanel();
		if (project.isMultiUserClient()) {
			panel.getUseNativeWriterCheckBox().setSelected(true);
			panel.getUseNativeWriterCheckBox().setEnabled(false);
		}

		int rval = ProtegeUI.getModalDialogFactory().showDialog(ProtegeUI.getTopLevelContainer(project),
				panel, "OWL File to Export", ModalDialogFactory.MODE_OK_CANCEL);
		if (rval == ModalDialogFactory.OPTION_OK) {
			String filePath = panel.getOWLFilePath();
			WaitCursor cursor = new WaitCursor(ProjectManager.getProjectManager().getMainPanel());
			try {
				exportProject(project.getKnowledgeBase(), filePath, panel.getUseNativeWriter());
			}
			catch (Exception ex) {
				Log.getLogger().log(Level.SEVERE, "Exception caught", ex);
				ProtegeUI.getModalDialogFactory().showErrorMessageDialog(panel,
						"Export failed. Please see console for details.\n" + ex);
			}
			finally {
				cursor.hide();
			}
		}
	}

	private void exportProject(KnowledgeBase kb, String filePath, boolean useNativeWriter) {
		Collection errors = new ArrayList();        
		JenaKnowledgeBaseFactory factory = new JenaKnowledgeBaseFactory();
		JenaOWLModel newOWLModel = null;
		URI fileURI = new File(filePath).toURI();

		if (kb instanceof OWLModel) { //OWL -> OWL
			Project newProject = Project.createNewProject(factory, errors);
			newOWLModel = (JenaOWLModel) newProject.getKnowledgeBase();			
			newProject.setProjectURI(fileURI);

			OWLModel oldOWLModel = (OWLModel)kb;
			WriterSettings writerSettings = oldOWLModel.getWriterSettings();
			oldOWLModel.setWriterSettings(useNativeWriter ? new ProtegeWriterSettings(newOWLModel) : new JenaWriterSettings(newOWLModel));
			if (oldOWLModel instanceof JenaOWLModel) {
				((JenaOWLModel)oldOWLModel).save(fileURI, FileUtils.langXMLAbbrev, errors);
			} else if (oldOWLModel instanceof OWLDatabaseModel) { // export an OWL Database model   	
				if (useNativeWriter) { //native writer
					ProtegeWriterSettings newWriterSettings = null;
					if (writerSettings instanceof ProtegeWriterSettings) { //try to preserve settings from exported model
						newWriterSettings = (ProtegeWriterSettings)writerSettings;
					} else {
						newWriterSettings = new ProtegeWriterSettings(newOWLModel);
						newWriterSettings.setSortAlphabetically(true);
					}            		 
					try {
						boolean useEntities = newWriterSettings.getUseXMLEntities();
						XMLWriterPreferences.getInstance().setUseNamespaceEntities(useEntities);
						OWLModelAllTripleStoresWriter writer = new OWLModelAllTripleStoresWriter
						(oldOWLModel, fileURI, newWriterSettings.isSortAlphabetically());
						writer.write();
					}
					catch (Exception ex) {
						String message = "Failed to save file " + fileURI;
						Log.getLogger().log(Level.SEVERE, message, ex);
						errors.add(new MessageError(ex, message));
					}
				} else { // Jena writer
					OntModel newModel = ((OWLDatabaseModel) oldOWLModel).getOntModel();
					OWLDatabaseModel dbModel = (OWLDatabaseModel) oldOWLModel;
					String xmlBase = dbModel.getTripleStoreModel().getActiveTripleStore().getOriginalXMLBase();
					String defaultNS = dbModel.getNamespaceManager().getDefaultNamespace();
					if (xmlBase == null) {
						if (defaultNS != null && defaultNS.endsWith("#")) {
							xmlBase = defaultNS.substring(0, defaultNS.length() -1);
						}
					}
					try {
						File file = new File(fileURI);
						//TT: writing to langXMl rather than langXMLAbbrev might be more efficient for DB mode; to be checked
						JenaOWLModel.save(file, newModel, FileUtils.langXML, defaultNS , xmlBase);
					} catch (Throwable t) {
						Log.getLogger().log(Level.SEVERE, "Errors at exporting the OWL Database to OWL file", t);
					}					            		
				}
			}
			oldOWLModel.setWriterSettings(writerSettings);
		} else {  // Any other Protege format	
			NewOwlProjectCreator creator = new NewOwlProjectCreator();
			try {
				creator.create(errors);				
			} catch (OntologyLoadException e) {
				Log.getLogger().log(Level.SEVERE, "Could not create new Jena OWL project", e);
			}
			newOWLModel = creator.getOwlModel();
			if (newOWLModel != null) {
				new ProtegeSaver(kb, newOWLModel, useNativeWriter).run();
				newOWLModel.save(fileURI, FileUtils.langXMLAbbrev, errors);
			}
		}		

		if (errors.size() == 0) {
			ProtegeUI.getModalDialogFactory().showMessageDialog(newOWLModel,
					"Project has been exported to:\n" + filePath);
		} else {
			ProtegeUI.getModalDialogFactory().showErrorMessageDialog(newOWLModel,
			"Export failed.\nPlease see console and logs for more details.");
		}

		if (newOWLModel != null) {
			try {			
				newOWLModel.getProject().dispose();
			} catch (Exception e) {
				Log.getLogger().log(Level.WARNING, "Errors at disposing temporary exported OWL model", e);
			}
		}

	}   

	public String getName() {
		return "OWL";
	}

	public void dispose() {}

}
