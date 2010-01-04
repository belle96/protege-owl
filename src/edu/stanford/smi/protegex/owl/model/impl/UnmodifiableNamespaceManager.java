package edu.stanford.smi.protegex.owl.model.impl;

import java.net.URI;
import java.util.Collection;

import edu.stanford.smi.protegex.owl.model.NamespaceManager;
import edu.stanford.smi.protegex.owl.model.NamespaceManagerListener;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.triplestore.TripleStore;

public class UnmodifiableNamespaceManager implements NamespaceManager {
    private NamespaceManager manager;
    
    public UnmodifiableNamespaceManager(NamespaceManager manager) {
        this.manager = manager;
    }

    public void addNamespaceManagerListener(NamespaceManagerListener listener) {
    }

    public void init(OWLModel owlModel) {
    }

    public boolean isModifiable(String prefix) {
        return false;
    }

    public void removeNamespaceManagerListener(NamespaceManagerListener listener) {
    }

    public void setModifiable(String prefix, boolean value) {
        throw new UnsupportedOperationException();
    }

    public String getDefaultNamespace() {
        return manager.getDefaultNamespace();
    }

    public String getNamespaceForPrefix(String prefix) {
        return manager.getNamespaceForPrefix(prefix);
    }

    public String getPrefix(String namespace) {
        return manager.getPrefix(namespace);
    }

    public Collection<String> getPrefixes() {
        return manager.getPrefixes();
    }

    public void removePrefix(String prefix) {
        throw new UnsupportedOperationException();
    }

    public void setDefaultNamespace(String value) {
        throw new UnsupportedOperationException();
    }

    public void setDefaultNamespace(URI uri) {
        throw new UnsupportedOperationException();
    }

    public void setPrefix(String namespace, String prefix) {
        throw new UnsupportedOperationException();
    }

    public void setPrefix(URI namespace, String prefix) {
        throw new UnsupportedOperationException();
    }

    public void addImport(TripleStore imported) {
    }

}
