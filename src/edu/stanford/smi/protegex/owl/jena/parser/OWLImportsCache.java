package edu.stanford.smi.protegex.owl.jena.parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class OWLImportsCache {

	public static HashMap<String, Set<String>> owlOntoloyToImportsMap = new HashMap<String, Set<String>>();
	public static HashMap<String, String> owlOntologyLocToOntologName = new HashMap<String, String>();

	public static Set<String> getOWLImportsURI(String owlOntologyURI) {
		Set<String> imports = owlOntoloyToImportsMap.get(owlOntologyURI);

		return imports == null ? new HashSet<String>() : imports;
	}

	public static void addOWLImport(String owlOntologyURI, String importURI) {
		Set<String> imports = getOWLImportsURI(owlOntologyURI);

		imports.add(importURI);
		owlOntoloyToImportsMap.put(owlOntologyURI, imports);
	}

	public static Set<String> getImportedOntologies() {
		HashSet<String> set = new HashSet<String>();
		set.addAll(owlOntologyLocToOntologName.keySet());
		set.addAll(owlOntologyLocToOntologName.values());
		set.addAll(owlOntoloyToImportsMap.keySet());
		return set;
	}

	public static Set<String> getAllOntologies() {
		HashSet<String> set = new HashSet<String>();
		set.addAll(owlOntologyLocToOntologName.keySet());
		set.addAll(owlOntologyLocToOntologName.values());
		set.addAll(owlOntoloyToImportsMap.keySet());
		for (String ont : owlOntoloyToImportsMap.keySet()) {
			set.addAll(owlOntoloyToImportsMap.get(ont));
		}
		return set;
	}

	public static boolean isImported(String ontology) {
 		return owlOntologyLocToOntologName.keySet().contains(ontology) ||
 			owlOntologyLocToOntologName.values().contains(ontology) ||
 			owlOntoloyToImportsMap.keySet().contains(ontology);
	}


	public static String getOntologyName(String ontologyLoc) {
		return owlOntologyLocToOntologName.get(ontologyLoc);
	}

	public static void setOntologyName(String ontologyLoc, String ontologyName) {
		owlOntologyLocToOntologName.put(ontologyLoc, ontologyName);
	}


	public static void dispose() {
		owlOntologyLocToOntologName.clear();
		owlOntoloyToImportsMap.clear();
	}

}
