package edu.stanford.smi.protegex.owl.swrl.owlapi;

import java.net.URI;

/**
 * Class that provides a very rough place holder for an OWLAPI IRI
 */
public class IRI 
{
	private String full;	
	
	public IRI(String uri) { full = uri; }
	public IRI(URI uri) { full = uri.toString();	}
  public String toString() { return full; }
}
