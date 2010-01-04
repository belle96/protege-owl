package edu.stanford.smi.protegex.owl.model.framestore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Frame;
import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.model.SimpleInstance;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.model.Transaction;
import edu.stanford.smi.protege.model.framestore.FrameStoreAdapter;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.model.OWLAnonymousClass;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLProperty;
import edu.stanford.smi.protegex.owl.model.OWLRestriction;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.model.factory.OWLFactoryClassType;
import edu.stanford.smi.protegex.owl.model.impl.AbstractOWLModel;
import edu.stanford.smi.protegex.owl.model.impl.OWLSystemFrames;

public class TypeUpdateFrameStore extends FrameStoreAdapter {

    private RDFSNamedClass untypedResource;
    private RDFSNamedClass untypedClass;
    private RDFSNamedClass untypedProperty;

    private RDFSNamedClass functionalPropertyClass;
    private RDFSNamedClass restrictionClass;
    private RDFSNamedClass owlClass;

    private RDFProperty rdfType;
    private RDFProperty rdfSubClassOfProperty;
    private RDFSNamedClass annotationPropertyClass;

    private Map<RDFProperty, RDFSNamedClass> fillerToProtegeTypeMap = new HashMap<RDFProperty, RDFSNamedClass>();

    private static Set<String> fakeProtege3FactoryTypes = new  HashSet<String>();
    static {
        for (OWLFactoryClassType factoryType  : OWLFactoryClassType.values()) {
            if (factoryType.isFakeProtege3Type()) {
                fakeProtege3FactoryTypes.add(factoryType.getTypeName());
            }
        }
    }


    public TypeUpdateFrameStore(OWLModel owlModel) {
        untypedResource = owlModel.getRDFUntypedResourcesClass();
        untypedClass = ((AbstractOWLModel) owlModel).getRDFExternalClassClass();
        untypedProperty = ((AbstractOWLModel)owlModel).getRDFExternalPropertyClass();

        functionalPropertyClass = owlModel.getOWLFunctionalPropertyClass();
        restrictionClass = owlModel.getSystemFrames().getOwlRestrictionClass();
        owlClass = owlModel.getOWLNamedClassClass();
        annotationPropertyClass = owlModel.getOWLAnnotationPropertyClass();

        rdfType = owlModel.getRDFTypeProperty();
        rdfSubClassOfProperty = owlModel.getRDFSSubClassOfProperty();

        initFillerMap(owlModel);
    }

    private void initFillerMap(OWLModel owlModel) {
        OWLSystemFrames systemFrames = owlModel.getSystemFrames();
        fillerToProtegeTypeMap.put(systemFrames.getOwlAllValuesFromProperty(), systemFrames.getOwlAllValuesFromClass());
        fillerToProtegeTypeMap.put(systemFrames.getOwlSomeValuesFromProperty(), systemFrames.getOwlSomeValuesFromClass());
        fillerToProtegeTypeMap.put(systemFrames.getOwlCardinalityProperty(), systemFrames.getOwlCardinalityClass());
        fillerToProtegeTypeMap.put(systemFrames.getOwlMinCardinalityProperty(), systemFrames.getOwlMinCardinalityClass());
        fillerToProtegeTypeMap.put(systemFrames.getOwlMaxCardinalityProperty(), systemFrames.getOwlMaxCardinalityClass());
        fillerToProtegeTypeMap.put(systemFrames.getOwlHasValueProperty(), systemFrames.getOwlHasValueClass());
    }

    /*
     * FrameStore implementations
     */


    @Override
    public Cls createCls(FrameID id, Collection directTypes, Collection directSuperclasses, boolean loadDefaults) {
        Cls cls = super.createCls(id, directTypes, directSuperclasses, loadDefaults);
        if (cls instanceof RDFSNamedClass) {
            ((RDFSNamedClass) cls).setPropertyValues(rdfSubClassOfProperty, directSuperclasses);
            if (!directTypes.contains(untypedClass)) {
            	super.setDirectOwnSlotValues(cls, rdfType, directTypes);
            }
        }
        else if (cls instanceof OWLRestriction) {
            super.setDirectOwnSlotValues(cls, rdfType, Collections.singleton(restrictionClass));
        }
        else if (cls instanceof OWLAnonymousClass) {
            super.setDirectOwnSlotValues(cls, rdfType, Collections.singleton(owlClass));
        }
        return cls;
    }

    @Override
    public Slot createSlot(FrameID id, Collection directTypes, Collection directSuperslots, boolean loadDefaults) {
        Slot slot = super.createSlot(id, directTypes, directSuperslots, loadDefaults);
        if (slot instanceof RDFProperty &&
        	 !directTypes.contains(untypedProperty)) {
            super.setDirectOwnSlotValues(slot, rdfType, directTypes);
        }
        return slot;
    }

