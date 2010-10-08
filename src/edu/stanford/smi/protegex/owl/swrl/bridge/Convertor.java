package edu.stanford.smi.protegex.owl.swrl.bridge;

import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLDataFactory;

public interface Convertor 
{
	SWRLRuleEngineBridge getBridge();
	OWLDataFactory getOWLDataFactory();
	OWLDataValueFactory getOWLDataValueFactory();
	String uri2PrefixedName(String uri);
	String name2URI(String name);
}
