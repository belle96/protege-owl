package edu.stanford.smi.protegex.owl.writer.xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Apr 8, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class XMLWriterNamespaceManager {

    private Map<String, String> prefixNamespaceMap;

    private Map<String, String> namespacePrefixMap;

    private String defaultNamespace;


    public XMLWriterNamespaceManager(String defaultNamespace) {
        prefixNamespaceMap = new HashMap<String, String>();
        namespacePrefixMap = new HashMap<String, String>();
        this.defaultNamespace = defaultNamespace;
    }


    public void setPrefix(String prefix, String namespace) {
        prefixNamespaceMap.put(prefix, namespace);
        namespacePrefixMap.put(namespace, prefix);
    }


    public String getPrefixForNamespace(String namespace) {
        return namespacePrefixMap.get(namespace);
    }


    public String getNamespaceForPrefix(String prefix) {
        return (String) prefixNamespaceMap.get(prefix);
    }


    public void createPrefixForNamespace(String namespace) {
        if (namespacePrefixMap.containsKey(namespace) == false) {
            int counter = 1;
            while (prefixNamespaceMap.get("p" + counter) != null) {
                counter++;
            }
            setPrefix("p" + counter, namespace);
        }
    }


    public String getDefaultNamespace() {
        return defaultNamespace;
    }


    public Collection<String> getPrefixes() {
        return new ArrayList<String>(prefixNamespaceMap.keySet());
    }


    public Collection<String> getNamespaces() {
        return new ArrayList<String>(namespacePrefixMap.keySet());
    }
}

