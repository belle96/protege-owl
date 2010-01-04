package edu.stanford.smi.protegex.owl.repository.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.repository.util.FileInputSource;
import edu.stanford.smi.protegex.owl.repository.util.OntologyNameExtractor;
import edu.stanford.smi.protegex.owl.repository.util.RepositoryUtil;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 21, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public abstract class AbstractLocalRepository extends AbstractStreamBasedRepositoryImpl {

    private File file;

    private boolean forceReadOnly;

    private Map<URI, File> ontologies;


    public AbstractLocalRepository(File file, boolean forceReadOnly) {
        this.file = file;
        this.forceReadOnly = forceReadOnly;
        ontologies = new HashMap<URI, File>();
    }


    protected File getFile() {
        return file;
    }


    public void refresh() {
        ontologies = new HashMap<URI, File>();
    }


    protected void putOntology(URI ontologyName, File file) {
        ontologies.put(ontologyName, file);
    }


    public boolean contains(URI ontologyName) {
        return ontologies.keySet().contains(ontologyName);
    }


    public Collection<URI> getOntologies() {
        return Collections.unmodifiableCollection(ontologies.keySet());
    }


    @Override
    public InputStream getInputStream(URI ontologyName)
            throws OntologyLoadException {
        File f = ontologies.get(ontologyName);
        if (f != null) {
            try {
				return new FileInputStream(f);
			} catch (FileNotFoundException e) {
				throw new OntologyLoadException(e, "Could not open file: " + f);
			}
        }
        else {
            return null;
        }
    }


    public OutputStream getOutputStream(URI ontologyName)
            throws IOException {
        if (isWritable(ontologyName)) {
            File f = ontologies.get(ontologyName);
            return new FileOutputStream(f);
        }
        else {
            return null;
        }
    }


    public boolean isSystem() {
        return false;
    }


    public String getOntologyLocationDescription(URI ontologyName) {
        File f = ontologies.get(ontologyName);
        if (f != null) {
            return f.getAbsolutePath();
        }
        else {
            return "";
        }
    }


    public String getRepositoryDescriptor() {
        try {
            return file.toURI().toURL().toString() + "?" +
                    RepositoryUtil.FORCE_READ_ONLY_FLAG + "=" + Boolean.toString(forceReadOnly);
        }
        catch (MalformedURLException e) {
            return null;
        }
    }


    public boolean isWritable(URI ontologyName) {
        if (forceReadOnly == false) {
            File f = ontologies.get(ontologyName);
            if (f != null) {
                return f.canWrite();
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }


    public boolean isForceReadOnly() {
        return forceReadOnly;
    }


    public void setForceReadOnly(boolean forceReadOnly) {
        this.forceReadOnly = forceReadOnly;
    }


    protected URI processFile(File file) {
        URI ontologyName = null;
        try {
            PrintStream oldErr = System.err;
            System.setErr(new PrintStream(new OutputStream() {
                @Override
                public void write(int b)
                        throws IOException {
                }
            }));
            OntologyNameExtractor extractor = new OntologyNameExtractor(new FileInputSource(file));
            ontologyName = extractor.getOntologyName();
            System.setErr(oldErr);
        } catch (Exception e) {
          Log.emptyCatchBlock(e);
        }

        return ontologyName;
    }
}

