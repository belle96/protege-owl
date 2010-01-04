package edu.stanford.smi.protegex.owl.ui.importstree.server;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import edu.stanford.smi.protege.model.framestore.AbstractFrameStoreInvocationHandler;
import edu.stanford.smi.protege.model.framestore.FrameStore;
import edu.stanford.smi.protege.model.framestore.MergingNarrowFrameStore;
import edu.stanford.smi.protege.model.framestore.NarrowFrameStore;
import edu.stanford.smi.protege.model.query.Query;
import edu.stanford.smi.protege.model.query.QueryCallback;
import edu.stanford.smi.protege.server.RemoteSession;
import edu.stanford.smi.protege.server.framestore.ServerFrameStore;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protege.util.transaction.TransactionMonitor;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLOntology;
import edu.stanford.smi.protegex.owl.model.triplestore.TripleStore;
import edu.stanford.smi.protegex.owl.model.triplestore.TripleStoreModel;

public class ActiveOntologyFrameStoreHandler extends
        AbstractFrameStoreInvocationHandler {
    private static Logger log = Log.getLogger(ActiveOntologyFrameStoreHandler.class);
    private OWLModel owlModel;
    private Map<RemoteSession, NarrowFrameStore> activeFrameStoreMap = new HashMap<RemoteSession, NarrowFrameStore>();
    private NarrowFrameStore topFrameStore;
    private MergingNarrowFrameStore mnfs;
    
    public ActiveOntologyFrameStoreHandler(OWLModel owlModel) {
        this.owlModel = owlModel;
        topFrameStore = owlModel.getTripleStoreModel().getTopTripleStore().getNarrowFrameStore();
        mnfs = MergingNarrowFrameStore.get(owlModel);
    }
      
    public void setActiveOntology(OWLOntology ontology) {
        TransactionMonitor tm = getDelegate().getTransactionStatusMonitor();
        if (tm != null && tm.inTransaction()) {
            throw new IllegalStateException("Can't change the active ontology while in a transaction");
        }
        TripleStoreModel tripleStoreModel = owlModel.getTripleStoreModel();
        TripleStore tripleStore = tripleStoreModel.getHomeTripleStore(ontology);
        NarrowFrameStore activeFrameStore = tripleStore.getNarrowFrameStore();
        if (activeFrameStore.equals(topFrameStore)) {
            activeFrameStoreMap.remove(ServerFrameStore.getCurrentSession());
        }
        else {
            activeFrameStoreMap.put(ServerFrameStore.getCurrentSession(), activeFrameStore);
        }
    }
    
    private static Set<Method> transactionMethods = new HashSet<Method>();
    static {
        try {
            transactionMethods.add(FrameStore.class.getMethod("beginTransaction", new Class[] { String.class }));
            transactionMethods.add(FrameStore.class.getMethod("commitTransaction", new Class[] { }));
            transactionMethods.add(FrameStore.class.getMethod("rollbackTransaction", new Class[] { }));
        }
        catch (NoSuchMethodException nsme) {
            log.severe("Could not install active ontology configurtion on the server.");
            throw new RuntimeException(nsme);
        }
    };

    @Override
    protected void executeQuery(Query q, QueryCallback qc) {
        getDelegate().executeQuery(q, qc);
    }

    @Override
    protected Object handleInvoke(Method method, Object[] args) {
        NarrowFrameStore activeFrameStore = activeFrameStoreMap.get(ServerFrameStore.getCurrentSession());
        if (isQuery(method) || activeFrameStore == null) {
            return invoke(method, args);
        }
        else if (transactionMethods.contains(method)) {
            mnfs.setActiveFrameStore(topFrameStore);
            return invoke(method, args);
        }
        else {
            try {
                mnfs.setActiveFrameStore(activeFrameStore);
                return invoke(method, args);
            }
            finally {
                mnfs.setActiveFrameStore(topFrameStore);
            }
        }
    }

}
