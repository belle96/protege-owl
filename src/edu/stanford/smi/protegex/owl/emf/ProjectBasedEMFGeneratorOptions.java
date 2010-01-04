package edu.stanford.smi.protegex.owl.emf;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.project.OWLProject;

import java.io.File;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ProjectBasedEMFGeneratorOptions implements EditableEMFGeneratorOptions {

    public final static String FILE_NAME = "EMFFileName";

    public final static String PACKAGE = "EMFPackage";

    private OWLProject project;


    public ProjectBasedEMFGeneratorOptions(OWLModel owlModel) {
        this.project = owlModel.getOWLProject();
    }


    public File getOutputFolder() {
        String fileName = project.getSettingsMap().getString(FILE_NAME);
        if (fileName == null) {
            return new File("");
        }
        else {
            return new File(fileName);
        }
    }


    public String getPackage() {
        return project.getSettingsMap().getString(PACKAGE);
    }


    public void setOutputFolder(File file) {
        if (file == null) {
            project.getSettingsMap().remove(FILE_NAME);
        }
        else {
            project.getSettingsMap().setString(FILE_NAME, file.getAbsolutePath());
        }
    }


    public void setPackage(String value) {
        if (value == null) {
            project.getSettingsMap().remove(PACKAGE);
        }
        else {
            project.getSettingsMap().setString(PACKAGE, value);
        }
    }
}
