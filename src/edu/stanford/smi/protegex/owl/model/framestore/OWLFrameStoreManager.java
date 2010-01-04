package edu.stanford.smi.protegex.owl.model.framestore;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.smi.protege.model.framestore.DeleteSimplificationFrameStore;
import edu.stanford.smi.protege.model.framestore.FrameStore;
import edu.stanford.smi.protege.model.framestore.FrameStoreManager;
import edu.stanford.smi.protege.util.ApplicationProperties;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.impl.AbstractOWLModel;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class OWLFrameStoreManager extends FrameStoreManager {
	public static final String USE_PROTEGE_READ_ONLY_FRAME_STORE="use.protege.read.only.flag";
    
    private OWLModel  owlModel;
    
    private OWLFrameStore owlFrameStore;
    private DuplicateValuesFrameStore duplicateValuesFrameStore;
    private DomainUpdateFrameStore domainUpdateFrameStore;
    private FacetUpdateFrameStore facetUpdateFrameStore;
    private RangeUpdateFrameStore rangeUpdateFrameStore;
    private OwlSubclassFrameStore owlSubclassFrameStore;
    private TypeUpdateFrameStore typeUpdateFrameStore;
    private LocalClassificationFrameStore localClassificationFrameStore;
    private ProtegeReadOnlyFrameStore protegeReadOnlyFrameStore;
    
    private List<FrameStore> frameStores = new ArrayList<FrameStore>();

    public OWLFrameStoreManager(OWLModel owlModel) {
        super(owlModel);
        this.owlModel = owlModel;
        initializeOwlFrameStores();
    }
    
    private void initializeOwlFrameStores() {
    	// Core Owl Frame Stores
        addFrameStore(owlFrameStore = new OWLFrameStore((AbstractOWLModel) owlModel));
        addFrameStore(duplicateValuesFrameStore = new DuplicateValuesFrameStore());
        addFrameStore(domainUpdateFrameStore = new DomainUpdateFrameStore(owlModel));
        addFrameStore(facetUpdateFrameStore = new FacetUpdateFrameStore(owlModel));
        addFrameStore(rangeUpdateFrameStore = new RangeUpdateFrameStore(owlModel));
        addFrameStore(owlSubclassFrameStore = new OwlSubclassFrameStore(owlModel));
        addFrameStore(typeUpdateFrameStore = new TypeUpdateFrameStore(owlModel));  // this goes near the end so that the others see the swizzle.
        
        protegeReadOnlyFrameStore = new ProtegeReadOnlyFrameStore(owlModel);
        if (ApplicationProperties.getBooleanProperty(USE_PROTEGE_READ_ONLY_FRAME_STORE, false)) {
        	addFrameStore(protegeReadOnlyFrameStore);
        }
        
        for (FrameStore fs : frameStores) {
            insertFrameStore(fs);
        }
        int lastPostion = getFrameStores().size() - 1;
        insertFrameStore(localClassificationFrameStore = new LocalClassificationFrameStore(owlModel), 
        				 lastPostion);
    }
    
    /*
     * add the frame stores in reverse order
     */
    private void addFrameStore(FrameStore fs) {
        frameStores.add(0, fs);
    }
    

    @Override
    protected FrameStore create(Class clas) {
        if (clas == DeleteSimplificationFrameStore.class) {
            return new OWLDeleteSimplificationFrameStore();
        }
        else {
            return super.create(clas);
        }
    }
    
    public void setOwlFrameStoresEnabled(boolean enabled) {
        for (FrameStore fs : frameStores) {
            setEnabled(fs, enabled);
        }
    }
    
    
    /*
     * getters
     */
    
    public OWLFrameStore getOWLFrameStore() {
        return owlFrameStore;
    }
    
    public DuplicateValuesFrameStore getDuplicateValuesFrameStore() {
        return duplicateValuesFrameStore;
    }    

    public DomainUpdateFrameStore getDomainUpdateFrameStore() {
        return domainUpdateFrameStore;
    }

    public FacetUpdateFrameStore getFacetUpdateFrameStore() {
        return facetUpdateFrameStore;
    }
    
    public RangeUpdateFrameStore getRangeUpdateFrameStore() {
        return rangeUpdateFrameStore;
    }

    public OwlSubclassFrameStore getOwlSubclassFrameStore() {
        return owlSubclassFrameStore;
    }

    public TypeUpdateFrameStore getTypeUpdateFrameStore() {
        return typeUpdateFrameStore;
    }

    public LocalClassificationFrameStore getLocalClassificationFrameStore() {
        return localClassificationFrameStore;
    }
    
    public ProtegeReadOnlyFrameStore getProtegeReadOnlyFrameStore() {
		return protegeReadOnlyFrameStore;
	}
    
}
