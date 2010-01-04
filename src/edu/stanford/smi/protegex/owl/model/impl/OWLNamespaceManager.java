package edu.stanford.smi.protegex.owl.model.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Logger;

import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.model.OWLNames;
import edu.stanford.smi.protegex.owl.model.ProtegeNames;
import edu.stanford.smi.protegex.owl.model.RDFNames;
import edu.stanford.smi.protegex.owl.model.RDFSNames;
import edu.stanford.smi.protegex.owl.model.XSDNames;
import edu.stanford.smi.protegex.owl.model.XSPNames;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLNames;

public class OWLNamespaceManager extends AbstractNamespaceManager {
    private static final long serialVersionUID = -7101015781624857363L;

    private static final transient Logger log = Log.getLogger(OWLNamespaceManager.class);
    //TODO: The interface should be modified to throw exceptions

    //the 2 hashmaps should be kept in sync at all times
    private HashMap<String, String> prefix2namespaceMap = new HashMap<String, String>();
    private HashMap<String, String> namespace2prefixMap = new HashMap<String, String>();

    private Collection<String> unmodifiablePrefixes = new HashSet<String>();


    public OWLNamespaceManager() {
        setPrefix(OWLNames.OWL_NAMESPACE, OWLNames.OWL_PREFIX);
        setModifiable(OWLNames.OWL_PREFIX, false);

        setPrefix(RDFNames.RDF_NAMESPACE, RDFNames.RDF_PREFIX);
        setModifiable(RDFNames.RDF_PREFIX, false);

        setPrefix(RDFSNames.RDFS_NAMESPACE, RDFSNames.RDFS_PREFIX);
        setModifiable(RDFSNames.RDFS_PREFIX, false);

        setPrefix(XSDNames.XSD_NAMESPACE, RDFNames.XSD_PREFIX);
        setModifiable(RDFNames.XSD_PREFIX, false);

        setPrefix(XSPNames.NS, XSPNames.DEFAULT_PREFIX);
        setModifiable(XSPNames.DEFAULT_PREFIX, false);

        setPrefix(ProtegeNames.PROTEGE_OWL_NAMESPACE, ProtegeNames.PROTEGE_PREFIX);

        setPrefix(SWRLNames.SWRL_NAMESPACE, SWRLNames.SWRL_PREFIX);
        setModifiable(SWRLNames.SWRL_PREFIX, false);

        setPrefix(SWRLNames.SWRLB_NAMESPACE, SWRLNames.SWRLB_PREFIX);
        setModifiable(SWRLNames.SWRLB_PREFIX, false);

    }

    public boolean isModifiable(String prefix) {
        return !unmodifiablePrefixes.contains(prefix);
    }

    public void setModifiable(String prefix, boolean value) {
        if (value) {
            unmodifiablePrefixes.remove(prefix);
        }
        else {
            unmodifiablePrefixes.add(prefix);
        }
    }

    public String getNamespaceForPrefix(String prefix) {
        return prefix2namespaceMap.get(prefix);
    }

    public String getPrefix(String namespace) {
        return namespace2prefixMap.get(namespace);
    }

    public Collection<String> getPrefixes() {
        return prefix2namespaceMap.keySet();
    }



    public void removePrefix(String prefix) {
        String namespace = prefix2namespaceMap.get(prefix);
        if (namespace != null) {
            removePrefixMappingSimple(namespace, prefix);
            tellNamespaceChanged(prefix, namespace, null);
            tellPrefixChanged(namespace, prefix, null);
        }
    }

    public void setPrefix(String namespace, String prefix) {
        String existingNamespace = prefix2namespaceMap.get(prefix);
        String existingPrefix = namespace2prefixMap.get(namespace);

        if (existingNamespace != null && namespace.equals(existingNamespace)) {
            return;
        }
        //should throw exception
        if (unmodifiablePrefixes.contains(prefix)) {
            log.warning("Trying to set namespace to an unmodifiable prefix: " + prefix + " -> " + namespace);
            return;
        }
        if (unmodifiablePrefixes.contains(existingPrefix)) {
            log.warning("Trying to remove an unmodifiable prefix mapping: " + existingPrefix + " -> "  + namespace);
            return;
        }
        if (existingPrefix != null) {
            removePrefixMappingSimple(namespace, existingPrefix);
            tellNamespaceChanged(existingPrefix, namespace, null);
        }
        if (existingNamespace != null) {
            removePrefixMappingSimple(existingNamespace, prefix);
            tellPrefixChanged(existingNamespace, prefix, null);
        }

        addPrefixMappingSimple(namespace, prefix);
        tellPrefixChanged(namespace, existingPrefix, prefix);
        tellNamespaceChanged(prefix, existingNamespace, namespace);
        return;
    }

    public void removePrefixMappingSimple(String namespace, String prefix) {
        prefix2namespaceMap.remove(prefix);
        namespace2prefixMap.remove(namespace);
    }

    private void addPrefixMappingSimple(String namespace, String prefix) {
        prefix2namespaceMap.put(prefix, namespace);
        namespace2prefixMap.put(namespace, prefix);
    }




}
