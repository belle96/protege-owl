package edu.stanford.smi.protegex.owl.javacode;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.project.OWLProject;

import java.io.File;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ProjectBasedJavaCodeGeneratorOptions implements EditableJavaCodeGeneratorOptions {

    public final static String ABSTRACT_MODE = "JavaCodeAbstract";

    public final static String FACTORY_CLASS_NAME = "JavaCodeFactoryClassName";

    public final static String FILE_NAME = "JavaCodeFileName";

    public final static String PACKAGE = "JavaCodePackage";

    public final static String SET_MODE = "JavaCodeSet";

    public final static String PREFIX_MODE = "JavaCodeUsePrefix";

    private OWLProject project;


    public ProjectBasedJavaCodeGeneratorOptions(OWLModel owlModel) {
        this.project = owlModel.getOWLProject();
    }


    public boolean getAbstractMode() {
        Boolean b = project.getSettingsMap().getBoolean(ABSTRACT_MODE);
        if (b != null) {
            return b.booleanValue();
        }
        else {
            return false;
        }
    }


    public String getFactoryClassName() {
        String value = project.getSettingsMap().getString(FACTORY_CLASS_NAME);
        if (value == null) {
            return "MyFactory";
        }
        else {
            return value;
        }
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


    public boolean getSetMode() {
        Boolean b = project.getSettingsMap().getBoolean(SET_MODE);
        if (b != null) {
            return b.booleanValue();
        }
        else {
            return false;
        }
    }


    public boolean getPrefixMode() {
    	Boolean b = project.getSettingsMap().getBoolean(PREFIX_MODE);
    	if (b != null) {
    		return b.booleanValue();
    	}
    	else {
    		return false;
    	}
    }


    public void setAbstractMode(boolean value) {
        project.getSettingsMap().setBoolean(ABSTRACT_MODE, value);
    }


    public void setOutputFolder(File file) {
        if (file == null) {
            project.getSettingsMap().remove(FILE_NAME);
        }
        else {
            project.getSettingsMap().setString(FILE_NAME, file.getAbsolutePath());
        }
    }


    public void setFactoryClassName(String value) {
        if (value == null || value.length() == 0) {
            project.getSettingsMap().remove(FACTORY_CLASS_NAME);
        }
        else {
            project.getSettingsMap().setString(FACTORY_CLASS_NAME, value);
        }
    }


    public void setPackage(String value) {
        if (value == null || value.length() == 0) {
            project.getSettingsMap().remove(PACKAGE);
        }
        else {
            project.getSettingsMap().setString(PACKAGE, value);
        }
    }


    public void setSetMode(boolean value) {
        project.getSettingsMap().setBoolean(SET_MODE, value);
    }
    
    
    public void setPrefixMode(boolean value) {
    	project.getSettingsMap().setBoolean(PREFIX_MODE, value);
}
}
