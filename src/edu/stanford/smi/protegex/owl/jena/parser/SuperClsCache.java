package edu.stanford.smi.protegex.owl.jena.parser;

import java.util.Collection;
import java.util.HashSet;

import edu.stanford.smi.protege.model.Cls;

public class SuperClsCache {

	private HashSet<Cls> clsesWithNoSuperClass = new HashSet<Cls>();


	public boolean hasFrameDeclaredSuperclass(Cls cls) {
		return !clsesWithNoSuperClass.contains(cls);
	}

	public void addFrame(Cls cls) {
		if (cls != null) {
			clsesWithNoSuperClass.add(cls);
		}
	}

	public void removeFrame(Cls cls) {
		if (cls != null) {
			clsesWithNoSuperClass.remove(cls);
		}
	}

	public void clearCache() {
		clsesWithNoSuperClass.clear();
	}

	public Collection<Cls> getCachedFramesWithNoSuperclass() {
		return clsesWithNoSuperClass;
	}

}
