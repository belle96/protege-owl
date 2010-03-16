package edu.stanford.smi.protegex.owl.swrl.owlapi;

import java.util.Map;
import java.util.Set;

public interface PrefixManager 
{
  String getDefaultPrefix();
  boolean containsPrefixMapping(String prefixName);
  String getPrefix(String prefixName);
  Map<String, String> getPrefixName2PrefixMap();
  IRI getIRI(String prefixIRI);
  String getPrefixIRI(IRI iri);
  Set<String> getPrefixNames();
}
