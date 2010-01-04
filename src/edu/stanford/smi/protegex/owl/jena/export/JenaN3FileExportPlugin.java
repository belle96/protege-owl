package edu.stanford.smi.protegex.owl.jena.export;

import com.hp.hpl.jena.util.FileUtils;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class JenaN3FileExportPlugin extends AbstractJenaFileExportPlugin {

    public JenaN3FileExportPlugin() {
        super(FileUtils.langN3);
    }
}
