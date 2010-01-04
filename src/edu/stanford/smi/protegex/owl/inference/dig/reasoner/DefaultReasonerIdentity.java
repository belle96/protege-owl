package edu.stanford.smi.protegex.owl.inference.dig.reasoner;

public class DefaultReasonerIdentity implements ReasonerIdentity {

	private String name;
	
	public DefaultReasonerIdentity() {
		this("Unknown Reasoner");
	}
	
	public DefaultReasonerIdentity(String name) {
		this.name = name;
	}
	
	public String getReasonerName() {
		return name;
	}

}
