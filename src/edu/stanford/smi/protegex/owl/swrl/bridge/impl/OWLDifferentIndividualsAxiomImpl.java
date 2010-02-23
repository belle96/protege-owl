
package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import edu.stanford.smi.protegex.owl.swrl.bridge.*;

import java.util.*;

public class OWLDifferentIndividualsAxiomImpl extends OWLNaryIndividualAxiomImpl implements OWLDifferentIndividualsAxiom
{
  public OWLDifferentIndividualsAxiomImpl(Set<OWLIndividual> individuals)
  {
    addIndividuals(individuals);
  } // OWLDifferentIndividualsAxiomImpl

  public OWLDifferentIndividualsAxiomImpl(OWLIndividual individual1, OWLIndividual individual2)
  {
    addIndividual(individual1);
    addIndividual(individual2);
  } // OWLDifferentIndividualsAxiomImpl

  public String toString() { return "differentFrom" + super.toString(); }  
} // OWLDifferentIndividualsAxiomImpl
