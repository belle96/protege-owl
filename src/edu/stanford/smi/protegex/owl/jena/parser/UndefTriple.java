package edu.stanford.smi.protegex.owl.jena.parser;


import com.hp.hpl.jena.rdf.arp.ALiteral;
import com.hp.hpl.jena.rdf.arp.AResource;

import edu.stanford.smi.protegex.owl.model.triplestore.TripleStore;

public class UndefTriple {

	private AResource tripleSubj;
	private AResource triplePred;
	private Object tripleObj;
	private TripleStore tripleStore;


	public UndefTriple(AResource subj, AResource pred, AResource obj, TripleStore ts) {
		this.tripleSubj = subj;
		this.triplePred = pred;
		this.tripleObj = obj;
		this.tripleStore = ts;
	}

	public UndefTriple(AResource subj, AResource pred, ALiteral obj, TripleStore ts) {
		this.tripleSubj = subj;
		this.triplePred = pred;
		this.tripleObj = obj;
		this.tripleStore = ts;
	}


	public Object getTripleObj() {
		return tripleObj;
	}

	public AResource getTriplePred() {
		return triplePred;
	}

	public AResource getTripleSubj() {
		return tripleSubj;
	}

	public TripleStore getTripleStore() {
		return tripleStore;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (!(obj instanceof UndefTriple)) {
			return false;
		}

		UndefTriple other = (UndefTriple) obj;

		return other.getTripleSubj().equals(getTripleSubj()) &&
		       other.getTriplePred().equals(getTriplePred()) &&
		       other.getTripleObj().equals(getTripleObj()) &&
		       other.getTripleStore().equals(getTripleStore());
	}

	@Override
	public int hashCode() {
		return 2*tripleSubj.hashCode() + 27*triplePred.hashCode() + tripleObj.hashCode();
	}


	@Override
	public String toString() {
		return "(" + getTripleSubj() + "  " + getTriplePred() + "  " + getTripleObj() + ") triplestore: " + getTripleStore();
	}

}
