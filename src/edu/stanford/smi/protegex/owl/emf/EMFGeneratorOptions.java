package edu.stanford.smi.protegex.owl.emf;

import java.io.File;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface EMFGeneratorOptions {

    File getOutputFolder();


    String getPackage();
}
