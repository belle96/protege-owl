package edu.stanford.smi.protegex.owl.swrl.bridge;

import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLDataFactory;

public class DefaultConvertor implements Convertor {
	
	private SWRLRuleEngineBridge bridge;

	public DefaultConvertor(SWRLRuleEngineBridge bridge) { this.bridge = bridge; }
	
	public SWRLRuleEngineBridge getBridge() { return bridge; }
	public OWLDataFactory getOWLDataFactory() { return bridge.getOWLDataFactory(); }
	public OWLDataValueFactory getOWLDataValueFactory() { return bridge.getOWLDataValueFactory(); }
	
	public String uri2PrefixedName(String uri) { return  bridge.uri2PrefixedName(uri); }
	public String name2URI(String name) { return  bridge.name2URI(name); }
}
