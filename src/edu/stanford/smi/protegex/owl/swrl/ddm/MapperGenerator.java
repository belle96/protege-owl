
package edu.stanford.smi.protegex.owl.swrl.ddm;

public interface MapperGenerator
{
  void addMap(OWLClassMap classMap);
  void addMap(OWLObjectPropertyMap objectPropertyMap);
  void addMap(OWLDatatypePropertyMap datatypePropertyMap);
} // MapperGenerator
