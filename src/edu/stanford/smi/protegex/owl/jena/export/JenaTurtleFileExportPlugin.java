package edu.stanford.smi.protegex.owl.jena.export;

import com.hp.hpl.jena.util.FileUtils;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class JenaTurtleFileExportPlugin extends AbstractJenaFileExportPlugin {

    public JenaTurtleFileExportPlugin() {
        super(FileUtils.langTurtle);
    }
}
