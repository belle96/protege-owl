package edu.stanford.smi.protegex.owl.javacode;

import java.io.File;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface EditableJavaCodeGeneratorOptions extends JavaCodeGeneratorOptions {

    void setAbstractMode(boolean value);


    void setFactoryClassName(String value);


    void setOutputFolder(File file);


    void setPackage(String value);


    void setSetMode(boolean value);
    
    
    void setPrefixMode(boolean value);
}
