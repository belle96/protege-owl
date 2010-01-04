package edu.stanford.smi.protegex.owl.model.triplestore;

import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;

/**
 * A TripleWriter that forwards all calls into a delegate TripleWriter.
 * This can be used to chain writers, e.g. to insert a filter in front of
 * a delegate.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DelegatingTripleWriter implements TripleWriter {

    private TripleWriter delegate;


    public DelegatingTripleWriter(TripleWriter delegate) {
        this.delegate = delegate;
    }


    public void addImport(String uri) {
        delegate.addImport(uri);
    }


    public void close() throws Exception {
        delegate.close();
    }


    public TripleWriter getDelegate() {
        return delegate;
    }


    public void init(String baseURI) {
        delegate.init(baseURI);
    }


    public void setDelegate(TripleWriter delegate) {
        this.delegate = delegate;
    }


    public void write(RDFResource resource, RDFProperty property, Object object) throws Exception {
        delegate.write(resource, property, object);
    }


    public void writePrefix(String prefix, String namespace) throws Exception {
        delegate.writePrefix(prefix, namespace);
    }
}
