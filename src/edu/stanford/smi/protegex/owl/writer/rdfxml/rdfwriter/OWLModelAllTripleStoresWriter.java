package edu.stanford.smi.protegex.owl.writer.rdfxml.rdfwriter;

import edu.stanford.smi.protege.util.FileUtilities;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.triplestore.TripleStore;
import edu.stanford.smi.protegex.owl.repository.Repository;
import edu.stanford.smi.protegex.owl.repository.util.RepositoryFileManager;

import java.io.*;
import java.net.URI;
import java.util.Iterator;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Oct 8, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class OWLModelAllTripleStoresWriter {

    private OWLModel model;

    private URI uri;

    private boolean ordered;


    public OWLModelAllTripleStoresWriter(OWLModel model, URI mainFileURI, boolean ordered) {
        this.model = model;
        this.uri = mainFileURI;
        this.ordered = ordered;
    }


    public void write() throws Exception {
        Iterator ts = model.getTripleStoreModel().listUserTripleStores();        
        TripleStore mainTS = (TripleStore) ts.next();
        
        File file = new File(uri);
        FileOutputStream mainOS = new FileOutputStream(file);
        String encoding = FileUtilities.getWriteEncoding();
	    OutputStreamWriter osw = new OutputStreamWriter(mainOS, encoding);
        BufferedWriter bw = new BufferedWriter(osw);
        
        OWLModelWriter mainWriter = getOwlModelWriter(mainTS, bw);
	   // mainWriter.getXmlWriter().setEncoding(osw.getEncoding());
        mainWriter.getXmlWriter().setEncoding(encoding);
        mainWriter.write();
        bw.flush();
        bw.close();
        
        while (ts.hasNext()) {
            TripleStore tripleStore = (TripleStore) ts.next();
            String name = tripleStore.getName();
            URI ontologyName = new URI(name);
            Repository rep = model.getRepositoryManager().getRepository(ontologyName);
            if (rep != null && rep.isWritable(ontologyName) && rep.hasOutputStream(ontologyName)) {
                Log.getLogger().info("Saving import " + ontologyName + " to " + rep.getOntologyLocationDescription(ontologyName));
                OutputStream os = rep.getOutputStream(ontologyName);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(os, FileUtilities.getWriteEncoding());
                bw = new BufferedWriter(outputStreamWriter);
                OWLModelWriter omw = getOwlModelWriter(tripleStore, bw);
                omw.getXmlWriter().setEncoding(FileUtilities.getWriteEncoding());
                omw.write();
                bw.flush();
                bw.close();
            }
        }

        // Save the repositories as well
        RepositoryFileManager fm = new RepositoryFileManager(model);
        fm.saveGlobalRepositories();
        fm.saveProjectRepositories(uri);

        Log.getLogger().info("... saving successful.");
    }


    private OWLModelWriter getOwlModelWriter(TripleStore ts, Writer writer) {
        if (ordered) {
            return new OWLModelOrderedWriter(model, ts, writer);
        }
        else {
            return new OWLModelWriter(model, ts, writer);
        }
    }
}

