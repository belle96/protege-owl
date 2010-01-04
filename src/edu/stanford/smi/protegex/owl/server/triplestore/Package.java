package edu.stanford.smi.protegex.owl.server.triplestore;

import java.io.Serializable;
import java.util.List;

import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Localizable;
import edu.stanford.smi.protege.server.narrowframestore.RemoteServerNarrowFrameStore;
import edu.stanford.smi.protege.util.LocalizeUtils;
import edu.stanford.smi.protegex.owl.model.NamespaceManager;

public class Package implements Localizable, Serializable {
    private static final long serialVersionUID = 6422510439112012894L;
    
    private List<RemoteServerNarrowFrameStore> frameStores;
    private List<NamespaceManager> namespaceManagers;
    private List<String> tripleStoreNames;
    private String activeTripleStore;
    private String systemTripleStore;
    
    public Package(List<RemoteServerNarrowFrameStore> frameStores,
                   List<NamespaceManager> namespaceManagers,
                   List<String>  tripleStoreNames,
                   String activeTripleStore,
                   String systemTripleStore) {
        this.frameStores = frameStores;
        this.namespaceManagers = namespaceManagers;
        this.tripleStoreNames = tripleStoreNames;
        this.activeTripleStore = activeTripleStore;
        this.systemTripleStore = systemTripleStore;
    }

    public List<RemoteServerNarrowFrameStore> getFrameStores() {
        return frameStores;
    }



    public List<NamespaceManager> getNamespaceManagers() {
        return namespaceManagers;
    }
    
    public List<String> getTripleStoreNames() {
        return tripleStoreNames;
    }
    
    public String getActiveTripleStore() {
        return activeTripleStore;
    }

    public String getSystemTripleStore() {
        return systemTripleStore;
    }

    public void localize(KnowledgeBase kb) {
        for (NamespaceManager ns : namespaceManagers) {
            LocalizeUtils.localize(ns, kb);
        }
    }
}
