package edu.stanford.smi.protegex.owl.ui.code;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.stanford.smi.protege.model.BrowserSlotPattern;
import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Frame;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protegex.owl.model.NamespaceUtil;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFIndividual;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSDatatype;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.model.classparser.OWLClassParseException;
import edu.stanford.smi.protegex.owl.model.classparser.ParserUtils;
import edu.stanford.smi.protegex.owl.model.impl.DefaultRDFSLiteral;
import edu.stanford.smi.protegex.owl.model.impl.OWLUtil;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLFactory;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class OWLResourceNameMatcher implements ResourceNameMatcher {

    public final static int SCALABLE_FRAME_COUNT = 1000;


    public String getInsertString(RDFResource resource) {
        return  resource.getBrowserText();
    }


    public Set<RDFResource> getMatchingResources(String prefix, String leftString, OWLModel owlModel) {
        boolean couldBeClass = couldBeClass(owlModel,prefix);
        boolean couldBeIndividual = couldBeIndividual(owlModel, prefix);
        boolean couldBeProperty = couldBeProperty(owlModel, prefix);
        boolean couldBeDatatype = couldBeDatatype(owlModel, prefix);
        
        if (prefix.startsWith(ParserUtils.SINGLE_QUOTE_STRING)) {
            prefix = prefix.substring(1);
        }
        Set<RDFResource> frames = new HashSet<RDFResource>();
        List<Class<? extends RDFResource>> possibleClasses = new ArrayList<Class<? extends RDFResource>>();
        
        if (couldBeClass) {
            addMatchingRDFSNamedClasses(prefix, frames, owlModel);
            possibleClasses.add(RDFSNamedClass.class);
        }
        if (couldBeIndividual) {
            addMatchingRDFIndividuals(prefix, frames, owlModel);
            possibleClasses.add(RDFIndividual.class);
        }
        if (couldBeProperty) {
            addMatchingRDFProperties(prefix, frames, owlModel);
            possibleClasses.add(RDFProperty.class);
        }
	    if(couldBeDatatype) {
		    getMatchingDatatypeNames(prefix, frames, owlModel);
		    possibleClasses.add(RDFSDatatype.class);
	    }
	    removeFilteredElements(frames, possibleClasses);
        return frames;
    }
    
    protected boolean couldBeClass(OWLModel owlModel, String prefix) {
        OWLClassParseException ex = OWLClassParseException.getRecentInstance();
        return ex.nextCouldBeClass || owlModel.getRDFResource(prefix) instanceof RDFSNamedClass || prefix.startsWith(ParserUtils.SINGLE_QUOTE_STRING);
    }
    
    protected boolean couldBeIndividual(OWLModel owlModel, String prefix) {
        OWLClassParseException ex = OWLClassParseException.getRecentInstance();
        return ex.nextCouldBeIndividual || owlModel.getRDFResource(prefix) != null || prefix.startsWith(ParserUtils.SINGLE_QUOTE_STRING);
    }
    
    protected boolean couldBeProperty(OWLModel owlModel, String prefix) {
        OWLClassParseException ex = OWLClassParseException.getRecentInstance();
        return ex.nextCouldBeProperty || owlModel.getRDFResource(prefix) instanceof RDFProperty || prefix.startsWith(ParserUtils.SINGLE_QUOTE_STRING);
    }
    
    protected boolean couldBeDatatype(OWLModel owlModel, String prefix) {
        OWLClassParseException ex = OWLClassParseException.getRecentInstance();
        return ex.nextCouldBeDatatypeName || owlModel.getRDFResource(prefix) instanceof RDFSDatatype;
    }


    public static void addMatchingRDFSNamedClasses(String prefix, Set<RDFResource> result, OWLModel owlModel) {
        if (prefix.length() == 0) {
            int count = owlModel.getRDFSClassCount();
            if (count < SCALABLE_FRAME_COUNT) {
                for (Iterator it = OWLUtil.getSelectableNamedClses(owlModel).iterator(); it.hasNext();) {
                    RDFSNamedClass aClass = (RDFSNamedClass) it.next();
                    if (aClass.isVisibleFromOWLThing()) {
                        result.add(aClass);
                    }
                }
            }
        }
        else {
            Collection<Slot> alreadySearchedSlots = new ArrayList<Slot>();
            addMatchingFrames(owlModel, result, owlModel.getRDFSNamedClassClass(), prefix, alreadySearchedSlots);
            addMatchingFrames(owlModel, result, owlModel.getOWLNamedClassClass(), prefix, alreadySearchedSlots);
        }
    }

    public static void addMatchingRDFProperties(String prefix, Set<RDFResource> result, OWLModel owlModel) {
        Collection<Slot> alreadySearchedSlots = new ArrayList<Slot>();
        addMatchingFrames(owlModel, result, owlModel.getRDFPropertyClass(), prefix, alreadySearchedSlots);
        addMatchingFrames(owlModel, result, owlModel.getOWLDatatypePropertyClass(), prefix, alreadySearchedSlots);
        addMatchingFrames(owlModel, result, owlModel.getOWLObjectPropertyClass(), prefix, alreadySearchedSlots);
    }




    public static void addMatchingRDFIndividuals(String prefix, Set<RDFResource> result, OWLModel owlModel) {
        Collection<Slot> alreadySearchedSlots = new ArrayList<Slot>();
        addMatchingFrames(owlModel, result, owlModel.getOWLThingClass(), prefix, alreadySearchedSlots);
    }

	public static void getMatchingDatatypeNames(String prefix, Set<RDFResource> result, OWLModel owlModel) {
		Collection matches = owlModel.getRDFSDatatypes();
        for (Iterator it = matches.iterator(); it.hasNext();) {
            RDFSDatatype datatype = (RDFSDatatype) it.next();
            if(datatype.isAnonymous() == false &&
               datatype.getBrowserText().startsWith(prefix)) {
	            result.add(datatype);
            }
        }
	}
    
	private static void addMatchingFrames(OWLModel owlModel,
	                                      Collection<RDFResource> frames, Cls type, String prefix, 
	                                      Collection<Slot> alreadySearchedSlots) {
	    Slot slot = getBrowserSlotForType(type);
	    if (!alreadySearchedSlots.contains(slot)) {
	        Collection<String> prefixesToTry = new ArrayList<String>();
	        prefixesToTry.add(prefix);
	        if (((KnowledgeBase) owlModel).getNameSlot().equals(slot))  {
	            String fullName = NamespaceUtil.getFullName(owlModel, prefix);
	            if (fullName != null) {
	                prefixesToTry.add(fullName);
	            }
	        }
	        String  lang = owlModel.getDefaultLanguage();
	        if (lang != null && slot instanceof RDFProperty 
	                && ((RDFProperty) slot).isAnnotationProperty()) {
	            prefixesToTry.add(DefaultRDFSLiteral.LANGUAGE_PREFIX + lang + DefaultRDFSLiteral.SEPARATOR + prefix);
	        }
	        for (String possiblePrefix : prefixesToTry)  {
	            Collection<Frame> newFrames = ((KnowledgeBase) owlModel).getMatchingFrames(slot, null, false, 
	                                                                                       possiblePrefix + "*", SCALABLE_FRAME_COUNT);
	            for (Frame frame : newFrames) {
	            	if (frame instanceof RDFResource) {
	            		frames.add((RDFResource) frame);
	            	}
	            }
	        }
	        alreadySearchedSlots.add(slot);
	    }  
	}
    
    public boolean isIdChar(char ch) {
        return SymbolTextField.isIdChar(ch);
    }

    private static Slot getBrowserSlotForType(Cls type) {
        BrowserSlotPattern bsp = type.getBrowserSlotPattern();
        List<Slot> slots = bsp.getSlots();
        if (slots.size() == 1) {
            return slots.get(0);
        }
        return type.getKnowledgeBase().getSystemFrames().getNameSlot();
    }
    
    
    protected void removeFilteredElements(Set<RDFResource> frames,
    		                                   List<Class<? extends RDFResource>> classes) {
    	Set<RDFResource> toRemove = new HashSet<RDFResource>();
        for (RDFResource resource  : frames) {        	
        	boolean remove = true;
        	if (!isFilteredOutResource(resource)) {
        		for (Class<? extends RDFResource> clazz : classes) {
        			if (clazz.isAssignableFrom(resource.getClass()) && isVisible(resource)) {
        				remove = false;
        				break;
        			}
        		}        	
        	}
        	if (remove) {
        		toRemove.add(resource);
        	}
        }
        frames.removeAll(toRemove);
    }
    
    protected boolean isFilteredOutResource(RDFResource resource) {
    	return resource.isAnonymous() || SWRLFactory.isSWRLFilteredResource(resource);
    }
    
    
    protected static boolean isVisible(Frame frame) {
        if (frame instanceof RDFSNamedClass) {
            return frame.isVisible() && 
                (isSlowProject(frame.getKnowledgeBase()) ||
                    ((RDFSNamedClass) frame).isVisibleFromOWLThing());
        }
        else return frame.isVisible();
    }
    
    private static boolean isSlowProject(KnowledgeBase kb) {
        return kb.getProject().isMultiUserClient();
    }
    
}
