package edu.stanford.smi.protegex.owl.repository.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.jena.parser.ProtegeOWLParser;

public class URLInputSource implements InputStreamSource {
    private URL url;
    
    public URLInputSource(URL url)  {
        this.url = url;
    }

    public InputStream getInputStream() throws IOException {
        try {
            return ProtegeOWLParser.getInputStream(url);
        }
        catch (OntologyLoadException ole) {
            IOException e = new IOException(ole.getMessage());
            e.initCause(ole);
            throw e;
        }
    }

    public URL getURL() {
        return url;
    }

}
