package edu.stanford.smi.protegex.owl.jena;

import java.io.File;
import java.net.URI;
import java.util.Collection;
import java.util.logging.Level;

import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protege.plugin.CreateProjectFromFilePlugin;
import edu.stanford.smi.protege.util.FileUtilities;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protege.util.MessageError;
import edu.stanford.smi.protege.util.URIUtilities;
import edu.stanford.smi.protegex.owl.jena.creator.OwlProjectFromUriCreator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateOWLProjectFromFilePlugin implements CreateProjectFromFilePlugin {


    @SuppressWarnings("unchecked")
    public Project createProject(File file, Collection errors) {
        OwlProjectFromUriCreator creator = new OwlProjectFromUriCreator();
        try {   
            JenaKnowledgeBaseFactory.useStandalone = false;
            creator.setOntologyUri(URIUtilities.createURI(file.getPath()).toString());            
            creator.create(errors);
        }
        catch (Exception ex) {
            errors.add(new MessageError(ex, "Ontology content might be incomplete or corrupted.\nSee console or log for the full stack trace."));
            Log.getLogger().log(Level.SEVERE, "Error loading file with the CreateOWLProjectFromFilePlugin", ex);
        }
        return creator.getProject();
    }


    public String[] getSuffixes() {
        return new String[]{
                "owl"
        };
    }


    public String getDescription() {
        return "OWL Files";
    }
    
    protected URI getBuildProjectURI(File file) {
    	String pprjFilePath = FileUtilities.replaceExtension(file.getAbsolutePath(), ".pprj");

    	try {
          	File pprjFile = new File(pprjFilePath);
                    	
            if (pprjFile.exists()) {
            	// Uncomment this part of the code if you want the user to be asked whether the pprj file should be loaded or not
            	/*int selection = ModalDialog.showMessageDialog(ProjectManager.getProjectManager().getMainPanel(), "A project file corresponding" +
                   				" to the OWL file has been found at:" +
                   				"\n" + pprjFilePath + "\nDo you want to load it?", ModalDialog.MODE_YES_NO);
                if (selection == ModalDialog.OPTION_YES)*/
                	return URIUtilities.createURI(pprjFilePath);
                /*else 
                	return null;*/
            }
       }
         catch (Exception ex) {
            Log.emptyCatchBlock(ex);
       }
         return null;
    }
    
}
