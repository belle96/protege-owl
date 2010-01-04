package edu.stanford.smi.protegex.owl.ui.importstree.server;

import java.util.WeakHashMap;

import edu.stanford.smi.protege.exception.ProtegeException;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protege.server.framestore.RemoteClientFrameStore;
import edu.stanford.smi.protege.util.ApplicationProperties;
import edu.stanford.smi.protege.util.LocalizeUtils;
import edu.stanford.smi.protege.util.ProtegeJob;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLOntology;
import edu.stanford.smi.protegex.owl.model.impl.DefaultOWLOntology;
import edu.stanford.smi.protegex.owl.server.metaproject.OwlMetaProjectConstants;

public class CheckSetActiveImportAllowedJob extends ProtegeJob {
    private static final long serialVersionUID = 7234800355325524952L;
    
    private OWLOntology ontology;
    private static WeakHashMap<OWLOntology, Boolean> cachedResults = new WeakHashMap<OWLOntology, Boolean>();
    
    public static String ALLOW_MULTIUSER_SET_ACTIVE_ONTOLOGY = "allow.multiuser.client.set.active.ontology";

    public CheckSetActiveImportAllowedJob(OWLOntology ontology) {
        super(ontology.getOWLModel());
        this.ontology = ontology;
    }
    
    @Override
    public Boolean run() throws ProtegeException {
        if (getKnowledgeBase().getProject().isMultiUserServer() &&
                (!ApplicationProperties.getBooleanProperty(ALLOW_MULTIUSER_SET_ACTIVE_ONTOLOGY, false) ||
                        !serverSideCheckOperationAllowed(OwlMetaProjectConstants.SET_ACTIVE_IMPORT))) {
            return false;
        }
        return ((DefaultOWLOntology) ontology).isAssociatedTriplestoreEditable();
    }
    
    @Override
    public Boolean execute() throws ProtegeException {
        Boolean result = cachedResults.get(ontology);
        if (result == null) {
            result = (Boolean) super.execute();
            cachedResults.put(ontology, result);
        }
        return result;
    }
    
    @Override
    public void localize(KnowledgeBase kb) {
        super.localize(kb);
        if (ontology != null) {
            LocalizeUtils.localize(ontology, kb);
        }
    }

}
