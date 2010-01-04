package edu.stanford.smi.protegex.owl.jena.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.util.Disposable;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.triplestore.TripleStore;

public class GlobalParserCache implements Disposable {
	transient Logger log = Log.getLogger(GlobalParserCache.class);

	//We need to make sure that there is only one UndefTripleManager per owlModel
	private OWLModel owlModel;

	private HashMap<String, Collection<UndefTriple>> undefTriplesMap = new HashMap<String, Collection<UndefTriple>>();

	/*
	 * Global caches
	 */
	private MultipleTypesInstanceCache multipleTypesInstanceCache = new MultipleTypesInstanceCache();
	private Set<String> framesWithWrongJavaType = new HashSet<String>();
	private Set<TripleStore> parsedTripleStores = new HashSet<TripleStore>();
	private Set<UndefTriple> oneOfTriples = new HashSet<UndefTriple>();

	//the GCI caches will be refactored
	private Collection<RDFProperty> possibleGCIPredicates = new ArrayList<RDFProperty>();
	private Collection<RDFSClass> gciAxioms = new ArrayList<RDFSClass>();
	private Map<String, Cls> objectToNamedLogicalClassSurrogate = new HashMap<String, Cls>();

	private TripleProcessor tripleProcessor;


	public GlobalParserCache(OWLModel owlModel) {
		this.owlModel = owlModel;
		initGCIPredicates();
	}

	public TripleProcessor getTripleProcessor() {
	    if (tripleProcessor == null) {
	        tripleProcessor = new TripleProcessor(owlModel);
	    }
	    return tripleProcessor;
	}


	public void addUndefTriple(UndefTriple triple, String undef) {
		if (log.isLoggable(Level.FINE)) {
			log.fine(" +++ Adding: " + triple);
		}

		if (log.isLoggable(Level.FINE)) {
			if (undefTriplesMap.keySet().size() % 1000 == 0) {
				log.fine(" Undef triples count: " + undefTriplesMap.keySet().size());
			}
		}

		Collection<UndefTriple> undefTriples = getUndefTriples(undef);
		undefTriples.add(triple);
		undefTriplesMap.put(undef, undefTriples); //don't think it's necessary
	}

	public Collection<UndefTriple> getUndefTriples(String uri) {
		Collection<UndefTriple> undefTriples = undefTriplesMap.get(uri);

		if (undefTriples == null) {
			return new ArrayList<UndefTriple>();
		}

		return undefTriples;
	}

	public Set<String> getUndefTriplesKeys() {
		return undefTriplesMap.keySet();
	}

	public int getUndefTripleSize() {
		return undefTriplesMap.size();
	}

	public void removeUndefTriple(String uri, UndefTriple undefTriple) {
            if (log.isLoggable(Level.FINE)) {
            	log.fine(" --- Removing: " + undefTriple);
            }
            Collection<UndefTriple> undefTriples = getUndefTriples(uri);
            undefTriples.remove(undefTriple);

            if (undefTriples.isEmpty()) {
                undefTriplesMap.remove(uri);
            } else {
                undefTriplesMap.put(uri, undefTriples);
            }
	}

	public void removeUndefTripleKey(String uri) {
		undefTriplesMap.remove(uri);
	}

	public void dumpUndefTriples(Level level) {
	    if (!log.isLoggable(level)) {
	        return;
	    }

		log.log(level, "\n --------------- Begin undef triples dump ----------------");
		for (String string : undefTriplesMap.keySet()) {
			String uri = string;
			for (UndefTriple undefTriple : undefTriplesMap.get(uri)) {
				UndefTriple triple = undefTriple;
				log.log(level, " * " + triple);
			}
		}
		log.log(level, " --------------- End undef triples dump ----------------\n");
	}


	/*
	 * Cache methods
	 */


	public MultipleTypesInstanceCache getMultipleTypesInstanceCache() {
		return multipleTypesInstanceCache;
	}

	public Collection<RDFProperty> getPossibleGCIPredicates() {
		return possibleGCIPredicates;
	}

	public Collection<RDFSClass> getGciAxioms() {
		return gciAxioms;
	}

	public Map<String, Cls> getObjectToNamedLogicalClassSurrogate() {
		return objectToNamedLogicalClassSurrogate;
	}

	public Set<TripleStore> getParsedTripleStores() {
		return parsedTripleStores;
	}

	protected void initGCIPredicates() {
		possibleGCIPredicates.add(owlModel.getOWLDisjointWithProperty());
		possibleGCIPredicates.add(owlModel.getRDFSSubClassOfProperty());
		possibleGCIPredicates.add(owlModel.getOWLEquivalentClassProperty());
	}

	public Set<String> getFramesWithWrongJavaType() {
		return framesWithWrongJavaType;
	}

	public Set<UndefTriple> getOneOfTriples() {
		return oneOfTriples;
	}

	public void dispose() {
		multipleTypesInstanceCache.dispose();
		framesWithWrongJavaType.clear();
		parsedTripleStores.clear();
		oneOfTriples.clear();
		possibleGCIPredicates.clear();
		gciAxioms.clear();
		objectToNamedLogicalClassSurrogate.clear();
		undefTriplesMap.clear();
	}

}
