package edu.stanford.smi.protegex.owl.jena.parser;

import java.util.HashMap;

import com.hp.hpl.jena.vocabulary.OWL;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Frame;
import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNames;
import edu.stanford.smi.protegex.owl.model.impl.DefaultOWLComplementClass;
import edu.stanford.smi.protegex.owl.model.impl.DefaultOWLIntersectionClass;
import edu.stanford.smi.protegex.owl.model.impl.DefaultOWLUnionClass;
import edu.stanford.smi.protegex.owl.model.impl.OWLSystemFrames;
import edu.stanford.smi.protegex.owl.model.triplestore.TripleStore;

public class LogicalClassCreatorUtility {


	private final static HashMap<String, String> logicalClassURI2MetaclassName = new HashMap<String, String>();

	//make the hashmap with objects
	static {
		logicalClassURI2MetaclassName.put(OWL.complementOf.getURI(), OWLNames.Cls.COMPLEMENT_CLASS);
		logicalClassURI2MetaclassName.put(OWL.intersectionOf.getURI(), OWLNames.Cls.INTERSECTION_CLASS);
		logicalClassURI2MetaclassName.put(OWL.unionOf.getURI(), OWLNames.Cls.UNION_CLASS);
		//OneOf - handled specially
		//logicalClassURI2MetaclassName.put(OWL.oneOf.getURI(), OWLNames.Cls.ENUMERATED_CLASS);
	}

	private final static HashMap<String, String> filler2SlotName = new HashMap<String, String>();

	//make the hashmap with objects
	static {
		filler2SlotName.put(OWL.complementOf.getURI(), OWLNames.Slot.COMPLEMENT_OF);
		filler2SlotName.put(OWL.intersectionOf.getURI(), OWLNames.Slot.INTERSECTION_OF);
		filler2SlotName.put(OWL.unionOf.getURI(), OWLNames.Slot.UNION_OF);
		//filler2SlotName.put(OWL.oneOf.getURI(), OWLNames.Slot.ONE_OF);
	}


	public static Frame createLogicalClass(OWLModel owlModel, FrameID id, String predUri, TripleStore ts) {
		Frame inst = ((KnowledgeBase) owlModel).getFrame(id);
		OWLSystemFrames systemFrames = owlModel.getSystemFrames();

		if (inst != null) {
			return inst;
		}

		if (predUri.equals(OWL.complementOf.getURI())) {
			inst = new DefaultOWLComplementClass(owlModel, id);
		} else if (predUri.equals(OWL.intersectionOf.getURI())) {
			inst = new DefaultOWLIntersectionClass(owlModel, id);
		} else 	if (predUri.equals(OWL.unionOf.getURI())) {
			inst = new DefaultOWLUnionClass(owlModel, id);
		}/* else if (predUri.equals(OWL.oneOf.getURI())) { //potential ambiguous case
			inst = new DefaultOWLEnumeratedClass(owlModel, id);
		}
		*/
		inst.assertFrameName();

		//if (!(inst instanceof DefaultOWLEnumeratedClass)) {
			FrameCreatorUtility.addOwnSlotValue(inst, systemFrames.getRdfTypeProperty(), systemFrames.getOwlNamedClassClass(), ts);
			// 	((RDFResource) inst).setPropertyValue(systemFrames.getRdfTypeProperty(), systemFrames.getOwlNamedClassClass());
			Cls metaCls = owlModel.getCls(logicalClassURI2MetaclassName.get(predUri));
			FrameCreatorUtility.addInstanceType((Instance)inst, metaCls, ts);
		//}

		return inst;
	}


	public static boolean addLogicalFiller(OWLModel owlModel, Frame logicalClass, Frame filler, String predUri, TripleStore ts) {
		if (logicalClass == null || filler == null) {
			return false;
		}

		Slot fillerSlot = owlModel.getSlot(filler2SlotName.get(predUri));

		if (fillerSlot == null) {
			return false;
		}

		FrameCreatorUtility.addOwnSlotValue(logicalClass, fillerSlot, filler, ts);

		return true;
	}

}
