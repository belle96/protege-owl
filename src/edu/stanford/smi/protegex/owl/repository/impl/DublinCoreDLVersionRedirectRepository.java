package edu.stanford.smi.protegex.owl.repository.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.jena.parser.ProtegeOWLParser;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Oct 4, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class DublinCoreDLVersionRedirectRepository extends AbstractStreamBasedRepositoryImpl {

    private URI dublinCoreOntologyURI;

    private URL redirectURL;

    public static final String DESCRIPTOR = "[Dublin Core DL Redirect]";

	public final static String DC = "http://purl.org/dc/elements/1.1/";

    public final static String DC_ALT = "http://protege.stanford.edu/plugins/owl/dc/protege-dc.owl";


    public DublinCoreDLVersionRedirectRepository() {
        try {
            dublinCoreOntologyURI = new URI(DC);
            redirectURL = new URL(DC_ALT);
        }
        catch (MalformedURLException e) {
          Log.getLogger().log(Level.SEVERE, "Exception caught", e);
        }
        catch (URISyntaxException e) {
          Log.getLogger().log(Level.SEVERE, "Exception caught", e);
        }
    }


    public boolean contains(URI ontologyName) {
        return dublinCoreOntologyURI.equals(ontologyName);
    }


    public void refresh() {

    }


    public Collection<URI> getOntologies() {
        return Collections.singleton(dublinCoreOntologyURI);
    }


    @Override
    public InputStream getInputStream(URI ontologyName)
            throws OntologyLoadException {
        return ProtegeOWLParser.getInputStream(redirectURL);
    }


    public boolean isWritable(URI ontologyName) {
        return false;
    }


    public OutputStream getOutputStream(URI ontologyName)
            throws IOException {
        return null;
    }


    public boolean isSystem() {
        return false;
    }


    public String getRepositoryDescription() {
        return "Dublin Core DL Version";
    }


    public String getOntologyLocationDescription(URI ontologyName) {
        return "Redirect to " + redirectURL.toString();
    }


    public String getRepositoryDescriptor() {
        return DESCRIPTOR;
    }


    @Override
    public int hashCode() {
        return redirectURL.hashCode();
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DublinCoreDLVersionRedirectRepository) {
            return true;
        }
        else {
            return false;
        }
    }
}

