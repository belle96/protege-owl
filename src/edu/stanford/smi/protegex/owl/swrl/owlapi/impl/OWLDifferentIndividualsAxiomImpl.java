
package edu.stanford.smi.protegex.owl.swrl.owlapi.impl;

import java.util.Set;

import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLDifferentIndividualsAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLNamedIndividual;

public class OWLDifferentIndividualsAxiomImpl extends OWLNaryIndividualAxiomImpl implements OWLDifferentIndividualsAxiom
{
  public OWLDifferentIndividualsAxiomImpl(Set<OWLNamedIndividual> individuals)
  {
    addIndividuals(individuals);
  } // OWLDifferentIndividualsAxiomImpl

  public OWLDifferentIndividualsAxiomImpl(OWLNamedIndividual individual1, OWLNamedIndividual individual2)
  {
    addIndividual(individual1);
    addIndividual(individual2);
  } // OWLDifferentIndividualsAxiomImpl

  public String toString() { return "differentFrom" + super.toString(); }  
} // OWLDifferentIndividualsAxiomImpl
