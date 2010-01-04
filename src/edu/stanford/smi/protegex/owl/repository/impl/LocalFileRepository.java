package edu.stanford.smi.protegex.owl.repository.impl;

import java.io.File;
import java.net.URI;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 21, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class LocalFileRepository extends AbstractLocalRepository {

    public LocalFileRepository(File f) {
        this(f, false);
    }


    public LocalFileRepository(File f, boolean forceReadOnly) {
        super(f, forceReadOnly);
        if (f.isDirectory()) {
            throw new IllegalArgumentException("The specified file must not be a directory.");
        }
        refresh();
    }


    public void refresh() {
        super.refresh();
        URI uri = processFile(getFile());
        if (uri != null) {
            putOntology(uri, getFile());
        }
    }


    public String getRepositoryDescription() {
        return "Local file " + getFile().getPath();
    }
}

