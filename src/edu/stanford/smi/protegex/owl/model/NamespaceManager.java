package edu.stanford.smi.protegex.owl.model;

import edu.stanford.smi.protegex.owl.model.triplestore.TripleStore;

/**
 * An interface for objects capable of mapping true URI namespaces into their
 * prefixed (e.g., "owl") and vice-versa.  Each OWLModel has exactly one NamespaceManager.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface NamespaceManager extends NamespaceMap {

    void addNamespaceManagerListener(NamespaceManagerListener listener);

    boolean isModifiable(String prefix);


    void removeNamespaceManagerListener(NamespaceManagerListener listener);


    void setModifiable(String prefix, boolean value);
    
    void addImport(TripleStore imported);
}
