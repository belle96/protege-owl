package edu.stanford.smi.protegex.owl.emf;

import java.io.File;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface EditableEMFGeneratorOptions extends EMFGeneratorOptions {

    void setOutputFolder(File file);


    void setPackage(String value);
}
