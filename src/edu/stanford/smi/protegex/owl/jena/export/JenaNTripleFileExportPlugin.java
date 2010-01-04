package edu.stanford.smi.protegex.owl.jena.export;

import com.hp.hpl.jena.util.FileUtils;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class JenaNTripleFileExportPlugin extends AbstractJenaFileExportPlugin {

    public JenaNTripleFileExportPlugin() {
        super(FileUtils.langNTriple);
    }
}
