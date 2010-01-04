package edu.stanford.smi.protegex.owl.jena.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.util.Disposable;

public class MultipleTypesInstanceCache implements Disposable {

	//maybe it should be singleton?

	private HashMap<Instance, Collection<Cls>> instWithMultipleTypesMap = new HashMap<Instance, Collection<Cls>>();

	public void addType(Instance inst, Cls type) {
		Collection<Cls> types = getTypesForInstance(inst);
		types.add(type);
		instWithMultipleTypesMap.put(inst, types);
	}

	public Collection<Cls> getTypesForInstance(Instance inst) {
		Collection<Cls> types = instWithMultipleTypesMap.get(inst);
		if (types == null) {
			types = new ArrayList<Cls>();
		}

		return types;
	}

	public Set<Instance> getInstancesWithMultipleTypes() {
		return instWithMultipleTypesMap.keySet();
	}

	public Set<Cls> getTypesForInstanceAsSet(Instance inst) {
		return new HashSet<Cls>(getTypesForInstance(inst));
	}

	public void dispose() {
		instWithMultipleTypesMap.clear();
	}

}
