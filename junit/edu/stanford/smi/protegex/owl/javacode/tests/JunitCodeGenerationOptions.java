package edu.stanford.smi.protegex.owl.javacode.tests;

import java.io.File;

import edu.stanford.smi.protegex.owl.javacode.EditableJavaCodeGeneratorOptions;

public class JunitCodeGenerationOptions implements EditableJavaCodeGeneratorOptions {
    private boolean abstractMode = false;
    private String factoryClassName = null;
    private File outputFolder = null;
    private String myPackage = null;
    private boolean setMode = false;
    private boolean prefixMode = false;
    
    public boolean getAbstractMode() {
        return abstractMode;
    }
    public void setAbstractMode(boolean abstractMode) {
        this.abstractMode = abstractMode;
    }
    public String getFactoryClassName() {
        return factoryClassName;
    }
    public void setFactoryClassName(String factoryClassName) {
        this.factoryClassName = factoryClassName;
    }
    public File getOutputFolder() {
        return outputFolder;
    }
    public void setOutputFolder(File outputFolder) {
        this.outputFolder = outputFolder;
    }
    public boolean getSetMode() {
        return setMode;
    }
    public void setSetMode(boolean setMode) {
        this.setMode = setMode;
    }
    public boolean getPrefixMode() {
        return prefixMode;
    }
    public void setPrefixMode(boolean prefixMode) {
        this.prefixMode = prefixMode;
    }
    
    public void setPackage(String value) {
        myPackage = value;
    }
    public String getPackage() {
        return myPackage;
    }

    
}
