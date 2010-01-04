package edu.stanford.smi.protegex.owl.model;

import java.net.URI;
import java.util.Collection;

/**
 * An interface for objects capable of mapping true URI namespaces into their
 * prefixed (e.g., "owl") and vice-versa.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface NamespaceMap {


    String getDefaultNamespace();


    String getNamespaceForPrefix(String prefix);


    String getPrefix(String namespace);


    Collection<String> getPrefixes();


    void removePrefix(String prefix);


    void setDefaultNamespace(String value);


    void setDefaultNamespace(URI uri);


    void setPrefix(String namespace, String prefix);


    void setPrefix(URI namespace, String prefix);
}
