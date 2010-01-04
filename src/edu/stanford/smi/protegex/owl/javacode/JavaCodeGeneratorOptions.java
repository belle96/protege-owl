package edu.stanford.smi.protegex.owl.javacode;

import java.io.File;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface JavaCodeGeneratorOptions {

    boolean getAbstractMode();


    String getFactoryClassName();


    File getOutputFolder();


    String getPackage();


    boolean getSetMode();
    
    
    boolean getPrefixMode();
}