    @Override
    public SimpleInstance createSimpleInstance(FrameID id, Collection directTypes, boolean loadDefaults) {
        SimpleInstance instance = super.createSimpleInstance(id, directTypes, loadDefaults);
        if (instance instanceof RDFResource &&
        		!directTypes.contains(untypedResource)) {
            super.setDirectOwnSlotValues(instance, rdfType, directTypes);
        }
        return instance;
    }


    @SuppressWarnings("unchecked")
    @Override
    public void addDirectType(Instance instance, Cls type) {
    	try {
        	beginTransaction("Add to " + instance.getBrowserText() + " direct type : " + type.getBrowserText()
        			+ Transaction.APPLY_TO_TRAILER_STRING + instance.getName());
            super.addDirectType(instance, type);
            instance = (Instance) super.getFrame(instance.getFrameID());
            if (instance instanceof RDFProperty) {
                if (type.equals(functionalPropertyClass)) {
                    ((Slot) instance).setAllowsMultipleValues(false);
                }
            }
            if (instance instanceof OWLRestriction) {
                super.setDirectOwnSlotValues(instance, rdfType, Collections.singleton(restrictionClass));
            }
            else if (instance instanceof OWLAnonymousClass) {
                super.setDirectOwnSlotValues(instance, rdfType, Collections.singleton(owlClass));
            }
            else if (instance instanceof RDFResource &&
                    !type.equals(untypedResource)) {
                Collection types = new ArrayList(super.getDirectOwnSlotValues(instance, rdfType));
                types.add(type);
                super.setDirectOwnSlotValues(instance, rdfType, types);
            }
            commitTransaction();
		} catch (Throwable e) {
			Log.getLogger().log(Level.WARNING, "Error in transaction at adding to " +
					instance + " direct type: " + type, e);
			rollbackTransaction();
			throw new RuntimeException(e);
		}
    }

    @SuppressWarnings("unchecked")
    @Override
    public void removeDirectType(Instance instance, Cls directType) {
    	try {
    		beginTransaction("Remove from " + instance.getBrowserText() + " direct type : " + directType.getBrowserText()
    				+ Transaction.APPLY_TO_TRAILER_STRING + instance.getName());
    		if (instance instanceof RDFProperty) {
    			if (directType.equals(functionalPropertyClass)) {
    				((Slot) instance).setAllowsMultipleValues(true);
    			}
    		}
    		if (instance instanceof OWLRestriction) {
    			super.setDirectOwnSlotValues(instance, rdfType, Collections.singleton(restrictionClass));
    		}
    		else if (instance instanceof OWLAnonymousClass) {
    			super.setDirectOwnSlotValues(instance, rdfType, Collections.singleton(owlClass));
    		}
    		else if (instance instanceof RDFResource) {
    			Collection types = new ArrayList(super.getDirectOwnSlotValues(instance, rdfType));
    			if (types.contains(directType)) {
    				types.remove(directType);
    				super.setDirectOwnSlotValues(instance, rdfType, types);
    			}
    		}
    		super.removeDirectType(instance, directType);
    		commitTransaction();
    	} catch (Throwable e) {
    		Log.getLogger().log(Level.WARNING, "Error in transaction at removing from " +
    				instance + " direct type: " + directType, e);
    		rollbackTransaction();
    		throw new RuntimeException(e);
    	}
    }

    @Override
    public void setDirectOwnSlotValues(Frame frame, Slot slot, Collection values) {
        super.setDirectOwnSlotValues(frame, slot, values);
        if (frame instanceof OWLAnonymousClass) {
            return;
        }
        Cls protegeType;
        if (frame instanceof RDFResource && slot.equals(rdfType)) {
            Collection directTypes = super.getDirectTypes((RDFResource) frame);
            for (Object newType : values) {
                if (!directTypes.contains(newType) && newType instanceof Cls) {
                    super.addDirectType((RDFResource) frame, (Cls) newType);
                }
            }
            for (Object oldType : directTypes) {
                if (!values.contains(oldType) && oldType instanceof Cls) {
                    super.removeDirectType((RDFResource) frame, (Cls) oldType);
                }
            }
        }
        else if ((protegeType = fillerToProtegeTypeMap.get(slot)) != null &&
                 !super.getDirectTypes((Instance) frame).contains(protegeType)) {
            super.addDirectType((Instance) frame, protegeType);
        }
    }

    @Override
    public void addDirectSuperslot(Slot slot, Slot superSlot) {
        super.addDirectSuperslot(slot, superSlot);
        if (slot instanceof RDFProperty) {
            RDFProperty property = (RDFProperty) slot;
            if (property instanceof OWLProperty && superSlot instanceof OWLProperty && ((OWLProperty) superSlot).isAnnotationProperty()) {
                addDirectType(slot, annotationPropertyClass);
            }
        }
    }
}
