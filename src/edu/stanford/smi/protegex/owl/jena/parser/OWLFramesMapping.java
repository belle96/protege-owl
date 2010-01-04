package edu.stanford.smi.protegex.owl.jena.parser;

import java.util.HashMap;
import java.util.HashSet;

import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDFS;

import edu.stanford.smi.protege.model.Model;

public class OWLFramesMapping {
	private static HashMap<String, String> owlProp2FramesSlotMap = new HashMap<String, String>();


	static {
		owlProp2FramesSlotMap.put(RDFS.subClassOf.getURI(), Model.Slot.DIRECT_SUPERCLASSES);
		owlProp2FramesSlotMap.put(RDFS.subPropertyOf.getURI(), Model.Slot.DIRECT_SUPERSLOTS);
		owlProp2FramesSlotMap.put(RDFS.domain.getURI(), Model.Slot.DIRECT_DOMAIN);
		//what to do with range?

		owlProp2FramesSlotMap.put(OWL.maxCardinality.getURI(), Model.Slot.MAXIMUM_CARDINALITY);
		owlProp2FramesSlotMap.put(OWL.minCardinality.getURI(), Model.Slot.MINIMUM_CARDINALITY);
		//what to do with owl:Cardinality?
	}


	private static HashMap<String, String> owlProp2FramesInvSlotMap = new HashMap<String, String>();

	static {
		owlProp2FramesInvSlotMap.put(RDFS.subClassOf.getURI(), Model.Slot.DIRECT_SUBCLASSES);
		owlProp2FramesInvSlotMap.put(RDFS.subPropertyOf.getURI(), Model.Slot.DIRECT_SUBSLOTS);
		owlProp2FramesInvSlotMap.put(RDFS.domain.getURI(), Model.Slot.DIRECT_TEMPLATE_SLOTS);
	}

	private final static HashSet<String> restrictionPredicates = new HashSet<String>();

	static {
		restrictionPredicates.add(OWL.someValuesFrom.getURI());
		restrictionPredicates.add(OWL.allValuesFrom.getURI());
		restrictionPredicates.add(OWL.hasValue.getURI());
		restrictionPredicates.add(OWL.maxCardinality.getURI());
		restrictionPredicates.add(OWL.minCardinality.getURI());
		restrictionPredicates.add(OWL.cardinality.getURI());
	}

	private final static HashSet<String> logicalPredicates = new HashSet<String>();

	static {
		logicalPredicates.add(OWL.intersectionOf.getURI());
		logicalPredicates.add(OWL.unionOf.getURI());
		logicalPredicates.add(OWL.complementOf.getURI());
		//logicalPredicates.add(OWL.oneOf.getURI());
	}

	private final static HashSet<String> equivalentPropSlots = new HashSet<String>();

	static {
		equivalentPropSlots.add(Model.Slot.DIRECT_SUBCLASSES);
		equivalentPropSlots.add(Model.Slot.DIRECT_SUPERCLASSES);
	}


	public static String getFramesSlotMapName(String propertyName) {
		return owlProp2FramesSlotMap.get(propertyName);
	}


	public static String getFramesInvSlotMapName(String propertyName) {
		return owlProp2FramesInvSlotMap.get(propertyName);
	}

	public static HashSet<String> getLogicalPredicatesNames() {
		return logicalPredicates;
	}

	public static HashSet<String> getRestrictionPredicatesNames() {
		return restrictionPredicates;
	}

	public static HashSet<String> getEquivalentPropertySlots() {
		return equivalentPropSlots;
	}

}
